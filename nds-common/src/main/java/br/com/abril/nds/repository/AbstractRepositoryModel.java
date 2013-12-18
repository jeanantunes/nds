package br.com.abril.nds.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Implementação das operações básicas do repositório
 * 
 * @author francisco.garcia
 *
 * @param <T> tipo em manipulação pelo repositório 
 * @param <K> tipo do identificador do repositório
 */
public abstract class AbstractRepositoryModel<T, K extends Serializable> extends AbstractRepository implements Repository<T, K> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> clazz;
	
	public AbstractRepositoryModel(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public K adicionar(T entity) {
		return (K) getSession().save(entity);		
	}
	
	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);		
	}
	
	public void remover(T entity) {
		getSession().delete(entity);
		getSession().flush();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void removerPorId(K... id) {		
		String hql  = "DELETE FROM "+ clazz.getCanonicalName() + " WHERE id in (:ids)" ;
		
		Query query = getSession().createQuery(hql);
		query.setParameterList("ids", id);		
		query.executeUpdate();
		getSession().flush();
	}
	
	@Transactional
	public void alterar(T entity) {
		getSession().update(entity);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public T merge(T entity) {
		return (T) getSession().merge(entity);
	}
	
	@SuppressWarnings("unchecked")
	public T buscarPorId(K id) {
		return (T) getSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> buscarTodos() {
		
		return getSession().createCriteria(this.clazz).list();
	}

	public void flush() {
		this.getSession().flush();
	}
	
	@Override
	public void detach(T entity){
		this.getSession().evict(entity);
	}
	
	@Override
	public void clear(){
		this.getSession().clear();
	}
	
	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			
		}
		
		return sessionFactory.openSession();
	}
}
