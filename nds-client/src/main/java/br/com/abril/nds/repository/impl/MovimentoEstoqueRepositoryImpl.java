package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
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
	 * Obtém uma lista de extratoEdicao.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(Long numeroEdicao) {

		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select new " + ExtratoEdicaoDTO.class.getCanonicalName() );		
		
		hql.append(" ( m.id, m.dataInclusao, m.tipoMovimento.descricao, ");		
		
		hql.append(" sum(case when m.tipoMovimento.tipoOperacao  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.tipoOperacao  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");		

		hql.append(" where m.produtoEdicao.numeroEdicao = :numeroEdicao ");		
		
		hql.append(" group by m.id, m.dataInclusao, m.tipoMovimento.id, m.tipoMovimento.descricao ");		
		
		hql.append(" order by m.dataInclusao asc ");		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("tipoOperacaoEntrada", TipoOperacao.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", TipoOperacao.SAIDA);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
				
	}
	
	
}
