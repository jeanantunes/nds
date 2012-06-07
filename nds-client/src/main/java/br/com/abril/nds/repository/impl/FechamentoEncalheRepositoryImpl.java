package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

@Repository
public class FechamentoEncalheRepositoryImpl extends AbstractRepository<FechamentoEncalhe, FechamentoEncalhePK> implements FechamentoEncalheRepository {

	public FechamentoEncalheRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		Criteria criteria = this.getSession().createCriteria(ConferenciaEncalhe.class, "ce");
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("p.codigo"), "codigo")
			.add(Projections.property("p.nome"), "produto")
			.add(Projections.property("pe.numeroEdicao"), "edicao")
			.add(Projections.property("pe.precoVenda"), "precoCapa")
			.add(Projections.sum("mec.qtde"), "exemplaresDevolucao")
			.add(Projections.groupProperty("p.codigo"))
			.add(Projections.groupProperty("p.nome"))
			.add(Projections.groupProperty("pe.numeroEdicao"))
			.add(Projections.groupProperty("pe.precoVenda"))
		);
		
		criteria.createAlias("ce.movimentoEstoqueCota", "mec");
		criteria.setFetchMode("mec", FetchMode.JOIN);
		
		criteria.createAlias("ce.controleConferenciaEncalheCota", "ccec");
		criteria.setFetchMode("ccec", FetchMode.JOIN);
		
		criteria.createAlias("mec.produtoEdicao", "pe");
		criteria.setFetchMode("pe", FetchMode.JOIN);
		
		criteria.createAlias("pe.produto", "p");
		criteria.setFetchMode("p", FetchMode.JOIN);
		
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
		
		criteria.setFirstResult(page);
		criteria.setMaxResults(rp);
		this.addOrderCriteria(criteria, sortorder, sortname);
		criteria.setResultTransformer(Transformers.aliasToBean(FechamentoFisicoLogicoDTO.class));
			
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
			criteria.setMaxResults(rp);
			
			this.addOrderCriteria(criteria, sortorder, sortname);
			
			criteria.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));
				
			return criteria.list();
		
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void criarCriteriaCotasAusentesEncalhe(Criteria criteria, Date dataEncalhe) {
		
		criteria.createAlias("ce.chamadaEncalheCotas", "cec");
		criteria.createAlias("cec.cota", "cota");
		criteria.createAlias("cota.box", "box");
		criteria.createAlias("cota.pessoa", "pessoa");
		criteria.createAlias("box.roteiros", "roteiros");
		criteria.createAlias("roteiros.rotas", "rotas");
		
		criteria.setFetchMode("cec", FetchMode.JOIN);
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.setFetchMode("box", FetchMode.JOIN);
		criteria.setFetchMode("pessoa", FetchMode.JOIN);
		criteria.setFetchMode("roteiros", FetchMode.JOIN);
		criteria.setFetchMode("rotas", FetchMode.JOIN);

		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("cota.id"), "idCota")
				.add(Projections.property("cota.numeroCota"), "numeroCota")
				.add(Projections.property("pessoa.nome"), "colaboradorName")
				.add(Projections.property("box.nome"), "boxName")
				.add(Projections.property("roteiros.descricaoRoteiro"), "roteiroName")
				.add(Projections.property("rotas.descricaoRota"), "rotaName")));

		criteria.add(Restrictions.eq("ce.dataRecolhimento", dataEncalhe));
		criteria.add(Restrictions.eq("roteiros.tipoRoteiro", TipoRoteiro.NORMAL));

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
	
}
