package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.OutraMovimentacaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.util.Intervalo;

@Repository
public class CotaAusenteRepositoryImpl extends AbstractRepositoryModel<CotaAusente, Long> implements CotaAusenteRepository { 
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CotaAusenteRepositoryImpl.class);

	/**
	 * Construtor.
	 */
	public CotaAusenteRepositoryImpl() {
		
		super(CotaAusente.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtro) {
						
		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append(" SELECT "); 		
		queryNative.append(" ca.ID as idCotaAusente, ");
		queryNative.append(" ca.DATA as data, ");
		queryNative.append(" box.NOME as box, ");
		queryNative.append(" cota.NUMERO_COTA as cota, ");
	    queryNative.append(" (case when (pessoa.TIPO = 'F') then pessoa.NOME else pessoa.RAZAO_SOCIAL end) AS nome, ");
	    queryNative.append(" SUM(ec.QTDE_EFETIVA * pe.PRECO_VENDA) as valorNE ");
		
		queryNative.append(getFromWhereBuscaCotaAusente(filtro));
		
		ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
		if (colunaOrdenacao != null) {
			if (ColunaOrdenacao.box == colunaOrdenacao) {
				queryNative.append("order by box.NOME ");
			} else if (ColunaOrdenacao.data == colunaOrdenacao) {
				queryNative.append("order by ca.DATA ");
			} else if (ColunaOrdenacao.cota == colunaOrdenacao) {
				queryNative.append("order by cota.NUMERO_COTA ");
			} else if (ColunaOrdenacao.nome == colunaOrdenacao) {
				queryNative.append("order by pessoa.NOME ");
			} else if (ColunaOrdenacao.valorNe == colunaOrdenacao) {
				queryNative.append("order by valorNE ");
			}	
			
			
			if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
				if ("DESC".equalsIgnoreCase(filtro.getPaginacao().getOrdenacao().name())) {
					queryNative.append(" DESC ");
				} else {
					queryNative.append(" ASC ");
				}
			}
		}
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			queryNative.append(" limit :inicio,:qtdeResult ");
		}
				
		Query query  = getSession().createSQLQuery(queryNative.toString())
				.addScalar("idCotaAusente")
				.addScalar("data")
				.addScalar("box")
				.addScalar("cota")
				.addScalar("nome")
				.addScalar("valorNe").setResultTransformer(Transformers.aliasToBean(CotaAusenteDTO.class));

		setParametersBuscaCotaAusente(filtro, query);
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			query.setParameter("inicio", filtro.getPaginacao().getPosicaoInicial());
			query.setParameter("qtdeResult", filtro.getPaginacao().getQtdResultadosPorPagina());
		}
				
		return query.list();
	}
	
	private void setParametersBuscaCotaAusente(FiltroCotaAusenteDTO filtro, Query query) {
		
		if(filtro.getData() != null){			
			query.setParameter("data", filtro.getData());
		}
		
		query.setParameter("tipoBox", TipoBox.ESPECIAL.name());
		if(filtro.getNumCota() != null){
			query.setParameter("numCota", filtro.getNumCota());
		}
		
		if(filtro.getBox() != null){
			query.setParameter("box", filtro.getBox());
		}
		
		if(filtro.getIdRoteiro() != null) {
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
	
		if(filtro.getIdRota() != null) {
			query.setParameter("idRota", filtro.getIdRota());
		}
	}
	
	private StringBuilder getFromWhereBuscaCotaAusente(FiltroCotaAusenteDTO filtro) {
		
		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append(" FROM COTA_AUSENTE ca ");
		
		queryNative.append(" JOIN COTA cota on (cota.ID = ca.COTA_ID) ");
		queryNative.append(" JOIN cota_ausente_movimento_estoque_cota camec ON (camec.COTA_AUSENTE_ID = ca.ID) ");
		queryNative.append(" JOIN movimento_estoque_cota mec on (camec.MOVIMENTO_ESTOQUE_COTA_ID = mec.id) ");
		queryNative.append(" JOIN produto_edicao pe on (pe.ID = mec.PRODUTO_EDICAO_ID) ");
		queryNative.append(" JOIN estudo_cota ec on (ec.cota_id = cota.id) ");
		queryNative.append(" JOIN estudo estudo on (ec.estudo_id = estudo.id) ");
		queryNative.append(" JOIN LANCAMENTO lancamento on (lancamento.id = mec.lancamento_id and estudo.PRODUTO_EDICAO_ID = lancamento.PRODUTO_EDICAO_ID and estudo.id = lancamento.estudo_id) ");
		
		queryNative.append(" JOIN PDV pdv ON (pdv.COTA_ID = cota.ID) ");
		queryNative.append(" JOIN ROTA_PDV rota_pdv ON (pdv.ID = rota_pdv.PDV_ID) ");
		queryNative.append(" JOIN ROTA rota ON (rota.ID = rota_pdv.ROTA_ID) ");
		queryNative.append(" JOIN ROTEIRO roteiro ON (rota.ROTEIRO_ID = roteiro.ID) ");
		queryNative.append(" JOIN ROTEIRIZACAO roteirizacao ON (roteirizacao.ID = roteiro.ROTEIRIZACAO_ID) ");
		queryNative.append(" JOIN BOX box ON (box.ID = roteirizacao.BOX_ID) ");
		
		queryNative.append(" JOIN PESSOA pessoa  ");
		
		queryNative.append(" WHERE cota.PESSOA_ID=pessoa.ID  and box.tipo_box != :tipoBox ");


		if(filtro.getData() != null){	
			queryNative.append("AND ca.DATA = :data  											");
		}
		
		
		if(filtro.getNumCota() != null){			
			queryNative.append("and cota.NUMERO_COTA = :numCota 												");
		}
		
		if(filtro.getBox() != null){
			
			queryNative.append("and box.NOME = :box 															");
		}
		
		if(filtro.getIdRoteiro() != null) {
			queryNative.append(" and roteiro.ID = :idRoteiro ");
		}
	
		if(filtro.getIdRota() != null) {
			queryNative.append(" and rota.ID = :idRota ");
		}
		
		queryNative.append("group by 		");
		queryNative.append("ca.DATA, 			");
		queryNative.append("box.NOME, 			");
		queryNative.append("cota.NUMERO_COTA,			");
		queryNative.append("pessoa.nome			");
		
		return queryNative;
	}
	
	
	public CotaAusente obterCotaAusentePor(Long idCota, Date data) {

		Criteria criteria = this.getSession().createCriteria(CotaAusente.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("data", data));

		CotaAusente cotaAusente = null;
		
		try {
		  	cotaAusente = (CotaAusente) criteria.uniqueResult();
		} catch(HibernateException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return  cotaAusente;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro) {

		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append("SELECT COUNT(*)																		"); 	
		queryNative.append(getFromWhereBuscaCotaAusente(filtro));
		
		Query query  = getSession().createSQLQuery(queryNative.toString());
		
		setParametersBuscaCotaAusente(filtro, query);
		
		List<BigInteger> result = query.list();
		
		return (long) result.size();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSuplementarDTO> obterDadosExclusaoCotaAusente(Long idCotaAusente) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produto.codigo as codigoProduto, ");
		hql.append(" produto.nome as nomeProdutoEdicao, ");
		hql.append(" produtoEdicao.id as idProdutoEdicao, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" sum(movimentosEstoqueCota.qtde) as reparte, ");
		hql.append(" coalesce(estoqueProduto.qtdeSuplementar,0) as quantidadeDisponivel ");
		hql.append(" from CotaAusente cotaAusente ");
		hql.append(" inner join cotaAusente.movimentosEstoqueCota movimentosEstoqueCota ");
		hql.append(" inner join movimentosEstoqueCota.produtoEdicao produtoEdicao ");
		hql.append(" inner join produtoEdicao.estoqueProduto estoqueProduto ");
		hql.append(" inner join produtoEdicao.produto produto ");
		hql.append(" where cotaAusente.id = :idCotaAusente ");
		hql.append(" group by produtoEdicao.id ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCotaAusente", idCotaAusente);
		
		query.setResultTransformer(
			Transformers.aliasToBean(ProdutoEdicaoSuplementarDTO.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> obterIdsCotasAusentesNoPeriodo(Intervalo<Date> periodo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ca.cota.id from CotaAusente ca where ca.data between :inicioPeriodo and :fimPeriodo ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("inicioPeriodo", periodo.getDe());
		query.setParameter("fimPeriodo", periodo.getAte());
		
		return query.list();
	}
	
	@Override
	public BigDecimal obterSaldoDeEntradaDoConsignadoDasCotasAusenteNoDistribuidor(final Date dataMovimentacao){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" coalesce(sum(if(tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:suplementarCotaAusente) ");
		sql.append("				,movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA  ");
		sql.append("				,movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA*-1)),0) as VALOR_COTA_AUSENTE ");
				
		sql.append(" from ");
		sql.append("		MOVIMENTO_ESTOQUE movimentoEstoque ");
		sql.append("		join TIPO_MOVIMENTO tipoMovimento on movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID ");
		sql.append("		join PRODUTO_EDICAO produtoEdicao on movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" where ");
		sql.append("	movimentoEstoque.DATA = :dataMovimento ");
		sql.append("	and movimentoEstoque.STATUS = :statusAprovado ");
		sql.append("	and tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in (:suplementarCotaAusente, :reparteCotaAusente)");
		sql.append("	and movimentoEstoque.PRODUTO_EDICAO_ID in (");
		sql.append("			select distinct produtoEdicao_.ID ");
		sql.append("			from EXPEDICAO expedicao ");
		sql.append("			inner join LANCAMENTO lancamento on expedicao.ID = lancamento.EXPEDICAO_ID ");
		sql.append("			inner join PRODUTO_EDICAO produtoEdicao_ on lancamento.PRODUTO_EDICAO_ID = produtoEdicao_.ID ");
		sql.append("			inner join PRODUTO produto_ on produtoEdicao_.PRODUTO_ID = produto_.ID ");
		sql.append("			where lancamento.STATUS <> :statusFuro");
		sql.append("                and lancamento.DATA_LCTO_DISTRIBUIDOR < :dataMovimento ");
		sql.append("				and produto_.FORMA_COMERCIALIZACAO = :formaComercializacaoConsignado");
		sql.append("		)");
		sql.append("	and movimentoEstoque.PRODUTO_EDICAO_ID not in ( ");
	    sql.append(" 		select distinct produtoEdicao.ID ");
	    sql.append(" 		from EXPEDICAO expedicaoProduto ");
	    sql.append(" 		inner join LANCAMENTO lancamentoProduto on expedicaoProduto.ID = lancamentoProduto.EXPEDICAO_ID "); 
	    sql.append(" 		inner join PRODUTO_EDICAO produtoEdicao on lancamentoProduto.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
	    sql.append(" 		inner join PRODUTO produto  on produtoEdicao.PRODUTO_ID = produto.ID ");
	    sql.append(" 		where lancamentoProduto.STATUS <> :statusFuro ");
	    sql.append(" 			and produto.FORMA_COMERCIALIZACAO = :formaComercializacaoConsignado ");
	    sql.append(" 			and data_lcto_distribuidor = :dataMovimento ");
	    sql.append(" ) ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimento", dataMovimentacao);
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
		query.setParameter("statusFuro", StatusLancamento.FURO.name());
		query.setParameter("formaComercializacaoConsignado", FormaComercializacao.CONSIGNADO.name());
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name());
		query.setParameter("reparteCotaAusente", GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name());
		
		query.addScalar("VALOR_COTA_AUSENTE",StandardBasicTypes.BIG_DECIMAL);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterSaldoDeSaidaDoConsignadoDasCotasAusenteNoDistribuidor(final Date dataMovimentacao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" coalesce(sum(if(tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:suplementarCotaAusente)");
		sql.append("			  ,movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA *-1");
		sql.append("			  ,movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA)),0) as VALOR_COTA_AUSENTE");
	    
		sql.append(" from");
		sql.append("	MOVIMENTO_ESTOQUE movimentoEstoque");
		sql.append("	join TIPO_MOVIMENTO tipoMovimento on movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID ");
		sql.append("	join PRODUTO_EDICAO produtoEdicao on movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" where");
		sql.append("	movimentoEstoque.DATA = :dataMovimento "); 
		sql.append("	and movimentoEstoque.STATUS=:statusAprovado ");
		sql.append("	and tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in (:grupoMovimentos)");
		sql.append("	and movimentoEstoque.PRODUTO_EDICAO_ID in (");
		sql.append("			select distinct produtoEdicao_.ID ");
		sql.append("			from ");
		sql.append("				EXPEDICAO expedicao ");
		sql.append("				inner join LANCAMENTO lancamento on expedicao.ID = lancamento.EXPEDICAO_ID ");
		sql.append("				inner join PRODUTO_EDICAO produtoEdicao_ on lancamento.PRODUTO_EDICAO_ID = produtoEdicao_.ID  ");
		sql.append("				inner join PRODUTO produto_ on produtoEdicao_.PRODUTO_ID = produto_.ID  ");
		sql.append("			where lancamento.STATUS <> :statusFuro ");
		sql.append("				and lancamento.DATA_LCTO_DISTRIBUIDOR = :dataMovimento ");
		sql.append("				and produto_.FORMA_COMERCIALIZACAO = :formaComercializacaoConsignado ");
		sql.append("		) ");
			
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimento", dataMovimentacao);
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
		query.setParameter("statusFuro", StatusLancamento.FURO.name());
		query.setParameter("formaComercializacaoConsignado", FormaComercializacao.CONSIGNADO.name());
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name());
		query.setParameterList("grupoMovimentos", Arrays.asList(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name(),
																 GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name()));
		
		query.addScalar("VALOR_COTA_AUSENTE",StandardBasicTypes.BIG_DECIMAL);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public List<OutraMovimentacaoDTO> obterOutraMovimentacaoCotaAusente(final Date dataMovimentacao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct ");
		sql.append(" cota.numero_cota as numeroCota, ");
		sql.append(" (case when (pessoa.TIPO = 'F') then pessoa.NOME else pessoa.RAZAO_SOCIAL end) AS nomeCota, ");
		sql.append(" 'Cota Ausente' as operacao, ");
		sql.append(" coalesce(sum(movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA),0)*-1 as valor ");
		sql.append(" from ");
		sql.append("	MOVIMENTO_ESTOQUE_COTA movimentoEstoque");
		sql.append("	join TIPO_MOVIMENTO tipoMovimento on movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID ");
		sql.append("	join PRODUTO_EDICAO produtoEdicao on movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append("	join COTA_AUSENTE ca on (ca.DATA = movimentoEstoque.DATA) ");
		sql.append("	join COTA cota on (ca.COTA_ID = cota.ID) and (movimentoEstoque.cota_id =  cota.ID) ");
		sql.append("	join PESSOA pessoa on pessoa.id = cota.PESSOA_ID ");
		sql.append(" where");
		sql.append("	movimentoEstoque.DATA = :dataMovimento "); 
		sql.append("	and movimentoEstoque.STATUS=:statusAprovado ");
		sql.append("	and movimentoEstoque.FORMA_COMERCIALIZACAO = :formaComercializacaoConsignado ");
		sql.append("	and tipoMovimento.id = 14 ");
		sql.append("   Group by cota.id, movimentoEstoque.DATA 	");
		
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataMovimento", dataMovimentacao);
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
		query.setParameter("formaComercializacaoConsignado", FormaComercializacao.CONSIGNADO.name());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(OutraMovimentacaoDTO.class));
		
		return query.list();
	}
	
}
