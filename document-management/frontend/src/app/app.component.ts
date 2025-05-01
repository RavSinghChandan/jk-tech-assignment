import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  template: `
    <mat-toolbar color="primary">
      <span>Document Management System</span>
      <span class="spacer"></span>
      <button mat-button *ngIf="isAuthenticated" (click)="logout()">Logout</button>
    </mat-toolbar>
    <div class="content">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }
    .content {
      padding: 20px;
    }
  `]
})
export class AppComponent {
  isAuthenticated = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.isAuthenticated = this.authService.isAuthenticated();
  }

  logout(): void {
    this.authService.logout();
    this.isAuthenticated = false;
    this.router.navigate(['/login']);
  }
} 