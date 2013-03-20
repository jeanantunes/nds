package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroConsultaVisaoEstoque implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean paginar;
	private Date dataMovimentacao;
	private Long idFornecedor;
	private String tipoEstoque;
	private String tipoEstoqueSelecionado;
	
	private GrupoMovimentoEstoque grupoMovimentoEntrada;
	private GrupoMovimentoEstoque grupoMovimentoSaida;
	
	List<VisaoEstoqueTransferenciaDTO> listaTransferencia;
	
	private PaginacaoVO paginacao;


	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}
	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public String getTipoEstoque() {
		return tipoEstoque;
	}
	public void setTipoEstoque(String tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}
	public String getTipoEstoqueSelecionado() {
		return tipoEstoqueSelecionado;
	}
	public void setTipoEstoqueSelecionado(String tipoEstoqueSelecionado) {
		this.tipoEstoqueSelecionado = tipoEstoqueSelecionado;
	}
	public List<VisaoEstoqueTransferenciaDTO> getListaTransferencia() {
		return listaTransferencia;
	}
	public void setListaTransferencia(List<VisaoEstoqueTransferenciaDTO> listaTransferencia) {
		this.listaTransferencia = listaTransferencia;
	}
	
	/**
	 * @return the grupoMovimentoEntrada
	 */
	public GrupoMovimentoEstoque getGrupoMovimentoEntrada() {
		return grupoMovimentoEntrada;
	}
	/**
	 * @param grupoMovimentoEntrada the grupoMovimentoEntrada to set
	 */
	public void setGrupoMovimentoEntrada(GrupoMovimentoEstoque grupoMovimentoEntrada) {
		this.grupoMovimentoEntrada = grupoMovimentoEntrada;
	}
	/**
	 * @return the grupoMovimentoSaida
	 */
	public GrupoMovimentoEstoque getGrupoMovimentoSaida() {
		return grupoMovimentoSaida;
	}
	/**
	 * @param grupoMovimentoSaida the grupoMovimentoSaida to set
	 */
	public void setGrupoMovimentoSaida(GrupoMovimentoEstoque grupoMovimentoSaida) {
		this.grupoMovimentoSaida = grupoMovimentoSaida;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataMovimentacao == null) ? 0 : dataMovimentacao.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroConsultaVisaoEstoque other = (FiltroConsultaVisaoEstoque) obj;
		if (dataMovimentacao == null) {
			if (other.dataMovimentacao != null)
				return false;
		} else if (!dataMovimentacao.equals(other.dataMovimentacao))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		return true;
	}
	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	public Boolean getPaginar() {
		return paginar;
	}
	public void setPaginar(Boolean paginar) {
		this.paginar = paginar;
	}
}
