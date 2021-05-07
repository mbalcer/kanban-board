import {Component, Input, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() user: Student;

  constructor() { }

  ngOnInit(): void {
  }

}
