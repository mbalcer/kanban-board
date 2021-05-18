import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private snackBar: MatSnackBar) { }

  error(message: string): void {
    this.snackBar.open('Podczas wykonywania operacji wystąpił błąd: ' + message, null, {
        duration: 5000,
        panelClass: ['error-color']
      });
  }

  success(message: string): void {
    this.snackBar.open(message, null, {
      duration: 5000,
      panelClass: ['success-color']
    });
  }
}
