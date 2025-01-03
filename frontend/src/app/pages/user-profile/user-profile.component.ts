import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

export interface User {
  id: string | null;
  name: string;
  email: string;
  role: string;
  avatarUrl: string;
  joinedDate: string;
}

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent implements OnInit {
  user: User = {
    id: null,
    name: '',
    email: '',
    role: '',
    avatarUrl: '',
    joinedDate: ''
  };

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    this.loadUser(userId);
  }

  loadUser(userId: string | null): void {
    this.user = {
      id: userId,
      name: 'John Doe',
      email: 'johndoe@example.com',
      role: 'Admin',
      avatarUrl: 'https://via.placeholder.com/150',
      joinedDate: '2023-01-01',
    };
  }
}
