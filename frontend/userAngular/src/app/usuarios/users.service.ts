import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { usuario } from "../models/usuario.models";
import { Observable } from "rxjs";
import { CookieService } from "ngx-cookie-service";

@Injectable({
  providedIn: "root",
})
export class usersService {
  constructor(private http: HttpClient, private cookies: CookieService) {} 

  login(user: usuario): Observable<any> {
    return this.http.post("http://localhost:8080/usuarios/login", user);
  }

  // forgotPassword(user: usuario): Observable<any> {
  //   return this.http.post("http://localhost:8080/usuarios/forgotPassword", user);
  // }
  // forgotPassword(email: string): Observable<any> {
  //   return this.http.post("http://localhost:8080/usuarios/forgotPassword", { email });
  // }
  resetPassword(email: string): Observable<any> {
    return this.http.post("http://localhost:8080/usuarios/forgotPassword", { email });
  }

  verifyEmail(email: string): Observable<any> {
    // Realiza la solicitud HTTP al backend para verificar si el correo electrónico existe en la base de datos
    return this.http.get<any>(`http://localhost:8080/usuarios/forgotPassword?email=${email}`);
  }
  

  setToken(token: string){
    this.cookies.set("token",token);
  }

  getToken(){
    return this.cookies.get("token");
  }




}