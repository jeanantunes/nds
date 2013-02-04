package br.com.abril.nds.integracao.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.Test;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;

public class TestJpaMock extends TestTemplate {
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void test() {
//		JPAMock jpaMock = new JPAMock(entityManagerFactory);
//		
//		jpaMock.when(NotaFiscalEntrada.class).thenInstance(NotaFiscalEntradaFornecedor.class);
//		jpaMock.when(Pessoa.class).thenInstance(PessoaJuridica.class);
		//jpaMock.mock(Produto.class);
	}

	@Test
	public void testTempTable() {
		EMS0107Input ems0107Input = entityManager.find(EMS0107Input.class, 1);
	}
}
