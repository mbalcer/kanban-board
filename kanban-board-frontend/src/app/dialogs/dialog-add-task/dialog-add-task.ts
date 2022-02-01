import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, Validators} from '@angular/forms';
import {DialogAction} from '../dialog-action';
import {Student} from '../../auth/student/student.model';
import {Task} from 'src/app/tasks/task';
import {AddEditAction} from '../../tasks/tasks.component';

@Component({
  selector: 'app-dialog-task-details',
  templateUrl: 'dialog-add-task.html',
  styleUrls: ['./dialog-add-task.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogAddTask {
  formResult: DialogAction = {
    action: 'add',
    data: null
  };

  formControl = {
    name: new FormControl('', [Validators.required]),
    sequence: new FormControl('', [Validators.required, Validators.min(0)])
  };

  options: Student[] = [];

  taskToAdd: Task = {
    name: '',
    description: '',
    sequence: null
  };

  constructor(public dialogRef: MatDialogRef<DialogAddTask>,
              @Inject(MAT_DIALOG_DATA) public data: AddEditAction) {
    this.formResult.action = data.action;
    // TODO fix
    //  this.options = data.students;
    if (this.formResult.action === 'edit') {
      this.taskToAdd = data.task;
    }
  }

  public objectComparisonFunction(option, value): boolean {
    return option.studentId === value.studentId;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  closeForm(): void {
    this.formResult.data = this.taskToAdd;
    this.dialogRef.close(this.formResult);
  }
}
