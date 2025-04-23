import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  [x: string]: any;
  constructor() {}

  private usernameKey = 'username';

  private isClientSide(): boolean {
    return (
      typeof window !== 'undefined' &&
      typeof window.localStorage !== 'undefined'
    );
  }

  getUsernameObservable(): Observable<string | undefined> {
    if (this.isClientSide()) {
      const username = localStorage.getItem(this.usernameKey);
      return of(username || undefined);
    }
    return of(undefined);
  }

  setUsername(username: string): void {
    if (this.isClientSide()) {
      localStorage.setItem(this.usernameKey, username);
    }
  }

  getUsername(): string | undefined {
    if (this.isClientSide()) {
      return localStorage.getItem(this.usernameKey) || undefined;
    }
    return undefined;
  }

  clearUsername(): void {
    if (this.isClientSide()) {
      localStorage.removeItem(this.usernameKey);
    }
  }
}
