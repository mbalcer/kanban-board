import {Component, Input, OnInit} from '@angular/core';
import {Student} from '../auth/student/student.model';
import {AuthService} from '../auth/auth-service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() user: Student;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  signOut(): void {
    this.authService.clearItems();
    this.router.navigate(['/logout']);
  }
}
