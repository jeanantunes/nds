package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.VendaProdutoEncalheRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class VendaProdutoEncalheRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private VendaProdutoEncalheRepository vendaProdutoEncalheRepository;

	@SuppressWarnings("unused")
	@Test
	public void buscarQntVendaEncalhe() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);

		Long quantidade = vendaProdutoEncalheRepository
				.buscarQntVendaEncalhe(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void buscarQntVendaEncalhePorTipoVendaEncalhe() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(TipoVendaEncalhe.ENCALHE);

		Long quantidade = vendaProdutoEncalheRepository
				.buscarQntVendaEncalhe(filtro);

	}

	@Test
	public void buscarVendasEncalheDTO() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(null);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOPaginacao() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(null);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaCodigoProduto() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaData() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.DATA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaNomeCota() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaNomeProduto() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaNumeroCota() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaNumeroEdicao() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_EDICAO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaPrecoCapa() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.PRECO_CAPA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaPrecoDesconto() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.PRECO_DESCONTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaQntProduto() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.QNT_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaTipoVenda() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.TIPO_VENDA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOOrdenacaoColunaTotalVenda() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.TOTAL_VENDA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOPorPaginacaoOrdenacao() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(null);
		filtro.setOrdenacaoColuna(null);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@Test
	public void buscarVendasEncalheDTOPorVendaEncalhe() {
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO();
		filtro.setTipoVendaEncalhe(TipoVendaEncalhe.ENCALHE);
		filtro.setOrdenacaoColuna(null);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(null);

		List<VendaEncalheDTO> vendasEncalheDTO = vendaProdutoEncalheRepository
				.buscarVendasEncalheDTO(filtro);

		Assert.assertNotNull(vendasEncalheDTO);

	}

	@SuppressWarnings("unused")
	@Test
	public void buscarVendaProdutoEncalhe(){
		VendaEncalheDTO vendaEncalheDTO = vendaProdutoEncalheRepository.buscarVendaProdutoEncalhe(1L);
	}
	
	
	
}
