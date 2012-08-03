package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
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
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;


public class BoletoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	private static final Integer NUMERO_COTA = 123;
	private static final Date    DT_VENCTO_DE = new Date();
	private static final Date    DT_VENCTO_ATE = DateUtil.adicionarDias(new Date(),5);
	private static final String  SORT_ORDER = "asc";
	private static final Integer PAGINA = 1;
	private static final Integer QTD_PAGINA = 15;

	
	//TAREFAS ANTES DA EXECUCAO DO METODO A SER TESTADO
	@Before
	public void setup() {
		
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
		save(bancoHSBC);
		
		
		
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
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cota, tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cota,
						new Date(), new BigDecimal(200), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cota, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida);
		
		
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		
		Divida divida1 = Fixture.divida(consolidado1, cota, new Date(), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		save(divida1);
		
	    Boleto boleto = Fixture.boleto("5", "5", "5",
                					   new Date(), 
                					   new Date(), 
                					   new Date(), 
                					   BigDecimal.ZERO, 
                					   new BigDecimal(100.00), 
                					   "1", 
                					   "1",
                					   StatusCobranca.PAGO,
                					   cota,
                					   bancoHSBC,
                					   divida,0);
		save(boleto);		
	}
	
	@Test
	public void obterBoletosPorNumeroDaCota() {
		
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
	public void obterPorNossoNumeroCompleto() {
		
		Boleto boleto = boletoRepository.obterPorNossoNumeroCompleto("5", null);
		
		Assert.assertNotNull(boleto);	
	}

}
