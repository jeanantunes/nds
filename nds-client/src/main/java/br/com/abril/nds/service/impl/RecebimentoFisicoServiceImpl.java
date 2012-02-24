package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

@Service
public class RecebimentoFisicoServiceImpl implements RecebimentoFisicoService {

	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;

	
	@Transactional
	public void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico){
		
	}
	@Transactional
	public List<RecebimentoFisicoDTO> obterRecebimentoFisico(String cnpj, String numeroNota, String serieNota ){
		return recebimentoFisicoRepository.obterRecebimentoFisico(cnpj, numeroNota, serieNota);
	}
	@Transactional
	 public void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico){
		
	}
	
	
}
