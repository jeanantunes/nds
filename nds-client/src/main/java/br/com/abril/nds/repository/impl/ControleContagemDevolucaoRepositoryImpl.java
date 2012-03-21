package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;



@Repository
public class ControleContagemDevolucaoRepositoryImpl extends AbstractRepository<ControleContagemDevolucao,Long> implements ControleContagemDevolucaoRepository {
	
	public ControleContagemDevolucaoRepositoryImpl(){
		super(ControleContagemDevolucao.class);
	}

}
