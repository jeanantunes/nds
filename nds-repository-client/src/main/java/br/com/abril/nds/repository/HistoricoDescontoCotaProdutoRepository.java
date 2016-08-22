package br.com.abril.nds.repository;

import java.util.List;

import org.hibernate.Query;

import br.com.abril.nds.dto.filtro.FiltroConsultaHistoricoDescontoDTO;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface HistoricoDescontoCotaProdutoRepository extends Repository<HistoricoDescontoCotaProdutoExcessao, Long> {
	
	/**
	 * Retorna o ultimo desconto valido
	 * 
	 * @param filtro
	 * 
	 * @return HistoricoDescontoCotaProdutoExcessao
	 */
	HistoricoDescontoCotaProdutoExcessao buscarUltimoDescontoValido(FiltroConsultaHistoricoDescontoDTO filtro);
	
	
	/**
	 * Obtém lista de HistoricoDescontoCotaProdutoExcessao
	 * 
	 * @param filtro
	 * 
	 * @return List - HistoricoDescontoCotaProdutoExcessao
	 */
	List<HistoricoDescontoCotaProdutoExcessao> buscarListaHistoricoDescontoCotaProdutoExcessao(FiltroConsultaHistoricoDescontoDTO filtro);


	HistoricoDescontoCotaProdutoExcessao buscarHistoricoDescontoCotaProdutoExcecaoao(Long idDescontoCotaProd);

	
}
