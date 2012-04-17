package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.TipoMovimentoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
@Repository
public class TipoMovimentoRepositoryImpl extends AbstractRepository<TipoMovimento, Long> 
										 implements TipoMovimentoRepository {

	@SuppressWarnings("unchecked")
	public List<TipoMovimento> obterTiposMovimento() {
		
		Criteria criteria = super.getSession().createCriteria(TipoMovimento.class);
		
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
	
	/**
	 * Construtor padrão.
	 */
	public TipoMovimentoRepositoryImpl() {
		super(TipoMovimento.class);
	}

}
