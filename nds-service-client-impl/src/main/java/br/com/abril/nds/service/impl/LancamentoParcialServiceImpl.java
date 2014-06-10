package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.service.LancamentoParcialService;

@Service
public class LancamentoParcialServiceImpl implements LancamentoParcialService {

	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Override
	@Transactional
	public List<ParcialDTO> buscarLancamentosParciais(FiltroParciaisDTO filtro) {
		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
				
		return lancamentoParcialRepository.buscarLancamentosParciais(filtro);
	}

	@Override
	@Transactional
	public Integer totalBuscaLancamentosParciais(FiltroParciaisDTO filtro) {
		return lancamentoParcialRepository.totalbuscaLancamentosParciais(filtro);
	}

}
