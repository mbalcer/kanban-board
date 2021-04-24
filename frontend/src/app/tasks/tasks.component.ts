import {Component, OnInit} from '@angular/core';
import {Task} from './task';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Board} from './board';
import {TaskService} from './task.service';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  boards: Board[] = [];

  constructor(private taskService: TaskService) {
    this.initBoards();
    this.getTasks();
  }

  ngOnInit(): void {
  }

  // tslint:disable-next-line:typedef
  initBoards() {
    this.boards.push({name: 'TODO', value: 'Do zrobienia', tasks: []});
    this.boards.push({name: 'IN_PROGRESS', value: 'W trakcie', tasks: []});
    this.boards.push({name: 'TESTING', value: 'Testowanie', tasks: []});
    this.boards.push({name: 'DONE', value: 'Zrobione', tasks: []});
  }

  // tslint:disable-next-line:typedef
  getTasks() {
    this.taskService.getTasksByProject(1).subscribe(result => { // TODO: change projectId from parameter
      this.boards.forEach(board => {
        board.tasks = result.filter(r => r.state === board.name);
      });
    }, error => console.log(error));
  }

  // tslint:disable-next-line:typedef
  drop(event: CdkDragDrop<Task[], any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data,
                        event.previousIndex, event.currentIndex);
    }
  }

}
