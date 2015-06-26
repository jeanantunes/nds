package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.Certificado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CertificadoRepository;

@Repository
public class CertificadoRepositoryImpl  extends AbstractRepositoryModel<Certificado, Long> implements CertificadoRepository {

	public CertificadoRepositoryImpl() {
		super(Certificado.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CertificadoNFEDTO> obterCertificado(CertificadoNFEDTO filtro) {
		
		Criteria criteria =  getSession().createCriteria(Certificado.class);	

		// criteria.add(Restrictions.eq("codigo", codigo));
		
		return criteria.list();
	}

	@Override
	public Long quantidade(CertificadoNFEDTO filtro) {
		
		Criteria criteria = addRestrictions(filtro.getNomeArquivo());
		criteria.setProjection(Projections.rowCount());
		
		return (Long)criteria.list().get(0);
	}
	
	private Criteria addRestrictions(String nomeArquivo) {
		
		Criteria criteria =  getSession().createCriteria(Certificado.class);	
		
		if(nomeArquivo != null ) {
			criteria.add(Restrictions.eq("nomeArquivo", nomeArquivo));
		}
		
		
		return criteria;
	}
}
