package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/applicationContext-test.xml")
public class LancamentoServiceImplTest {

	@Autowired
	LancamentoService lancamentoService;
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	DistribuidorService distribuidorService;
	
	@Autowired
	FornecedorService fornecedorService;
	
	@Test
	public void listarDatasValidasLancamento() {
		
		StringBuilder datas = new StringBuilder("");
		for(Fornecedor f : fornecedorService.obterFornecedoresAtivos()) {
			
			Calendar c = Calendar.getInstance();
			Date data = lancamentoService.obterDataLancamentoValido(c.getTime(), f.getId());
			datas.append(f.getJuridica().getNome()).append(" - ").append(c.getTime().toString()).append(" / ").append(data.toString());
//			c.add(Calendar.DAY_OF_WEEK, 11);
			c.set(Calendar.MONTH, 3);
			c.set(Calendar.DAY_OF_MONTH, 3);
			data = lancamentoService.obterDataLancamentoValido(c.getTime(), f.getId());
			datas.append("\n").append(f.getJuridica().getNome()).append(" - ").append(c.getTime().toString()).append(" / ").append(data.toString()).append("\n");
		}
		
		List<Date> datasDisponiveis = lancamentoRepository.obterDatasLancamentoValidas();
			
		System.out.println(datas.toString());
		if(datasDisponiveis != null) {
			
			if(datasDisponiveis.size() > 5) {
				for(int i = 0; i < 5; i++) {
					System.out.println(datasDisponiveis.get(i).toString());
				}
			} else {
				for(Date d : datasDisponiveis) {
					System.out.println(d.toString());
				}
			}
		}
	}
	
}
