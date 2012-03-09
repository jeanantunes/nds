package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class ExpedicaoResumoBoxRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	private Date dataLancamento = Fixture.criarData(23, Calendar.FEBRUARY, 2012);
	
	@Before
	public void setup() {
		
		//TODO implementar os teste unitarios no desenvolvimento do resumo por box
	}

	@SuppressWarnings("unused")
	@Test
	public void cosnultarResumoExpedicaoPorBox(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		List<ExpedicaoDTO> lista = expedicaoRepository.obterResumoExpedicaoPorBox(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void cosnultarQuantidadeResumoExpedicaoPorBox(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));

		Long quantidade =  expedicaoRepository.obterQuantidadeResumoExpedicaoPorBox(filtro);
		
	}
	
	/**
	 * Retorna um objeto com  valores de paginação.
	 * @param paginaAtual
	 * @param resultadoPorPagina
	 * @param ordenacao
	 * @return PaginacaoVO
	 */
	private PaginacaoVO getPaginacaoVO(int paginaAtual, int resultadoPorPagina, Ordenacao ordenacao){
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(ordenacao);
		paginacao.setPaginaAtual(paginaAtual);
		paginacao.setQtdResultadosPorPagina(resultadoPorPagina);
		
		return paginacao;
	}
	
}
