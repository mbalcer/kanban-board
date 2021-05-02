import {Component, OnInit} from '@angular/core';
import {Utils} from '../utils/utils';
import {TextConstants} from '../utils/text-constants';
import {Router} from '@angular/router';
import {AuthService} from '../auth-service/auth.service';
import {StudentService} from '../student/student.service';
import {Student} from '../student/student.model';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css',
    '../login/login.component.css']
})
export class RegistrationComponent implements OnInit {

  public firstName: string;
  public lastName: string;
  public indexNumber: string;
  public fullTime: boolean;
  public email: string;
  public password: string;
  public feedback: string;

  constructor(private router: Router,
              private authService: AuthService,
              private studentService: StudentService) {
  }

  ngOnInit(): void {
  }

  public register(): void {
    if (this.checkIfDataCorrect()) {
      const student = Student.register(this.firstName, this.lastName,
        this.indexNumber, this.fullTime, this.email, this.password);
      this.studentService.register(student).subscribe(
        () => {
          this.authService.authenticate(this.email, this.password)
            .subscribe(() => {
              this.router.navigate(['/home']);
            }, () => {
              this.router.navigate(['/login']);
            });
        },
        error => {
          if (error.status === 409) {
            this.feedback = TextConstants.REGISTRATION_DUPLICATED_EMAIL;
          } else {
            this.feedback = TextConstants.REGISTRATION_INVALID_DATA;
          }
        });
    }
  }

  private checkIfDataCorrect(): boolean {
    if (!Utils.checkIfStringNotEmpty(this.firstName)
      || !Utils.checkIfStringNotEmpty(this.lastName) || this.fullTime === undefined) {
      this.feedback = TextConstants.REGISTRATION_INCOMPLETE_DATA;
      return false;
    } else if (!Utils.checkIfIndexNumberCorrect(this.indexNumber)) {
      this.feedback = TextConstants.REGISTRATION_INVALID_INDEX_NUMBER;
      return false;
    } else if (!Utils.checkIfEmailCorrect(this.email)) {
      this.feedback = TextConstants.REGISTRATION_INVALID_EMAIL;
      return false;
    } else if (!Utils.checkIfPasswordCorrect(this.password)) {
      this.feedback = TextConstants.REGISTRATION_WEAK_PASSWORD;
      return false;
    }
    return true;
  }

}
