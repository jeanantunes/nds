package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.seguranca.Usuario;

public interface MixCotaProdutoRepository extends
		Repository<MixCotaProduto, Long> {

	public List<MixCotaDTO> pesquisarPorCota(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO);
	
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO);
	
	public void  excluirTodos();
	
	public boolean existeMixCotaProdutoCadastrado(Long idProduto, Long idCota);
	
	public void removerPorIdCota(Long idCota);

	public void execucaoQuartz();

	public void gerarCopiaMixCota(List<MixCotaDTO> mixCotaOrigem,Usuario usuario);

	public void gerarCopiaMixProduto(List<MixProdutoDTO> mixProdutoOrigem, Usuario usuarioLogado);
	
	public MixCotaProduto obterMixPorCotaProduto(Long cotaId, Long produtoId);

	public BigInteger obterSomaReparteMinimoPorProdutoUsuario(Long produtoId, Long idUsuario);
}
