package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.ControleBaixaBancaria}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class ControleBaixaBancariaRepositoryImpl extends AbstractRepositoryModel<ControleBaixaBancaria,Long> implements ControleBaixaBancariaRepository {

	
	/**
	 * Construtor padrão
	 */
	public ControleBaixaBancariaRepositoryImpl() {
		super(ControleBaixaBancaria.class);
	}
	
	@Override
	public ControleBaixaBancaria obterControleBaixaBancaria(Date dataPagamento, Banco banco) {

		Criteria criteria = super.getSession().createCriteria(ControleBaixaBancaria.class);
		
		criteria.add(Restrictions.eq("dataPagamento", dataPagamento));
		criteria.add(Restrictions.eq("banco", banco));
		
		criteria.setMaxResults(1);
		
		return (ControleBaixaBancaria) criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ControleBaixaBancaria> obterListaControleBaixaBancaria(Date dataOperacao,
																	   StatusControle status) {

		Query query = getSession().createQuery(" select c from ControleBaixaBancaria c where c.dataOperacao=:dataOperacao and c.status=:status ");
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("status", status);
		 
		return query.list();
	}
	
}
