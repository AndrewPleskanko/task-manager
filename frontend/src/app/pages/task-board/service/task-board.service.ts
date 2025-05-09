import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Task} from '../models/task.models';
import {HttpClient} from "@angular/common/http";
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskBoardService {

  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getAllTasks(): Observable<Task[]> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Task[]>(`${environment.apiUrl}/tasks`, {headers});
  }

  addTask(task: Omit<Task, 'id' | 'createdAt' | 'updatedAt' | 'completedAt'>): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.post<Task>(`${environment.apiUrl}`, task, {headers});
  }

  updateTaskStatus(taskId: string, newStatus: string): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.put<Task>(`${environment.apiUrl}/tasks/${taskId}/status`, newStatus, {headers});
  }

  getStatuses(): Observable<string[]> {
    const headers = this.headerService.getHeaders();
    return this.http.get<string[]>(`${environment.apiUrl}/tasks/statuses`, {headers});
  }
}
