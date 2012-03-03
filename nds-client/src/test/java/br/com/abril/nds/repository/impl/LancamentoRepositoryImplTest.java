package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO.ColunaOrdenacao;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO;

public class LancamentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private LancamentoRepositoryImpl lancamentoRepository;
	
	private Lancamento lancamentoVeja;
	private Lancamento lancamentoQuatroRodas;
	private Lancamento lancamentoInfoExame;
	private Lancamento lancamentoCapricho;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;

	@Before
	public void setUp() {
		fornecedorFC = Fixture.fornecedorFC();
		fornecedorDinap = Fixture.fornecedorDinap();
		save(fornecedorFC, fornecedorDinap);

		TipoProduto tipoRevista = Fixture.tipoRevista();
		save(tipoRevista);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorFC);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorFC);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorFC);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorFC);
		save(veja, quatroRodas, infoExame, capricho);

		ProdutoEdicao veja1 = Fixture.produtoEdicao(1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), veja, Fixture.fornecedorDinap());

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN,
				quatroRodas, Fixture.fornecedorDinap());

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), infoExame, Fixture.fornecedorDinap());

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, capricho, Fixture.fornecedorDinap());
		save(veja1, quatroRoda2, infoExame3, capricho1);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		NotaFiscalFornecedor notaFiscal1Veja = Fixture
				.notaFiscalFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario);
		save(notaFiscal1Veja);

		ItemNotaFiscal itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal1Veja, Fixture.criarData(22, Calendar.FEBRUARY,
						2012), new BigDecimal(50));
		save(itemNotaFiscal1Veja);
		
		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, new BigDecimal(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalFornecedor notaFiscal2Veja = Fixture
				.notaFiscalFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario);
		save(notaFiscal2Veja);

		ItemNotaFiscal itemNotaFiscal2Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal2Veja, Fixture.criarData(22, Calendar.FEBRUARY,
						2012), new BigDecimal(50));
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, new BigDecimal(50));
		save(itemRecebimentoFisico2Veja);
		
		
		NotaFiscalFornecedor notaFiscal4Rodas= Fixture
				.notaFiscalFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario);
		save(notaFiscal4Rodas);

		ItemNotaFiscal itemNotaFiscal4Rodas = Fixture.itemNotaFiscal(quatroRoda2, usuario,
				notaFiscal4Rodas, Fixture.criarData(22, Calendar.FEBRUARY,
						2012), new BigDecimal(25));
		save(itemNotaFiscal4Rodas);
		
		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);
			
		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, new BigDecimal(25));
		save(itemRecebimentoFisico4Rodas);
		
		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.RECEBIDO, itemRecebimentoFisico1Veja);
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				new BigDecimal(25),
				StatusLancamento.RECEBIDO, itemRecebimentoFisico4Rodas);
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012), 
				new Date(),
				new Date(),
				new BigDecimal(40),
				StatusLancamento.RECEBIDO, null);
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho1,
				Fixture.criarData(27, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				BigDecimal.TEN,
				StatusLancamento.RECEBIDO, null);
		
		Estudo estudo = Fixture.estudo(new BigDecimal(100), Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame, lancamentoCapricho, estudo);
		
		getSession().flush();
	}

	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByCodigoProduto() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.CODIGO_PRODUTO.getNomeColuna());
		
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());
		
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}

	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByNomeProduto() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.NOME_PRODUTO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByNumeroEdicao() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.NUMERO_EDICAO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByPreco() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.PRECO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByPacotePadrao() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.PACOTE_PADRAO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByReparte() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.REPARTE.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByFisico() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.FISICO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByEstudoGerado() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.ESTUDO_GERADO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByLancamento() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.LANCAMENTO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByRecolhimento() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.RECOLHIMENTO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByNomeFornecedor() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.FORNECEDOR.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByDataLancDistribuidor() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.DATA_LANC_DISTRIB.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByDataLancPrevisto() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.DATA_LANC_PREVISTO.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizOrderByTotal() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				ColunaOrdenacao.TOTAL.getNomeColuna());

		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(3, lancamentos.size());

		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(2).getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosData() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(23, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorFC.getId()), paginacao,
				"nomeProduto");
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosFornecedor() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				Collections.singletonList(fornecedorDinap.getId()), paginacao,
				"nomeProduto");
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}
	
	@Test
	public void totalLancamentosBalanceamentoMatriz() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		Long total = lancamentoRepository.totalBalanceamentoMatrizLancamentos(
				data, Collections.singletonList(fornecedorFC.getId()));
		Assert.assertNotNull(total);
		Assert.assertEquals(Long.valueOf(3), total);
	}
	
	@Test
	public void totalLancamentosBalanceamentoMatrizNenhum() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		Long total = lancamentoRepository.totalBalanceamentoMatrizLancamentos(
				data, Collections.singletonList(fornecedorDinap.getId()));
		Assert.assertNotNull(total);
		Assert.assertEquals(Long.valueOf(0), total);
	}

}
