package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.seguranca.Usuario;

import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.BoletoService;

@Ignore //TODO: Henrique, corrigir a fixture banco para incluir a carteira
public class BoletoServiceImplTest  extends AbstractRepositoryImplTest {
	
	@Autowired
	private BoletoService boletoService;
	
	@Before
	public void setup() {
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com");
		save(pessoaJuridica);
		
		Box box = Fixture.criarBox("300", "Box 300", TipoBox.REPARTE);
		save(box);
		
		Cota cota = Fixture.cota(1000, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		Carteira carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		
		Banco bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
			  							123456L, "1", "1", "Instruções.", Moeda.REAL, "HSBC", "399");
		save(bancoHSBC);

		
		//AMARRAÇAO DIVIDA X BOLETO
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoFinanceiro tipoMovimentoFinenceiroReparte = Fixture.tipoMovimentoFinanceiroReparte();
		save(tipoMovimentoFinenceiroReparte);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);		
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cota, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), new Date());
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cota,
						new Date(), new BigDecimal(200));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cota, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		save(divida);
		
		
		Boleto boleto = Fixture.boleto("5", new Date(), new Date(), new Date(), "0", 
                					   new BigDecimal(100.00), "1", "1", StatusCobranca.PAGO, cota, bancoHSBC, divida);
		save(boleto);

	}
	
	@Test
	@Ignore
	public void teste() {
		
		boletoService.baixarBoletos(null, null, null);
	}
	
	@Test
	public void testeImpressao() throws IOException {
		byte[] b = boletoService.gerarImpressaoBoleto("123");
		Assert.assertTrue(b.length > 0);
	}
	
}
