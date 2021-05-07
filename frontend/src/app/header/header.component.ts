import {Component, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  user: Student = new Student(1, 'Jan', 'Kowalski', '111111', true, 'jankow@wp.pl', '');

  constructor() { }

  ngOnInit(): void {
  }

}
