import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogAction} from '../dialog-action';
import {Project} from '../../home/projects/project';

@Component({
  selector: 'app-dialog-project-details',
  templateUrl: 'dialog-project-details.html',
  styleUrls: ['./dialog-project-details.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogProjectDetails {
  editAction: DialogAction;
  deleteAction: DialogAction;

  constructor(
    public dialogRef: MatDialogRef<DialogProjectDetails>,
    @Inject(MAT_DIALOG_DATA) public data: Project) {
    this.editAction = {action: 'edit', data};
    this.deleteAction = {action: 'delete', data};
  }

  closeDialog(action?): void {
    this.dialogRef.close(action);
  }

  delete(): void {
    if (confirm('Jesteś pewien że chcesz usunąć projekt "' + this.deleteAction.data.name + '"?')) {
      this.closeDialog(this.deleteAction);
    }
  }
}
