package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;

public class VisaoEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;

	@Test
	public void obterDetalhe() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(-1L);
		// filtro.setDataMovimentacao(new Date());

		List<VisaoEstoqueDetalheDTO> list = visaoEstoqueRepository
				.obterVisaoEstoqueDetalhe(filtro);

		Assert.assertTrue(list != null);
	}

	@Test
	public void obterDetalheIdPositivo() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);
		// filtro.setDataMovimentacao(new Date());

		List<VisaoEstoqueDetalheDTO> list = visaoEstoqueRepository
				.obterVisaoEstoqueDetalhe(filtro);

		Assert.assertTrue(list != null);
	}

	@Test
	public void obterVisaoEstoque() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(-1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueIdPositivo() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueTipoEstoqueLancamento() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueTipoEstoqueSuplementar() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueTipoEstoqueRecolhimento() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueTipoEstoqueProdutosDanificados() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository.obterVisaoEstoque(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueJuramentado() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
		filtro.setIdFornecedor(-1L);
		filtro.setDataMovimentacao(new Date());

		VisaoEstoqueDTO dto = visaoEstoqueRepository
				.obterVisaoEstoqueJuramentado(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueJuramentadoIdPositivo() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository
				.obterVisaoEstoqueJuramentado(filtro);

		Assert.assertTrue(dto != null);
	}

	@Test
	public void obterVisaoEstoqueDetalheJuramentado() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setDataMovimentacao(new Date());
		filtro.setIdFornecedor(-1L);

		List<VisaoEstoqueDetalheJuramentadoDTO> list = visaoEstoqueRepository
				.obterVisaoEstoqueDetalheJuramentado(filtro);

		Assert.assertNotNull(list);
	}
	
	@Test
	public void obterVisaoEstoqueDetalheJuramentadoIdPositivo() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setDataMovimentacao(new Date());
		filtro.setIdFornecedor(1L);

		List<VisaoEstoqueDetalheJuramentadoDTO> list = visaoEstoqueRepository
				.obterVisaoEstoqueDetalheJuramentado(filtro);

		Assert.assertNotNull(list);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterVisaoEstoqueHistorico() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setDataMovimentacao(new Date());
		filtro.setIdFornecedor(-1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository
				.obterVisaoEstoqueHistorico(filtro);

	}

	@Test
	public void obterVisaoEstoqueHistoricoIdPositivo() {

		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);

		VisaoEstoqueDTO dto = visaoEstoqueRepository
				.obterVisaoEstoqueHistorico(filtro);

		Assert.assertTrue(dto != null);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterVisaoEstoqueDetalheHistorico() {
		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(-1L);

		List<VisaoEstoqueDetalheDTO> dto = visaoEstoqueRepository
				.obterVisaoEstoqueDetalheHistorico(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterVisaoEstoqueDetalheHistoricoIdPositivo() {
		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);

		List<VisaoEstoqueDetalheDTO> dto = visaoEstoqueRepository
				.obterVisaoEstoqueDetalheHistorico(filtro);
	}
	
	

}
