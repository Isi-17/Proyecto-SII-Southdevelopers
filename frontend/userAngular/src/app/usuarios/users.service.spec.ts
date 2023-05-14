import { TestBed } from '@angular/core/testing';

import { usersService } from './users.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('UsersService', () => {
  let service: usersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      declarations:[
        usersService
      ]
    });
    service = TestBed.inject(usersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
