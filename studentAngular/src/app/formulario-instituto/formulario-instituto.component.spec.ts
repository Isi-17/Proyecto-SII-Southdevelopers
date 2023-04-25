import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioInstitutoComponent } from './formulario-instituto.component';

describe('FormularioInstitutoComponent', () => {
  let component: FormularioInstitutoComponent;
  let fixture: ComponentFixture<FormularioInstitutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormularioInstitutoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioInstitutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
