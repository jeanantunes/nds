package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class PeriodoLancamentoParcialRepositoryImplTest extends AbstractRepositoryImplTest {

	private ProdutoEdicao produtoEdicaoVeja1;
	private Fornecedor fornecedorFC;
	private Produto produtoVeja;
	private Lancamento lancamento;
	private Lancamento lancamento2;
	private LancamentoParcial lancamentoParcial;
	private PeriodoLancamentoParcial periodo;
	private PeriodoLancamentoParcial periodo2;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Before
	public void setup() {
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		save(fornecedorFC);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorFC);
		save(produtoVeja);
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 10,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L,
				produtoVeja, null, false);
		
		produtoEdicaoVeja1.setParcial(true);
		save(produtoEdicaoVeja1);
		
		
		Date dtInicial = Fixture.criarData(1, 1, 2010);
		Date dtFinal = Fixture.criarData(1, 1, 2012);
		
		lancamentoParcial = Fixture.criarLancamentoParcial(produtoEdicaoVeja1, dtInicial, dtFinal,StatusLancamentoParcial.PROJETADO);
		save(lancamentoParcial);
		
		
		lancamento = Fixture.lancamento(TipoLancamento.PARCIAL, produtoEdicaoVeja1,
				Fixture.criarData(1,1, 2010),
				Fixture.criarData(1,2,2010),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);
		save(lancamento);
		
		lancamento2 = Fixture.lancamento(TipoLancamento.PARCIAL, produtoEdicaoVeja1,
				Fixture.criarData(1,3, 2010),
				Fixture.criarData(1,4,2010),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 2);
		save(lancamento2);
		
		
		Estudo estudo = Fixture.estudo(BigInteger.valueOf(200),Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoVeja1);
		save(estudo);
		
		periodo = Fixture.criarPeriodoLancamentoParcial(lancamento, lancamentoParcial, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);
		
		periodo2 = Fixture.criarPeriodoLancamentoParcial(lancamento2, lancamentoParcial, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);
		
		save(periodo, periodo2);
	}
		
	@Test
	public void obterPeriodosParciais() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacaoPeriodo.DATA_LANCAMENTO.toString()));
		
		
		List<PeriodoParcialDTO> periodos = periodoLancamentoParcialRepository.obterPeriodosParciais(filtro);
		Assert.assertEquals(periodos.size(), 2);	
	}
	
	@Test
	public void totalBuscaLancamentosParciais() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
				
		Integer total = periodoLancamentoParcialRepository.totalObterPeriodosParciais(filtro);
		
		Assert.assertTrue(total == 2);
		
	}
	
	@Test
	public void obterPeriodoPorIdLancamento() {
		PeriodoLancamentoParcial periodoL = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(lancamento.getId());
		
		Assert.assertEquals(periodoL.getId(), periodo.getId());
	}
	
	@Test
	public void verificarValidadeNovoPeriodoParcialValido() {
		
		Date novaDataLancamento = Fixture.criarData(1,3,2011);
		Date novaDataRecolhimento = Fixture.criarData(1,4,2011);
		
		boolean possivel = periodoLancamentoParcialRepository.verificarValidadeNovoPeriodoParcial(lancamento.getId(), novaDataLancamento, novaDataRecolhimento);
		
		Assert.assertTrue(possivel);
		

		novaDataLancamento = Fixture.criarData(1,1,2010);
		novaDataRecolhimento = Fixture.criarData(1,2,2010);
		
		possivel = periodoLancamentoParcialRepository.verificarValidadeNovoPeriodoParcial(lancamento.getId(), novaDataLancamento, novaDataRecolhimento);
		
		Assert.assertTrue(possivel);
		
		novaDataLancamento = Fixture.criarData(1,1,2010);
		novaDataRecolhimento = Fixture.criarData(1,1,2011);
		
		possivel = periodoLancamentoParcialRepository.verificarValidadeNovoPeriodoParcial(lancamento.getId(), novaDataLancamento, novaDataRecolhimento);
		
		Assert.assertFalse(possivel);
	}
	
}
