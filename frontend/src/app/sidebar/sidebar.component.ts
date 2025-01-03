import { Component, signal, effect } from '@angular/core';
import { SidebarService } from '../global-services/sidebar.service';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  imports: [RouterLink, RouterLinkActive, NgForOf],
  styleUrls: ['./sidebar.component.css'],
})
export class SidebarComponent {
  isOpen = signal(false);
  links = [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Tasks', path: '/tasks' },
    { label: 'Projects', path: '/projects' },
    { label: 'Calendar', path: '/calendar' },
    { label: 'Notifications', path: '/notifications' },
    { label: 'Teams', path: '/teams' },
    { label: 'Settings', path: '/settings' },
    { label: 'AI Assistant', path: '/ai' },
    { label: 'Charts', path: '/charts' },
  ];

  constructor(private sidebarService: SidebarService) {
    effect(() => {
      this.isOpen.set(this.sidebarService.getSidebarSignal()());
    }, { allowSignalWrites: true });
  }
}
