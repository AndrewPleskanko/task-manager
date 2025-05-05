import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";
import {UserStory} from "../models/user-stories.models";

@Injectable({
  providedIn: 'root'
})
export class UserStoryService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getUserStoriesByProjectId(projectId: number): Observable<UserStory[]> {
    const headers: HttpHeaders = this.headerService.getHeaders();
    return this.http.get<UserStory[]>(
      `${environment.apiUrl}/user-stories/project/${projectId}`,
      {headers}
    );
  }
}
