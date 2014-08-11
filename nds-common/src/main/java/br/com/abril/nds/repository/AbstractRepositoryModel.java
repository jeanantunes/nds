package br.com.abril.nds.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepositoryModel.class);
	
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
	
	
	public void alterarPorId(K id, Map<String, String> campos) {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append("UPDATE ");
		hql.append(clazz.getCanonicalName());
		hql.append(" SET ");
		
		int size = campos.size();
		int counter = 0;
        for (Entry<String, String> entry : campos.entrySet()) {
				
            hql.append(entry.getKey());
			hql.append("=");
			
            if (entry.getValue() == null) {
				hql.append("NULL");
			} else {
				hql.append("'");
                hql.append(entry.getValue());
				hql.append("'");
				
			}
			
			hql.append( ++counter == size ? "" : ", "   );
			
		}
		
		hql.append(" WHERE id = :id");
		
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("id", id);		
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
			LOGGER.error("\n\nFaltando @Transaction: "+ InformacoesTransaction.getInfo() +"\n\n", e);
		}
		
		//return null;
		return sessionFactory.openSession();
	}
	
	protected void setParameters(Query query, Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			
			if(entry.getValue() instanceof List){
				query.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
			}else{
				query.setParameter(entry.getKey(), entry.getValue());
			}
			
		}
	}
	
}
