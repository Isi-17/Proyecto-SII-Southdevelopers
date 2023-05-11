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
    this.userService.verifyEmail(this.usuario.email).subscribe((response) => {
      if (response.exists) {
        // El correo electrónico existe, realizar el reseteo de contraseña
        this.userService.resetPassword(this.usuario.email).subscribe((data) => {
          console.log('Nueva contraseña:', data.newPassword);
          this.router.navigateByUrl("/");
        }, (error) => {
          // Mostrar mensaje de error al usuario
          console.log('Error al resetear la contraseña:', error);
          // Agregar código adicional según sea necesario
          this.router.navigateByUrl("/");
        });
      } else {
        // El correo electrónico no existe en la base de datos, mostrar un mensaje de error o tomar alguna acción apropiada
        console.log('El correo electrónico no existe en la base de datos.');
        // Agregar código adicional según sea necesario
        this.router.navigateByUrl("/");
      }
    });
  }
}  
