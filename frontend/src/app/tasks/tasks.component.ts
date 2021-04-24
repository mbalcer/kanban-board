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
    this.boards.push({name: 'Do zrobienia', tasks: []});
    this.boards.push({name: 'W trakcie', tasks: []});
    this.boards.push({name: 'Testowanie', tasks: []});
    this.boards.push({name: 'Zrobione', tasks: []});
  }

  // tslint:disable-next-line:typedef
  getTasks() {
    this.taskService.getTasksByProject(1).subscribe(result => {
      this.boards[0].tasks = result;
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
