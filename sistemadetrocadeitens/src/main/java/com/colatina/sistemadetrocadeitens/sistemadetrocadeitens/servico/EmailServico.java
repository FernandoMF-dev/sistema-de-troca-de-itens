package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.configuracao.ApplicationProperties;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.EmailDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServico {

	private final JavaMailSender javaMailSender;
	private final ApplicationProperties applicationProperties;

	public void sendMail(EmailDto emailDto) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			message.setFrom(applicationProperties.getEnderecoRemetente(),
					applicationProperties.getNomeRemetente());
			message.setTo(emailDto.getDestinatario());
			message.setSubject(emailDto.getAssunto());
			if (emailDto.getCopias() != null) {
				for (String s : emailDto.getCopias()) {
					message.addCc(s);
				}
			}
			message.setText(emailDto.getTexto(), true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException("ERRO AO ENVIAR E-MAIL");
		}
	}

	public void enviarEmailNovoUsuario(Usuario usuario) {
		EmailDto emailDto = new EmailDto();
		emailDto.setAssunto("SISTEMA DE TROCA DE ITENS");
		emailDto.setDestinatario(usuario.getEmail());
		emailDto.setTexto("O seu cadastro no SISTEMA DE TROCA DE ITENS foi realizado com sucesso!" +
				"\nPara realizar a????es no sistema, todo usuario precisa informar seu TOKEN, que ?? ??nico para cada um." +
				"\nO seu TOKEN ??: | " + usuario.getToken() + " |. Guarde-o com cuidado, voc?? vai precisar.");

		sendMail(emailDto);
	}

	public void enviarEmailNovaOferta(OfertaDto ofertaDto, ItemDto itemDesejado, UsuarioDto usuarioDtoDisponivel, UsuarioDto usuarioDtoOfertante) {
		EmailDto emailDto = new EmailDto();
		emailDto.setAssunto("UM DE SEUS PRODUTOS RECEBEU UMA NOVA OFERTA!");
		emailDto.setDestinatario(usuarioDtoDisponivel.getEmail());

		emailDto.setTexto("O senhor(a) " + usuarioDtoOfertante.getNome() +
				" ofereceu " + ofertaDto.getItensOfertados().size() +
				" produto(s) pelo seu produto: " + itemDesejado.getNome());

		sendMail(emailDto);
	}

	public void enviarEmailOfertaAceita(UsuarioDto usuarioDtoDisponivel, UsuarioDto usuarioDtoOfertante) {
		EmailDto emailDto = new EmailDto();
		emailDto.setAssunto("UMA DE SUAS OFERTAS FOI ACEITA!");
		emailDto.setDestinatario(usuarioDtoOfertante.getEmail());

		emailDto.setTexto("O senhor(a) " + usuarioDtoDisponivel.getNome() +
				" ACEITOU a sua oferta feita." +
				"\n\n\tAVISO: todas as outras ofertas de troca envolvendo qualquer um dos itens que voc?? ofereceu" +
				" (tenham elas sido feitas desejando seu item ou voc?? oferendo-o para em outra troca) foram automaticamente canceladas." +
				" Essa opera??ao n??o pode ser revertida (pelo menos n??o diretamente).");

		sendMail(emailDto);
	}

	public void enviarEmailOfertaCancelada(UsuarioDto usuarioDtoDisponivel, UsuarioDto usuarioDtoOfertante) {
		EmailDto emailDto = new EmailDto();
		emailDto.setAssunto("UMA DE SUAS OFERTAS FOI CANCELADA!");
		emailDto.setDestinatario(usuarioDtoOfertante.getEmail());

		emailDto.setTexto("A oferta que voc?? fez para do(a) senhor(a) " + usuarioDtoDisponivel.getNome() + " foi CANCELADA." +
				"\nCaso n??o tenha sido voc?? mesmo n??o tenha pedido o cancelamento," +
				" ?? provavel que um dos itens envolvidas na oferta tenha sido trocado em outra oferta.");

		sendMail(emailDto);
	}

	public void enviarEmailOfertaRecusada(UsuarioDto usuarioDtoDisponivel, UsuarioDto usuarioDtoOfertante) {
		EmailDto emailDto = new EmailDto();
		emailDto.setAssunto("UMA DE SUAS OFERTAS FOI RECUSADA!");
		emailDto.setDestinatario(usuarioDtoOfertante.getEmail());

		emailDto.setTexto("Lamentamos " + usuarioDtoOfertante.getNome() +
				", mas o(a) senhor(a) " + usuarioDtoDisponivel.getNome() +
				" RECUSOU a sua oferta. Uma pena.");

		sendMail(emailDto);
	}
}
