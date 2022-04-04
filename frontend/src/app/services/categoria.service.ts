import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';

import { Categoria } from './../shared/models/categoria.model';

@Injectable({
    providedIn: 'root'
})
export class CategoriaService {

    private api = `${ environment.apiUrl }/categoria/`;

    constructor(private http: HttpClient) {
    }

    buscarTodos(): Observable<Categoria[]> {
        return this.http.get<Categoria[]>(this.api);
    }

    obterPorId(id): Observable<Categoria> {
        return this.http.get<Categoria>(this.api + id);
    }
}
