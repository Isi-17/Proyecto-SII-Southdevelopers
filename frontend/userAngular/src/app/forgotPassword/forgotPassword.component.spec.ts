import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ForgotPasswordComponent } from './forgotPassword.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('forgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule,
        FormsModule
      ],
      declarations: [ 
        ForgotPasswordComponent
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Se crea correctamente el componente', () => {
    expect(component).toBeTruthy();
  });

  it('Debe generar correctamente la pagina de recuperacion de contraseña', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    const emailInput = compiled.querySelector('#email') as HTMLInputElement;
    expect(emailInput).toBeTruthy();
    const submitButton  = compiled.querySelector('#submit') as HTMLButtonElement;
    expect(submitButton).toBeTruthy();
  });

  it('Debe aparecer el formulario vacío', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const nombre = compiled.querySelector('#email') as HTMLInputElement;

    expect(nombre.value).toEqual('');
  });

});
