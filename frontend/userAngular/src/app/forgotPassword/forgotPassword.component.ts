import { Component } from '@angular/core';
import { usuario } from '../models/usuario.models'
import { usersService } from '../usuarios/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgotPassword',
  templateUrl: './forgotPassword.component.html',
  styleUrls: ['./forgotPassword.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';

  constructor(public userService: usersService, public router: Router) {}

  forgotPassword() {
    this.userService.resetPassword(this.email).subscribe(
      data => {
        if(data) {
          alert('Debug: La nueva contraseña es '+data);
        } else {
          alert('Debug: El email no existe en la base de datos');
        }
        this.router.navigateByUrl("/login");
    },
      error => {
      console.log('Error al resetear la contraseña:', error);
    });
  }
}  
