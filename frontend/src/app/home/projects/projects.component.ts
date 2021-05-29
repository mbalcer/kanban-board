import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Student} from '../../auth/student/student.model';
import {ProjectService} from './project.service';
import {Project} from './project';
import {MatDialog} from '@angular/material/dialog';
import {DialogAddProjectStudent} from '../../dialogs/dialog-add-project-student/dialog-add-project-student';
import {NotificationService} from '../../notification.service';
import {StudentService} from '../../auth/student/student.service';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit, OnChanges {

  projects: Project[];
  students: Student[];
  initProject = false;

  @Input() user: Student;

  constructor(private projectService: ProjectService, private studentService: StudentService,
              private dialog: MatDialog, private notification: NotificationService) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(): void {
    if (this.user && !this.initProject) {
      this.getProjects();
      this.getStudents();
      this.initProject = true;
    }
  }

  getProjects(): void {
    this.projectService.getAllProjectsByUser(this.user).subscribe(result => {
      this.projects = result;
    });
  }

  getStudents(): void {
    this.studentService.getAll().subscribe(result => {
      this.students = result;
    });
  }

  addStudent(project: Project): void {
    const possibleStudents = this.students.filter(
      student => project.students.find(
        wanted => wanted.studentId === student.studentId) === undefined);

    const addStudentAction: AddStudentAction = {students: possibleStudents};
    const dialogRef = this.dialog.open(DialogAddProjectStudent, {
      width: '50%',
      data: addStudentAction
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.projectService.addStudentToProject(project, result.data).subscribe(() => {
          this.notification.success('Pomyślnie dodano studenta');
          this.getProjects();
        }, error => this.notification.error(error.error.message));
      }
    });
  }
}

export interface AddStudentAction {
  students: Student[];
}
