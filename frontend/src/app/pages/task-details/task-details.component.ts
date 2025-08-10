import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {PipesModule} from "../../pipes/pipes.module";
import {Task} from "./models/task-details.models";
import {ActivatedRoute, Router} from "@angular/router";
import {TaskService} from "./services/task-details.service";

@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    PipesModule,
    NgIf
  ],
  templateUrl: './task-details.component.html',
  styleUrl: './task-details.component.css'
})
export class TaskDetailsComponent implements OnInit {
  isEditMode = false;
  task: Task = {};
  statuses: string[] = [];
  priorities: string[] = [];

  constructor(private route: ActivatedRoute,
              private taskService: TaskService,
              private router: Router) {
  }

  ngOnInit(): void {
    const taskId = this.route.snapshot.paramMap.get('taskId');
    this.isEditMode = !!taskId;
    if (this.isEditMode && taskId) {
      this.getTaskById(taskId);
    }
    this.loadStatuses();
    this.loadPriorities();
  }

  private getTaskById(taskId: string): void {
    this.taskService.getTaskById(taskId).subscribe((task: Task) => {
      this.task = task;
    });
  }

  loadStatuses(): void {
    this.taskService.getStatuses().subscribe(data => {
      this.statuses = data;
    });
  }

  loadPriorities(): void {
    this.taskService.getPriorities().subscribe(data => {
      this.priorities = data;
    });
  }

  createTask() {
    this.taskService.createTask(this.task).subscribe((task: Task) => {
      this.task = task;
      this.isEditMode = true;
      this.router.navigate(['/dashboard']);
    });
  }

  cancel() {
    return false;
  }
}
