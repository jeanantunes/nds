
package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;

/**
 * 
 * @author Gustavo
 *
 */
@Repository
public class RecebimentoFisicoRepositoryImpl extends AbstractRepositoryModel<RecebimentoFisico, Long> implements RecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public RecebimentoFisicoRepositoryImpl() {
		super(RecebimentoFisico.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select new ");
		
		hql.append(RecebimentoFisicoDTO.class.getCanonicalName());
		
		hql.append(" ( 	itemNotaFiscal.id, 													");
		hql.append(" 	itemRecebimentoFisico.id, 											");
		hql.append(" 	produto.codigo, 													");
		hql.append("  	produto.nome, 														");
		hql.append("  	produtoEdicao.origem, 												");
		hql.append("  	produtoEdicao.numeroEdicao, 										");
		hql.append("  	produtoEdicao.id, 													");
		hql.append(" 	produtoEdicao.precoVenda as precoCapa,							    ");
		hql.append(" 	itemNotaFiscal.preco as precoItem,								    ");
		hql.append(" 	itemNotaFiscal.desconto as percentualDesconto,						");
		hql.append(" 	itemNotaFiscal.qtde, 												");
		hql.append(" 	itemRecebimentoFisico.qtdeFisico, 									");
		hql.append("	produtoEdicao.pacotePadrao, 										");
		hql.append("	produtoEdicao.peso, 												");
		hql.append(" 	itemNotaFiscal.dataLancamento, 										");
		hql.append(" 	itemNotaFiscal.dataRecolhimento, 									");
		hql.append(" 	itemNotaFiscal.tipoLancamento, 										");
		hql.append(" 	diferenca.qtde,  													");
		hql.append(" 	diferenca.tipoDiferenca,  											");
		hql.append(" 	itemNotaFiscal.origem  )											");
		
		hql.append(" from ");

		hql.append(" ItemNotaFiscalEntrada itemNotaFiscal ");

		hql.append(" join itemNotaFiscal.produtoEdicao as produtoEdicao ");

		hql.append(" join produtoEdicao.produto as produto ");

		hql.append(" left join produto.descontoLogistica as descontoLogistica ");

		hql.append(" left join itemNotaFiscal.recebimentoFisico as itemRecebimentoFisico ");
		
		hql.append(" left join itemRecebimentoFisico.diferenca as diferenca ");

		hql.append(" where ");
		
		hql.append(" itemNotaFiscal.notaFiscal.id = :idNotaFiscal ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
	
	public RecebimentoFisico obterRecebimentoFisicoPorNotaFiscal(Long idNotaFiscal){
		StringBuffer hql = new StringBuffer();
		hql.append("select rf from RecebimentoFisico rf where rf.notaFiscal.id = :idNotaFiscal ");
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return (RecebimentoFisico) query.uniqueResult();
	}


	@SuppressWarnings("unchecked")
	public List<ItemRecebimentoFisico> obterItensRecebimentoFisicoDoProduto(Long idProdutoEdicao) {
		
		String hql = " select itemRecebimentoFisico "
				   + " from ItemRecebimentoFisico itemRecebimentoFisico "
				   + " where itemRecebimentoFisico.itemNotaFiscal.produtoEdicao.id = :idProdutoEdicao "
				   + " order by itemRecebimentoFisico.recebimentoFisico.dataConfirmacao asc ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return query.list();
	}


	@Override
	public boolean existeNotaFiscal(Long numero, Long serie, String cnpj, Long numeroNotaEnvio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select n from NotaFiscalEntrada n 	");
		hql.append(" join n.emitente e 					");
		hql.append(" where ( n.numero = :numero 		");
		hql.append(" and n.serie = :serie				");
		hql.append(" and replace(replace(replace(e.cnpj, '.', ''), '-', ''), '/', '') = :cnpj ) 		");
		
		if(numeroNotaEnvio!=null) {
			hql.append(" or n.numeroNotaEnvio = :numeroNotaEnvio ");
		}
		
		Query query = getSession().createQuery(hql.toString());

		query.setMaxResults(1);
		
		query.setParameter("numero", numero);
		query.setParameter("serie", serie);
		query.setParameter("cnpj", cnpj);
		
		if(numeroNotaEnvio != null) {
			query.setParameter("numeroNotaEnvio", numeroNotaEnvio);
		}

		return query.uniqueResult() != null;
	}
	
	
	/**
	 * Verifica se produto possui recebimento físico
	 * @param idProduto
	 * @return boolean
	 */
	@Override
	public boolean produtoPossuiRecebimentoFisico(Long idProduto) {
		
		String hql = " select itemRecebimentoFisico "
				   + " from ItemRecebimentoFisico itemRecebimentoFisico "
				   + " where itemRecebimentoFisico.itemNotaFiscal.produtoEdicao.produto.id = :idProduto ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("idProduto", idProduto);
		
		return query.list().size() > 0;
	}

}
