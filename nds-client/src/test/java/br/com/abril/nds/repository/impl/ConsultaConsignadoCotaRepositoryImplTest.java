package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.internal.core.search.matching.QualifiedTypeDeclarationPattern;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO.ColunaOrdenacaoConsultaConsignadoCota;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class ConsultaConsignadoCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ConsultaConsignadoCotaRepository consignadoCotaRepository;
	
	private FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO;
	
	private Fornecedor fornecedorAcme;
	private Cota cotaManoelCunha;
	
	@Before
	public void setUp(){
		
		setUpBuscarMovimentosCotaPeloFornecedor();
		
		this.filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		this.filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
	}
	
	private void setUpBuscarMovimentosCotaPeloFornecedor() {
		
		//Fornecedor
		TipoFornecedor tipoFornecedorOutros = Fixture.tipoFornecedorOutros();
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();

		fornecedorAcme = Fixture.fornecedorAcme(tipoFornecedorOutros);
		fornecedorAcme.setCodigoInterface(123);
		fornecedorAcme.setResponsavel("João");
		fornecedorAcme.setOrigem(Origem.INTERFACE);
		fornecedorAcme.setEmailNfe("joao@email.com");
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorDinap.setCodigoInterface(9999999);
		fornecedorDinap.setResponsavel("Maria");
		fornecedorDinap.setOrigem(Origem.MANUAL);
		fornecedorDinap.setEmailNfe("maria@email.com");
		
		Fornecedor fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorFC.setCodigoInterface(Integer.MAX_VALUE);
		fornecedorFC.setResponsavel("Manoel");
		fornecedorFC.setOrigem(Origem.MANUAL);
		fornecedorFC.setEmailNfe("manoel@email.com");

		
		save(tipoFornecedorOutros, tipoFornecedorPublicacao, fornecedorAcme, fornecedorDinap, fornecedorFC);
		
		//Cota
		PessoaFisica manoelCunha = Fixture.pessoaFisica("45300458970",
				"sys.discover@gmail.com", "Manoel da Cunha");
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		cotaManoelCunha = Fixture.cota(1232, manoelCunha, SituacaoCadastro.ATIVO,box1);

		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedorAcme);
		fornecedores.add(fornecedorDinap);
		cotaManoelCunha.setFornecedores(fornecedores);

		Banco bancoHSBC = Fixture.banco(10L, true, 1, "1010",
				  123456L, "1", "1", "Instrucoes HSBC.", "HSBC","BANCO HSBC S/A", "399", BigDecimal.ONE, BigDecimal.ZERO);
		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
				  BigDecimal.ONE, BigDecimal.ONE, null);
		HashSet<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobrancaManoel = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaManoelCunha, 1,
				false, new BigDecimal(1000), TipoCota.CONSIGNADO);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaManoel);
		formaBoleto.setPrincipal(true);
		
		super.save(manoelCunha, box1, cotaManoelCunha, bancoHSBC, parametroCobrancaManoel, formaBoleto);
		
		//Produto
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		Editor editoraAbril = Fixture.editoraAbril();

		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorAcme);
		produtoVeja.setEditor(editoraAbril);
		produtoVeja.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);

		Produto produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		produtoCapricho.setEditor(editoraAbril);
		produtoCapricho.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		
		Produto produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
		produtoQuatroRodas.addFornecedor(fornecedorFC);
		produtoQuatroRodas.setEditor(editoraAbril);
		produtoQuatroRodas.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("COD_1", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"111", produtoVeja, null, false, "Veja 1");
		ProdutoEdicao produtoEdicaoCapricho1 = Fixture.produtoEdicao("COD_6", 1L, 9, 14,
				new Long(150), new BigDecimal(9), new BigDecimal(13.5),
				"116", produtoCapricho, null, false, "Capricho 1");
		ProdutoEdicao produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao("COD_100", 1L, 9, 14,
				new Long(150), new BigDecimal(9), new BigDecimal(13.5),
				"116", produtoQuatroRodas, null, false, "Quatro Rodas 1");
		
		super.save(ncmRevistas, tipoProdutoRevista, editoraAbril, produtoVeja, produtoCapricho, produtoQuatroRodas,
				produtoEdicaoVeja1, produtoEdicaoCapricho1, produtoEdicaoQuatroRodas1);
		
		//Movimento
		Usuario usuarioJoao = Fixture.usuarioJoao();

		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		EstoqueProdutoCota estoqueProdutoCotaCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		EstoqueProdutoCota estoqueProdutoCotaQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);


		MovimentoEstoqueCota movimentoEstoqueCota1 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);
		MovimentoEstoqueCota movimentoEstoqueCota2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.ONE, cotaManoelCunha, StatusAprovacao.PENDENTE, null);
		MovimentoEstoqueCota movimentoEstoqueCota3 = Fixture.movimentoEstoqueCota(produtoEdicaoCapricho1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);
		MovimentoEstoqueCota movimentoEstoqueCota4 = Fixture.movimentoEstoqueCota(produtoEdicaoQuatroRodas1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaQuatroRodas1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);
		
		super.save(usuarioJoao, tipoMovimentoEnvioEncalhe,
				tipoMovimentoRecReparte, estoqueProdutoCotaVeja1,
				estoqueProdutoCotaCapricho1, estoqueProdutoCotaQuatroRodas1, movimentoEstoqueCota1,
				movimentoEstoqueCota2, movimentoEstoqueCota3, movimentoEstoqueCota4);

	}
	
	@Test
	public void buscarConsignadoCota(){
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(this.filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 4;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	@Test
	public void buscarConsignadoCotaIdCota(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		filtroConsultaConsignadoCotaDTO.setIdCota(cotaManoelCunha.getId());
		
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 4;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	@Test
	public void buscarConsignadoCotaIdCotaNulo(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setIdCota(null);
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);
}
	
	
	
	@Test
	public void buscarConsignadoCotaIdFornecedor(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		filtroConsultaConsignadoCotaDTO.setIdFornecedor(fornecedorAcme.getId());
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 2;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	@Test
	public void buscarConsignadoCotaIdCotaOrderByIdCota(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		filtroConsultaConsignadoCotaDTO.getPaginacao().setSortColumn("");
		filtroConsultaConsignadoCotaDTO.setIdCota(cotaManoelCunha.getId());
		
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 4;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	
	@Test
	public void buscarConsignadoCotaOrderByIdFornecedor(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		filtroConsultaConsignadoCotaDTO.getPaginacao().setSortColumn("");
		filtroConsultaConsignadoCotaDTO.setIdFornecedor(fornecedorAcme.getId());
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 2;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	@Test
	public void buscarConsignadoCotaOrderByIdFornecedorOrdenacao(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
		filtroConsultaConsignadoCotaDTO.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtroConsultaConsignadoCotaDTO.getPaginacao().setSortColumn("");
		filtroConsultaConsignadoCotaDTO.setIdFornecedor(fornecedorAcme.getId());
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);

		int tamanhoEsperado = 2;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
	}
	
	

	
	@Test
	public void buscarConsignadoCotaPaginacaoNull(){
		
		FiltroConsultaConsignadoCotaDTO	filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
				
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);
		

	}
		
	@Test
	public void buscarConsignadoCotaPaginacaoNotNull(){
		
		FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "des");
		filtroConsultaConsignadoCotaDTO.setPaginacao(paginacao);
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);
		
	}
	
	@Test
	public void buscarConsignadoCotaPaginacaoNotNullLimitar(){
		
		FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "des");
		filtroConsultaConsignadoCotaDTO.setPaginacao(paginacao);
				
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(filtroConsultaConsignadoCotaDTO, true);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarMovimentosCotaPeloFornecedor(){
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> lista = 
				this.consignadoCotaRepository.buscarMovimentosCotaPeloFornecedor(
						this.filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);
		
		int tamanhoEsperado = 3;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
		
	}
	
	@Test
	public void buscarMovimentosCotaPeloFornecedorIdCota(){
		
		FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setIdCota(cotaManoelCunha.getId());
		filtroConsultaConsignadoCotaDTO.setPaginacao (new PaginacaoVO());
		
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> lista = 
				this.consignadoCotaRepository.buscarMovimentosCotaPeloFornecedor(
						filtroConsultaConsignadoCotaDTO, false);
		
		Assert.assertNotNull(lista);
		
				
	}
	
	@Test
	public void buscarTodasMovimentacoesPorCota(){
		
		Long totalRegistros = 
				this.consignadoCotaRepository.buscarTodasMovimentacoesPorCota(
						this.filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(totalRegistros);
		
		Long tamanhoEsperado = 3L;
		
		Assert.assertEquals(tamanhoEsperado, totalRegistros);
		
	}
	
	
	
	@Test
	public void buscarTotalGeralDaCota(){
		
		BigDecimal resultado =
				this.consignadoCotaRepository.buscarTotalGeralDaCota(this.filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void buscarTotalGeralDaCotaIdCota(){
		
		FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		filtroConsultaConsignadoCotaDTO.setIdCota(cotaManoelCunha.getId());
		
		BigDecimal resultado =
				this.consignadoCotaRepository.buscarTotalGeralDaCota(filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(resultado);
	}

	
	@Test
	public void buscarTotalDetalhado(){
		
		List<TotalConsultaConsignadoCotaDetalhado> lista = 
				this.consignadoCotaRepository.buscarTotalDetalhado(this.filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(lista);
	}
	
}