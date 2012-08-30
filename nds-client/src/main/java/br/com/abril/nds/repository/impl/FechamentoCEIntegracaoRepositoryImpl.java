package br.com.abril.nds.repository.impl;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;

@Repository
public class FechamentoCEIntegracaoRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements
		FechamentoCEIntegracaoRepository {
	
	public FechamentoCEIntegracaoRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FechamentoCEIntegracaoDTO> buscarConferenciaEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		 
		Criteria criteria = this.getSession().createCriteria(ConferenciaEncalhe.class, "ce");
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("p.codigo"), "codigo")
			.add(Projections.property("p.nome"), "produto")
			.add(Projections.property("pe.numeroEdicao"), "edicao")
			.add(Projections.property("pe.precoVenda"), "precoCapa")
			.add(Projections.property("ce.qtdeInformada"), "reparte")
			.add(Projections.property("ce.qtde"), "encalhe")
			.add(Projections.property("cec.fechado"), "tipo")
			.add(Projections.groupProperty("p.codigo"))
			.add(Projections.groupProperty("p.nome"))
			.add(Projections.groupProperty("pe.numeroEdicao"))
			.add(Projections.groupProperty("pe.precoVenda"))
			.add(Projections.groupProperty("pe.id"))
		);
		
		criteria.createAlias("ce.movimentoEstoqueCota", "mec");
		criteria.setFetchMode("mec", FetchMode.JOIN);
		
		criteria.createAlias("ce.controleConferenciaEncalheCota", "ccec");
		criteria.setFetchMode("ccec", FetchMode.JOIN);
		
		criteria.createAlias("ce.chamadaEncalheCota", "cec");
		criteria.setFetchMode("cec", FetchMode.JOIN);
		
		criteria.createAlias("mec.produtoEdicao", "pe");
		criteria.setFetchMode("pe", FetchMode.JOIN);
		
		criteria.createAlias("pe.produto", "p");
		criteria.setFetchMode("p", FetchMode.JOIN);
		
		criteria.createAlias("p.fornecedores", "pf");
		criteria.setFetchMode("pf", FetchMode.JOIN);
		
		criteria.createAlias("mec.cota", "cota");
		criteria.setFetchMode("cota", FetchMode.JOIN);
		
		if(filtro.getData() != null){
			criteria.add(Restrictions.between("ce.data", filtro.getData(), DateUtils.addDays(filtro.getData(),7) ));			
		}
		
		if (filtro.getIdFornecedor() != -1) {
			criteria.add(Restrictions.eq("pf.id", filtro.getIdFornecedor()));
		}
		
		if (filtro.getPaginacao().getQtdResultadosPorPagina() != null){
			criteria.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosPorPagina() != null){
			criteria.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}	
		
		criteria.setResultTransformer(Transformers.aliasToBean(FechamentoCEIntegracaoDTO.class));
			
		return criteria.list();
	}

	@Override
	public Long obterDesconto(Long idCota, Long idProdutoEdica,
			Long idFornecedor) {
		// TODO Auto-generated method stub
		return null;
	}

}
