import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Project} from '../../home/projects/project';
import {Student} from '../../auth/student/student.model';
import {Message} from './message';
import {NotificationService} from '../../notification.service';
import {ChatService} from './chat.service';
import {webSocket, WebSocketSubject} from 'rxjs/webSocket';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnChanges, OnDestroy {

  webSocketSubject: WebSocketSubject<any> = null;
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
    // TODO: fix chat
    // if (this.project && !this.initWebSocket) {
    //   this.webSocketConnect(this.project.projectId);
      // this.getHistoryOfMessages();
      // this.initWebSocket = true;
    // }
  }

  ngOnDestroy(): void {
    // if (this.initWebSocket) {
    //   this.webSocketDisconnect(this.project.projectId);
    // }
  }

  // getHistoryOfMessages(): void {
  //   this.chatService.getHistory(this.project.projectId).subscribe(result => {
  //     if (result != null) {
  //       this.messages = result;
  //     }
  //   });
  // }

  webSocketConnect(projectId): void {
    this.webSocketSubject = webSocket(environment.webSocketUrl + '/chat/' + projectId);
    this.webSocketSubject.subscribe(payload => {
      if (!this.openChat) {
        this.newMessage.emit(true);
      }
      const newMessage = payload as Message;
      this.messages.push(newMessage);
    });
  }

  webSocketDisconnect(projectId): void {
    this.webSocketSubject.unsubscribe();
  }

  sendMessage(): void {
    if (!this.message) {
      this.notificationService.warn('Wpisz wiadomość');
    } else {
      const messageToSend = {
        student: this.user,
        message: this.message,
      };
      this.webSocketSubject.next(JSON.stringify(messageToSend));
      this.message = '';
    }
  }
}
