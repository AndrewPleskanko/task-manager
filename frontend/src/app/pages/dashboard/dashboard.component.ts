import {Component, OnInit} from '@angular/core';
import {TaskService} from "./services/task.service";
import {Task} from "./models/dashboard.models";
import {DatePipe, NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {PipesModule} from "../../pipes/pipes.module";
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    DatePipe,
    FormsModule,
    NgForOf,
    PipesModule,
    RouterModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  tasksList: Task[] = [];
  task: Task = {};
  filterStatus = 'all';
  statuses: string[] = [];
  priorities: string[] = [];
  isEditMode = false;

  constructor(private taskService: TaskService) {
  }

  ngOnInit(): void {
    this.loadTasks();
    this.loadStatuses();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe(data => {
      this.tasksList = data;
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

  filterTasks(): Task[] {
    if (this.filterStatus === 'all') return this.tasksList;
    return this.tasksList.filter(task => task.status === this.filterStatus);
  }

  createTask() {
    this.taskService.createTask(this.task).subscribe(
      () => {
        this.loadTasks();
      }
    )
  }

  openEditDialog(task: Task) {
    this.isEditMode = true;
    this.task = task;
    this.loadPriorities();
    this.loadStatuses();
  }

  openCreateDialog() {
    this.loadPriorities();
    this.loadStatuses();
  }

  confirmDelete() {
    if (this.task.id !== undefined) {
      this.taskService.deleteTask(this.task.id).subscribe(
        () => {
          this.loadTasks();
        },
        error => {
          console.error('Error deleting task:', error);
        }
      );
    }
    }
}
