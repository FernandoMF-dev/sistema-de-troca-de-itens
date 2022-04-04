import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';
import { Item } from './../shared/models/item.model';

@Injectable({
    providedIn: 'root'
})
export class ItemService {
    private api = `${ environment.apiUrl }/item/`;

    constructor(private http: HttpClient) {
    }

    alterar(item): Observable<Item> {
        return this.http.put<Item>(this.api, item);
    }

    deletar(id) {
        return this.http.delete(this.api + id);
    }

    inventarioListar(usuarioId): Observable<Item[]> {
        return this.http.get<Item[]>(this.api + 'inventario-' + usuarioId);
    }

    listar(): Observable<Item[]> {
        return this.http.get<Item[]>(this.api);
    }

    listarDisponivel(): Observable<Item[]> {
        return this.http.get<Item[]>(this.api + 'disponivel/');
    }

    listarDisponivelExcetoUsuario(id): Observable<Item[]> {
        return this.http.get<Item[]>(this.api + 'disponivel/' + id);
    }

    listarPorDono(id): Observable<Item[]> {
        return this.http.get<Item[]>(this.api + 'dono/' + id);
    }

    obterPorId(id): Observable<Item> {
        return this.http.get<Item>(this.api + id);
    }

    salvar(item): Observable<Item> {
        return this.http.post<Item>(this.api, item);
    }

}
