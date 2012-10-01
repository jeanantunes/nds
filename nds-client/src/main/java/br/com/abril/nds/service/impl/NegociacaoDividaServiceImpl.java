package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.service.NegociacaoDividaService;

@Service
public class NegociacaoDividaServiceImpl implements NegociacaoDividaService{
	
	@Autowired
	private NegociacaoDividaRepository negociacaoDividaRepository;
	
	@Override
	@Transactional
	public List<NegociacaoDividaDTO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro) {
			
		return negociacaoDividaRepository.obterCotaPorNumero(filtro);
	}
	
	
}
