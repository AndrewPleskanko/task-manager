import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskChartService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getTaskCountByStatus(): Observable<Map<string, number>> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Map<string, number>>(`${environment.apiUrl}/tasks/chart/status`, { headers });
  }

  getTaskCountByPriority(): Observable<Map<string, number>> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Map<string, number>>(`${environment.apiUrl}/tasks/chart/priority`, { headers });
  }

  getTaskCountByUser(): Observable<Map<number, number>> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Map<number, number>>(`${environment.apiUrl}/tasks/chart/users`, { headers });
  }

  getCompletedTaskCountByDay(): Observable<Map<string, number>> {
    const headers = this.headerService.getHeaders();
    return this.http.get<Map<string, number>>(`${environment.apiUrl}/tasks/chart/completed`, { headers });
  }
}
