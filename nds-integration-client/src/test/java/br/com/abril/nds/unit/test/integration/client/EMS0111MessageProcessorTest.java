package br.com.abril.nds.unit.test.integration.client;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ProdutoEdicaoService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/spring/applicationContext-ndsi-test.xml")
public class EMS0111MessageProcessorTest {

	@Autowired
	CalendarioService calendarioService;

	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	ProdutoEdicaoService produtoEdicaoService;

	@Test
	@Transactional
	public void testAtualizaPeb() {

		Date dataLancamento = new Date();
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao("00000552", "2414");
		
		// Cálcular data de recolhimento
		Calendar calRecolhimento = Calendar.getInstance();
		calRecolhimento.setTime(dataLancamento);
		int peb = produtoEdicao.getPeb() == 0 ? produtoEdicao.getProduto().getPeb() : produtoEdicao.getPeb();
		if (peb <= 0) {
			peb = 15;
		}
		calRecolhimento.add(Calendar.DAY_OF_MONTH, peb);
		Date dataInicial = calRecolhimento.getTime();

		while(!calendarioService.isDiaOperante(calRecolhimento.getTime(), produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO)) {

			calRecolhimento.add(Calendar.DAY_OF_MONTH, 1);
		};

		System.out.println(dataInicial.toString());
		System.out.println(calRecolhimento.getTime().toString());
	}
}