package br.com.abril.nds.controllers.distribuicao.test;

import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;

public class HistogramaPosEstudoFaixaReparteTest {

	public static void main(String[] args) {
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/main/resources/applicationContext-local.xml");
		
		EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService = applicationContext.getBean(EstudoProdutoEdicaoBaseService.class);
		
//		HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService = applicationContext.getBean(HistogramaPosEstudoFaixaReparteService.class);
//	
//		BaseEstudoAnaliseFaixaReparteDTO histogramaPosEstudo = histogramaPosEstudoFaixaReparteService.obterHistogramaPosEstudo(0, 100, new BigInteger("80222").intValue());
		
		List<EdicaoBaseEstudoDTO> listEdicaoBase = estudoProdutoEdicaoBaseService.obterEdicoesBase(81075l);
		
		System.out.println(listEdicaoBase);

	}
	
}
