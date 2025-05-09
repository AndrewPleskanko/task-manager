import {Component, ElementRef, ViewChild} from '@angular/core';
import {PredictionService} from "./services/predicted-task.service";
import {NgForOf, NgIf} from "@angular/common";
import {PredictedTasksByUser} from "./models/assignment.models";
import Gantt from 'frappe-gantt';

@Component({
  selector: 'app-ai',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './ai.component.html',
  styleUrl: './ai.component.css'
})
export class AiComponent {
  @ViewChild('ganttContainer') ganttContainer!: ElementRef;

  showPrediction = false;
  predictedAssignments: PredictedTasksByUser[] = [];
  ganttTasks: {
    name: string;
    start: string;
    end: string;
    progress: number;
    dependencies: string;
    hours: number;
  }[] = [];

  constructor(private predictionService: PredictionService) {
  }

  predictNextSprint(): void {
    this.predictionService.predictTasksForNextSprint().subscribe(
      (data) => {
        this.predictedAssignments = Array.isArray(data) ? data : [data];
        this.showPrediction = true;

        this.ganttTasks = this.predictedAssignments.flatMap((assignment) =>
          assignment.assignedStories.flatMap((story) =>
            story.tasks.map((task) => ({
              name: `Task ${task.title} for ${assignment.userName}`,
              start: task.estimatedStartDate,
              end: task.estimatedEndDate,
              progress: 0,
              dependencies: '',
              hours: task.estimatedHours
            }))
          )
        );
        this.renderGanttChart();
      }
    );
  }

  renderGanttChart(): void {
    setTimeout(() => {
      if (this.ganttContainer && this.ganttTasks.length > 0) {
        new Gantt(this.ganttContainer.nativeElement, this.ganttTasks, {
          view_mode: 'Day',
          language: 'en',
          readonly: true
        });
      }
    }, 0);
  }

  countTasks(assignedStories: { tasks: any[] }[]): number {
    return assignedStories.reduce((acc, story) => acc + (story.tasks?.length || 0), 0);
  }
}
