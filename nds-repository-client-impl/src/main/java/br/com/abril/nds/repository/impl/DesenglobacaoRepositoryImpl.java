package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DesenglobacaoRepository;

@Repository
public class DesenglobacaoRepositoryImpl extends AbstractRepositoryModel<Desenglobacao, Long> implements DesenglobacaoRepository {

	public DesenglobacaoRepositoryImpl() {
		super(Desenglobacao.class);
	}

	@Override
	public void obterDesenglobacaoPorCota() {
		// TODO Auto-generated method stub
		
	}

}
