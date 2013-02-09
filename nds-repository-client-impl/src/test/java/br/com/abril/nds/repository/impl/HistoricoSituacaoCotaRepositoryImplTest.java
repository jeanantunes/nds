package br.com.abril.nds.repository.impl;

import java.util.Calendar;
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
	
//	TESTE SEM USO DE MASSA
	
//	Testes de condições método criarQueryHistoricoStatusCota() com obterHistoricoStatusCota()
	
//	NumeroCota
	@Test
	public void testarCriarQueryHistoricoStatusCotaNumeroCota() {
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setNumeroCota(1);
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	StatusCota
	@Test
	public void testarCriarQueryHistoricoStatusCotaStatusCota() {
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();		
		filtro.setStatusCota(SituacaoCadastro.ATIVO);
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	Periodo
	@Test
	public void testarCriarQueryHistoricoStatusCotaPeriodo() {
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();	
		
		Calendar d = Calendar.getInstance();
		Date dataInicial = d.getTime(); 
		Date dataFinal = d.getTime();
		
		PeriodoVO periodo = new PeriodoVO(dataInicial,dataFinal);
		filtro.setPeriodo(periodo);
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	MotivoStatusCota
	@Test
	public void testarCriarQueryHistoricoStatusCotaMotivoStatusCota() {
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();			
		filtro.setMotivoStatusCota(MotivoAlteracaoSituacao.CHAMADAO);
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
	
//	Testes de condições método adicionarOrdenacaoQueryHistoricoStatusCota() com obterHistoricoStatusCota()
	
//	DATA
	@Test
	public void testarCriarQueryHistoricoStatusCotaDATA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.DATA);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	DESCRICAO
	@Test
	public void testarCriarQueryHistoricoStatusCotaDESCRICAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.DESCRICAO);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	MOTIVO
	@Test
	public void testarCriarQueryHistoricoStatusCotaMOTIVO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.MOTIVO);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	STATUS_ANTERIOR
	@Test
	public void testarCriarQueryHistoricoStatusCotaSTATUSANTERIOR() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.STATUS_ANTERIOR);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	STATUS_ATUALIZADO
	@Test
	public void testarCriarQueryHistoricoStatusCotaSTATUSATUALIZADO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.STATUS_ATUALIZADO);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
//	USUARIO
	@Test
	public void testarCriarQueryHistoricoStatusCotaUSUARIO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunasStatusCota.USUARIO);
		
		
		listaHistoricoStatusCota = historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaHistoricoStatusCota);
		
	}
	
	@Test
	public void testarObterUltimoHistoricoInativo() {
		
		HistoricoSituacaoCota historicoSituacaoCota;
		
		Integer numeroCota = 1;
		
		historicoSituacaoCota = historicoSituacaoCotaRepository.obterUltimoHistoricoInativo(numeroCota);
		
//		Assert.assertNull(historicoSituacaoCota);
		
	}
	
	@Test
	public void testarBuscarUltimaSuspensaoCotasDia() {
		
		Date ultimaSuspencao;
		
		Calendar d = Calendar.getInstance();
		Date dataOperacao = d.getTime();
		
		ultimaSuspencao = historicoSituacaoCotaRepository.buscarUltimaSuspensaoCotasDia(dataOperacao);
		
//		Assert.assertNull(ultimaSuspencao);
		
	}
	
	@Test
	public void testarBuscarDataUltimaSuspensaoCotas() {
		
		Date dataUltimaSuspencao;
		
		dataUltimaSuspencao = historicoSituacaoCotaRepository.buscarDataUltimaSuspensaoCotas();
		
		Assert.assertNotNull(dataUltimaSuspencao);
		
	}
	
	@Test
	public void testarObterUltimoHistoricoStatusCota() {
		
		List<HistoricoSituacaoCota> listaUltimoHistorico;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		
		listaUltimoHistorico = historicoSituacaoCotaRepository.obterUltimoHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(listaUltimoHistorico);
		
	}
	
	@Test
	public void testarObterTotalUltimoHistoricoStatusCota() {
		
		Long totalUltimoHistorico;
		
		FiltroStatusCotaDTO filtro = new FiltroStatusCotaDTO();
		
		totalUltimoHistorico = historicoSituacaoCotaRepository.obterTotalUltimoHistoricoStatusCota(filtro);
		
		Assert.assertNotNull(totalUltimoHistorico);
		
	}
	
	
	

}
