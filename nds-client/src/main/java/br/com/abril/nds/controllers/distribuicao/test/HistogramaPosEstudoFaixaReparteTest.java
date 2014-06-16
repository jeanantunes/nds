package br.com.abril.nds.controllers.distribuicao.test;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.service.EstudoService;

public class HistogramaPosEstudoFaixaReparteTest {

	public static void main(String[] args) {
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/main/resources/applicationContext-local.xml");

		EstudoService service = applicationContext.getBean(EstudoService.class);
		ResumoEstudoHistogramaPosAnaliseDTO dto = service.obterResumoEstudo(80222l, null, null);
		
//		EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService = applicationContext.getBean(EstudoProdutoEdicaoBaseService.class);
//		List<EdicaoBaseEstudoDTO> dto = estudoProdutoEdicaoBaseService.obterEdicoesBase(81075l);
		
//		HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService = applicationContext.getBean(HistogramaPosEstudoFaixaReparteService.class);
//		BaseEstudoAnaliseFaixaReparteDTO dto = histogramaPosEstudoFaixaReparteService.obterHistogramaPosEstudo(0, 100, new BigInteger("80222").intValue());
		
		System.out.println(dto);

	}
	
}
