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

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
    this.http.get<Instituto[]>('http://localhost:8080/institutos')
      .subscribe((data) => {
        this.institutos = data;
      });
  }

  deleteInstituto(id: number) {
    this.http.delete('http://localhost:8080/institutos/' + id)
    .subscribe(() => {
      this.http.get<Instituto[]>('http://localhost:8080/institutos')
        .subscribe((data) => {
          this.institutos = data;
        });
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
        this.http.get<Instituto[]>('http://localhost:8080/institutos')
        .subscribe((data) => {
          this.institutos = data;
        });
      });
    }, (reason) => {});

  }

  updateInstituto(instituto: Instituto): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.instituto = instituto;
    ref.result.then((instituto: Instituto) => {
      this.http.put<Instituto>('http://localhost:8080/institutos/'+instituto.id, instituto)
      .subscribe(() => {
        this.http.get<Instituto[]>('http://localhost:8080/institutos')
        .subscribe((data) => {
          this.institutos = data;
        });
      });
    }, (reason) => {});

  }


}
