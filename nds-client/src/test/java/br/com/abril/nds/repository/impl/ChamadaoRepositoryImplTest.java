package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ChamadaoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private ChamadaoRepository chamadaoRepository;
	
	private static BigDecimal qtdEstoqueVeja1;
	
	@Before
	public void setup() {
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja1 =
			Fixture.produtoEdicao("1", 1L, 10, 14, new BigDecimal(0.1),
								  BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 =
			Fixture.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel,
									   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCotaVeja1);
		
		qtdEstoqueVeja1 = 
			estoqueProdutoCotaVeja1.getQtdeRecebida().subtract(
				estoqueProdutoCotaVeja1.getQtdeDevolvida());
		
		Estudo estudoVeja1 =
			Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicaoVeja1);
		save(estudoVeja1);
		
		EstudoCota estudoCotaVeja1 =
			Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1, cotaManoel);
		save(estudoCotaVeja1);
		
		Lancamento lancamentoVeja1 =
			Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoVeja1, new Date(),
										DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
										BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoVeja1);
	}
	
	@Test
	public void obterConsignadoCotaChamadao() {
		
		FiltroChamadaoDTO filtro = getFiltro();

		List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO = 
			this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(listaConsignadoCotaChamadaoDTO);
		
		Assert.assertTrue(!listaConsignadoCotaChamadaoDTO.isEmpty());
	}
	
	@Test
	public void obterTotalConsignadoCotaChamadao() {
		
		FiltroChamadaoDTO filtro = getFiltro();
		
		Long qtdeTotalRegistros =
			this.chamadaoRepository.obterTotalConsignadosParaChamadao(filtro);
		
		Assert.assertTrue(qtdeTotalRegistros == 1);
	}
	
	@Test
	public void obterResumoConsignadoCotaChamadao() {
		
		FiltroChamadaoDTO filtro = getFiltro();

		ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadaoDTO = 
			this.chamadaoRepository.obterResumoConsignadosParaChamadao(filtro);
			
		Assert.assertNotNull(resumoConsignadoCotaChamadaoDTO);
		
		Assert.assertTrue(
			resumoConsignadoCotaChamadaoDTO.getQtdExemplaresTotal().compareTo(qtdEstoqueVeja1) == 0);
	}
	
	private FiltroChamadaoDTO getFiltro() {

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		
		filtro.setNumeroCota(123);
		
		filtro.setDataChamadao(new Date());
		
		filtro.setPaginacao(new PaginacaoVO(1, 15, "asc"));

		return filtro;
	}
}
