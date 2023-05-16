import { TestBed } from '@angular/core/testing';

import { usersService } from './users.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';


describe('UsersService', () => {
  let service: usersService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers:[
        usersService
      ]
    });
    service = TestBed.inject(usersService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('El servicio se crea correctamente', () => {
    expect(service).toBeTruthy();
  });
});
