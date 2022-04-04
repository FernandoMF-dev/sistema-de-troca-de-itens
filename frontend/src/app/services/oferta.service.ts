import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { OfertaListagem } from '../shared/models/oferta-listagem.model';
import { Oferta } from '../shared/models/oferta.model';

import { environment } from './../../environments/environment.prod';

@Injectable({
    providedIn: 'root'
})
export class OfertaService {
    private api = `${ environment.apiUrl }/oferta/`;

    constructor(private http: HttpClient) {
    }

    aceitar(id): Observable<Oferta> {
        return this.http.patch<Oferta>(this.api + 'aceitar/' + id, null);
    }

    alterar(oferta): Observable<Oferta> {
        return this.http.put<Oferta>(this.api, oferta);
    }

    cancelar(id): Observable<Oferta> {
        return this.http.patch<Oferta>(this.api + 'cancelar/' + id, null);
    }

    deletar(id) {
        return this.http.delete(this.api + id);
    }

    listar(): Observable<OfertaListagem[]> {
        return this.http.get<OfertaListagem[]>(this.api);
    }

    listarPorOfertado(id): Observable<OfertaListagem[]> {
        return this.http.get<OfertaListagem[]>(this.api + 'ofertado/' + id);
    }

    listarPorOfertante(id): Observable<OfertaListagem[]> {
        return this.http.get<OfertaListagem[]>(this.api + 'ofertante/' + id);
    }

    obterPorId(id): Observable<Oferta> {
        return this.http.get<Oferta>(this.api + id);
    }

    recusar(id): Observable<Oferta> {
        return this.http.patch<Oferta>(this.api + 'recusar/' + id, null);
    }

    salvar(oferta): Observable<Oferta> {
        return this.http.post<Oferta>(this.api, oferta);
    }
}
