package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;

@Repository
public class ControleConferenciaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ControleConferenciaEncalheCota, Long> implements ControleConferenciaEncalheCotaRepository {

	@Autowired
	private DataSource dataSource;
	
	/**
	 * Construtor padrão.
	 */
	public ControleConferenciaEncalheCotaRepositoryImpl() {
		super(ControleConferenciaEncalheCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Date> obterDatasControleConferenciaEncalheCotaFinalizada(Long idCota, Date dataDe, Date dataAte) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select c.dataOperacao from ControleConferenciaEncalheCota c ");		
		
		hql.append(" where ");		

		hql.append(" c.dataOperacao between :dataDe and :dataAte and ");
		
		hql.append(" c.cota.id = :idCota ");
		

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataDe", dataDe);

		query.setParameter("dataAte", dataAte);
		
		query.setParameter("idCota", idCota);
		
		return (List<Date>) query.list();
		
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
		
		sql.append("	inner join COTA on ");
		sql.append("	( COTA.ID = CHAMADA_ENCALHE_COTA.COTA_ID) ");
		

		sql.append("	inner join PDV on (PDV.COTA_ID = COTA.ID and PDV.PONTO_PRINCIPAL = true) ");
		
		sql.append("	inner join ROTA_PDV on (ROTA_PDV.PDV_ID = PDV.ID) ");
		
		sql.append("	inner join ROTA on (ROTA.ID = ROTA_PDV.ROTA_ID) ");
		
		sql.append("	inner join ROTEIRO on (ROTEIRO.ID = ROTA.ROTEIRO_ID) ");
		
		sql.append("	inner join ROTEIRIZACAO on (ROTEIRIZACAO.ID = ROTEIRO.ROTEIRIZACAO_ID) ");
		
		sql.append("	inner join BOX on (BOX.ID = ROTEIRIZACAO.BOX_ID) ");
		

		sql.append("	where	");
		
		sql.append("	(CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.POSTERGADO = :isPostergado ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}

		sql.append("	ORDER BY BOX.CODIGO,ROTEIRO.ORDEM , ROTA.ORDEM, ROTA_PDV.ORDEM  ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		parameters.put("isPostergado", false);
		
		return namedParameterJdbcTemplate.queryForList(sql.toString(), parameters, Long.class);
		
	}
	
	@Override
	public List<Integer> obterListaNumCotaConferenciaEncalheCota(FiltroConsultaEncalheDTO filtro) {
		
		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	DISTINCT(COTA.NUMERO_COTA)	as numCota ");
		
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
		
		sql.append("	inner join COTA on ");
		sql.append("	( COTA.ID = CHAMADA_ENCALHE_COTA.COTA_ID) ");
		

		sql.append("	inner join PDV on (PDV.COTA_ID = COTA.ID and PDV.PONTO_PRINCIPAL = true) ");
		
		sql.append("	inner join ROTA_PDV on (ROTA_PDV.PDV_ID = PDV.ID) ");
		
		sql.append("	inner join ROTA on (ROTA.ID = ROTA_PDV.ROTA_ID) ");
		
		sql.append("	inner join ROTEIRO on (ROTEIRO.ID = ROTA.ROTEIRO_ID) ");
		
		sql.append("	inner join ROTEIRIZACAO on (ROTEIRIZACAO.ID = ROTEIRO.ROTEIRIZACAO_ID) ");
		
		sql.append("	inner join BOX on (BOX.ID = ROTEIRIZACAO.BOX_ID) ");
		

		sql.append("	where	");
		
		sql.append("	(CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.POSTERGADO = :isPostergado ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}
		
		if(filtro.getIdBox() != null) {
			sql.append(" and BOX.CODIGO =  :idBox ");
		}

		sql.append("	ORDER BY BOX.CODIGO, ROTEIRO.ORDEM, ROTA.ORDEM, ROTA_PDV.ORDEM  ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		if(filtro.getIdBox() != null) {
			parameters.put("idBox", filtro.getIdBox());
		}
		
		parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		parameters.put("isPostergado", false);
		
		return namedParameterJdbcTemplate.queryForList(sql.toString(), parameters, Integer.class);
		
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


	@Override
	public boolean obterAceitaJuramentado(Long idCota) {
		
		StringBuffer sql = new StringBuffer();

		/*
		 * Verifica se ha algum produto com recolhimento parcial
		 * Se houver, retorna true
		 * 
		 */
		sql.append("SELECT CASE WHEN ((SELECT DIA_RECOLHIMENTO_PRIMEIRO from distribuidor) ")
			.append("AND (SELECT ACEITA_JURAMENTADO FROM distribuidor) ")
			.append("AND DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) = 0) ")
			.append("	            AND (NOT EXISTS(SELECT DATA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor)) ") 
			.append("	                OR (SELECT IND_OPERA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor) = true)) THEN DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                                                                                                        WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                                                                                                        ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) + 1 ")
			.append("	            WHEN ((SELECT DIA_RECOLHIMENTO_SEGUNDO from distribuidor) ")
			.append("	            AND (SELECT ACEITA_JURAMENTADO FROM distribuidor) ")
			.append("	            AND DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) = 1) ")
			.append("	            AND (NOT EXISTS(SELECT DATA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor)) ")
			.append("				                OR (SELECT IND_OPERA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor) = true)) THEN DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("																															WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("				                                                                                                            ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) + 1 ")
			.append("	        WHEN ((SELECT DIA_RECOLHIMENTO_TERCEIRO from distribuidor) ")
			.append("	            AND (SELECT ACEITA_JURAMENTADO FROM distribuidor) ")
			.append("	            AND DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) = 2) ")
			.append("	            AND (NOT EXISTS(SELECT DATA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor)) ") 
			.append("	                OR (SELECT IND_OPERA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor) = true)) THEN DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                                                                                                        WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                                                                                                        ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) + 1 ")
			.append("	        WHEN ((SELECT DIA_RECOLHIMENTO_QUARTO from distribuidor) ")
			.append("	            AND (SELECT ACEITA_JURAMENTADO FROM distribuidor) ")
			.append("	            AND DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) = 3) ")
			.append("	            AND (NOT EXISTS(SELECT DATA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor)) ") 
			.append("	                OR (SELECT IND_OPERA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor) = true)) THEN DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                                                                                                        WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                                                                                                        ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) + 1 ")
			.append("	        WHEN ((SELECT DIA_RECOLHIMENTO_QUINTO from distribuidor) ")
			.append("	            AND (SELECT ACEITA_JURAMENTADO FROM distribuidor) ")
			.append("	            AND DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) = 4) ")
			.append("	            AND (NOT EXISTS(SELECT DATA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor)) ")
			.append("	                OR (SELECT IND_OPERA FROM feriado WHERE data = (select DATA_OPERACAO from distribuidor) = true)) THEN DATEDIFF(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 1 DAY) ")
			.append("	                                                                                                                        WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL 2 DAY) ")
			.append("	                                                                                                                        ELSE (select DATA_OPERACAO from distribuidor) END, DATA_RECOLHIMENTO) + 1 ")
			.append("	        ELSE -1 END AS dia ")
			.append("	from chamada_encalhe ce ")
			.append("	inner join chamada_encalhe_cota cec on cec.chamada_encalhe_id = ce.id ")
			.append("	where DATA_RECOLHIMENTO IN ( ")
			.append("	    select CASE WHEN (select DIA_RECOLHIMENTO_PRIMEIRO from distribuidor) THEN ") 
			.append("	            CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -2 DAY) ")
			.append("	            WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -1 DAY) ")
			.append("	            ELSE (select DATA_OPERACAO from distribuidor) END ELSE (select DATA_OPERACAO from distribuidor) END ")
			.append("	    from distribuidor ")
			.append("	    union ")
			.append("	    select CASE WHEN (select DIA_RECOLHIMENTO_SEGUNDO from distribuidor) THEN ") 
			.append("	                DATE_ADD(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -2 DAY) ")
			.append("	                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -1 DAY) ")
			.append("	                ELSE (select DATA_OPERACAO from distribuidor) END,  INTERVAL -1 DAY) ELSE (select DATA_OPERACAO from distribuidor) END ")
			.append("	    from distribuidor ")
			.append("	    union ")
			.append("	    select CASE WHEN (select DIA_RECOLHIMENTO_TERCEIRO from distribuidor) THEN ") 
			.append("	                DATE_ADD(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -2 DAY) ")
			.append("	                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -1 DAY) ")
			.append("	                ELSE (select DATA_OPERACAO from distribuidor) END,  INTERVAL -2 DAY) ELSE (select DATA_OPERACAO from distribuidor) END ")
			.append("	    from distribuidor ")
			.append("	    union ")
			.append("	    select CASE WHEN (select DIA_RECOLHIMENTO_QUARTO from distribuidor) THEN ") 
			.append("	                DATE_ADD(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -2 DAY) ")
			.append("	                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -1 DAY) ")
			.append("	                ELSE (select DATA_OPERACAO from distribuidor) END,  INTERVAL -3 DAY) ELSE (select DATA_OPERACAO from distribuidor) END ")
			.append("	    from distribuidor ")
			.append("	    union ")
			.append("	    select CASE WHEN (select DIA_RECOLHIMENTO_QUINTO from distribuidor) THEN ") 
			.append("	                DATE_ADD(CASE WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 0) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -2 DAY) ")
			.append("	                WHEN (DAYOFWEEK((select DATA_OPERACAO from distribuidor)) = 7) THEN DATE_ADD((select DATA_OPERACAO from distribuidor), INTERVAL -1 DAY) ")
			.append("	                ELSE (select DATA_OPERACAO from distribuidor) END,  INTERVAL -4 DAY) ELSE (select DATA_OPERACAO from distribuidor) END ")
			.append("	    from distribuidor ")
			.append("	) ")
			.append("	and cota_id = :idCota ")
			.append("	group by dia ")
			.append("	having dia > 0");
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		
		sqlQuery.setParameter("idCota", idCota);
		
		return (sqlQuery.list() != null && !sqlQuery.list().isEmpty());
		
	}
	
	/**
	 * Verifica se a cota possui conferencia de encalhe finalizada na data
	 * @param idCota
	 * @param dataOperacao
	 * @return boolean
	 */
    @Override
	public boolean isConferenciaEncalheCotaFinalizada(Long idCota, Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select c from ControleConferenciaEncalheCota c ");		
		
		hql.append(" where c.dataOperacao = :dataOperacao ");
		
		hql.append(" and c.cota.id = :idCota ");
		
		hql.append(" and c.status = :statusConcluido ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("statusConcluido", StatusOperacao.CONCLUIDO);
		
		return (query.list().size() > 0);
	}
    
    /**
     * Obtém ControleConferenciaEncalheCota por Cobrança
     * @param idCobranca
     * @return ControleConferenciaEncalheCota
     */
    @Override
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCotaPorIdCobranca(Long idCobranca) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select c from ControleConferenciaEncalheCota c ");		
		
		hql.append(" join c.cobrancasControleConferenciaEncalheCota cobrancaControleConferencia ");
		
		hql.append(" join cobrancaControleConferencia.cobranca cobranca ");
		
		hql.append(" where cobranca.id = :idCobranca ");
		
		hql.append(" and c.status = :statusConcluido ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idCobranca", idCobranca);
		
		query.setParameter("statusConcluido", StatusOperacao.CONCLUIDO);
		
		return (ControleConferenciaEncalheCota) query.uniqueResult();
	}
}
