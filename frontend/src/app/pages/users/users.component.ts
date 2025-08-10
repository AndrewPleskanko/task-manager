import {Component} from '@angular/core';
import {UserService} from "./services/user-profile.service";

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {

  constructor(private userService: UserService) {
  }

  saveUsersFile(format: 'excel' | 'csv') {
    this.userService.getUsersFile(format).subscribe((data: Blob) => {
      const url = window.URL.createObjectURL(data);
      const a = document.createElement('a');
      a.href = url;

      // Визначаємо ім'я файлу в залежності від формату
      const fileExtension = format === 'excel' ? 'xlsx' : 'csv';
      a.download = `users.${fileExtension}`;

      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    });
  }

}
