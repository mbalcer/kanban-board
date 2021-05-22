import {Component, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';
import {StudentService} from '../auth/student/student.service';
import {FormControl, Validators} from '@angular/forms';
import {NotificationService} from '../notification.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: Student;

  formControl = {
    firstName: new FormControl('', [Validators.required, Validators.min(3)]),
    lastName: new FormControl('', [Validators.required, Validators.min(3)]),
    indexNumber: new FormControl('', [Validators.required]),
    fullTime: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email])
  };


  constructor(private studentService: StudentService, private notificationService: NotificationService) {
    this.getUser();
  }

  ngOnInit(): void {
  }

  getUser(): void {
    this.studentService.getLoggedUser().subscribe(result => {
      this.user = result;
    }, error => console.log(error));
  }

  changeProfile(): void {
    this.studentService.updateStudent(this.user).subscribe(result => {
      this.user = result;
      this.notificationService.success('Zmiana danych przebiegła pomyślnie');
    }, error => this.notificationService.error(error.error.message));
  }
}
