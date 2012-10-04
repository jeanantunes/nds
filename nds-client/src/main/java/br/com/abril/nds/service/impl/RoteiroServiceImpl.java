package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class RoteiroServiceImpl implements RoteiroService {

	@Autowired
	RoteiroRepository roteiroRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Roteiro> obterRoteiros() {
		return roteiroRepository.buscarRoteiro("descricaoRoteiro", Ordenacao.ASC);
	}

}
