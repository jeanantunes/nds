package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import br.com.abril.nds.dto.DeparaDTO;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Depara;

import br.com.abril.nds.repository.DeparaRepository;

import br.com.abril.nds.service.DeparaService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class DeparaServiceImpl implements DeparaService  {
	
	@Autowired
	private DeparaRepository deparaRepository;

	
	
	@Override
	@Transactional
	public void salvarDepara(Depara depara) {
		deparaRepository.adicionar(depara);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<DeparaDTO> buscarDepara() {
		return deparaRepository.buscarDepara();
	}
	
	

	@Override
	@Transactional
	public void excluirDepara(Long id) {
		Depara depara = this.deparaRepository.buscarPorId(id);
		
		deparaRepository.remover(depara);
	}

	
	@Override
	@Transactional
	public Depara obterDeparaPorId(Long idDepara) {
		return deparaRepository.buscarPorId(idDepara);
	}



	@Override
	@Transactional
	public void alterarDepara(Depara depara) {
		deparaRepository.merge(depara);
	}
	
	@Override
	@Transactional
	public String obterBoxDinap(String boxfc){
		 return deparaRepository.obterBoxDinap(boxfc);
	}
	


	
}
