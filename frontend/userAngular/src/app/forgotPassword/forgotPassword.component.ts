import { Component } from '@angular/core';
import { usuario } from '../models/usuario.models'
import { usersService } from '../usuarios/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgotPassword',
  templateUrl: './forgotPassword.component.html',
  styleUrls: ['./forgotPassword.component.css']
})
export class forgotPasswordComponent {
  usuario: usuario = {email: '',
  password: ''};

  constructor(public userService: usersService, public router: Router) {}

  forgotPassword() {
    this.userService.forgotPassword(this.usuario).subscribe((data) => { 
      // this.userService.setToken(data.token);
      this.router.navigateByUrl("/");
    });
  }
}
