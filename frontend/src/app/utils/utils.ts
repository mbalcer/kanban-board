export class Utils {

  public static checkIfStringNotEmpty(data: string | null): boolean {
    return data != null && data.trim() !== '';
  }

  public static checkIfIndexNumberCorrect(indexNumber: string): boolean {
    return indexNumber == null ? false :
      indexNumber.match('^[0-9]+$') != null;
  }

  public static checkIfEmailCorrect(email: string): boolean {
    return email == null ? false :
      email.match('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$') != null;
  }

  public static checkIfPasswordCorrect(password: string): boolean {
    return password == null ? false :
      password.match('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$') != null;
  }

}
