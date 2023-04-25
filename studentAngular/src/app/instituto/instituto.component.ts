import { Component, Input, inject } from '@angular/core';
import { Instituto } from '../models/instituto.model';

@Component({
  selector: 'app-instituto',
  templateUrl: './instituto.component.html',
  styleUrls: ['./instituto.component.css']
})
export class InstitutoComponent {

  @Input() instituto: Instituto = {
    id: 0,
    nombre: '',
    direccion1: '',
    direccion2: '',
    localidad: '',
    codigoPostal: 0,
    pais: ''
  };

}
