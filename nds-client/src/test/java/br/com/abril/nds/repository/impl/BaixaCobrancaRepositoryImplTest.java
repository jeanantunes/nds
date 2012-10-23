package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.vo.PaginacaoVO;

public class BaixaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	BaixaCobrancaRepositoryImpl baixaCobrancaRepositoryImpl;

	@Test
	public void testarBuscarUltimaBaixaAutomaticaDia() {

		Calendar dataOperacao = Calendar.getInstance();
		Date data = baixaCobrancaRepositoryImpl
				.buscarUltimaBaixaAutomaticaDia(dataOperacao.getTime());

		// Assert.assertNull(data);
	}

	@Test
	public void testarBuscarDiaUltimaBaixaAutomaticaTest() {

		Date data = baixaCobrancaRepositoryImpl
				.buscarDiaUltimaBaixaAutomatica();

		// Assert.assertNull(data);

	}

	@Test
	public void testarBuscarCobrancasBaixadas() {

		List<CobrancaVO> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setNumeroCota(1);
		
		cobrancas = baixaCobrancaRepositoryImpl.buscarCobrancasBaixadas(filtro);

		Assert.assertNotNull(cobrancas);

	}
	
	@Test
	public void testarBuscarCobrancasBaixadasNossoNumero() {

		List<CobrancaVO> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setNossoNumero("1a");
		
		cobrancas = baixaCobrancaRepositoryImpl.buscarCobrancasBaixadas(filtro);

		Assert.assertNotNull(cobrancas);

	}
	
	@Test
	public void testarBuscarCobrancasBaixadasPaginacao() {

		List<CobrancaVO> cobrancas;
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC","cota.numeroCota");
//		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		
		cobrancas = baixaCobrancaRepositoryImpl.buscarCobrancasBaixadas(filtro);

		Assert.assertNotNull(cobrancas);

	}
	
	@Test
	public void testarObterUltimaBaixaCobranca() {
		
		BaixaCobranca baixaCobranca;
		
		baixaCobranca = baixaCobrancaRepositoryImpl.obterUltimaBaixaCobranca(1L);
		
//		Assert.assertNull(baixaCobranca);
	}
	
	@Test
	public void testarObterBaixasManual() {
		
		List<Long> idsCobranca = new ArrayList<Long>();
		
		idsCobranca.add(1L);
		idsCobranca.add(2L);
		idsCobranca.add(3L);
		
		List<BaixaManual> baixas = baixaCobrancaRepositoryImpl.obterBaixasManual(idsCobranca);
		
		Assert.assertNotNull(baixas);
		
	}
	

}
