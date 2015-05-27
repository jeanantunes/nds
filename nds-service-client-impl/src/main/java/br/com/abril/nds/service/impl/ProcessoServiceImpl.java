package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.repository.ProcessoRepository;
import br.com.abril.nds.service.ProcessoService;

@Service
public class ProcessoServiceImpl implements ProcessoService {

	@Autowired
	private ProcessoRepository processoRepository;
	
	@Override
	@Transactional
	public List<Processo> obterTodosProcessos() {
		return this.processoRepository.obterTodosProcessos();
	}

	@Override
	@Transactional
	public Processo buscarPeloNome(String nome) {
		return this.processoRepository.buscarPeloNome(nome);
	}

	@Override
	@Transactional
	public List<ItemDTO<String, String>> buscarProcessos(String parametros[]) {
		return this.processoRepository.buscarProcessos(parametros);
	}
	
	
}