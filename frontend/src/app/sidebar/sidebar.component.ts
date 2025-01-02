import {Component, effect, signal} from '@angular/core';
import {SidebarService} from '../global-services/sidebar.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  isOpen = signal(false);
  links = [
    {label: 'Dashboard', path: '/dashboard'},
    {label: 'Tasks', path: '/tasks'},
    {label: 'Projects', path: '/projects'},
    {label: 'Calendar', path: '/calendar'},
    {label: 'Notifications', path: '/notifications'},
    {label: 'Teams', path: '/teams'},
    {label: 'Settings', path: '/settings'}
  ];

  constructor(private sidebarService: SidebarService) {
    effect(() => {
      this.isOpen.set(this.sidebarService.getSidebarState()());
    });
  }
}
