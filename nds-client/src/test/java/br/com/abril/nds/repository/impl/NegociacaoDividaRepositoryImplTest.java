package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO.Ordenacao;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO.OrdenacaoColuna;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;


public class NegociacaoDividaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	NegociacaoDividaRepository negociacaoDividaRepository;
	
		
	@Test
	public void obterCotaPorNumeroLancamentoTrue(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setLancamento(true);
				
		List<NegociacaoDividaDTO> negociacaoDividaDTOs = negociacaoDividaRepository.obterCotaPorNumero(filtro);
		
		Assert.assertNotNull(negociacaoDividaDTOs);		
	}
	
	@Test
	public void obterCotaPorNumeroNumeroCota(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setNumeroCota(1);
						
		List<NegociacaoDividaDTO> negociacaoDividaDTOs = negociacaoDividaRepository.obterCotaPorNumero(filtro);
		
		Assert.assertNotNull(negociacaoDividaDTOs);		
	}
	
	@Test
	public void obterCotaPorNumeroPaginacaoSortColumn(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortColumn("dtEmissao");
		filtro.getPaginacaoVO().setPaginaAtual(1);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(2);
						
		List<NegociacaoDividaDTO> negociacaoDividaDTOs = negociacaoDividaRepository.obterCotaPorNumero(filtro);
		
		Assert.assertNotNull(negociacaoDividaDTOs);		
	}
	
	@Test
	public void obterCotaPorNumeroPaginacaoOrdenacao(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortColumn("dtEmissao");
		filtro.getPaginacaoVO().setOrdenacao(br.com.abril.nds.vo.PaginacaoVO.Ordenacao.ASC);
		filtro.getPaginacaoVO().setPaginaAtual(1);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(2);
						
		List<NegociacaoDividaDTO> negociacaoDividaDTOs = negociacaoDividaRepository.obterCotaPorNumero(filtro);
		
		Assert.assertNotNull(negociacaoDividaDTOs);		
	}
	
	@Test
	public void obterCotaPorNumeroCounLancamento(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setLancamento(false);
								
		negociacaoDividaRepository.obterCotaPorNumeroCount(filtro);
	
	}
	
	@Test
	public void obterCotaPorNumeroCount(){
		FiltroConsultaNegociacaoDivida filtro = new FiltroConsultaNegociacaoDivida();
		filtro.setLancamento(true);
								
		negociacaoDividaRepository.obterCotaPorNumeroCount(filtro);
	
	}
	
	@Test
	public void obterNegociacaoPorCobranca(){
		Long id = 1L;
								
		Negociacao negociacao = negociacaoDividaRepository.obterNegociacaoPorCobranca(id);
	
	}
	

	@Test
	public void obterNegociacaoPorComissaoCota(){
		Long idCota = 1L;
								
		List<Negociacao> negociacao = negociacaoDividaRepository.obterNegociacaoPorComissaoCota(idCota);
		
		Assert.assertNotNull(negociacao);
	
	}
	
	@Test
	public void obterQuantidadeNegociacaoFollowup(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
										
		negociacaoDividaRepository.obterQuantidadeNegociacaoFollowup(filtro);
	
	}
	
	@Test
	public void obterNegociacaoFollowup(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacao(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(2);
		filtro.getPaginacao().setPaginaAtual(1);
				
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoSortColumn(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoDataVencimento(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.DATA_VENCIMENTO);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoFormaPagamento(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.FORMA_PAGAMENTO);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoNegociacao(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NEGOCIACAO);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoNomeCota(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_COTA);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}

	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoNumeroCota(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_COTA);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterNegociacaoFollowupPaginacaoColunaOrdenacaoNumeroCotaOrdenacao(){
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("");
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_COTA);
		filtro.getPaginacao().setOrdenacao(br.com.abril.nds.vo.PaginacaoVO.Ordenacao.ASC);
			
										
		List<ConsultaFollowupNegociacaoDTO> consultaFollowupNegociacaoDTOs = negociacaoDividaRepository.obterNegociacaoFollowup(filtro);
		
		Assert.assertNotNull(consultaFollowupNegociacaoDTOs);
	}
	
	@Test
	public void obterIdCobrancaPor(){
		Long idNegociacao = 1L;
		
		negociacaoDividaRepository.obterIdCobrancaPor(idNegociacao);
	}

}
