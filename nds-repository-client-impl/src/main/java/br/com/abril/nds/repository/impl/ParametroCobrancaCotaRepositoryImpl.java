package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		
		hql.append(" SELECT distinct parametro.cota.valorMinimoCobranca ");
		hql.append(" FROM ParametroCobrancaCota parametro ");
		hql.append(" WHERE parametro.cota.valorMinimoCobranca is not null ");
		hql.append(" ORDER BY parametro.cota.valorMinimoCobranca ASC ");

		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
		
	}

	@Override
	public ParametroCobrancaCota obterParametroCobrancaCotaPorCota(Integer numeroCota) {
		
		Query query = this.getSession().createQuery("select p from ParametroCobrancaCota p where p.cota.numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		
		return (ParametroCobrancaCota) query.uniqueResult();
	}
	
	@Override
	public Cota obterCotaPorParametroCobranca(ParametroCobrancaCota parametroCobrancaCota) {
		
		Query query = this.getSession().createQuery("select c from Cota c where c.parametroCobranca = :parametroCobrancaCota");
		
		query.setParameter("parametroCobrancaCota", parametroCobrancaCota);
		
		return (Cota) query.uniqueResult();
	}

	@Override
	public boolean verificarCotaSemParametroCobrancaPorFormaCobranca(Long id) {
		
		Query query = 
			this.getSession().createQuery(
				"select case when count(c.id) > 0 then true else false end " +
				"from Cota c " +
				"left join c.parametroCobranca p " +
				"join p.formasCobrancaCota f " +
				"where f.id = :id " +
				"and p.id is null ");
		
		query.setParameter("id", id);
		
		return (boolean) query.uniqueResult();
	}
}
