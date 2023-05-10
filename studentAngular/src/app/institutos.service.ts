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

  // obtainInstitutos(): Instituto[] {
  //  this.http.get<Instituto[]>('http://localhost:8080/institutos')
  //     .subscribe((data) => {
  //       this.institutos = data;
  //       return this.institutos;
  //     });
  //   return this.institutos;
  // }

  obtainInstitutos(): Observable<Instituto[]> {
    return this.http.get<Instituto[]>('http://localhost:8080/institutos');
  }

  deleteInstituto(id: number) {
    this.http.delete('http://localhost:8080/institutos/' + id)
    .subscribe(() => {
    }, (error) => {
        if(error.status === 409) {
          alert('No se puede eliminar el instituto porque hay estudiantes asociados a él.');
        }
    });
  }

  addInstituto(instituto: Instituto): Observable<Instituto> {
    return this.http.post<Instituto>('http://localhost:8080/institutos', instituto);
  }

  updateInstituto(instituto: Instituto): void {

    this.http.put<Instituto>('http://localhost:8080/institutos/'+instituto.id, instituto)
      .subscribe(() => {
    });

  }

  searchInstituto(id: number | null): Instituto[] {

      this.http.get<Instituto>('http://localhost:8080/institutos/' + id)
      .subscribe((data) => {
          this.institutos = [];
          this.institutos.push(data);
          return this.institutos;
        }, (error) => {
          if(error.status === 404) {
            alert('No se han encontrado institutos con ese id.');
          } else if (error.status === 400) {
            alert('El id introducido no es válido.');
          }
        });
        return this.institutos;
   }

}