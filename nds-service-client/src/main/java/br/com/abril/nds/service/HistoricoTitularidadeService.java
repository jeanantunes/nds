package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;

/**
 * Interface que define regras para o componente de geração de Histórico de Titularidade da cota.
 * 
 * @author Discover Technology
 *
 */
public interface HistoricoTitularidadeService {

	/**
	 * Método capaz de gerar um histórico de titularidade a partir de uma determinada cota.
	 * 
	 * @param cota - Cota utilizada para a geração do histórico.
	 * 
	 * @return {@link br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota}
	 */
	public HistoricoTitularidadeCota gerarHistoricoTitularidadeCota(Cota cota);
}
