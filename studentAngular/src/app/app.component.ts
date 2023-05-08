import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Instituto } from './models/instituto.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioInstitutoComponent } from './formulario-instituto/formulario-instituto.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'studentAngular';
  http = inject(HttpClient);
  institutos: Instituto[] = [];
  searchValue!: number | null;

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
    this.obtainInstitutos();
  }

  obtainInstitutos() {
    this.http.get<Instituto[]>('http://localhost:8080/institutos')
      .subscribe((data) => {
        this.institutos = data;
      });
  }

  deleteInstituto(id: number) {
    this.http.delete('http://localhost:8080/institutos/' + id)
    .subscribe(() => {
      this.obtainInstitutos();
    }, (error) => {
        if(error.status === 409) {
          alert('No se puede eliminar el instituto porque hay estudiantes asociados a él.');
        }
    });
  }

  addInstituto(): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.instituto = {id: 0, nombre: '', direccion1: '', 
    direccion2: '', localidad: '', codigoPostal: 0, pais: ''};
    ref.result.then((instituto: Instituto) => {
      this.http.post<Instituto>('http://localhost:8080/institutos', instituto)
      .subscribe(() => {
        this.obtainInstitutos();
      });
    }, (reason) => {});

  }

  updateInstituto(instituto: Instituto): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.instituto = {...instituto};
    ref.result.then((instituto: Instituto) => {
      this.http.put<Instituto>('http://localhost:8080/institutos/'+instituto.id, instituto)
      .subscribe(() => {
        this.obtainInstitutos();
      });
    }, (reason) => {});

  }

  searchInstituto(id: number | null): void {
    if(id === null || id === undefined) {
      this.obtainInstitutos();
    } else {
        this.http.get<Instituto>('http://localhost:8080/institutos/' + id)
        .subscribe((data) => {
          this.institutos = [];
          this.institutos.push(data);
          this.searchValue = null;
        }, (error) => {
          if(error.status === 404) {
            alert('No se han encontrado institutos con ese id.');
            this.searchValue = null;
          } else if (error.status === 400) {
            alert('El id introducido no es válido.');
            this.searchValue = null;
          }
        });
    }


  }


}
