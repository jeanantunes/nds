package br.com.abril.nds.repository;

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
	
	VisaoEstoqueDetalheDTO obterLancamentoDetalhe(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDetalheJuramentadoDTO obterLancamentoJuramentadoDetalhe(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDetalheDTO obterSuplementarDetalhe(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDetalheDTO obterRecolhimentoDetalhe(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDetalheDTO obterProdutosDanificadosDetalhe(FiltroConsultaVisaoEstoque filtro);
	
	
}
