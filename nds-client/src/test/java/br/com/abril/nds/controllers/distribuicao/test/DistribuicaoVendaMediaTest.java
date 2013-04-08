package br.com.abril.nds.controllers.distribuicao.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import br.com.abril.nds.controllers.distribuicao.DistribuicaoVendaMediaController;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import static org.mockito.Mockito.*;


public class DistribuicaoVendaMediaTest {
	
	@Test
	public void converterResultadoTest() throws IllegalAccessException, InvocationTargetException{
		HttpSession session = mock(HttpSession.class);
		List<ProdutoEdicao> resultadoPesquisa = new ArrayList<ProdutoEdicao>();
		ProdutoEdicao pe = new ProdutoEdicao();
		resultadoPesquisa.add(pe);
		when(session.getAttribute(DistribuicaoVendaMediaController.RESULTADO_PESQUISA_PRODUTO_EDICAO)).thenReturn(resultadoPesquisa);
		when(session.getAttribute(DistribuicaoVendaMediaController.SELECIONADOS_PRODUTO_EDICAO_BASE)).thenReturn(null);
		
		DistribuicaoVendaMediaController controller = new DistribuicaoVendaMediaController();
		controller.setSession(session);
		
		List<Integer> indexes = new ArrayList<Integer>();
		indexes.add(0);
		controller.adicionarProdutoEdicaoABase(indexes);
	}
	
}
