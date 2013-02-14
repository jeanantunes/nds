package br.com.abril.nds.integracao.test;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.integracao.fileimporter.StartBatch;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations={StartBatch.SPRING_FILE_LOCATION})
public abstract class TestTemplate {
	
	@Autowired
	DataSource dataSourceIcd;
	
	@Autowired
	DataSource dataSourceNdsi;
	
}
