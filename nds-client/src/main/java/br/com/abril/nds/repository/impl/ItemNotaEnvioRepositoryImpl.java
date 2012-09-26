package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

@Repository
public class ItemNotaEnvioRepositoryImpl extends AbstractRepositoryModel<ItemNotaEnvio, ItemNotaEnvioPK> implements ItemNotaEnvioRepository {

	public ItemNotaEnvioRepositoryImpl() {
		super(ItemNotaEnvio.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemNotaEnvio> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota) {
		
		String hql = "from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.listaMovimentoEstoqueCota movimentoEstoqueCota "
				   + " where notaEnvio.dataEmissao = :dataEmissao"
				   + " and movimentoEstoqueCota.cota.numeroCota = :numeroCota";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("dataEmissao", dataEmissao);
		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
	}
	
}
