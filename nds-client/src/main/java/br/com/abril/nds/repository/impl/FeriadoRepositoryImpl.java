package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.repository.FeriadoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Feriado}
 * 
 * @author Discover Technology
 */
@Repository
public class FeriadoRepositoryImpl extends AbstractRepository<Feriado, Long> 
										 implements FeriadoRepository {

	/**
	 * Construtor padrão.
	 */
	public FeriadoRepositoryImpl() {
		super(Feriado.class);
	}

	@Override
	public Feriado obterPorData(Date data) {

		Criteria criteria = super.getSession().createCriteria(Feriado.class);
		
		criteria.add(Restrictions.eq("data", data));
		
		criteria.setMaxResults(1);
		
		return (Feriado) criteria.uniqueResult();
	}

}
