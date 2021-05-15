import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Task} from '../task';
import {FormControl, Validators} from '@angular/forms';
import {Student} from '../../auth/student/student.model';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-dialog-task-details',
  templateUrl: 'dialog-add-task.html',
  styleUrls: ['./dialog-add-task.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogAddTask {
  formControl = {
    name: new FormControl('', [Validators.required]),
    sequence: new FormControl('', [Validators.required, Validators.min(0)]),
    student: new FormControl('')
  };

  options: Student[] = [];

  taskToAdd: Task = {
    name: '',
    description: '',
    sequence: null
  };

  constructor(public dialogRef: MatDialogRef<DialogAddTask>,
              @Inject(MAT_DIALOG_DATA) public data: Observable<Student[]>) {
    this.options = data[0];
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
