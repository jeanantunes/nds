package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.cfg.annotations.Nullability;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TipoImpressaoInformeEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class TipoNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	Long numeroNota = 2344242L;
	String serie = "345353543";
	
	@Before
	public void setup() {
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		
		Fornecedor fornecedor = Fixture.fornecedorFC(tipoFornecedor);
		save(fornecedor);
		
		CFOP cfop  = Fixture.cfop5102();
		save(cfop);

		TipoNotaFiscal tp = Fixture.tipoNotaFiscalDevolucao(cfop);
		save(tp);
		
		NotaFiscalEntrada notaFiscal = new NotaFiscalEntradaFornecedor();
		notaFiscal.setCfop(cfop);
		notaFiscal.setChaveAcesso(chave);
		notaFiscal.setNumero(numeroNota);
		notaFiscal.setSerie(serie);
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setOrigem(Origem.INTERFACE);
		notaFiscal.setValorBruto(BigDecimal.TEN);
		notaFiscal.setValorLiquido(BigDecimal.TEN);
		notaFiscal.setValorDesconto(BigDecimal.TEN);
		((NotaFiscalEntradaFornecedor)notaFiscal).setFornecedor(fornecedor);
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscal.setTipoNotaFiscal(tp);
		save(notaFiscal);
		
	}
	
	@Test
	public void obterTiposNotasFiscais() {
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.obterTiposNotasFiscais();
		
		Assert.assertNotNull(tipoNotaFiscal);
		
	}
	
	@Test
	public void obterTiposNotasFiscaisTipoOperacao() {
		TipoOperacao tipo =  TipoOperacao.ENTRADA;
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.obterTiposNotasFiscais(tipo);
		
		Assert.assertNotNull(tipoNotaFiscal);
		
	}
	
	@Test
	public void testObterTipoNotaFiscal() {
		
		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		
		Assert.assertTrue(tipoNotaFiscal != null);
		
	}
	

	@Test
	public void obterTipoNotaFiscalGrupoNotaFiscal() {
		
		GrupoNotaFiscal grupo = GrupoNotaFiscal.NF_TERCEIRO;
		
		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository.obterTipoNotaFiscal(grupo, null, false);
		
		
	}
	
	@Test
	public void obterTipoNotaFiscalTipoAtividade() {
		
		TipoAtividade tipo = TipoAtividade.MERCANTIL;
		
		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository.obterTipoNotaFiscal(null, tipo, false);
		
		
	}
	
	@Test
	public void obterTipoNotaFiscalContribuinte() {
		
		Boolean isContruinte = true;
		
		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository.obterTipoNotaFiscal(null, null, isContruinte);
		
		
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoNota() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
			
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividade() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadeColunaOrdenacaoOperacao() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.setOrdenacaoColuna(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.OPERACAO);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadeColunaOrdenacaoDescricao() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.setOrdenacaoColuna(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.DESCRICAO);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadeColunaOrdenacaoCFOP_ESTADO() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.setOrdenacaoColuna(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.CFOP_ESTADO);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);

	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadeColunaOrdenacaoCFOP_OUTROS_ESTADOS() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.setOrdenacaoColuna(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.CFOP_OUTROS_ESTADOS);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadePaginacaoOrdecacao() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota("entrada");
		filtro.setTipoAtividade(TipoAtividade.MERCANTIL);
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.setOrdenacaoColuna(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.CFOP_OUTROS_ESTADOS);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void consultarTipoNotaFiscalTipoAtividadePaginacao() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setPaginacao(new PaginacaoVO());		
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(2);
		
		
		List<TipoNotaFiscal> tipoNotaFiscal = tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);
		
		Assert.assertNotNull(tipoNotaFiscal);
	
	}
	
	@Test
	public void obterQuantidadeTiposNotasFiscaisTipoNota() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
			filtro.setTipoNota("saida");
		
			tipoNotaFiscalRepository.obterQuantidadeTiposNotasFiscais(filtro);
		
	}
	
	@Test
	public void obterQuantidadeTiposNotasFiscaisTipoAtividade() {
	
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
			filtro.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		
			tipoNotaFiscalRepository.obterQuantidadeTiposNotasFiscais(filtro);
		
	}
	
	@Test
	public void obterTiposNotasFiscaisCotasNaoContribuintesPor() {
		
		TipoAtividade tipo = TipoAtividade.MERCANTIL;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(tipo);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotasFiscaisPorTipoAtividadeDistribuidor() {
		
		TipoAtividade tipo = TipoAtividade.MERCANTIL;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(tipo);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotaFiscalGrupoNF() {
		
		GrupoNotaFiscal grupo = GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotaFiscal(grupo);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotaFiscalTipoOperacao() {
		
		TipoOperacao tipo = TipoOperacao.ENTRADA;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscais(tipo, null, null, null);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotaFiscalTipoDestinatario() {
		
		TipoUsuarioNotaFiscal tipoDestinatario = TipoUsuarioNotaFiscal.DISTRIBUIDOR;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscais(null,tipoDestinatario, null, null);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotaFiscalTipoEmitente() {
		
		TipoUsuarioNotaFiscal tipoEmitente = TipoUsuarioNotaFiscal.COTA;
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscais(null,null, tipoEmitente, null);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	@Test
	public void obterTiposNotaFiscalGrupoNotaFiscal() {

		GrupoNotaFiscal[] grupo = new GrupoNotaFiscal[2];
		grupo[0] = GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR;
		grupo[1] = GrupoNotaFiscal.NF_DEVOLUCAO_SIMBOLICA;
		
		
			List<TipoNotaFiscal> tipoNotaFiscal =  
					tipoNotaFiscalRepository.obterTiposNotasFiscais(null,null, null, grupo);
			
			Assert.assertNotNull(tipoNotaFiscal);
	}
	
	
}
