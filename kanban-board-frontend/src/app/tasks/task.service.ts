import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from './task';
import {AuthService} from '../auth/auth-service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private TASK_URL = environment.backendUrl + '/api/task';
  private HEADERS = this.authService.getAuthHeaders();

  constructor(private httpClient: HttpClient,
              private authService: AuthService) {
  }

  getTasksByProject(projectId: string): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.TASK_URL + '/project/' + projectId,
      {headers: this.HEADERS});
  }

  createTask(task: Task): Observable<Task> {
    return this.httpClient.post<Task>(this.TASK_URL, task,
      {headers: this.HEADERS});
  }

  updateTask(task: Task): Observable<Task> {
    task.project = null;
    return this.httpClient.put<Task>(this.TASK_URL + '/' + task.taskId, task,
      {headers: this.HEADERS});
  }

  deleteTask(task: Task): Observable<any> {
    return this.httpClient.delete<any>(this.TASK_URL + '/' + task.taskId,
      {headers: this.HEADERS});
  }

}
