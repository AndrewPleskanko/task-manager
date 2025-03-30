import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'priorityDisplay',
})
export class PriorityPipe implements PipeTransform {
  private readonly translations: Record<string, string> = {
    LOW: 'Low',
    MEDIUM: 'Medium',
    HIGH: 'High',
  };

  transform(priority: string): string {
    return this.translations[priority] || priority;
  }
}
