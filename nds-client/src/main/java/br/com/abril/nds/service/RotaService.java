package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Rota;

public interface RotaService {

	/**
	 * @return
	 */
	List<Rota> obterRotas();
	
	/**
	 * Retorna uma lista de rotas referente um roteiro.
	 * @param roteiroId
	 * @return
	 */
	List<Rota> buscarRotaPorRoteiro(Long roteiroId);
	
}
