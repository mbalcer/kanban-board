import {Pipe, PipeTransform} from '@angular/core';
import {Student} from '../../auth/student/student.model';

@Pipe({
  name: 'studentName'
})
export class StudentNamePipe implements PipeTransform {

  transform(value: Student): string {
    if (value != null) {
      return value.firstName + ' ' + value.lastName + ' (' + value.email + ')';
    }
  }

}
