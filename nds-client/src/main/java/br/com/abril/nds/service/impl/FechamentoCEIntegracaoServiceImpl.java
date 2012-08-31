package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.util.CurrencyUtil;

@Service
public class FechamentoCEIntegracaoServiceImpl implements FechamentoCEIntegracaoService {
	
	@Autowired
	private FechamentoCEIntegracaoRepository fechamentoCEIntegracaoRepository;

	@Transactional
	public List<FechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalhe(filtro);
	}

	@Transactional
	public Long obterDesconto(Long idCota, Long idProdutoEdica,	Long idFornecedor) {		 
		return null;
	}
	
	@Override
	public List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento) {
		List<FechamentoCEIntegracaoDTO> lista = new ArrayList<FechamentoCEIntegracaoDTO>();
		int sequencial = 1;
		for(FechamentoCEIntegracaoDTO dto: listaFechamento){
			dto.setVenda(dto.getReparte().subtract(dto.getEncalhe()));
			double valorDaVenda = dto.getVenda().doubleValue() * dto.getPrecoCapa().doubleValue();
			dto.setvalorVendaFormatado(CurrencyUtil.formatarValor(valorDaVenda));
			dto.setSequencial(sequencial);
			sequencial++;
			lista.add(dto);
		}
		return lista;		
	}
	
}
