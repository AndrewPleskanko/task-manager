import {Component} from '@angular/core';
import {PredictionService} from "./services/predicted-task.service";
import {NgForOf, NgIf} from "@angular/common";
import {PredictedTasksByUser} from "./models/assignment.models";

@Component({
  selector: 'app-ai',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './ai.component.html',
  styleUrl: './ai.component.css'
})
export class AiComponent {
  showPrediction: boolean = false;
  predictedAssignments: PredictedTasksByUser[] = [];

  constructor(private predictionService: PredictionService) {
  }

  predictNextSprint(): void {
    this.predictionService.predictTasksForNextSprint().subscribe(
      (data) => {
        this.predictedAssignments = Array.isArray(data) ? data : [data];
        this.showPrediction = true;
      },
      (error) => {
        console.error('Помилка при отриманні передбачень:', error);
      }
    );
  }
}
