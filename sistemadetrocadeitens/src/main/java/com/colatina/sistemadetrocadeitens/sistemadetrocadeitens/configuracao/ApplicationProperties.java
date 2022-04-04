package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.configuracao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "mail")
@Configuration
@Getter
@Setter
public class ApplicationProperties {
	private String enderecoRemetente;
	private String nomeRemetente;
}
