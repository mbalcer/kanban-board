import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Task} from '../../task';
import {DialogAction} from '../dialog-action';

@Component({
  selector: 'app-dialog-task-details',
  templateUrl: 'dialog-task-details.html',
  styleUrls: ['./dialog-task-details.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogTaskDetails {
  editAction: DialogAction;
  deleteAction: DialogAction;

  constructor(
    public dialogRef: MatDialogRef<DialogTaskDetails>,
    @Inject(MAT_DIALOG_DATA) public data: Task) {
    this.editAction = {action: 'edit', data};
    this.deleteAction = {action: 'delete', data};
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
