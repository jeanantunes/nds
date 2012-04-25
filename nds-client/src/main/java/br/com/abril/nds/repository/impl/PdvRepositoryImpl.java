package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.PDV;
import br.com.abril.nds.repository.PdvRepository;

@Repository
public class PdvRepositoryImpl extends AbstractRepository<PDV, Long> implements PdvRepository {

	public PdvRepositoryImpl() {
		super(PDV.class);
	}
	
	public List<PDV> obterPDVsPorCota(FiltroPdvDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT pdv FROM PDV pdv ")
		.append(" JOIN pdv.cota cota ")
		.append(" JOIN pdv.enderecos endereco ")
		.append(" JOIN pdv.telefones telefone ")
		.append(" WHERE cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", filtro.getIdCota());
		
		return query.list();
		
	}
	
}
