package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoOperacao;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long> 
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
		
		hql.append(" ( m.id, m.dataInclusao, m.tipoMovimento.descricao, ");		
		
		hql.append(" sum(case when m.tipoMovimento.tipoOperacao  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.tipoOperacao  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");		

		hql.append(" where m.produtoEdicao.numeroEdicao = :numeroEdicao and ");		

		hql.append(" m.produtoEdicao.produto.codigo = :codigoProduto ");		

		
		if(statusAprovacao != null) {
			hql.append(" and m.status = :statusAprovacao  ");
		}
		
		hql.append(" group by m.id, m.dataInclusao, m.tipoMovimento.id, m.tipoMovimento.descricao ");		
		
		hql.append(" order by m.dataInclusao asc ");		
		
		Query query = getSession().createQuery(hql.toString());
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		query.setParameter("tipoOperacaoEntrada", TipoOperacao.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", TipoOperacao.SAIDA);

		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
				
	}
	
	
}
