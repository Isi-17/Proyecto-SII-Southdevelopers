import { RouterModule } from "@angular/router";
import { AppComponent } from "./app.component";
import { LoginComponent } from "./login/login.component";

const appRoutes = [
  { path: "", component: AppComponent, pathMatch: "full" },
  { path: "login", component: LoginComponent, pathMatch: "full" },
];

export const routing = RouterModule.forRoot([
    { path: "", component: AppComponent, pathMatch: "full" },
    { path: "login", component: LoginComponent, pathMatch: "full" },
  ]);