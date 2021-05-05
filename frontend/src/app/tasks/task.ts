import {Student} from '../auth/student/student.model';

export interface Task {
  taskId?: number;
  name?: string;
  description?: string;
  sequence?: number;
  state?: string;
  createDateTime?: string;
  project?: any;
  student?: Student;
}
