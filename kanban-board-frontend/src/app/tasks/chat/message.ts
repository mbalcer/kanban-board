import {Student} from '../../auth/student/student.model';

export interface Message {
  student: Student;
  message: string;
}
