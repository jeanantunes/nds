package br.com.abril.nds.service;

import java.util.Date;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.ControleBaixaBancaria}
 * 
 * @author Discover Technology
 */
public interface ControleBaixaBancariaService {
	
	/**
	 * Altera o controle da baixa bancária.
	 * 
	 * @param statusControle - status de controle da baixa
	 * @param dataOperacao - data de operação
	 * @param usuario - usuário
	 * 
	 * @return controle baixa bancária
	 */
	ControleBaixaBancaria alterarControleBaixa(StatusControle statusControle,
			  								   Date dataOperacao, Usuario usuario);
	
}
