package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.StatusEstudo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoRepository extends Repository<Estudo, Long> {
	
	Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao);
	
	void alterarStatusEstudos(List<Long> listIdEstudos, StatusEstudo status);
}
