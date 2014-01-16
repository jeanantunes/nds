package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaVO;
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
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(
			Integer numeroCota, Long politicaCobrancaId) {
		
        StringBuilder hql = new StringBuilder();
        hql.append(" select cotaUnificacao ")
		   .append(" from CotaUnificacao cotaUnificacao ")
		   .append(" where cotaUnificacao.cota.numeroCota = :numeroCota ")
		   .append(" and cotaUnificacao.politicaCobranca.id = :politicaCobrancaId ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("politicaCobrancaId", politicaCobrancaId);
		
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
	public List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora,
			Long politicaCobrancaId) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" c.numeroCota as numero, ")
		   .append(" case when c.pessoa.cnpj is not null then concat('(', c.pessoa.razaoSocial, ')') else concat('(', c.pessoa.nome, ')') end as nome ")
		   .append(" from CotaUnificacao cu ")
		   .append(" join cu.cotas c ")
		   .append(" where cu.cota.numeroCota = :numeroCotaCentralizadora ")
		   .append(" and cu.politicaCobranca.id = :politicaCobrancaId ")
		   .append(" order by c.numeroCota ");
		
		Query query =
			this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCotaCentralizadora", numeroCotaCentralizadora);
		query.setParameter("politicaCobrancaId", politicaCobrancaId);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaVO.class));
		
		return query.list();
	}

	@Override
	public boolean verificarCotaUnificadora(Integer numeroCota, Long politicaCobrancaId) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" count(c.id) from CotaUnificacao c ")
		   .append(" where c.cota.numeroCota = :numeroCota ");
		
		if (politicaCobrancaId != null){
			
			hql.append(" and c.politicaCobranca.id = :politicaCobrancaId ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		if (politicaCobrancaId != null){
			
			query.setParameter("politicaCobrancaId", politicaCobrancaId);
		}
		
		return (Long)query.uniqueResult() > 0;
	}

	@Override
	public boolean verificarCotaUnificada(Integer numeroCota, Long politicaCobrancaId) {
		
		StringBuilder hql = new StringBuilder("select count(c.id) ");
		hql.append(" from CotaUnificacao c ")
		   .append(" join c.cotas cotaUnificada ")
		   .append(" where cotaUnificada.numeroCota = :numeroCota");
		
		if (politicaCobrancaId != null){
			
			hql.append(" and c.politicaCobranca.id = :politicaCobrancaId");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		if (politicaCobrancaId != null){
		
			query.setParameter("politicaCobrancaId", politicaCobrancaId);
		}
		
		return (Long)query.uniqueResult() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> buscarNumeroCotasUnificadoras(Long politicaCobrancaId) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" cu.cota.numeroCota from CotaUnificacao cu ")
		   .append(" where cu.politicaCobranca.id = :politicaCobrancaId ")
		   .append(" order by cu.cota.numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("politicaCobrancaId", politicaCobrancaId);
		
		return query.list();
	}

	@Override
	public void removerCotaUnificacao(Long politicaCobrancaId) {
		
		Query query = 
			this.getSession().createQuery(
				"delete from CotaUnificacao c where c.politicaCobranca.id = :politicaCobrancaId");
		
		query.setParameter("politicaCobrancaId", politicaCobrancaId);
		
		query.executeUpdate();
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
}