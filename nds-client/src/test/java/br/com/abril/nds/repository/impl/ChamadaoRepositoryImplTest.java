package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO.OrdenacaoColunaChamadao;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ChamadaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ChamadaoRepository chamadaoRepository;

	private static BigInteger qtdEstoqueVeja1;

	private static BigInteger qtdChamadaEncalheVeja2;

	private Fornecedor fornecedor;

	@Before
	public void setup() {

		TipoFornecedor fornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		save(fornecedorPublicacao);

		fornecedor = Fixture.fornecedorFC(fornecedorPublicacao);
		save(fornecedor);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);

		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setFornecedores(fornecedores);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10,
				14, new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPQ", 1L, produtoVeja, null, false);

		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao("2", 2L, 10,
				14, new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPQ", 1L, produtoVeja, null, false);

		save(produtoEdicaoVeja1, produtoEdicaoVeja2);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);

		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,
				box1);
		save(cotaManoel);

		EstoqueProdutoCota estoqueProdutoCotaVeja1 = Fixture
				.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel,
						BigInteger.TEN, BigInteger.ONE);
		save(estoqueProdutoCotaVeja1);

		qtdEstoqueVeja1 = estoqueProdutoCotaVeja1.getQtdeRecebida().subtract(
				estoqueProdutoCotaVeja1.getQtdeDevolvida());

		Estudo estudoVeja1 = Fixture.estudo(BigInteger.TEN, new Date(),
				produtoEdicaoVeja1);
		save(estudoVeja1);

		EstudoCota estudoCotaVeja1 = Fixture.estudoCota(BigInteger.valueOf(10),
				BigInteger.valueOf(10), estudoVeja1, cotaManoel);
		save(estudoCotaVeja1);

		Lancamento lancamentoVeja1 = Fixture.lancamentoExpedidos(
				TipoLancamento.LANCAMENTO, produtoEdicaoVeja1, new Date(),
				DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
				BigInteger.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoVeja1);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(new Date(),
				produtoEdicaoVeja2, TipoChamadaEncalhe.CHAMADAO);

		save(chamadaEncalhe);

		qtdChamadaEncalheVeja2 = BigInteger.TEN;

		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, true, cotaManoel, qtdChamadaEncalheVeja2);

		save(chamadaEncalheCota);
	}

	@Test
	public void obterConsignadoCotaChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO = this.chamadaoRepository
				.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(listaConsignadoCotaChamadaoDTO);

		Assert.assertTrue(!listaConsignadoCotaChamadaoDTO.isEmpty());
	}

	@Test
	public void obterChamadaEncalheParaChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		List<ConsignadoCotaChamadaoDTO> listaChamadaEncalheChamadaoDTO = this.chamadaoRepository
				.obterConsignadosComChamadao(filtro);

		Assert.assertNotNull(listaChamadaEncalheChamadaoDTO);

		Assert.assertTrue(!listaChamadaEncalheChamadaoDTO.isEmpty());
	}

	@Test
	public void obterTotalConsignadoCotaChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		Long qtdeTotalRegistros = this.chamadaoRepository
				.obterTotalConsignadosParaChamadao(filtro);

		Assert.assertTrue(qtdeTotalRegistros == 1);
	}

	@Test
	public void obterTotalChamadaEncalheChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		Long qtdeTotalRegistros = this.chamadaoRepository
				.obterTotalConsignadosComChamadao(filtro);

		Assert.assertTrue(qtdeTotalRegistros == 1);
	}

	@Test
	public void obterResumoConsignadoCotaChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadaoDTO = this.chamadaoRepository
				.obterResumoConsignadosParaChamadao(filtro);

		Assert.assertNotNull(resumoConsignadoCotaChamadaoDTO);

		Assert.assertTrue(resumoConsignadoCotaChamadaoDTO
				.getQtdExemplaresTotal().compareTo(qtdEstoqueVeja1) == 0);
	}

	@Test
	public void obterResumoChamadaEncalheChamadao() {

		FiltroChamadaoDTO filtro = getFiltro();

		ResumoConsignadoCotaChamadaoDTO resumoChamadaEncalheChamadaoDTO = this.chamadaoRepository
				.obterResumoConsignadosComChamadao(filtro);

		Assert.assertNotNull(resumoChamadaEncalheChamadaoDTO);

		Assert.assertTrue(resumoChamadaEncalheChamadaoDTO
				.getQtdExemplaresTotal().compareTo(qtdChamadaEncalheVeja2) == 0);
	}

	private FiltroChamadaoDTO getFiltro() {

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();

		filtro.setNumeroCota(123);

		filtro.setDataChamadao(new Date());

		filtro.setIdFornecedor(fornecedor.getId());

		filtro.setPaginacao(new PaginacaoVO(1, 15, "asc"));

		return filtro;
	}

	// TESTE SEM USO DE MASSA -----------------------------------

	@Test
	public void testarObterResumoConsignadosParaChamadao() {

		ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadaoDTO;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());

		resumoConsignadoCotaChamadaoDTO = chamadaoRepository
				.obterResumoConsignadosParaChamadao(filtro);

		Assert.assertNotNull(resumoConsignadoCotaChamadaoDTO);

	}

	@Test
	public void testarObterConsignadosParaChamadao() {

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// obterConsignadosParaChamadao() - OrdenacaoColuna
	// CODIGO_PRODUTO
	@Test
	public void testarObterConsignadosParaChamadaoCODIGOPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.CODIGO_PRODUTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// NOME_PRODUTO
	@Test
	public void testarObterConsignadosParaChamadaoNOMEPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.NOME_PRODUTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// EDICAO
	@Test
	public void testarObterConsignadosParaChamadaoEDICAO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.EDICAO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// BRINDE
	@Test
	public void testarObterConsignadosParaChamadaoBRINDE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.BRINDE);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// PRECO_VENDA
	@Test
	public void testarObterConsignadosParaChamadaoPRECOVENDA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.PRECO_VENDA);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// PRECO_DESCONTO
	@Test
	public void testarObterConsignadosParaChamadaoPRECODESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.PRECO_DESCONTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// REPARTE
	@Test
	public void testarObterConsignadosParaChamadaoREPARTE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.REPARTE);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// FORNECEDOR
	@Test
	public void testarObterConsignadosParaChamadaoFORNECEDOR() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.FORNECEDOR);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// RECOLHIMENTO
	@Test
	public void testarObterConsignadosParaChamadaoRECOLHIMENTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.RECOLHIMENTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// VALOR_TOTAL
	@Test
	public void testarObterConsignadosParaChamadaoVALORTOTAL() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.VALOR_TOTAL);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// VALOR_TOTAL_DESCONTO
	@Test
	public void testarObterConsignadosParaChamadaoVALORTOTALDESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.VALOR_TOTAL_DESCONTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	@Test
	public void testarobterConsignadosComChamado() {

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// obterConsignadosComChamadao() - OrdenacaoColuna
	// CODIGO_PRODUTO
	@Test
	public void testarObterConsignadosComChamadaoCODIGOPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.CODIGO_PRODUTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// NOME_PRODUTO
	@Test
	public void testarObterConsignadosComChamadaoNOMEPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.NOME_PRODUTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// EDICAO
	@Test
	public void testarObterConsignadosComChamadaoEDICAO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.EDICAO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// BRINDE
	@Test
	public void testarObterConsignadosComChamadaoBRINDE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.BRINDE);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// PRECO_VENDA
	@Test
	public void testarObterConsignadosComChamadaoPRECOVENDA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.PRECO_VENDA);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// PRECO_DESCONTO
	@Test
	public void testarObterConsignadosComChamadaoPRECODESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.PRECO_DESCONTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// REPARTE
	@Test
	public void testarObterConsignadosComChamadaoREPARTE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.REPARTE);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// FORNECEDOR
	@Test
	public void testarObterConsignadosComChamadaoFORNECEDOR() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.FORNECEDOR);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// RECOLHIMENTO
	@Test
	public void testarObterConsignadosComChamadaoRECOLHIMENTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.RECOLHIMENTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// VALOR_TOTAL
	@Test
	public void testarObterConsignadosComChamadaoVALORTOTAL() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.VALOR_TOTAL);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	// VALOR_TOTAL_DESCONTO
	@Test
	public void testarObterConsignadosComChamadaoVALORTOTALDESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cod.ID");

		List<ConsignadoCotaChamadaoDTO> consignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaChamadao.VALOR_TOTAL_DESCONTO);

		consignados = chamadaoRepository.obterConsignadosParaChamadao(filtro);

		Assert.assertNotNull(consignados);

	}

	@Test
	public void testarObterTotalConsignadosParaChamadao() {

		Long totalConsignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());

		totalConsignados = chamadaoRepository
				.obterTotalConsignadosParaChamadao(filtro);

		Assert.assertNotNull(totalConsignados);

	}

	// Teste gerarQueryConsignados(), 
	// gerarQueryChamadasEncalhe(),
	// aplicarParametrosParaPesquisaConsignadosCota(),
	// aplicarParametrosParaPesquisaChamadaEncalhe(), com
	// obterTotalConsignadosParaChamadao()
	// NumeroCota
	@Test
	public void testarGerarQueryConsignadosNumeroCota() {

		Long totalConsignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setNumeroCota(1);

		totalConsignados = chamadaoRepository
				.obterTotalConsignadosParaChamadao(filtro);

		Assert.assertNotNull(totalConsignados);

	}

	// IdFornecedor
	@Test
	public void testarGerarQueryConsignadosIdFornecedor() {

		Long totalConsignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setIdFornecedor(1L);

		totalConsignados = chamadaoRepository
				.obterTotalConsignadosParaChamadao(filtro);

		Assert.assertNotNull(totalConsignados);

	}

	// IdEditor
	@Test
	public void testarGerarQueryConsignadosIdEditor() {

		Long totalConsignados;
		Calendar data = Calendar.getInstance();

		FiltroChamadaoDTO filtro = new FiltroChamadaoDTO();
		filtro.setDataChamadao(data.getTime());
		filtro.setIdEditor(1L);

		totalConsignados = chamadaoRepository
				.obterTotalConsignadosParaChamadao(filtro);

		Assert.assertNotNull(totalConsignados);

	}

}
