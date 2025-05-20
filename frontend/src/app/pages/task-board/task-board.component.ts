import {Component, OnInit} from '@angular/core';
import {CdkDrag, CdkDropList, CdkDropListGroup, CdkDragHandle} from '@angular/cdk/drag-drop';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {DatePipe, NgClass, NgForOf, NgIf, TitleCasePipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ToastrModule, ToastrService} from 'ngx-toastr';
import {TaskBoardService} from './service/task-board.service';
import {Task} from './models/task.models';
import {RouterLink} from "@angular/router";
import {StatusPipe} from "../../pipes/status.pipe";

interface Column {
  name: string;
  tasks: Task[];
}

@Component({
  selector: 'app-task-board',
  standalone: true,
  imports: [
    CdkDropList,
    CdkDropListGroup,
    CdkDrag,
    CdkDragHandle,
    NgForOf,
    FormsModule,
    ToastrModule,
    NgIf,
    TitleCasePipe,
    DatePipe,
    NgClass,
    RouterLink
  ],
  templateUrl: './task-board.component.html',
  styleUrls: ['./task-board.component.css']
})
export class TaskBoardComponent implements OnInit {
  columns: Column[] = [];

  newTask: string[] = [];
  showInput: boolean[] = [];
  tasks: Task[] = [];
  statusColumns: string[] = [];
  private statusPipe = new StatusPipe();

  constructor(private toast: ToastrService, private taskBoard: TaskBoardService) {
  }

  ngOnInit(): void {
    this.loadStatusesAndTasks();
     }

  loadStatusesAndTasks() {
    this.taskBoard.getStatuses().subscribe(
      (statusList) => {
        this.columns = statusList.map((status: string) => ({
          name: status,
          tasks: []
        }));

        this.loadTasks();
      },
      (error) => {
        this.toast.error('Failed to load statuses!', 'Error');
        console.error('Error loading statuses:', error);
      }
    );
  }

  loadTasks() {
    this.taskBoard.getAllTasks().subscribe(
      (tasks) => {
        this.tasks = tasks;
        this.groupTasksByStatus();
      },
      (error) => {
        this.toast.error('Failed to load tasks!', 'Error');
        console.error('Error loading tasks:', error);
      }
    );
  }

  groupTasksByStatus() {
    this.columns.forEach(column => {
      column.tasks = this.tasks.filter(task => task.status === column.name);
    });
  }

  drop(event: CdkDragDrop<Task[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      const movedTask = event.container.data[event.currentIndex];
      const newStatus = (event.container.id as string).replace('column-', '').toUpperCase();
      this.updateTaskStatus(movedTask.id.toString(), newStatus);
    }
  }

  updateTaskStatus(taskId: string, newStatus: string) {
    this.taskBoard.updateTaskStatus(taskId, newStatus).subscribe(
      () => {
        const statusText = this.statusPipe.transform(newStatus);
        this.toast.success(`Task status updated to ${statusText}!`, 'Success');
        this.loadTasks();
      },
      (error) => {
        this.toast.error('Failed to update task status!', 'Error');
        console.error('Error updating task status:', error);
        this.loadTasks();
      }
    );
  }

  toggleInput(index: number) {
    this.showInput[index] = !this.showInput[index];
  }

  addTask(columnIndex: number, taskName: string) {
    if (taskName) {
      const newTask: Omit<Task, 'id' | 'createdAt' | 'updatedAt' | 'completedAt'> = {
        title: taskName,
        description: null,
        status: this.columns[columnIndex].name,
        assignedTo: null,
        dueDate: null,
        priority: 'MEDIUM',
        tags: [],
        userStoryId: null,
        blockedByTaskId: null,
      };
      this.taskBoard.addTask(newTask).subscribe(
        (addedTask) => {
          this.tasks.push(addedTask);
          this.groupTasksByStatus();
          this.toast.success('Task added successfully!', 'Success');
          this.newTask[columnIndex] = '';
          this.toggleInput(columnIndex);
        },
        (error) => {
          this.toast.error('Failed to add task!', 'Error');
          console.error('Error adding task:', error);
        }
      );
    } else {
      this.toast.error('Task name cannot be empty!', 'Error');
    }
  }

  loadStatuses(): void {
    this.taskBoard.getStatuses().subscribe(data => {
      this.statusColumns = data;
    });
  }
}
