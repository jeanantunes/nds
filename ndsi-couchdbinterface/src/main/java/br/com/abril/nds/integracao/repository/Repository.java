package br.com.abril.nds.integracao.repository;

import java.io.Serializable;
import java.util.List;

/**
 * Interface com operações básicas de repositório
 * 
 * @author francisco.garcia
 *
 * @param <T> tipo em manipulação pelo repositório 
 * @param <K> tipo do identificador do repositório
 */
public interface Repository<T, K extends Serializable> {
	
	K adicionar(T entity);
	
	void remover(T entity);
	
	T buscarPorId(K id);
	
	List<T> buscarTodos();
	
	void alterar(T entity);
	
	T merge(T entity);

	public abstract void removerPorId(K... id);

	void flush();

	public abstract void detach(T entity);
}
