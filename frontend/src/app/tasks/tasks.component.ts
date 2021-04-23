import {Component, OnInit} from '@angular/core';
import {Task} from './task';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  todo: Task[] = [];
  done: Task[] = [];

  constructor() {
    this.todo.push({id: 1, name: 'task', description: 'description', createDateTime: '2021-04-22'});
    this.todo.push({id: 2, name: 'new_task', description: 'description', createDateTime: '2021-04-21'});
    this.done.push({id: 3, name: 'old_task', description: 'description', createDateTime: '2021-04-22'});
  }

  ngOnInit(): void {
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
