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
import {NotificationService} from '../notification.service';
import {MatDrawer} from '@angular/material/sidenav';
import {environment} from '../../environments/environment';
import {DialogTaskDetails} from '../dialogs/dialog-task-details/dialog-task-details';
import {DialogAddTask} from '../dialogs/dialog-add-task/dialog-add-task';
import {webSocket} from 'rxjs/webSocket';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  user: Student;
  project: Project;
  boards: Board[] = [];
  chatToggle = false;
  newMessageNotification = 0;
  studentInTask: Map<Task, Student> = new Map<Task, Student>();
  studentsInProject: Student[] = [];

  constructor(private studentService: StudentService, private taskService: TaskService, private projectService: ProjectService,
              private dialog: MatDialog, private route: ActivatedRoute, private router: Router, private notification: NotificationService) {
    this.initBoards();
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
      this.getProject();
    }, error => this.notification.error(error.error.message));
  }

  getProject(): void {
    const projectId = this.route.snapshot.paramMap.get('projectId');
    this.projectService.getProjectById(projectId).subscribe(result => {
      if (result.students.find(studentId => studentId === this.user.studentId) === undefined) {
        this.router.navigate(['/forbidden']);
      }
      this.project = result;
      this.taskService.getTasksByProject(result.projectId).subscribe(tasksResult => {
        this.boards.forEach(board => {
          board.tasks = tasksResult.filter(t => t.state === board.name).sort((a, b) => a.sequence - b.sequence);
        });
        tasksResult.forEach(task => {
          this.studentService.getStudentById(task.student).subscribe(studentResult => {
            this.studentInTask.set(task, studentResult);
          }, error => {
            this.studentInTask.set(task, null);
          });
        });
      });

      this.project.students.forEach(studentId => {
        this.studentService.getStudentById(studentId).subscribe(studentResult => {
          this.studentsInProject.push(studentResult);
        });
      });
      this.taskObserver(this.project);
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
        this.notification.success('Zadanie "' + result.name + '" zostało przesunięte do listy "' + board.value + '".');
      }, error => this.notification.error(error.error.message));
    }
  }

  openTaskDetails(task: Task): void {
    const dialogRef = this.dialog.open(DialogTaskDetails, {
      width: '50%',
      data: new Map<Task, Student>().set(task, this.studentInTask.get(task))
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined && result.action === 'delete') {
        this.deleteTask(result.data);
      } else if (result !== undefined && result.action === 'edit') {
        this.editTask(result.data);
      }
    });
  }

  openAddTask(action, taskMap?: Map<Task, Student>): void {
    const addEditAction: AddEditAction = {action, students: this.studentsInProject, task: taskMap};
    const dialogRef = this.dialog.open(DialogAddTask, {
      width: '50%',
      data: addEditAction
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        result.data.project = this.project.projectId;

        if (result.action === 'add') {
          this.taskService.createTask(result.data).subscribe(createResult => {
            this.notification.success('Pomyślnie dodano zadanie');
          }, error => this.notification.error(error.error.message));
        } else if (result.action === 'edit') {
          this.taskService.updateTask(result.data).subscribe(updateResult => {
            this.notification.success('Pomyślnie edytowano zadanie');
          }, error => this.notification.error(error.error.message));
        }
      } else {
        this.getProject();
      }
    });
  }

  deleteTask(task: Map<Task, Student>): void {
    this.taskService.deleteTask(task.keys().next().value).subscribe(result => {
      this.notification.success('Pomyślnie usunąłeś zadanie');
    }, error => this.notification.error(error.error.message));
  }

  editTask(task: Map<Task, Student>): void {
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

  taskObserver(project: Project): void {
    project.tasks.forEach(taskId => {
      this.subscribeTask(taskId);
    });

    const ws = webSocket(environment.webSocketUrl + '/newTask/' + project.projectId);
    ws.subscribe(payload => {
      const newTask = payload as Task;
      this.boards[0].tasks.push(newTask);
      this.studentService.getStudentById(newTask.student).subscribe(result => {
        this.studentInTask.set(newTask, result);
      }, error => {
        this.studentInTask.set(newTask, null);
      });
      this.notification.success('Dodano nowe zadanie');
      this.subscribeTask(newTask.taskId);
    });
  }

  subscribeTask(taskId: string): void {
    const ws = webSocket(environment.webSocketUrl + '/task/' + taskId);
    ws.subscribe(payload => {
      const taskEvent = payload as TaskEditedEvent;
      if (taskEvent.action === 'deleted') {
        this.websocketDeleteTask(taskId);
      } else {
        this.websocketEditTask(taskEvent.source);
      }
    });
  }

  websocketEditTask(receivedTask: Task): void {
    const indexNewBoard = this.boards.findIndex(board => board.name === receivedTask.state);
    let newTask = true;
    this.boards.forEach((board, index) => {
      const indexTask = board.tasks.findIndex(t => t.taskId === receivedTask.taskId);
      if (indexTask !== -1) {
        if (index === indexNewBoard) {
          this.notification.success('Zadanie "' + receivedTask.name + '" zostało edytowane');
        } else {
          this.notification.success('Zadanie "' + receivedTask.name + '" zostało przesunięte do listy "'
            + this.boards[indexNewBoard].value + '".');
        }

        newTask = false;
        this.boards[indexNewBoard].tasks.push(receivedTask);
        this.boards[index].tasks.splice(indexTask, 1);
      }
    });

    if (newTask) {
      this.boards[indexNewBoard].tasks.push(receivedTask);
    }
  }

  websocketDeleteTask(taskId: string): void {
    this.boards.forEach((board, index) => {
      const indexTask = board.tasks.findIndex(t => t.taskId === taskId);
      if (indexTask !== -1) {
        this.boards[index].tasks.splice(indexTask, 1);
        this.notification.success('Zadanie "' + taskId + '" zostało usunięte');
      }
    });
  }
}

export interface AddEditAction {
  action: string;
  task: Map<Task, Student>;
  students: Student[];
}

export interface TaskEditedEvent {
  source: Task;
  timestamp: number;
  action: string;
}
