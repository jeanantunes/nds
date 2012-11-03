package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoCota;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoDivida.TipoDividaFechamentoDia;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoEncalhe;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoReparte;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioConsolidadoSuplementar;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioCota;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioCota.TipoSituacaoCota;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioDivida;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioLancamentoReparte;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioResumoConsignado;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioResumoConsignado.TipoValor;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.HistoricoFechamentoDiarioResumoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.FechamentoDiarioRepository;
import br.com.abril.nds.util.DateUtil;

public class FechamentoDiarioRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoDiarioRepository diarioRepository;
	
	private ProdutoEdicao produtoEdicaoVeja;
	
	@Before
	public void setUp(){
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);

		produtoEdicaoVeja = Fixture.produtoEdicao(1L, 10, 14, new Long(100), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produto, null, false);
		save(produtoEdicaoVeja);
	}
	
	@Test
	public void testeInclusaoReparte(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(new Date(), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		
		Assert.assertNotNull(fechamentoDiario);
		
		HistoricoFechamentoDiarioConsolidadoReparte consolidadoReparte 
				= Fixture.historicoFechamentoDiarioConsolidadoReparte(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoReparte = merge(consolidadoReparte);
		
		Assert.assertNotNull(consolidadoReparte);
		
		HistoricoFechamentoDiarioLancamentoReparte historicoMovimentoReparte 
				= Fixture.historicoFechamentoDiarioLancamentoReparte(produtoEdicaoVeja, consolidadoReparte, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN);
		
		historicoMovimentoReparte = merge(historicoMovimentoReparte);
		
		Assert.assertNotNull(fechamentoDiario);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario.getConsolidadoReparte());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoReparte().getHistoricoLancamentosReparte());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoReparte().getHistoricoLancamentosReparte().isEmpty());
	}
	
	@Test
	public void testeInclusaoEncalhe(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 1), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		HistoricoFechamentoDiarioConsolidadoEncalhe consolidadoEncalhe 
				= Fixture.historicoFechamentoDiarioConsolidadoEncalhe(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, 
															  BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoEncalhe = merge(consolidadoEncalhe);
		Assert.assertNotNull(consolidadoEncalhe);
		
		HistoricoFechamentoDiarioLancamentoEncalhe historicoMovimentoEncalhe 
				= Fixture.historicoFechamentoDiarioLancamentoEncalhe(produtoEdicaoVeja, BigInteger.ONE, BigInteger.ONE);
		
		historicoMovimentoEncalhe.setHistoricoConsolidadoEncalhe(consolidadoEncalhe);
		historicoMovimentoEncalhe = merge(historicoMovimentoEncalhe);
		Assert.assertNotNull(historicoMovimentoEncalhe);
		
		HistoricoFechamentoDiarioMovimentoVendaEncalhe encalhe 
				= Fixture.historicoFechamentoDiarioMovimentoVendaEncalhe(produtoEdicaoVeja, BigInteger.ZERO, BigDecimal.TEN, new Date());
		
		encalhe.setHistoricoConsolidadoEncalhe(consolidadoEncalhe);
		encalhe = merge(encalhe);
		Assert.assertNotNull(encalhe);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe().getHistoricoLancamentosReparte());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoEncalhe().getHistoricoLancamentosReparte().isEmpty());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe().getHistoricoMovimentoVendaEncalhes());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoEncalhe().getHistoricoMovimentoVendaEncalhes().isEmpty());
		
	}
	
	@Test
	public void testeInclusaoSuplementar(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 2), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		HistoricoFechamentoDiarioConsolidadoSuplementar consolidadoSuplementar 
				= Fixture.historicoFechamentoDiarioConsolidadoSuplementar(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoSuplementar = merge(consolidadoSuplementar);
		Assert.assertNotNull(consolidadoSuplementar);
		
		HistoricoFechamentoDiarioLancamentoSuplementar historicoMovimentoSuplementar 
				= Fixture.historicoFechamentoDiarioLancamentoSuplementar(produtoEdicaoVeja, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN);
		historicoMovimentoSuplementar.setHistoricoConsolidadoSuplementar(consolidadoSuplementar);
		
		historicoMovimentoSuplementar = merge(historicoMovimentoSuplementar);
		Assert.assertNotNull(historicoMovimentoSuplementar);
		
		HistoricoFechamentoDiarioMovimentoVendaSuplementar suplementar 
				= Fixture.historicoFechamentoDiarioMovimentoVendaSuplementar(produtoEdicaoVeja, BigInteger.TEN, BigDecimal.TEN, new Date());
		suplementar.setHistoricoConsolidadoSuplementar(consolidadoSuplementar);
		
		suplementar = merge(suplementar);
		Assert.assertNotNull(suplementar);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar().getHistoricoLancamentosSuplementar());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoSuplementar().getHistoricoLancamentosSuplementar().isEmpty());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar().getHistoricoMovimentoVendaSuplemetares());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoSuplementar().getHistoricoMovimentoVendaSuplemetares().isEmpty());
		
	}
	
	@Test
	public void testeInclusaoDividaAReceber(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_RECEBER);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoDividas());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoDividas().isEmpty());
		
		for(HistoricoFechamentoDiarioConsolidadoDivida historico : fechamentoDiario.getConsolidadoDividas()){
			Assert.assertEquals(TipoDividaFechamentoDia.A_RECEBER, historico.getTipoDivida());
		}
	}
	
	
	@Test
	public void testeInclusaoDividaAVencer(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_VENCER);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoDividas());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoDividas().isEmpty());
		
		for(HistoricoFechamentoDiarioConsolidadoDivida historico : fechamentoDiario.getConsolidadoDividas()){
			Assert.assertEquals(TipoDividaFechamentoDia.A_VENCER, historico.getTipoDivida());
		}
	}
	
	@Test
	public void testeInclusaoDividas(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_VENCER);
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_RECEBER);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoDividas());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoDividas().isEmpty());
		Assert.assertTrue(fechamentoDiario.getConsolidadoDividas().size() == 2);
	}
	
	@Test
	public void testeInclusaoCotas(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		HistoricoFechamentoDiarioConsolidadoCota consolidadoCota 
				= Fixture.historicoFechamentoDiarioConsolidadoCota(fechamentoDiario, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
		
		consolidadoCota = merge(consolidadoCota);
		Assert.assertNotNull(consolidadoCota);
		
		HistoricoFechamentoDiarioCota movimentoCotaAusenteEncalhe = Fixture.historicoFechamentoDiarioCota("José", 123, TipoSituacaoCota.AUSENTE_ENCALHE);
		movimentoCotaAusenteEncalhe.setHistoricoConsolidadoCota(consolidadoCota);
		
		movimentoCotaAusenteEncalhe = merge(movimentoCotaAusenteEncalhe);
		Assert.assertNotNull(movimentoCotaAusenteEncalhe);
		
		HistoricoFechamentoDiarioCota movimentoCotaAusenteReparte = Fixture.historicoFechamentoDiarioCota("José Mane", 1234, TipoSituacaoCota.AUSENTE_REPARTE);
		movimentoCotaAusenteReparte.setHistoricoConsolidadoCota(consolidadoCota);
		
		movimentoCotaAusenteReparte = merge(movimentoCotaAusenteReparte);
		Assert.assertNotNull(movimentoCotaAusenteReparte);
		
		HistoricoFechamentoDiarioCota movimentoCotaInativas = Fixture.historicoFechamentoDiarioCota("Maria", 12345, TipoSituacaoCota.INATIVAS);
		movimentoCotaInativas.setHistoricoConsolidadoCota(consolidadoCota);
		
		movimentoCotaInativas = merge(movimentoCotaInativas);
		Assert.assertNotNull(movimentoCotaInativas);
		
		HistoricoFechamentoDiarioCota movimentoCotaNovas = Fixture.historicoFechamentoDiarioCota("Tião", 345, TipoSituacaoCota.NOVAS);
		movimentoCotaNovas.setHistoricoConsolidadoCota(consolidadoCota);
		
		movimentoCotaNovas = merge(movimentoCotaNovas);
		Assert.assertNotNull(movimentoCotaNovas);
	}
	
	@Test
	public void testeInclusaoEstoque(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		incluirResumoEstoque(TipoEstoque.LANCAMENTO,fechamentoDiario);
		
		incluirResumoEstoque(TipoEstoque.JURAMENTADO,fechamentoDiario);
		
		incluirResumoEstoque(TipoEstoque.SUPLEMENTAR,fechamentoDiario);
		
		incluirResumoEstoque(TipoEstoque.RECOLHIMENTO,fechamentoDiario);
		
		incluirResumoEstoque(TipoEstoque.DANIFICADO,fechamentoDiario);
	
	}
	
	@Test
	public void testeIncluirConsignado(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), null);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		incluirResumoConsignado(TipoValor.ENTRADA,fechamentoDiario);
		
		incluirResumoConsignado(TipoValor.SAIDA,fechamentoDiario);
		
		incluirResumoConsignado(TipoValor.SALDO_ANTERIOR,fechamentoDiario);
		
		incluirResumoConsignado(TipoValor.SALDO_ATUAL,fechamentoDiario);
	}

	private void incluirResumoConsignado(TipoValor tipoValor,FechamentoDiario fechamentoDiario) {
		
		HistoricoFechamentoDiarioResumoConsignado resumo = Fixture.historicoFechamentoDiarioResumoConsignado(tipoValor, BigDecimal.TEN, BigDecimal.TEN);
		resumo.setFechamentoDiario(fechamentoDiario);
	
		resumo = merge(resumo);
		Assert.assertNotNull(resumo);
	}

	private void incluirResumoEstoque(TipoEstoque tipoEstoque,FechamentoDiario fechamentoDiario) {
		
		HistoricoFechamentoDiarioResumoEstoque resumoLancamento = Fixture.historicoFechamentoDiarioResumoEstoque(BigInteger.TEN, BigInteger.TEN, BigDecimal.TEN, tipoEstoque);
		resumoLancamento.setFechamentoDiario(fechamentoDiario);
		
		resumoLancamento = merge(resumoLancamento);
		Assert.assertNotNull(resumoLancamento);
	}
	

	private void inserirDividas(FechamentoDiario fechamentoDiario, TipoDividaFechamentoDia tipoDividaFechamentoDia) {
		
		HistoricoFechamentoDiarioConsolidadoDivida fechamentoDiarioConsolidadoDivida 
				= Fixture.historicoFechamentoDiarioConsolidadoDivida(fechamentoDiario, tipoDividaFechamentoDia);
		
		fechamentoDiarioConsolidadoDivida = merge(fechamentoDiarioConsolidadoDivida);
		Assert.assertNotNull(fechamentoDiarioConsolidadoDivida);
		
		HistoricoFechamentoDiarioDivida historicoFechamentoDiarioMovimentoDivida 
				= Fixture.historicoFechamentoDiarioDivida("Banco Brasil", new Date(), TipoCobranca.BOLETO, "Jose", 10L, "781023456", 123, BigDecimal.ZERO);
		historicoFechamentoDiarioMovimentoDivida.setHistoricoConsolidadoDivida(fechamentoDiarioConsolidadoDivida); 
		
		historicoFechamentoDiarioMovimentoDivida = merge(historicoFechamentoDiarioMovimentoDivida);
		Assert.assertNotNull(historicoFechamentoDiarioMovimentoDivida);
		
		HistoricoFechamentoDiarioResumoConsolidadoDivida movimentoConsolidadoDivida
				= Fixture.historicoFechamentoDiarioResumoConsolidadoDivida(TipoCobranca.BOLETO, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		movimentoConsolidadoDivida.setHistoricoConsolidadoDivida(fechamentoDiarioConsolidadoDivida);
		
		movimentoConsolidadoDivida = merge(movimentoConsolidadoDivida);
		Assert.assertNotNull(movimentoConsolidadoDivida);
	}
}
