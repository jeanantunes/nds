package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;

public interface ControleConferenciaEncalheRepository extends Repository<ControleConferenciaEncalhe,Long> { 

	public ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao);

	
}
