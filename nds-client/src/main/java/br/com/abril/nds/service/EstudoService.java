package br.com.abril.nds.service;

import java.util.Date;

import br.com.abril.nds.model.planejamento.Estudo;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoService {
	
	Estudo obterEstudoDoLancamentoMaisProximo(Date dataReferencia, String codigoProduto, Long numeroEdicao);

}
