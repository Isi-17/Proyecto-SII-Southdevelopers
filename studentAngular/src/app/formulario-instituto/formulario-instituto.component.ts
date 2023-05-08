import { Component } from '@angular/core';
import { Instituto } from '../models/instituto.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-formulario-instituto',
  templateUrl: './formulario-instituto.component.html',
  styleUrls: ['./formulario-instituto.component.css']
})
export class FormularioInstitutoComponent {

  accion: "Añadir" | "Editar" | undefined;
  instituto: Instituto = {id: 0, nombre: '', direccion1: '', 
    direccion2: '', localidad: '', codigoPostal: 0, pais: ''};


  constructor(public modal: NgbActiveModal) { }

  guardarInstituto(): void {
    this.modal.close(this.instituto);
  }
}
