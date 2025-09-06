import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';
  private tokenKey = 'authToken';
  private roleKey = 'userRole';

  constructor(private http: HttpClient, private router: Router) {}

  // REGISTER
  register(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/register`, userData);
  }

  // LOGIN -> STORE TOKEN -> FETCH PROFILE
  login(email: string, password: string): Observable<any> {
  return this.http
    .post(`${this.baseUrl}/users/login`, { email, password }, { responseType: 'text' })
    .pipe(
      tap((token: string) => {
        localStorage.setItem(this.tokenKey, token);
      }),
      switchMap((token: string) => {
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        return this.http.get<any>(`${this.baseUrl}/users/profile`, { headers });
      }),
      tap((user: any) => {
        if (user?.role) {
          localStorage.setItem(this.roleKey, user.role);
        }
        if (user?.fullName) {
          localStorage.setItem('fullName', user.fullName);
        }
        if (user?.id) { 
          localStorage.setItem('userId', user.id.toString());
        }
      })
    );
}



  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.roleKey);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getUsername(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('fullName');
    }
    return null;
  }
 

  getRole(): string | null {
    return localStorage.getItem(this.roleKey);
  }

  getUserId(): number | null {
    const id = localStorage.getItem('userId');
    return id ? Number(id) : null;
  }

  getProfile(): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.getToken()}`);
    return this.http.get(`${this.baseUrl}/users/profile`, { headers });
  }

  updateProfile(profileData: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.getToken()}`);
    return this.http.put(`${this.baseUrl}/users/profile`, profileData, { headers });
  }

  updatePassword(passwordData: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.getToken()}`);
    return this.http.put(`${this.baseUrl}/users/password`, passwordData, { headers });
  }

  deleteProfile(): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.getToken()}`);
    return this.http.delete(`${this.baseUrl}/users/profile`, { headers });
  }
}
