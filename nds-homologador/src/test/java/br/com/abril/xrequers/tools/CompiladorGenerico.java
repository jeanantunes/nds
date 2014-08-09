package br.com.abril.xrequers.tools;

import org.junit.Assert;
import org.junit.Test;

import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class CompiladorGenerico extends AbstractRepositoryTest {
/*
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}*/
	
	/**
	 * Método que realiza a compilação de uma query hibernate
	 * Este método valida se a sintaxe da consulta em hql está 
	 * correta de acordo com a estrutura das entindades mapeadas 
	 * em classes Java no projeto. 
	 * 
	 * Observação: Este método NãO valida a estrutura das tabelas reais no banco de dados.
	 * 
	 * @param hqlQueryText
	 * 
	 * @return String
	 */
	public String toSql(String hqlQueryText) {
		/*
		if (hqlQueryText != null && hqlQueryText.trim().length() > 0) {
			
			final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
			
			final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
			
			final QueryTranslator translator = translatorFactory.createQueryTranslator(hqlQueryText, hqlQueryText,
							Collections.EMPTY_MAP, factory, null);
			
			translator.compile(Collections.EMPTY_MAP, false);
			
			return translator.getSQLString();
		}*/
		return null;
	}
	
	@Test
	public void testQueryGenerica() {
		
		String sql = toSql(" select c.diaRecolhimento from ConferenciaEncalhe c ");
		
		Assert.assertNotNull(sql);
		
		
	}

}
