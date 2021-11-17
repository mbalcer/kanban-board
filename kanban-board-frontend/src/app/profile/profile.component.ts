import {Component, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';
import {StudentService} from '../auth/student/student.service';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {NotificationService} from '../notification.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: Student;
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;

  readonly indexPattern = '^[0-9]+$';
  readonly passwordPattern = '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$';

  profileControls = this.fb.group({
    firstName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    indexNumber: new FormControl('', [Validators.required, Validators.pattern(this.indexPattern)]),
    fullTime: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  passwordControls = this.fb.group({
    currentPassword: new FormControl('', [Validators.required, Validators.pattern(this.passwordPattern)]),
    newPassword: new FormControl('', [Validators.required, Validators.pattern(this.passwordPattern)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.pattern(this.passwordPattern)])
  });

  constructor(private studentService: StudentService,
              private notificationService: NotificationService, private fb: FormBuilder) {
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

  changePassword(): void {
    if (this.newPassword === this.confirmPassword) {
      this.studentService.changePassword(this.user, this.currentPassword, this.newPassword)
        .subscribe(result => {
          this.user = result;
          this.notificationService.success('Zmiana hasła przebiegła pomyślnie');
        }, error => this.notificationService.error(error.error.message));
    } else {
      this.notificationService.error('Nowe hasło nie zostało poprawnie potwierdzone');
    }
  }
}
