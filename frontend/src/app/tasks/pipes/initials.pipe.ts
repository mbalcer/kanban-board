import {Pipe, PipeTransform} from '@angular/core';
import {Student} from '../../auth/student/student.model';

@Pipe({
  name: 'initials'
})
export class InitialsPipe implements PipeTransform {

  transform(value: Student): string {
    if (value != null) {
      return value.firstName.substr(0, 1).toUpperCase() + value.lastName.substr(0, 1).toUpperCase();
    }
  }

}
