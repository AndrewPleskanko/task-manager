import {Component} from '@angular/core';
import {CdkDrag, CdkDropList, CdkDropListGroup, CdkDragHandle} from '@angular/cdk/drag-drop';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ToastrModule, ToastrService} from 'ngx-toastr';

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
    ToastrModule
  ],
  templateUrl: './task-board.component.html',
  styleUrls: ['./task-board.component.css']
})
export class TaskBoardComponent {

  columns = [
    {name: 'To Do', tasks: [{name: 'Task 1'}, {name: 'Task 2'}]},
    {name: 'In Progress', tasks: [{name: 'Task 3'}, {name: 'Task 4'}]},
    {name: 'Done', tasks: [{name: 'Task 5'}]},
  ];

  newTask: string[] = [];
  showInput: boolean[] = [];

  constructor(private toastr: ToastrService) {
  }

  drop(event: CdkDragDrop<{ name: string }[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }

  toggleInput(index: number) {
    this.showInput[index] = !this.showInput[index];
  }

  addTask(columnIndex: number, taskName: string) {
    if (taskName) {
      this.columns[columnIndex].tasks.push({name: taskName});
      this.toastr.success('Task added successfully!', 'Success');
      this.newTask[columnIndex] = '';
      this.toggleInput(columnIndex);
    } else {
      this.toastr.error('Task name cannot be empty!', 'Error');
    }
  }
}
