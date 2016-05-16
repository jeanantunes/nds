package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.InformacoesReparteComplementarEstudo;
import br.com.abril.nds.repository.InformacoesReparteEstudoComplementarRepository;
import br.com.abril.nds.service.InformacoesReparteEstudoComplementarService;

@Service
public class InformacoesReparteEstudoComplementarServiceImpl implements InformacoesReparteEstudoComplementarService {
	
	@Autowired
	private InformacoesReparteEstudoComplementarRepository informacoesRepository;
	
	@Override
	@Transactional
	public void salvarInformacoes(InformacoesReparteComplementarEstudo informacoes){
		this.informacoesRepository.adicionar(informacoes);
	}
	
}
