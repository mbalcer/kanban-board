import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {
  errors: Error[] = [];
  activeError: Error;

  constructor(private route: ActivatedRoute) {
    this.initErrors();
    const activePath = this.route.snapshot.routeConfig.path;
    this.activeError = this.errors.find(error => error.path === activePath);
  }

  initErrors(): void {
    this.errors.push({status: 404, path: 'not-found', reason: 'Looks like the page you were looking for is no longer here'});
    this.errors.push({status: 403, path: 'forbidden', reason: 'You do not have access to this resource'});
  }

  ngOnInit(): void {
  }

}

export interface Error {
  status: number;
  path: string;
  reason: string;
}
