package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;

@Repository
public class TipoSegmentoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoSegmentoProduto, Long> implements
		TipoSegmentoProdutoRepository {

	public TipoSegmentoProdutoRepositoryImpl(){
		super(TipoSegmentoProduto.class);
	}
	
}
