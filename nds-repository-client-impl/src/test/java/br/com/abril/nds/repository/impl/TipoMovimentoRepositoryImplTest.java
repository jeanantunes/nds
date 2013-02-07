package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class TipoMovimentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;

	@Before
	public void setUp() {

		TipoMovimentoEstoque movEstoque = Fixture.tipoMovimentoEnvioEncalhe();
		TipoMovimentoFinanceiro movFinanceiro = Fixture
				.tipoMovimentoFinanceiroCompraEncalhe();

		save(movEstoque, movFinanceiro);
	}

	@Test
	public void obterTiposMovimento() {
		List<TipoMovimento> lista = tipoMovimentoRepository
				.obterTiposMovimento();

		Assert.assertNotNull(lista);
	}

	@Test
	public void obterTipoMovimentoPorDescricao() {

		FiltroTipoMovimento filtro = new FiltroTipoMovimento();
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "codigo"));
		filtro.setDescricao("Encalhe");

		List<TipoMovimentoDTO> tipos = tipoMovimentoRepository
				.obterTiposMovimento(filtro);

		Assert.assertNotNull(tipos);
		Assert.assertTrue(tipos.size() == 2);

	}

	@Test
	public void obterTiposMovimentoOrdenacaoCodigo() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigo");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoOrdenacaoDescricao() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("descricao");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoOrdenacaoGrupoOperacao() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("grupoOperacao");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoOrdenacaoOperacao() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("operacao");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoOrdenacaoAprovacao() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("aprovacao");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoOrdenacaoIncideDivida() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("incideDivida");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoPorPaginacaoPosicaoInicialEQtdeResultPag() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("incideDivida");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@Test
	public void obterTiposMovimentoPorCodigo() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();
		filtro.setCodigo(1L);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("incideDivida");

		tipoMovimentoRepository.obterTiposMovimento(filtro);
	}

	@SuppressWarnings("unused")
	@Test
	public void countObterTiposMovimento() {
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();

		Integer count = tipoMovimentoRepository.countObterTiposMovimento(filtro);
	}
	
	
	
}
