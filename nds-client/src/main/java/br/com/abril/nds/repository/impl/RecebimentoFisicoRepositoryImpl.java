package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemNotaRecebimentoFisicoDTO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;

/**
 * 
 * @author Gustavo
 *
 */
@Repository
public class RecebimentoFisicoRepositoryImpl extends AbstractRepository<RecebimentoFisico, Long> 
									  implements RecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public RecebimentoFisicoRepositoryImpl() {
		super(RecebimentoFisico.class);
	}

	
	public List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select new ");
		
		hql.append(RecebimentoFisicoDTO.class.getCanonicalName());
		
		hql.append(" (	itemNotaFiscal.produtoEdicao.produto.codigo, 				");
		hql.append("  	itemNotaFiscal.produtoEdicao.produto.nome, 					");
		hql.append("  	itemNotaFiscal.produtoEdicao.numeroEdicao, 					");
		hql.append(" 	itemNotaFiscal.produtoEdicao.precoVenda, 					");
		hql.append(" 	itemNotaFiscal.qtde, 										");
		hql.append(" 	itemRecebimentoFisico.qtdeFisico, 							");
		hql.append(" 	diferenca.qtde,  											");
		hql.append(" 	diferenca.tipoDiferenca, 											");
		hql.append(" 	itemNotaFiscal.id )											");
		
		
		hql.append(" from ");

		hql.append(" ItemNotaFiscal itemNotaFiscal ");

		hql.append(" left join itemNotaFiscal.recebimentoFisico as itemRecebimentoFisico ");
		
		hql.append(" left join itemRecebimentoFisico.diferenca as diferenca ");
		
		hql.append(" where ");
		
		hql.append(" itemNotaFiscal.notaFiscal.id = :idNotaFiscal ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
}
	

