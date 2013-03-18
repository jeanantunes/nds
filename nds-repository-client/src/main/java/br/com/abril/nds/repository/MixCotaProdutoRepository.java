<<<<<<< HEAD
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
	
	public void  excluirTodos();
	
	public boolean existeMixCotaProdutoCadastrado(Long idProduto, Long idCota);

}
=======
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
	
	public void  excluirTodos();
	
	public boolean existeMixCotaProdutoCadastrado(Long idProduto, Long idCota);

	public void removerPorIdCota(Long idCota);

}
>>>>>>> 03f1ca6c8da04a45696f13aca9cd81446f5232f7
