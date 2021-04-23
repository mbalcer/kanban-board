import {Component, OnInit} from '@angular/core';
import {Task} from './task';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Board} from './board';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  boards: Board[] = [];

  constructor() {
    this.initBoards();
    this.boards[0].tasks.push({id: 1, name: 'task', description: 'description', createDateTime: '2021-04-22'});
    this.boards[0].tasks.push({id: 2, name: 'new_task', description: 'description', createDateTime: '2021-04-21'});
    this.boards[0].tasks.push({id: 3, name: 'old_task', description: 'description', createDateTime: '2021-04-22'});
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
  drop(event: CdkDragDrop<Task[], any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data,
                        event.previousIndex, event.currentIndex);
    }
  }


}
