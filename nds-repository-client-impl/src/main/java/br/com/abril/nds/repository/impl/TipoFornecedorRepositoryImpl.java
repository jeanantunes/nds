package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoFornecedorRepository;

@Repository
public class TipoFornecedorRepositoryImpl extends AbstractRepositoryModel<TipoFornecedor, Long> 
										  implements TipoFornecedorRepository {

	public TipoFornecedorRepositoryImpl() {
		super(TipoFornecedor.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComboTipoFornecedorDTO> obterComboTipoFornecedor() {

		String hql = " select tipoFornecedor.id as id, " 
				   + " tipoFornecedor.descricao as descricao " 
				   + " from TipoFornecedor tipoFornecedor ";
		
		Query query = getSession().createQuery(hql);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ComboTipoFornecedorDTO.class));
		
		return query.list();
	}
	

}
