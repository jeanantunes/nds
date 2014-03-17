package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupCadastroParcialRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FollowupCadastroParcialImpl extends AbstractRepositoryModel<ProdutoEdicao,Long> implements FollowupCadastroParcialRepository {

	public FollowupCadastroParcialImpl() {
		super(ProdutoEdicao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupCadastroParcialDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroParcialDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produto.codigo as codigoProduto ");
		hql.append(" , produto.nome as nomeProduto ");
		hql.append(" , produtoEdicao.numeroEdicao as numeroEdicao  ");
		hql.append(" from ProdutoEdicao as produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" where  produto.origem = :origem and not exists ( ");
		hql.append( 	 getSubqueryPeridoLancamento()    );
		hql.append(" ) " );
		
		if(filtro.getPaginacao() != null) {
			hql.append(" order by "+filtro.getPaginacao().getSortColumn()+" "+filtro.getPaginacao().getOrdenacao().toString() );
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("origem", Origem.MANUAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroParcialDTO.class));
		
		if(filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			
			filtro.getPaginacao().setQtdResultadosTotal(obterConsignadosParaChamadaoTotalRegistros(filtro));
		}
		
		return query.list();
		 
	}

	private Integer  obterConsignadosParaChamadaoTotalRegistros(FiltroFollowupCadastroParcialDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(*) ");
		hql.append(" from ProdutoEdicao as produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" where  produto.origem = :origem and  not exists ( ");
		hql.append( 	 getSubqueryPeridoLancamento()    );
		hql.append(" ) " );
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("origem", Origem.MANUAL);
	
		return  ((Long)query.uniqueResult()).intValue();
	
	}

	private String getSubqueryPeridoLancamento() {
		
		StringBuilder hqlSub = new StringBuilder();
		hqlSub.append( " from Lancamento as lanc ");
		hqlSub.append( " join  lanc.periodoLancamentoParcial "); 
		hqlSub.append( " where lanc.produtoEdicao = produtoEdicao "   );
		return hqlSub.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupCadastroParcialDTO> obterDadosParaAbaCadastroParcial(FiltroFollowupCadastroParcialDTO filtro) {

		StringBuilder sql = new StringBuilder("");
		
		sql.append("  SELECT  ");
		sql.append("  	pd.CODIGO as codigoProduto, 		 ");
		sql.append("  pd.NOME_COMERCIAL as nomeProduto,   	 ");
		sql.append("  pe.NUMERO_EDICAO as numeroEdicaoPesquisa");
		sql.append("  from lancamento_parcial lp  			 ");
		sql.append("  join periodo_lancamento_parcial plp    ");
		sql.append("  	ON plp.LANCAMENTO_PARCIAL_ID = lp.ID ");
		sql.append("  left join produto_edicao pe            ");
		sql.append("  	ON lp.PRODUTO_EDICAO_ID = pe.ID      ");
		sql.append("  left join produto pd                   ");
		sql.append("  	ON pe.PRODUTO_ID = pd.ID  			 ");
		sql.append("  where plp.NUMERO_PERIODO < 2 			 ");
		
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroParcialDTO.class));
		
		this.configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}

}
