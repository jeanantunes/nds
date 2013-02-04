package br.com.abril.nds.integracao.engine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

public class EntityScanner implements PersistenceUnitPostProcessor {

	private String[] targetPersistenceUnits = new String[] {};
	private String[] locationPatterns;
	private String[] packagesToScan;
	
	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	private Log logger = LogFactory.getLog(getClass());

	public String[] scanPath() {
		return scanPath(locationPatterns);
	}

	public String[] scanPath(String... locationPatterns) {
		Set<Class<?>> entities = new HashSet<Class<?>>();
		
		Reflections reflections = new Reflections(packagesToScan);
		
		// @Entity
		entities.addAll(reflections.getTypesAnnotatedWith(Entity.class));
		
		// @Embeddable
		entities.addAll(reflections.getTypesAnnotatedWith(Embeddable.class));

		// @MappedSuperclass
		entities.addAll(reflections.getTypesAnnotatedWith(MappedSuperclass.class));
		
		String[] ret = new String[entities.size()];
		
		int i = 0;
		
		for (Iterator it = entities.iterator(); it.hasNext(); i++) {
			Class<?> entityClass = (Class<?>) it.next();
			
			ret[i] = entityClass.getName();
		}
		
		return ret;
	}

	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		if (isTargetPersistenceUnit(pui)) {
			String[] entities = scanPath(locationPatterns);
			for (String entity : entities) {
				pui.addManagedClassName(entity);
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("Skipping PersistenceUnitInfo with name :"
						+ pui.getPersistenceUnitName());
			}
		}
	}

	private boolean isTargetPersistenceUnit(PersistenceUnitInfo pui) {
		return targetPersistenceUnits.length > 0 ? ArrayUtils.contains(
				targetPersistenceUnits, pui.getPersistenceUnitName()) : true;
	}
}