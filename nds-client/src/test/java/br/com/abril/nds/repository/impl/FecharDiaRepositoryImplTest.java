package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.mail.imap.protocol.Status;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;


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
	

}
