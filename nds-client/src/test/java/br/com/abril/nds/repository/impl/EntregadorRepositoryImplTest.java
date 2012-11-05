package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoPaginacaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO.OrdenacaoColunaEntregador;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class EntregadorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EntregadorRepository entregadorRepository;
	private PessoaFisica maria;
	private PessoaJuridica acme;
	private PessoaJuridica fc;
	private PessoaFisica jose;

	private Entregador entregadorAcme;
	private Entregador entregadorFC;
	private Entregador entregadorJose;
	private Entregador entregadorMaria;

	private static Cota cota;

	private static final Integer NUMERO_COTA = 1;

	@Before
	public void setup() {

		Editor abril = Fixture.editoraAbril();
		save(abril);

		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("FC",
				"01.001.001/001-00", "000.000.000.00", "fc@mail.com",
				"99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39",
				"joao@gmail.com", "João da Silva");
		save(pessoaFisica);

		Box box = Fixture.boxReparte300();
		save(box);

		cota = Fixture.cota(NUMERO_COTA, pessoaFisica, SituacaoCadastro.ATIVO,
				box);
		cota.setSugereSuspensao(true);
		save(cota);

		acme = Fixture.juridicaAcme();

		entregadorAcme = Fixture.criarEntregador(234L, true, new Date(),
				BigDecimal.TEN, acme, false, null);

		save(acme, entregadorAcme);

		fc = Fixture.juridicaFC();

		entregadorFC = Fixture.criarEntregador(123L, false, new Date(), null,
				fc, false, null);
		save(fc, entregadorFC);

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA,
				"13131313", "Rua Marechal deodoro", "50", "Centro", "Mococa",
				"SP", 1);

		EnderecoEntregador enderecoEntregador = Fixture.enderecoEntregador(
				entregadorFC, endereco, false, TipoEndereco.COMERCIAL);

		Telefone telefone = Fixture.telefone("19", "36560000", null);

		TelefoneEntregador telefoneEntregador = Fixture.telefoneEntregador(
				entregadorFC, false, telefone, TipoTelefone.COMERCIAL);

		save(endereco, enderecoEntregador, telefone, telefoneEntregador);

		jose = Fixture.pessoaFisica("3001258790", "jose@algumacoisa.com",
				"José");

		jose.setApelido("Zezinho");

		entregadorJose = Fixture.criarEntregador(345L, false, new Date(), null,
				jose, false, null);
		save(jose, entregadorJose);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650",
				"Avenida Brasil", "10", "Centro", "Ribeirão Preto", "SP", 2);

		enderecoEntregador = Fixture.enderecoEntregador(entregadorJose,
				endereco, false, TipoEndereco.COBRANCA);

		telefone = Fixture.telefone("19", "36112887", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregadorJose, false,
				telefone, TipoTelefone.CELULAR);

		save(endereco, enderecoEntregador, telefone, telefoneEntregador);

		maria = Fixture.pessoaFisica("4001258790", "maria@outracoisa.com",
				"Maria");

		maria.setApelido("Mariazinha");

		save(maria);

		entregadorMaria = Fixture.criarEntregador(456L, false, new Date(),
				null, maria, false, null);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650",
				"Itaquera", "10", "Centro", "São Paulo", "SP", 5);

		enderecoEntregador = Fixture.enderecoEntregador(entregadorMaria,
				endereco, false, TipoEndereco.RESIDENCIAL);

		telefone = Fixture.telefone("11", "31053333", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregadorMaria, false,
				telefone, TipoTelefone.CELULAR);

		save(entregadorMaria, endereco, enderecoEntregador, telefone,
				telefoneEntregador);
	}

	@Test
	public void obterEntregadoresSemFiltroSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CODIGO);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		int listSizeExpected = 4;
		int listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);
	}

	@Test
	public void obterEntregadoresComPaginacaoSemFiltroAscOrderSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(Ordenacao.ASC);

		paginacao.setPaginaAtual(1);

		paginacao.setQtdResultadosPorPagina(3);

		// ORDER BY NOME/RAZAO SOCIAL
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.NOME_RAZAO_SOCIAL);

		filtroEntregador.setPaginacao(paginacao);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		int listSizeExpected = paginacao.getQtdResultadosPorPagina();
		int listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(),
				acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
				jose.getId());

		// ORDER BY CODIGO
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CODIGO);

		filtroEntregador.setPaginacao(paginacao);

		entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(),
				acme.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
				jose.getId());

		// ORDER BY APELIDO/NOME FANTASIA
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.APELIDO_NOME_FANTASIA);

		filtroEntregador.setPaginacao(paginacao);

		entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(),
				acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
				maria.getId());

		// ORDER BY CPF/CNPJ
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CPF_CNPJ);

		filtroEntregador.setPaginacao(paginacao);

		entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(),
				acme.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
				jose.getId());

		// ORDER BY EMAIL
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.EMAIL);

		filtroEntregador.setPaginacao(paginacao);

		entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(),
				acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
				jose.getId());

		// //ORDER BY TELEFONE
		// filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.TELEFONE);
		//
		// filtroEntregador.setPaginacao(paginacao);
		//
		// entregadores =
		// this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		//
		// Assert.assertNotNull(entregadores);
		//
		// listSizeExpected = paginacao.getQtdResultadosPorPagina();
		// listSizeActual = entregadores.size();
		//
		// Assert.assertEquals(listSizeExpected, listSizeActual);
		//
		// Assert.assertEquals(entregadores.get(0).getPessoa().getId(),
		// maria.getId());
		// Assert.assertEquals(entregadores.get(1).getPessoa().getId(),
		// jose.getId());
		// Assert.assertEquals(entregadores.get(2).getPessoa().getId(),
		// fc.getId());
	}

	@Test
	public void obterEntregadoresComPaginacaoSemFiltroDescOrderSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(Ordenacao.DESC);

		paginacao.setPaginaAtual(1);

		paginacao.setQtdResultadosPorPagina(3);

		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.NOME_RAZAO_SOCIAL);

		filtroEntregador.setPaginacao(paginacao);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		int listSizeExpected = paginacao.getQtdResultadosPorPagina();
		int listSizeActual = entregadores.size();

		Assert.assertEquals(listSizeExpected, listSizeActual);

		Assert.assertEquals(entregadores.get(0).getPessoa().getId(),
				maria.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(),
				jose.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), fc.getId());
	}

	@Test
	public void obterEntregadoresPorFiltroNomeRazaoSocial() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador.setNomeRazaoSocial("NomeTeste");
		filtroEntregador.setPaginacao(new PaginacaoVO());
		filtroEntregador.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.NOME_RAZAO_SOCIAL);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

	}

	@Test
	public void obterEntregadoresPorFiltroCpfCnpj() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador.setCpfCnpj("123.123.123-12");
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CPF_CNPJ);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

	}

	@Test
	public void obterEntregadoresPorFiltroApelidoNomeFantasia() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador.setApelidoNomeFantasia("ApelidoTeste");
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.APELIDO_NOME_FANTASIA);

		List<Entregador> entregadores = this.entregadorRepository
				.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);
	}

	@Test
	public void obterContagemEntregadoresPorFiltro() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador.setApelidoNomeFantasia("ApelidoTeste");
		filtroEntregador
				.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.APELIDO_NOME_FANTASIA);

		entregadorRepository
				.obterContagemEntregadoresPorFiltro(filtroEntregador);
	}

	@Test
	public void obterEnderecosEntregadorFCSucesso() {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.entregadorRepository
				.obterEnderecosPorIdEntregador(fc.getId());

		Assert.assertNotNull(listaEnderecoAssociacao);

		}

	@Test
	public void verificarEntregador() {

		boolean indCotaPossuiEntregador = entregadorRepository
				.verificarEntregador(cota.getId());

		Assert.assertFalse(indCotaPossuiEntregador);

	}

	@Test
	public void obterEnderecosPorIdEntregador() {

		Long idEntregador = 1L;

		List<EnderecoAssociacaoDTO> enderecoAssociacaoDTOs = entregadorRepository
				.obterEnderecosPorIdEntregador(idEntregador);

		Assert.assertNotNull(enderecoAssociacaoDTOs);

	}

	@Test
	public void obterProcuracaoEntregadorPorIdEntregador() {

		Long idEntregador = 1L;

		ProcuracaoEntregador procuracaoEntregador = entregadorRepository
				.obterProcuracaoEntregadorPorIdEntregador(idEntregador);

	}

	@Test
	public void obterQuantidadeEntregadoresPorIdPessoaIdPessoa() {

		Long idPessoa = 1L;

		entregadorRepository.obterQuantidadeEntregadoresPorIdPessoa(idPessoa,
				null);

	}

	@Test
	public void obterQuantidadeEntregadoresPorIdPessoaIdEntregador() {

		Long idEntregador = 1L;

		entregadorRepository.obterQuantidadeEntregadoresPorIdPessoa(null,
				idEntregador);

	}

	@Test
	public void buscarCotasAtendidas() {

		Long idEntregador = 1L;
		int pagina = 1;
		int resultadoPorpagina = 1;
		String sortname = "";
		String sortorder = "";

		EntregadorCotaProcuracaoPaginacaoVO entregadorCotaProcuracaoPaginacaoVO = entregadorRepository
				.buscarCotasAtendidas(idEntregador, pagina, resultadoPorpagina,
						sortname, sortorder);

	}

	@Test
	public void buscarCotasAtendidasNumeroCota() {

		Long idEntregador = 1L;
		int pagina = 1;
		int resultadoPorpagina = 1;
		String sortname = "numeroCota";
		String sortorder = "";

		EntregadorCotaProcuracaoPaginacaoVO entregadorCotaProcuracaoPaginacaoVO = entregadorRepository
				.buscarCotasAtendidas(idEntregador, pagina, resultadoPorpagina,
						sortname, sortorder);

	}

	@Test
	public void buscarCotasAtendidasAsc() {

		Long idEntregador = 1L;
		int pagina = 1;
		int resultadoPorpagina = 1;
		String sortname = "";
		String sortorder = "asc";

		EntregadorCotaProcuracaoPaginacaoVO entregadorCotaProcuracaoPaginacaoVO = entregadorRepository
				.buscarCotasAtendidas(idEntregador, pagina, resultadoPorpagina,
						sortname, sortorder);

	}

	@Test
	public void hasEntregadorCodigo() {

		Long codigo = 1L;

		boolean temEntregador = entregadorRepository
				.hasEntregador(codigo, null);

		Assert.assertFalse(temEntregador);

	}

	@Test
	public void hasEntregadorId() {

		Long id = 1L;

		boolean temEntregador = entregadorRepository.hasEntregador(null, id);

		Assert.assertFalse(temEntregador);

	}

	@Test
	public void obterEntregadorPorRota() {

		Long idRota = 1L;

		Entregador entregador = entregadorRepository
				.obterEntregadorPorRota(idRota);
	}

	@Test
	public void obterMinCodigoEntregadorDisponivel() {

	entregadorRepository.obterMinCodigoEntregadorDisponivel();
	}
	
	@Test
	public void obterEntregadoresPorNome() {

	String nome = "nomeTeste";
		
	 List<Entregador> entregador =  entregadorRepository.obterEntregadoresPorNome(nome);
		
	 Assert.assertNotNull(entregador);
	}
	
	@Test
	public void obterPorNome() {

	String nome = "nomeTeste";
		
	 Entregador entregador =  entregadorRepository.obterPorNome(nome);
		
	}
}

