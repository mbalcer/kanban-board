import {Component, Input, OnChanges, OnDestroy, OnInit} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {environment} from '../../../environments/environment';
import {Project} from '../../home/projects/project';
import {Student} from '../../auth/student/student.model';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnChanges, OnDestroy {
  private serverUrl = environment.backendUrl + '/chat';
  private stompClient;

  message: string;
  initWebSocket = false;

  @Input() project: Project;
  @Input() user: Student;

  constructor() {

  }

  ngOnInit(): void {}

  ngOnChanges(): void {
    if (this.project && !this.initWebSocket) {
      this.webSocketConnect(this.project.projectId);
      this.initWebSocket = true;
    }
  }

  ngOnDestroy(): void {
    this.webSocketDisconnect(this.project.projectId);
  }

  webSocketConnect(projectId): void {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, frame => {
      this.stompClient.subscribe('/project/' + projectId, message => {
        console.log(message);
      });
    });
  }

  webSocketDisconnect(projectId): void {
    this.stompClient.disconnect('/project/' + projectId);
  }

  sendMessage(): void {
    const messageToSend = {
      student: this.user,
      message: this.message,
    };
    this.stompClient.send('/app/chat/' + this.project.projectId, {}, JSON.stringify(messageToSend));
    this.message = '';
  }
}
