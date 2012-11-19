package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO.OrdenacaoColunaParametrosCobranca;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class PoliticaCobrancaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;

	@Before
	public void setUp() {

		Banco banco = Fixture.hsbc();
		save(banco);

		ParametroCobrancaCota parametroCobranca = Fixture
				.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, true,
						BigDecimal.TEN, null);
		save(parametroCobranca);

		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true,
				new BigDecimal(200), true, banco, BigDecimal.ONE,
				BigDecimal.ONE, parametroCobranca);
		save(formaBoleto);

		PoliticaCobranca politicaCobranca = Fixture.criarPoliticaCobranca(null,
				formaBoleto, true, true, true, 1, "Assunto", "Mensagem", true,
				FormaEmissao.INDIVIDUAL_BOX);

		save(politicaCobranca);
	}

	@Test
	public void obterPorTipoCobranca() {
		PoliticaCobranca politicaCobranca = politicaCobrancaRepository
				.obterPorTipoCobranca(TipoCobranca.BOLETO);

		Assert.assertNotNull(politicaCobranca);
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarPoliticaCobrancaPrincipal() {
		PoliticaCobranca politicaCobranca = politicaCobrancaRepository
				.buscarPoliticaCobrancaPrincipal();
	}

	@Test
	public void obterPoliticasCobranca() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterPoliticasCobrancaPorIdBanco() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		Long idBanco = 1L;
		filtro.setIdBanco(idBanco);
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterPoliticasCobrancaPorTipoCobranca() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setTipoCobranca(TipoCobranca.BOLETO);
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaTipoCobranca() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.TIPO_COBRANCA);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaNomeBanco() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.NOME_BANCO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaValorMinimoEmissao() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.VALOR_MINIMO_EMISSAO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaAcumulaDivida() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.ACUMULA_DIVIDA);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaCobrancaUnificada() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.COBRANCA_UNIFICADA);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaFormaEmissao() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.FORMA_EMISSAO);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterPoliticasCobrancaPorOrdenacaoColunaEnvioEmail() {
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.ENVIO_EMAIL);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<PoliticaCobranca> lista = politicaCobrancaRepository
				.obterPoliticasCobranca(filtro);
		Assert.assertNotNull(lista);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterQuantidadePoliticasCobranca(){
		
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		
		int quantidade = politicaCobrancaRepository.obterQuantidadePoliticasCobranca(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQuantidadePoliticasCobrancaPorIdBanco(){
		
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setIdBanco(1L);
		
		int quantidade = politicaCobrancaRepository.obterQuantidadePoliticasCobranca(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQuantidadePoliticasCobrancaPorTipoCobranca(){
		
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setTipoCobranca(TipoCobranca.DEPOSITO);
		
		int quantidade = politicaCobrancaRepository.obterQuantidadePoliticasCobranca(filtro);
		
	}
	
	@Test
	public void desativarPoliticaCobranca(){
		long idPolitica = 1L;
		politicaCobrancaRepository.desativarPoliticaCobranca(idPolitica);
	}
}
