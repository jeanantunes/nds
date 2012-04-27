package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;

@Repository
public class ItemNotaFiscalSaidaRepositoryImpl extends
		AbstractRepository<ItemNotaFiscalSaida, Long> implements ItemNotaFiscalSaidaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalSaidaRepositoryImpl() {
		super(ItemNotaFiscalSaida.class);
	}
	

	public List<ItemDanfe> obterListaItemNotaFiscalSaidaDadosDanfe(Long idNotaFiscal) {
		
		StringBuffer hql = new StringBuffer("")
		
		.append(" select ")
		
		.append(" item.produtoEdicao.produto.codigo as codigoProduto, 					")
		.append(" item.produtoEdicao.produto.codigo as descricaoProduto,   				")
		.append(" item.qtde as quantidadeProduto, ")
		.append(" item.produtoEdicao.precoVenda as valorUnitarioProduto, 				")
		.append(" sum(item.produtoEdicao.precoVenda * item.qtde) as valorTotalProduto 	")
		
		.append(" from ItemNotaFiscalSaida item ")

		.append(" where ")
		
		.append(" item.notaFiscal.id = :idNotaFiscal ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDanfe.class));
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
	
	
}
