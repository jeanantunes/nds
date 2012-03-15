package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemNotaFiscalEntradaRepositoryImpl extends
		AbstractRepository<ItemNotaFiscalEntrada, Long> implements ItemNotaFiscalEntradaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalEntradaRepositoryImpl() {
		super(ItemNotaFiscalEntrada.class);
	}
	
	public List<ItemNotaFiscalEntrada> buscarItensPorIdNota(Long idNotaFiscal){
		
		String hql = "from ItemNotaFiscalEntrada item "				
				+ " where item.notaFiscal.id = :idNotaFiscal";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idNotaFiscal", idNotaFiscal);

		return query.list();
	}

}
