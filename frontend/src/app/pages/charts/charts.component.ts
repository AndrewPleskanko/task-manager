import { Component, OnInit } from '@angular/core';
import { TaskService } from "../dashboard/services/task.service";
import { Task } from "../dashboard/models/dashboard.models";
import { NgxChartsModule, Color, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-charts',
  standalone: true,
  imports: [NgxChartsModule],
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.scss']
})
export class ChartsComponent implements OnInit {
  view: [number, number] = [700, 400];
  taskStatusData: { name: string, value: number }[] = [];
  taskPriorityData: { name: string, value: number }[] = [];
  showLegend = true;
  showLabels = true;
  animations = true;
  colorScheme: Color = {
    name: 'default',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };
  tasks: Task[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe(data => {
      this.tasks = data;
      this.processTaskData(this.tasks);
    });
  }

  processTaskData(tasks: Task[]): void {
    const statusCounts: Record<string, number> = tasks.reduce((acc, task) => {
      acc[task.status] = (acc[task.status] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    this.taskStatusData = Object.keys(statusCounts).map(status => ({
      name: status,
      value: statusCounts[status]
    }));

    const priorityCounts: Record<string, number> = tasks.reduce((acc, task) => {
      acc[task.priority] = (acc[task.priority] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    this.taskPriorityData = Object.keys(priorityCounts).map(priority => ({
      name: priority,
      value: priorityCounts[priority]
    }));
  }
}
