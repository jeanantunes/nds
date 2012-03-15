package br.com.abril.nds.repository.impl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.PoliticaCobranca}
 * 
 * @author Discover Technology
 */
@Repository
public class PoliticaCobrancaRepositoryImpl extends AbstractRepository<PoliticaCobranca,Long> implements PoliticaCobrancaRepository {

	
	/**
	 * Construtor padrão
	 */
	public PoliticaCobrancaRepositoryImpl() {
		super(PoliticaCobranca.class);
	}
	
	@Override
	public PoliticaCobranca obterPorTipoCobranca(TipoCobranca tipoCobranca) {
		
		Criteria criteria = super.getSession().createCriteria(PoliticaCobranca.class);
		
		criteria.add(Restrictions.eq("tipoCobranca", tipoCobranca));
		
		criteria.setMaxResults(1);
		
		return (PoliticaCobranca) criteria.uniqueResult();
	}

}



