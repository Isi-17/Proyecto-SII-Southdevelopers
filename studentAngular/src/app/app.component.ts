import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Instituto } from './models/instituto.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioInstitutoComponent } from './formulario-instituto/formulario-instituto.component';
import { InstitutosService } from './institutos.service';

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
    this.institutosService.deleteInstituto(id);
    this.institutosService.obtainInstitutos().subscribe((data) => {
      this.institutos = data;
    });  
  }

  addInstituto(): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.instituto = {id: 0, nombre: '', direccion1: '', 
    direccion2: '', localidad: '', codigoPostal: 0, pais: ''};
    ref.result.then((instituto: Instituto) => {
      this.institutosService.addInstituto(instituto);
      this.institutosService.obtainInstitutos().subscribe((data) => {
        this.institutos = data;
      });
    }, (reason) => {});

  }

  updateInstituto(instituto: Instituto): void {
    let ref = this.modalService.open(FormularioInstitutoComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.instituto = {...instituto};
    ref.result.then((instituto: Instituto) => {
      this.institutosService.updateInstituto(instituto);
      this.institutosService.obtainInstitutos().subscribe((data) => {
        this.institutos = data;
      });
    }, (reason) => {});

  }

  searchInstituto(id: number | null): void {
    if(id === null || id === undefined) {
      this.institutosService.obtainInstitutos();
    } else {
        this.institutos = this.institutosService.searchInstituto(id);
        this.searchValue = null;
    }

  }


}
