package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
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

		Criteria criteria = this.getSession().createCriteria(ConferenciaEncalhe.class, "ce");
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("p.codigo"), "codigo")
			.add(Projections.property("p.nome"), "produto")
			.add(Projections.property("pe.numeroEdicao"), "edicao")
			.add(Projections.property("pe.precoVenda"),"precoCapa")
			.add(Projections.property("pe.id"), "produtoEdicao")
			.add(Projections.property("pe.parcial"), "parcial")
			.add(Projections.property("che.dataRecolhimento"), "dataRecolhimento")
			.add(Projections.sum("mec.qtde"), "exemplaresDevolucao")
			.add(Projections.groupProperty("p.codigo"))
			.add(Projections.groupProperty("p.nome"))
			.add(Projections.groupProperty("pe.numeroEdicao"))
			.add(Projections.groupProperty("pe.precoVenda"))
			.add(Projections.groupProperty("pe.id"))
			.add(Projections.groupProperty("pe.parcial"))
			.add(Projections.groupProperty("che.dataRecolhimento"))
			
		);
		
		criteria.createAlias("ce.movimentoEstoqueCota", "mec");
		criteria.setFetchMode("mec", FetchMode.JOIN);
		
		criteria.createAlias("ce.controleConferenciaEncalheCota", "ccec");
		criteria.setFetchMode("ccec", FetchMode.JOIN);
		
		criteria.createAlias("mec.produtoEdicao", "pe");
		criteria.setFetchMode("pe", FetchMode.JOIN);
		
		
		criteria.createAlias("pe.produto", "p");
		criteria.setFetchMode("p", FetchMode.JOIN);
		
		criteria.createAlias("ce.chamadaEncalheCota", "cec");
		criteria.setFetchMode("cec", FetchMode.JOIN);
		
		criteria.createAlias("cec.chamadaEncalhe", "che");
		criteria.setFetchMode("che", FetchMode.JOIN);
		
		if (filtro.getFornecedorId() != null) {
			criteria.createAlias("pe.fornecedores", "pf");
			criteria.setFetchMode("pf", FetchMode.JOIN);
		}
		
		criteria.add(Restrictions.eq("ccec.dataOperacao", filtro.getDataEncalhe()));		
		
		if (filtro.getBoxId() != null) {
			criteria.add(Restrictions.eq("ccec.box.id", filtro.getBoxId()));
		}
		
		if (filtro.getFornecedorId() != null) {
			criteria.add(Restrictions.eq("pf.id", filtro.getFornecedorId()));
		}
		
		if (page != null){
			criteria.setFirstResult(page);
		}
		
		if (rp != null){
			criteria.setMaxResults(rp);
		}
		
		if (sortname != null) {
			this.addOrderCriteria(criteria, sortorder, sortname);
		}
		criteria.setResultTransformer(Transformers.aliasToBean(FechamentoFisicoLogicoDTO.class));
			
		return criteria.list();
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
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(
		Date dataEncalhe, String sortorder, String sortname, int page, int rp) {
		
		try {

			Criteria criteria = super.getSession().createCriteria(ChamadaEncalhe.class, "ce");
			
			this.criarCriteriaCotasAusentesEncalhe(criteria, dataEncalhe);

			criteria.setFirstResult(page);
			
			if (rp >= 0) {
				criteria.setMaxResults(rp);
			}
			
			if (sortname != null && sortorder != null) {
				this.addOrderCriteria(criteria, sortorder, sortname);
			}
			
			criteria.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));
				
			return criteria.list();
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		
		try {
			
			Criteria criteria = super.getSession().createCriteria(ChamadaEncalhe.class, "ce");
			
			this.criarCriteriaCotasAusentesEncalhe(criteria, dataEncalhe);

			criteria.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));
			
			List<CotaAusenteEncalheDTO> listaCotasAusentes = criteria.list();
			
			if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()) {
				return 0;
			}
			
			return listaCotasAusentes.size();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void criarCriteriaCotasAusentesEncalhe(Criteria criteria, Date dataEncalhe) {
		
		criteria.createAlias("ce.chamadaEncalheCotas", "cec");
		criteria.createAlias("cec.cota", "cota");
		criteria.createAlias("cota.box", "box");
		criteria.createAlias("cota.pessoa", "pessoa");
		criteria.createAlias("cota.pdvs", "pdvs");
		criteria.createAlias("pdvs.rotas", "rotas");
		criteria.createAlias("rotas.rota", "rota");
		criteria.createAlias("rota.roteiro", "roteiro");
		
		criteria.setFetchMode("cec", FetchMode.JOIN);
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.setFetchMode("box", FetchMode.JOIN);
		criteria.setFetchMode("pessoa", FetchMode.JOIN);
		criteria.setFetchMode("pdvs", FetchMode.JOIN);
		criteria.setFetchMode("rotas", FetchMode.JOIN);
		criteria.setFetchMode("roteiro", FetchMode.JOIN);

		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("cota.id"), "idCota")
				.add(Projections.property("cota.numeroCota"), "numeroCota")
				.add(Projections.property("pessoa.nome"), "colaboradorName")
				.add(Projections.property("box.nome"), "boxName")
				.add(Projections.property("roteiro.descricaoRoteiro"), "roteiroName")
				.add(Projections.property("rota.descricaoRota"), "rotaName")
				.add(Projections.property("cec.fechado"), "fechado")
				.add(Projections.property("cec.postergado"), "postergado")
				.add(Projections.property("ce.dataRecolhimento"), "dataEncalhe")));
				
		criteria.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
		criteria.add(Restrictions.eq("roteiro.tipoRoteiro", TipoRoteiro.NORMAL));

		DetachedCriteria subQuery = DetachedCriteria.forClass(ControleConferenciaEncalheCota.class, "ccec");
		subQuery.add(Restrictions.eq("ccec.dataOperacao", dataEncalhe));
		subQuery.setFetchMode("ccec.cota", FetchMode.JOIN);
		subQuery.setProjection(Property.forName("ccec.cota.id"));

		criteria.add(Property.forName("cec.cota.id").notIn(subQuery));
	}
	
	private void addOrderCriteria(Criteria criteria, String sortorder, String sortname) {
		
		if (("asc").equalsIgnoreCase(sortorder)) {
			criteria.addOrder(Order.asc(sortname));
		} else if (("desc").equalsIgnoreCase(sortorder)) {
			criteria.addOrder(Order.desc(sortname));
		}
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
	public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {

		StringBuffer hql = new StringBuffer();
		hql.append("   SELECT cota.numeroCota as numeroCota,");
		hql.append("          pess.nome as nomeCota,");
		hql.append("          box.nome as boxEncalhe,");
		hql.append("          ce.qtdeInformada * pe.precoVenda as total");
		//hql.append("           as statusCobranca");
		hql.append("     FROM ConferenciaEncalhe ce");
		hql.append("     JOIN ce.controleConferenciaEncalheCota as ccec");
		hql.append("     JOIN ccec.cota as cota");
		hql.append("     JOIN cota.pessoa as pess");
		hql.append("     JOIN ccec.box as box");
		hql.append("     JOIN ce.produtoEdicao as pe");
		hql.append("    WHERE ce.data = :data");
		
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliticoEncalheDTO.class));
		
		query.setDate("data", filtro.getDataEncalhe());
		
		
		

		return query.list();
	}
	
	
}
