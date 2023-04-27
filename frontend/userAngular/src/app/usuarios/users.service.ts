import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { usuario } from "../models/usuario.models";
import { Observable } from "rxjs";
import { CookieService } from "ngx-cookie-service";

@Injectable({
  providedIn: "root",
})
export class usersService {
  constructor(private http: HttpClient , private cookies: CookieService) {}

  login(user: usuario): Observable<any> {
    return this.http.post("http://localhost:8080/usuarios/login", user);
  }

  forgotPassword(user: usuario): Observable<any> {
    return this.http.post("http://localhost:8080/usuarios/forgotPassword", user);
  }

  setToken(token: string){
    this.cookies.set("token",token);
  }

  getToken(){
    return this.cookies.get("token");
  }
}