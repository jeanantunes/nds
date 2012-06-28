package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.repository.ProcuracaoEntregadorRepository;


@Repository
public class ProcuracaoEntregadorRepositoryImpl extends AbstractRepositoryModel<ProcuracaoEntregador, Long> 
												implements ProcuracaoEntregadorRepository {
	
	public ProcuracaoEntregadorRepositoryImpl() {
		super(ProcuracaoEntregador.class);
	}

}
