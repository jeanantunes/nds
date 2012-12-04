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
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida.TipoDividaFechamentoDia;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoReparte;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota.TipoSituacaoCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.FechamentoDiarioRepository;
import br.com.abril.nds.util.DateUtil;

public class FechamentoDiarioRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoDiarioRepository diarioRepository;
	
	private ProdutoEdicao produtoEdicaoVeja;

	private Usuario usuario;
	
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
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
	}
	
	@Test
	public void testeInclusaoReparte(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(new Date(), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		
		Assert.assertNotNull(fechamentoDiario);
		
		FechamentoDiarioConsolidadoReparte consolidadoReparte 
				= Fixture.historicoFechamentoDiarioConsolidadoReparte(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoReparte = merge(consolidadoReparte);
		
		Assert.assertNotNull(consolidadoReparte);
		
		FechamentoDiarioLancamentoReparte historicoMovimentoReparte 
				= Fixture.historicoFechamentoDiarioLancamentoReparte(produtoEdicaoVeja, consolidadoReparte, 10, 10, 10, 10, 10, 10, 10, 10);
		
		historicoMovimentoReparte = merge(historicoMovimentoReparte);
		
		Assert.assertNotNull(fechamentoDiario);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario.getConsolidadoReparte());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoReparte().getLancamentosReparte());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoReparte().getLancamentosReparte().isEmpty());
	}
	
	@Test
	public void testeInclusaoEncalhe(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 1), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe 
				= Fixture.historicoFechamentoDiarioConsolidadoEncalhe(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, 
															  BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoEncalhe = merge(consolidadoEncalhe);
		Assert.assertNotNull(consolidadoEncalhe);
		
		FechamentoDiarioLancamentoEncalhe historicoMovimentoEncalhe 
				= Fixture.historicoFechamentoDiarioLancamentoEncalhe(produtoEdicaoVeja, 10, 23);
		
		historicoMovimentoEncalhe.setFechamentoDiarioConsolidadoEncalhe(consolidadoEncalhe);
		historicoMovimentoEncalhe = merge(historicoMovimentoEncalhe);
		Assert.assertNotNull(historicoMovimentoEncalhe);
		
		FechamentoDiarioMovimentoVendaEncalhe encalhe 
				= Fixture.historicoFechamentoDiarioMovimentoVendaEncalhe(produtoEdicaoVeja, BigInteger.ZERO, BigDecimal.TEN, new Date());
		
		encalhe.setFechamentoDiarioConsolidadoEncalhe(consolidadoEncalhe);
		encalhe = merge(encalhe);
		Assert.assertNotNull(encalhe);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe().getLancamentosReparte());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoEncalhe().getLancamentosReparte().isEmpty());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoEncalhe().getMovimentoVendaEncalhes());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoEncalhe().getMovimentoVendaEncalhes().isEmpty());
		
	}
	
	@Test
	public void testeInclusaoSuplementar(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 2), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar 
				= Fixture.historicoFechamentoDiarioConsolidadoSuplementar(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consolidadoSuplementar = merge(consolidadoSuplementar);
		Assert.assertNotNull(consolidadoSuplementar);
		
		FechamentoDiarioLancamentoSuplementar historicoMovimentoSuplementar 
				= Fixture.historicoFechamentoDiarioLancamentoSuplementar(produtoEdicaoVeja, new Long(10));
		historicoMovimentoSuplementar.setFechamentoDiarioConsolidadoSuplementar(consolidadoSuplementar);
		
		historicoMovimentoSuplementar = merge(historicoMovimentoSuplementar);
		Assert.assertNotNull(historicoMovimentoSuplementar);
		
		FechamentoDiarioMovimentoVendaSuplementar suplementar 
				= Fixture.historicoFechamentoDiarioMovimentoVendaSuplementar(produtoEdicaoVeja, BigInteger.TEN, BigDecimal.TEN, new Date());
		suplementar.setFechamentoDiarioConsolidadoSuplementar(consolidadoSuplementar);
		
		suplementar = merge(suplementar);
		Assert.assertNotNull(suplementar);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar().getLancamentosSuplementar());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoSuplementar().getLancamentosSuplementar().isEmpty());
		Assert.assertNotNull(fechamentoDiario.getConsolidadoSuplementar().getmovimentoVendaSuplemetares());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoSuplementar().getmovimentoVendaSuplemetares().isEmpty());
		
	}
	
	@Test
	public void testeInclusaoDividaAReceber(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_RECEBER);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoDividas());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoDividas().isEmpty());
		
		for(FechamentoDiarioConsolidadoDivida historico : fechamentoDiario.getConsolidadoDividas()){
			Assert.assertEquals(TipoDividaFechamentoDia.A_RECEBER, historico.getTipoDivida());
		}
	}
	
	
	@Test
	public void testeInclusaoDividaAVencer(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		inserirDividas(fechamentoDiario, TipoDividaFechamentoDia.A_VENCER);
		
		fechamentoDiario = diarioRepository.buscarPorId(fechamentoDiario.getId());
		
		Assert.assertNotNull(fechamentoDiario.getConsolidadoDividas());
		Assert.assertTrue(!fechamentoDiario.getConsolidadoDividas().isEmpty());
		
		for(FechamentoDiarioConsolidadoDivida historico : fechamentoDiario.getConsolidadoDividas()){
			Assert.assertEquals(TipoDividaFechamentoDia.A_VENCER, historico.getTipoDivida());
		}
	}
	
	@Test
	public void testeInclusaoDividas(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
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
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		FechamentoDiarioConsolidadoCota consolidadoCota 
				= Fixture.historicoFechamentoDiarioConsolidadoCota(fechamentoDiario, 10, 20, 30, 40, 50);
		
		consolidadoCota = merge(consolidadoCota);
		Assert.assertNotNull(consolidadoCota);
		
		FechamentoDiarioCota movimentoCotaAusenteEncalhe = Fixture.historicoFechamentoDiarioCota("José", 123, TipoSituacaoCota.AUSENTE_ENCALHE);
		movimentoCotaAusenteEncalhe.setFechamentoDiarioConsolidadoCota(consolidadoCota);
		
		movimentoCotaAusenteEncalhe = merge(movimentoCotaAusenteEncalhe);
		Assert.assertNotNull(movimentoCotaAusenteEncalhe);
		
		FechamentoDiarioCota movimentoCotaAusenteReparte = Fixture.historicoFechamentoDiarioCota("José Mane", 1234, TipoSituacaoCota.AUSENTE_REPARTE);
		movimentoCotaAusenteReparte.setFechamentoDiarioConsolidadoCota(consolidadoCota);
		
		movimentoCotaAusenteReparte = merge(movimentoCotaAusenteReparte);
		Assert.assertNotNull(movimentoCotaAusenteReparte);
		
		FechamentoDiarioCota movimentoCotaInativas = Fixture.historicoFechamentoDiarioCota("Maria", 12345, TipoSituacaoCota.INATIVAS);
		movimentoCotaInativas.setFechamentoDiarioConsolidadoCota(consolidadoCota);
		
		movimentoCotaInativas = merge(movimentoCotaInativas);
		Assert.assertNotNull(movimentoCotaInativas);
		
		FechamentoDiarioCota movimentoCotaNovas = Fixture.historicoFechamentoDiarioCota("Tião", 345, TipoSituacaoCota.NOVAS);
		movimentoCotaNovas.setFechamentoDiarioConsolidadoCota(consolidadoCota);
		
		movimentoCotaNovas = merge(movimentoCotaNovas);
		Assert.assertNotNull(movimentoCotaNovas);
	}
	
	@Test
	public void testeInclusaoEstoque(){
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
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
		
		FechamentoDiario fechamentoDiario = Fixture.fechamentoDiario(DateUtil.adicionarDias(new Date(), 3), usuario);
		
		fechamentoDiario = merge(fechamentoDiario);
		Assert.assertNotNull(fechamentoDiario);
		
		FechamentoDiarioResumoConsignado consignado = Fixture.getValorConsignado(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
		consignado = merge(consignado);
		Assert.assertNotNull(consignado);
		
    	FechamentoDiarioResumoAvista  valorAvista = Fixture.getValorAvista(fechamentoDiario, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		
    	valorAvista = merge(valorAvista);
		Assert.assertNotNull(valorAvista);
	}

	private void incluirResumoEstoque(TipoEstoque tipoEstoque,FechamentoDiario fechamentoDiario) {
		
		FechamentoDiarioResumoEstoque resumoLancamento = Fixture.historicoFechamentoDiarioResumoEstoque(10, 20, BigDecimal.TEN, tipoEstoque);
		resumoLancamento.setFechamentoDiario(fechamentoDiario);
		
		resumoLancamento = merge(resumoLancamento);
		Assert.assertNotNull(resumoLancamento);
	}
	

	private void inserirDividas(FechamentoDiario fechamentoDiario, TipoDividaFechamentoDia tipoDividaFechamentoDia) {
		
		FechamentoDiarioConsolidadoDivida fechamentoDiarioConsolidadoDivida 
				= Fixture.historicoFechamentoDiarioConsolidadoDivida(fechamentoDiario, tipoDividaFechamentoDia);
		
		fechamentoDiarioConsolidadoDivida = merge(fechamentoDiarioConsolidadoDivida);
		Assert.assertNotNull(fechamentoDiarioConsolidadoDivida);
		
		FechamentoDiarioDivida historicoFechamentoDiarioMovimentoDivida 
				= Fixture.historicoFechamentoDiarioDivida("Banco Brasil", new Date(), TipoCobranca.BOLETO, "Jose", 10L, "781023456", 123, BigDecimal.ZERO);
		historicoFechamentoDiarioMovimentoDivida.setFechamentoDiarioConsolidadoDivida(fechamentoDiarioConsolidadoDivida); 
		
		historicoFechamentoDiarioMovimentoDivida = merge(historicoFechamentoDiarioMovimentoDivida);
		Assert.assertNotNull(historicoFechamentoDiarioMovimentoDivida);
		
		FechamentoDiarioResumoConsolidadoDivida movimentoConsolidadoDivida
				= Fixture.historicoFechamentoDiarioResumoConsolidadoDivida(TipoCobranca.BOLETO, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		movimentoConsolidadoDivida.setFechamentoDiarioConsolidadoDivida(fechamentoDiarioConsolidadoDivida);
		
		movimentoConsolidadoDivida = merge(movimentoConsolidadoDivida);
		Assert.assertNotNull(movimentoConsolidadoDivida);
	}
}
