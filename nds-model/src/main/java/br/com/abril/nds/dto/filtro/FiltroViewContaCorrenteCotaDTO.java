package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroViewContaCorrenteCotaDTO {
	
	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome")
	private String nomeCota;
	
	private Date inicioPeriodo;
	
	private Date fimPeriodo;
	
	private PaginacaoVO paginacao = new PaginacaoVO();
	
	private	ColunaOrdenacao colunaOrdenacao;
	
	public enum ColunaOrdenacao {

		DT_CONSOLIDADO("data"),
		VALOR_POSTERGADO("vlrpostergado"),
		NUMERO_ATRASADOS("na"),
		CONSIGNADO("consignadoaVencer"),
		ENCALHE("encalhe"),
		VENDA_ENCALHE("vendaEncalhe"),
		DEBITO_CREDITO("debCred"),	
		ENCARGOS("encargos"),
		PENDENTE("pendente"),
		TOTAL("total");

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
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

	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}
	
	/**
	 * @return the inicioPeriodo
	 */
	public Date getInicioPeriodo() {
		return inicioPeriodo;
	}

	/**
	 * @param inicioPeriodo the inicioPeriodo to set
	 */
	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}

	/**
	 * @return the fimPeriodo
	 */
	public Date getFimPeriodo() {
		return fimPeriodo;
	}

	/**
	 * @param fimPeriodo the fimPeriodo to set
	 */
	public void setFimPeriodo(Date fimPeriodo) {
		this.fimPeriodo = fimPeriodo;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		FiltroViewContaCorrenteCotaDTO other = (FiltroViewContaCorrenteCotaDTO) obj;
		if (colunaOrdenacao != other.colunaOrdenacao)
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
}
