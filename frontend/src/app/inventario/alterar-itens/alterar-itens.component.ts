import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageNotificationService } from '@nuvem/primeng-components';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';
import { ItemService } from 'src/app/services/item.service';
import { Item } from '../../shared/models/item.model';

@Component({
    selector: 'app-alterar-itens',
    templateUrl: './alterar-itens.component.html',
    styleUrls: ['./alterar-itens.component.css']
})

export class AlterarItensComponent implements OnInit {

    displayBasic: boolean = false;
    item: Item;
    itemOriginal: Item;
    @Output() atualiozou = new EventEmitter();
    @BlockUI() blockUI: NgBlockUI;
    private _mensagemBlockUi: String = 'Carregando...';

    private form: FormGroup;
    private imagem: any;
    private usuarioLogado: any;

    constructor(
        private itensServices: ItemService,
        private formBuilder: FormBuilder,
        private notification: PageNotificationService
    ) {
    }

    abrir(item: Item) {
        this.desmontarImagem(item);
        this.displayBasic = true;
        this.form.patchValue(item);
    }

    alterar() {
        console.log(this.form.value);
        this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
        this.form.value.usuarioId = this.usuarioLogado.id;
        this.blockUI.start(this._mensagemBlockUi);
        this.itensServices.alterar(this.form.value).pipe(
            finalize(() => {
                this.blockUI.stop();
                this.fecharModal();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Item alterado com sucesso');
            },
            () => {
                this.notification.addErrorMessage('Erro ao alterar usuario');
            }
        );
        this.atualiozou.emit('');
    }

    desmontarImagem(item: Item) {
        let blob = item.imagem.split(',');
        this.form.patchValue({ imagem: blob[1] });
    }

    fecharModal() {
        this.displayBasic = false;
    }

    iniciarForm() {
        this.form = this.formBuilder.group({
            id: [null],
            descricao: [null, [Validators.required]],
            disponibilidade: [null, [Validators.required]],
            nome: [null, [Validators.required]],
            imagem: [null, [Validators.required]],
            usuarioId: [null, [Validators.required]],
            categoriaId: [null, [Validators.required]]
        });
    }

    load() {
        //Session storage salva os dados como string
        (sessionStorage.refresh == 'true' || !sessionStorage.refresh) && location.reload();
        sessionStorage.refresh = false;
    }

    montarImagem(item: Item) {
        this.itemOriginal = item;
        let formato = 'data:image/jpg;base64,';
        let img = formato.concat(item.imagem);
        item.imagem = img;
        return item;
    }

    ngOnInit(): void {
        this.iniciarForm();
    }

    uploadImagem(event) {
        let fileReader = new FileReader();
        let file = event.currentFiles[0];

        fileReader.onloadend = () => {
            this.imagem = fileReader.result;
            let blob = this.imagem.split(',');
            this.form.patchValue({ imagem: blob[1] });
        };
        fileReader.readAsDataURL(file);
    }
}
