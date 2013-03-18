<<<<<<< HEAD
package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;

public interface FixacaoReparteRepository  extends Repository<FixacaoReparte, Long> {
	
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO produto);
	
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO cota);
	
	public FixacaoReparte buscarPorId(Long id);
	
	public FixacaoReparte buscarPorProdutoCota(Cota cota, Produto produto);

}
=======
package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;

public interface FixacaoReparteRepository  extends Repository<FixacaoReparte, Long> {
	
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO produto);
	
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO cota);
	
	public FixacaoReparte buscarPorId(Long id);
	
	public FixacaoReparte buscarPorProdutoCota(Cota cota, Produto produto);
	
	public List<FixacaoReparte> buscarPorCota(Cota cota);
	
	public void removerPorCota(Cota cota);

}
>>>>>>> 03f1ca6c8da04a45696f13aca9cd81446f5232f7
