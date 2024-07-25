import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { log } from 'console';
import { LoginRequestDTO } from '../../dtos/login.request.dto';
import { catchError, map, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class SignInService {
  private apiURL: string = environment.apiUrl;
  constructor(private http: HttpClient) {}

  public login(userLogin: LoginRequestDTO): Observable<any> {
    const loginUrl = `${this.apiURL}/auth/signin`;

    return this.http.post(loginUrl, userLogin, { withCredentials: true }).pipe(
      map((response) => {
        // Xử lý dữ liệu ở đây nếu cần
        return response;
      }),
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }
}
