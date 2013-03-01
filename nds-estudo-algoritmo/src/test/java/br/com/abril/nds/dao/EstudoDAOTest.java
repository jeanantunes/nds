package br.com.abril.nds.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class EstudoDAOTest {

    @Autowired
    private EstudoDAO estudoDAO;
    
    @Test
    public void testGravarCotas() {
	
    }
}
