import {Component} from '@angular/core';
import {UserService} from "../services/user.service";
import {User} from "../entities/User";
import {map} from "rxjs";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  errorMessage: string;
  userList: User[];
  userListHtml = '';

  constructor(private userService: UserService) {
    this.errorMessage = '';
    this.userList = [];
  }

  getList() {
    this.userService.getList().pipe(
      map(response => response as User[])
    ).subscribe(
      (userList: User[]) => {
        this.userList = userList;
        this.userListHtml = userList.map(user => `<li>User: ${user.username}</li>`).join('');
      },
      () => {
        this.errorMessage = 'Only admin has access to this functionality.';
      }
    );
  }
}
