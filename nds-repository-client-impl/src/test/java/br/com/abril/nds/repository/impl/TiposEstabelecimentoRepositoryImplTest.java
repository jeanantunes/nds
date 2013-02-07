package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.repository.TiposEstabelecimentoRepository;

public class TiposEstabelecimentoRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private TiposEstabelecimentoRepository tiposEstabelecimentoRepository;
	
	@SuppressWarnings("unused")
	@Test
	public void obterTipoEstabelecimentoAssociacaoPDV(){
		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV= tiposEstabelecimentoRepository.obterTipoEstabelecimentoAssociacaoPDV(1L);
	}

}
