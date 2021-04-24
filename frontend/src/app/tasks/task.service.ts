import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from './task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private TASK_URL = environment.backendUrl + '/api/task';

  constructor(private httpClient: HttpClient) { }

  getTasksByProject(projectId: number): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.TASK_URL + '/project/' + projectId);
  }
}
