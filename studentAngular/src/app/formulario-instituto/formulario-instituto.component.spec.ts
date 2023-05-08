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
      imports:[FormsModule],
      declarations: [ FormularioInstitutoComponent ],
      providers: [NgbActiveModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioInstitutoComponent);
    component = fixture.componentInstance;
  });

  it('Debe aparecer el formulario vacío y con el texto añadir instituto', () => {
    component.accion = "Añadir";
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect((compiled.querySelector('#nombre') as HTMLInputElement).value).toEqual('');
    expect((compiled.querySelector('#direccion1') as HTMLInputElement).value).toEqual('');
    expect((compiled.querySelector('#direccion2') as HTMLInputElement).value).toEqual('');
    expect((compiled.querySelector('#localidad') as HTMLInputElement).value).toEqual('');
    expect((compiled.querySelector('#codigoPostal') as HTMLInputElement).value).toEqual('');
    expect((compiled.querySelector('#pais') as HTMLInputElement).value).toEqual('');

  });
  it('Debe aparecer el formulario con los datos del instituto y con el texto editar instituto', (done: DoneFn) => {
    component.accion = "Editar";
    const instituto: Instituto = {id: 1, nombre: 'IES malaga', direccion1: 'Avenida Carlos de haya', 
    direccion2: '', localidad: 'Malaga', codigoPostal: 29010, pais: 'España'};
    component.instituto = instituto;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    alert((compiled.querySelector('#nombre') as HTMLInputElement));
    const nombre = (compiled.querySelector('#nombre') as HTMLInputElement);
    alert(nombre.value);
    expect(nombre.value).toEqual(instituto.nombre);
    expect((compiled.querySelector('#direccion1') as HTMLInputElement).value).toEqual(instituto.direccion1);
    expect((compiled.querySelector('#direccion2') as HTMLInputElement).value).toEqual(instituto.direccion2);
    expect((compiled.querySelector('#localidad') as HTMLInputElement).value).toEqual(instituto.localidad);
    expect((compiled.querySelector('#codigoPostal') as HTMLInputElement).value).toEqual(instituto.codigoPostal.toString());
    expect((compiled.querySelector('#pais') as HTMLInputElement).value).toEqual(instituto.pais);
    done();
  });
});
