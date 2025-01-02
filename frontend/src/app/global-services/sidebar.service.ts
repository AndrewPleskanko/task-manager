import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  private sidebarState = signal(false);

  toggleSidebar() {
    this.sidebarState.set(!this.sidebarState());
  }

  getSidebarState() {
    return this.sidebarState;
  }
}
