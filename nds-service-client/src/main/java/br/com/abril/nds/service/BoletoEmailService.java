package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.financeiro.BoletoEmail;



/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.BoletoEmail}
 * 
 * @author Discover Technology
 */
public interface BoletoEmailService {

	/**
	 * Salva controle de emissao de boletos/cobrancas por email
	 * 
	 * @param listaNossoNumeroEnvioEmail
	 */
	void salvarBoletoEmail(Set<String> listaNossoNumeroEnvioEmail);

	/**
	 * Obtem todos os boletos/cobrancas pendentes de envio por email
	 * 
	 * @return List<BoletoEmail>
	 */
	List<BoletoEmail> buscarTodos();

	/**
	 * Envia Cobrança por email - Controle de Envio de boletos/cobrancas
	 * 
	 * @param boletoEmail
	 */
	void enviarBoletoEmail(BoletoEmail boletoEmail);
}
