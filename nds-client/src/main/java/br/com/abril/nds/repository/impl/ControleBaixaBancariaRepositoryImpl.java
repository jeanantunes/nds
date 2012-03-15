package br.com.abril.nds.repository.impl;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.ControleBaixaBancaria}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class ControleBaixaBancariaRepositoryImpl extends AbstractRepository<ControleBaixaBancaria,Long> implements ControleBaixaBancariaRepository {

	
	/**
	 * Construtor padrão
	 */
	public ControleBaixaBancariaRepositoryImpl() {
		super(ControleBaixaBancaria.class);
	}
	
	@Override
	public ControleBaixaBancaria obterPorData(Date data) {

		Criteria criteria = super.getSession().createCriteria(ControleBaixaBancaria.class);
		
		criteria.add(Restrictions.eq("data", data));
		
		criteria.setMaxResults(1);
		
		return (ControleBaixaBancaria) criteria.uniqueResult();
	}

}
