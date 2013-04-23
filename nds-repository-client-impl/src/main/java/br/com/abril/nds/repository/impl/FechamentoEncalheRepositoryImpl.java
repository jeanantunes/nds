package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

@Repository
public class FechamentoEncalheRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements FechamentoEncalheRepository {

	public FechamentoEncalheRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}
	
    private StringBuilder getFromConferenciaEncalhe(){
		
		StringBuilder hql = new StringBuilder();
		
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
		
        StringBuilder subquery = new StringBuilder();
		
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
	
	private StringBuilder getWhereFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
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
	public List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp) {
		
		StringBuilder hql = new StringBuilder();
	
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
			if (("asc").equalsIgnoreCase(sortorder)) {
				hql.append(sortname+" asc ");	
			} else {
				hql.append(sortname+" desc ");
			}
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
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
	public int buscarQuantidadeConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT distinct  p.codigo as  codigo "); 
		hql.append(" , p.nome as produto ");
		hql.append(" , pe.numeroEdicao as edicao");
		
		hql.append(" , coalesce(pe.precoVenda, 0)  -  ( coalesce(pe.precoVenda, 0)  * ( ");
		hql.append("   CASE WHEN pe.origem = :origemInterface ");
		hql.append("   THEN ( coalesce(descLogProdEdicao.percentualDesconto, descLogProd.percentualDesconto, 0 ) ) ");
		hql.append("   ELSE ( coalesce(pe.desconto, p.desconto, 0) / 100) END ");
		hql.append(" ) ) as precoCapaDesconto ");
		
		hql.append(" , coalesce(pe.precoVenda, 0) as precoCapa ");
		
		hql.append(" , pe.id as produtoEdicao ");
		hql.append(" , case when  pe.parcial  = true  then 'P' else 'N' end  as tipo ");
		hql.append(" , che.dataRecolhimento as dataRecolhimento ");
		hql.append(" , sum (mec.qtde) - ( "+ this.getQueryVendaProduto() +" )    as exemplaresDevolucao ");		

		hql.append(this.getFromConferenciaEncalhe());
	
		hql.append(this.getWhereFechamentoEncalhe(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
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
			
		return query.list().size();
	}
	
	@Override
	public Boolean buscaControleFechamentoEncalhe(Date dataEncalhe) {
		
		Criteria criteria = this.getSession().createCriteria(ControleFechamentoEncalhe.class, "cfe");
		criteria.add(Restrictions.eq("cfe.dataEncalhe", dataEncalhe));
		
		return !criteria.list().isEmpty();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FechamentoEncalhe> buscarFechamentoEncalhe(Date dataEncalhe) {
		
		Criteria criteria = this.getSession().createCriteria(FechamentoEncalhe.class);
		criteria.add(Restrictions.eq("fechamentoEncalhePK.dataEncalhe", dataEncalhe));
		criteria.setFetchMode("listFechamentoEncalheBox", FetchMode.JOIN);
		
		return criteria.list();
	}
	
	
	public Integer obterTotalCotasAusentes(Date dataEncalhe, 
			boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(*) from  ");
		
		
		sql.append("	( select cota.ID as idCota ")
		
		.append( getClausulaFromWhereQueryCotaAusentes(isSomenteCotasSemAcao) )
		 
		.append("	) as ausentes	");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataEncalhe", dataEncalhe);
		
		query.setParameter("principal", true);

		query.setParameter("statusConcluido", StatusOperacao.CONCLUIDO.name());
		
		BigInteger qtde = (BigInteger) query.uniqueResult();
		
		return ( qtde != null ) ? qtde.intValue() : 0;
		
	}
	
	public List<CotaAusenteEncalheDTO> obterCotasAusentes(Date dataEncalhe, 
			boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp) {
	
		StringBuilder sql = new StringBuilder();
		
		sql.append("	 select  ")
		.append("	        cota.ID as idCota,                                          ")
		.append("	        cota.NUMERO_COTA as numeroCota,                             ")
		.append("	        coalesce(pessoa.NOME,                                       ")
		.append("	        pessoa.RAZAO_SOCIAL) as colaboradorName,                    ")
		.append("	        box.NOME as boxName,                                        ")
		.append("	        roteiro.DESCRICAO_ROTEIRO as roteiroName,                   ")
		.append("	        rota.DESCRICAO_ROTA as rotaName,                            ")
		.append("	        coalesce(chamadaEncalheCota.FECHADO, 0)  as fechado,		")
		.append("	        coalesce(chamadaEncalheCota.POSTERGADO, 0) as postergado, 	")
		.append("	        coalesce(chamadaEncalhe.DATA_RECOLHIMENTO, :dataEncalhe) as dataEncalhe ")
		
		.append( getClausulaFromWhereQueryCotaAusentes(isSomenteCotasSemAcao) );
		 
		
		if("acao".equals(sortname)) {
			sortname = "fechado";
		}
		 
		if (sortname != null && sortorder != null) {
			sql.append("  ORDER BY " + sortname + " " + sortorder);
		}
		
		Query query = this.getSession().createSQLQuery(sql.toString());

		((SQLQuery) query).addScalar("idCota", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
		((SQLQuery) query).addScalar("colaboradorName", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("boxName", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("roteiroName", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("rotaName", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("fechado", StandardBasicTypes.BOOLEAN);
		((SQLQuery) query).addScalar("postergado", StandardBasicTypes.BOOLEAN);
		((SQLQuery) query).addScalar("dataEncalhe", StandardBasicTypes.DATE);

		query.setParameter("dataEncalhe", dataEncalhe);
		query.setParameter("principal", true);
		query.setParameter("statusConcluido", StatusOperacao.CONCLUIDO.name());

		query.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));

		query.setFirstResult(page);

		if (rp >= 0) {
			query.setMaxResults(rp);
		}
		
		return query.list();
		
	}
	
	
	
	private String getClausulaFromWhereQueryCotaAusentes(boolean isSomenteCotasSemAcao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("	from Cota cota	")
		
		.append("	    join                                                               ")
		.append("	        CHAMADA_ENCALHE_COTA chamadaEncalheCota								")
		.append("	            on chamadaEncalheCota.COTA_ID=cota.ID                           ")

		.append("	    join                                                               ")
		.append("	        CHAMADA_ENCALHE chamadaEncalhe 										")
		.append("	            on chamadaEncalheCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID      ")
		
		.append("	    inner join                                                              ")
		.append("	        PESSOA pessoa                                                       ")
		.append("	            on cota.PESSOA_ID=pessoa.ID                                     ")

		.append("	    inner join                                                              ")
		.append("	        BOX box                                                             ")
		.append("	            on cota.BOX_ID=box.ID                                           ")
		
		.append("	    inner join                                                              ")
		.append("	        PDV pdv                                                             ")
		.append("	            on cota.ID=pdv.COTA_ID                                          ")
		
		.append("	    inner join                                                              ")
		.append("	        ROTA_PDV rotaPdv                                                    ")
		.append("	            on pdv.ID=rotaPdv.PDV_ID,                                       ")
		
		.append("	        ROTA rota,                                                          ")
		
		.append("	        ROTEIRO roteiro                                                     ")
		
		.append("	     where                                                                  ")
		
		.append("	        rotaPdv.ROTA_ID=rota.ID                                             ")
		
		.append(" and ( chamadaEncalheCota.POSTERGADO = false or chamadaEncalheCota.POSTERGADO is null ) ")
		
		.append("	        and rota.ROTEIRO_ID=roteiro.ID                                      ")

		.append("	        and ( chamadaEncalhe.DATA_RECOLHIMENTO is null         				")
		.append("	        or chamadaEncalhe.DATA_RECOLHIMENTO=:dataEncalhe )                  ")
		
		.append("   and cota.ID not in  ( select                    ")
        .append("      distinct( cec.COTA_ID )                      ") 
        .append("  from                                             ")
        .append("      controle_conferencia_encalhe_cota cec        ")
        .append("  where                                            ")
        .append("      cec.data_operacao = :dataEncalhe and         ")
        .append("      cec.status = :statusConcluido                ")
		.append("   )												")
		.append("	                                                ")
		.append("	and pdv.PONTO_PRINCIPAL=:principal  ");
		
		if (isSomenteCotasSemAcao) {
			
			sql.append(" and ( chamadaEncalheCota.FECHADO = false or chamadaEncalheCota.FECHADO is null ) 		");
			   		
		}
		
		sql.append(" group by ")
		   .append(" cota.ID  ");
		
		return sql.toString();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FechamentoFisicoLogicoDTO> buscarValorTotalEncalhe(Date dataEncalhe, Long idCota) {

		try {
			
			Criteria criteria = this.getSession().createCriteria(ChamadaEncalhe.class, "ce");
            
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
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void salvarControleFechamentoEncalhe(
			ControleFechamentoEncalhe controleFechamentoEncalhe) {
		this.getSession().save(controleFechamentoEncalhe);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ChamadaEncalheCota> buscarChamadaEncalheCota(Date dataEncalhe, Long idCota) {
		
		Criteria criteria = this.getSession().createCriteria(ChamadaEncalheCota.class, "cec");
		
		criteria.createAlias("cec.chamadaEncalhe", "ce");
        criteria.setFetchMode("ce", FetchMode.JOIN);
        
        criteria.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
        criteria.add(Restrictions.eq("cec.cota.id", idCota));
        criteria.add(Restrictions.eq("cec.fechado", false));
        criteria.add(Restrictions.eq("cec.postergado", false));
        
		return criteria.list();
	}
	
	@Override
	public int buscaQuantidadeConferencia(Date dataEncalhe, boolean porBox) {
		
		Criteria criteria = this.getSession().createCriteria(ConferenciaEncalhe.class, "ce");
		
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
	public Date obterChamdasEncalhePostergadas(Long idCota, Date dataEncalhe) {
		
		Criteria query = this.getSession().createCriteria(ChamadaEncalhe.class, "ce");

		query.createAlias("ce.chamadaEncalheCotas", "cec");
		
		query.setFetchMode("cec", FetchMode.JOIN);
		
		query.add(Restrictions.eq("cec.cota.id", idCota));		
		query.add(Restrictions.gt("ce.dataRecolhimento", dataEncalhe));
		query.add(Restrictions.eq("cec.postergado", false));	
		
		query.setProjection(Projections.alias(Projections.min("ce.dataRecolhimento"), "dataRecolhimento"));
		
		DetachedCriteria subquery = DetachedCriteria.forClass(ChamadaEncalhe.class, "ce");

		subquery.createAlias("ce.chamadaEncalheCotas", "cec");
		
		subquery.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));		
		subquery.add(Restrictions.eq("cec.cota.id", idCota));	
		subquery.setProjection(Projections.alias(Projections.property("ce.produtoEdicao.id"), "idProdutoEdicao"));
		
		query.add(Property.forName("ce.produtoEdicao.id").in(subquery));

		return (Date) query.uniqueResult();
	}

	@Override
	public ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe) {
		Criteria criteria = getSession().createCriteria(ControleFechamentoEncalhe.class);
		criteria.add(Restrictions.eq("dataEncalhe", dataFechamentoEncalhe));
		return (ControleFechamentoEncalhe) criteria.uniqueResult();
	}

	@Override
	public Date buscaDataUltimoControleFechamentoEncalhe() {
		Criteria criteria = getSession().createCriteria(ControleFechamentoEncalhe.class);
		criteria.setProjection(Projections.max("dataEncalhe"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe) {
		Criteria criteria = getSession().createCriteria(FechamentoEncalhePK.class);
		criteria.add(Restrictions.eq("dataEncalhe", dataFechamentoEncalhe));
		criteria.setProjection(Projections.max("dataEncalhe"));
		return (Date) criteria.uniqueResult();
	}
	
	
	
	@Override
	public BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("   SELECT  ");
		
		hql.append("   sum( coalesce(mec.qtde, 0) * coalesce(mec.valoresAplicados.precoComDesconto, 0) ) ");
		
		getQueryAnalitico(filtro, hql);	
		
		Query query = this.getSession().createQuery(hql.toString());
		
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
	public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp ) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("   SELECT  ");
		
		hql.append("	cota.numeroCota as numeroCota,			");
		
		hql.append("    coalesce(pess.nome, pess.razaoSocial, '') as nomeCota, 	");
		
		hql.append("	box.nome as boxEncalhe, 	");
		
		hql.append("   sum( coalesce(mec.qtde, 0)  *  coalesce(mec.valoresAplicados.precoComDesconto, 0)  ) as total ");
		
		hql.append("   , coalesce(div.status, 'POSTERGADA') as statusCobranca ");
		
		getQueryAnalitico(filtro, hql);	
		
		hql.append("   group by cota.id ");
		
		if (sortname != null) {
			hql.append(" order by ");
			if (("asc").equalsIgnoreCase(sortorder)) {
				hql.append(sortname+" asc ");	
			} else if (("desc").equalsIgnoreCase(sortorder)) {
				hql.append(sortname+" desc ");
			}
			
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
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

	@Override
	public Integer buscarTotalAnaliticoEncalhe(	FiltroFechamentoEncalheDTO filtro) {
		StringBuffer hql = new StringBuffer();
		hql.append("   SELECT count( distinct cota.id )  ");
		
		getQueryAnalitico(filtro, hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
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
	
	
	@SuppressWarnings("unchecked")
	public List<Cota> buscarCotaFechamentoChamadaEncalhe(Date dataEncalhe) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("  SELECT cota  ")
			.append("  FROM ChamadaEncalhe chamadaEncalhe ")
			.append("  join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCota ")
			.append("  join chamadaEncalheCota.cota cota ")
			.append("  join chamadaEncalheCota.conferenciasEncalhe conferenciaEncalhe ")
			.append("  join conferenciaEncalhe.controleConferenciaEncalheCota controleConferenciaEncalheCota ")
			.append("  join controleConferenciaEncalheCota.controleConferenciaEncalhe controleConferenciaEncalhe ");

		hql.append("   WHERE controleConferenciaEncalheCota.dataOperacao= :dataEncalhe")
			.append("   AND chamadaEncalheCota.fechado= false")
			.append("   AND chamadaEncalheCota.postergado= false");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setDate("dataEncalhe", dataEncalhe);
        
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cota> buscarCotaChamadaEncalhe(Date dataEncalhe) {
		StringBuffer hql = new StringBuffer();
		hql.append("   SELECT cota  ");
		hql.append("   FROM ChamadaEncalheCota  cec");
		hql.append("   JOIN cec.chamadaEncalhe as ce");
		hql.append("   JOIN cec.cota as  cota");
		hql.append("   WHERE ce.dataRecolhimento= :dataEncalhe");
		hql.append("   AND cec.fechado= false");
		hql.append("   AND cec.postergado= false");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setDate("dataEncalhe", dataEncalhe);
        
		return query.list();
	}

	private void getQueryAnalitico(FiltroFechamentoEncalheDTO filtro,
			StringBuffer hql) {
		
		hql.append("     FROM ControleConferenciaEncalheCota  controle ");

		hql.append("     JOIN controle.cota cota");
		
		hql.append("     JOIN cota.pessoa pess");
		
		hql.append("     JOIN controle.box  box ");

		hql.append("     LEFT JOIN controle.conferenciasEncalhe confEnc ");
		
		hql.append("     LEFT JOIN confEnc.movimentoEstoqueCota mec ");

		hql.append("     LEFT JOIN mec.produtoEdicao pe ");
		
		hql.append("     LEFT JOIN pe.produto pro ");
		
		hql.append("  	LEFT JOIN pe.descontoLogistica as descLogProdEdicao ");
		
		hql.append("  	LEFT JOIN pro.descontoLogistica as descLogProd ");
		
		
		hql.append("     LEFT JOIN controle.cobrancasControleConferenciaEncalheCota cobrancaControle ");
		
		hql.append("     LEFT JOIN cobrancaControle.cobranca cob");
		
		hql.append("     LEFT JOIN cob.divida div");
		

		if (filtro.getFornecedorId() != null) {
			hql.append("     JOIN  pro.fornecedores for ");
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
	
	
}
