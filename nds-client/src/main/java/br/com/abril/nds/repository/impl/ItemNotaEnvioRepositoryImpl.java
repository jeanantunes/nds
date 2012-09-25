package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

@Repository
public class ItemNotaEnvioRepositoryImpl extends AbstractRepositoryModel<ItemNotaEnvio, ItemNotaEnvioPK> implements ItemNotaEnvioRepository {

	public ItemNotaEnvioRepositoryImpl() {
		super(ItemNotaEnvio.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemNotaEnvio> obterItensNotaEnvioPorDataEmissao(Date dataEmissao, Pessoa pessoa) {
		
		Criteria criteria = getSession().createCriteria(ItemNotaEnvio.class);
		
		criteria.createAlias("itemNotaEnvioPK.notaEnvio", "notaEnvio");
		criteria.createAlias("itemNotaEnvioPK.notaEnvio.emitente.pessoa", "pessoa");
		
		criteria.add(Restrictions.eq("notaEnvio.dataEmissao", dataEmissao));
		criteria.add(Restrictions.eq("pessoa", pessoa));
		
		return criteria.list();
	}
}
