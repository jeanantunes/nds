package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @deprecated Mover os métodos para {@link FecharDiaService} e remover este service.
 * 
 */
@Deprecated
public interface ResumoEncalheFecharDiaService {


	ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao);
	
	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacao, PaginacaoVO paginacao);
	
	/**
	 * Retorna os dados das vendas de encalhe referentes o fechamento do dia
	 * 
	 * @param dataOperacao - data de operação
	 * 
	 * @return List<VendaFechamentoDiaDTO>
	 */
	List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao);
	
	/**
     * Conta o total de registros de {@link ProdutoEdicao} que tiveream o
     * encalhe conferido na data
     * 
     * @param data
     *            data para recuperação dos {@link ProdutoEdicao} com encalhe
     *            conferido na data
     * @return total de {@link ProdutoEdicao} com encalhe conferido na data
     **/
    Long contarProdutoEdicaoEncalhe(Date data);

}
