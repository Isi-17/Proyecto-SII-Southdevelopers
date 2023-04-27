import { Component } from '@angular/core';
import { usuario } from '../models/usuario.models'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario: usuario = {email: '',
  password: ''};

  constructor() {}

  login() {
    console.log(this.usuario.email);
    console.log(this.usuario.password);
  }
}
