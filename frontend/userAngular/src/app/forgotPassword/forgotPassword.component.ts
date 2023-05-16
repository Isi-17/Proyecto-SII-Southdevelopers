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
  usuario: usuario = {email: '',
  password: ''};

  constructor(public userService: usersService, public router: Router) {}

  forgotPassword() {
    this.userService.resetPassword(this.usuario.email).subscribe((data) => {
      console.log('Nueva contraseña:', data.newPassword); // Para las pruebas
      this.router.navigateByUrl("/");
    }, (error) => {
      // Mostrar mensaje de error al usuario
      console.log('Error al resetear la contraseña:', error);
      this.router.navigateByUrl("/");
    });
  }
}  
