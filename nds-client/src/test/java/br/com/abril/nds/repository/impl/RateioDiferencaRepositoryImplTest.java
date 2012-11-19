package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SortOrder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO.ColunaOrdenacao;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.vo.PaginacaoVO;



public class RateioDiferencaRepositoryImplTest extends AbstractRepositoryImplTest{

	@Autowired
	private RateioDiferencaRepository rateioDiferencaRepository;
	
	FiltroDetalheDiferencaCotaDTO filtro = new FiltroDetalheDiferencaCotaDTO();
	
	@Before
	public void setUp(){
		
		filtro.setCodigoProduto("1a");
		filtro.setDescricaoProduto("produtoTeste");
		filtro.setIdDiferenca(1L);
		filtro.setNomeFornecedor("fornecedorTeste");
		filtro.setNumeroEdicao("55.b");
		filtro.setQuantidade(BigInteger.ONE);
		filtro.setTipoDiferenca("tipoDiferencaTeste");
	}
	
	@Test
	public void removerRateioDiferencaPorDiferenca(){
		Long idDiferenca = 1L;
		
		rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(idDiferenca);
	}
	
	@Test
	public void removerRateiosNaoAssociadosDiferenca(){
		List<Long> idrateios = new ArrayList<Long>();
		idrateios.add(1L);
		idrateios.add(2L);
		Long idDiferenca = 1L;
		
		rateioDiferencaRepository.removerRateiosNaoAssociadosDiferenca(idDiferenca, idrateios);
	}
	
	@Test
	public void obterRateioDiferencaCota(){
				
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaPosicaoInicial(){
		
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setPaginaAtual(1);
		
				
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoBox(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.BOX);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoCota(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.COTA);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoData(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.DATA);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoExemplares(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.EXEMPLARES);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoNome(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.NOME);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoPrecoDesconto(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.PRECO_DESCONTO);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoTotal(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.TOTAL);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoTotalAprovadas(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Asc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.TOTAL_APROVADAS);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterRateioDiferencaCotaOrdenacaoTotalRejeitadas(){
				
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortOrder("Desc");
		filtro.setColunaOrdenacao(ColunaOrdenacao.TOTAL_REJEITADAS);
		
		List<RateioDiferencaCotaDTO> rateioDiferencaCotaDTOs = 
				rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
		
		Assert.assertNotNull(rateioDiferencaCotaDTOs);
	}
	
	@Test
	public void obterDetalhesDiferencaCota(){
				
		DetalheDiferencaCotaDTO detalheDiferencaCotaDTO = rateioDiferencaRepository.obterDetalhesDiferencaCota(filtro);
	}
	
}
