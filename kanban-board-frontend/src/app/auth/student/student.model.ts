export class Student {

  studentId: string;
  firstName: string;
  lastName: string;
  indexNumber: string;
  fullTime: boolean;
  email: string;
  password: string;

  constructor(studentId: string, firstName: string, lastName: string, indexNumber: string,
              fullTime: boolean, email: string, password: string) {
    this.studentId = studentId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.indexNumber = indexNumber;
    this.fullTime = fullTime;
    this.email = email;
    this.password = password;
  }

  public static register(firstName: string, lastName: string, indexNumber: string,
                         fullTime: boolean, email: string, password: string): Student {
    return new Student(null, firstName, lastName, indexNumber, fullTime, email, password);
  }

}
