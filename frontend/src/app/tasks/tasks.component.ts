import {Component, OnInit} from '@angular/core';
import {Task} from './task';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Board} from './board';
import {TaskService} from './task.service';
import {MatDialog} from '@angular/material/dialog';
import {Student} from '../auth/student/student.model';
import {StudentService} from '../auth/student/student.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Project} from '../home/projects/project';
import {ProjectService} from '../home/projects/project.service';
import {DialogAddTask} from './dialogs/dialog-add-task/dialog-add-task';
import {DialogTaskDetails} from './dialogs/dialog-task-details/dialog-task-details';
import {NotificationService} from '../notification.service';
import {MatDrawer} from '@angular/material/sidenav';
import {environment} from '../../environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  private serverUrl = environment.backendUrl + '/chat';
  private stompClient;

  user: Student;
  project: Project;
  boards: Board[] = [];
  chatToggle = false;
  newMessageNotification = 0;

  constructor(private studentService: StudentService, private taskService: TaskService, private projectService: ProjectService,
              private dialog: MatDialog, private route: ActivatedRoute, private router: Router, private notification: NotificationService) {
    const projectId = this.route.snapshot.paramMap.get('projectId');
    this.initBoards();
    this.getProject(Number(projectId));
    this.getUser();
  }

  ngOnInit(): void {
  }

  initBoards(): void {
    this.boards.push({name: 'TODO', value: 'Do zrobienia', tasks: []});
    this.boards.push({name: 'IN_PROGRESS', value: 'W trakcie', tasks: []});
    this.boards.push({name: 'TESTING', value: 'Testowanie', tasks: []});
    this.boards.push({name: 'DONE', value: 'Zrobione', tasks: []});
  }

  getUser(): void {
    this.studentService.getLoggedUser().subscribe(result => {
      this.user = result;
    }, error => this.notification.error(error.error.message));
  }

  getProject(id: number): void {
    this.projectService.getProjectById(id).subscribe(result => {
      this.project = result;
      this.boards.forEach(board => {
        board.tasks = result.tasks.filter(r => r.state === board.name).sort((a, b) => a.sequence - b.sequence);
      });
      this.taskObserver(this.project.tasks);
    }, error => {
      if (error.status === 404) {
        this.router.navigate(['/not-found']);
      } else {
        console.log(error);
      }
    });
  }

  drop(event: CdkDragDrop<Task[], any>, board: Board): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
      const taskToEdit = event.container.data[event.currentIndex];
      taskToEdit.state = board.name;
      this.taskService.updateTask(taskToEdit).subscribe(result => {
        event.container.data[event.currentIndex] = result;
        this.notification.success('Zadanie "' + result.name + '" zostało przesunięte do listy "' + board.value + '".');
      }, error => this.notification.error(error.error.message));
    }
  }

  openTaskDetails(task: Task): void {
    const dialogRef = this.dialog.open(DialogTaskDetails, {
      width: '50%',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined && result.action === 'delete') {
        this.deleteTask(result.data);
      } else if (result !== undefined && result.action === 'edit') {
        this.editTask(result.data);
      }
    });
  }

  openAddTask(action, task?): void {
    const addEditAction: AddEditAction = {action, students: this.project.students, task};
    const dialogRef = this.dialog.open(DialogAddTask, {
      width: '50%',
      data: addEditAction
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        result.data.project = this.project;
        result.data.project.tasks = [];

        if (result.action === 'add') {
          this.taskService.createTask(result.data).subscribe(createResult => {
            this.boards[0].tasks.push(createResult);
            this.notification.success('Pomyślnie dodano zadanie');
          }, error => this.notification.error(error.error.message));
        } else if (result.action === 'edit') {
          this.taskService.updateTask(result.data).subscribe(updateResult => {
            this.boards.forEach(board => {
              const indexTask = board.tasks.findIndex(t => t.taskId === updateResult.taskId);
              board.tasks[indexTask] = updateResult;
            });
            this.notification.success('Pomyślnie edytowano zadanie');
          }, error => this.notification.error(error.error.message));
        }
      }
    });
  }

  deleteTask(task: Task): void {
    this.taskService.deleteTask(task).subscribe(result => {
      const indexBoard = this.boards.findIndex(board => board.name === task.state);
      const indexTask = this.boards[indexBoard].tasks.findIndex(t => t.taskId === task.taskId);
      this.boards[indexBoard].tasks.splice(indexTask, 1);
      this.notification.success('Pomyślnie usunąłeś zadanie');
    }, error => this.notification.error(error.error.message));
  }

  editTask(task: Task): void {
    this.openAddTask('edit', task);
  }

  toggleChat(drawer: MatDrawer): void {
    drawer.toggle();
    this.chatToggle = !this.chatToggle;
    if (this.chatToggle) {
      this.newMessageNotification = 0;
    }
  }

  addNewMessageNotification(event: boolean): void {
    if (event) {
      this.newMessageNotification++;
    }
  }

  taskObserver(tasks: Task[]): void {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = false;
    const that = this;
    this.stompClient.connect({}, frame => {
      tasks.forEach(task => {
        this.stompClient.subscribe('/task/' + task.taskId, payload => {
          const receivedTask = JSON.parse(payload.body);
          const indexNewBoard = that.boards.findIndex(board => board.name === receivedTask.state);
          that.notification.success('Zadanie "' + receivedTask.name + '" zostało przesunięte do listy "'
            + that.boards[indexNewBoard].value + '".');
          that.boards.forEach((board, index) => {
            const indexTask = board.tasks.findIndex(t => t.taskId === receivedTask.taskId);
            if (indexTask !== -1) {
              that.boards[indexNewBoard].tasks.push(receivedTask);
              that.boards[index].tasks.splice(indexTask, 1);
            }
          });
        });
      });
    });
  }
}

export interface AddEditAction {
  action: string;
  task: Task;
  students: Student[];
}
