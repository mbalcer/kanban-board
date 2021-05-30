import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Project} from './project';
import {Student} from '../../auth/student/student.model';
import {AuthService} from '../../auth/auth-service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private PROJECT_URL = environment.backendUrl + '/api/project';
  private HEADERS = this.authService.getAuthHeaders();

  constructor(private httpClient: HttpClient,
              private authService: AuthService) {
  }

  getProjectById(id: number): Observable<Project> {
    return this.httpClient.get<Project>(this.PROJECT_URL + '/' + id,
      {headers: this.HEADERS});
  }

  getAllProjectsByUser(user: Student): Observable<Project[]> {
    return this.httpClient.get<Project[]>(this.PROJECT_URL + '/user/' + user.email,
      {headers: this.HEADERS});
  }

  createProject(project: Project): Observable<Project> {
    return this.httpClient.post<Project>(this.PROJECT_URL, project,
      {headers: this.HEADERS});
  }

  addStudentToProject(project: Project, user: Student): Observable<Project> {
    return this.httpClient.put<Project>(this.PROJECT_URL + '/student/' + project.projectId, user,
      {headers: this.HEADERS});
  }

  updateProject(project: Project): Observable<Project> {
    return this.httpClient.put<Project>(this.PROJECT_URL + '/' + project.projectId, project,
      {headers: this.HEADERS});
  }

  deleteProject(project: Project): Observable<any> {
    return this.httpClient.delete<any>(this.PROJECT_URL + '/' + project.projectId,
      {headers: this.HEADERS});
  }

}
