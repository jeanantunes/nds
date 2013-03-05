package br.com.abril.nds.controllers.distribuicao.teste;

import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.service.RankingFaturamentoService;

public class RankingFaturamentoModelTest {

		public static void main(String[] args) {
			FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/main/resources/applicationContext-local.xml");
			
			RankingFaturamentoService rankingFaturamentoService = applicationContext.getBean(RankingFaturamentoService.class);
			
			List<RankingFaturamento> ranks = rankingFaturamentoService.buscarTodos();
			
			System.out.println(ranks);
		

		}
	
}
