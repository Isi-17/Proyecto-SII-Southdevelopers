import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      declarations: [ LoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Debe generar correctamente la pagina de login', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    const emailInput = compiled.querySelector('#email') as HTMLInputElement;
    expect(emailInput).toBeTruthy();
    const passwordInput = compiled.querySelector('#password') as HTMLInputElement;
    expect(passwordInput).toBeTruthy();
    const loginBoton = compiled.querySelector('#login') as HTMLButtonElement;
    expect(loginBoton).toBeTruthy();
    const forgotPasswordBoton = compiled.querySelector('#forgotPassword') as HTMLButtonElement;
    expect(forgotPasswordBoton).toBeTruthy();
  });
});
