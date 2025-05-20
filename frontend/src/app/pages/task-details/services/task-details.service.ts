import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../models/task-details.models';
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
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

  updateTask(id: string, task: Task): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.put<Task>(`${environment.apiUrl}/tasks/${id}`, task, {headers});
  }

  createTask(task: Task): Observable<Task> {
    const headers = this.headerService.getHeaders();
    return this.http.post<Task>(`${environment.apiUrl}/tasks`, task, {headers});
  }
}
