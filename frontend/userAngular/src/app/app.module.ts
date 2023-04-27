import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { routing } from './app.routing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";
import { CookieService } from "ngx-cookie-service";

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
