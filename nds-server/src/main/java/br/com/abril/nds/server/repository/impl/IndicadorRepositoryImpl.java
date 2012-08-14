package br.com.abril.nds.server.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.impl.AbstractRepositoryModel;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.repository.IndicadorRepository;
import br.com.abril.nds.util.DateUtil;

@Repository
public class IndicadorRepositoryImpl extends AbstractRepositoryModel<Indicador, Long> implements
		IndicadorRepository {

	public IndicadorRepositoryImpl() {
		super(Indicador.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Indicador> buscarIndicadores() {
		
		String queryString = " select indicador from Indicador indicador "
						   + " where data = "
						   + " (select max(indicador.data) from Indicador indicador where data between :dataAtual and :dataDeAmanha) "
						   + " group by indicador.tipoIndicador "
						   + " order by indicador.operacaoDistribuidor.uf, indicador.operacaoDistribuidor.idDistribuidorInterface, "
				           + " indicador.grupoIndicador, indicador.tipoIndicador ";
		
		Query query = this.getSession().createQuery(queryString);
		
		Date dataAtual = DateUtil.removerTimestamp(new Date());
		
		Date dataDeAmanha = DateUtil.adicionarDias(dataAtual, 1);
		
		query.setParameter("dataAtual", dataAtual);
		query.setParameter("dataDeAmanha", dataDeAmanha);
		
		return query.list();
	}
}