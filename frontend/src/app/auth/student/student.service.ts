import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../auth-service/auth.service';
import {Student} from './student.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private STUDENT_URL = environment.backendUrl + '/api/student';
  private HEADERS = this.authService.getAuthHeaders();

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  public register(student: Student): Observable<Student> {
    return this.http.post<Student>(this.STUDENT_URL, student);
  }

  public getLoggedUser(): Observable<Student> {
    const email = this.authService.getUserEmail();
    return this.http.get<Student>(this.STUDENT_URL + '/email/' + email,
      {headers: this.HEADERS});
  }

  public getAll(): Observable<Student[]> {
    return this.http.get<Student[]>(this.STUDENT_URL + '/all',
      {headers: this.HEADERS});
  }

  public updateStudent(updateStudent: Student): Observable<Student> {
    return this.http.put<Student>(this.STUDENT_URL + '/' + updateStudent.studentId, updateStudent,
      {headers: this.HEADERS});
  }

  public changePassword(updateStudent: Student, currentPassword: string, newPassword: string): Observable<Student> {
    return this.http.put<Student>(this.STUDENT_URL + '/password/' + updateStudent.studentId,
      {currentPassword, newPassword}, {headers: this.HEADERS});
  }

}
