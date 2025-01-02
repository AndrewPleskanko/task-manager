import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SidebarService {
  private sidebarState = signal(false);

  toggleSidebar(): void {
    this.sidebarState.set(!this.sidebarState());
  }

  getSidebarState(): boolean {
    return this.sidebarState();
  }

  getSidebarSignal() {
    return this.sidebarState;
  }
}
