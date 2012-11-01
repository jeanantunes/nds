package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.FollowupCadastroParcialRepository;

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
		
		hql.append(" order by "+filtro.getPaginacao().getSortColumn()+" "+filtro.getPaginacao().getOrdenacao().toString() );

		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("origem", Origem.MANUAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroParcialDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		filtro.getPaginacao().setQtdResultadosTotal(obterConsignadosParaChamadaoTotalRegistros(filtro));
		
		
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

}
