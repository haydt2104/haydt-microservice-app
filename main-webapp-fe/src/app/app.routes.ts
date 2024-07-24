import { SignUpComponent } from './auth/signup/signup.component';
import { SignInComponent } from './auth/signin/signin.component';
import { Routes } from '@angular/router';
import { HomeComponent } from './user/home/home.component';
import { AdminComponent } from './admin/admin/admin.component';
import { UserManagementComponent } from './admin/user-management/user-management.component';
import { ChildUserManagementComponent } from './admin/user-management/child-user-management/child-user-management.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent,
      },
    ],
  },
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      {
        path: 'user/management',
        component: UserManagementComponent,
        children: [
          {
            path: 'child',
            component: ChildUserManagementComponent,
          },
        ],
      },
    ],
  },
  {
    path: 'auth',
    children: [
      {
        path: 'sign-in',
        component: SignInComponent,
      },
      {
        path: 'sign-up',
        component: SignUpComponent,
      },
    ],
  },
];
