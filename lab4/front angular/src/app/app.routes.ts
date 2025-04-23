import { Routes } from '@angular/router';

import { WelcomeComponent } from './welcome/welcome.component';
import { HomeComponent } from './home/home.component';
import { TableComponent } from './table/table.component';

export const routes: Routes = [
  {
    path: '',
    title: 'Welcome Page',
    component: WelcomeComponent,
  },
  {
    path: 'home',
    title: 'Home',
    component: HomeComponent,
  },
  {
    path: 'table',
    title: 'Table',
    component: TableComponent,
  },
  {
    path: '**',
    title: '',
    redirectTo: '',
  },
];
