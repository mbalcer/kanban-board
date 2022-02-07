import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../auth/auth-service/auth.service";
import {Observable} from "rxjs";
import {Project} from "../home/projects/project";
import {FlowRegister} from "./flow-register";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private REGISTER_URL = environment.backendUrl + '/api/register';
  private HEADERS = this.authService.getAuthHeaders();

  constructor(private httpClient: HttpClient,
              private authService: AuthService) {
  }

  getAllRegisters(): Observable<FlowRegister[]> {
    return this.httpClient.get<FlowRegister[]>(this.REGISTER_URL + '/all',
      {headers: this.HEADERS});
  }

  getAllByProject(project: Project): Observable<FlowRegister[]> {
    return this.httpClient.get<FlowRegister[]>(this.REGISTER_URL + '/project/' + project.projectId,
      {headers: this.HEADERS});
  }

  getAllByDate(date: Date): Observable<FlowRegister[]> {
    return this.httpClient.get<FlowRegister[]>(this.REGISTER_URL + '/date/' + date.toUTCString(),
      {headers: this.HEADERS});
  }

  getRegisterById(id: string): Observable<FlowRegister> {
    return this.httpClient.get<FlowRegister>(this.REGISTER_URL + '/' + id,
      {headers: this.HEADERS});
  }

  getRegisterByProjectIdAndDate(project: Project, date: Date): Observable<FlowRegister> {
    return this.httpClient.get<FlowRegister>(this.REGISTER_URL + '/project-date/' + project.projectId + '/' + date.toUTCString(),
      {headers: this.HEADERS});
  }

  updateByProject(project: Project): Observable<Project> {
    return this.httpClient.get<Project>(this.REGISTER_URL + '/update/' + project.projectId,
      {headers: this.HEADERS});
  }

}
