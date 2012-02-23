package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;

/**
 * 
 * @author Gustavo
 *
 */
@Repository
public class RecebimentoFisicoRepositoryImpl extends AbstractRepository<RecebimentoFisico, Long> 
									  implements RecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public RecebimentoFisicoRepositoryImpl() {
		super(RecebimentoFisico.class);
	}

	@Override
	public List<RecebimentoFisico> obterRecebimentoFisico() {
		return null;
		
	}
	@Override
	public void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico){
		this.adicionar(recebimentoFisico);
	}
	
	
}
	

