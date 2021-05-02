import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {TasksComponent} from './tasks/tasks.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {DialogTaskDetails} from './tasks/dialog-task-details/dialog-task-details';
import {MaterialModule} from './material-module';

@NgModule({
  declarations: [
    AppComponent,
    TasksComponent,
    DialogTaskDetails
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    DragDropModule,
    HttpClientModule,
    FormsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [DialogTaskDetails]
})
export class AppModule { }
