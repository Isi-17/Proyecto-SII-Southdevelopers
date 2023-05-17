import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule,
        FormsModule
      ],
      declarations: [ 
        LoginComponent 
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('Se crea correctamente el componente', () => {
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

  it('Debe aparecer el formulario vacío', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const emailInput = compiled.querySelector('#email') as HTMLInputElement;
    expect(emailInput.value).toEqual('');
    const passwordInput = compiled.querySelector('#password') as HTMLInputElement;
    expect(passwordInput.value).toEqual('');
    const loginBoton = compiled.querySelector('#login') as HTMLButtonElement;
    expect(loginBoton.value).toEqual('');
    const forgotPasswordBoton = compiled.querySelector('#forgotPassword') as HTMLButtonElement;
    expect(forgotPasswordBoton.value).toEqual('');
  });

  it('Se debe ejecutar forgotPassword() cuando el boton submit se pulsa el boton para recuperar la contraseña', () => {
    spyOn(component, 'forgotpassword').and.callThrough();
    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('#forgotPassword') as HTMLButtonElement;
    button.click();
    expect(component.forgotpassword).toHaveBeenCalled();
  });

  it('Se debe ejecutar login() cuando el boton submit se pulsa el boton de submit', () => {
    spyOn(component, 'login').and.callThrough();
    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('#login') as HTMLButtonElement;
    button.click();
    expect(component.login).toHaveBeenCalled();

  });
});
