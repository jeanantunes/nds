package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;


public interface MixCotaProdutoService {
	
	
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<MixProdutoDTO> pesquisarPorProduto(FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO);
	
	public void removerMixCotaProduto(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<RepartePDVDTO> obterRepartePdv(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
}
