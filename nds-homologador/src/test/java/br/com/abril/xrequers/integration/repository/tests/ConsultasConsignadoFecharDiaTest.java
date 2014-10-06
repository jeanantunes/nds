package br.com.abril.xrequers.integration.repository.tests;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.util.Util;
import br.com.abril.xrequers.integration.service.tests.TestUtil;

public class ConsultasConsignadoFecharDiaTest extends AbstractRepositoryTest {
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private DiferencaEstoqueRepository diferencaRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaAusenteRepository cotaAusenteRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Test
	public void test_obter_valores_consignado_fechar_dia() {

		Date dataFechamento = TestUtil.criarData(16, Calendar.SEPTEMBER, 2014);
		
		ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado = new ResumoFechamentoDiarioConsignadoDTO();
				
		ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado resumoConsignado = resumoFechamentoDiarioConsignado.new ResumoConsignado();
		
	    BigDecimal valorExpedido = Util.nvl(
		movimentoEstoqueRepository.obterSaldoDeReparteExpedido(dataFechamento),BigDecimal.ZERO);

	    valorExpedido = valorExpedido.add(Util.nvl(
        diferencaRepository.obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidorNoDia(dataFechamento),BigDecimal.ZERO));

	    BigDecimal valorSaidaDoConsignadoAVista =  Util.nvl(
		this.movimentoEstoqueCotaRepository.obterValorExpedicaoCotaAVista(dataFechamento, true),BigDecimal.ZERO);

		resumoConsignado.setValorAVista(valorSaidaDoConsignadoAVista);
		
		resumoConsignado.setValorExpedicao(valorExpedido.subtract(valorSaidaDoConsignadoAVista));
		
		resumoConsignado.setValorOutrosValoresSaidas(this.obterValorSaidaOutros(dataFechamento));
		
		resumoConsignado.setValorSaidas(valorExpedido.add(resumoConsignado.getValorOutrosValoresSaidas()));

		resumoConsignado.setSaldoAtual(
		        resumoConsignado.getSaldoAnterior().subtract(
		                resumoConsignado.getValorEntradas()).add(resumoConsignado.getValorSaidas()));

		resumoFechamentoDiarioConsignado.setResumoConsignado(resumoConsignado);
		
	}
	
	private BigDecimal obterValorSaidaOutros(final Date dataFechamento){
		
		BigDecimal valorDiferencasSaida =  Util.nvl(
        		diferencaRepository.obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        BigDecimal valorCotaAusenteSaida = Util.nvl(
        		cotaAusenteRepository.obterSaldoDeSaidaDoConsignadoDasCotasAusenteNoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        BigDecimal valorVendaSuplementar = Util.nvl(
        		movimentoEstoqueService.obterValorConsignadoDeVendaSuplementar(dataFechamento), BigDecimal.ZERO);
        
        return (valorDiferencasSaida.add(valorVendaSuplementar).add(valorCotaAusenteSaida));
	}	   
	
}
