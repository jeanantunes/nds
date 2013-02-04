package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;


public interface VisaoEstoqueRepository {

	VisaoEstoqueDTO obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro);
	
	VisaoEstoqueDTO obterVisaoEstoqueHistorico(FiltroConsultaVisaoEstoque filtro);
	
	VisaoEstoqueDTO obterVisaoEstoqueJuramentado(FiltroConsultaVisaoEstoque filtro);
	
	List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro);
	
	List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalheHistorico(FiltroConsultaVisaoEstoque filtro);
	
	List<VisaoEstoqueDetalheJuramentadoDTO> obterVisaoEstoqueDetalheJuramentado(FiltroConsultaVisaoEstoque filtro);

	public abstract BigInteger obterQuantidadeEstoqueJuramentado(long idProdutoEdicao);

	public abstract BigInteger obterQuantidadeEstoqueHistorico(long idProdutoEdicao,
			String tipoEstoque);

	public abstract BigInteger obterQuantidadeEstoque(long idProdutoEdicao, String tipoEstoque);
}
