package br.com.abril.nds.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
	
	void saveOrUpdate(T entity);
	
	T buscarPorId(K id);
	
	List<T> buscarTodos();
	
	void alterarPorId(K id, Map<String, String> campos);
	
	void alterar(T entity);
	
	T merge(T entity);

	@SuppressWarnings("unchecked")
	public abstract void removerPorId(K... id);

	void flush();

	public abstract void detach(T entity);
	
	public abstract void clear();

}
