package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.repository.ContasAPagarRepository;

public class ContasAPagarRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ContasAPagarRepository contasAPagarRepository;
	
	private FiltroContasAPagarDTO getFiltroPesquisaPorDistribuidor(){
		
		List<Long> idsFornecedores = new ArrayList<Long>();
		idsFornecedores.add(1L);
		idsFornecedores.add(2L);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 1, 1);
		
		FiltroContasAPagarDTO filtro = new FiltroContasAPagarDTO();
		filtro.setCe(1);
		filtro.setDataDe(cal.getTime());
		cal.clear();
		cal.set(2013, 1, 1);
		filtro.setDataAte(cal.getTime());
		filtro.setEdicao(1L);
		//filtro.setIdsFornecedores(idsFornecedores);
		
		return filtro;
	}
	
	@Test
	public void testBuscarDatasLancamentoContasAPagar(){
		
		List<Date> lista = 
				this.contasAPagarRepository.buscarDatasLancamentoContasAPagar(this.getFiltroPesquisaPorDistribuidor());
		
		Assert.assertNotNull(lista);
	}
	
	//@Test
	public void testPesquisarPorDistribuidor(){
		
		List<ContasApagarConsultaPorDistribuidorDTO> lista = 
				this.contasAPagarRepository.pesquisarPorDistribuidor(this.getFiltroPesquisaPorDistribuidor());
		
		Assert.assertNotNull(lista);
	}
	
	//@Test
	public void testPesquisaPorDistribuidorSuplementacaoEntrada(){
		
		List<GrupoMovimentoEstoque> movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
		movimentosSuplementar.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
		movimentosSuplementar.add(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		
		this.contasAPagarRepository.pesquisaPorDistribuidorValorPorGrupoMovimento(new Date(), movimentosSuplementar);
	}
	
	//@Test
	public void testPesquisaPorDistribuidorFaltasSobras(){
		
		this.contasAPagarRepository.pesquisaPorDistribuidorFaltasSobras(new Date(), TipoDiferenca.FALTA_DE);
	}
	
	//@Test
	public void testPesquisaPorDistribuidorPerdasGanhos(){
		
		this.contasAPagarRepository.pesquisaPorDistribuidorPerdasGanhos(new Date(), StatusAprovacao.GANHO);
	}
}
