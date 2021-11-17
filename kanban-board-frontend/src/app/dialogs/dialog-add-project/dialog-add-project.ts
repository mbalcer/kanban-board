import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogAction} from '../dialog-action';
import {Project} from '../../home/projects/project';
import {AddEditAction} from '../../home/projects/projects.component';
import {FormBuilder, FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'app-dialog-add-project',
  templateUrl: 'dialog-add-project.html',
  styleUrls: ['./dialog-add-project.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogAddProject {

  formResult: DialogAction = {
    action: 'add',
    data: null
  };

  formControl = this.fb.group({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', []),
    submitDateTime: new FormControl('', [])
  });

  projectToAdd: Project = {
    name: '',
    description: '',
    submitDateTime: null
  };

  constructor(public dialogRef: MatDialogRef<DialogAddProject>,
              @Inject(MAT_DIALOG_DATA) public data: AddEditAction, private fb: FormBuilder) {
    this.formResult.action = data.action;
    if (this.formResult.action === 'edit') {
      data.project.submitDateTime = this.dateTimeFormatter(data.project.submitDateTime);
      this.projectToAdd = data.project;
    }
  }

  dateTimeFormatter(dateTime: string): string {
    return dateTime === null ? dateTime : dateTime.slice(0, 16);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  closeForm(): void {
    this.formResult.data = this.projectToAdd;
    this.dialogRef.close(this.formResult);
  }

}
