package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.repository.SemaforoRepository;
import br.com.abril.nds.service.SemaforoService;

@Service
public class SemaforoServiceImpl implements SemaforoService {
	
	@Autowired
	private SemaforoRepository semaforoRepository;
	
	@Autowired
	public List obterStatusProcessoEncalhe() {
		//TODO: implementar this shit
		return null;
	}

}
