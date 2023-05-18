import { Component } from '@angular/core';
import { usuario } from '../models/usuario.models'
import { usersService } from '../usuarios/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario: usuario = {email: '',
  password: ''};

  incorrecto: boolean = false;

  constructor(public userService: usersService, public router: Router) {}

  login() {
    this.userService.login(this.usuario).subscribe(
      data => { 
        this.userService.setToken(data.token);
        this.router.navigateByUrl("/");
    },
    error => {
      console.log(error);
      this.incorrecto=true;
    });
  }

  forgotpassword() {
      this.router.navigateByUrl("/forgotPassword");
  }
}
