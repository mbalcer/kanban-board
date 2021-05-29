import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {DialogAction} from '../dialog-action';
import {Student} from '../../auth/student/student.model';
import {AddStudentAction} from '../../home/projects/projects.component';

@Component({
  selector: 'app-dialog-add-project-student',
  templateUrl: 'dialog-add-project-student.html',
  styleUrls: ['./dialog-add-project-student.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogAddProjectStudent {

  formResult: DialogAction = {
    action: 'add',
    data: null
  };

  formControl = this.fb.group({
    student: new FormControl('', [Validators.required]),
  });

  options: Student[] = [];
  studentToAdd: Student;

  constructor(public dialogRef: MatDialogRef<DialogAddProjectStudent>,
              @Inject(MAT_DIALOG_DATA) public data: AddStudentAction, private fb: FormBuilder) {
    this.options = data.students;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  closeForm(): void {
    this.formResult.data = this.studentToAdd;
    this.dialogRef.close(this.formResult);
  }

}
