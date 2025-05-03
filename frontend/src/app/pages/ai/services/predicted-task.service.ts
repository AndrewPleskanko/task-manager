import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'; // Import HttpHeaders
import {Observable} from 'rxjs';
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";
import {PredictedTasksByUser} from "../models/assignment.models";

@Injectable({
  providedIn: 'root'
})
export class PredictionService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  predictTasksForNextSprint(): Observable<PredictedTasksByUser> {
    const headers: HttpHeaders = this.headerService.getHeaders();
    return this.http.post<PredictedTasksByUser>(
      `${environment.apiUrl}/ai/process/1`,
      {},
      { headers }
    );
  }
}
