package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> 
implements MovimentoEstoqueRepository {
	
	/**
	 * Construtor padrão.
	 */
	public MovimentoEstoqueRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
		
	/**
	 * Obtém uma lista de extratoEdicao de acordo com statusAprovacao.
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(String codigoProduto, Long numeroEdicao, StatusAprovacao statusAprovacao) {

		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select new " + ExtratoEdicaoDTO.class.getCanonicalName() );		
		
		hql.append(" ( m.id, m.data, m.tipoMovimento.descricao, ");		
		
		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");		

		hql.append(" where m.produtoEdicao.numeroEdicao = :numeroEdicao and ");		

		hql.append(" m.produtoEdicao.produto.codigo = :codigoProduto ");		

		
		if(statusAprovacao != null) {
			hql.append(" and m.status = :statusAprovacao  ");
		}
		
		hql.append(" group by m.id, m.data, m.tipoMovimento.id, m.tipoMovimento.descricao ");		
		
		hql.append(" order by m.data asc ");		
		
		Query query = getSession().createQuery(hql.toString());
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", OperacaoEstoque.SAIDA);

		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
				
	}
	
	
}
