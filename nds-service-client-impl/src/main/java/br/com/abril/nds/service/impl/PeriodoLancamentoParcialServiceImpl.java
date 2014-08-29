package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.service.PeriodoLancamentoParcialService;

@Service
public class PeriodoLancamentoParcialServiceImpl implements PeriodoLancamentoParcialService{

	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;

	@Transactional
	public List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro) {
		
		List<PeriodoParcialDTO> periodosParciais = periodoLancamentoParcialRepository.obterPeriodosParciais(filtro); 
		
		BigInteger reparteAcumulado = BigInteger.ZERO;
		
		BigInteger vendasAcumuladas = BigInteger.ZERO;
		
		for(PeriodoParcialDTO item : periodosParciais ){
			
			BigInteger valoReparte = item.getReparte().add(item.getSuplementacao()); 
			
			//Venda
			item.setVendas(valoReparte.subtract(item.getEncalhe()));
			
			//% Venda
			if(item.getVendas().compareTo(BigInteger.ZERO) > 0){
				BigDecimal vlVenda = new BigDecimal(item.getVendas());
				vlVenda = vlVenda.multiply(new BigDecimal("100"));
				item.setPercVenda(vlVenda.divide(new BigDecimal(valoReparte),2, RoundingMode.HALF_UP));
			}
			
			// Reparte Acumulado
			reparteAcumulado = reparteAcumulado.add(valoReparte);
			item.setReparteAcum(reparteAcumulado);

			//Vendas Acumuladas
			vendasAcumuladas = vendasAcumuladas.add(item.getVendas());
			item.setVendaAcumulada(vendasAcumuladas);
			
			//% Venda Acumulada
			if(item.getVendaAcumulada().compareTo(BigInteger.ZERO)> 0){
				BigDecimal vlVendaAcm = new BigDecimal(item.getVendaAcumulada());
				vlVendaAcm = vlVendaAcm.multiply(new BigDecimal("100"));
				item.setPercVendaAcumulada(vlVendaAcm.divide(new BigDecimal(item.getReparteAcum()),2,RoundingMode.HALF_UP));
			}
		}
		
		return periodosParciais;
	}
	
	@Transactional
	public Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro) {
		return periodoLancamentoParcialRepository.totalObterPeriodosParciais(filtro);
	}

	@Transactional
	@Override
	public PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento) {
		return periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
	}

}
