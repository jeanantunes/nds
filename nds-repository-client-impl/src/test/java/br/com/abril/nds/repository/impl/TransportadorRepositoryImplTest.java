package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TransportadorRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class TransportadorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TransportadorRepository transportadorRepository;

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadoras() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CNPJ);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);

		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorRazaSocial() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setRazaoSocial("Teste");
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CNPJ);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorNomeFantasia() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setNomeFantasia("Teste");
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CNPJ);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorCnpj() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setCnpj("1234567889");
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CNPJ);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoCodigo() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CODIGO);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoCnpj() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CNPJ);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoEmail() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.EMAIL);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoRazaoSocial() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.RAZAO_SOCIAL);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoResponsavel() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.RESPONSAVEL);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTransportadorasPorOrdenacaoTelefone() {
		FiltroConsultaTransportadorDTO filtro = new FiltroConsultaTransportadorDTO();
		filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.TELEFONE);
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
		filtro.getPaginacaoVO().setPaginaAtual(2);
		ConsultaTransportadorDTO consulta = transportadorRepository
				.pesquisarTransportadoras(filtro);

	}

	@SuppressWarnings("unused")
	@Test
	public void buscarTransportadorPorCNPJ() {
		Transportador transportador = transportadorRepository
				.buscarTransportadorPorCNPJ("123456789");

	}

	@Test
	public void buscarCotasAtendidadasSortnameNumeroCota() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "numeroCota");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarCotasAtendidadasSortnameNomeCota() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "nomeCota");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarCotasAtendidadasSortnameBox() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "box");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarCotasAtendidadasSortnameRoteiro() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "roteiro");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarCotasAtendidadasSortnameRota() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "rota");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarCotasAtendidadasOrdenacaoASC() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "ASC", "rota");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarCotasAtendidadasOrdenacaoDESC() {
		List<CotaAtendidaTransportadorVO> lista = transportadorRepository
				.buscarCotasAtendidadas(1L, "DESC", "rota");

		Assert.assertNotNull(lista);
	}
}
