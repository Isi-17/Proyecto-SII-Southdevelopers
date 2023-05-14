import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Instituto } from './models/instituto.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioInstitutoComponent } from './formulario-instituto/formulario-instituto.component';
import { InstitutosService } from './institutos.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

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

  constructor(private institutosService: InstitutosService, private modalService: NgbModal) { }

  ngOnInit() {
    this.institutosService.obtainInstitutos().subscribe((data) => {
      this.institutos = data;
    });
  }

  obtainInstitutos(): void {
    this.institutosService.obtainInstitutos().subscribe((data) => {
      this.institutos = data;
    });
  }

  deleteInstituto(id: number) {
    this.institutosService.deleteInstituto(id)
      .subscribe(() => {
        this.institutosService.obtainInstitutos().subscribe((data) => {
          this.institutos = data;
        });
      }, (error: any) => {
        if (error.status === 409) {
          alert('No se puede eliminar el instituto porque hay estudiantes asociados a él.');
        }
      });;

  }

  addInstituto(): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.instituto = {
      id: 0, nombre: '', direccion1: '',
      direccion2: '', localidad: '', codigoPostal: 0, pais: ''
    };
    ref.result.then((instituto: Instituto) => {
      this.institutosService.addInstituto(instituto).subscribe(() => {

        this.institutosService.obtainInstitutos().subscribe((data) => {
          this.institutos = data;
        });

      });
    }, (reason) => { });
  }

  updateInstituto(instituto: Instituto): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.instituto = { ...instituto };
    ref.result.then((instituto: Instituto) => {
      this.institutosService.updateInstituto(instituto).subscribe(() => {

        this.institutosService.obtainInstitutos().subscribe((data) => {
          this.institutos = data;
        });

      });
    }, (reason) => { });

  }

  searchInstituto(id: number | null | undefined): void {
    if (id === null || id === undefined) {
      this.institutosService.obtainInstitutos().subscribe((data) => {
        this.institutos = data;
      });
    } else {

      this.institutosService.searchInstituto(id)
        .subscribe((data) => {
          this.institutos = [];
          this.institutos.push(data);
        }, (error) => {
          if (error.status === 404) {
            alert('No se han encontrado institutos con ese id.');
          } else if (error.status === 400) {
            alert('El id introducido no es válido.');
          }
        });

      this.searchValue = null;
    }

  }


}