package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;


public interface MixCotaProdutoService {
	
	
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<MixProdutoDTO> pesquisarPorProduto(FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO);
	
	public void removerMixCotaProduto(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
//	public List<RepartePDVDTO> obterRepartePdv(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);

	public List<PdvDTO> obterListaPdvPorMix(Long id);
	
	public MixCotaProdutoDTO obterPorId(Long id);
	
	public void excluirTodos();
	
	public void excluirMixPorCota(Long idCota);
	
	public void adicionarListaMixPorCota(List<MixCotaProdutoDTO> listaMixCota, Integer cotaId);

	public void adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixProduto , String produtoId);	
	
	public void adicionarMixEmLote(List<MixCotaProdutoDTO> mixCotaProdutoDTOList);

	public boolean gerarCopiaMix(CopiaMixFixacaoDTO copiaMix);
}
