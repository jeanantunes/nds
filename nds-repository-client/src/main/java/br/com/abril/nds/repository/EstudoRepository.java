package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.planejamento.Estudo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoRepository extends Repository<Estudo, Long> {
	
	Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao);

}