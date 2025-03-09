import {Component, effect, signal} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {FooterComponent} from './footer/footer.component';
import {HeaderComponent} from './header/header.component';
import {CommonModule} from '@angular/common';
import {SidebarComponent} from './sidebar/sidebar.component';
import {SidebarService} from './global-services/sidebar.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  isSidebarOpen = signal(true);

  constructor(private sidebarService: SidebarService,
              private router: Router) {
    effect(() => {
      this.isSidebarOpen.set(this.sidebarService.getSidebarSignal()());
    }, {allowSignalWrites: true});
  }

  get sidebarClass() {
    return this.isSidebarOpen() ? 'sidebar-open' : 'sidebar-closed';
  }

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}
