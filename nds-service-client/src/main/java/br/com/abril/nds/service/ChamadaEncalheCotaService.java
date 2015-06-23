package br.com.abril.nds.service;

import java.util.Date;
import java.util.Set;

import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;



/**
 * Interface de serviços referentes a Chamadas de Encalhe da Cota. 
 *   
 */
public interface ChamadaEncalheCotaService {
	
	ChamadaEncalheCota obterChamadaEncalheCota(long cotaId, long produtoEdicaoId, Date dataRecolhimentoDistribuidor);
	Set<Lancamento> obterLancamentos(ChamadaEncalhe chamadaEncalhe);
}