package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.Semaforo;

public interface SemaforoService {

	public List<Semaforo> obterStatusProcessosEncalhe(Date data);
	
	void atualizarStatusProcessoEncalheIniciadoEm(Date data);
	
	public Long obterTotalStatusProcessosEncalhe(Date data);
	
}