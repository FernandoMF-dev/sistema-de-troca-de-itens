import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from './../../environments/environment';

import { UsuarioListagem } from './../shared/models/usuario-listagem.model';
import { Usuario } from './../shared/models/usuario.model';

@Injectable({
    providedIn: 'root'
})
export class UsuarioService {

    private api = `${ environment.apiUrl }/usuario/`;

    constructor(private http: HttpClient) {
    }

    alterar(usuario) {
        return this.http.put(this.api, usuario);
    }

    deletar(id) {
        return this.http.delete(this.api + id);
    }

    listar() {
        return this.http.get<UsuarioListagem[]>(this.api);
    }

    obterPorId(id) {
        return this.http.get<Usuario>(this.api + id);
    }

    salvar(usuario) {
        return this.http.post(this.api, usuario);
    }

}
