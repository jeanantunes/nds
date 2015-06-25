package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
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
	public List<CertificadoNFEDTO> obterCertificado() {
		
		Criteria criteria =  getSession().createCriteria(Certificado.class);	

		// criteria.add(Restrictions.eq("codigo", codigo));
		
		return criteria.list();
	}	
}
