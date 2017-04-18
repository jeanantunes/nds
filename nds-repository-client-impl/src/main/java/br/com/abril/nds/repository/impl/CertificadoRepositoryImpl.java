package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
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
		
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" cert.ID as id, cert.SENHA as senha, cert.ALIAS as alias, cert.NOME_ARQUIVO as nomeArquivo, ");
		sql.append(" cert.EXTENSAO as extensao, ");
		sql.append(" cert.DATA_INICIO as dataInicio, cert.DATA_FIM as dataFim ");
		sql.append(" from CERTIFICADO as cert ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CertificadoNFEDTO.class));
		
		return query.list();
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
