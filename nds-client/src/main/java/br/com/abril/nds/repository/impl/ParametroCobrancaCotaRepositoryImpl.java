package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;

@Repository
public class ParametroCobrancaCotaRepositoryImpl extends AbstractRepositoryModel<ParametroCobrancaCota,Long> implements ParametroCobrancaCotaRepository  {

	
	/**
	 * Construtor padrão
	 */
	public ParametroCobrancaCotaRepositoryImpl() {
		super(ParametroCobrancaCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigDecimal> comboValoresMinimos() {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT distinct parametro.valorMininoCobranca ");
		hql.append(" FROM ParametroCobrancaCota parametro ");
		hql.append(" ORDER BY parametro.valorMininoCobranca ASC ");

		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
		
	}
	
	
}
