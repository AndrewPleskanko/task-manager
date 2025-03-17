import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'statusDisplay',
})
export class StatusPipe implements PipeTransform {
  private readonly translations: Record<string, string> = {
    OPEN: 'Open',
    IN_PROGRESS: 'In Progress',
    COMPLETED: 'Completed',
    BLOCKED: 'Blocked',
    CANCELLED: 'Cancelled',
  };

  transform(status: string): string {
    return this.translations[status] || status;
  }
}
