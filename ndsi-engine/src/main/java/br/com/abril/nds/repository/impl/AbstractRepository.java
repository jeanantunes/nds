package br.com.abril.nds.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	public AbstractRepository() {
		
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}