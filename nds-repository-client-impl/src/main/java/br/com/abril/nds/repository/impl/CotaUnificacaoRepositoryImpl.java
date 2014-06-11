package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaUnificacaoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class CotaUnificacaoRepositoryImpl extends AbstractRepositoryModel<CotaUnificacao,Long> implements CotaUnificacaoRepository {

	/**
	 * Construtor padrão
	 */
	public CotaUnificacaoRepositoryImpl() {
		super(CotaUnificacao.class);
	}

	/**
	 * Obtem Unificacao de Cotas por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota) {
		
        StringBuilder hql = new StringBuilder();
        hql.append(" select cotaUnificacao ")
		   .append(" from CotaUnificacao cotaUnificacao ")
		   .append(" where cotaUnificacao.cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return (CotaUnificacao) query.uniqueResult();
	}
	
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota) {
		
        StringBuilder hql = new StringBuilder();
        hql.append(" select cotaUnificacao ")
		   .append(" from CotaUnificacao cotaUnificacao ")
		   .append(" where cotaUnificacao.cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return (CotaUnificacao) query.uniqueResult();
	}

	/**
	 * Obtem unificacao de Cotas por Cota Centralizada
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota) {
		
		    StringBuilder hql = new StringBuilder();
			
			hql.append(" select cotaUnificacao ");
			
			hql.append(" from CotaUnificacao cotaUnificacao ");
			
			hql.append(" join cotaUnificacao.cotas as cota ");
			
			hql.append(" where cota.numeroCota = :numeroCota ");
			
			Query query = this.getSession().createQuery(hql.toString());
			
			query.setParameter("numeroCota", numeroCota);
			
			return (CotaUnificacao) query.uniqueResult(); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" c.numeroCota as numero, ")
		   .append(" case when c.pessoa.cnpj is not null then concat(c.pessoa.razaoSocial, ' (', :siglaPJ, ')') else concat(c.pessoa.nome, ' (', :siglaPF, ')') end as nome ")
		   .append(" from CotaUnificacao cu ")
		   .append(" join cu.cotas c ")
		   .append(" where cu.cota.numeroCota = :numeroCotaCentralizadora ")
		   .append(" order by c.numeroCota ");
		
		Query query =
			this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCotaCentralizadora", numeroCotaCentralizadora);
		query.setParameter("siglaPJ", "PJ");
		query.setParameter("siglaPF", "PF");
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaVO.class));
		
		return query.list();
	}

	@Override
	public boolean verificarCotaUnificadora(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" count(c.id) from CotaUnificacao c ")
		   .append(" where c.cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return (Long)query.uniqueResult() > 0;
	}

	@Override
	public boolean verificarCotaUnificada(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select count(c.id) ");
		hql.append(" from CotaUnificacao c ")
		   .append(" join c.cotas cotaUnificada ")
		   .append(" where cotaUnificada.numeroCota = :numeroCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return (Long)query.uniqueResult() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> buscarNumeroCotasUnificadoras() {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" cu.cota.numeroCota from CotaUnificacao cu ")
		   .append(" order by cu.cota.numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaUnificacao> obterCotaUnificacaoPorCotaUnificada(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder("select distinct c ");
		hql.append(" from CotaUnificacao c ")
		   .append(" join c.cotas cotaUnificada ")
		   .append(" where cotaUnificada.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
	}
	
	@Override
	public Cota obterCotaUnificadoraPorCota(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder("select distinct cu.cota ");
		hql.append(" from CotaUnificacao cu ")
		   .append(" join cu.cotas cotaUnificada ")
		   .append(" where cu.cota.numeroCota = :numeroCota ")
		   .append(" or cotaUnificada.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		return (Cota) query.uniqueResult();
	}
	
	@Override
    public Long obterIdCotaUnificadoraPorCota(Long idCota){
        
        StringBuilder hql = new StringBuilder("select distinct cu.cota.id ");
        hql.append(" from CotaUnificacao cu ")
           .append(" join cu.cotas cotaUnificada ")
           .append(" where cu.cota.id = :idCota ")
           .append(" or cotaUnificada.id = :idCota ");
        
        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        
        return (Long) query.uniqueResult();
    }
}