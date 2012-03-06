package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Repository
public class ParametroSistemaRepositoryImpl extends AbstractRepository<ParametroSistema, Long>
		implements ParametroSistemaRepository {

	public ParametroSistemaRepositoryImpl() {
		super(ParametroSistema.class);
	}
	
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema){
		String hql = "from ParametroSistema p where p.tipoParametroSistema = :tipoParametroSistema";
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("tipoParametroSistema", tipoParametroSistema);
		
		query.setMaxResults(1);
		
		return (ParametroSistema) query.uniqueResult();
	}
}