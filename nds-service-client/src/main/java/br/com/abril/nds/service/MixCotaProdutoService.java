package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;


public interface MixCotaProdutoService {
	
	
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<MixProdutoDTO> pesquisarPorProduto(FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO);
	
	public void removerMixCotaProduto(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<PdvDTO> obterListaPdvPorMix(Long id);
	
	
	public MixCotaProdutoDTO obterPorId(Long id);
	public void excluirTodos();
	
	public void excluirMixPorCota(Long idCota);
	
	public List<String> adicionarListaMixPorCota(List<MixCotaProdutoDTO> listaMixCota, Integer cotaId);

	public List<String> adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixProduto , String produtoId);	
	
	public List<String> adicionarMixEmLote(List<MixCotaProdutoDTO> mixCotaProdutoDTOList);

	public boolean gerarCopiaMix(CopiaMixFixacaoDTO copiaMix);

	public String obterValidacaoLinha(MixCotaProdutoDTO mixCotaProdutoDTO);

	public void updateReparteMixCotaProduto(Long novoValorReparte, String tipoCampo, Long idMix);
}
