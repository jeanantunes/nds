package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;


public interface MovimentoEstoqueCotaRepository extends Repository<MovimentoEstoqueCota, Long> {

	public Integer obterQtdProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento, boolean indQtdEncalheAposPrimeiroDia);
	
	public BigDecimal obterQtdItemProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento, boolean indQtdEncalheAposPrimeiroDia);
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque, 
			boolean indBuscaTotalParcial);
	
	public Integer obterQuantidadeContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque);
	
	public BigDecimal obterValorTotalGeralContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque);
	
	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(
			Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
	
	public Integer obterQtdConsultaEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento);
	
	public  List<ConsultaEncalheDTO> obterListaConsultaEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento);
	
}
