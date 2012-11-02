package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;

public interface ControleConferenciaEncalheRepository extends Repository<ControleConferenciaEncalhe,Long> { 

	public ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao);

	
}
