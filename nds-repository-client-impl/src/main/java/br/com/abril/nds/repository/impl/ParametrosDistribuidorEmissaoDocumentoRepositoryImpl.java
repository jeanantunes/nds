package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;

@Repository
public class ParametrosDistribuidorEmissaoDocumentoRepositoryImpl extends AbstractRepositoryModel<ParametrosDistribuidorEmissaoDocumento, Long> implements ParametrosDistribuidorEmissaoDocumentoRepository {

	public ParametrosDistribuidorEmissaoDocumentoRepositoryImpl() {
		super(ParametrosDistribuidorEmissaoDocumento.class);
	}

	@Override
	public void alterarOuCriar(ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumento) {
		getSession().saveOrUpdate(parametrosDistribuidorEmissaoDocumento);
	}
	
	
	public boolean isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select  				");
		hql.append(" p.utilizaImpressao 	");
		hql.append(" from ");
		hql.append(" ParametrosDistribuidorEmissaoDocumento p ");
		hql.append(" where   ");
		hql.append(" p.tipoParametrosDistribuidorEmissaoDocumento = :tipoParametrosDistribuidorEmissaoDocumento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("tipoParametrosDistribuidorEmissaoDocumento", tipoParametrosDistribuidorEmissaoDocumento);
		
		query.setMaxResults(1);
		
		Boolean isUtilizaImpressao = (Boolean) query.uniqueResult();
		
		return (isUtilizaImpressao == null) ? false : isUtilizaImpressao; 
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUtilizaEnvioEmail(TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select  				");
		hql.append(" p.utilizaEmail	 		");
		hql.append(" from ");
		hql.append(" ParametrosDistribuidorEmissaoDocumento p ");
		hql.append(" where   ");
		hql.append(" p.tipoParametrosDistribuidorEmissaoDocumento = :tipoParametrosDistribuidorEmissaoDocumento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("tipoParametrosDistribuidorEmissaoDocumento", tipoParametrosDistribuidorEmissaoDocumento);
		
		query.setMaxResults(1);
		
		Boolean isUtilizaImpressao = (Boolean) query.uniqueResult();
		
		return (isUtilizaImpressao == null) ? false : isUtilizaImpressao; 
	}
}
