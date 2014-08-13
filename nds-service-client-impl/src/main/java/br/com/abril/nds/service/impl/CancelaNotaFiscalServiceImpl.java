package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.CancelaNotaFiscalService;

@Service
public class CancelaNotaFiscalServiceImpl implements CancelaNotaFiscalService {

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Override
	@Transactional
	public List<NfeDTO> pesquisar() {


		return null;
	}
	
}
