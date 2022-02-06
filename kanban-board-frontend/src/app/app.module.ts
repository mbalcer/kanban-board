import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {TasksComponent} from './tasks/tasks.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LoginComponent} from './login/login.component';
import {RegistrationComponent} from './registration/registration.component';
import {AppRoutingModule} from './app-routing.module';
import {MaterialModule} from './material-module';
import {InitialsPipe} from './tasks/pipes/initials.pipe';
import {StudentNamePipe} from './tasks/pipes/student-name.pipe';
import {HomeComponent} from './home/home.component';
import {AuthService} from './auth/auth-service/auth.service';
import {AuthGuardService} from './auth/auth-guard/auth-guard.service';
import {CryptoJsService} from './auth/crypto-js-service/crypto-js.service';
import {StudentService} from './auth/student/student.service';
import {HeaderComponent} from './header/header.component';
import {ProjectsComponent} from './home/projects/projects.component';
import {ErrorPageComponent} from './error-page/error-page.component';
import {DialogAddTask} from './dialogs/dialog-add-task/dialog-add-task';
import {DialogTaskDetails} from './dialogs/dialog-task-details/dialog-task-details';
import {ChatComponent} from './tasks/chat/chat.component';
import {ProfileComponent} from './profile/profile.component';
import {DialogAddProjectStudent} from './dialogs/dialog-add-project-student/dialog-add-project-student';
import {DialogProjectDetails} from './dialogs/dialog-project-details/dialog-project-details';
import {DialogAddProject} from './dialogs/dialog-add-project/dialog-add-project';
import {DialogFlowChart} from "./dialogs/dialog-flow-chart/dialog-flow-chart";
import {NgxChartsModule} from "@swimlane/ngx-charts";

@NgModule({
  declarations: [
    AppComponent,
    TasksComponent,
    LoginComponent,
    RegistrationComponent,
    DialogTaskDetails,
    DialogAddTask,
    InitialsPipe,
    StudentNamePipe,
    HomeComponent,
    HeaderComponent,
    ProjectsComponent,
    ErrorPageComponent,
    ChatComponent,
    ProfileComponent,
    DialogAddProjectStudent,
    DialogProjectDetails,
    DialogAddProject,
    DialogFlowChart
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    DragDropModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    NgxChartsModule
  ],
  providers: [
    AuthService,
    AuthGuardService,
    CryptoJsService,
    StudentService
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    DialogTaskDetails,
    DialogAddTask,
    DialogAddProjectStudent,
    DialogProjectDetails,
    DialogAddProject,
    DialogFlowChart
  ]
})
export class AppModule {
}
