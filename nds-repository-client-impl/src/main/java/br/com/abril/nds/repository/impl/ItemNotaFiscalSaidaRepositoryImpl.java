package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;

@Repository
public class ItemNotaFiscalSaidaRepositoryImpl extends
		AbstractRepositoryModel<ItemNotaFiscalSaida, Long> implements ItemNotaFiscalSaidaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalSaidaRepositoryImpl() {
		super(ItemNotaFiscalSaida.class);
	}
	

	public List<ItemDanfe> obterListaItemNotaFiscalSaidaDadosDanfe(Long idNotaFiscal) {
		
		StringBuffer hql = new StringBuffer("")
		
		.append(" select ")
		
		.append(" item.produtoEdicao.produto.codigo as codigoProduto, 	 ")
		.append(" item.produtoEdicao.produto.codigo as descricaoProduto, ")
		.append(" item.qtde as quantidadeProduto, 						 ")
		.append(" item.produtoEdicao.precoVenda as valorUnitarioProduto, ")
		.append(" (item.produtoEdicao.precoVenda * item.qtde) as valorTotalProduto, ")
		.append(" item.NCMProduto as NCMProduto,		")
		.append(" item.CFOPProduto as CFOPProduto,		")
		.append(" item.unidadeProduto as unidadeProduto,")
		.append(" item.CSTProduto as CSTProduto, 		")
		.append(" item.CSOSNProduto as CSOSNProduto, 	")
		.append(" item.baseCalculoProduto as baseCalculoProduto,	")
		.append(" item.aliquotaICMSProduto as aliquotaICMSProduto,	")
		.append(" item.valorICMSProduto as valorICMSProduto,	")
		.append(" item.aliquotaIPIProduto as aliquotaIPIProduto,	")
		.append(" item.valorIPIProduto as valorIPIProduto	")
		
		.append(" from ItemNotaFiscalSaida item ")
		
		.append(" where ")
		
		.append(" item.notaFiscal.id = :idNotaFiscal ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDanfe.class));
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
	
	
}
