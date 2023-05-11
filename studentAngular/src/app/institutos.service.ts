import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Instituto } from './models/instituto.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstitutosService {

  institutos: Instituto[] = [];

  http = inject(HttpClient);

  constructor() { }

  obtainInstitutos(): Observable<Instituto[]> {
    return this.http.get<Instituto[]>('http://localhost:8080/institutos');
  }

  deleteInstituto(id: number): Observable<any> {
    return this.http.delete('http://localhost:8080/institutos/' + id);
  }

  addInstituto(instituto: Instituto): Observable<Instituto> {
    return this.http.post<Instituto>('http://localhost:8080/institutos', instituto);
  }


  updateInstituto(instituto: Instituto): Observable<Instituto> {
    return this.http.put<Instituto>('http://localhost:8080/institutos/' + instituto.id, instituto);
  }

  searchInstituto(id: number | null): Observable<Instituto> {
    return this.http.get<Instituto>('http://localhost:8080/institutos/' + id);
  }

}