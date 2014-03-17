package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Processo;

public interface ProcessoService {

	/**
	 * Obtém lista de processo
	 */
	public List<Processo> obterTodosProcessos();
	
	/**
	 * Obtém pelo nome
	 */	
	public Processo buscarPeloNome(String nome);
	
	public List<ItemDTO<String, String>> buscarProcessos(String parametros[]);

}