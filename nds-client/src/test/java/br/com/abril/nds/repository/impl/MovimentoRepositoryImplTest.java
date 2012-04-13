package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class MovimentoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private MovimentoRepository movimentoRepository;
	
	private TipoMovimentoEstoque tipoMovimentoFaltaEm;
	
	@Before
	public void setup() {
		/*Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteira);
		
		Banco banco = Fixture.hsbc(carteira); 
		save(banco);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333.333.333.333", "distrib_acme@mail.com");
		save(juridicaDistrib);
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem");
		
		Distribuidor distribuidor = Fixture.distribuidor(juridicaDistrib, new Date(), politicaCobranca);
		save(distribuidor);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito =
			Fixture.tipoMovimentoFinanceiroCredito();
		save(tipoMovimentoFinanceiroCredito);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		Box box = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"developertestermail@gmail.com", "Manoel da Silva");
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box);
		save(cotaManoel);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(200), null, new Date());
		save(movimentoFinanceiroCota);
		
		
		List<MovimentoFinanceiroCota> lista = new ArrayList<MovimentoFinanceiroCota>();
		lista.add(movimentoFinanceiroCota);
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = 
				Fixture.consolidadoFinanceiroCota(lista, cotaManoel, new Date(), BigDecimal.TEN);
		save(consolidadoFinanceiroCota);*/
		
		
		Calendar calendar = Calendar.getInstance();
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja = Fixture.produtoEdicao(1L, 10, 14,
			new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produtoVeja);
		save(produtoEdicaoVeja);
		
		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaEm = merge(tipoMovimentoFaltaEm);
		
		EstoqueProduto estoqueProdutoVeja = Fixture.estoqueProduto(produtoEdicaoVeja, BigDecimal.ZERO);
		save(estoqueProdutoVeja);
		
		MovimentoEstoque movimentoEstoque = 
			Fixture.movimentoEstoque(
				null, produtoEdicaoVeja, tipoMovimentoFaltaEm, usuarioJoao, estoqueProdutoVeja,
				calendar.getTime(), BigDecimal.TEN, StatusAprovacao.PENDENTE, "motivo");
		save(movimentoEstoque);
	}
	
	@Test
	public void obterMovimentosAprovacao() {
		
		FiltroControleAprovacaoDTO filtroControleAprovacaoDTO = getFiltroDebitoCredito();

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = 
				this.movimentoRepository.obterMovimentosAprovacao(filtroControleAprovacaoDTO);
		
		Assert.assertNotNull(listaControleAprovacaoDTO);
		
		Assert.assertTrue(!listaControleAprovacaoDTO.isEmpty());
		
		Long qtdeTotalRegistros =
				this.movimentoRepository.obterTotalMovimentosAprovacao(filtroControleAprovacaoDTO);
		
		Assert.assertTrue(qtdeTotalRegistros == 1);
	}
	
	private FiltroControleAprovacaoDTO getFiltroDebitoCredito() {

		Calendar calendar = Calendar.getInstance();
		
		FiltroControleAprovacaoDTO filtroControleAprovacaoDTO = new FiltroControleAprovacaoDTO();
		
		filtroControleAprovacaoDTO.setIdTipoMovimento(tipoMovimentoFaltaEm.getId());
		
		filtroControleAprovacaoDTO.setDataMovimento(calendar.getTime());
		
		filtroControleAprovacaoDTO.setPaginacao(new PaginacaoVO(1, 15, "asc"));

		return filtroControleAprovacaoDTO;
	}
}
