package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;


public interface VisaoEstoqueRepository {

	VisaoEstoqueDTO obterLancamento(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterLancamentoJuramentado(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterSuplementar(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterRecolhimento(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterProdutosDanificados(FiltroConsultaVisaoEstoque filtro);
	
	List<VisaoEstoqueDetalheDTO> obterLancamentoDetalhe(FiltroConsultaVisaoEstoque filtro);
	List<VisaoEstoqueDetalheJuramentadoDTO> obterLancamentoJuramentadoDetalhe(FiltroConsultaVisaoEstoque filtro);
	List<VisaoEstoqueDetalheDTO> obterSuplementarDetalhe(FiltroConsultaVisaoEstoque filtro);
	List<VisaoEstoqueDetalheDTO> obterRecolhimentoDetalhe(FiltroConsultaVisaoEstoque filtro);
	List<VisaoEstoqueDetalheDTO> obterProdutosDanificadosDetalhe(FiltroConsultaVisaoEstoque filtro);
	
	
}
