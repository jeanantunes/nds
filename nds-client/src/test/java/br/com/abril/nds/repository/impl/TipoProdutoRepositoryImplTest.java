package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class TipoProdutoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;

	private TipoProduto tipoProduto;

	@Before
	public void setUp() {

		Editor abril = Fixture.editoraAbril();
		save(abril);

		for (int i = 0; i < 3; i++) {

			GrupoProduto grupoProduto = GrupoProduto.OUTROS;

			if (i > 1) {
				grupoProduto = GrupoProduto.LIVRO;
			}

			NCM ncmRevistas = Fixture.ncm(i + 99000642l, "REVISTAS", "KG");
			save(ncmRevistas);

			this.tipoProduto = Fixture.tipoProduto("TipoProduto0" + i,
					grupoProduto, ncmRevistas, "nbm", 14L + i);

			save(tipoProduto);

			if (i % 2 == 0) {

				for (int j = 0; j < 2; j++) {

					Produto produto = Fixture.produto(i + j + "1", "Descricao0"
							+ i + j, "Produto0" + i + j,
							PeriodicidadeProduto.SEMANAL, this.tipoProduto, j,
							j, new Long(j), TributacaoFiscal.TRIBUTADO);

					produto.setEditor(abril);
					save(produto);
				}
			}
		}
	}

	@Test
	public void hasProdutoVinculado() {

		List<TipoProduto> list = this.tipoProdutoRepository.buscarTodos();

		boolean hasProduto;

		hasProduto = this.tipoProdutoRepository
				.hasProdutoVinculado(list.get(0));

		Assert.assertTrue(hasProduto);

		hasProduto = this.tipoProdutoRepository
				.hasProdutoVinculado(list.get(1));

		Assert.assertFalse(hasProduto);
	}

	@Test
	public void buscaNulo() {
		List<TipoProduto> lista = tipoProdutoRepository.busca("", null, "", "",
				"codigo", Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaPorNomeTipoProduto() {
		List<TipoProduto> lista = tipoProdutoRepository.busca(
				tipoProduto.getDescricao(), null, "", "", "codigo",
				Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaPorCodigo() {
		List<TipoProduto> lista = tipoProdutoRepository
				.busca("", tipoProduto.getCodigo(), "", "", "codigo",
						Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaPorCodigoNCM() {
		List<TipoProduto> lista = tipoProdutoRepository.busca("", null,
				String.valueOf(tipoProduto.getNcm().getCodigo()), "", "codigo",
				Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaPorCodigoNBM() {
		List<TipoProduto> lista = tipoProdutoRepository.busca("", null, "",
				tipoProduto.getCodigoNBM(), "codigo", Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaOrdenacaoAsc() {
		List<TipoProduto> lista = tipoProdutoRepository.busca(
				tipoProduto.getDescricao(), tipoProduto.getCodigo(),
				String.valueOf(tipoProduto.getNcm().getCodigo()),
				tipoProduto.getCodigoNBM(), "codigo", Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaOrdenacaoDesc() {
		List<TipoProduto> lista = tipoProdutoRepository.busca(
				tipoProduto.getDescricao(), tipoProduto.getCodigo(),
				String.valueOf(tipoProduto.getNcm().getCodigo()),
				tipoProduto.getCodigoNBM(), "codigo", Ordenacao.DESC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscaMaxResultsEInitialResultNegativos() {
		List<TipoProduto> lista = tipoProdutoRepository.busca(
				tipoProduto.getDescricao(), tipoProduto.getCodigo(),
				String.valueOf(tipoProduto.getNcm().getCodigo()),
				tipoProduto.getCodigoNBM(), "codigo", Ordenacao.DESC, -1, -1);

		Assert.assertNotNull(lista);
	}

	@SuppressWarnings("unused")
	@Test
	public void quantidade() {
		Long quantidade = tipoProdutoRepository.quantidade(
				tipoProduto.getDescricao(), tipoProduto.getCodigo(),
				String.valueOf(tipoProduto.getNcm().getCodigo()),
				tipoProduto.getCodigoNBM());

	}

	@SuppressWarnings("unused")
	@Test
	public void getMaxCodigo() {
		Long maxCodigo = tipoProdutoRepository.getMaxCodigo();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterPorCodigo(){
		TipoProduto tipoProduto = tipoProdutoRepository.obterPorCodigo(1L);
	}

}
