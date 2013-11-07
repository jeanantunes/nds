package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ResumoSuplementarFecharDiaServiceImp implements
		ResumoSuplementarFecharDiaService {
	
	@Autowired
	private ResumoSuplementarFecharDiaRepository resumoSuplementarFecharDiaRepository;

	@Override
	@Transactional
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorEstoqueLogico(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorTransferencia(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorTransferencia(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorVenda(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorVenda(dataOperacao);
	}

	@Override
	@Transactional
	public BigDecimal obterValorFisico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorFisico(dataOperacao);
	}

	@Override
	@Transactional
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterVendasSuplementar(dataOperacao);
		
	}

	@Override
	@Transactional
	public ResumoSuplementarFecharDiaDTO obterResumoGeralSuplementar(Date dataOperacional) {
		 
		ResumoSuplementarFecharDiaDTO dto = new ResumoSuplementarFecharDiaDTO();
		List<BigDecimal> listaDeSuplementares = new ArrayList<BigDecimal>();
		
		BigDecimal totalEstoqueLogico = this.obterValorEstoqueLogico(dataOperacional);
		dto.setTotalEstoqueLogico(totalEstoqueLogico);
		
		BigDecimal totalTransferencia = this.obterValorTransferencia(dataOperacional);
		dto.setTotalTransferencia(totalTransferencia);		
		
		BigDecimal totalVenda = this.obterValorVenda(dataOperacional);
		dto.setTotalVenda(totalVenda);
		listaDeSuplementares.add(totalVenda);
		
		dto.setSaldo(totalEstoqueLogico.add(totalTransferencia).subtract(totalVenda));
		
		return dto;
	}

	@Override
	@Transactional
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date data, PaginacaoVO paginacao) {
		
		List<SuplementarFecharDiaDTO> listaSuplementar = this.resumoSuplementarFecharDiaRepository.obterDadosGridSuplementar(data, paginacao);
		
		carregarSaldoGridSuplementar(listaSuplementar);
		
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
	@Transactional
	public Long contarProdutoEdicaoSuplementar() {
		return this.resumoSuplementarFecharDiaRepository.contarProdutoEdicaoSuplementar();
	}

	@Override
	@Transactional
	public Long contarVendasSuplementar(Date data) {
		 Objects.requireNonNull(data, "Data para contagem das vendas de encalhe não deve ser nula!");
		return this.resumoSuplementarFecharDiaRepository.contarVendasSuplementar(data);
	}

}
