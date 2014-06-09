package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsolidadoVendaCotaDTO {
	
	
	@Export(label="Data")
	private Date dataConsolidado;
	
	@Export(label="Cota")
	private String cota;	
	
	private Long idConsolidado;
	
	private Integer numeroCota;
			
	public Long getIdConsolidado() {
		return idConsolidado;
	}

	public void setIdConsolidado(Long idConsolidado) {
		this.idConsolidado = idConsolidado;
	}

	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 	
	 */
	public enum OrdenacaoColuna {
		
		codigoProduto,
		nomeProduto,
		numeroEdicao,
		precoCapa,
		precoComDesconto,
		box,
		exemplares,
		nomeFornecedor,
		total;	
	}	
	
	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}

	

	public String getCota() {
		return cota;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataConsolidado == null) ? 0 : dataConsolidado.hashCode());
		result = prime * result
				+ ((cota == null) ? 0 : cota.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
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
		FiltroConsolidadoVendaCotaDTO other = (FiltroConsolidadoVendaCotaDTO) obj;
		if (dataConsolidado == null) {
			if (other.dataConsolidado != null)
				return false;
		} else if (!dataConsolidado.equals(other.dataConsolidado))
			return false;
		if (cota == null) {
			if (other.cota != null)
				return false;
		} else if (!cota.equals(other.cota))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	

}
