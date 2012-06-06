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
	
	@Override
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro) {
		
		StringBuffer hql = new StringBuffer();
		hql.append("   select p.codigo,                                          \n");
		hql.append("          p.nome,                                            \n");
		hql.append("          pe.numeroEdicao,                                   \n");
		hql.append("          pe.precoVenda,                                     \n");
		hql.append("          sum(mec.qtde) as qtde,                             \n");
		hql.append("          (pe.precoVenda * mec.qtde) as total                \n");
		hql.append("     from ConferenciaEncalhe ce,                             \n");
		hql.append("          MovimentoEstoqueCota mec,                          \n");
		hql.append("          ControleConferenciaEncalheCota ccec,               \n");
		hql.append("          ProdutoEdicao pe,                                  \n");
		hql.append("          Produto p,                                         \n");
		hql.append("          ProdutoFornecedor pf                               \n");
		hql.append("    where ce.movimentoEstoqueCota.id = mec.id                \n");
		hql.append("      and ce.controleConferenciaEncalheCota.id = ccec.id     \n");
		hql.append("      and mec.produto_edicao_id = pe.id                      \n");
		hql.append("      and pe.produto_id = p.id                               \n");
		hql.append("      and ccec.box_id = 1                                    \n");
		hql.append("      and pf.produto_id = p.id                               \n");
		hql.append("      and pf.fornecedores_id = 2                             \n");
		hql.append(" group by p.id, p.nome, pe.numero_edicao, pe.preco_venda     \n");
		
		return null;
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
