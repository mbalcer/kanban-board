import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Student} from '../../auth/student/student.model';
import {ProjectService} from './project.service';
import {Project} from './project';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit, OnChanges {
  projects: Project[];
  initProject = false;

  @Input() user: Student;

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
  }

  ngOnChanges(): void {
    if (this.user && !this.initProject) {
      this.getProjects();
      this.initProject = true;
    }
  }

  getProjects(): void {
    this.projectService.getAllProjectsByUser(this.user).subscribe(result => {
      this.projects = result;
    });
  }

}
