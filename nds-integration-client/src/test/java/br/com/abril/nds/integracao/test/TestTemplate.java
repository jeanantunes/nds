<<<<<<< HEAD
package br.com.abril.nds.integracao.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.integracao.spring.NdsiRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations={NdsiRunner.SPRING_FILE_LOCATION})
public abstract class TestTemplate extends AbstractTransactionalJUnit4SpringContextTests {
	
}
=======
package br.com.abril.nds.integracao.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.integracao.spring.NdsiRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations={NdsiRunner.SPRING_FILE_LOCATION})
public abstract class TestTemplate extends AbstractTransactionalJUnit4SpringContextTests {
	
}
>>>>>>> refs/remotes/DGBTi/fase2
