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
import {NotFoundComponent} from './not-found/not-found.component';
import {DialogAddTask} from './tasks/dialogs/dialog-add-task/dialog-add-task';
import {DialogTaskDetails} from './tasks/dialogs/dialog-task-details/dialog-task-details';

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
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    DragDropModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthService,
    AuthGuardService,
    CryptoJsService,
    StudentService
  ],
  bootstrap: [AppComponent],
  entryComponents: [DialogTaskDetails, DialogAddTask]
})
export class AppModule {
}
