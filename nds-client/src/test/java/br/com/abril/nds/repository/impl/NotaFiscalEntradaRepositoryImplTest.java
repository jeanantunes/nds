package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Serie;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

public class NotaFiscalEntradaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private NotaFiscalEntradaRepository notaFiscalRepository;

	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	Long numeroNota = 2344242L;
	String serie = "345353543";
	
	private CFOP cfop;

	@Before
	public void setup() {
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();

		Fornecedor fornecedor = Fixture.fornecedorFC(tipoFornecedor);
		save(fornecedor);

		cfop = Fixture.cfop5102();
		save(cfop);

		TipoNotaFiscal tp = Fixture.tipoNotaFiscalRecebimento(cfop);
		save(tp);

		NotaFiscalEntrada notaFiscal = new NotaFiscalEntradaFornecedor();
		notaFiscal.setCfop(cfop);
		notaFiscal.setChaveAcesso(chave);
		notaFiscal.setNumero(numeroNota);
		notaFiscal.setSerie(serie);
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setOrigem(Origem.INTERFACE);
		notaFiscal.setValorBruto(BigDecimal.TEN);
		notaFiscal.setValorLiquido(BigDecimal.TEN);
		notaFiscal.setValorDesconto(BigDecimal.TEN);
		((NotaFiscalEntradaFornecedor) notaFiscal).setFornecedor(fornecedor);

		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscal.setTipoNotaFiscal(tp);
		save(notaFiscal);
	}

	@Test
	public void obterNotaFiscalPorNumeroSerieCnpj() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setCnpj(cnpj);
		filtro.setChave(chave);
		filtro.setNumeroNota(numeroNota);
		filtro.setSerie(serie);

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);
		Assert.assertEquals(1, listaNotas.size());

	}

	@Test
	public void obterNotaFiscalPorNumeroSerieCnpjNulo() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);

	}
	
	@Test
	public void obterNotaFiscalPorNumeroSerieCnpjPorCnpj() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setCnpj(cnpj);

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);

	}
	
	@Test
	public void obterNotaFiscalPorNumeroSerieCnpjPorChave() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setChave(chave);

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);

	}
	
	@Test
	public void obterNotaFiscalPorNumeroSerieCnpjPorNumeroNota() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setNumeroNota(numeroNota);

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);

	}
	
	@Test
	public void obterNotaFiscalPorNumeroSerieCnpjPorSerie() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setSerie(serie);

		List<NotaFiscalEntrada> listaNotas = notaFiscalRepository
				.obterNotaFiscalPorNumeroSerieCnpj(filtro);

		Assert.assertNotNull(listaNotas);

	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradas(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorPeriodo(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		filtro.getPeriodo().setDataInicial(Fixture.criarData(10, Calendar.OCTOBER, 2012));
		filtro.getPeriodo().setDataFinal(Fixture.criarData(10, Calendar.OCTOBER, 2012));
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorIdTipoNotaFiscal(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		filtro.setIdTipoNotaFiscal(1L);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorIdDistribuidor(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		filtro.setIdDistribuidor(1L);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorIdFornecedor(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		filtro.setIdFornecedor(1L);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorIsNotaRecebida(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setPeriodo(new PeriodoVO());
		filtro.setIsNotaRecebida(true);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorDataEmissao(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.DATA_EMISSAO);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorDataExpedicao(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.DATA_EXPEDICAO);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorFornecedor(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.FORNECEDOR);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorNotaRecebida(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.NOTA_RECEBIDA);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorNumeroNota(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.NUMERO_NOTA);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorTipoNota(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.TIPO_NOTA);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterQuantidadeNotasFicaisCadastradasPorValor(){
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		List<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<FiltroConsultaNotaFiscalDTO.ColunaOrdenacao>();
		listaColunaOrdenacao.add(FiltroConsultaNotaFiscalDTO.ColunaOrdenacao.VALOR);
		filtro.setPeriodo(new PeriodoVO());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setListaColunaOrdenacao(listaColunaOrdenacao);
		Integer quantidade = notaFiscalRepository.obterQuantidadeNotasFicaisCadastradas(filtro);
		
		Assert.assertNotNull(quantidade);
	}
	
	@Test
	public void obterListaFornecedorNotaFiscal(){
		List<Long> filtroConsultaNotaFiscal = new ArrayList<>();
		filtroConsultaNotaFiscal.add(1L);
		List<ItemDTO<Long, String>> lista = notaFiscalRepository.obterListaFornecedorNotaFiscal(filtroConsultaNotaFiscal);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterNotasFiscaisCadastradasSemPaginacao(){
		FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal = new FiltroConsultaNotaFiscalDTO();
		filtroConsultaNotaFiscal.setPeriodo(new PeriodoVO());
		List<NotaFiscalEntradaFornecedor> lista = notaFiscalRepository.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
		 Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterNotasFiscaisCadastradasComPaginacao(){
		FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal = new FiltroConsultaNotaFiscalDTO();
		filtroConsultaNotaFiscal.setPeriodo(new PeriodoVO());
		filtroConsultaNotaFiscal.setPaginacao(new PaginacaoVO());
		 List<NotaFiscalEntradaFornecedor> lista = notaFiscalRepository.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
		 Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterDetalhesNotaFicalNulo(){
		List<DetalheItemNotaFiscalDTO> lista = notaFiscalRepository.obterDetalhesNotaFical(null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterDetalhesNotaFical(){
		List<DetalheItemNotaFiscalDTO> lista = notaFiscalRepository.obterDetalhesNotaFical(1L);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void inserirNotaFiscal(){
		NotaFiscalEntradaFornecedor notaFiscal = new NotaFiscalEntradaFornecedor();
		notaFiscal.setFornecedor(new Fornecedor());
		notaFiscal.setOrigem(Origem.MANUAL);
		notaFiscal.setUsuario(new Usuario());
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		notaFiscal.setDataEmissao(Fixture.criarData(10, Calendar.AUGUST, 2012));
		notaFiscal.setDataExpedicao(Fixture.criarData(12, Calendar.AUGUST, 2012));
		notaFiscal.setNumero(1L);
		notaFiscal.setSerie("teste");
		notaFiscal.setValorBruto(new BigDecimal("10"));
		notaFiscal.setValorLiquido(new BigDecimal("9"));
		notaFiscal.setValorDesconto(new BigDecimal("1"));

		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("teste");
		tipoNotaFiscal.setGrupoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO);
		tipoNotaFiscal.setNopCodigo(1L);
		tipoNotaFiscal.setTipoAtividade(TipoAtividade.MERCANTIL);
		tipoNotaFiscal.setEmitente(TipoUsuarioNotaFiscal.COTA);
		tipoNotaFiscal.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		tipoNotaFiscal.setContribuinte(false);
		tipoNotaFiscal.setSerieNotaFiscal(1);
		
		tipoNotaFiscal.setCfopEstado(cfop);
		tipoNotaFiscal.setCfopOutrosEstados(cfop);
		
		save(tipoNotaFiscal);
		
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setEmitida(true);
		notaFiscalRepository.inserirNotaFiscal(notaFiscal);
	}
	
	
}

