import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'roleDisplay',
})
export class RolePipe implements PipeTransform {
  private readonly translations: Record<string, string> = {
    ROLE_ADMIN: 'Admin',
    ROLE_MODERATOR: 'Moderator',
    ROLE_USER: 'User',
  };

  transform(role: string): string {
    return this.translations[role] || role;
  }
}
