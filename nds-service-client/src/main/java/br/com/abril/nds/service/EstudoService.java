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
	
	Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao);

	Estudo obterEstudo(Long id);

}
