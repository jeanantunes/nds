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
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioRepository;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ResumoSuplementarFecharDiaServiceImpl implements ResumoSuplementarFecharDiaService {
	
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
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;

	@Autowired
	private FechamentoDiarioRepository fechamentoDiarioRepository;
	
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
	public BigDecimal obterValorAlteracaoPreco(Date dataOperacao) {		 
		return this.resumoSuplementarFecharDiaRepository.obterValorAlteracaoPreco(dataOperacao);
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
			
		} else {

			Date dataUltimoFechamento = fechamentoDiarioRepository.obterDataUltimoFechamento(dataOperacional);
			
			BigDecimal totalEstoqueLogico = Util.nvl(this.obterValorEstoqueLogico(dataUltimoFechamento), BigDecimal.ZERO);
			
			BigDecimal totalTransferencia = Util.nvl(this.obterValorTransferencia(dataOperacional), BigDecimal.ZERO);
			
			BigDecimal totalVenda = Util.nvl(this.obterValorVenda(dataOperacional), BigDecimal.ZERO);
			
			BigDecimal totalInventario = Util.nvl(this.obterValorInventario(dataOperacional), BigDecimal.ZERO);
			
			BigDecimal totalAlteracaoPreco = Util.nvl(this.obterValorAlteracaoPreco(dataOperacional), BigDecimal.ZERO);
			
			dto.setTotalEstoqueLogico(totalEstoqueLogico);
		
			dto.setTotalTransferencia(totalTransferencia);		
			
			dto.setTotalVenda(totalVenda);
			
			dto.setTotalInventario(totalInventario);
			
			dto.setTotalAlteracaoPreco(totalAlteracaoPreco);
			
			dto.setSaldo(totalEstoqueLogico.add(totalTransferencia).subtract(totalVenda).add(totalAlteracaoPreco).add(totalInventario));			
		}
		
		return dto;
	}

	private BigDecimal obterValorInventario(Date dataOperacao) {
		
		return this.resumoSuplementarFecharDiaRepository.obterValorInventario(dataOperacao);
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
			
			BigInteger entrada = Util.nvl(suplementar.getQuantidadeTransferenciaEntrada(),BigInteger.ZERO);
			
			BigInteger saida = Util.nvl(suplementar.getQuantidadeTransferenciaSaida(),BigInteger.ZERO);
			
			BigInteger logico = Util.nvl(suplementar.getQuantidadeLogico(),BigInteger.ZERO);
			
			BigInteger venda = Util.nvl(suplementar.getQuantidadeVenda(),BigInteger.ZERO);
			
			suplementar.setQuantidadeLogico(logico.add(venda));
			
			BigInteger saldo = logico.add(entrada).subtract(saida);
			
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
