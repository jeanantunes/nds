package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupDistribuicaoDTO;
import br.com.abril.nds.repository.FollowupDistribuicaoRepository;
import br.com.abril.nds.service.FollowupDistribuicaoService;

@Service
public class FollowupDistribuicaoServiceImpl implements FollowupDistribuicaoService {
	
	@Autowired
	private FollowupDistribuicaoRepository followupDistribuicaoRepository;

	@Override
	@Transactional
	public List<ConsultaFollowupDistribuicaoDTO> obterCotas(ConsultaFollowupDistribuicaoDTO dto) {

		return followupDistribuicaoRepository.obterCotas(dto);
	}

}
