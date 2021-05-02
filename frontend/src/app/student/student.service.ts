import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../auth-service/auth.service';
import {Student} from './student.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private STUDENT_URL = environment.backendUrl + '/api/student';

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  public register(student: Student): Observable<Student> {
    return this.http.post<Student>(this.STUDENT_URL, student);
  }

}