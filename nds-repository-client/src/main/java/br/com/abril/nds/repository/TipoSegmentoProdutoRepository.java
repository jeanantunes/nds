package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 *  Interface que define as regras de implementação referentes a entidade TipoSegmentoProduto
 * {@link br.com.abril.nds.model.distribuicao.TipoSegmentoProduto} 
 * 
 * @author InfoA2 - Samuel Mendes
 *
 */
public interface TipoSegmentoProdutoRepository extends Repository<TipoSegmentoProduto, Long>{
	
	List<TipoSegmentoProduto> obterTipoSegmentoProdutoOrdenados(Ordenacao ordem);
	
}
