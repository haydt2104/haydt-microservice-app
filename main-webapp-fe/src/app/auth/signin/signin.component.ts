import { SignInService } from './../../service/auth/sign-in.service';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { LoginRequestDTO } from '../../dtos/login.request.dto';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule, HttpClientModule],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
})
export class SignInComponent implements OnInit {
  public userLogin: LoginRequestDTO | undefined;
  public formLoginDataReq: LoginRequestDTO | any;
  public submitted = false;
  constructor(
    private signInService: SignInService,
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.userLogin = {
      email: '',
      password: '',
    };
  }
  ngOnInit(): void {
    this.formLoginDataReq = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  public login(): void {
    this.submitted = true;

    if (this.formLoginDataReq.valid) {
      console.log('Is login.....');
      this.signInService.login(this.formLoginDataReq.value).subscribe({
        next: (response) => {
          console.log('Login successful', response);
          this.formLoginDataReq.reset();
        },
        error: (error) => {
          console.error('Login failed', error);
          Swal.fire({
            position: 'center',
            icon: 'error',
            title: 'Login failed',
            text: 'Username or password is incorrect',
            showConfirmButton: false,
            timer: 1500,
          });
        },
        complete: () => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Login successful',
            showConfirmButton: false,
            timer: 1500,
          }).then(() => {
            this.router.navigate(['/home']);
          });
          console.log('Login completed');
        },
      });
    } else {
      // Xử lý trường hợp thiếu thông tin đăng nhập
      console.error('Username and password are required');
    }
  }

  onSubmit() {
    if (this.formLoginDataReq.valid) {
      console.log('Form Data: ', this.formLoginDataReq.value);
    } else {
      console.log('Invalid form');
    }
  }
}
