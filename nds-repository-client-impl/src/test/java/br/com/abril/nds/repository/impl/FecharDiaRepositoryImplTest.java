package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;


public class FecharDiaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	FecharDiaRepositoryImpl fecharDiaRepositoryImpl;
	
	@Test
	public void existeCobrancaParaFecharDia(){
		
		Date diaDeOperacaoMenosUm = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		boolean existeCobrancaParaFecharDia = fecharDiaRepositoryImpl.existeCobrancaParaFecharDia(diaDeOperacaoMenosUm);
		
		Assert.assertFalse(existeCobrancaParaFecharDia);
	}
	
	@Test
	public void existeNotaFiscalSemRecebimentoFisico(){
		
		Date dataOperacaoDistribuidor = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		boolean existeNotaFiscalSemRecebimentoFisico = 
				fecharDiaRepositoryImpl.existeNotaFiscalSemRecebimentoFisico(dataOperacaoDistribuidor);
		
		Assert.assertTrue(existeNotaFiscalSemRecebimentoFisico);
	}
	
	@Test
	public void obterNotaFiscalComRecebimentoFisicoNaoConfirmado(){
		
		Date dataOperacaoDistribuidor = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> validacaoRecebimentoFisicoFecharDiaDTO = 
				fecharDiaRepositoryImpl.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacaoDistribuidor);
		
		Assert.assertNotNull(validacaoRecebimentoFisicoFecharDiaDTO);
	}
	
	@Test
	public void existeConfirmacaoDeExpedicao(){
		
		Date dataOperacaoDistribuidor = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		Boolean existeConfirmacaoDeExpedicao = 
				fecharDiaRepositoryImpl.existeConfirmacaoDeExpedicao(dataOperacaoDistribuidor);
		
		Assert.assertTrue(existeConfirmacaoDeExpedicao);
	}
	
	
	@Test
	public void obterConfirmacaoDeExpedicao(){
		
		Date dataOperacaoDistribuidor = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		List <ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> validacaoConfirmacaoDeExpedicaoFecharDiaDTOs = 
				fecharDiaRepositoryImpl.obterConfirmacaoDeExpedicao(dataOperacaoDistribuidor);
		
		Assert.assertNotNull(validacaoConfirmacaoDeExpedicaoFecharDiaDTOs);
	}
	
	@Test
	public void existeLancamentoFaltasESobrasPendentes(){
		
		Date dataOperacaoDistribuidor = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		List <ValidacaoLancamentoFaltaESobraFecharDiaDTO> validacaoLancamentoFaltaESobraFecharDiaDTOs = 
				fecharDiaRepositoryImpl.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor);
		
		Assert.assertNotNull(validacaoLancamentoFaltaESobraFecharDiaDTOs);
	}
	
	@Test
	public void obterPendenciasDeAprovacaoDataOperacao(){
		
		Date dataOperacao = Fixture.criarData(30, Calendar.OCTOBER, 2012);
		
		List <ValidacaoControleDeAprovacaoFecharDiaDTO> validacaoControleDeAprovacaoFecharDiaDTOs = 
				fecharDiaRepositoryImpl.obterPendenciasDeAprovacao(dataOperacao, null);
		
		Assert.assertNotNull(validacaoControleDeAprovacaoFecharDiaDTOs);
	}
	
	@Test
	public void obterPendenciasDeAprovacaoStatusAprovacao(){
		
		StatusAprovacao status = StatusAprovacao.APROVADO; 
		
		List <ValidacaoControleDeAprovacaoFecharDiaDTO> validacaoControleDeAprovacaoFecharDiaDTOs = 
				fecharDiaRepositoryImpl.obterPendenciasDeAprovacao(null, status);
		
		Assert.assertNotNull(validacaoControleDeAprovacaoFecharDiaDTOs);
	}

	@Test
	public void obterMovimentosPorStatusData() {

		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = new ArrayList<GrupoMovimentoFinaceiro>();
		
		gruposMovimentoEstoque.add(GrupoMovimentoEstoque.SOBRA_DE);
		gruposMovimentoFinanceiro.add(GrupoMovimentoFinaceiro.DEBITO);

		List<Movimento> movimentos =  this.fecharDiaRepositoryImpl.obterMovimentosPorStatusData(
												gruposMovimentoEstoque, 
												gruposMovimentoFinanceiro, 
												new Date(), 
												StatusAprovacao.PENDENTE);
		
		Assert.assertNotNull(movimentos);
	}
	
	@Test
	public void obterMovimentosPorStatusDataNulo() {

		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = null;
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = null;
		
		List<Movimento> movimentos =  this.fecharDiaRepositoryImpl.obterMovimentosPorStatusData(
												gruposMovimentoEstoque, 
												gruposMovimentoFinanceiro, 
												new Date(), 
												StatusAprovacao.PENDENTE);
		
		Assert.assertNotNull(movimentos);
	}
	
	@Test
	public void obterMovimentosPorStatusDataPorGrupoMovimentosEstoque() {

		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = null;
		
		gruposMovimentoEstoque.add(GrupoMovimentoEstoque.SOBRA_DE);

		List<Movimento> movimentos =  this.fecharDiaRepositoryImpl.obterMovimentosPorStatusData(
												gruposMovimentoEstoque, 
												gruposMovimentoFinanceiro, 
												new Date(), 
												StatusAprovacao.PENDENTE);
		
		Assert.assertNotNull(movimentos);
	}
	
	@Test
	public void obterMovimentosPorStatusDataPorGrupoMovimentoFinaceiro() {

		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = null;
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = new ArrayList<GrupoMovimentoFinaceiro>();
		
		gruposMovimentoFinanceiro.add(GrupoMovimentoFinaceiro.DEBITO);

		List<Movimento> movimentos =  this.fecharDiaRepositoryImpl.obterMovimentosPorStatusData(
												gruposMovimentoEstoque, 
												gruposMovimentoFinanceiro, 
												new Date(), 
												StatusAprovacao.PENDENTE);
		
		Assert.assertNotNull(movimentos);
	}
	
	@Test
	public void obterFormasDeCobranca() {
		
		List<ValidacaoGeracaoCobrancaFecharDiaDTO> listaFormasCobranca =  this.fecharDiaRepositoryImpl.obterFormasDeCobranca();
		
		Assert.assertNotNull(listaFormasCobranca);
		
	}
	
	@Test
	public void obterDiasDaConcentracao() {
		
		FormaCobranca formaCobranca = new FormaCobranca();
		formaCobranca.setId(1L);
		
		List<ValidacaoGeracaoCobrancaFecharDiaDTO> listaFormasCobranca =  this.fecharDiaRepositoryImpl.obterDiasDaConcentracao(formaCobranca);
	
		Assert.assertNotNull(listaFormasCobranca);
		
	}
	
}
