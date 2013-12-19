package br.com.abril.nds.nfe;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroNFe;
import br.com.abril.nds.vo.PaginacaoVO;

public class BuilderFaturaTest {
	
	@Autowired
	private NotaFiscalFaturaNdsServiceTest notaFiscalFaturaNds;
	
	@Test
	public void pesquisarNotaFiscal() {
		
		FiltroNFe filtro = obterFiltroNFe();
		
		notaFiscalFaturaNds.obterDadoFatura(filtro);
		
		Assert.assertNotNull(null);
		
	}
	
	private FiltroNFe obterFiltroNFe() {
		
		FiltroNFe filtro = new FiltroNFe();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);
		
		return filtro;
		
	}
}
