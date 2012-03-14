package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemNotaFiscalRepositoryImpl extends
		AbstractRepository<ItemNotaFiscal, Long> implements ItemNotaFiscalRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalRepositoryImpl() {
		super(ItemNotaFiscal.class);
	}
	
	public List<ItemNotaFiscal> buscarItensPorIdNota(Long idNotaFiscal){
		
		String hql = "from ItemNotaFiscal item "				
				+ " where item.notaFiscal.id = :idNotaFiscal";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idNotaFiscal", idNotaFiscal);

		return query.list();
	}

}
