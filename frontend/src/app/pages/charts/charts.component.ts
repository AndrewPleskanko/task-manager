import {Component, OnInit} from '@angular/core';
import {NgxChartsModule, Color, ScaleType} from '@swimlane/ngx-charts';
import {TaskChartService} from './services/task-chart.service';

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
  taskUserData: { name: string, value: number }[] = [];
  completedTaskDailyData: { name: string; series: { name: string; value: number }[] }[] = [];
  showLegend = true;
  showLabels = true;
  animations = true;
  colorScheme: Color = {
    name: 'default',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  constructor(private taskChartService: TaskChartService) {
  }

  ngOnInit(): void {
    this.loadTaskData();
  }

  loadTaskData(): void {
    this.loadTaskCountByStatus();
    this.loadTaskCountByPriority();
    this.loadTaskCountByUser();
    this.loadCompletedTaskCountByDay();
  }

  loadTaskCountByStatus(): void {
    this.taskChartService.getTaskCountByStatus().subscribe(data => {
      this.taskStatusData = Object.entries(data).map(([key, value]) => ({
        name: key,
        value: value as number
      }));
    });
  }

  loadTaskCountByPriority(): void {
    this.taskChartService.getTaskCountByPriority().subscribe(data => {
      this.taskPriorityData = Object.entries(data).map(([key, value]) => ({
        name: key,
        value: value as number
      }));
    });
  }

  loadTaskCountByUser(): void {
    this.taskChartService.getTaskCountByUser().subscribe(data => {
      this.taskUserData = Object.entries(data).map(([key, value]) => ({
        name: key.toString(),
        value: value as number
      }));
    });
  }

  loadCompletedTaskCountByDay(): void {
    this.taskChartService.getCompletedTaskCountByDay().subscribe(data => {
      this.completedTaskDailyData = [
        {
          name: 'Completed Tasks',
          series: Object.entries(data).map(([key, value]) => ({
            name: new Date(key).toLocaleDateString(),
            value: value as number
          }))
        }
      ];
    });
  }
}
