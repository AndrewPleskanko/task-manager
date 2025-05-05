import {Component, signal, effect, computed} from '@angular/core';
import {SidebarService} from '../global-services/sidebar.service';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  imports: [RouterLink, RouterLinkActive, NgForOf],
  styleUrls: ['./sidebar.component.css'],
})
export class SidebarComponent {
  isOpen = signal(false);
  isAdmin = true;

  sections = [
    {
      title: 'Work',
      items: [
        {label: 'Tasks', path: '/tasks'},
        {label: 'Projects', path: '/projects'},
        {label: 'Calendar', path: '/calendar'},
        {label: 'User Stories', path: '/user-stories'}
      ]
    },
    {
      title: 'Communication',
      items: [
        {label: 'Notifications', path: '/notifications'},
        {label: 'Teams', path: '/teams'},
      ]
    },
    {
      title: 'Tools',
      items: [
        {label: 'Dashboard', path: '/dashboard'},
        {label: 'AI Assistant', path: '/ai'},
        {label: 'Charts', path: '/charts'},
        {label: 'Settings', path: '/settings'},
      ]
    },
    {
      title: 'Administration',
      items: [
        {label: 'Users', path: '/admin/users'},
        {label: 'Tasks', path: '/admin/tasks'},
        {label: 'Tags', path: '/admin/tags'},
        {label: 'Settings', path: '/admin/settings'},
        {label: 'Logs', path: '/admin/logs'},
        {label: 'Reports', path: '/admin/reports'},
      ],
      adminOnly: true
    }
  ];

  filteredSections = computed(() =>
    this.sections.filter(section => !section.adminOnly || this.isAdmin)
  );

  constructor(private sidebarService: SidebarService) {
    effect(() => {
      this.isOpen.set(this.sidebarService.getSidebarSignal()());
    }, {allowSignalWrites: true});
  }
}
