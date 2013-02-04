package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;

public interface EstoqueProdutoCotaJuramentadoRepository extends Repository<EstoqueProdutoCotaJuramentado, Long> {

	/**
	 * Obtém o estoque do produto da cota juramentado.
	 * 
	 * @param idProdutoEdicao - id do {@link ProdutoEdicao}
	 * @param idCota - id da {@link Cota}
	 * @param data - data
	 * 
	 * @return {@link EstoqueProdutoCotaJuramentado}
	 */
	EstoqueProdutoCotaJuramentado buscarEstoquePorProdutoECotaNaData(Long idProdutoEdicao, 
															  		 Long idCota,
															  		 Date data);
	
}
