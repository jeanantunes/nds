package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.repository.ExpedicaoRepository;

@Repository
public class ExposicaoRepositoryImpl extends AbstractRepository<Expedicao, Long> 
implements ExpedicaoRepository{

	public ExposicaoRepositoryImpl() {
		super(Expedicao.class);
	}

	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(
			FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorBox(
			FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(
			FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorBox(
			FiltroResumoExpedicaoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}
