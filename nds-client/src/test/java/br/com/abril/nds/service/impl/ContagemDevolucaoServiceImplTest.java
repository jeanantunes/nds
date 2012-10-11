package br.com.abril.nds.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ContagemDevolucaoServiceImplTest  {
    
    @Autowired
    private ContagemDevolucaoServiceImpl service;
    
    @Autowired
    private SessionFactory sf;

    private void flushClear() {
        getSession().flush();
        getSession().clear();
    }
    
    private Session getSession() {
        return sf.getCurrentSession();
    }
    
    private void save(Object... entidades) {
        for (Object entidade : entidades) {
            getSession().saveOrUpdate(entidade);
        }
        flushClear();
    }
  
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    
    @Test
    public void gerarCEDevolucao() {
        service.gerarCEDevolucao(null);
    }

}
