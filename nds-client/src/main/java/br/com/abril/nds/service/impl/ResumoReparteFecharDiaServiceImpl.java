package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;

@Service
public class ResumoReparteFecharDiaServiceImpl  implements ResumoReparteFecharDiaService {

	@Autowired
	private ResumoReparteFecharDiaRepository resumoFecharDiaRepository;
	
	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma) {		
		return resumoFecharDiaRepository.obterValorReparte(dataOperacaoDistribuidor, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca) {		
		return this.resumoFecharDiaRepository.obterValorDiferenca(dataOperacao, soma, tipoDiferenca);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorTransferencia(Date dataOperacao, boolean soma) {		 
		return this.resumoFecharDiaRepository.obterValorTransferencia(dataOperacao, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorDistribuido(Date dataOperacao, boolean soma) {		 
		return this.resumoFecharDiaRepository.obterValorDistribuido(dataOperacao, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao) {
		return this.resumoFecharDiaRepository.obterResumoReparte(dataOperacao);
	}

	@Override
	@Transactional
	public ResumoReparteFecharDiaDTO obterResumoGeralReparte(Date dataOperacao) {
		
		ResumoReparteFecharDiaDTO dto = new ResumoReparteFecharDiaDTO();

		List<ReparteFecharDiaDTO> lista = this.obterValorReparte(dataOperacao, true);
		
		BigDecimal totalReparte = lista.get(0).getValorTotalReparte() != null ? lista.get(0).getValorTotalReparte() : BigDecimal.ZERO;
		dto.setTotalReparte(totalReparte);		
		
		lista = this.obterValorDiferenca(dataOperacao, true, "sobra");
		BigDecimal totalSobras = lista.get(0).getSobras() != null ? lista.get(0).getSobras() : BigDecimal.ZERO;
		dto.setTotalSobras(totalSobras);
		
		lista = this.obterValorDiferenca(dataOperacao, true, "falta");
		BigDecimal totalFaltas = lista.get(0).getFaltas() != null ? lista.get(0).getFaltas() : BigDecimal.ZERO;
		dto.setTotalFaltas(totalFaltas);
		
		lista = this.obterValorTransferencia(dataOperacao, true);

		BigDecimal totalTranferencia =  lista.get(0).getTransferencias() != null ? lista.get(0).getTransferencias() : BigDecimal.ZERO;		
		dto.setTotalTransferencia(totalTranferencia);
		
		BigDecimal totalADistribuir = (totalReparte.add(totalSobras)).subtract(totalFaltas);
		dto.setTotalADistribuir(totalADistribuir);		
		
		lista = this.obterValorDistribuido(dataOperacao, true);
		BigDecimal totalDistribuido = lista.get(0).getDistribuidos() != null ? lista.get(0).getDistribuidos() : BigDecimal.ZERO;
		dto.setTotalDistribuido(totalDistribuido);
		
		BigDecimal sobraDistribuido = totalADistribuir.subtract(totalDistribuido);
		dto.setSobraDistribuido(sobraDistribuido);
		
		BigDecimal diferenca = totalDistribuido.subtract(sobraDistribuido);
		dto.setDiferenca(diferenca);
		
		return dto;
	}

		

}
