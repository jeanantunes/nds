package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoRepositoryImpl extends AbstractRepositoryModel<Estudo, Long> implements EstudoRepository {

	/**
	 * Construtor.
	 */
	public EstudoRepositoryImpl() {
		
		super(Estudo.class);
	}
	
	@Override
	public void removerEstudos(List<Long> listIdEstudos) {
		
		StringBuilder hql = new StringBuilder(" delete from Estudo e ");
		
		hql.append(" where e.id in (:listIdEstudos) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}

}
