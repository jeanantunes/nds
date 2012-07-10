package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.PermissaoRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.seguranca.Permissao}
 * 
 * @author InfoA2
 *
 */
@Repository
public class PermissaoRepositoryImpl extends AbstractRepositoryModel<Permissao,Long> implements PermissaoRepository {

	/**
	 * Construtor Padrão
	 */
	public PermissaoRepositoryImpl() {
		super(Permissao.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.PermissaoRepository#busca(java.lang.String, java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	public List<Permissao> busca(String nome, String descricao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		Criteria criteria = addRestrictions(nome, descricao);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setMaxResults(maxResults);
		criteria.setFirstResult(initialResult);
		
		return criteria.list();
	}

	/**
	 * Adiciona a restrição ás consultas
	 * @param nome
	 * @param descricao
	 * @return Criteria
	 */
	private Criteria addRestrictions(String nome, String descricao) {
		Criteria criteria =  getSession().createCriteria(Permissao.class);	
		
		if(!StringUtil.isEmpty(nome)){
			criteria.add(Restrictions.ilike("nome", nome));
		}
		
		if(!StringUtil.isEmpty(descricao)){
			criteria.add(Restrictions.eq("descricao", descricao));
		}
		
		return criteria;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.PermissaoRepository#quantidade(java.lang.String, java.lang.String)
	 */
	@Override
	public Long quantidade(String nome, String descricao) {
		Criteria criteria = addRestrictions(nome, descricao);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

}
