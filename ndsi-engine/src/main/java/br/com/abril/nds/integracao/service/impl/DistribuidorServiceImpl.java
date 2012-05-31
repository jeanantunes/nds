package br.com.abril.nds.integracao.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Distribuidor findDistribuidor() {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d ");
		sql.append("FROM Distribuidor d");

		try {

			Query query = entityManager.createQuery(sql.toString());

			return (Distribuidor) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isDistribuidor(Integer codigo) {
		if (findDistribuidor().getCodigo().equals(codigo))
			return true;
		return false;
	}

}
