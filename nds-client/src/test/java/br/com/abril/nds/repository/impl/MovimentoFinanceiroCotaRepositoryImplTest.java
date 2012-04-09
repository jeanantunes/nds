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

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class MovimentoFinanceiroCotaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	private Cota cotaManoel;	
	
	@Before
	public void setup() {
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
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
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box);
		save(cotaManoel);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(200), null, new Date());
		save(movimentoFinanceiroCota);
		
		
		List<MovimentoFinanceiroCota> lista = new ArrayList<MovimentoFinanceiroCota>();
		lista.add(movimentoFinanceiroCota);
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = 
				Fixture.consolidadoFinanceiroCota(lista, cotaManoel, new Date(), BigDecimal.TEN);
		save(consolidadoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentoFinanceiroCotaDataOperacao() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(1L);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiro =
			movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(null, calendar.getTime());
		
		Assert.assertTrue(!listaMovimentoFinanceiro.isEmpty());
		
		listaMovimentoFinanceiro =
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(cotaManoel.getId(), calendar.getTime());
		
		Assert.assertTrue(!listaMovimentoFinanceiro.isEmpty());
		
		calendar.setTime(new Date());
		
		listaMovimentoFinanceiro =
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(null, calendar.getTime());
			
		Assert.assertTrue(listaMovimentoFinanceiro.isEmpty());
		
		listaMovimentoFinanceiro =
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(cotaManoel.getId(), calendar.getTime());
		
		Assert.assertTrue(listaMovimentoFinanceiro.isEmpty());
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaSucesso() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
		
		Assert.assertFalse(listaMovimentoFinanceiroCota.isEmpty());
		
		int expectedListSize = 1;
		int actualListSize = listaMovimentoFinanceiroCota.size();
		
		Assert.assertEquals(expectedListSize, actualListSize);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPaginadoSucesso() {

		try {

			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
			
			MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository
					.buscarTodos().get(0);

			TipoMovimentoFinanceiro tipoMovimentoFinanceiro = (TipoMovimentoFinanceiro) movimentoFinanceiroCota
					.getTipoMovimento();

			tipoMovimentoFinanceiro.setDescricao("nova descrição");

			MovimentoFinanceiroCota novoMovimentoFinanceiroCota = Fixture
					.movimentoFinanceiroCota(movimentoFinanceiroCota.getCota(),
							tipoMovimentoFinanceiro,
							movimentoFinanceiroCota.getUsuario(),
							new BigDecimal("450"), null, new Date());

			save(novoMovimentoFinanceiroCota);

			tipoMovimentoFinanceiro.setDescricao("outra nova descrição");

			novoMovimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
					movimentoFinanceiroCota.getCota(), tipoMovimentoFinanceiro,
					movimentoFinanceiroCota.getUsuario(),
					new BigDecimal("170"), null, new Date());

			save(novoMovimentoFinanceiroCota);
			
			Integer quantidadeTotalRegistros = 
					this.movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(filtroDebitoCreditoDTO);			

			boolean condicaoRegistrosEncontrados = quantidadeTotalRegistros > 0;
			
			boolean condicaoQuantidadeRegistrosCorreta = quantidadeTotalRegistros == 3;

			Assert.assertNotNull(quantidadeTotalRegistros);
			
			Assert.assertTrue(condicaoRegistrosEncontrados);
			
			Assert.assertTrue(condicaoQuantidadeRegistrosCorreta);
			
			List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
					this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
			
			Assert.assertNotNull(listaMovimentoFinanceiroCota);
			
			Assert.assertFalse(listaMovimentoFinanceiroCota.isEmpty());
			
			int expectedListSize = 1; //Quantidade de registros retornados pela paginação.
			int actualListSize = listaMovimentoFinanceiroCota.size();
			
			Assert.assertEquals(expectedListSize, actualListSize);
			
		} catch (Exception e) {

			Assert.fail();
		}
	}
	
	private FiltroDebitoCreditoDTO getFiltroDebitoCredito() {

		Calendar calendar = Calendar.getInstance();
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = new FiltroDebitoCreditoDTO();
		
		filtroDebitoCreditoDTO.setColunaOrdenacao(ColunaOrdenacao.TIPO_LANCAMENTO);
		
		calendar.add(Calendar.DATE, -5);
		filtroDebitoCreditoDTO.setDataVencimentoInicio(calendar.getTime());
		
		calendar.add(Calendar.DATE, 10);
		filtroDebitoCreditoDTO.setDataVencimentoFim(calendar.getTime());
		
		filtroDebitoCreditoDTO.setPaginacao(new PaginacaoVO(1, 1, "asc"));

		return filtroDebitoCreditoDTO;
	}
}
