import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  private router: Router;
  private userService: UserService;

  username: string | undefined;

  constructor() {
    this.userService = inject(UserService);
    this.router = inject(Router);
  }

  logout() {
    this.userService.clearUsername();
    this.router.navigate(['/']);
  }

  ngOnInit(): void {
    this.username = this.userService.getUsername();
  }
}
