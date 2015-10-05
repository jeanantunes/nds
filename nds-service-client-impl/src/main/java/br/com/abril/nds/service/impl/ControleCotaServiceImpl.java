package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ControleCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.repository.ControleCotaRepository;
import br.com.abril.nds.service.ControleCotaService;

@Service
public class ControleCotaServiceImpl implements ControleCotaService  {
	
	@Autowired
	private ControleCotaRepository controleCotaRepository;

	
	
	@Override
	@Transactional
	public void salvarControleCota(ControleCota ControleCota) {
		controleCotaRepository.adicionar(ControleCota);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<ControleCotaDTO> buscarControleCota() {
		return controleCotaRepository.buscarControleCota();
	}
	
	

	@Override
	@Transactional
	public void excluirControleCota(Long id) {
		ControleCota ControleCota = this.controleCotaRepository.buscarPorId(id);
		
		controleCotaRepository.remover(ControleCota);
	}

	
	@Override
	@Transactional
	public ControleCota obterControleCotaPorId(Long idControleCota) {
		return controleCotaRepository.buscarPorId(idControleCota);
	}
	
	
	



	@Override
	@Transactional
	public void alterarControleCota(ControleCota ControleCota) {
		controleCotaRepository.merge(ControleCota);
	}
	
	
	
	/**
	 * Salva/Altera Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	@Transactional
	@Override
	public void salvarCotaUnificacao(Integer numeroCotaCentralizadora, 
			                         List<Integer> numeroCotasCentralizadas){
		
	
		//adiciona novas cotas na centralização
		for (Integer numeroCota : numeroCotasCentralizadas) {
			
			ControleCota controleCota = new ControleCota();
			controleCota.setNumeroCotaMaster(numeroCotaCentralizadora);
			controleCota.setSituacao("A");
			controleCota.setNumeroCota(numeroCota);
			this.salvarControleCota(controleCota);
			
			
		}
	}
	
		
	public	Integer buscarCotaMaster(Integer cota) {
		return controleCotaRepository.buscarCotaMaster(cota);
	}
		
	

	
	

	


	
}
