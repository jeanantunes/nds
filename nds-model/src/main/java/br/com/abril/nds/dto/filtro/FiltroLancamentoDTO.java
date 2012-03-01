package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * Filtro para consulta de lançamentos 
 * @author francisco.garcia
 *
 */
public class FiltroLancamentoDTO implements Serializable {

	private static final long serialVersionUID = -2638836889195236797L;

	private PeriodoVO periodo;
	private List<Long> idsFornecedores = new ArrayList<Long>() ;
	private PaginacaoVO paginacao;
	
	public PeriodoVO getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(PeriodoVO periodo) {
		this.periodo = periodo;
	}
	
	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}
	
	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}
	
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	public boolean filtraFornecedores() {
		return idsFornecedores != null && !idsFornecedores.isEmpty();
	}
	
}
