package br.com.abril.nds.server.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractRepositoryImplTest {
	
	@Autowired
	private SessionFactory sf;

	protected void flushClear() {
		getSession().flush();
		getSession().clear();
	}
	
	private Session getSession() {
		return sf.getCurrentSession();
	}
	
	protected void save(Object... entidades) {
		for (Object entidade : entidades) {
			getSession().saveOrUpdate(entidade);
		}
		getSession().flush();
		getSession().clear();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Object> T merge(T entidade) {
		
		entidade = (T) getSession().merge(entidade);
		
		getSession().flush();
		getSession().clear();
		
		return entidade;
	}
	
	protected void update(Object... entidades) {
		for (Object entidade : entidades) {
			getSession().update(entidade);
		}
		getSession().flush();
		getSession().clear();
	}
	
}
