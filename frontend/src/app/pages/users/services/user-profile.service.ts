import {Injectable} from '@angular/core';
import {environment} from '../../../environment/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HeaderService} from "../../../global-services/header.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getUsersFile(format: 'excel' | 'csv'): Observable<Blob> {
    const headers = this.headerService.getHeaders();

    return this.http.get(`${environment.apiUrl}/tasks/export?format=${format}`, {
      headers,
      responseType: 'blob'
    });
  }

}
