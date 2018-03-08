package br.com.abril.nds.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Implementação das operações básicas do repositório
 * 
 * @author francisco.garcia
 *
 * @param <T> tipo em manipulação pelo repositório 
 * @param <K> tipo do identificador do repositório
 */
public abstract class AbstractRepository extends CouchDBRepositoryImpl {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			
		}
		return null;
		//return sessionFactory.openSession();
	}

	/**
	 * Recupera distribuidores a serem processados.
	 */
	protected List<String> getDistribuidores(String diretorio, Long codigoDistribuidor) {

		List<String> distribuidores = new ArrayList<String>();

		if (codigoDistribuidor == null) {

			FilenameFilter numericFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.matches("\\d+");
				}
			};

			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list( numericFilter )));

		} else {

			distribuidores.add(codigoDistribuidor.toString());
		}

		return distribuidores;
	}
	
}
