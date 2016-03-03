package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.repository.SemaforoRepository;
import br.com.abril.nds.service.SemaforoService;

@Service
public class SemaforoServiceImpl implements SemaforoService {
	
	@Autowired
	private SemaforoRepository semaforoRepository;
	
	@Transactional(readOnly = true)
	public List<Semaforo> obterStatusProcessosEncalhe(Date data) {
		
		Validate.notNull(data, "A data informada é inválida");
		
		return this.semaforoRepository.obterSemaforosAtualizadosEm(data);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalStatusProcessosEncalhe(Date data) {
		
		Validate.notNull(data, "A data informada é inválida");
		
		return this.semaforoRepository.obterTotalSemaforosAtualizadosEm(data);
	}
	
	@Transactional
	public void atualizarStatusProcessoEncalheIniciadoEm(Date data) {
		
		this.semaforoRepository.atualizarStatusProcessoEncalheIniciadoEm(data);
	}

}