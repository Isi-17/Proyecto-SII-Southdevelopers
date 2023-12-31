import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { Instituto } from './models/instituto.model';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let instituto1: Instituto;
  let instituto2: Instituto;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        NgbModule,
        FormsModule
      ],
      declarations: [
        AppComponent
      ]
    }).compileComponents();


    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;

    instituto1 = {
      id: 1,
      nombre: 'IES 1',
      direccion1: 'Calle 1',
      direccion2: 'Calle 2',
      localidad: 'Localidad 1',
      codigoPostal: 11111,
      pais: 'Pais 1'
    }

    component.institutos.push(instituto1);

    instituto2 = {
      id: 2,
      nombre: 'IES 2',
      direccion1: 'Calle 1',
      direccion2: 'Calle 2',
      localidad: 'Localidad 1',
      codigoPostal: 11111,
      pais: 'Pais 1'
    }

    component.institutos.push(instituto2);

  });

  it('Deberamos obtener todos los institutos al darle a obtener todos, despues de buscar por id', () => {


    const compiled = fixture.nativeElement as HTMLElement;
    const searchBar = compiled.querySelector('#searchBar') as HTMLInputElement;
    searchBar.value = '1';
    const searchButton = compiled.querySelector('#searchId') as HTMLButtonElement;
    searchButton.click();

    fixture.detectChanges();

    var accordionButtons = compiled.querySelectorAll('button[ngbAccordionButton]');
    expect(accordionButtons.length).toBeGreaterThan(0);

    const firstButton = accordionButtons[0];
    expect(firstButton.textContent).toEqual(instituto1.nombre);

    const searchAllButton = compiled.querySelector('#searchAll') as HTMLButtonElement;
    searchAllButton.click();

    accordionButtons = compiled.querySelectorAll('button[ngbAccordionButton]');
    expect(accordionButtons.length).toBe(2);

  });

  it('Deberiamos obtener el instituto al buscar por el id', () => {

    const compiled = fixture.nativeElement as HTMLElement;
    const searchBar = compiled.querySelector('#searchBar') as HTMLInputElement;
    searchBar.value = '1';
    const searchButton = compiled.querySelector('#searchId') as HTMLButtonElement;
    searchButton.click();

    fixture.detectChanges();

    const accordionButtons = compiled.querySelectorAll('button[ngbAccordionButton]');
    expect(accordionButtons.length).toBeGreaterThan(0);

    const firstButton = accordionButtons[0];
    expect(firstButton.textContent).toEqual(instituto1.nombre);
  });

  it('Al darle al boton de eliminar el instituto se elimina', () => {

    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    const accordionButtons = compiled.querySelectorAll('button[ngbAccordionButton]');
    expect(accordionButtons.length).toBeGreaterThan(0);

    const firstButton = accordionButtons[0] as HTMLButtonElement;
    firstButton.click();

    fixture.detectChanges();

    fixture.whenStable().then(() => {
      const accordionBodies = document.querySelectorAll('div[ngbAccordionCollapse] > div[ngbAccordionBody]');
      expect(accordionBodies.length).toBeGreaterThan(0);

      const accordionBody = accordionBodies[0] as HTMLElement;
      expect(accordionBody).toBeTruthy();
      console.log(accordionBody);

      const deleteButton = accordionBody.querySelector('#deleteButton') as HTMLButtonElement;
      console.log(deleteButton);
      expect(deleteButton).toBeTruthy();
      deleteButton.click();
      expect(component.institutos.length).toBe(0);
    });

  });

  it('Se debe ejecutar addInstituto() cuando el boton add se pulsa', () => {

    spyOn(component, 'addInstituto').and.callThrough();
    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('#addButton') as HTMLButtonElement;
    button.click();
    expect(component.addInstituto).toHaveBeenCalled();

  });

  it('Se debe ejecutar obtainInstitutos() cuando el boton search se pulsa', () => {

    spyOn(component, 'obtainInstitutos').and.callThrough();
    const button = fixture.nativeElement.querySelector('#searchAll');
    button.click();
    expect(component.obtainInstitutos).toHaveBeenCalled();
    expect(component.institutos.length).toBeGreaterThan(0);
  });
});