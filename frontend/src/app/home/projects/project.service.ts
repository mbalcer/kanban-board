import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Project} from './project';
import {Student} from '../../auth/student/student.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private PROJECT_URL = environment.backendUrl + '/api/project';

  constructor(private httpClient: HttpClient) { }

  getProjectById(id: number): Observable<Project> {
    return this.httpClient.get<Project>(this.PROJECT_URL + '/' + id);
  }

  getAllProjectsByUser(user: Student): Observable<Project[]> {
    return this.httpClient.get<Project[]>(this.PROJECT_URL + '/user/' + user.email);
  }
}
