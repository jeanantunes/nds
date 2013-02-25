package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;

public interface MixCotaProdutoRepository extends
		Repository<MixCotaProduto, Long> {

	public List<MixCotaDTO> pesquisarPorCota(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO);

}
