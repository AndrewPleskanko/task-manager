import {Component, OnInit} from '@angular/core';
import {UserProfileService} from "./services/user-profile.service";
import {User} from "../../entities/User";
import {PipesModule} from "../../pipes/pipes.module";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    PipesModule,
    FormsModule
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent implements OnInit {
  user = new User();

  constructor(private userProfileService: UserProfileService) {
  }

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    this.userProfileService.getUserInfo().subscribe({
      next: (data: User) => {
        this.user = data;
      }
    });
  }

  saveUserChanges() {
    // TODO: Implement logic to save user changes
  }

  deleteUser() {
    // TODO: Implement logic to delete user
  }
}
