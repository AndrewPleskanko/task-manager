import {Component} from '@angular/core';
import {SidebarService} from '../global-services/sidebar.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  constructor(private sidebarService: SidebarService) {
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }
}
