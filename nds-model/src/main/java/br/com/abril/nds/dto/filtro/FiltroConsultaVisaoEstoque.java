package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroConsultaVisaoEstoque implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean buscaHistorico;
	private Boolean paginar;
	private Date dataMovimentacao;
	private String dataMovimentacaoStr;
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
	public String getDataMovimentacaoStr() {
		return dataMovimentacaoStr;
	}
	public void setDataMovimentacaoStr(String dataMovimentacaoStr) {
		this.dataMovimentacaoStr = dataMovimentacaoStr;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataMovimentacao == null) ? 0 : dataMovimentacao.hashCode());
		result = prime
				* result
				+ ((dataMovimentacaoStr == null) ? 0 : dataMovimentacaoStr
						.hashCode());
		result = prime
				* result
				+ ((grupoMovimentoEntrada == null) ? 0 : grupoMovimentoEntrada
						.hashCode());
		result = prime
				* result
				+ ((grupoMovimentoSaida == null) ? 0 : grupoMovimentoSaida
						.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime
				* result
				+ ((listaTransferencia == null) ? 0 : listaTransferencia
						.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result + ((paginar == null) ? 0 : paginar.hashCode());
		result = prime * result
				+ ((tipoEstoque == null) ? 0 : tipoEstoque.hashCode());
		result = prime
				* result
				+ ((tipoEstoqueSelecionado == null) ? 0
						: tipoEstoqueSelecionado.hashCode());
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
		if (dataMovimentacaoStr == null) {
			if (other.dataMovimentacaoStr != null)
				return false;
		} else if (!dataMovimentacaoStr.equals(other.dataMovimentacaoStr))
			return false;
		if (grupoMovimentoEntrada != other.grupoMovimentoEntrada)
			return false;
		if (grupoMovimentoSaida != other.grupoMovimentoSaida)
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (listaTransferencia == null) {
			if (other.listaTransferencia != null)
				return false;
		} else if (!listaTransferencia.equals(other.listaTransferencia))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (paginar == null) {
			if (other.paginar != null)
				return false;
		} else if (!paginar.equals(other.paginar))
			return false;
		if (tipoEstoque == null) {
			if (other.tipoEstoque != null)
				return false;
		} else if (!tipoEstoque.equals(other.tipoEstoque))
			return false;
		if (tipoEstoqueSelecionado == null) {
			if (other.tipoEstoqueSelecionado != null)
				return false;
		} else if (!tipoEstoqueSelecionado.equals(other.tipoEstoqueSelecionado))
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
	/**
	 * @return the buscaHistorico
	 */
	public boolean isBuscaHistorico() {
		return buscaHistorico;
	}
	/**
	 * @param buscaHistorico the buscaHistorico to set
	 */
	public void setBuscaHistorico(boolean buscaHistorico) {
		this.buscaHistorico = buscaHistorico;
	}
}