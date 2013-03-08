package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.DesenglobacaoRepository;
import br.com.abril.nds.service.DesenglobacaoService;

@Service
public class DesenglobacaoServiceImpl implements DesenglobacaoService {

	@Autowired private DesenglobacaoRepository repository;
	
	@Override
	public void obterDesenglobacaoPorCota() {
		

	}

	@Override
	public void inserirDesenglobacao(Desenglobacao desenglobacao) {
		repository.adicionar(desenglobacao);
	}

}
