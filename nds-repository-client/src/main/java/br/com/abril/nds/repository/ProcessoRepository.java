package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Processo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Box }  
 * 
 * @author Discover Technology
 */
public interface ProcessoRepository extends Repository<Processo,Long> {
	
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