import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {UserStoryService} from "./services/user-stories.service";
import {UserStory} from "./models/user-stories.models";
import {PipesModule} from "../../pipes/pipes.module";

@Component({
  selector: 'app-user-stories',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    PipesModule
  ],
  templateUrl: './user-stories.component.html',
  styleUrl: './user-stories.component.scss'
})
export class UserStoriesComponent implements OnInit {
  userStories: UserStory[] = [];
  projectId = 1;
  errorMessage = '';

  constructor(private userStoryService: UserStoryService) {
  }

  ngOnInit(): void {
    this.fetchUserStories();
  }

  fetchUserStories(): void {
    this.userStoryService.getUserStoriesByProjectId(this.projectId).subscribe(
      (data: UserStory[]) => {
        this.userStories = data;
      },
      () => {
        this.errorMessage = 'Failed to fetch user stories.';
      }
    );
  }

  editUserStory(id: number) {
    console.error(id);
  }

  deleteUserStory(id: number) {
    console.error(id);
  }

  createUserStory() {
    console.error("Some info");
  }
}
