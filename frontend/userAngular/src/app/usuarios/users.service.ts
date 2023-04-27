import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { usuario } from "../models/usuario.models";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class usersService {
  constructor(private http: HttpClient) {}

  login(user: usuario): Observable<any> {
    return this.http.post("http://localhost:8080/usuarios/login", user);
  }
}