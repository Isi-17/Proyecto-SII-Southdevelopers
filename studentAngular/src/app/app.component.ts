import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Instituto } from './models/instituto.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'studentAngular';
  http = inject(HttpClient);
  institutos: Instituto[] = [];

  ngOnInit() {
    this.http.get<Instituto[]>('http://localhost:8080/institutos')
      .subscribe((data) => {
        this.institutos = data;
      });
  }

  deleteInstituto(id: number) {
    this.http.delete('http://localhost:8080/institutos/' + id);
  }

  

}
