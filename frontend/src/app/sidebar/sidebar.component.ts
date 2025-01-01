import { Component, effect, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarService } from '../global-services/sidebar.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  isOpen = signal(true);

  constructor(private sidebarService: SidebarService) {
    effect(() => {
      const state = this.sidebarService.getSidebarState()();
      this.isOpen.set(state);
    }, { allowSignalWrites: true });
  }
}
