import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioInstitutoComponent } from './formulario-instituto.component';
import { FormsModule } from '@angular/forms';
import { Instituto } from '../models/instituto.model';

describe('FormularioInstitutoComponent', () => {
  let component: FormularioInstitutoComponent;
  let fixture: ComponentFixture<FormularioInstitutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [FormularioInstitutoComponent],
      providers: [NgbActiveModal]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormularioInstitutoComponent);
    component = fixture.componentInstance;
  });

  it('Debe generar correctamente el formulario con los inputs y botones necesarios', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    const nombreInput = compiled.querySelector('#nombre') as HTMLInputElement;
    expect(nombreInput).toBeTruthy();
    const direccion1Input = compiled.querySelector('#direccion1') as HTMLInputElement;
    expect(direccion1Input).toBeTruthy();
    const direccion2Input = compiled.querySelector('#direccion2') as HTMLInputElement;
    expect(direccion2Input).toBeTruthy();
    const localidadInput = compiled.querySelector('#localidad') as HTMLInputElement;
    expect(localidadInput).toBeTruthy();
    const codigoPostalInput = compiled.querySelector('#codigoPostal') as HTMLInputElement;
    expect(codigoPostalInput).toBeTruthy();
    const paisInput = compiled.querySelector('#pais') as HTMLInputElement;
    expect(paisInput).toBeTruthy();
    const añadirBoton = compiled.querySelector('#guardar') as HTMLButtonElement;
    expect(añadirBoton).toBeTruthy();
    const cancelarBoton = compiled.querySelector('#cerrar') as HTMLButtonElement;
    expect(cancelarBoton).toBeTruthy();
  });


  it('Debe aparecer el formulario vacío y con el texto añadir instituto', () => {
    component.accion = "Añadir";
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const nombre = compiled.querySelector('#nombre') as HTMLInputElement;
    const direccion1 = compiled.querySelector('#direccion1') as HTMLInputElement;
    const direccion2 = compiled.querySelector('#direccion2') as HTMLInputElement;
    const localidad = compiled.querySelector('#localidad') as HTMLInputElement;
    const codigoPostal = compiled.querySelector('#codigoPostal') as HTMLInputElement;
    const pais = compiled.querySelector('#pais') as HTMLInputElement;

    expect(nombre.value).toEqual('');
    expect(direccion1.value).toEqual('');
    expect(direccion2.value).toEqual('');
    expect(localidad.value).toEqual('');
    expect(codigoPostal.value).toEqual('');
    expect(pais.value).toEqual('');

  });


  it('Debe aparecer el formulario con los datos del instituto y con el texto editar instituto', (done: DoneFn) => {
    component.accion = "Editar";
    const instituto: Instituto = {
      id: 1, nombre: 'IES malaga', direccion1: 'Avenida Carlos de haya',
      direccion2: '', localidad: 'Malaga', codigoPostal: 29010, pais: 'España'
    };
    component.instituto = instituto;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const nombre = compiled.querySelector('#nombre') as HTMLInputElement;
    const direccion1 = compiled.querySelector('#direccion1') as HTMLInputElement;
    const direccion2 = compiled.querySelector('#direccion2') as HTMLInputElement;
    const localidad = compiled.querySelector('#localidad') as HTMLInputElement;
    const codigoPostal = compiled.querySelector('#codigoPostal') as HTMLInputElement;
    const pais = compiled.querySelector('#pais') as HTMLInputElement;
    fixture.whenStable().then(() => {
      expect(nombre.value).toEqual(instituto.nombre);
      expect(direccion1.value).toEqual(instituto.direccion1);
      expect(direccion2.value).toEqual(instituto.direccion2);
      expect(localidad.value).toEqual(instituto.localidad);
      expect(codigoPostal.value).toEqual(instituto.codigoPostal.toString());
      expect(pais.value).toEqual(instituto.pais);
      done();
    });
  });
});
