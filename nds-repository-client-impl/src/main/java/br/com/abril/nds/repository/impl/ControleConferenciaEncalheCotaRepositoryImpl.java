package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;

@Repository
public class ControleConferenciaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ControleConferenciaEncalheCota, Long> implements ControleConferenciaEncalheCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public ControleConferenciaEncalheCotaRepositoryImpl() {
		super(ControleConferenciaEncalheCota.class);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository#obterControleConferenciaEncalheCota(java.lang.Integer, java.util.Date)
	 */
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(Integer numeroCota, Date dataOperacao) {
			
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select controleConferenciaEncalheCota  ");
		
		hql.append(" from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" controleConferenciaEncalheCota.cota.numeroCota = :numeroCota and ");
		
		hql.append(" controleConferenciaEncalheCota.dataOperacao = :dataOperacao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);

		query.setParameter("dataOperacao", dataOperacao);
		
		return (ControleConferenciaEncalheCota) query.uniqueResult();
		
	}
	
	public StatusOperacao obterStatusControleConferenciaEncalheCota(Long idControleConferenciaEncalheCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ccec.status ");
		hql.append(" from ControleConferenciaEncalheCota ccec ");
		hql.append(" where  ");
		hql.append(" ccec.id = :idControleConferenciaEncalheCota  ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return (StatusOperacao) query.uniqueResult();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> obterListaIdControleConferenciaEncalheCota(FiltroConsultaEncalheDTO filtro) {
		
		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	DISTINCT(CONTROLE_CONF_ENC_COTA.ID)	as idControle ");
		
		sql.append("	from	");

		sql.append("	CHAMADA_ENCALHE  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
		
		sql.append("	inner join CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA on ");
		sql.append("	( CONTROLE_CONF_ENC_COTA.DATA_OPERACAO = CHAMADA_ENCALHE.DATA_RECOLHIMENTO 	");
		sql.append("	AND  CONTROLE_CONF_ENC_COTA.COTA_ID = CHAMADA_ENCALHE_COTA.COTA_ID ) ");
		
		sql.append("	where	");
		
		sql.append("	(CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.FECHADO = :isPostergado ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString()).addScalar("idControle", StandardBasicTypes.LONG);
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		sqlquery.setParameter("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		sqlquery.setParameter("isPostergado", false);
		
		return sqlquery.list();
		
	}

	
	@SuppressWarnings("unchecked")
	public List<ControleConferenciaEncalheCota> obterControleConferenciaEncalheCotaPorFiltro(FiltroConsultaEncalheDTO filtro) {
				
		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	CONTROLE_CONF_ENC_COTA ");
		
		sql.append("	from	");

		sql.append("	CHAMADA_ENCALHE  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
		
		sql.append("	inner join CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA on ");
		sql.append("	( CONTROLE_CONF_ENC_COTA.DATA_OPERACAO = CHAMADA_ENCALHE.DATA_RECOLHIMENTO 	");
		sql.append("	AND  CONTROLE_CONF_ENC_COTA.COTA_ID = CHAMADA_ENCALHE_COTA.COTA_ID ) ");
		
		sql.append("	where	");
		
		sql.append("	(CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.FECHADO = :isPostergado ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		sqlquery.setParameter("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		sqlquery.setParameter("isPostergado", false);
		
		return sqlquery.list();
	}
	
	
}
