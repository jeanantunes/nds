package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO.OrdenacaoColunaBoletos;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


public class BoletoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	private static final Integer NUMERO_COTA = 123;
	private static final Date    DT_VENCTO_DE = new Date();
	private static final Date    DT_VENCTO_ATE = DateUtil.adicionarDias(new Date(),5);
	private static final String  SORT_ORDER = "asc";
	private static final Integer PAGINA = 1;
	private static final Integer QTD_PAGINA = 15;
	
	private Date dataAtual;

	
	//TAREFAS ANTES DA EXECUCAO DO METODO A SER TESTADO
	@Before
	public void setup() {
		
		dataAtual = DateUtil.removerTimestamp(new Date());
		
		//CRIA UM OBJETO PESSOA NA SESSAO PARA TESTES
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com", "99.999-9");
		save(pessoaJuridica);
		
		//CRIA UM OBJETO BOX NA SESSAO PARA TESTES
		Box box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		//CRIA UM OBJETO COTA NA SESSAO PARA TESTES
		Cota cota = Fixture.cota(NUMERO_COTA, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		
		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010",
				  			  		123456L, "1", "1", "Instruções.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		Banco bancoITAU = Fixture.banco(10L, true, 1, "1010",
				  					12345L, "1", "1", "Instrucoes ITAU.", "ITAU", "BANCO ITAU S/A", "184", BigDecimal.TEN, BigDecimal.ONE);
		
		save(bancoHSBC, bancoITAU);
		
		
		//AMARRAÇAO DIVIDA X BOLETO
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoFinanceiro tipoMovimentoFinenceiroRecebimentoReparte =
			Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinenceiroRecebimentoReparte);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);		
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14, new Long(100),
				BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produtoVeja, 
				null, false);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(100), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cota, tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, dataAtual, true);
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado1 =
			Fixture.consolidadoFinanceiroCota(
				Arrays.asList(movimentoFinanceiroCota), cota, dataAtual, new BigDecimal(200),
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		save(consolidado1);
		
		ConsolidadoFinanceiroCota consolidado2 =
			Fixture.consolidadoFinanceiroCota(
				null, cota, dataAtual, new BigDecimal(200),
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		save(consolidado2);
			
			
		ConsolidadoFinanceiroCota consolidado3 =
			Fixture.consolidadoFinanceiroCota(
				null, cota, dataAtual, new BigDecimal(200),
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		save(consolidado3);
		
		Divida divida1 =
			Fixture.divida(consolidado1, cota, dataAtual, usuarioJoao,
						   StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida1);
		
		Divida divida2 =
			Fixture.divida(consolidado2, cota, dataAtual, usuarioJoao,
						   StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida2);
		
		Divida divida3 =
			Fixture.divida(consolidado3, cota, dataAtual, usuarioJoao,
						   StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida3);
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado4 = Fixture.consolidadoFinanceiroCota(null, cota, dataAtual, new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado4);
		
		Divida divida4 = Fixture.divida(consolidado4, cota, dataAtual, usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		save(divida4);
		
		Boleto boleto =
			Fixture.boleto(
				"5", "5", "5", dataAtual, dataAtual, dataAtual, BigDecimal.ZERO,
				new BigDecimal(100.00), TipoBaixaCobranca.MANUAL, "1", StatusCobranca.PAGO, cota, bancoHSBC, divida1, 0);
		save(boleto);
		
		Boleto boleto2 =
			Fixture.boleto(
				"55", "5", "55", dataAtual, dataAtual, dataAtual, BigDecimal.ZERO,
				new BigDecimal(100.00), TipoBaixaCobranca.MANUAL, "1", StatusCobranca.NAO_PAGO, cota, bancoHSBC, divida2, 0);
		save(boleto2);

		Boleto boleto3 =
			Fixture.boleto(
				"555", "5", "555", dataAtual, dataAtual, dataAtual, BigDecimal.ZERO,
				new BigDecimal(100.00), TipoBaixaCobranca.MANUAL, "1", StatusCobranca.PAGO, cota, bancoHSBC, divida3, 0);
		save(boleto3);
		
		BaixaAutomatica baixa =
			Fixture.baixaAutomatica(
				boleto, DateUtil.removerTimestamp(dataAtual), null, null, null, StatusBaixa.PAGO, BigDecimal.TEN, bancoHSBC);
		
		BaixaAutomatica baixa2 =
			Fixture.baixaAutomatica(
				boleto2, DateUtil.removerTimestamp(dataAtual), null, null, null, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, BigDecimal.TEN, bancoITAU);
		
		BaixaAutomatica baixa3 =
			Fixture.baixaAutomatica(
				boleto3, DateUtil.removerTimestamp(dataAtual), null, null, null, StatusBaixa.PAGO_DIVERGENCIA_VALOR, BigDecimal.TEN, bancoHSBC);
		
		save(baixa, baixa2, baixa3);
	}
	
	@Test
	public void obterBoletosPorCota() {
		
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO(NUMERO_COTA,
                															   DT_VENCTO_DE,
                															   DT_VENCTO_ATE,
                															   StatusCobranca.PAGO);
		PaginacaoVO paginacao = new PaginacaoVO(PAGINA,QTD_PAGINA,SORT_ORDER);
		filtro.setPaginacao(paginacao);
		
        List<Boleto> boletos = this.boletoRepository.obterBoletosPorCota(filtro);

        //VERIFICA SE A LISTA DE BOLETOS E NULA
        Assert.assertNotNull(boletos);
        
        //VERIFICA SE A LISTA DE BOLETOS CONTEM RESULTADOS
        Assert.assertTrue(boletos.size() > 0);
        
        //VERIFICA SE FILTRO TROUXE VALORES CORRETOS
		for (int i=0; i<boletos.size();i++){
		    Assert.assertEquals(NUMERO_COTA, boletos.get(i).getCota().getNumeroCota());
		}
	}

	@Test
	public void obterBoletosPorCotaDatadeVencimentoDeTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setDataVencimentoDe(Fixture.criarData(18, Calendar.OCTOBER, 2011));
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaDatadeVencimentoAteTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setDataVencimentoAte(Fixture.criarData(28, Calendar.OCTOBER, 2011));
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaStatusTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setStatus(StatusCobranca.PAGO);
		
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaNNumeroTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.NOSSO_NUMERO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaDTEmissaoTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.DATA_EMISSAO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaDTVencimentoTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.DATA_VENCIMENTO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaDTPagamentoTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.DATA_PAGAMENTO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaEncargosTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.ENCARGOS);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}

	@Test
	public void obterBoletosPorCotaOrdenacaoColunaTPBaixaTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.TIPO_BAIXA);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
		
	}
	
	@Test
	public void obterBoletosPorCotaOrdenacaoColunaStatusCobrancaTest() {
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaBoletos.STATUS_COBRANCA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<Boleto> listaBoletos= boletoRepository.obterBoletosPorCota(filtro);
		
		Assert.assertNotNull(listaBoletos);
}
	
	
	@Test
	public void obterQuantidadeDeBoletosPorNumeroDaCota() {
		
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO(NUMERO_COTA,
                															   DT_VENCTO_DE,
                															   DT_VENCTO_ATE,
                															   StatusCobranca.PAGO);
		PaginacaoVO paginacao = new PaginacaoVO(PAGINA,QTD_PAGINA,SORT_ORDER);
		filtro.setPaginacao(paginacao);
		
        long qtdBoletos = this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
        
       //VERIFICA SE A QUANTIDADE DE BOLETOS RETORNADOS E MAIOR QUE 0
        Assert.assertTrue(qtdBoletos > 0);
	}
	
	@Test
	public void obterPorNossoNumero() {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero("5", null);
		
		Assert.assertNotNull(boleto);	
	}
	
	@Test
	public void obterPorNossoNumeroDividaAcumulada() {
		Boleto boleto = boletoRepository.obterPorNossoNumero("5", false);
				
		Assert.assertNotNull(boleto);	
	}
	
	@Test
	public void obterPorNossoNumeroCompleto() {
		
		Boleto boleto = boletoRepository.obterPorNossoNumeroCompleto("5", null);
		
		Assert.assertNotNull(boleto);	
	}
	
	@Test
	public void obterPorNossoNumeroCompletoDividaAcululadaTest() {
		
		Boleto boleto = boletoRepository.obterPorNossoNumeroCompleto("5", false);
		
		Assert.assertNotNull(boleto);	
	}
	
	@Test
	public void obterQuantidadePrevisaoBoletos() {
		
		Long quantidadeBoletosPrevistos =
			this.boletoRepository.obterQuantidadeBoletosPrevistos(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosPrevistos);
		
		Assert.assertTrue(!quantidadeBoletosPrevistos.equals(0L));
	}
	
	@Test
	public void obterQuantidadeBoletosLidos() {
		
		Long quantidadeBoletosLidos =
			this.boletoRepository.obterQuantidadeBoletosLidos(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosLidos);
		
		Assert.assertTrue(!quantidadeBoletosLidos.equals(0L));
	}
	
	@Test
	public void obterQuantidadeBoletosBaixados() {
		
		Long quantidadeBoletosBaixados =
			this.boletoRepository.obterQuantidadeBoletosBaixados(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosBaixados);
		
		Assert.assertTrue(!quantidadeBoletosBaixados.equals(0L));
	}
	
	@Test
	public void obterQuantidadeBoletosRejeitados() {
		
		Long quantidadeBoletosRejeitados =
			this.boletoRepository.obterQuantidadeBoletosRejeitados(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosRejeitados);
		
		Assert.assertTrue(!quantidadeBoletosRejeitados.equals(0L));
	}
	
	@Test
	public void obterQuantidadeBoletosBaixadosComDivergencia() {
		
		Long quantidadeBoletosBaixados =
			this.boletoRepository.obterQuantidadeBoletosBaixadosComDivergencia(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosBaixados);
		
		Assert.assertTrue(!quantidadeBoletosBaixados.equals(0L));
	}
	
	@Test
	public void obterQuantidadeBoletosInadimplentes() {
		
		Long quantidadeBoletosInadimplentes =
			this.boletoRepository.obterQuantidadeBoletosInadimplentes(dataAtual);
		
		Assert.assertNotNull(quantidadeBoletosInadimplentes);
		
		Assert.assertTrue(!quantidadeBoletosInadimplentes.equals(0L));
	}
	
	@Test
	public void obterValorTotalBancario() {
		
		BigDecimal valorTotalBancario =
			this.boletoRepository.obterValorTotalBancario(dataAtual);
		
		Assert.assertNotNull(valorTotalBancario);
		
		Assert.assertTrue(valorTotalBancario.compareTo(BigDecimal.ZERO) == 1);
	}
	
	@Test
	public void obterQuantidadeTotalBancario() {
		
		Long quantidadeTotalBancario =
			this.boletoRepository.obterQuantidadeTotalBancario(dataAtual);
		
		Assert.assertNotNull(quantidadeTotalBancario);
		
		Assert.assertTrue(!quantidadeTotalBancario.equals(0L));
	}
	
	@Test
	public void obterBoletosPrevistos() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();

		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 3, "asc");

		filtro.setData(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.NOSSO_NUMERO);

		List<DetalheBaixaBoletoDTO> boletosPrevistos =
				this.boletoRepository.obterBoletosPrevistos(filtro);
		
		Assert.assertNotNull(boletosPrevistos);
		Assert.assertEquals(3, boletosPrevistos.size());
		
		DetalheBaixaBoletoDTO detalheBaixaBoleto = boletosPrevistos.get(0);

		DetalheBaixaBoletoDTO detalheBaixaBoleto1 = boletosPrevistos.get(1);
		
		DetalheBaixaBoletoDTO detalheBaixaBoleto2 = boletosPrevistos.get(2);
		
		boolean ordenacaoNossoNumero = detalheBaixaBoleto.getNossoNumero().compareTo(detalheBaixaBoleto1.getNossoNumero()) < 0;
		
		Assert.assertTrue(ordenacaoNossoNumero);
		
		ordenacaoNossoNumero = detalheBaixaBoleto1.getNossoNumero().compareTo(detalheBaixaBoleto2.getNossoNumero()) < 0;
		
		Assert.assertTrue(ordenacaoNossoNumero);
	}
	
	@Test
	public void obterBoletosPrevistosPaginacaoNulo() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();

		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 3, "asc");

		filtro.setData(dataAtual);
		

		List<DetalheBaixaBoletoDTO> boletosPrevistos =
				this.boletoRepository.obterBoletosPrevistos(filtro);
		
		Assert.assertNotNull(boletosPrevistos);
		Assert.assertEquals(3, boletosPrevistos.size());
		
		DetalheBaixaBoletoDTO detalheBaixaBoleto = boletosPrevistos.get(0);

		DetalheBaixaBoletoDTO detalheBaixaBoleto1 = boletosPrevistos.get(1);
		
		DetalheBaixaBoletoDTO detalheBaixaBoleto2 = boletosPrevistos.get(2);
		
		boolean ordenacaoNossoNumero = detalheBaixaBoleto.getNossoNumero().compareTo(detalheBaixaBoleto1.getNossoNumero()) < 0;
		
		Assert.assertTrue(ordenacaoNossoNumero);
		
		ordenacaoNossoNumero = detalheBaixaBoleto1.getNossoNumero().compareTo(detalheBaixaBoleto2.getNossoNumero()) < 0;
		
		Assert.assertTrue(ordenacaoNossoNumero);
	}
	
	@Test
	public void obterBoletosBaixados() {
		
        FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.DATA_VENCIMENTO);
		
		List<DetalheBaixaBoletoDTO> boletosBaixados =
			this.boletoRepository.obterBoletosBaixados(filtro);
		
		Assert.assertNotNull(boletosBaixados);
	}
	
	@Test
	public void obterBoletosBaixadosPaginacaoNulo() {
		
        FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		
		
		List<DetalheBaixaBoletoDTO> boletosBaixados =
			this.boletoRepository.obterBoletosBaixados(filtro);
		
		Assert.assertNotNull(boletosBaixados);
	}
	
	@Test
	public void obterBoletosRejeitados() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, null);

		filtro.setData(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.NOME_BANCO);
		
		List<DetalheBaixaBoletoDTO> boletosRejeitados =
				this.boletoRepository.obterBoletosRejeitados(filtro);
		
		Assert.assertNotNull(boletosRejeitados);
		Assert.assertEquals(1, boletosRejeitados.size());
	}
	
	@Test
	public void obterBoletosRejeitadosPaginacaoNulo() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, null);

		filtro.setData(dataAtual);
		
		
		List<DetalheBaixaBoletoDTO> boletosRejeitados =
				this.boletoRepository.obterBoletosRejeitados(filtro);
		
		Assert.assertNotNull(boletosRejeitados);
		Assert.assertEquals(1, boletosRejeitados.size());
	}
	
	
	@Test
	public void obterBoletosBaixadosComDivergencia() {
		
		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.MOTIVO_DIVERGENCIA);
		
		List<DetalheBaixaBoletoDTO> boletosDivergentes =
				this.boletoRepository.obterBoletosBaixadosComDivergencia(filtro);
		
		Assert.assertNotNull(boletosDivergentes);
		Assert.assertEquals(1, boletosDivergentes.size());
		
		DetalheBaixaBoletoDTO resultado = boletosDivergentes.get(0);
		
		BigDecimal valorDiferencaCorreto = resultado.getValorBoleto().subtract(resultado.getValorPago());
		
		Assert.assertEquals(valorDiferencaCorreto, resultado.getValorDiferenca());
	}
	
	@Test
	public void obterBoletosBaixadosComDivergenciaPaginacaoNulo() {
		
		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		
		
		List<DetalheBaixaBoletoDTO> boletosDivergentes =
				this.boletoRepository.obterBoletosBaixadosComDivergencia(filtro);
		
		Assert.assertNotNull(boletosDivergentes);
		Assert.assertEquals(1, boletosDivergentes.size());
		
		DetalheBaixaBoletoDTO resultado = boletosDivergentes.get(0);
		
		BigDecimal valorDiferencaCorreto = resultado.getValorBoleto().subtract(resultado.getValorPago());
		
		Assert.assertEquals(valorDiferencaCorreto, resultado.getValorDiferenca());
	}
	
	@Test
	public void obterBoletosInadimplentes() {
		
        FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		filtro.setDataVencimento(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.DATA_VENCIMENTO);
		
		List<DetalheBaixaBoletoDTO> boletosInadimplentes =
			this.boletoRepository.obterBoletosInadimplentes(filtro);
		
		Assert.assertNotNull(boletosInadimplentes);
	}
	
	@Test
	public void obterBoletosInadimplentesPaginacaoNulo() {
		
        FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 1, "desc");

		filtro.setData(dataAtual);
		filtro.setDataVencimento(dataAtual);
		
		
		List<DetalheBaixaBoletoDTO> boletosInadimplentes =
			this.boletoRepository.obterBoletosInadimplentes(filtro);
		
		Assert.assertNotNull(boletosInadimplentes);
	}
	
	
	@Test
	public void obterTotalBancario() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 10, null);

		filtro.setData(dataAtual);
		filtro.setPaginacao(paginacaoVO);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto.NOME_BANCO);
		
		List<DetalheBaixaBoletoDTO> detalheValorTotalBancario =
			this.boletoRepository.obterTotalBancario(filtro);
		
		Assert.assertNotNull(detalheValorTotalBancario);
		Assert.assertEquals(2, detalheValorTotalBancario.size());
	}
	
	@Test
	public void obterTotalBancarioPaginacaoNulo() {

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 10, null);

		filtro.setData(dataAtual);
		
		
		List<DetalheBaixaBoletoDTO> detalheValorTotalBancario =
			this.boletoRepository.obterTotalBancario(filtro);
		
		Assert.assertNotNull(detalheValorTotalBancario);
		Assert.assertEquals(2, detalheValorTotalBancario.size());
	}
	
	
}