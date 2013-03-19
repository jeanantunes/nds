package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.repository.DistribuidorRepository;

public class DistribuidorRepositoryImplTest extends AbstractRepositoryImplTest {

	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	private Distribuidor distribuidor;
	private Fornecedor fornecedorFC; 
	private Fornecedor fornecedorDinap; 
	private DistribuicaoFornecedor dinapSegunda;
	private DistribuicaoFornecedor dinapQuarta;
	private DistribuicaoFornecedor dinapSexta;
	private DistribuicaoFornecedor fcSegunda;
	private DistribuicaoFornecedor fcSexta;
	private TipoFornecedor tipoFornecedorPublicacao;
	
	@Before
	public void setUp() {
			
		Banco banco = Fixture.hsbc(); 
		save(banco);
		
		
		PessoaJuridica pj = Fixture.pessoaJuridica("Distrib", "01.001.001/001-00",
				"000.000.000.00", "distrib@mail.com", "99.999-9");
		
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
  		save(parametroCobranca);
  		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE, parametroCobranca);
		save(formaBoleto);
		
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto,true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		distribuidor = Fixture.distribuidor(1, pj, new Date(), politicasCobranca);

		save(pj);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);
		
		dinapSegunda = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		dinapQuarta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		dinapSexta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);
		save(dinapSegunda, dinapQuarta, dinapSexta);

		fcSegunda = Fixture.distribuicaoFornecedor(
				fornecedorFC, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		fcSexta = Fixture.distribuicaoFornecedor(
				fornecedorFC, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);
		save(fcSegunda, fcSexta);
		
		for(TipoGarantia tipo:TipoGarantia.values()){
			save(Fixture.criarTipoGarantiaAceita(distribuidor, tipo));
		}
	}
	
	@Test
	public void obter() {
		Distribuidor distribuidorDB = distribuidorRepository.obter();
		Assert.assertNotNull(distribuidorDB);
		Assert.assertEquals(distribuidor.getId(), distribuidorDB.getId());
	}
	
	@Test
	public void buscarDiasDistribuicaoFornecedor() {
		List<Long> idsFornecedores = new ArrayList<Long>(2);
		idsFornecedores.add(fornecedorFC.getId());
		idsFornecedores.add(fornecedorDinap.getId());
		List<DistribuicaoFornecedor> resultado = distribuidorRepository
				.buscarDiasDistribuicaoFornecedor(idsFornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		Assert.assertNotNull(resultado);
		Assert.assertEquals(3, resultado.size());
		Assert.assertTrue(resultado.contains(dinapSegunda));
		Assert.assertTrue(resultado.contains(dinapQuarta));
		Assert.assertTrue(resultado.contains(fcSegunda));
		
	}	
	
	@SuppressWarnings("unused")
	@Test
	public void obterEnderecoPrincipal(){
		EnderecoDistribuidor endereco = distribuidorRepository.obterEnderecoPrincipal();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterTelefonePrincipal(){
		TelefoneDistribuidor telefone = distribuidorRepository.obterTelefonePrincipal();
	}
	
	@Test
	public void obtemTiposGarantiasAceitas(){
		List<TipoGarantia> garantias =  distribuidorRepository.obtemTiposGarantiasAceitas();
		
		Assert.assertNotNull(garantias);
		Assert.assertEquals(TipoGarantia.values().length, garantias.size());
		Assert.assertTrue(garantias.containsAll(Arrays.asList(TipoGarantia.values())));
	}

	@Test
	public void obterNomeCNPJDistribuidor(){
		List<String> lista = distribuidorRepository.obterNomeCNPJDistribuidor();
		Assert.assertNotNull(lista);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterInformacoesComplementaresProcuracao(){
		String informacoes = distribuidorRepository.obterInformacoesComplementaresProcuracao();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterRazaoSocialDistribuidor(){
		String razaoSocial = distribuidorRepository.obterRazaoSocialDistribuidor();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterInformacoesComplementaresTermoAdesao(){
		String informacoes = distribuidorRepository.obterInformacoesComplementaresTermoAdesao();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void buscarInicioSemana(){
		DiaSemana diaSemana = distribuidorRepository.buscarInicioSemana();
	}

	@Test
	public void obterDataOperacaoDistribuidor() {
		
		this.distribuidorRepository.obterDataOperacaoDistribuidor();
	}
	
	@Test
	public void obterDescontoCotaNegociacao(){
		
		this.distribuidorRepository.obterDescontoCotaNegociacao();
	}
	
	@Test
	public void utilizaGarantiaPdv(){
		
		this.distribuidorRepository.utilizaGarantiaPdv();
	}
	
	@Test
	public void aceitaJuramentado(){
		
		this.distribuidorRepository.aceitaJuramentado();
	}
	
	@Test
	public void qtdDiasEncalheAtrasadoAceitavel(){
		
		this.distribuidorRepository.qtdDiasEncalheAtrasadoAceitavel();
	}

	@Test
	public void obterNumeroDiasNovaCobranca() {
		
		this.distribuidorRepository.obterNumeroDiasNovaCobranca();
	}

	@Test
	public void utilizaControleAprovacao() {
		
		this.distribuidorRepository.utilizaControleAprovacao();
	}

	@Test
	public void utilizaTermoAdesao() {
		
		this.distribuidorRepository.utilizaTermoAdesao();
	}

	@Test
	public void utilizaProcuracaoEntregadores() {
		
		this.distribuidorRepository.utilizaProcuracaoEntregadores();
	}

	@Test
	public void utilizaSugestaoIncrementoCodigo() {
		
		this.distribuidorRepository.utilizaSugestaoIncrementoCodigo();
	}

	@Test
	public void getEmail() {
		
		this.distribuidorRepository.getEmail();
	}

	@Test
	public void parametrosRecolhimentoDistribuidor() {
		
		this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
	}

	@Test
	public void tipoImpressaoCE() {
		
		this.distribuidorRepository.tipoImpressaoCE();
	}

	@Test
	public void qntDiasVencinemtoVendaEncalhe() {
		
		this.distribuidorRepository.qntDiasVencinemtoVendaEncalhe();
	}

	@Test
	public void aceitaBaixaPagamentoVencido() {
		
		this.distribuidorRepository.aceitaBaixaPagamentoVencido();
	}

	@Test
	public void aceitaBaixaPagamentoMaior() {
		
		this.distribuidorRepository.aceitaBaixaPagamentoMaior();
	}

	@Test
	public void aceitaBaixaPagamentoMenor() {

		this.distribuidorRepository.aceitaBaixaPagamentoMenor();
	}

	@Test
	public void negociacaoAteParcelas() {
		
		this.distribuidorRepository.negociacaoAteParcelas();
	}

	@Test
	public void qtdDiasLimiteParaReprogLancamento() {
		
		this.distribuidorRepository.qtdDiasLimiteParaReprogLancamento();
	}

	@Test
	public void obrigacaoFiscal() {
		
		this.distribuidorRepository.obrigacaoFiscal();
	}

	@Test
	public void tipoImpressaoNENECADANFE() {
		
		this.distribuidorRepository.tipoImpressaoNENECADANFE();
	}

	@Test
	public void capacidadeRecolhimento() {
		
		this.distribuidorRepository.capacidadeRecolhimento();
	}

	@Test
	public void cidadeDistribuidor() {
		
		this.distribuidorRepository.cidadeDistribuidor();
	}

	@Test
	public void codigoDistribuidorDinap() {
		
		this.distribuidorRepository.codigoDistribuidorDinap();
	}

	@Test
	public void codigoDistribuidorFC() {
		
		this.distribuidorRepository.codigoDistribuidorFC();
	}

	@Test
	public void diasNegociacao() {
		
		this.distribuidorRepository.diasNegociacao();
	}

	@Test
	public void tipoContabilizacaoCE() {
		
		this.distribuidorRepository.tipoContabilizacaoCE();
	}

	@Test
	public void preenchimentoAutomaticoPDV() {
		
		this.distribuidorRepository.preenchimentoAutomaticoPDV();
	}

	@Test
	public void qntDiasReutilizacaoCodigoCota() {
		
		this.distribuidorRepository.qntDiasReutilizacaoCodigoCota();
	}

	@Test
	public void politicasCobranca() {
		
		this.distribuidorRepository.politicasCobranca();
	}

	@Test
	public void assuntoEmailCobranca() {
		
		this.distribuidorRepository.assuntoEmailCobranca();
	}

	@Test
	public void mensagemEmailCobranca() {
		
		this.distribuidorRepository.mensagemEmailCobranca();
	}

	@Test
	public void regimeEspecial() {
		
		this.distribuidorRepository.regimeEspecial();
	}

	@Test
	public void tipoAtividade() {
		
		this.distribuidorRepository.tipoAtividade();
	}

	@Test
	public void fatorRelancamentoParcial() {
		
		this.distribuidorRepository.fatorRelancamentoParcial();
	}

	@Test
	public void obterId() {
		
		this.distribuidorRepository.obterId();
	}
	
	@Test
	public void cnpj(){
		
		this.distribuidorRepository.cnpj();
	}
	
	@Test
	public void parametrosDistribuidorEmissaoDocumentos(){
		
		this.distribuidorRepository.parametrosDistribuidorEmissaoDocumentos();
	}
	
	@Test
	public void codigo(){
		
		this.distribuidorRepository.codigo();
	}

	@Test
	public void capacidadeDistribuicao() {
		
		this.distribuidorRepository.capacidadeDistribuicao();
	}
	
	@Test
	public void juridica(){
		
		this.distribuidorRepository.juridica();
	}

	@Test
	public void parametroContratoCota() {
		
		this.distribuidorRepository.parametroContratoCota();
	}
}