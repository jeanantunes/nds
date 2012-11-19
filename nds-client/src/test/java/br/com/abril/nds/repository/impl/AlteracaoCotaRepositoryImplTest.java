package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class AlteracaoCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private AlteracaoCotaRepository alteracaoCotaRepository;

	@Test
	public void testarObterCobrancasPorIDS() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);
	}

	@Test
	public void testarPesquisarAlteracaoCotaNumeroCota() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setNumeroCota(1);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaNomeCota() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setNomeCota("teste");

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaIdMunicipio() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setIdMunicipio("teste");

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaIdFornecedor() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setIdFornecedor(1L);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaTipoDesconto() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setTipoDesconto(TipoDesconto.PRODUTO);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaIdVrMinimo() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO.setIdVrMinimo(new BigDecimal("1"));

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	@Test
	public void testarPesquisarAlteracaoCotaDescricaoTipoEntrega() {

		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(1);

		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		filtroAlteracaoCotaDTO
				.setDescricaoTipoEntrega(DescricaoTipoEntrega.ENTREGADOR);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

	/**
	 * Teste de ordenação
	 */
	@Test
	public void testarPesquisarAlteracaoCotaOrdenacao() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "cota.id");
		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);
		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);

		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository
				.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);

		Assert.assertNotNull(pesquisarAlteracaoCota);

	}

}
