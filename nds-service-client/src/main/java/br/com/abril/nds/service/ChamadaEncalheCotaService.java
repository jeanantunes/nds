package br.com.abril.nds.service;

import java.util.Date;

import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;



/**
 * Interface de serviços referentes a Chamadas de Encalhe da Cota. 
 *   
 */
public interface ChamadaEncalheCotaService {
	
	ChamadaEncalheCota obterChamadaEncalheCota(long cotaId, long produtoEdicaoId, Date dataRecolhimentoDistribuidor);
	
}