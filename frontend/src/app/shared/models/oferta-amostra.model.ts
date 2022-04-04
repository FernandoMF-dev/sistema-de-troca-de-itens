import { Item } from 'src/app/shared/models/item.model';
import { Usuario } from 'src/app/shared/models/usuario.model';

export class OfertaAmostra {
    id: number;
    item: Item;
    usuarioOfertante: Usuario;
    usuarioDisponivel: Usuario;
    itensOfertados: number[];
}
