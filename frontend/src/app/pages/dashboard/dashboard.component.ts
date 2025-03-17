import {Component, OnInit} from '@angular/core';
import {TaskService} from "./services/task.service";
import {Task} from "./models/dashboard.models";
import {DatePipe, NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {PipesModule} from "../../pipes/pipes.module";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    DatePipe,
    FormsModule,
    NgForOf,
    PipesModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  tasks: Task[] = [];
  filterStatus = 'all';
  statuses: string[] = [];

  constructor(private taskService: TaskService) {
  }

  ngOnInit(): void {
    this.loadTasks();
    this.loadStatuses();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe(data => {
      this.tasks = data;
    });
  }

  loadStatuses(): void {
    this.taskService.getStatuses().subscribe(data => {
      this.statuses = data;
    });
  }

  filterTasks(): Task[] {
    if (this.filterStatus === 'all') return this.tasks;
    return this.tasks.filter(task => task.status === this.filterStatus);
  }

  confirmDeleteTask() {
    return false;
  }

  setTaskToDelete(task: Task) {
    return task;
  }
}
