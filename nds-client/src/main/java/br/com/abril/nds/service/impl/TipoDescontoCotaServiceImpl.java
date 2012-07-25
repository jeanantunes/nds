package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;
import br.com.abril.nds.service.TipoDescontoCotaService;

@Service
public class TipoDescontoCotaServiceImpl implements TipoDescontoCotaService {
	
	@Autowired
	private TipoDescontoCotaRepository  tipoDescontoCotaRepository;
	
	/**
	 * Método responsável por incluir um desconto
	 * @param {@link br.com.abril.nds.model.cadastro.TipoDescontoCota} 
	 */
	@Transactional
	@Override
	public void incluirDesconto(TipoDescontoCota tipoDescontoCota) {
		 this.tipoDescontoCotaRepository.adicionar(tipoDescontoCota);
	}
	

	@Override
	@Transactional
	public void excluirDesconto(TipoDescontoCota tipoDescontoCota) {		
		this.tipoDescontoCotaRepository.remover(tipoDescontoCota);
		
	}

	@Override
	@Transactional
	public Integer buscarTotalDescontosPorCota() {		 
		return this.tipoDescontoCotaRepository.buscarTotalDescontoPorCota();
	}

	@Override
	@Transactional
	public List<TipoDescontoCotaDTO> obterTipoDescontosCota() {		 
		return this.tipoDescontoCotaRepository.obterTipoDescontosCota();
	}

}
