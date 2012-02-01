package br.com.abril.nds.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRepositoryImpl {

	@Autowired
	protected SessionFactory sf;

	public Session getCurrentSession() {
		return sf.getCurrentSession();
	}
	
}
