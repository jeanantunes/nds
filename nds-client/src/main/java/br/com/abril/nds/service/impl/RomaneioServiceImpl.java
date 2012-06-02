package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.RomaneioRepository;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class RomaneioServiceImpl implements RomaneioService {
	
	@Autowired
	private RomaneioRepository romaneioRepository;
	
	@Override
	@Transactional
	public List<RomaneioDTO> buscarRomaneio(FiltroRomaneioDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		
		return this.romaneioRepository.buscarRomaneios(filtro);
	}

}
