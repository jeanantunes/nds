package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsolidadoConsignadoCotaDTO {
	
	@Export(label="Data")
	private Date dataConsolidado;
	
	@Export(label="Numero Cota")
	private Integer numeroCota;	
			
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	private Long lineId;
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 	
	 */
	public enum OrdenacaoColuna {
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_CAPA("precoVenda"),
		PRECO_COM_DESCONTO("precoComDesconto"),
		REPARTE_SUGERIDO("reparteSugerido"),
		REPARTE_FINAL("reparteFinal"),
		DIFERENCA("diferenca"),
		MOTIVO("motivo"),		
		FORNECEDOR("fornecedor"),
		TOTAL("total");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
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

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataConsolidado == null) ? 0 : dataConsolidado.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		FiltroConsolidadoConsignadoCotaDTO other = (FiltroConsolidadoConsignadoCotaDTO) obj;
		if (dataConsolidado == null) {
			if (other.dataConsolidado != null)
				return false;
		} else if (!dataConsolidado.equals(other.dataConsolidado))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
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
		
}
