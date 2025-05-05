import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'statusDisplay',
})
export class StatusPipe implements PipeTransform {
  private readonly translations: Record<string, string> = {
    TO_DO: 'To Do',
    IN_PROGRESS: 'In Progress',
    COMPLETED: 'Completed',
    BLOCKED: 'Blocked',
    CANCELLED: 'Cancelled',
    IN_REVIEW: 'In Review',
  };

  transform(status: string): string {
    return this.translations[status] || status;
  }
}
