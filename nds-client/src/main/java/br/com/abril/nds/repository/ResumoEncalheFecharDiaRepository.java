package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
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

	BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada);

	BigDecimal obterValorEncalheLogico(Date dataOperacao);

	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao);

	BigDecimal obterValorFaltasOuSobras(Date dataOperacao, StatusAprovacao status);

	List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao);

	BigDecimal obterValorVendaEncalhe(Date dataOperacao);
	
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
