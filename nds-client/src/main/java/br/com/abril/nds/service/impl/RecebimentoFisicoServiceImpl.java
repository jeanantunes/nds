package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

@Service
public class RecebimentoFisicoServiceImpl implements RecebimentoFisicoService {

	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	

	@Transactional
	public List<RecebimentoFisico> obterRecebimentoFisico(){
		return recebimentoFisicoRepository.obterRecebimentoFisico();
	}
	@Transactional
	public void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico){
		
	}
	
	
}
