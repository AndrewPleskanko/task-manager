import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../models/dashboard.models';
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getTasks(): Observable<Task[]> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Task[]>(`${environment.apiUrl}/tasks`, {headers});
  }

  getStatuses(): Observable<string[]> {
    const headers = this.headerService.getHeaders();
    return this.http.get<string[]>(`${environment.apiUrl}/tasks/statuses`, {headers});
  }

  getPriorities(): Observable<string[]> {
    const headers = this.headerService.getHeaders();
    return this.http.get<string[]>(`${environment.apiUrl}/tasks/priorities`, {headers});
  }

  getTaskById(id: string): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Task>(`${environment.apiUrl}/tasks/${id}`, {headers});
  }

  createTask(task: Task): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.post<Task>(`${environment.apiUrl}/tasks`, task, {headers});
  }

  updateTask(id: string, task: Task): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.put<Task>(`${environment.apiUrl}/tasks/${id}`, task, {headers});
  }

  deleteTask(id: string): Observable<void> {
    const headers = this.headerService.getHeaders();
    return this.http.delete<void>(`${environment.apiUrl}/tasks/${id}`, {headers});
  }
}
