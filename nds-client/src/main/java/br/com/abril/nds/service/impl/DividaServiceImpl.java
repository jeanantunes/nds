package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.service.DividaService;

@Service
public class DividaServiceImpl implements DividaService {

	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;

	protected MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Override
	@Transactional
	public List<StatusDividaDTO> obterInadimplenciasCota(
			FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterInadimplenciasCota(filtro);
	}

	@Override
	@Transactional
	public Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalInadimplenciasCota(filtro);
	}

	@Override
	@Transactional
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalCotasInadimplencias(filtro);
	}

	@Override
	@Transactional
	public Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterSomaDividas(filtro);
	}

	@Override
	@Transactional
	public List<Divida> getDividasAcumulo(Long idDivida) {
		
		List<Divida> dividas = new ArrayList<Divida>(dividaRepository.buscarPorId(idDivida).getAcumulado());
		
		for(Divida divida:dividas) {
			divida.getCobranca();
		}
		return dividas; 
	}

	@Override
	@Transactional
	public Divida obterDividaPorId(Long idDivida) {
		return dividaRepository.buscarPorId(idDivida);
	}

	@Override
	@Transactional
	public void postergarCobrancaCota(List<Cobranca> listaCobranca, Date dataPostergacao, BigDecimal encargos) {
	
		// FIXME: Validar cobrancas de entrada.
		
		for (Cobranca cobranca : listaCobranca) {
			
			cobranca.setDataPagamento(dataPostergacao);
			cobranca.getDivida().setStatus(StatusDivida.POSTERGADA);
			cobranca.setEncargos(encargos);
			
			this.cobrancaRepository.alterar(cobranca);
		}
	}

}
