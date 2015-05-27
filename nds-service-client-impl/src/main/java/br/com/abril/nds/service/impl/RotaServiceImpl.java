package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class RotaServiceImpl implements RotaService {

	@Autowired
	RotaRepository rotaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Rota> obterRotas() {
		return rotaRepository.buscarRota("descricaoRota", Ordenacao.ASC);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Rota> buscarRotaPorRoteiro(Long roteiroId) {
		return rotaRepository.buscarRotaPorRoteiro(roteiroId, "descricaoRota", Ordenacao.ASC);
	}
}
