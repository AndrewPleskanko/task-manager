import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SidebarService {
  private isSidebarOpen = signal(false);

  toggleSidebar() {
    this.isSidebarOpen.set(!this.isSidebarOpen());
  }

  getSidebarSignal() {
    return this.isSidebarOpen.asReadonly();
  }

  openSidebar() {
    this.isSidebarOpen.set(true);
  }

  closeSidebar() {
    this.isSidebarOpen.set(false);
  }
}
