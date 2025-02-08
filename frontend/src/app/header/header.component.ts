import {Component} from '@angular/core';
import {SidebarService} from '../global-services/sidebar.service';
import {RouterModule} from "@angular/router";
import {AuthService} from "../global-services/auth.service";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  constructor(private sidebarService: SidebarService,
              private authService: AuthService) {
  }

  logout() {
    this.authService.logout();
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }
}
