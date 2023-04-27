import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { routing } from './app.routing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";
import { CookieService } from 'ngx-cookie-service';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { forgotPasswordComponent } from './forgotPassword/forgotPassword.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    forgotPasswordComponent
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
