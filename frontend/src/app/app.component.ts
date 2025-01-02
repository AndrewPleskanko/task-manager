import { Component, effect, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './sidebar/sidebar.component';
import { SidebarService } from './global-services/sidebar.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  isSidebarOpen = signal(false);

  constructor(private sidebarService: SidebarService) {
    effect(() => {
      this.isSidebarOpen.set(this.sidebarService.getSidebarSignal()());
    }, { allowSignalWrites: true });
  }
}
