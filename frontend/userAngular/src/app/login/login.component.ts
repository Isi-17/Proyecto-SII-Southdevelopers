import { Component } from '@angular/core';
import { usuario } from '../models/usuario.models'
import { usersService } from '../usuarios/users.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario: usuario = {email: '',
  password: ''};

  constructor(public userService: usersService) {}

  login() {
    this.userService.login(this.usuario).subscribe((data) => { console.log(data); });
  }
}
