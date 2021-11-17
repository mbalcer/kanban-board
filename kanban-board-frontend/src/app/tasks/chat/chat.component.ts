import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {environment} from '../../../environments/environment';
import {Project} from '../../home/projects/project';
import {Student} from '../../auth/student/student.model';
import {Message} from './message';
import {NotificationService} from '../../notification.service';
import {ChatService} from './chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnChanges, OnDestroy {
  private serverUrl = environment.backendUrl + '/chat';
  private stompClient;

  message: string;
  messages: Message[] = [];
  initWebSocket = false;

  @Input() project: Project;
  @Input() user: Student;
  @Input() openChat: boolean;
  @Output() newMessage: EventEmitter<boolean> = new EventEmitter();

  constructor(private notificationService: NotificationService, private chatService: ChatService) {}

  ngOnInit(): void {}

  ngOnChanges(): void {
    if (this.project && !this.initWebSocket) {
      this.webSocketConnect(this.project.projectId);
      this.getHistoryOfMessages();
      this.initWebSocket = true;
    }
  }

  ngOnDestroy(): void {
    if (this.initWebSocket) {
      this.webSocketDisconnect(this.project.projectId);
    }
  }

  getHistoryOfMessages(): void {
    this.chatService.getHistory(this.project.projectId).subscribe(result => {
      if (result != null) {
        this.messages = result;
      }
    });
  }

  webSocketConnect(projectId): void {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = false;
    this.stompClient.connect({}, frame => {
      this.stompClient.subscribe('/project/' + projectId, message => {
        if (!this.openChat) {
          this.newMessage.emit(true);
        }
        const newMessage = JSON.parse(message.body);
        this.messages.push(newMessage);
      });
    });
  }

  webSocketDisconnect(projectId): void {
    this.stompClient.disconnect('/project/' + projectId);
  }

  sendMessage(): void {
    if (!this.message) {
      this.notificationService.warn('Wpisz wiadomość');
    } else {
      const messageToSend = {
        student: this.user,
        message: this.message,
      };
      this.stompClient.send('/app/chat/' + this.project.projectId, {}, JSON.stringify(messageToSend));
      this.message = '';
    }
  }
}
