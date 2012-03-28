package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.repository.RotaRoteiroOperacaoRepository;
import br.com.abril.nds.service.RotaRoteiroOperacaoService;

@Service
public class RotaRoteiroOperacaoServiceImpl implements RotaRoteiroOperacaoService {

	@Autowired
	private RotaRoteiroOperacaoRepository operacaoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public RotaRoteiroOperacao obterRotaRoteiroImpressaoDividaCota(Integer numeroCota) {
		
		return operacaoRepository.obterRoterioImpressaoDividaCota(numeroCota) ;
	}
}
