package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class MovimentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private MovimentoRepository movimentoRepository;

	private TipoMovimentoEstoque tipoMovimentoFaltaEm;

	@Before
	public void setup() {

		Calendar calendar = Calendar.getInstance();

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);

		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja = Fixture.produtoEdicao(1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPQ", produtoVeja, null, false);
		save(produtoEdicaoVeja);

		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaEm = merge(tipoMovimentoFaltaEm);

		EstoqueProduto estoqueProdutoVeja = Fixture.estoqueProduto(
				produtoEdicaoVeja, BigInteger.ZERO);
		save(estoqueProdutoVeja);

		MovimentoEstoque movimentoEstoque = Fixture.movimentoEstoque(null,
				produtoEdicaoVeja, tipoMovimentoFaltaEm, usuarioJoao,
				estoqueProdutoVeja, calendar.getTime(), BigInteger.TEN,
				StatusAprovacao.PENDENTE, "motivo");
		save(movimentoEstoque);
	}

	@Test
	public void obterMovimentosAprovacao() {

		FiltroControleAprovacaoDTO filtro = getFiltro();

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

		Assert.assertTrue(!listaControleAprovacaoDTO.isEmpty());

		Long qtdeTotalRegistros = this.movimentoRepository
				.obterTotalMovimentosAprovacao(filtro);

		Assert.assertTrue(qtdeTotalRegistros == 1);
	}

	private FiltroControleAprovacaoDTO getFiltro() {

		Calendar calendar = Calendar.getInstance();

		FiltroControleAprovacaoDTO filtro = new FiltroControleAprovacaoDTO();

		filtro.setIdTipoMovimento(tipoMovimentoFaltaEm.getId());

		filtro.setDataMovimento(calendar.getTime());

		filtro.setPaginacao(new PaginacaoVO(1, 15, "asc"));

		return filtro;
	}

	@Test
	public void obterMovimentosAprovacaoStatusAprovacao() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setStatusAprovacao(StatusAprovacao.APROVADO);

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaTipoMovimento() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.TIPO_MOVIMENTO);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaDataMovimento() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.DATA_MOVIMENTO);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaNumeroCota() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.NUMERO_COTA);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaNomeCota() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.NOME_COTA);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaValor() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.VALOR);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaParcelas() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.PARCELAS);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaPrazo() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.PRAZO);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaRequerente() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.REQUERENTE);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterMovimentosAprovacaoOrdecacaoColunaStatus() {

		FiltroControleAprovacaoDTO filtro = getFiltro();
		filtro.setOrdenacaoColuna(FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao.STATUS);
				

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = this.movimentoRepository
				.obterMovimentosAprovacao(filtro);

		Assert.assertNotNull(listaControleAprovacaoDTO);

	}
	
	@Test
	public void obterTotalMovimentosAprovacao() {

		FiltroControleAprovacaoDTO filtro = getFiltro();

		this.movimentoRepository.obterTotalMovimentosAprovacao(filtro);

	}
	
}
