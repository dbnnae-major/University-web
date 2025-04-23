import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-table',
  imports: [CommonModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
})
export class TableComponent {
  private userService: UserService;
  private http: HttpClient;
  private router: Router;

  username$: Observable<string | undefined>;
  requests: any[] = [];

  constructor() {
    this.userService = inject(UserService);
    this.username$ = new BehaviorSubject<string | undefined>(undefined);
    this.http = inject(HttpClient);
    this.router = inject(Router);
  }

  ngOnInit(): void {
    this.userService.getUsernameObservable().subscribe((username) => {
      (this.username$ as BehaviorSubject<string | undefined>).next(username);

      if (username) {
        const url = `http://127.0.0.1:25664/controller?username=${username}`;

        this.http
          .get(url, {
            headers: {
              Authorization: 'Bearer token',
              'X-Request-Type': 'Check',
            },
            withCredentials: true,
          })
          .subscribe({
            next: (response: any) => {
              console.log('Table info', response);
              this.requests = response;
            },
            error: (error) => {
              if (error.status === 400) {
                console.error('Bad request: Missing username', error);
              } else if (error.status === 401) {
                console.error('Unauthorized: Invalid username', error);
              } else if (error.status === 404) {
                console.error('Error: No requests for that username', error);
              } else {
                console.error('Other error occurred', error);
              }
            },
            complete: () => {
              console.log('Login request completed');
            },
          });
      }
    });
  }

  deleteRequest(index: number, requestId: string): void {
    if (index > -1) {
      this.requests.splice(index, 1);

      const username =
        this.username$ instanceof BehaviorSubject
          ? (this.username$ as BehaviorSubject<string | undefined>).value
          : undefined;

      if (username) {
        const url = `http://127.0.0.1:25664/controller?username=${username}&id=${requestId}`;

        this.http
          .delete(url, {
            headers: {
              Authorization: 'Bearer token',
              'X-Request-Type': 'Delete',
            },
            withCredentials: true,
          })
          .subscribe({
            next: (response) => {
              console.log('Request deleted successfully:', response);
            },
            error: (error) => {
              console.error('Error deleting request:', error);
              this.requests.splice(index, 0, { id: requestId });
            },
            complete: () => {
              console.log('Delete request completed');
            },
          });
      }
    }
  }
}
