import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Instituto } from './models/instituto.model';
import { InstitutosService } from './institutos.service';
import { take } from 'rxjs/operators';

describe('InstitutosService', () => {
  let service: InstitutosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [InstitutosService]
    });

    // Inyectar el servicio y el HttpTestingController para poder realizar pruebas HTTP
    service = TestBed.inject(InstitutosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verificar que no haya solicitudes pendientes al final de cada prueba
    httpMock.verify();
  });

  it('El service se crea correctamente', () => {
    expect(service).toBeTruthy();
  });

});
