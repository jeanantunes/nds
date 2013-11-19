package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaSuplementarRepository;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ResumoSuplementarFecharDiaServiceImp implements
		ResumoSuplementarFecharDiaService {
	
	@Autowired
	private ResumoSuplementarFecharDiaRepository resumoSuplementarFecharDiaRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoSuplementarRepository fechamentoDiarioConsolidadoSuplementarRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoSuplementarRepository lancamentoSuplementarRepository;
	
	@Autowired
	private FecharDiaService fecharDiaService;
	
	@Autowired
	private FechamentoDiarioMovimentoVendaSuplementarRepository movimentoVendaSuplementarRepository;

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorEstoqueLogico(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterValorTransferencia(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorTransferencia(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterValorVenda(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorVenda(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterValorFisico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorFisico(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao,PaginacaoVO paginacao) {
		
		if (fecharDiaService.isDiaComFechamentoRealizado(dataOperacao)){
			
			return movimentoVendaSuplementarRepository.obterDadosVendaSuplementar(dataOperacao, paginacao);
		}
		else{
			
			return this.resumoSuplementarFecharDiaRepository.obterVendasSuplementar(dataOperacao);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public ResumoSuplementarFecharDiaDTO obterResumoGeralSuplementar(Date dataOperacional) {
		
		ResumoSuplementarFecharDiaDTO dto = new ResumoSuplementarFecharDiaDTO();
		
		if (fecharDiaService.isDiaComFechamentoRealizado(dataOperacional)){
			
			dto = fechamentoDiarioConsolidadoSuplementarRepository.obterResumoGeralSuplementar(dataOperacional);
			
		}else{

			BigDecimal totalEstoqueLogico = this.obterValorEstoqueLogico(dataOperacional);
			dto.setTotalEstoqueLogico(totalEstoqueLogico);
			
			BigDecimal totalTransferencia = this.obterValorTransferencia(dataOperacional);
			dto.setTotalTransferencia(totalTransferencia);		
			
			BigDecimal totalVenda = this.obterValorVenda(dataOperacional);
			dto.setTotalVenda(totalVenda);
			
			dto.setSaldo(totalEstoqueLogico.add(totalTransferencia).subtract(totalVenda));			
		}
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date data, PaginacaoVO paginacao) {
		
		List<SuplementarFecharDiaDTO> listaSuplementar = null;	
		
		if (fecharDiaService.isDiaComFechamentoRealizado(data)){
			
			listaSuplementar = lancamentoSuplementarRepository.obterDadosGridSuplementar(data, paginacao);
			
		}
		else{
			 
			listaSuplementar = this.resumoSuplementarFecharDiaRepository.obterDadosGridSuplementar(data, paginacao);
			
			carregarSaldoGridSuplementar(listaSuplementar);
		}
		
		return listaSuplementar;
	}
	
	private void carregarSaldoGridSuplementar(List<SuplementarFecharDiaDTO> listaSuplementar) {
		
		if(listaSuplementar == null || listaSuplementar.isEmpty()) {
			return;
		}
		
		for(SuplementarFecharDiaDTO suplementar : listaSuplementar) {

			BigInteger entrada = (suplementar.getQuantidadeTransferenciaEntrada() == null) ? BigInteger.ZERO : suplementar.getQuantidadeTransferenciaEntrada();
			BigInteger saida = (suplementar.getQuantidadeTransferenciaSaida() == null) ? BigInteger.ZERO : suplementar.getQuantidadeTransferenciaSaida();
			BigInteger logico = (suplementar.getQuantidadeContabil() == null) ? BigInteger.ZERO : suplementar.getQuantidadeContabil();
			BigInteger venda = (suplementar.getQuantidadeVenda() == null) ? BigInteger.ZERO : suplementar.getQuantidadeVenda();
			
			BigInteger saldo = logico.add(entrada).subtract(saida).subtract(venda);
			
			suplementar.setSaldo(saldo);
			
		}
		
	}
	

	@Override
	@Transactional(readOnly=true)
	public Long contarProdutoEdicaoSuplementar(Date dataFechamento) {
		
		if (fecharDiaService.isDiaComFechamentoRealizado(dataFechamento)){
			
			return lancamentoSuplementarRepository.countDadosGridSuplementar(dataFechamento);
		}
		else{
			return resumoSuplementarFecharDiaRepository.contarProdutoEdicaoSuplementar();
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Long contarVendasSuplementar(Date data) {
		 
		Objects.requireNonNull(data, "Data para contagem das vendas de encalhe não deve ser nula!");
		
		if (fecharDiaService.isDiaComFechamentoRealizado(data)){
			
			return movimentoVendaSuplementarRepository.countDadosVendaSuplementar(data);
		}
		else{
			
			return this.resumoSuplementarFecharDiaRepository.contarVendasSuplementar(data);
		}
	}

}
