package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class BoxRepositoryImplTest extends AbstractRepositoryImplTest {
	
	
	@Autowired
	private BoxRepository boxRepository;
	
	private static final String[] campos = {"codigo", "tipoBox","nome","postoAvancado"};
	
	@Before
	public void setUp() throws Exception {
		
		for (int i = 0; i < 100; i++) {
			save(Fixture.criarBox("Box-"+i, "BX-"+i, TipoBox.LANCAMENTO));
		}
		
		
		
		
	}

	@Test
	public void testBusca() {
		List<Box> boxs =  boxRepository.busca(null, null, true, null, null, 0, 10);		
		Assert.assertTrue("Encontrado postos avançados", boxs.isEmpty());
		
		
		boxs =  boxRepository.busca(null, null, false, null, null, 1, 10);	
		
		Assert.assertTrue("Tamanho da pagina errado", boxs.size() == 10);
		
		
		for(String campo:campos){
			 boxRepository.busca(null, null, false, campo, Ordenacao.ASC, 0, 10);
			 boxRepository.busca(null, null, false, campo, Ordenacao.DESC, 0, 10);
			 boxRepository.busca(null, null, false, campo, null, 0, 10);
		}
		
		boxs = boxRepository.busca("Box-1", TipoBox.LANCAMENTO, false, null, null, 0, 10);
		Assert.assertTrue("Encontrado mais de um box", boxs.size() == 1);
	}

	@Test
	public void testQuantidade() {
		long quantidade = boxRepository.quantidade(null, TipoBox.LANCAMENTO, false);
		
		Assert.assertEquals(quantidade, 100l);
		
	}

}
