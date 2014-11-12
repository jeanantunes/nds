package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.vo.PaginacaoVO;



public interface ResumoSuplementarFecharDiaService {

	BigDecimal obterValorEstoqueLogico(Date dataOperacao);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	BigDecimal obterValorVenda(Date dataOperacao);

	BigDecimal obterValorFisico(Date dataOperacao);

	List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date date,PaginacaoVO paginacao);
	
	ResumoSuplementarFecharDiaDTO obterResumoGeralSuplementar(Date dataOperacional);

	List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date data, PaginacaoVO paginacao);
	
	Long contarProdutoEdicaoSuplementar(Date dataFechamento);
	
    Long contarVendasSuplementar(Date data);

	BigDecimal obterValorAlteracaoPreco(Date dataOperacao);

}