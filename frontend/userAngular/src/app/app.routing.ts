import { RouterModule } from "@angular/router";
import { AppComponent } from "./app.component";
import { LoginComponent } from "./login/login.component";
import { ForgotPasswordComponent } from "./forgotPassword/forgotPassword.component";

const appRoutes = [
  { path: "", component: AppComponent, redirectTo: 'login', pathMatch: "full" },
  { path: "login", component: LoginComponent, pathMatch: "full" },
  { path: "forgotPassword", component: ForgotPasswordComponent, pathMatch: "full" },
];

// export const routing = RouterModule.forRoot([
//   { path: "", component: AppComponent, pathMatch: "full" },
//   { path: "login", component: LoginComponent, pathMatch: "full" },
//   { path: "forgotPassword", component: ForgotPasswordComponent, pathMatch: "full" },
// ]);

export const routing = RouterModule.forRoot([
    { path: "", redirectTo: 'login', pathMatch: "full" },
    { path: "login", component: LoginComponent, pathMatch: "full" },
    { path: "forgotPassword", component: ForgotPasswordComponent, pathMatch: "full" },
  ]);
