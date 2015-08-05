package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroViewContaCorrenteDTO {
	
	
	@Export(label = "Periodo de")
	private Date inicioPeriodo;
	
	@Export(label = "Ate")
	private Date fimPeriodo;
	
	private PaginacaoVO paginacao = new PaginacaoVO();
	
	private	String colunaOrdenacao;
	
	

	

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public String getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	public void setColunaOrdenacao(String colunaOrdenacao) {
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

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		
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
		FiltroViewContaCorrenteDTO other = (FiltroViewContaCorrenteDTO) obj;
		if (colunaOrdenacao != other.colunaOrdenacao)
			return false;
		
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
}
