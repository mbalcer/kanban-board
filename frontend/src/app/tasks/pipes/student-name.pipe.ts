import {Pipe, PipeTransform} from '@angular/core';
import {Student} from '../../student/student.model';

@Pipe({
  name: 'studentName'
})
export class StudentNamePipe implements PipeTransform {

  transform(value: Student): string {
    return value.firstName + ' ' + value.lastName + ' (' + value.email + ')';
  }

}
