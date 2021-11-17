import {Student} from '../auth/student/student.model';
import {Project} from '../home/projects/project';

export interface Task {
  taskId?: number;
  name?: string;
  description?: string;
  sequence?: number;
  state?: string;
  createDateTime?: string;
  project?: Project;
  student?: Student;
}
