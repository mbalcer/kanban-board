import {Student} from '../../auth/student/student.model';
import {Task} from '../../tasks/task';

export interface Project {
  projectId?: number;
  name?: string;
  description?: string;
  createDateTime?: string;
  updateDateTime?: string;
  submitDateTime?: string;
  tasks?: Task[];
  students?: Student[];
}
