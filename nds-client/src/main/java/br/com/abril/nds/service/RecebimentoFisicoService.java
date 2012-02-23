package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.estoque.RecebimentoFisico;

public interface RecebimentoFisicoService {


	
	List<RecebimentoFisico> obterRecebimentoFisico(); 
	void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico);
}
