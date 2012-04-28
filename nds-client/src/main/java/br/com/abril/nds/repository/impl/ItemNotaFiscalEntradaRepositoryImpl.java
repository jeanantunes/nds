package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemNotaFiscalEntradaRepositoryImpl extends
		AbstractRepository<ItemNotaFiscalEntrada, Long> implements ItemNotaFiscalEntradaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalEntradaRepositoryImpl() {
		super(ItemNotaFiscalEntrada.class);
	}
	
	public List<ItemNotaFiscalEntrada> buscarItensPorIdNota(Long idNotaFiscal){
		
		String hql = "from ItemNotaFiscalEntrada item "				
				+ " where item.notaFiscal.id = :idNotaFiscal";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idNotaFiscal", idNotaFiscal);

		return query.list();
	}

	public List<ItemDanfe> obterListaItemNotaFiscalEntradaDadosDanfe(Long idNotaFiscal) {
		
		
		StringBuffer hql = new StringBuffer("")
		
		.append(" select ")
		
		.append(" item.produtoEdicao.produto.codigo as codigoProduto, 					")
		.append(" item.produtoEdicao.produto.codigo as descricaoProduto,   				")
		.append(" item.qtde as quantidadeProduto, ")
		.append(" item.produtoEdicao.precoVenda as valorUnitarioProduto, 				")
		.append(" sum(item.produtoEdicao.precoVenda * item.qtde) as valorTotalProduto 	")
		
		.append(" from ItemNotaFiscalEntrada item ")

		.append(" where ")
		
		.append(" item.notaFiscal.id = :idNotaFiscal ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDanfe.class));
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
	
}
