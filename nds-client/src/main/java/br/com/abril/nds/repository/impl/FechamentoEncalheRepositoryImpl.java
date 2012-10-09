package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

@Repository
public class FechamentoEncalheRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements FechamentoEncalheRepository {

	public FechamentoEncalheRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp) {
		
		StringBuilder hql = new StringBuilder();
		
		StringBuilder subquery = new StringBuilder();
		subquery.append(" select sum( vp.qntProduto ) ");
		subquery.append(" from VendaProduto vp ");
		subquery.append(" where vp.produtoEdicao = pe  and vp.dataVenda = :dataEncalhe and  vp.tipoVenda= :tipoVenda ");
	
		hql.append("SELECT distinct  p.codigo as  codigo ");
		hql.append(" , p.nome as produto ");
		hql.append(" , pe.numeroEdicao as edicao");
		hql.append(" , pe.precoVenda as precoCapa ");
		hql.append(" , pe.id as produtoEdicao ");
		hql.append(" , pe.parcial as parcial ");
		hql.append(" , che.dataRecolhimento as dataRecolhimento ");
		hql.append(" , nullif ( sum (mec.qtde - ("+ subquery.toString()  +")  ), 0 ) as exemplaresDevolucao ");
		hql.append(" from ConferenciaEncalhe as ce ");
		hql.append("  JOIN ce.movimentoEstoqueCota as mec ");
		hql.append("  JOIN ce.controleConferenciaEncalheCota as ccec ");
		hql.append("  JOIN mec.produtoEdicao as pe ");		
		hql.append("  JOIN pe.produto as p ");
		hql.append("  JOIN ce.chamadaEncalheCota as cec ");
		hql.append("  JOIN cec.chamadaEncalhe as che ");

		
		if (filtro.getFornecedorId() != null) {
			hql.append("  JOIN p.fornecedores as pf ");
			
		}
		
		hql.append(" WHERE ccec.dataOperacao =:dataEncalhe ");

		if (filtro.getBoxId() != null) {
			hql.append("  and ccec.box.id = :boxId ");
		}
		
		if (filtro.getFornecedorId() != null) {
			hql.append("  and pf.id = :fornecedorId ");
		}
		
		
		hql.append(" group by p.codigo,  p.nome, pe.numeroEdicao, pe.precoVenda, pe.id , pe.parcial, che.dataRecolhimento");
		
		if (sortname != null) {
			hql.append(" order by ");
			if (("asc").equalsIgnoreCase(sortorder)) {
				hql.append(sortname+" asc ");	
			} else if (("desc").equalsIgnoreCase(sortorder)) {
				hql.append(sortname+" desc ");
			}
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setDate("dataEncalhe", filtro.getDataEncalhe());
		query.setParameter("tipoVenda", TipoVendaEncalhe.ENCALHE);
		
		
		
		
		
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

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe, String sortorder, String sortname, int page, int rp) {
		
		Query query = this.criarQueryCotasAusentesEncalhe(dataEncalhe, sortorder, sortname);
		
		query.setFirstResult(page);
		if (rp >= 0) {
			query.setMaxResults(rp);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		
		Query query = this.criarQueryCotasAusentesEncalhe(dataEncalhe, null, null);
		
		List<CotaAusenteEncalheDTO> listaCotasAusentes = query.list();
		
		if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()) {
			return 0;
		}
		
		return listaCotasAusentes.size();		
	}
	
	
	private Query criarQueryCotasAusentesEncalhe(Date dataEncalhe, String sortorder, String sortname) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("    SELECT cec.cota.id as idCota");
		hql.append("          ,cec.cota.numeroCota as numeroCota");
		hql.append("          ,cec.cota.pessoa.nome as colaboradorName");
		hql.append("          ,cec.cota.box.nome as boxName");
		//hql.append("          ,cec.cota.pdvs.rotas.rota.roteiro.descricaoRoteiro as roteiroName");
		//hql.append("          ,cec.cota.pdvs.rotas.rota.descricaoRota as rotaName");
		hql.append("          ,cec.fechado as fechado");
		hql.append("          ,cec.postergado as postergado");
		hql.append("          ,cec.chamadaEncalhe.dataRecolhimento as dataEncalhe");
		hql.append("      FROM ChamadaEncalheCota cec");
		hql.append("     WHERE cec.chamadaEncalhe.dataRecolhimento = :dataEncalhe");
		hql.append("       AND cec NOT IN (");
		hql.append("              SELECT ce.chamadaEncalheCota");
		hql.append("                FROM ConferenciaEncalhe ce");
		hql.append("               WHERE ce.data = :dataEncalhe");
		hql.append("       )");
		if (sortname != null && sortorder != null) {
			hql.append("  ORDER BY " + sortname + " " + sortorder);
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));
		
		query.setDate("dataEncalhe", dataEncalhe);
		
		return query;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp ) {

		StringBuffer hql = new StringBuffer();
		hql.append("   SELECT cota.numeroCota as numeroCota ");
		hql.append("       , pess.nome as nomeCota");
		hql.append("       , box.nome as boxEncalhe");
		hql.append("       ,  cob.valor as total ");
		hql.append("       ,  div.status  as statusCobranca");
		
		getQueryAnalitico(filtro, hql);
	
		
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
		query.setDate("dataEncalhe", filtro.getDataEncalhe());
		
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
	

		
		

		return query.list();
	}

	@Override
	public Integer buscarTotalAnaliticoEncalhe(	FiltroFechamentoEncalheDTO filtro) {
		StringBuffer hql = new StringBuffer();
		hql.append("   SELECT count(*)  ");
		
		getQueryAnalitico(filtro, hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setDate("dataEncalhe", filtro.getDataEncalhe());
		
		if (filtro.getBoxId() != null) {
			query.setLong("boxId", filtro.getBoxId());
		}	
		
		if (filtro.getFornecedorId() != null) {
			query.setLong("fornecedorId", filtro.getFornecedorId());
		}
			

		return ((Long)query.uniqueResult()).intValue();
	}

	private void getQueryAnalitico(FiltroFechamentoEncalheDTO filtro,
			StringBuffer hql) {
		hql.append("     FROM CobrancaControleConferenciaEncalheCota  ce");
		hql.append("     JOIN ce.cobranca as cob");
		hql.append("     JOIN cob.divida as div");
		hql.append("     JOIN ce.controleConferenciaEncalheCota as ccec");
		
		hql.append("     JOIN ccec.cota as cota");
		hql.append("     JOIN cota.pessoa as pess");
		hql.append("     JOIN ccec.box as  box ");
		
		if (filtro.getFornecedorId() != null) {
			
			hql.append("     JOIN ccec.conferenciasEncalhe as  confEnc ");
			hql.append("     JOIN confEnc.produtoEdicao as  pe ");
			hql.append("     JOIN pe.produto as  pro ");
			hql.append("     JOIN  pro.fornecedores as  for ");
		}
		
		hql.append("    WHERE ccec.dataOperacao = :dataEncalhe ");
		if (filtro.getBoxId() != null) {
			hql.append("     and  box.id = :boxId ");
		}	
		
		if (filtro.getFornecedorId() != null) {
			hql.append("     and for.id =:fornecedorId ");
		}
	}
	
	
}
