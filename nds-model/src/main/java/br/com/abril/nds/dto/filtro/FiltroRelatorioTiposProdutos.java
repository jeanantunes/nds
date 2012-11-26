package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroRelatorioTiposProdutos implements Serializable {

	private static final long serialVersionUID = 104473624178466067L;

	private Long tipoProduto;
	private Date dataLancamentoDe;
	private Date dataLancamentoAte;
	private Date dataRecolhimentoDe;
	private Date dataRecolhimentoAte;
	
	private PaginacaoVO paginacaoVO;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna{
		
		CODIGO("codigo"),
		NOME_PRODUTO("produto"),
		NUMERO_EDICAO("edicao"),
		PRECO_CAPA("precoCapa"),
		FATURAMENTO("faturamento"),
		TIPO_PRODUTO("tipoProduto"),
		DATA_LANCAMENTO("lancamento"),
		DATA_RECOLHIMENTO("recolhimento");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}
	
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	public Long getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(Long tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Date getDataLancamentoDe() {
		return dataLancamentoDe;
	}
	public void setDataLancamentoDe(Date dataLancamentoDe) {
		this.dataLancamentoDe = dataLancamentoDe;
	}
	public Date getDataLancamentoAte() {
		return dataLancamentoAte;
	}
	public void setDataLancamentoAte(Date dataLancamentoAte) {
		this.dataLancamentoAte = dataLancamentoAte;
	}
	public Date getDataRecolhimentoDe() {
		return dataRecolhimentoDe;
	}
	public void setDataRecolhimentoDe(Date dataRecolhimentoDe) {
		this.dataRecolhimentoDe = dataRecolhimentoDe;
	}
	public Date getDataRecolhimentoAte() {
		return dataRecolhimentoAte;
	}
	public void setDataRecolhimentoAte(Date dataRecolhimentoAte) {
		this.dataRecolhimentoAte = dataRecolhimentoAte;
	}
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataLancamentoAte == null) ? 0 : dataLancamentoAte
						.hashCode());
		result = prime
				* result
				+ ((dataLancamentoDe == null) ? 0 : dataLancamentoDe.hashCode());
		result = prime
				* result
				+ ((dataRecolhimentoAte == null) ? 0 : dataRecolhimentoAte
						.hashCode());
		result = prime
				* result
				+ ((dataRecolhimentoDe == null) ? 0 : dataRecolhimentoDe
						.hashCode());
		result = prime * result
				+ ((paginacaoVO == null) ? 0 : paginacaoVO.hashCode());
		result = prime * result
				+ ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
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
		FiltroRelatorioTiposProdutos other = (FiltroRelatorioTiposProdutos) obj;
		if (dataLancamentoAte == null) {
			if (other.dataLancamentoAte != null)
				return false;
		} else if (!dataLancamentoAte.equals(other.dataLancamentoAte))
			return false;
		if (dataLancamentoDe == null) {
			if (other.dataLancamentoDe != null)
				return false;
		} else if (!dataLancamentoDe.equals(other.dataLancamentoDe))
			return false;
		if (dataRecolhimentoAte == null) {
			if (other.dataRecolhimentoAte != null)
				return false;
		} else if (!dataRecolhimentoAte.equals(other.dataRecolhimentoAte))
			return false;
		if (dataRecolhimentoDe == null) {
			if (other.dataRecolhimentoDe != null)
				return false;
		} else if (!dataRecolhimentoDe.equals(other.dataRecolhimentoDe))
			return false;
		if (paginacaoVO == null) {
			if (other.paginacaoVO != null)
				return false;
		} else if (!paginacaoVO.equals(other.paginacaoVO))
			return false;
		if (tipoProduto == null) {
			if (other.tipoProduto != null)
				return false;
		} else if (!tipoProduto.equals(other.tipoProduto))
			return false;
		return true;
	}
	
	
}
