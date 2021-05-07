import {Component, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';
import {StudentService} from '../auth/student/student.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  user: Student;

  constructor(private studentService: StudentService) {
    this.getUser();
  }

  ngOnInit(): void {
  }

  getUser(): void {
    this.studentService.getLoggedUser().subscribe(result => {
      this.user = result;
    }, error => console.log(error));
  }
}
