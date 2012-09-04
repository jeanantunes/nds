package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.repository.DistribuidorRepository;

@Repository
public class DistribuidorRepositoryImpl extends
		AbstractRepositoryModel<Distribuidor, Long> implements
		DistribuidorRepository {

	public DistribuidorRepositoryImpl() {
		super(Distribuidor.class);
	}

	@Override
	public Distribuidor obter() {
		String hql = "from Distribuidor";
		Query query = getSession().createQuery(hql);		
		return (Distribuidor) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores,
															OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder hql =
			new StringBuilder("from DistribuicaoFornecedor where fornecedor.id in (:idsFornecedores) ");

		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsFornecedores", idsForncedores);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoDistribuidor> buscarDiasDistribuicaoDistribuidor(
															Long idDistruibuidor,
															OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append("from DistribuicaoDistribuidor ");
		hql.append("where distribuidor.id = :idDistribuidor ");
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idDistribuidor", idDistruibuidor);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterEnderecoPrincipal()
	 */
	@Override
	public EnderecoDistribuidor obterEnderecoPrincipal(){
		Criteria criteria=  getSession().createCriteria(EnderecoDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );
		criteria.setMaxResults(1);

		return (EnderecoDistribuidor) criteria.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterTelefonePrincipal()
	 */
	@Override
	public TelefoneDistribuidor obterTelefonePrincipal(){
		Criteria criteria=  getSession().createCriteria(TelefoneDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );
		criteria.setMaxResults(1);

		return (TelefoneDistribuidor) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {
		Criteria criteria =  getSession().createCriteria(TipoGarantiaAceita.class);
		criteria.setProjection(Projections.property("tipoGarantia"));
		return criteria.list();
	}

	@Override
	public String obterInformacoesComplementaresProcuracao() {
		
		return (String) 
				this.getSession().
				createQuery(
						"select d.informacoesComplementaresProcuracao from Distribuidor d").uniqueResult();
	}
}