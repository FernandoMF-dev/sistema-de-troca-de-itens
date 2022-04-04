import { Categoria } from './categoria.model';
import { Usuario } from './usuario.model';

export class ItemAmostra {
    id: number;
    descricao: string;
    disponibilidade: boolean;
    nome: string;
    imagem: string;
    usuario: Usuario;
    categoria: Categoria;
}
