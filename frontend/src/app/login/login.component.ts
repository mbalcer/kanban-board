import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Utils} from '../utils/utils';
import {AuthService} from '../auth-service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public email = '';
  public password = '';

  constructor(private router: Router,
              private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  public login(): void {
    if (Utils.checkIfStringNotEmpty(this.email) && Utils.checkIfStringNotEmpty(this.password)) {
      this.authService.authenticate(this.email, this.password)
        .subscribe(() => {
          this.router.navigate(['/home']);
        }, () => {
          this.clearFields();
        });
    }
  }

  private clearFields(): void {
    this.email = '';
    this.password = '';
  }

}
