package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @deprecated Repositórios devem estar associados a uma entidade no modelo.
 * Considerar refatoração, extinguindo este repositório e enviando os métodos para
 * repositórios que estejam "associados" com as consultas efetuadas
 * 
 */
@Deprecated
public interface ResumoEncalheFecharDiaRepository {

	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao);

	List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao, PaginacaoVO paginacao);
	
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
    
    /**
     * Resumo com informações de encalhe na data
     * 
     * @param data
     *            data para consulta das informções de encalhe
     * @return DTO com as informações do encalhe da data
     */
    ResumoEncalheFecharDiaDTO obterResumoEncalhe(Date data);
    
    /**
     * Conta as vendas de encalhe na data
     * 
     * @param data
     *            data para contagem das vendas de encalhe
     * @return total de vendas de encalhe na data
     */
    Long contarVendasEncalhe(Date data);

}
