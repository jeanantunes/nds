package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO.OrdenacaoColunasStatusCota;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.PeriodoVO;

public class HistoricoSituacaoCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	private Date dataInicialPeriodo = DateUtil.parseDataPTBR("01/01/2011");
	
	private Date dataFinalPeriodo = DateUtil.parseDataPTBR("31/12/2011");
	
	private Integer numeroCota = 123;
	
	private SituacaoCadastro situacaoCota = SituacaoCadastro.ATIVO;
	
	private static final Integer QTDE_TOTAL_HISTORICOS = 10;
	
	@Before
	public void setup() {
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		
		Pessoa pessoaManoel = Fixture.pessoaFisica("123.456.789-00", "manoel@mail.com", "Manoel da Silva");
		
		Cota cotaManoel = Fixture.cota(this.numeroCota, pessoaManoel, this.situacaoCota, box1);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		
		super.save(box1, pessoaManoel, cotaManoel, usuarioJoao);
		
		for (int i = 1; i <= QTDE_TOTAL_HISTORICOS; i++) {
			
			String dataInicial = "/01/2011";
			
			String dataFinal = "/05/2011";
			
			HistoricoSituacaoCota historicoSituacaoCota = new HistoricoSituacaoCota();
			
			historicoSituacaoCota.setCota(cotaManoel);
			historicoSituacaoCota.setDataEdicao(new Date());
			historicoSituacaoCota.setDescricao("Descrição " + i);
			historicoSituacaoCota.setMotivo(MotivoAlteracaoSituacao.INCIDENTES);
			historicoSituacaoCota.setNovaSituacao(this.situacaoCota);
			historicoSituacaoCota.setSituacaoAnterior(SituacaoCadastro.SUSPENSO);
			historicoSituacaoCota.setTipoEdicao(TipoEdicao.ALTERACAO);
			historicoSituacaoCota.setDataInicioValidade(DateUtil.parseDataPTBR(i + dataInicial));
			historicoSituacaoCota.setDataFimValidade(DateUtil.parseDataPTBR(i + dataFinal));
			historicoSituacaoCota.setResponsavel(usuarioJoao);
			
			super.save(historicoSituacaoCota);
		}
	}
	
	@Test
	public void obterHistoricoStatusCotaSemFiltro() {
		
		List<HistoricoSituacaoCota> listaHistoricoSituacaoCota =
			this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(null);
		
		Assert.assertNotNull(listaHistoricoSituacaoCota);
	}
	
	@Test
	public void obterHistoricoStatusCotaComFiltroSemPaginacao() {
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		
		filtro.setNumeroCota(this.numeroCota);
		filtro.setStatusCota(this.situacaoCota);
		filtro.setPeriodo(new PeriodoVO(this.dataInicialPeriodo, this.dataFinalPeriodo));
		filtro.setMotivoStatusCota(MotivoAlteracaoSituacao.INCIDENTES);
		
		List<HistoricoSituacaoCota> listaHistoricoSituacaoCota =
			this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoSituacaoCota);
		
		int tamanhoListaEsperado = 10;
		
		Assert.assertEquals(tamanhoListaEsperado, listaHistoricoSituacaoCota.size());
	}
	
	@Test
	public void obterHistoricoStatusCotaComFiltroComPaginacao() {
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		
		filtro.setNumeroCota(this.numeroCota);
		filtro.setStatusCota(this.situacaoCota);
		filtro.setPeriodo(new PeriodoVO(this.dataInicialPeriodo, this.dataFinalPeriodo));
		filtro.setMotivoStatusCota(MotivoAlteracaoSituacao.INCIDENTES);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.DATA);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(5);
		paginacao.setOrdenacao(Ordenacao.DESC);
		
		filtro.setPaginacao(paginacao);
		
		List<HistoricoSituacaoCota> listaHistoricoSituacaoCota =
			this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoSituacaoCota);
		
		int tamanhoListaEsperado = 5;
		
		Assert.assertEquals(tamanhoListaEsperado, listaHistoricoSituacaoCota.size());
	}
	
	@Test
	public void obterTotalHistoricoStatusCota() {
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		
		filtro.setNumeroCota(this.numeroCota);
		filtro.setStatusCota(this.situacaoCota);
		filtro.setPeriodo(new PeriodoVO(this.dataInicialPeriodo, this.dataFinalPeriodo));
		filtro.setMotivoStatusCota(MotivoAlteracaoSituacao.INCIDENTES);
		
		long totalHistoricoStatusCota =
			this.historicoSituacaoCotaRepository.obterTotalHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(totalHistoricoStatusCota);
		
		Assert.assertEquals(QTDE_TOTAL_HISTORICOS.longValue(), totalHistoricoStatusCota);
	}

}
