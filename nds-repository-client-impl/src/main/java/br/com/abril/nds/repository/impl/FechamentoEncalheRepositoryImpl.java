package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.ExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

@Repository
public class FechamentoEncalheRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements FechamentoEncalheRepository {
    
    public FechamentoEncalheRepositoryImpl() {
        super(FechamentoEncalhe.class);
    }
    
    private StringBuilder getFromConferenciaEncalhe(){
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" from ConferenciaEncalhe as ce ");
        hql.append("  JOIN ce.movimentoEstoqueCota as mec ");
        hql.append("  JOIN ce.controleConferenciaEncalheCota as ccec ");
        hql.append("  JOIN mec.produtoEdicao as pe ");
        hql.append("  JOIN pe.produto as p ");
        hql.append("  LEFT JOIN pe.descontoLogistica as descLogProdEdicao ");
        hql.append("  LEFT JOIN p.descontoLogistica as descLogProd ");
        hql.append("  JOIN ce.chamadaEncalheCota as cec ");
        hql.append("  JOIN ce.chamadaEncalheCota.cota as cota ");
        hql.append("  JOIN cec.chamadaEncalhe as che ");
        hql.append("  JOIN p.fornecedores as pf ");
        
        return hql;
    }
    
    private StringBuilder getQueryVendaProduto(){
        
        final StringBuilder subquery = new StringBuilder();
        
        subquery.append(" select COALESCE(sum(vp.qntProduto ),0) ");
        
        subquery.append(" from VendaProduto vp, ConferenciaEncalhe conf ");
        
        subquery.append(" where conf.controleConferenciaEncalheCota.dataOperacao = :dataEncalhe and ");
        
        subquery.append(" conf.controleConferenciaEncalheCota.status = :statusOperacaoFinalizada and ");
        
        subquery.append(" conf.produtoEdicao.id = vp.produtoEdicao.id and ");
        
        subquery.append(" conf.controleConferenciaEncalheCota.cota.id = vp.cota.id and	");
        
        subquery.append(" vp.produtoEdicao.id = pe.id and ");
        
        subquery.append(" vp.dataOperacao = :dataEncalhe and ");
        
        subquery.append(" vp.tipoVenda = :tipoVenda ");
        
        subquery.append(" and vp.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
        
        return subquery;
    }
    
    private StringBuilder getWhereFechamentoEncalhe(final FiltroFechamentoEncalheDTO filtro){
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" WHERE ccec.dataOperacao =:dataEncalhe ");
        
        hql.append(" and ccec.status = :statusOperacaoFinalizada ");
        
        if (filtro.getBoxId() != null) {
            hql.append("  and ccec.box.id = :boxId ");
        }
        
        if (filtro.getFornecedorId() != null) {
            hql.append("  and pf.id = :fornecedorId ");
        }
        
        hql.append(" group by 			")
        .append(" p.codigo,  			")
        .append(" p.nome, 				")
        .append(" pe.numeroEdicao, 		")
        .append(" pe.precoVenda,  		")
        .append(" pe.id, 				")
        .append(" pe.parcial,  			")
        .append(" che.dataRecolhimento 	");
        
        return hql;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalheNovo(final FiltroFechamentoEncalheDTO filtro,
            final String sortorder,
            final String sortname,
            final Integer page,
            final Integer rp) {
        
        String queryString = this.getQueryFechamentoEncalhe(filtro, false);
        
        queryString += " ORDER BY sequencia ";
        
        final Query query = getSession().createSQLQuery(queryString);
        
        query.setParameter("dataRecolhimento", filtro.getDataEncalhe());
        query.setParameter("origemInterface", Origem.INTERFACE.name());
        query.setParameter("tipoChamadaEncalheChamadao", TipoChamadaEncalhe.CHAMADAO.name());
        query.setParameter("tipoChamadaEncalheAntecipada", TipoChamadaEncalhe.ANTECIPADA.name());
        query.setParameter("tipoChamadaEncalheMatrizRecolhimento", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO.name());
        
        if (filtro.getFornecedorId() != null) {
            
            query.setLong("fornecedorId", filtro.getFornecedorId());
        }
        
        if (page != null) {
            
            query.setFirstResult(page);
        }
        
        if (rp != null) {
            
            query.setMaxResults(rp);
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        
        final Properties params = new Properties();
        
        params.put("enumClass", Origem.class.getCanonicalName());
        params.put("type", "12");
        
        final Type origemEnumType = new TypeLocatorImpl(new TypeResolver()).custom(EnumType.class, params);
        
        ((SQLQuery) query).addScalar("pacotePadrao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("produtoEdicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("sequencia", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("produto", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("codigo", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("edicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("origem", origemEnumType);
        ((SQLQuery) query).addScalar("produtoEdicaoDescontoLogisticaId", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("produtoDescontoLogisticaId", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("precoCapaDesconto", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("tipo", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("recolhimento", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("matrizRecolhimento", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("chamadao", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("antecipada", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("parcial", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("chamadaEncalheId", StandardBasicTypes.LONG);
        
        return query.list();
    }
    
    private String getQueryFechamentoEncalhe(final FiltroFechamentoEncalheDTO filtro, final boolean count) {
        
        final StringBuilder sql = new StringBuilder();
        
        if (count) {
            
            sql.append("select count(*) from ( ");
        }
        

        sql.append(" SELECT ");

        sql.append(" ENCALHE_INFO_EDICAO.* ");

        
        if(!count) {

    		sql.append(", ( ");
    		sql.append(" 	ENCALHE_INFO_EDICAO.PRECOCAPA - ( ENCALHE_INFO_EDICAO.PRECOCAPA * ");
    		sql.append(" 	CASE ");
    		sql.append(" 	WHEN ENCALHE_INFO_EDICAO.prodEdicaoOrigem = :origemInterface ");
    		sql.append(" 	THEN (COALESCE(		");
    		sql.append(" 			DLPE.PERCENTUAL_DESCONTO, ");
    		sql.append(" 			DLP.PERCENTUAL_DESCONTO,  ");
    		sql.append(" 			ENCALHE_INFO_EDICAO.PRODEDICAODESCONTO, ");
    		sql.append(" 			ENCALHE_INFO_EDICAO.PRODDESCONTO, 0) / 100) ");
    		sql.append(" 	ELSE (COALESCE(ENCALHE_INFO_EDICAO.PRODEDICAODESCONTO, ENCALHE_INFO_EDICAO.PRODDESCONTO, 0) / 100) END) ");
    		sql.append(" ) AS precoCapaDesconto, 	");
    		sql.append(" CASE WHEN (				");
    		sql.append(" (SELECT CHEN.TIPO_CHAMADA_ENCALHE	");
    		sql.append(" FROM CHAMADA_ENCALHE CHEN          ");
    		sql.append(" WHERE CHEN.PRODUTO_EDICAO_ID = ENCALHE_INFO_EDICAO.PRODUTOEDICAO ");
    		sql.append(" AND CHEN.DATA_RECOLHIMENTO <= :dataRecolhimento                  ");
    		sql.append(" ORDER BY CHEN.DATA_RECOLHIMENTO DESC LIMIT 1) = :tipoChamadaEncalheChamadao) THEN TRUE ELSE FALSE END AS chamadao,    ");
    		sql.append(" CASE WHEN (				");
    		sql.append(" (SELECT CHEN.TIPO_CHAMADA_ENCALHE	");
    		sql.append(" FROM CHAMADA_ENCALHE CHEN          ");
    		sql.append(" WHERE CHEN.PRODUTO_EDICAO_ID = ENCALHE_INFO_EDICAO.PRODUTOEDICAO ");
    		sql.append(" AND CHEN.DATA_RECOLHIMENTO <= :dataRecolhimento                  ");
    		sql.append(" ORDER BY CHEN.DATA_RECOLHIMENTO DESC LIMIT 1) = :tipoChamadaEncalheAntecipada) THEN TRUE ELSE FALSE END AS antecipada,    ");
    		sql.append(" CASE WHEN (						");
    		sql.append(" (SELECT CHEN.TIPO_CHAMADA_ENCALHE	");
    		sql.append(" FROM CHAMADA_ENCALHE CHEN          ");
    		sql.append(" WHERE CHEN.PRODUTO_EDICAO_ID = ENCALHE_INFO_EDICAO.PRODUTOEDICAO	");
    		sql.append(" AND CHEN.DATA_RECOLHIMENTO <= :dataRecolhimento                    ");
    		sql.append(" ORDER BY CHEN.DATA_RECOLHIMENTO DESC LIMIT 1) = :tipoChamadaEncalheMatrizRecolhimento) THEN TRUE ELSE FALSE END AS matrizRecolhimento ");
//			sql.append(" CASE WHEN PLP.TIPO IS NOT NULL THEN PLP.TIPO ELSE NULL END AS recolhimento ");
        	
        }

		sql.append(" FROM (   ");
		
		sql.append(" SELECT   ");
		sql.append(" PE.ID AS produtoEdicao,	");
		sql.append(" PE.DESCONTO_LOGISTICA_ID AS produtoEdicaoDescontoLogisticaId, ");
		sql.append(" P.DESCONTO_LOGISTICA_ID AS produtoDescontoLogisticaId ");

		
		if(!count) {
			sql.append(" , PE.pacote_padrao AS pacotePadrao,     ");
			sql.append(" PE.ORIGEM AS prodEdicaoOrigem,     ");
			sql.append(" PE.DESCONTO AS prodEdicaoDesconto, ");
			sql.append(" P.DESCONTO AS prodDesconto,        ");
			sql.append(" COALESCE(CE.SEQUENCIA, 0) AS sequencia, ");
			sql.append(" P.NOME AS produto,          ");
			sql.append(" P.CODIGO AS codigo,         ");
			sql.append(" PE.NUMERO_EDICAO AS edicao, ");
			sql.append(" PE.PARCIAL AS parcial,      ");
			sql.append(" CE.ID AS chamadaEncalheId,  ");
			sql.append(" PE.ORIGEM AS origem,        ");
			sql.append(" COALESCE(PE.PRECO_VENDA, 0) AS precoCapa, ");
			sql.append(" CASE WHEN PE.PARCIAL = TRUE THEN 'P' ELSE 'N' END AS tipo, ");
			sql.append(" PLP.tipo as recolhimento ");
		}
		
		sql.append(" FROM CHAMADA_ENCALHE_COTA CEC     ignore index(NDX_POSTERGADO)                                 ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE CE ON (CE.ID = CEC.CHAMADA_ENCALHE_ID)  ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE_LANCAMENTO CEL ON (CEL.CHAMADA_ENCALHE_ID = CE.ID) ");
		sql.append(" INNER JOIN LANCAMENTO L ON (CEL.LANCAMENTO_ID = L.ID     AND (L.DATA_REC_DISTRIB = CE.DATA_RECOLHIMENTO or CE.TIPO_CHAMADA_ENCALHE = 'CHAMADAO' )) ");
		sql.append(" LEFT OUTER JOIN PERIODO_LANCAMENTO_PARCIAL PLP ON PLP.ID = L.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" LEFT OUTER JOIN LANCAMENTO_PARCIAL LP ON PLP.LANCAMENTO_PARCIAL_ID = LP.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = CE.PRODUTO_EDICAO_ID)     ");
		sql.append(" INNER JOIN PRODUTO P ON (PE.PRODUTO_ID = P.ID)                     ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR PF ON (PF.PRODUTO_ID = P.ID)         ");
		sql.append(" WHERE ");
		sql.append(" CE.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append(" AND CEC.POSTERGADO = FALSE ");
		
		if(filtro.getFornecedorId()!=null) {
			sql.append("	AND PF.FORNECEDORES_ID = :fornecedorId ");
		}
		
		sql.append(" GROUP BY PE.ID ");
		
		sql.append(" UNION ALL      ");
		
		sql.append(" SELECT         ");
		sql.append(" PE.ID AS produtoEdicao, ");
		sql.append(" PE.DESCONTO_LOGISTICA_ID AS produtoEdicaoDescontoLogisticaId, ");
		sql.append(" P.DESCONTO_LOGISTICA_ID AS produtoDescontoLogisticaId        ");
		
		if(!count) {
			sql.append(" , PE.pacote_padrao as pacotePadrao,    ");
			sql.append("  PE.ORIGEM AS prodEdicaoOrigem,     ");
			sql.append(" PE.DESCONTO AS prodEdicaoDesconto, ");
			sql.append(" P.DESCONTO AS prodDesconto,        ");
			sql.append(" COALESCE(CE.SEQUENCIA, 0) AS sequencia, ");
			sql.append(" P.NOME AS produto,          ");
			sql.append(" P.CODIGO AS codigo,         ");
			sql.append(" PE.NUMERO_EDICAO AS edicao, ");
			sql.append(" PE.PARCIAL AS parcial,      ");
			sql.append(" CE.ID AS chamadaEncalheId,  ");
			sql.append(" PE.ORIGEM AS origem,        ");
			sql.append(" COALESCE(PE.PRECO_VENDA, 0) AS precoCapa, ");
			sql.append(" CASE WHEN PE.PARCIAL = TRUE THEN 'P' ELSE 'N' END AS tipo, ");
			sql.append(" PLP.tipo as recolhimento ");
		}
		
		sql.append(" FROM CONTROLE_CONFERENCIA_ENCALHE CCE                     ");
		sql.append(" INNER JOIN CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ON (CCE.ID = CCEC.CTRL_CONF_ENCALHE_ID)            ");
		sql.append(" INNER JOIN CONFERENCIA_ENCALHE CONFENC ON (CONFENC.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID)   ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE_COTA CEC ON (CEC.ID = CONFENC.CHAMADA_ENCALHE_COTA_ID)                    ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE CE ON (CE.ID = CEC.CHAMADA_ENCALHE_ID)   ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE_LANCAMENTO CEL ON (CEL.CHAMADA_ENCALHE_ID = CE.ID) ");
		sql.append(" INNER JOIN LANCAMENTO L ON (CEL.LANCAMENTO_ID = L.ID AND  (L.DATA_REC_DISTRIB = CE.DATA_RECOLHIMENTO or CE.TIPO_CHAMADA_ENCALHE = 'CHAMADAO' )) ");
		sql.append(" LEFT OUTER JOIN PERIODO_LANCAMENTO_PARCIAL PLP ON PLP.ID = L.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" LEFT OUTER JOIN LANCAMENTO_PARCIAL LP ON PLP.LANCAMENTO_PARCIAL_ID = LP.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = CONFENC.PRODUTO_EDICAO_ID) ");
		sql.append(" INNER JOIN PRODUTO P ON (PE.PRODUTO_ID = P.ID) ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR PF ON (PF.PRODUTO_ID = P.ID) ");
		sql.append(" WHERE ");
		sql.append(" CCE.DATA = :dataRecolhimento	");
		sql.append(" AND CEC.POSTERGADO = FALSE ");
		
		if(filtro.getFornecedorId()!=null) {
			sql.append("	AND PF.FORNECEDORES_ID = :fornecedorId ");
		}
		
		sql.append(" GROUP BY PE.ID ");
		
		sql.append(" ) AS ENCALHE_INFO_EDICAO ");
		
		sql.append(" LEFT JOIN DESCONTO_LOGISTICA DLPE ON (DLPE.ID = ENCALHE_INFO_EDICAO.produtoEdicaoDescontoLogisticaId)  ");
		sql.append(" LEFT JOIN DESCONTO_LOGISTICA DLP ON (DLP.ID = ENCALHE_INFO_EDICAO.produtoDescontoLogisticaId)          ");
		sql.append(" LEFT OUTER JOIN LANCAMENTO_PARCIAL LP ON LP.PRODUTO_EDICAO_ID = ENCALHE_INFO_EDICAO.produtoEdicao    ");
		sql.append(" LEFT OUTER JOIN PERIODO_LANCAMENTO_PARCIAL PLP ON PLP.LANCAMENTO_PARCIAL_ID = LP.ID                  ");
		sql.append(" GROUP BY ENCALHE_INFO_EDICAO.produtoEdicao                                                           ");
        
        if (count) {
            
            sql.append(") as unionEncalheCount");
        }
        
        return sql.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalhe(final FiltroFechamentoEncalheDTO filtro,
            final String sortorder, final String sortname, final Integer page, final Integer rp) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("SELECT distinct  p.codigo as  codigo ");
        hql.append(" , p.nome as produto ");
        hql.append(" , pe.numeroEdicao as edicao");
        
        hql.append(" , coalesce(pe.precoVenda, 0) - (coalesce(pe.precoVenda, 0)  * ( ");
        hql.append("   CASE WHEN pe.origem = :origemInterface ");
        hql.append("   THEN (coalesce(descLogProdEdicao.percentualDesconto, descLogProd.percentualDesconto, 0 ) /100 ) ");
        hql.append("   ELSE (coalesce(pe.desconto, p.desconto, 0) / 100) END ");
        hql.append("   )) as precoCapaDesconto ");
        
        hql.append(" , coalesce(pe.precoVenda, 0) as precoCapa ");
        
        hql.append(" , pe.id as produtoEdicao ");
        hql.append(" , case when  pe.parcial  = true  then 'P' else 'N' end  as tipo ");
        
        hql.append(" , che.dataRecolhimento as dataRecolhimento ");
        
        hql.append(" , sum(mec.qtde) - ( "+ this.getQueryVendaProduto()  +" )    as exemplaresDevolucao ");
        
        hql.append(" , che.sequencia as sequencia ");
        
        hql.append(this.getFromConferenciaEncalhe());
        
        hql.append(this.getWhereFechamentoEncalhe(filtro));
        
        if (sortname != null) {
            hql.append(" order by ");
            if ("asc".equalsIgnoreCase(sortorder)) {
                hql.append(sortname).append(" asc ");
            } else {
                hql.append(sortname).append(" desc ");
            }
        }
        
        final Query query =  getSession().createQuery(hql.toString());
        
        query.setDate("dataEncalhe", filtro.getDataEncalhe());
        query.setParameter("tipoVenda", TipoVendaEncalhe.ENCALHE);
        query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);
        query.setParameter("origemInterface", Origem.INTERFACE);
        query.setParameter("statusOperacaoFinalizada", StatusOperacao.CONCLUIDO);
        
        if (filtro.getBoxId() != null) {
            query.setLong("boxId", filtro.getBoxId());
        }
        
        if (filtro.getFornecedorId() != null) {
            query.setLong("fornecedorId", filtro.getFornecedorId());
        }
        
        if (page != null){
            query.setFirstResult(page);
        }
        
        if (rp != null){
            query.setMaxResults(rp);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(FechamentoFisicoLogicoDTO.class));
        
        return query.list();
    }
    
    @Override
    public int buscarQuantidadeConferenciaEncalheNovo(final FiltroFechamentoEncalheDTO filtro) {
        
        final Query query =  getSession().createSQLQuery(this.getQueryFechamentoEncalhe(filtro, true));
        
        query.setParameter("dataRecolhimento", filtro.getDataEncalhe());
        
        if (filtro.getFornecedorId() != null) {
            
            query.setLong("fornecedorId", filtro.getFornecedorId());
        }
        
        return ((BigInteger) query.uniqueResult()).intValue();
    }
    
    @Override
    public Boolean buscaControleFechamentoEncalhe(final Date dataEncalhe) {
        
        final StringBuilder hql = new StringBuilder("select count(cfe.id) ");
        hql.append(" from ControleFechamentoEncalhe cfe ")
        .append(" where cfe.dataEncalhe = :dataEncalhe ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("dataEncalhe", dataEncalhe);
        
        return (Long)query.uniqueResult() != 0;
    }
    
    @Override
    public Boolean buscaControleConferenciaEncalhe(final Date dataEncalhe) {
        
        final Criteria criteria = this.getSession().createCriteria(ControleConferenciaEncalhe.class, "cce");
        
        criteria.add(Restrictions.eq("cce.data", dataEncalhe));
        
        return !criteria.list().isEmpty();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Date> obterDatasControleFechamentoEncalheRealizado(final Date dataDe, final Date dataAte) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select controle.dataEncalhe from ControleFechamentoEncalhe controle ");
        hql.append(" where controle.dataEncalhe between :dataDe and :dataAte ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataDe", dataDe);
        
        query.setParameter("dataAte", dataAte);
        
        return query.list();
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<FechamentoEncalhe> buscarFechamentoEncalhe(final Date dataEncalhe) {
        
        final Criteria criteria = this.getSession().createCriteria(FechamentoEncalhe.class);
        criteria.add(Restrictions.eq("fechamentoEncalhePK.dataEncalhe", dataEncalhe));
        criteria.setFetchMode("listFechamentoEncalheBox", FetchMode.JOIN);
        
        return criteria.list();
    }
    
    
    @Override
    public BigInteger buscarQtdeFechamentoEncalhe(final Date dataEncalhe,Long produtoEdicaoId) {
        
    	   final StringBuilder sql = new StringBuilder();
           
           sql.append(" select coalesce(quantidade,0) as quantidade " );
           sql.append(" from FECHAMENTO_ENCALHE f ");
           sql.append(" where f.DATA_ENCALHE = :dataEncalhe and ");
           sql.append(" f.produto_edicao_id  = :produtoEdicaoId ");
           
           final Query query = getSession().createSQLQuery(sql.toString());
           
           ((SQLQuery) query ).addScalar("quantidade", BigIntegerType.INSTANCE);
           
           query.setParameter("dataEncalhe", dataEncalhe);
           query.setParameter("produtoEdicaoId", produtoEdicaoId);
           
           return (BigInteger) query.uniqueResult();
    }
    
    @Override
    public boolean verificarExistenciaFechamentoEncalheConsolidado(final Date dataEncalhe) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select case when (count(f.PRODUTO_EDICAO_ID) > 0)  then true else false end as verificacao " );
        sql.append(" from FECHAMENTO_ENCALHE f ");
        sql.append(" where f.DATA_ENCALHE = :dataEncalhe and ");
        sql.append(" f.QUANTIDADE > 0 ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        ((SQLQuery) query ).addScalar("verificacao", BooleanType.INSTANCE);
        
        query.setParameter("dataEncalhe", dataEncalhe);
        
        return (Boolean) query.uniqueResult();
        
    }
    
    @Override
    public Integer obterTotalCotasAusentes(final Date dataEncalhe, final DiaSemana diaRecolhimento,
            final boolean isSomenteCotasSemAcao, final String sortorder, final String sortname, final int page, final int rp,
            Integer numeroCota) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(idCota) from  ");
        
        sql.append(" ( ");
        boolean fechado=false;
        fechado = isChamadaEncalheFechada(dataEncalhe);
        if (fechado ) {
	        sql.append(getSqlCotaAusenteComChamadaEncalheFechado(true, isSomenteCotasSemAcao, numeroCota).toString());
	        
	        sql.append(" union all ");
       
	        sql.append(getSqlCotaAusenteSemChamadaEncalheFechado(true, false, numeroCota).toString());
        } else {
        	 sql.append(getSqlCotaAusenteComChamadaEncalhe(true, isSomenteCotasSemAcao, numeroCota).toString());
 	        
 	        sql.append(" union all ");
        
 	        sql.append(getSqlCotaAusenteSemChamadaEncalhe(true, false, numeroCota).toString());
         }
        	
        
        sql.append(" ) as ausentes	");
        
        final Query query = this.getSession().createSQLQuery(sql.toString());
        
        query.setParameter("dataEncalhe", dataEncalhe);
        
        query.setParameter("principal", true);
        
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO.name());
        
        query.setParameter("inativo", SituacaoCadastro.INATIVO.name());
        
        query.setParameter("pendente", SituacaoCadastro.PENDENTE.name());
        
        query.setParameter("diaRecolhimento", diaRecolhimento.name());
        
        query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
        
        if (numeroCota != null){
            
            query.setParameter("numeroCota", numeroCota);
        }
        
        final BigInteger qtde = (BigInteger) query.uniqueResult();
        
        return qtde != null ? qtde.intValue() : 0;
        
    }
    
    private StringBuilder getSqlCotaAusenteComChamadaEncalhe(final boolean indCount, final boolean isSomenteCotasSemAcao,
            Integer numeroCota) {
        
        final StringBuilder sql = new StringBuilder();
        
        if(indCount) {
            
            sql.append("	 select                                                             ");
            sql.append("        cota.ID as idCota,                                              ");
            sql.append("        false as indMFCNaoConsolidado                                   ");
            
        } else {
            
            sql.append("	 select                                                             ");
            sql.append("        cota.ID as idCota,                                              ");
            sql.append("        cota.NUMERO_COTA as numeroCota,                                 ");
            sql.append("        coalesce(if(pessoa.tipo = 'F',pessoa.NOME,                                           ");
            sql.append("        pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia) as colaboradorName,                        ");
            sql.append("        box.NOME as boxName,                                            ");
            sql.append("        roteiro.DESCRICAO_ROTEIRO as roteiroName,                       ");
            sql.append("        rota.DESCRICAO_ROTA as rotaName,                                ");
            sql.append(" 		true as indPossuiChamadaEncalheCota, 							");
            sql.append(" 		false as indMFCNaoConsolidado, 									");
            
            sql.append(" 		case when (grupoCota.id is null) then false			");
            sql.append(" 		else true end as operacaoDiferenciada,				");
            
            sql.append("        coalesce(chamadaEncalheCota.FECHADO, 0)  as fechado,            ");
            sql.append("        coalesce(chamadaEncalheCota.POSTERGADO, 0) as postergado,       		");
            sql.append("        coalesce(chamadaEncalhe.DATA_RECOLHIMENTO, :dataEncalhe) as dataEncalhe, ");
            sql.append("		(select count(cu.id) from COTA_UNIFICACAO cu ");
            sql.append("		join COTAUNIFICACAO_COTAUNIFICADA co_un on (cu.ID = co_un.COTA_UNIFICACAO_ID) ");
            sql.append("		where cu.COTA_ID = cota.ID or ");
            sql.append("		co_un.COTA_UNIFICADA_ID = cota.ID > 0) as unificacao ");
        }
        
        sql.append("	from                                                                ");
        sql.append("        Cota cota                                                       ");
        sql.append("	inner join                                                          ");
        sql.append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota                         ");
        sql.append("            on chamadaEncalheCota.COTA_ID=cota.ID                       ");
        
        sql.append("            and ( cota.TIPO_COTA <> :tipoCotaAVista OR cota.DEVOLVE_ENCALHE = true ) ");
        				
        sql.append("	inner join                                                          ");
        sql.append("        CHAMADA_ENCALHE chamadaEncalhe                                  ");
        sql.append("            on chamadaEncalheCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID  ");
        sql.append("	inner join                                                          ");
        sql.append("        PESSOA pessoa                                                   ");
        sql.append("            on cota.PESSOA_ID=pessoa.ID                                 ");
        sql.append("	inner join                                                          ");
        sql.append("        BOX box                                                         ");
        sql.append("            on cota.BOX_ID=box.ID                                       ");
        sql.append("	inner join                                                          ");
        sql.append("        PDV pdv                                                         ");
        sql.append("            on cota.ID=pdv.COTA_ID                                      ");
        sql.append("    left outer join                                                     ");
        sql.append("        ROTA_PDV rotaPdv                                                ");
        sql.append("            on pdv.ID=rotaPdv.PDV_ID                                    ");
        sql.append("        and rotaPdv.ROTA_ID not in ( select ro.id from Rota ro inner join Roteiro  rt on ro.roteiro_id  = rt.id and rt.tipo_roteiro = 'ESPECIAL')");

        sql.append("	left outer join                                                     ");
        sql.append("    	ROTA rota                                                       ");
        sql.append("    		on rotaPdv.ROTA_ID=rota.ID                                  ");
     
        sql.append("    left outer join                                                     ");
        sql.append("        ROTEIRO roteiro                                                 ");
        sql.append("        	on rota.ROTEIRO_ID=roteiro.ID                               ");
        
        sql.append("	left outer join COTA_GRUPO cotaGrupo	");
        sql.append("	on cotaGrupo.cota_id = cota.id			");
        
        sql.append("	left outer join GRUPO_COTA grupoCota 		");
        sql.append("	on grupoCota.id = cotaGrupo.grupo_cota_id	");
        
        sql.append("	left outer join DIA_RECOLHIMENTO_GRUPO_COTA diaRecolhimentoGrupoCota	");
        sql.append("	on grupoCota.id = diaRecolhimentoGrupoCota.grupo_id						");
        
        sql.append("    where (grupoCota.DATA_VIGENCIA_INICIO is null or grupoCota.DATA_VIGENCIA_INICIO <= :dataEncalhe) ");
        sql.append("           and (grupoCota.DATA_VIGENCIA_FIM is null or grupoCota.DATA_VIGENCIA_FIM >= :dataEncalhe) and ");
        
        sql.append("    (	grupoCota.id is null		");
        sql.append("    or diaRecolhimentoGrupoCota.dia_id = :diaRecolhimento ) and	");
        
        sql.append("        chamadaEncalhe.DATA_RECOLHIMENTO = 	:dataEncalhe                ");
        sql.append("        and cota.ID not in  (                                           ");
        sql.append("            select                                                      ");
        sql.append("                distinct( cec.COTA_ID )                                 ");
        sql.append("            from                                                        ");
        sql.append("                controle_conferencia_encalhe_cota cec                   ");
        sql.append("            where                                                       ");
        sql.append("                cec.data_operacao = :dataEncalhe                        ");
        sql.append("        )                                                               ");
        sql.append("		and pdv.PONTO_PRINCIPAL = :principal                            ");
        
        
        if (isSomenteCotasSemAcao) {
            
            sql.append(" and ( chamadaEncalheCota.FECHADO = false or chamadaEncalheCota.FECHADO is null ) 		");
            
            sql.append(" and ( chamadaEncalheCota.POSTERGADO = false or chamadaEncalheCota.POSTERGADO is null ) ");
        }
        
        if (numeroCota != null){
            
            sql.append(" and cota.NUMERO_COTA = :numeroCota ");
        }
        
        sql.append("	group by                                      ");
        sql.append("        cota.ID                                   ");
        
        
        return sql;
    }
    
    private StringBuilder getSqlCotaAusenteComChamadaEncalheSemPostergado(final boolean indCount,
            final boolean isSomenteCotasSemAcao,
            final boolean ignorarUnificacao) {
        
        final StringBuilder sql = new StringBuilder();
        
        if(indCount) {
            
            sql.append("	 select                                                             ");
            sql.append("        cota.ID as idCota,                                              ");
            sql.append("        false as indMFCNaoConsolidado                                   ");
            
        } else {
            
            sql.append("	 select                                                             ");
            sql.append("        cota.ID as idCota,                                              ");
            sql.append("        cota.NUMERO_COTA as numeroCota,                                 ");
            sql.append("        coalesce(if(pessoa.tipo = 'F',pessoa.NOME,                                           ");
            sql.append("        pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia) as colaboradorName,                        ");
            sql.append("        box.NOME as boxName,                                            ");
            sql.append("        roteiro.DESCRICAO_ROTEIRO as roteiroName,                       ");
            sql.append("        rota.DESCRICAO_ROTA as rotaName,                                ");
            sql.append(" 		true as indPossuiChamadaEncalheCota, 							");
            sql.append(" 		false as indMFCNaoConsolidado, 									");
            
            sql.append(" 		case when (grupoCota.id is null) then false			");
            sql.append(" 		else true end as operacaoDiferenciada,				");
            sql.append("        coalesce(chamadaEncalheCota.FECHADO, 0)  as fechado,            ");
            sql.append("        coalesce(chamadaEncalheCota.POSTERGADO, 0) as postergado,       		");
            sql.append("        coalesce(chamadaEncalhe.DATA_RECOLHIMENTO, :dataEncalhe) as dataEncalhe ");
            
            
        }
        
        sql.append("	from                                                                ");
        sql.append("        Cota cota                                                       ");
        sql.append("	inner join                                                          ");
        sql.append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota                         ");
        sql.append("            on chamadaEncalheCota.COTA_ID=cota.ID                       ");
        
        sql.append("            and ( cota.TIPO_COTA <> :tipoCotaAVista OR cota.DEVOLVE_ENCALHE = true ) ");
        
        sql.append("	inner join                                                          ");
        sql.append("        CHAMADA_ENCALHE chamadaEncalhe                                  ");
        sql.append("            on chamadaEncalheCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID  ");
        sql.append("	inner join                                                          ");
        sql.append("        PESSOA pessoa                                                   ");
        sql.append("            on cota.PESSOA_ID=pessoa.ID                                 ");
        sql.append("	inner join                                                          ");
        sql.append("        BOX box                                                         ");
        sql.append("            on cota.BOX_ID=box.ID                                       ");
        sql.append("	inner join                                                          ");
        sql.append("        PDV pdv                                                         ");
        sql.append("            on cota.ID=pdv.COTA_ID                                      ");
        sql.append("    left outer join                                                     ");
        sql.append("        ROTA_PDV rotaPdv                                                ");
        sql.append("            on pdv.ID=rotaPdv.PDV_ID                                    ");
        sql.append("        and rotaPdv.ROTA_ID not in ( select ro.id from Rota ro inner join Roteiro  rt on ro.roteiro_id  = rt.id and rt.tipo_roteiro = 'ESPECIAL')");

        sql.append("	left outer join                                                     ");
        sql.append("    	ROTA rota                                                       ");
        sql.append("    		on rotaPdv.ROTA_ID=rota.ID                                  ");
        sql.append("    left outer join                                                     ");
        sql.append("        ROTEIRO roteiro                                                 ");
        sql.append("        	on rota.ROTEIRO_ID=roteiro.ID                               ");
        
        sql.append("	left outer join COTA_GRUPO cotaGrupo	");
        sql.append("	on cotaGrupo.cota_id = cota.id			");
        
        sql.append("	left outer join GRUPO_COTA grupoCota 		");
        sql.append("	on grupoCota.id = cotaGrupo.grupo_cota_id	");
        
        sql.append("	left outer join DIA_RECOLHIMENTO_GRUPO_COTA diaRecolhimentoGrupoCota	");
        sql.append("	on grupoCota.id = diaRecolhimentoGrupoCota.grupo_id						");
        
        
        sql.append("    where                                                               ");
        
        sql.append("    (	grupoCota.id is null		");
        sql.append("    or diaRecolhimentoGrupoCota.dia_id = :diaRecolhimento ) and	");
        
        sql.append("        chamadaEncalhe.DATA_RECOLHIMENTO = 	:dataEncalhe                ");
        sql.append("        and chamadaEncalheCota.postergado = :postergadoCota             ");
        sql.append("        and cota.ID not in  (                                           ");
        sql.append("            select                                                      ");
        sql.append("                distinct( cec.COTA_ID )                                 ");
        sql.append("            from                                                        ");
        sql.append("                controle_conferencia_encalhe_cota cec                   ");
        sql.append("            where                                                       ");
        sql.append("                cec.data_operacao = :dataEncalhe                        ");
        sql.append("        )                                                               ");
        
        if (ignorarUnificacao){
            
            sql.append("	and cota.ID not in ( 											");
            sql.append("		select aoada.COTA_UNIFICADA_ID								");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada) 			 	");
            
            sql.append("	and cota.ID not in ( 											");
            sql.append("		select uni.cota_id 											");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada join COTA_UNIFICACAO uni ON (aoada.COTA_UNIFICACAO_ID = uni.ID	)) ");
            
        }
        
        sql.append("		and pdv.PONTO_PRINCIPAL = :principal                            ");
        
        if (isSomenteCotasSemAcao) {
            
            sql.append(" and ( chamadaEncalheCota.FECHADO = false or chamadaEncalheCota.FECHADO is null ) 		");
            
            sql.append(" and ( chamadaEncalheCota.POSTERGADO = false or chamadaEncalheCota.POSTERGADO is null ) ");
        }
        
        sql.append("	group by                                      ");
        sql.append("        cota.ID                                   ");
        
        
        return sql;
    }
    
    
    private StringBuilder getSqlCotaAusenteSemChamadaEncalhe(final boolean indCount,
            final boolean ignorarUnificacao, Integer numeroCota) {
        
        final StringBuilder sqlMovimentoFinaceiroCotaNaoConsolidado = new StringBuilder();
        
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("( select count(mfc.id)>0  ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" from ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" movimento_financeiro_cota mfc ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" where ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	mfc.cota_id = cota.id ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.data <= :dataEncalhe ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.STATUS = :statusAprovacao ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.id not in ( ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("                       select cmfc.MVTO_FINANCEIRO_COTA_ID from ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("                       CONSOLIDADO_MVTO_FINANCEIRO_COTA cmfc  where cmfc.MVTO_FINANCEIRO_COTA_ID = mfc.id )");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" ) ");
        
        final StringBuilder sql = new StringBuilder();
        
        if(indCount) {
            
            sql.append(" 	select                                                  ");
            sql.append("    cota.ID as idCota,                                      ");
            sql.append(sqlMovimentoFinaceiroCotaNaoConsolidado.toString()).append(" as indMFCNaoConsolidado ");
            
        } else {
            
            sql.append(" 	select                                                  ");
            sql.append("    cota.ID as idCota,                                      ");
            sql.append("    cota.NUMERO_COTA as numeroCota,                         ");
            sql.append("    coalesce(if(pessoa.tipo = 'F',pessoa.NOME,                                   ");
            sql.append("    pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia) as colaboradorName,                ");
            sql.append("    box.NOME as boxName,                                    ");
            sql.append("    roteiro.DESCRICAO_ROTEIRO as roteiroName,               ");
            sql.append("    rota.DESCRICAO_ROTA as rotaName,                        ");
            sql.append(" 	false as indPossuiChamadaEncalheCota, 					");
            
            sql.append(sqlMovimentoFinaceiroCotaNaoConsolidado.toString()).append(" as indMFCNaoConsolidado, ");
            
            sql.append(" 		case when (grupoCota.id is null) then false			");
            sql.append(" 		else true end as operacaoDiferenciada,				");
            
            
            sql.append("   false as fechado, ");
            sql.append("   false as postergado, ");
            sql.append("   coalesce(:dataEncalhe) as dataEncalhe, ");
            sql.append("		(select count(cu.ID) from COTA_UNIFICACAO cu ");
            sql.append("		join COTAUNIFICACAO_COTAUNIFICADA co_un on (cu.ID = co_un.COTA_UNIFICACAO_ID) ");
            sql.append("		where cu.COTA_ID = cota.ID or ");
            sql.append("		co_un.COTA_UNIFICADA_ID = cota.ID > 0) as unificacao ");
            
        }
        
        sql.append("	from                                                    ");
        sql.append("        Cota cota                                           ");
        sql.append("	inner join                                              ");
        sql.append("        PESSOA pessoa                                       ");
        sql.append("            on cota.PESSOA_ID=pessoa.ID                     ");
        
        sql.append("            and cota.TIPO_COTA <> :tipoCotaAVista ");
        
        sql.append("	inner join                                              ");
        sql.append("        BOX box                                             ");
        sql.append("            on cota.BOX_ID=box.ID                           ");
        sql.append("	inner join                                              ");
        sql.append("        PDV pdv                                             ");
        sql.append("            on cota.ID=pdv.COTA_ID                          ");
        sql.append("    left outer join                                         ");
        sql.append("        ROTA_PDV rotaPdv                                    ");
        sql.append("            on pdv.ID=rotaPdv.PDV_ID                        ");
        sql.append("        and rotaPdv.ROTA_ID not in ( select ro.id from Rota ro inner join Roteiro  rt on ro.roteiro_id  = rt.id and rt.tipo_roteiro = 'ESPECIAL')");

        sql.append("	left outer join                                         ");
        sql.append("    	ROTA rota                                           ");
        sql.append("    		on rotaPdv.ROTA_ID=rota.ID                      ");
        sql.append("    left outer join                                         ");
        sql.append("        ROTEIRO roteiro                                     ");
        sql.append("        	on rota.ROTEIRO_ID=roteiro.ID                   ");
        
        sql.append("	left outer join COTA_GRUPO cotaGrupo	");
        sql.append("	on cotaGrupo.cota_id = cota.id			");
        
        sql.append("	left outer join GRUPO_COTA grupoCota 		");
        sql.append("	on grupoCota.id = cotaGrupo.grupo_cota_id	");
        
        sql.append("	left outer join DIA_RECOLHIMENTO_GRUPO_COTA diaRecolhimentoGrupoCota	");
        sql.append("	on grupoCota.id = diaRecolhimentoGrupoCota.grupo_id						");
        
        sql.append("    where  ");
        
        sql.append("	cota.SITUACAO_CADASTRO <> :inativo and cota.SITUACAO_CADASTRO <> :pendente ");
        
        
        sql.append("    and (	grupoCota.id is null		");
        sql.append("    or diaRecolhimentoGrupoCota.dia_id = :diaRecolhimento )	");
        
        
        if (ignorarUnificacao){
            
            sql.append("	and cota.ID not in ( 								");
            sql.append("		select aoada.COTA_UNIFICADA_ID					");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada)	");
            
            sql.append("	and cota.ID not in ( 								");
            sql.append("		select uni.cota_id 								");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada join COTA_UNIFICACAO uni ON (aoada.COTA_UNIFICACAO_ID = uni.ID	)) ");
            
        }
        
        sql.append("	and cota.ID not in  (                                   ");
        sql.append("            select                                          ");
        sql.append("                distinct( cec.COTA_ID )                     ");
        sql.append("            from                                            ");
        sql.append("                controle_conferencia_encalhe_cota cec       ");
        sql.append("            where                                           ");
        sql.append("                cec.data_operacao = :dataEncalhe            ");
        sql.append("        )                                                   ");
        sql.append("		and pdv.PONTO_PRINCIPAL = :principal                ");
                                                                                   
        
        if (numeroCota != null){
            
            sql.append("and cota.NUMERO_COTA = :numeroCota ");
        }
        
        sql.append("	group by    ");
        
        sql.append("    cota.ID, indMFCNaoConsolidado ");
        
        sql.append(" having indMFCNaoConsolidado = true ");
        sql.append("		and cota.ID not in (                                ");
        sql.append("			select                                          ");
        sql.append("				distinct(cota.ID )                                   ");
        sql.append("			from                                            ");
        sql.append("		        Cota cota                                   ");
        sql.append("			inner join                                      ");
        sql.append("		        CHAMADA_ENCALHE_COTA chamadaEncalheCota     ");
        sql.append("		            on chamadaEncalheCota.COTA_ID=cota.ID   ");
        sql.append("			inner join                                                                     ");
        sql.append("			CHAMADA_ENCALHE chamadaEncalhe                                                 ");
        sql.append("		            on ( chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID and     ");
        sql.append("						 chamadaEncalhe.DATA_RECOLHIMENTO = :dataEncalhe )                 ");
        sql.append("		)     ");
        
        return sql;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaAusenteEncalheDTO> obterCotasAusentes(final Date dataEncalhe, final DiaSemana diaRecolhimento,
            final boolean isSomenteCotasSemAcao, final String sortorder, String sortname, final int page, final int rp) {
        
        final StringBuilder sql = new StringBuilder();
        
        boolean fechado=false;
        fechado = isChamadaEncalheFechada(dataEncalhe);
        if (fechado ) {
	        sql.append(getSqlCotaAusenteComChamadaEncalheFechado(false, isSomenteCotasSemAcao, null).toString());
	        
	        sql.append(" union all ");
	        
	        sql.append(getSqlCotaAusenteSemChamadaEncalheFechado(false, false, null).toString());
	        
        } else {
        	  sql.append(getSqlCotaAusenteComChamadaEncalhe(false, isSomenteCotasSemAcao, null).toString());
              
              sql.append(" union all ");
              
              sql.append(getSqlCotaAusenteSemChamadaEncalhe(false, false, null).toString());
        
        }
        if("acao".equals(sortname)) {
            
            sortname = "fechado";
        }
        
        if (sortname != null && sortorder != null) {
            
            sql.append("  ORDER BY " + sortname + " " + sortorder);
        }
        
        final Query query = this.getSession().createSQLQuery(sql.toString());
        
        ((SQLQuery) query).addScalar("idCota", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("colaboradorName", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("boxName", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("roteiroName", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("rotaName", StandardBasicTypes.STRING);
        
        ((SQLQuery) query).addScalar("indPossuiChamadaEncalheCota", StandardBasicTypes.BOOLEAN);
        
        ((SQLQuery) query).addScalar("operacaoDiferenciada", StandardBasicTypes.BOOLEAN);
        
        ((SQLQuery) query).addScalar("fechado", StandardBasicTypes.BOOLEAN);
        
        ((SQLQuery) query).addScalar("indMFCNaoConsolidado", StandardBasicTypes.BOOLEAN);
        
        ((SQLQuery) query).addScalar("postergado", StandardBasicTypes.BOOLEAN);
        
        ((SQLQuery) query).addScalar("dataEncalhe", StandardBasicTypes.DATE);
        
        ((SQLQuery) query).addScalar("unificacao", StandardBasicTypes.BOOLEAN);
        
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO.name());
        
        query.setParameter("dataEncalhe", dataEncalhe);
        
        query.setParameter("principal", true);
        
        query.setParameter("inativo", SituacaoCadastro.INATIVO.name());
        
        query.setParameter("pendente", SituacaoCadastro.PENDENTE.name());
        
        query.setParameter("diaRecolhimento", diaRecolhimento.name());
        
        query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaAusenteEncalheDTO.class));
        
        query.setFirstResult(page);
        
        if (rp >= 0) {
            query.setMaxResults(rp);
        }
        
        return query.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<FechamentoFisicoLogicoDTO> buscarValorTotalEncalhe(final Date dataEncalhe, final Long idCota) {
        
        try {
            
            final Criteria criteria = this.getSession().createCriteria(ChamadaEncalhe.class, "ce");
            
            criteria.setProjection(Projections.projectionList()
                    .add(Projections.property("pe.precoVenda"), "precoCapa")
                    .add(Projections.property("cec.qtdePrevista"), "exemplaresDevolucao")
                    );
            
            criteria.createAlias("ce.chamadaEncalheCotas", "cec");
            criteria.setFetchMode("cec", FetchMode.JOIN);
            
            criteria.createAlias("ce.produtoEdicao", "pe");
            criteria.setFetchMode("pe", FetchMode.JOIN);
            
            criteria.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
            criteria.add(Restrictions.eq("cec.cota.id", idCota));
            
            criteria.setResultTransformer(Transformers.aliasToBean(FechamentoFisicoLogicoDTO.class));
            
            return criteria.list();
            
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void salvarControleFechamentoEncalhe(
            final ControleFechamentoEncalhe controleFechamentoEncalhe) {
        this.getSession().save(controleFechamentoEncalhe);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ChamadaEncalheCota> buscarChamadaEncalheCota(final Date dataEncalhe, final Long idCota) {
        
        final Criteria criteria = this.getSession().createCriteria(ChamadaEncalheCota.class, "cec");
        
        criteria.createAlias("cec.chamadaEncalhe", "ce");
        criteria.setFetchMode("ce", FetchMode.JOIN);
        
        criteria.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
        criteria.add(Restrictions.eq("cec.cota.id", idCota));
        criteria.add(Restrictions.eq("cec.fechado", false));
        criteria.add(Restrictions.eq("cec.postergado", false));
        
        return criteria.list();
    }
    
    @Override
    public int buscaQuantidadeConferencia(final Date dataEncalhe, final boolean porBox) {
        
        final Criteria criteria = this.getSession().createCriteria(ConferenciaEncalhe.class, "ce");
        
        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("mec.produtoEdicao.id"))
                .add(Projections.groupProperty("ccec.box.id"))
                );
        
        criteria.createAlias("ce.movimentoEstoqueCota", "mec");
        criteria.setFetchMode("mec", FetchMode.SELECT);
        
        criteria.createAlias("ce.controleConferenciaEncalheCota", "ccec");
        criteria.setFetchMode("ccec", FetchMode.SELECT);
        
        criteria.add(Restrictions.eq("ccec.dataOperacao", dataEncalhe));
        
        return criteria.list().size();
    }
    
    @Override
    public Date obterChamdasEncalhePostergadas(final Long idCota, final Date dataEncalhe) {
        
        final Criteria query = this.getSession().createCriteria(ChamadaEncalhe.class, "ce");
        
        query.createAlias("ce.chamadaEncalheCotas", "cec");
        
        query.setFetchMode("cec", FetchMode.JOIN);
        
        query.add(Restrictions.eq("cec.cota.id", idCota));
        query.add(Restrictions.gt("ce.dataRecolhimento", dataEncalhe));
        query.add(Restrictions.eq("cec.postergado", false));
        
        query.setProjection(Projections.alias(Projections.min("ce.dataRecolhimento"), "dataRecolhimento"));
        
        final DetachedCriteria subquery = DetachedCriteria.forClass(ChamadaEncalhe.class, "ce");
        
        subquery.createAlias("ce.chamadaEncalheCotas", "cec");
        
        subquery.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
        subquery.add(Restrictions.eq("cec.cota.id", idCota));
        subquery.setProjection(Projections.alias(Projections.property("ce.produtoEdicao.id"), "idProdutoEdicao"));
        
        query.add(Property.forName("ce.produtoEdicao.id").in(subquery));
        
        return (Date) query.uniqueResult();
    }
    
    @Override
    public ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(final Date dataFechamentoEncalhe) {
        final Criteria criteria = getSession().createCriteria(ControleFechamentoEncalhe.class);
        criteria.add(Restrictions.eq("dataEncalhe", dataFechamentoEncalhe));
        return (ControleFechamentoEncalhe) criteria.uniqueResult();
    }
    
    @Override
    public Date buscaDataUltimoControleFechamentoEncalhe() {
        final Criteria criteria = getSession().createCriteria(ControleFechamentoEncalhe.class);
        criteria.setProjection(Projections.max("dataEncalhe"));
        return (Date) criteria.uniqueResult();
    }
    
    @Override
    public Date buscarUltimoFechamentoEncalheDia(final Date dataFechamentoEncalhe) {
        final Criteria criteria = getSession().createCriteria(FechamentoEncalhePK.class);
        criteria.add(Restrictions.eq("dataEncalhe", dataFechamentoEncalhe));
        criteria.setProjection(Projections.max("dataEncalhe"));
        return (Date) criteria.uniqueResult();
    }
    
    
    
    @Override
    public BigDecimal obterValorTotalAnaliticoEncalhe(final FiltroFechamentoEncalheDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("   SELECT  ");
        
        hql.append("   sum( coalesce(mec.qtde, 0) * coalesce(mec.valoresAplicados.precoComDesconto, 0) ) ");
        
        getQueryAnalitico(filtro, hql);
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("dataEncalhe", filtro.getDataEncalhe());
        
        query.setParameter("statusOperacaoFinalizada", StatusOperacao.CONCLUIDO);
        
        if (filtro.getBoxId() != null) {
            query.setParameter("boxId", filtro.getBoxId());
        }
        
        if (filtro.getFornecedorId() != null) {
            query.setParameter("fornecedorId", filtro.getFornecedorId());
        }
        
        return (BigDecimal) query.uniqueResult();
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(final FiltroFechamentoEncalheDTO filtro,
            final String sortorder, final String sortname, final Integer page, final Integer rp ) {
        
        final String hqlCobrancaCotaAVista = "select d.status from Cobranca c " +
        		" join c.cota cc " +
        		" join c.divida d " +
        		" where cc.id = cota.id " +
        		" and c.dataEmissao = :dataEncalhe " +
        		" group by c.dataEmissao ";
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("   SELECT  ");
        
        hql.append("	cota.numeroCota as numeroCota,			");
        
        hql.append("    coalesce(pess.nome, pess.razaoSocial, '') as nomeCota, 	");
        
        hql.append("	box.nome as boxEncalhe, 	");
        
        hql.append("    ROUND(sum( coalesce(mec.qtde, 0)  *  coalesce(mec.valoresAplicados.precoComDesconto, 0)  ),2) as total ");
        
        hql.append("   , coalesce(div.status, (");
        
        hql.append(hqlCobrancaCotaAVista);
        
        hql.append("), 'POSTERGADA') as statusCobranca, ");
        
        hql.append("	u.nome as usuario,			");
        
        hql.append("	TIME(controle.dataInicio) as inicio, ");
        
        hql.append("	TIME(controle.dataFim) as fim, ");
        
        hql.append("	controle.id as id, ");
        
        hql.append(" ( ");
        hql.append(getQueryQuantidadeHistorico());
        hql.append(" ) as quantidade ");
        getQueryAnalitico(filtro, hql);
        
        hql.append("   group by cota.id ");
        
        if (sortname != null) {
            hql.append(" order by ");
            if ("asc".equalsIgnoreCase(sortorder)) {
                hql.append(sortname+" asc ");
            } else if ("desc".equalsIgnoreCase(sortorder)) {
                hql.append(sortname+" desc ");
            }
            
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliticoEncalheDTO.class));
        
        query.setParameter("dataEncalhe", filtro.getDataEncalhe());
        
        query.setParameter("statusOperacaoFinalizada", StatusOperacao.CONCLUIDO);
        
        if (filtro.getBoxId() != null) {
            query.setParameter("boxId", filtro.getBoxId());
        }
        
        if (filtro.getFornecedorId() != null) {
            query.setParameter("fornecedorId", filtro.getFornecedorId());
        }
        
        if (page != null){
            query.setFirstResult(page);
        }
        
        if (rp != null){
            query.setMaxResults(rp);
        }
        
        return query.list();
    }

    private StringBuilder getQueryQuantidadeHistorico(){
        
        final StringBuilder subquery = new StringBuilder();
        
        subquery.append(" select count(hi.id ) ");
        
        subquery.append(" from HistoricoConfChamadaEncalheCota hi ");
        
        subquery.append(" where hi.dataOperacao = :dataEncalhe ");
        
        subquery.append(" and hi.cota.id = cota.id ");
        subquery.append(" group by hi.dataOperacao ");
        return subquery;
    }
    
    @Override
    public Integer buscarTotalAnaliticoEncalhe(	final FiltroFechamentoEncalheDTO filtro) {
        final StringBuilder hql = new StringBuilder();
        hql.append("   SELECT count( distinct cota.id )  ");
        
        getQueryAnalitico(filtro, hql);
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("dataEncalhe", filtro.getDataEncalhe());
        
        query.setParameter("statusOperacaoFinalizada", StatusOperacao.CONCLUIDO);
        
        if (filtro.getBoxId() != null) {
            query.setLong("boxId", filtro.getBoxId());
        }
        
        if (filtro.getFornecedorId() != null) {
            query.setLong("fornecedorId", filtro.getFornecedorId());
        }
        
        return ((Long)query.uniqueResult()).intValue();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Cota> buscarCotaChamadaEncalhe(final Date dataEncalhe) {
        final StringBuilder hql = new StringBuilder();
        hql.append("   SELECT cota  ");
        hql.append("   FROM ChamadaEncalheCota  cec");
        hql.append("   JOIN cec.chamadaEncalhe as ce");
        hql.append("   JOIN cec.cota as  cota");
        hql.append("   WHERE ce.dataRecolhimento= :dataEncalhe");
        hql.append("   AND cec.fechado= false");
        hql.append("   AND cec.postergado= false");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setDate("dataEncalhe", dataEncalhe);
        
        return query.list();
    }
    
    private void getQueryAnalitico(final FiltroFechamentoEncalheDTO filtro,
            final StringBuilder hql) {
        
        hql.append("    FROM ControleConferenciaEncalheCota  controle ");
        
        hql.append("    JOIN controle.usuario u");
        
        hql.append("    JOIN controle.cota cota");
        
        hql.append("    JOIN cota.pessoa pess");
        
        hql.append("    JOIN controle.box  box ");
        
        hql.append("    LEFT OUTER JOIN controle.conferenciasEncalhe confEnc ");
        
        hql.append("    LEFT OUTER JOIN confEnc.movimentoEstoqueCota mec ");
        
        hql.append("    LEFT OUTER JOIN mec.produtoEdicao pe ");
        
        hql.append("    LEFT OUTER JOIN pe.produto pro ");
        
        hql.append("  	LEFT OUTER JOIN pe.descontoLogistica as descLogProdEdicao ");
        
        hql.append("  	LEFT OUTER JOIN pro.descontoLogistica as descLogProd ");
        
        hql.append("    LEFT OUTER JOIN controle.cobrancasControleConferenciaEncalheCota cobrancaControle ");
        
        hql.append("    LEFT OUTER JOIN cobrancaControle.cobranca cob");
        
        hql.append("    LEFT OUTER JOIN cob.divida div");
        
        if (filtro.getFornecedorId() != null) {
            hql.append("  LEFT OUTER JOIN  pro.fornecedores for ");
        }
        
        hql.append(" WHERE controle.dataOperacao = :dataEncalhe ");
        
        hql.append(" and controle.status = :statusOperacaoFinalizada  ");
        
        if (filtro.getBoxId() != null) {
            hql.append("     and  box.id = :boxId ");
        }
        
        if (filtro.getFornecedorId() != null) {
            hql.append("     and for.id =:fornecedorId ");
        }
    }
    
    @Override
    public FechamentoFisicoLogicoDTO buscarDescontosLogistica(final FechamentoFisicoLogicoDTO fechamento) {
        final StringBuilder hql = new StringBuilder();
        
        hql.append("select descontoLogistica.percentualDesconto as desconto ");
        hql.append("from DescontoLogistica as descontoLogistica ");
        hql.append("where descontoLogistica.id = :id");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliticoEncalheDTO.class));
        
        if (fechamento.getProdutoEdicaoDescontoLogisticaId() != null) {
            query.setParameter("id", fechamento.getProdutoEdicaoDescontoLogisticaId());
        } else if (fechamento.getProdutoDescontoLogisticaId() != null) {
            query.setParameter("id", fechamento.getProdutoDescontoLogisticaId());
        } else {
            query.setParameter("id", null);
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        return (FechamentoFisicoLogicoDTO) query.uniqueResult();
        
    }
    
    @Override
    public FechamentoFisicoLogicoDTO buscarDescontosProduto(
            final FechamentoFisicoLogicoDTO fechamento) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("select produto.desconto as desconto ");
        hql.append("from Produto as produto ");
        hql.append("where produto.codigo = :codigo");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        
        query.setParameter("codigo", fechamento.getCodigo());
        return (FechamentoFisicoLogicoDTO) query.uniqueResult();
    }
    
    @Override
    public FechamentoFisicoLogicoDTO buscarDescontosProdutoEdicao(
            final FechamentoFisicoLogicoDTO fechamento) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("select produtoEdicao.desconto as desconto ");
        hql.append("from ProdutoEdicao as produtoEdicao ");
        hql.append("join produtoEdicao.produtoId as produto ");
        hql.append("where produto.codigo = :codigo");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        
        query.setParameter("codigo", fechamento.getCodigo());
        
        return (FechamentoFisicoLogicoDTO) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FechamentoFisicoLogicoDTO> buscarMovimentoEstoqueCota(
            final FiltroFechamentoEncalheDTO filtro, final ArrayList<Long> listaDeIdsProdutosEdicoes) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append("select produto.codigo as codigo, ");
        hql.append("produtoEdicao.id as produtoEdicao, ");
        hql.append("conferenciaEncalhe.movimentoEstoqueCota.qtde as exemplaresDevolucao, ");
        
        hql.append("controleConferenciaEncalhe.data as dataRecolhimento, ");
        
        hql.append("(case when conferenciaEncalhe.juramentada is null then false else conferenciaEncalhe.juramentada end) as juramentada ");
        
        hql.append("from ConferenciaEncalhe as conferenciaEncalhe ");
        hql.append("join conferenciaEncalhe.produtoEdicao as produtoEdicao ");
        hql.append("join produtoEdicao.produto as produto ");
        
        hql.append("join conferenciaEncalhe.controleConferenciaEncalheCota as controleConferenciaEncalheCota ");
        hql.append("join controleConferenciaEncalheCota.controleConferenciaEncalhe as controleConferenciaEncalhe ");
        
        hql.append("where produtoEdicao.id in (:produtosEdicoesId) ");
        
        hql.append(" and controleConferenciaEncalhe.data = :dataEncalhe ");
        
        if (filtro.getBoxId() != null) {
            hql.append("  and conferenciaEncalhe.controleConferenciaEncalheCota.box.id = :boxId ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        query.setParameterList("produtosEdicoesId", listaDeIdsProdutosEdicoes);
        query.setParameter("dataEncalhe", filtro.getDataEncalhe());
        
        if (filtro.getBoxId() != null) {
            query.setLong("boxId", filtro.getBoxId());
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FechamentoFisicoLogicoDTO> buscarMovimentoEstoqueCotaVendaProduto(
            final FiltroFechamentoEncalheDTO filtro, final ArrayList<Long> listaDeIdsProdutosEdicoes) {
        
        final StringBuilder subquery = new StringBuilder();
        
        subquery.append(" select sum(vp.qntProduto) as exemplaresDevolucao, ");
        subquery.append(" produtoEdicao.id as produtoEdicao, ");
        subquery.append(" produto.codigo as codigo ");
        subquery.append(" from VendaProduto vp ");
        subquery.append(" join vp.produtoEdicao as produtoEdicao ");
        subquery.append(" join produtoEdicao.produto as produto ");
        
        subquery.append(" where ");
        subquery.append(" vp.dataOperacao = :dataEncalhe ");
        subquery.append(" and vp.tipoVenda = :tipoVenda ");
        subquery.append(" and vp.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
        subquery.append(" and produtoEdicao.id in (:produtosEdicoesId)");
        subquery.append(" group by produtoEdicao.id ");
        
        final Query query = this.getSession().createQuery(subquery.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(FechamentoFisicoLogicoDTO.class));
        
        query.setParameterList("produtosEdicoesId", listaDeIdsProdutosEdicoes);
        query.setDate("dataEncalhe", filtro.getDataEncalhe());
        query.setParameter("tipoVenda", TipoVendaEncalhe.ENCALHE);
        query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);
        
        return query.list();
        
    }
    
    @Override
    public Integer obterTotalCotasAusentesSemPostergado(final Date dataEncalhe, final DiaSemana diaRecolhimento, final boolean isSomenteCotasSemAcao,
            final String sortorder, final String sortname, final int page, final int rp, final boolean ignorarUnificacao) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(idCota) from  ");
        
        sql.append(" ( ");
        
        sql.append(getSqlCotaAusenteComChamadaEncalheSemPostergado(true, isSomenteCotasSemAcao, ignorarUnificacao).toString());
        sql.append(" union all ");
        sql.append(getSqlCotaAusenteSemChamadaEncalhe(true, ignorarUnificacao, null).toString());
        
        sql.append(" ) as ausentes	");
        
        final Query query = this.getSession().createSQLQuery(sql.toString());
        
        query.setParameter("dataEncalhe", dataEncalhe);
        
        query.setParameter("postergadoCota", false);
        
        query.setParameter("principal", true);
        
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO.name());
        
        query.setParameter("inativo", SituacaoCadastro.INATIVO.name());
        
        query.setParameter("pendente", SituacaoCadastro.PENDENTE.name());
        
        query.setParameter("diaRecolhimento", diaRecolhimento.name());
        
        query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
        
        final BigInteger qtde = (BigInteger) query.uniqueResult();
        
        return qtde != null ? qtde.intValue() : 0;
        
    }
    
    @Override
    public Boolean validarEncerramentoOperacaoEncalhe(final Date data) {
                
        String sql = "select " 
                + "(select count(*)FROM CHAMADA_ENCALHE "
                + " WHERE DATA_RECOLHIMENTO = :dataOperacao) > 0 "
                + " as chamadaEncalhe, "
                   
                + " (SELECT count(* )FROM CONTROLE_FECHAMENTO_ENCALHE "
                + " WHERE data_encalhe = :dataOperacao)  > 0 "
                + " as fechamentoEncalhe ";
        
        final SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("dataOperacao", data);
        query.addScalar("chamadaEncalhe", StandardBasicTypes.BOOLEAN);
        query.addScalar("fechamentoEncalhe", StandardBasicTypes.BOOLEAN);
        
        Object[] result = (Object[]) query.uniqueResult();
        
        Boolean chamadaEncalhe = (Boolean) result[0];
        Boolean fechamentoEncalhe = (Boolean) result[1];
        
        return !chamadaEncalhe || fechamentoEncalhe;
    }
    
    @Override
    public Integer obterDiaRecolhimento(Long produtoEdicao,Date dataRecolhimento) {
       
       // obter  chamada de encalhe deste produto/edicao, de acordo com a data de recolhimento
    	// primeira chamada igual ou menor que a data de recolhimento
    	
        String queryString = "SELECT id as chamadaEncalheId  FROM  CHAMADA_ENCALHE CHEN WHERE "+
                             " CHEN.PRODUTO_EDICAO_ID = :produtoEdicao  "+
                             "AND CHEN.DATA_RECOLHIMENTO <= :dataRecolhimento "+
                             "AND CHEN.TIPO_CHAMADA_ENCALHE = :tipoChamadaEncalheMatrizRecolhimento "+
                             
                            " ORDER BY CHEN.DATA_RECOLHIMENTO DESC LIMIT 1";
        final Query query = getSession().createSQLQuery(queryString);
        
        query.setParameter("produtoEdicao", produtoEdicao);
        
        query.setParameter("dataRecolhimento", dataRecolhimento);
        
        query.setParameter("tipoChamadaEncalheMatrizRecolhimento", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO.name());
     
            
        ((SQLQuery) query).addScalar("chamadaEncalheId", StandardBasicTypes.LONG);
        
        Long chamadaEncalheId =(Long) query.uniqueResult();
        
        final StringBuilder hql = new StringBuilder();
        
        // obter maior dia de recolhimento de encalhe desta chamada
		hql.append("select max(conf.diaRecolhimento) from ConferenciaEncalhe conf");
		hql.append(	"  where  1=1 " );
		hql.append(	"  and  conf.data = :dataRecolhimentodia" );
		hql.append(	"  and  conf.produtoEdicao = (select distinct cha.produtoEdicao from ChamadaEncalhe cha where cha.id = :chamadaEncalheId )" );
   
		
        final Query querym = getSession().createQuery(hql.toString());
        
        querym.setParameter("chamadaEncalheId", chamadaEncalheId);
        querym.setParameter("dataRecolhimentodia", dataRecolhimento);
          
        
        final Integer dia = (Integer) querym.uniqueResult();
        
        return dia != null ? dia : 1; // se nao tem devolucao ainda, inicar como primeiro dia
        
    }
    
    
    
    private StringBuilder getSqlCotaAusenteComChamadaEncalheFechado(final boolean indCount, final boolean isSomenteCotasSemAcao,
            Integer numeroCota) {
        
        final StringBuilder sql = new StringBuilder();
        
        if(indCount) {
            
            sql.append("	 select  ");
            sql.append("        cota.ID as idCota, ");
            sql.append("        false as indMFCNaoConsolidado ");
            
        } else {
            
            sql.append("	 select                                                             ");
            sql.append("        cota.ID as idCota,                                              ");
            sql.append("        cota.NUMERO_COTA as numeroCota,                                 ");
            sql.append("        coalesce(if(pessoa.tipo= 'F',pessoa.NOME,                                           ");
            sql.append("        pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia) as colaboradorName,                        ");
            sql.append("        box.NOME as boxName,                                            ");
            sql.append("        roteiro.DESCRICAO_ROTEIRO as roteiroName,                       ");
            sql.append("        rota.DESCRICAO_ROTA as rotaName,                                ");
            sql.append(" 		true as indPossuiChamadaEncalheCota, 							");
            sql.append(" 		false as indMFCNaoConsolidado, 									");
            
            sql.append(" 		case when (grupoCota.id is null) then false			");
            sql.append(" 		else true end as operacaoDiferenciada,				");
            
            sql.append("        coalesce(chamadaEncalheCota.FECHADO, 0)  as fechado,            ");
            sql.append("        coalesce(chamadaEncalheCota.POSTERGADO, 0) as postergado,       		");
            sql.append("        coalesce(chamadaEncalhe.DATA_RECOLHIMENTO, :dataEncalhe) as dataEncalhe, ");
            sql.append("		(select count(cu.id) from COTA_UNIFICACAO cu ");
            sql.append("		join COTAUNIFICACAO_COTAUNIFICADA co_un on (cu.ID = co_un.COTA_UNIFICACAO_ID) ");
            sql.append("		where cu.COTA_ID = cota.ID or ");
            sql.append("		co_un.COTA_UNIFICADA_ID = cota.ID > 0) as unificacao ");
        }
        
        sql.append("	from ");
        sql.append("        Cota cota ");
        sql.append("	inner join  ");
        sql.append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota ");
        sql.append("            on chamadaEncalheCota.COTA_ID=cota.ID ");
        sql.append("            and ( cota.TIPO_COTA <> :tipoCotaAVista OR cota.DEVOLVE_ENCALHE = true ) ");
        sql.append("	inner join ");
        sql.append("        CHAMADA_ENCALHE chamadaEncalhe ");
        sql.append("            on chamadaEncalheCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID  ");
        sql.append("	inner join ");
        sql.append("        PESSOA pessoa ");
        sql.append("            on cota.PESSOA_ID=pessoa.ID ");
        sql.append("	inner join ");
        sql.append("        BOX box ");
        sql.append("            on cota.BOX_ID=box.ID ");
        sql.append("	inner join ");
        sql.append("        PDV pdv ");
        sql.append("            on cota.ID=pdv.COTA_ID ");
        sql.append("    left outer join ");
        sql.append("        ROTA_PDV rotaPdv ");
        sql.append("            on pdv.ID=rotaPdv.PDV_ID ");
        sql.append("	left outer join ");
        sql.append("    	ROTA rota ");
        sql.append("    		on rotaPdv.ROTA_ID=rota.ID ");
        sql.append("    left outer join ");
        sql.append("        ROTEIRO roteiro ");
        sql.append("        	on rota.ROTEIRO_ID=roteiro.ID ");
        
        sql.append("	left outer join COTA_GRUPO cotaGrupo ");
        sql.append("	on cotaGrupo.cota_id = cota.id	");
        
        sql.append("	left outer join GRUPO_COTA grupoCota ");
        sql.append("	on grupoCota.id = cotaGrupo.grupo_cota_id ");
        
        sql.append("	left outer join DIA_RECOLHIMENTO_GRUPO_COTA diaRecolhimentoGrupoCota	");
        sql.append("	on grupoCota.id = diaRecolhimentoGrupoCota.grupo_id						");
        
        sql.append("    where (grupoCota.DATA_VIGENCIA_INICIO is null or grupoCota.DATA_VIGENCIA_INICIO <= :dataEncalhe) ");
        sql.append("           and (grupoCota.DATA_VIGENCIA_FIM is null or grupoCota.DATA_VIGENCIA_FIM >= :dataEncalhe) and ");
        
        sql.append("    (	grupoCota.id is null		");
        sql.append("    or diaRecolhimentoGrupoCota.dia_id = :diaRecolhimento ) and	");
        
        sql.append("        chamadaEncalhe.DATA_RECOLHIMENTO = 	:dataEncalhe                ");
                                                              
        sql.append("		and pdv.PONTO_PRINCIPAL = :principal                            ");
        
        
        if (isSomenteCotasSemAcao) {
            
            sql.append(" and ( chamadaEncalheCota.FECHADO = false or chamadaEncalheCota.FECHADO is null ) 		");
            
            sql.append(" and ( chamadaEncalheCota.POSTERGADO = false or chamadaEncalheCota.POSTERGADO is null ) ");
        }
        
        if (numeroCota != null){
            
            sql.append(" and cota.NUMERO_COTA = :numeroCota ");
        }
        
        sql.append("	group by                                      ");
        sql.append("        cota.ID                                   ");
        sql.append(" having ");
        sql.append("		 cota.ID  in (                                ");
        sql.append("			select                                          ");
        sql.append("				distinct(cota.ID )                                   ");
        sql.append("			from                                            ");
        sql.append("		        Cota cota                                   ");
        sql.append("			inner join                                      ");
        sql.append("		        CHAMADA_ENCALHE_COTA chamadaEncalheCota     ");
        sql.append("		            on chamadaEncalheCota.COTA_ID=cota.ID  and  chamadaEncalheCota.fechado = true  ");
        sql.append("			inner join                                                                     ");
        sql.append("			CHAMADA_ENCALHE chamadaEncalhe                                                 ");
        sql.append("		            on ( chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID and     ");
        sql.append("						 chamadaEncalhe.DATA_RECOLHIMENTO = :dataEncalhe )                 ");
        sql.append("		)     ");
   
        
        
        return sql;
    }
    
    
    private StringBuilder getSqlCotaAusenteSemChamadaEncalheFechado(final boolean indCount,
            final boolean ignorarUnificacao, Integer numeroCota) {
        
        final StringBuilder sqlMovimentoFinaceiroCotaNaoConsolidado = new StringBuilder();
        
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("( select count(mfc.id)>0  ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" from ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" movimento_financeiro_cota mfc ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" where ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	mfc.cota_id = cota.id ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.data <= :dataEncalhe ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.STATUS = :statusAprovacao ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("	and mfc.id not in ( ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("                       select cmfc.MVTO_FINANCEIRO_COTA_ID from ");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append("                       CONSOLIDADO_MVTO_FINANCEIRO_COTA cmfc where cmfc.MVTO_FINANCEIRO_COTA_ID = mfc.id  )");
        sqlMovimentoFinaceiroCotaNaoConsolidado.append(" ) ");
        
        final StringBuilder sql = new StringBuilder();
        
        if(indCount) {
            
            sql.append(" 	select                                                  ");
            sql.append("    cota.ID as idCota,                                      ");
            sql.append(sqlMovimentoFinaceiroCotaNaoConsolidado.toString()).append(" as indMFCNaoConsolidado ");
            
        } else {
            
            sql.append(" 	select                                                  ");
            sql.append("    cota.ID as idCota,                                      ");
            sql.append("    cota.NUMERO_COTA as numeroCota,                         ");
            sql.append("    coalesce(if(pessoa.tipo = 'F',pessoa.NOME,                                   ");
            sql.append("    pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia) as colaboradorName,                ");
            sql.append("    box.NOME as boxName,                                    ");
            sql.append("    roteiro.DESCRICAO_ROTEIRO as roteiroName,               ");
            sql.append("    rota.DESCRICAO_ROTA as rotaName,                        ");
            sql.append(" 	false as indPossuiChamadaEncalheCota, 					");
            
            sql.append(sqlMovimentoFinaceiroCotaNaoConsolidado.toString()).append(" as indMFCNaoConsolidado, ");
            
            sql.append(" 		case when (grupoCota.id is null) then false			");
            sql.append(" 		else true end as operacaoDiferenciada,				");
            
            
            sql.append("   false as fechado, ");
            sql.append("   false as postergado, ");
            sql.append("   coalesce(:dataEncalhe) as dataEncalhe, ");
            sql.append("		(select count(cu.ID) from COTA_UNIFICACAO cu ");
            sql.append("		join COTAUNIFICACAO_COTAUNIFICADA co_un on (cu.ID = co_un.COTA_UNIFICACAO_ID) ");
            sql.append("		where cu.COTA_ID = cota.ID or ");
            sql.append("		co_un.COTA_UNIFICADA_ID = cota.ID > 0) as unificacao ");
            
        }
        
        sql.append("	from                                                    ");
        sql.append("        Cota cota                                           ");
        sql.append("	inner join                                              ");
        sql.append("        PESSOA pessoa                                       ");
        sql.append("            on cota.PESSOA_ID=pessoa.ID                     ");
        
        sql.append("            and cota.TIPO_COTA <> :tipoCotaAVista ");
        
        sql.append("	inner join                                              ");
        sql.append("        BOX box                                             ");
        sql.append("            on cota.BOX_ID=box.ID                           ");
        sql.append("	inner join                                              ");
        sql.append("        PDV pdv                                             ");
        sql.append("            on cota.ID=pdv.COTA_ID                          ");
        sql.append("    left outer join                                         ");
        sql.append("        ROTA_PDV rotaPdv                                    ");
        sql.append("            on pdv.ID=rotaPdv.PDV_ID                        ");
        sql.append("        and rotaPdv.ROTA_ID not in ( select ro.id from Rota ro inner join Roteiro  rt on ro.roteiro_id  = rt.id and rt.tipo_roteiro = 'ESPECIAL')");
        sql.append("	left outer join                                         ");
        sql.append("    	ROTA rota                                           ");
        sql.append("    		on rotaPdv.ROTA_ID=rota.ID                      ");
        sql.append("    left outer join                                         ");
        sql.append("        ROTEIRO roteiro                                     ");
        sql.append("        	on rota.ROTEIRO_ID=roteiro.ID                   ");
        
        sql.append("	left outer join COTA_GRUPO cotaGrupo	");
        sql.append("	on cotaGrupo.cota_id = cota.id			");
        
        sql.append("	left outer join GRUPO_COTA grupoCota 		");
        sql.append("	on grupoCota.id = cotaGrupo.grupo_cota_id	");
        
        sql.append("	left outer join DIA_RECOLHIMENTO_GRUPO_COTA diaRecolhimentoGrupoCota	");
        sql.append("	on grupoCota.id = diaRecolhimentoGrupoCota.grupo_id						");
        
        sql.append("    where  ");
        
        sql.append("	cota.SITUACAO_CADASTRO <> :inativo and cota.SITUACAO_CADASTRO <> :pendente ");
        
        
        sql.append("    and (	grupoCota.id is null		");
        sql.append("    or diaRecolhimentoGrupoCota.dia_id = :diaRecolhimento )	");
        
        
        if (ignorarUnificacao){
            
            sql.append("	and cota.ID not in ( 								");
            sql.append("		select aoada.COTA_UNIFICADA_ID					");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada)	");
            
            sql.append("	and cota.ID not in ( 								");
            sql.append("		select uni.cota_id 								");
            sql.append("			from COTAUNIFICACAO_COTAUNIFICADA aoada join COTA_UNIFICACAO uni ON (aoada.COTA_UNIFICACAO_ID = uni.ID	)) ");
            
        }
        
              
        sql.append("		and pdv.PONTO_PRINCIPAL = :principal                ");
                                                                                   
        
        if (numeroCota != null){
            
            sql.append("and cota.NUMERO_COTA = :numeroCota ");
        }
        
        sql.append("	group by    ");
        
        sql.append("    cota.ID, indMFCNaoConsolidado ");
        
        sql.append(" having indMFCNaoConsolidado = true and ");
        sql.append("		 cota.ID not in (                                ");
        sql.append("			select                                          ");
        sql.append("				distinct(cota.ID )                                   ");
        sql.append("			from                                            ");
        sql.append("		        Cota cota                                   ");
        sql.append("			inner join                                      ");
        sql.append("		        CHAMADA_ENCALHE_COTA chamadaEncalheCota     ");
        sql.append("		            on chamadaEncalheCota.COTA_ID=cota.ID  and  chamadaEncalheCota.fechado = true  ");
        sql.append("			inner join                                                                     ");
        sql.append("			CHAMADA_ENCALHE chamadaEncalhe                                                 ");
        sql.append("		            on ( chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID and     ");
        sql.append("						 chamadaEncalhe.DATA_RECOLHIMENTO = :dataEncalhe )                 ");
        sql.append("		)     ");
        
        return sql;
    }
    
    private boolean isChamadaEncalheFechada(Date dataEncalhe ) {
    
    	 final StringBuilder sql = new StringBuilder("select count(*) ");
         sql.append(" from controle_fechamento_encalhe fe ")
         .append(" where data_encalhe = :dataEncalhe ");
         
         final Query query = this.getSession().createSQLQuery(sql.toString());
         query.setParameter("dataEncalhe", dataEncalhe);
         
         return (BigInteger)query.uniqueResult() != BigInteger.ZERO;
      
    }
    
    public boolean existeFechamentoEncalhePorData(final Date dataEncalhe) {
        
        final StringBuilder hql = new StringBuilder("select count(cfe.id) ");
        hql.append(" from ControleFechamentoEncalhe cfe ")
        .append(" where cfe.dataEncalhe = :dataEncalhe ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("dataEncalhe", dataEncalhe);
        
        return (Long)query.uniqueResult() != 0;
    }
 
    public boolean existeFechamentoEncalhePorCota(Date dataOperacao, Integer numeroCota) {
        
        final StringBuilder hql = new StringBuilder("select count(ccec.id) ");
//        hql.append(" from ConsolidadoFinanceiroCota consolidado ")
//        .append(" join consolidado.cota cota ")
//        .append(" where 1=1 ")
//        .append(" and consolidado.dataConsolidado = :dataOperacao ")
//        .append(" and consolidado.id not in (")
//        .append(" select c.id from Divida d join d.consolidados c ")
//        .append(" join d.cobranca cob ")
//        .append(" where c.id = consolidado.id ")
//        .append(" and cob.tipoCobranca <> :tipoCobranca ")
//        .append(" and d.origemNegociacao = true ")
//        .append(")");
        
        hql.append(" from ControleConferenciaEncalheCota ccec ");
        hql.append(" join ccec.cota cota ");
        hql.append(" where 1=1 ");
        hql.append(" and cota.numeroCota = :numeroCota ");
        hql.append(" and ccec.dataOperacao = :dataOperacao ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("numeroCota", numeroCota);        
        
        query.setParameter("dataOperacao", dataOperacao);
        
        return (Long) query.uniqueResult() != 0;
    }
    
    @Override
	@SuppressWarnings("unchecked")
	public List<ExtracaoContaCorrenteDTO> extracaoContaCorrente(FiltroExtracaoContaCorrenteDTO filtro){

    	StringBuilder sql = new StringBuilder();

    	sql.append(" select  ");
    	sql.append(" 	T.sequenciaMatriz, ");
    	sql.append(" 	T.codigoProduto, ");
    	sql.append(" 	T.nomeProduto, ");
    	sql.append(" 	T.numeroEdicao, ");
    	sql.append(" 	T.precoVendaCapa as precoCapa, ");
    	sql.append(" 	T.pacotePadrao, ");
    	sql.append(" 	T.desconto, ");
    	sql.append(" 	(T.precoVendaCapa -((T.precoVendaCapa * T.desconto) / 100)) as descLogistica, ");
    	sql.append(" 	(T.precoVendaCapa -((T.precoVendaCapa * T.valorDesconto) / 100)) as descCota, ");
    	sql.append(" 	coalesce((select ");
    	sql.append(" 					sum( mecReparte.QTDE ) vdencalhe ");
    	sql.append(" 				from ");
    	sql.append(" 					lancamento lancamento join produto_edicao pe2 on ");
    	sql.append(" 					pe2.id = lancamento.produto_edicao_id left join periodo_lancamento_parcial plp on ");
    	sql.append(" 					plp.id = lancamento.periodo_lancamento_parcial_id join produto produto on ");
    	sql.append(" 					produto.id = pe2.produto_id left join tipo_classificacao_produto tipoClassificacaoProduto on ");
    	sql.append(" 					tipoClassificacaoProduto.id = pe2.tipo_classificacao_produto_id join movimento_estoque_cota mecReparte on ");
    	sql.append(" 					mecReparte.LANCAMENTO_ID = lancamento.id left join tipo_movimento tipo on ");
    	sql.append(" 					tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
    	sql.append(" 				where ");
    	sql.append(" 					pe2.id = T.prodEdicaoID ");
    	sql.append(" 					and tipo.GRUPO_MOVIMENTO_ESTOQUE in('COMPRA_ENCALHE') ");
    	sql.append(" 					and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" 					and lancamento.status in('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO', ");
    	sql.append(" 											 'EM_RECOLHIMENTO','RECOLHIDO','FECHADO') ");
    	sql.append(" 				group by ");
    	sql.append(" 					pe2.numero_Edicao, ");
    	sql.append(" 					produto.codigo ");
    	sql.append(" 			),0) as vendaEncalhe, ");
    	sql.append(" 			 ");
    	sql.append(" 	coalesce((select ");
    	sql.append(" 				round( sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else - mecReparte.QTDE end ), 0 ) as repartePrevisto ");
    	sql.append(" 			from ");
    	sql.append(" 				lancamento lancamento join produto_edicao pe2 on ");
    	sql.append(" 				pe2.id = lancamento.produto_edicao_id left join periodo_lancamento_parcial plp on ");
    	sql.append(" 				plp.id = lancamento.periodo_lancamento_parcial_id join produto produto on ");
    	sql.append(" 				produto.id = pe2.produto_id left join tipo_classificacao_produto tipoClassificacaoProduto on ");
    	sql.append(" 				tipoClassificacaoProduto.id = pe2.tipo_classificacao_produto_id join movimento_estoque_cota mecReparte on ");
    	sql.append(" 				mecReparte.LANCAMENTO_ID = lancamento.id left join tipo_movimento tipo on ");
    	sql.append(" 				tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
    	sql.append(" 			where ");
    	sql.append(" 				pe2.id = T.prodEdicaoID ");
    	sql.append(" 				and tipo.GRUPO_MOVIMENTO_ESTOQUE not in('ENVIO_ENCALHE') ");
    	sql.append(" 				and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" 				and lancamento.status in('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO', ");
    	sql.append(" 										 'EM_RECOLHIMENTO','RECOLHIDO','FECHADO') ");
    	sql.append(" 			group by ");
    	sql.append(" 				pe2.numero_Edicao, ");
    	sql.append(" 				produto.codigo ");
    	sql.append(" 		),0) as reparte, ");
    	sql.append(" 		 ");
    	sql.append(" 	coalesce((select coalesce(round( case when lancamento.STATUS in( 'FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO' ) then  ");
    	sql.append(" 						 sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else - mecReparte.QTDE end )  ");
    	sql.append(" 					 		-( select sum( mecEncalhe.qtde ) from lancamento lanc left join chamada_encalhe_lancamento cel  ");
    	sql.append(" 				 				on cel.LANCAMENTO_ID = lanc.ID left join chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID  ");
    	sql.append(" 				 				left join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID  ");
    	sql.append(" 				 				left join conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID  ");
    	sql.append(" 				 				left join movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
    	sql.append(" 				 				where lanc.id = lancamento.id and ce.DATA_RECOLHIMENTO = lanc.DATA_REC_DISTRIB ) else null end, 0 ), ");
    	sql.append(" 						0) as qtdeVendas ");
    	sql.append(" 			from ");
    	sql.append(" 				lancamento lancamento join produto_edicao pe2 on ");
    	sql.append(" 				pe2.id = lancamento.produto_edicao_id left join periodo_lancamento_parcial plp on ");
    	sql.append(" 				plp.id = lancamento.periodo_lancamento_parcial_id join produto produto on ");
    	sql.append(" 				produto.id = pe2.produto_id left join tipo_classificacao_produto tipoClassificacaoProduto on ");
    	sql.append(" 				tipoClassificacaoProduto.id = pe2.tipo_classificacao_produto_id join movimento_estoque_cota mecReparte on ");
    	sql.append(" 				mecReparte.LANCAMENTO_ID = lancamento.id left join tipo_movimento tipo on ");
    	sql.append(" 				tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
    	sql.append(" 			where ");
    	sql.append(" 				pe2.id = T.prodEdicaoID ");
    	sql.append(" 				and tipo.GRUPO_MOVIMENTO_ESTOQUE not in('ENVIO_ENCALHE') ");
    	sql.append(" 				and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" 				and lancamento.status in('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO', ");
    	sql.append(" 										 'EM_RECOLHIMENTO','RECOLHIDO','FECHADO') ");
    	sql.append(" 			group by ");
    	sql.append(" 				pe2.numero_Edicao, ");
    	sql.append(" 				produto.codigo ");
    	sql.append(" 		),0) as venda, ");
    	sql.append(" 	 ");
    	sql.append(" 	coalesce((select coalesce(round( case when lancamento.STATUS in( 'FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO' )  ");
    	sql.append(" 					then( select sum( mecEncalhe.qtde ) from lancamento lanc left join chamada_encalhe_lancamento cel on  ");
    	sql.append(" 							cel.LANCAMENTO_ID = lanc.ID left join chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID  ");
    	sql.append(" 							left join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID left join conferencia_encalhe confEnc on  ");
    	sql.append(" 							confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID left join movimento_estoque_cota mecEncalhe on  ");
    	sql.append(" 							mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
    	sql.append(" 						  where lanc.id = lancamento.id and ce.DATA_RECOLHIMENTO = lanc.DATA_REC_DISTRIB )  ");
    	sql.append(" 						  else null end, 0 ),0) as enc ");
    	sql.append(" 			from ");
    	sql.append(" 				lancamento lancamento join produto_edicao pe2 on ");
    	sql.append(" 				pe2.id = lancamento.produto_edicao_id left join periodo_lancamento_parcial plp on ");
    	sql.append(" 				plp.id = lancamento.periodo_lancamento_parcial_id join produto produto on ");
    	sql.append(" 				produto.id = pe2.produto_id left join tipo_classificacao_produto tipoClassificacaoProduto on ");
    	sql.append(" 				tipoClassificacaoProduto.id = pe2.tipo_classificacao_produto_id join movimento_estoque_cota mecReparte on ");
    	sql.append(" 				mecReparte.LANCAMENTO_ID = lancamento.id left join tipo_movimento tipo on ");
    	sql.append(" 				tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
    	sql.append(" 			where ");
    	sql.append(" 				pe2.id = T.prodEdicaoID ");
    	sql.append(" 				and tipo.GRUPO_MOVIMENTO_ESTOQUE not in('ENVIO_ENCALHE') ");
    	sql.append(" 				and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" 				and lancamento.status in('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO', ");
    	sql.append(" 										 'EM_RECOLHIMENTO','RECOLHIDO','FECHADO') ");
    	sql.append(" 			group by ");
    	sql.append(" 				pe2.numero_Edicao, ");
    	sql.append(" 				produto.codigo ");
    	sql.append(" 		),0) as encalhe, ");
    	sql.append(" 	 ");
    	sql.append(" 	coalesce((select coalesce(round( case when lancamento.STATUS in( 'FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO' )  ");
    	sql.append(" 							then sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else - mecReparte.QTDE end )  ");
    	sql.append(" 							-( select sum( mecEncalhe.qtde ) from lancamento lanc left join chamada_encalhe_lancamento cel  ");
    	sql.append(" 								on cel.LANCAMENTO_ID = lanc.ID left join chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID  ");
    	sql.append(" 								left join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID  ");
    	sql.append(" 								left join conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID  ");
    	sql.append(" 								left join movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
    	sql.append(" 								where lanc.id = lancamento.id and ce.DATA_RECOLHIMENTO = lanc.DATA_REC_DISTRIB ) else null end, 0  ");
    	sql.append(" 						),0) as qtdeVendas ");
    	sql.append(" 			from ");
    	sql.append(" 				lancamento lancamento join produto_edicao pe2 on ");
    	sql.append(" 				pe2.id = lancamento.produto_edicao_id left join periodo_lancamento_parcial plp on ");
    	sql.append(" 				plp.id = lancamento.periodo_lancamento_parcial_id join produto produto on ");
    	sql.append(" 				produto.id = pe2.produto_id left join tipo_classificacao_produto tipoClassificacaoProduto on ");
    	sql.append(" 				tipoClassificacaoProduto.id = pe2.tipo_classificacao_produto_id join movimento_estoque_cota mecReparte on ");
    	sql.append(" 				mecReparte.LANCAMENTO_ID = lancamento.id left join tipo_movimento tipo on ");
    	sql.append(" 				tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
    	sql.append(" 			where ");
    	sql.append(" 				pe2.id = T.prodEdicaoID ");
    	sql.append(" 				and tipo.GRUPO_MOVIMENTO_ESTOQUE not in('ENVIO_ENCALHE') ");
    	sql.append(" 				and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" 				and lancamento.status in('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO', ");
    	sql.append(" 										 'EM_RECOLHIMENTO','RECOLHIDO','FECHADO') ");
    	sql.append(" 			group by ");
    	sql.append(" 				pe2.numero_Edicao, ");
    	sql.append(" 				produto.codigo) * T.precoVendaCapa, ");
    	sql.append(" 		0) as vendaTotal ");
    	sql.append("  ");
    	sql.append(" from (select ");
    	sql.append(" 			ITEM_CH_ENC_FORNECEDOR.NUEMRO_ITEM as sequenciaMatriz, ");
    	sql.append(" 			pd.codigo as codigoProduto, ");
    	sql.append(" 			pe.NOME_COMERCIAL as nomeProduto, ");
    	sql.append(" 			pe.numero_edicao as numeroEdicao, ");
    	sql.append(" 			(select preco_venda from movimento_estoque_cota where id = ce.MOVIMENTO_ESTOQUE_COTA_ID) as precoVendaCapa, ");
    	sql.append(" 			pe.pacote_padrao as pacotePadrao, ");
    	sql.append(" 			pe.id as prodEdicaoID, ");
    	sql.append(" 			descLog.percentual_desconto as desconto, ");
    	sql.append(" 			(select valor_desconto from movimento_estoque_cota where id = ce.MOVIMENTO_ESTOQUE_COTA_ID) as valorDesconto ");
    	sql.append(" 			 ");
    	sql.append(" 		FROM ");
    	sql.append(" 			ITEM_CHAMADA_ENCALHE_FORNECEDOR ITEM_CH_ENC_FORNECEDOR  ");
    	sql.append(" 			 ");
    	sql.append(" 			inner join chamada_encalhe_fornecedor chmFornecedor  ");
    	sql.append(" 				on chmFornecedor.ID = ITEM_CH_ENC_FORNECEDOR.CHAMADA_ENCALHE_FORNECEDOR_ID ");
    	sql.append(" 			inner join PRODUTO_EDICAO pe  ");
    	sql.append(" 				on ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID = pe.ID ");
    	sql.append(" 			inner join PRODUTO pd  ");
    	sql.append(" 				on pe.PRODUTO_ID = pd.ID ");
    	sql.append(" 			left join conferencia_encalhe ce  ");
    	sql.append(" 				on ce.produto_edicao_id = pe.id  ");
    	sql.append(" 			left join controle_conferencia_encalhe_cota ccec  ");
    	sql.append(" 				on ce.controle_conferencia_encalhe_cota_id = ccec.id  ");
    	sql.append(" 			join cota ct  ");
    	sql.append(" 				on ct.id = ccec.cota_id  ");
    	sql.append(" 			left join chamada_encalhe che ");
    	sql.append(" 				on che.produto_edicao_id = pe.id ");
    	sql.append(" 			left join chamada_encalhe_cota cec ");
    	sql.append(" 				on cec.id = ce.CHAMADA_ENCALHE_COTA_ID ");
    	sql.append(" 			left join venda_produto vp  ");
    	sql.append(" 				on (vp.data_venda >= :dataDe and vp.data_venda <= :dataAte) ");
    	sql.append(" 				and vp.id_produto_edicao = pe.produto_id ");
    	sql.append(" 				and vp.id_cota = ct.id ");
    	sql.append(" 				and vp.tipo_venda_encalhe = 'ENCALHE'  ");
    	sql.append(" 			join desconto_logistica descLog  ");
    	sql.append(" 				on pe.desconto_logistica_id = descLog.id ");
    	sql.append(" 				 ");
    	sql.append(" 			where ");
    	sql.append(" 			 ");
    	sql.append(" 			chmFornecedor.ANO_REFERENCIA = :ano  ");
    	sql.append(" 			and chmFornecedor.NUMERO_SEMANA = :semana ");
    	sql.append(" 		group by ");
    	sql.append(" 			pe.id ");
    	sql.append(" 		order by ");
    	sql.append(" 			ISNULL(sequenciaMatriz), sequenciaMatriz asc) T ");
    	
    	
    	SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        
    	query.setParameter("dataDe", filtro.getPeriodoRecolhimento().getDe());
        query.setParameter("dataAte", filtro.getPeriodoRecolhimento().getAte());
        query.setParameter("semana", filtro.getSemana().substring(4, 6));
        query.setParameter("ano", filtro.getSemana().substring(0, 4));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ExtracaoContaCorrenteDTO.class));
        
        ((SQLQuery) query).addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("codigoProduto", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("nomeProduto", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("pacotePadrao", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("descLogistica", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("descCota", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("vendaEncalhe", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("venda", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("vendaTotal", StandardBasicTypes.BIG_DECIMAL);
        
        return query.list();
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> extracaoContaCorrente_BuscarCotasObservacoes(FiltroExtracaoContaCorrenteDTO filtro){

    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select c.numero_cota ");
    	sql.append(" 	from chamada_encalhe ce ");
    	sql.append(" 		join chamada_encalhe_cota cec ");
    	sql.append(" 			on cec.chamada_encalhe_id = ce.id ");
    	sql.append(" 		join cota c ");
    	sql.append(" 			on cec.cota_id = c.id ");
    	sql.append(" 	where ce.data_recolhimento >= :dataDe and ce.data_recolhimento <= :dataAte ");
    	
    	if(filtro.isBuscarCotasPostergadas()){
    		sql.append(" 		and cec.postergado is true ");
    	}else{
    		sql.append(" and cec.cota_id not in (select ccec.cota_id from conferencia_encalhe cfe ");
    		sql.append(" 				join controle_conferencia_encalhe_cota ccec ");
    		sql.append(" 					on cfe.controle_conferencia_encalhe_cota_id = ccec.id ");
    		sql.append(" 				where cfe.`DATA` = ce.data_recolhimento ");
    		sql.append(" 					and cfe.chamada_encalhe_cota_id = cec.id) ");
    	}
    	
    	sql.append(" 	group by c.numero_cota; ");
    	
    	
    	SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        
    	query.setParameter("dataDe", filtro.getDataDe());
        query.setParameter("dataAte", filtro.getDataAte());
        
        return query.list();
    }
    
}