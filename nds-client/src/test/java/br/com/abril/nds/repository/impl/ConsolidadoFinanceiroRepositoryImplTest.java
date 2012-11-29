package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ConsolidadoFinanceiroRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;

	@Test
	public void testarVerificarConsodidadoCotaPorDataoperacao() {

		boolean verificaConsididado;

		Long idCota = 1L;

		verificaConsididado = consolidadoFinanceiroRepository
				.verificarConsodidadoCotaPorDataOperacao(idCota);

		Assert.assertFalse(verificaConsididado);

	}

	@Test
	public void testarObterMovimentoEstoqueCotaEncalhe() {

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	
	// CODIGO_PRODUTO
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaCODIGOPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// NOME_PRODUTO
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaNOMEPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_PRODUTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// PRECO_CAPA
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaPRECOCAPA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.PRECO_CAPA);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// PRECO_COM_DESCONTO
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaPRECOCOMDESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.PRECO_COM_DESCONTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// ENCALHE
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaENCALHE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.ENCALHE);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// FORNECEDOR
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaFORNECEDOR() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.FORNECEDOR);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// TOTAL
	@Test
	public void testarObterMovimentoEstoqueCotaEncalheOrdenacaoColunaTOTAL() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<EncalheCotaDTO> movimentosEstoque;

		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.TOTAL);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaEncalhe(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	@Test
	public void testarObterMovimentoVendaEncalhe() {

		List<ConsultaVendaEncalheDTO> movimentosVenda;

		FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();

		movimentosVenda = consolidadoFinanceiroRepository
				.obterMovimentoVendaEncalhe(filtro);

		Assert.assertNotNull(movimentosVenda);

	}

	@Test
	public void testarObterMovimentoVendaEncalheOrdenacaoColuna() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsultaVendaEncalheDTO> movimentosVenda;

		FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoVendaCotaDTO.OrdenacaoColuna.codigoProduto);

		movimentosVenda = consolidadoFinanceiroRepository
				.obterMovimentoVendaEncalhe(filtro);

		Assert.assertNotNull(movimentosVenda);

	}

	@Test
	public void testarObterMovimentoEstoqueCotaConsignado() {

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// CODIGO_PRODUTO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaCODIGOPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.CODIGO_PRODUTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// NOME_PRODUTO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaNOMEPRODUTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.NOME_PRODUTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// NUMERO_EDICAO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaNUMEROEDICAO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.NUMERO_EDICAO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// PRECO_CAPA
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaPRECOCAPA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.PRECO_CAPA);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// PRECO_COM_DESCONTO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaPRECOCOMDESCONTO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.PRECO_COM_DESCONTO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// REPARTE_SUGERIDO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaREPARTESUGERIDO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.REPARTE_SUGERIDO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// REPARTE_FINAL
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaREPARTEFINAL() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.REPARTE_FINAL);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// DIFERENCA
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaDIFERENCA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.DIFERENCA);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// MOTIVO
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaMOTIVO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.MOTIVO);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// FORNECEDOR
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaFORNECEDOR() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.FORNECEDOR);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	// TOTAL
	@Test
	public void testarObterMovimentoEstoqueCotaConsignadoOrdenacaoColunaTOTAL() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "p.codigo");

		List<ConsignadoCotaDTO> movimentosEstoque;

		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(FiltroConsolidadoConsignadoCotaDTO.OrdenacaoColuna.TOTAL);

		movimentosEstoque = consolidadoFinanceiroRepository
				.obterMovimentoEstoqueCotaConsignado(filtro);

		Assert.assertNotNull(movimentosEstoque);

	}

	@Test
	public void testarObterConsolidadoPorIdMovimentoFinanceiro() {

		ConsolidadoFinanceiroCota consolidadoFinanceirocota;

		Long idMovimentoFinanceiro = 1L;

		consolidadoFinanceirocota = consolidadoFinanceiroRepository
				.obterConsolidadoPorIdMovimentoFinanceiro(idMovimentoFinanceiro);

//		Assert.assertNull(consolidadoFinanceirocota);

	}
	
	@Test
	public void testarBuscarUltimaDividaGeradaDia() {
		
		Date dataUltimaDivida;
		
		Calendar data = Calendar.getInstance();
		Date dataOperacao = data.getTime();
		
		dataUltimaDivida = consolidadoFinanceiroRepository.buscarUltimaDividaGeradaDia(dataOperacao);
		
//		Assert.assertNull(dataUltimaDivida);
		
	}
	
	@Test
	public void testarBuscarDiaUltimaDividaGerada() {
		
		Date dataUltimaDivida;
		
		dataUltimaDivida = consolidadoFinanceiroRepository.buscarDiaUltimaDividaGerada();
		
//		Assert.assertNull(dataUltimaDivida);
		
	}
	
	@Test
	public void testarObterQuantidadeDividasGeradasData() {
		
		Long quantidadeDividas;
		
		Calendar d = Calendar.getInstance();
		Date data = d.getTime();
		
		quantidadeDividas = consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(data);
		
		Assert.assertNotNull(quantidadeDividas);
		
	}
	
	@Test
	public void buscarPorCotaEData() {
		
		Cota cota = new Cota();
		cota.setId(1L);
		

		java.sql.Date data = new java.sql.Date(Fixture.criarData(14, Calendar.NOVEMBER, 2012).getTime());

		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = 
				consolidadoFinanceiroRepository.buscarPorCotaEData(cota, data);
		
	}
	

}
