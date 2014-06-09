package br.com.abril.nds.integracao.ems0118.inbound;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;
/**
 * @author Jones.Costa
 * @version 1.0
 */
@Record
public class EMS0118Input implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String codigoPublicacao;
	private Long edicao;
	private BigDecimal preco;
	private boolean condRecolhimentoFinal;
	
	
	
	@Field(offset = 1, length = 8)
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}
	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}
	
	
	@Field(offset = 9, length = 4  )
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	
	@Field(offset = 13, length = 10  )
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	@Field(offset = 23, length = 1)
	@FixedFormatBoolean(trueValue = "S", falseValue = "N")
	public boolean isCondRecolhimentoFinal() {
		return condRecolhimentoFinal;
	}
	public void setCondRecolhimentoFinal(boolean condRecolhimentoFinal) {
		this.condRecolhimentoFinal = condRecolhimentoFinal;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoPublicacao == null) ? 0 : codigoPublicacao.hashCode());
		result = prime * result + (condRecolhimentoFinal ? 1231 : 1237);
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result + ((preco == null) ? 0 : preco.hashCode());
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
		EMS0118Input other = (EMS0118Input) obj;
		if (codigoPublicacao == null) {
			if (other.codigoPublicacao != null)
				return false;
		} else if (!codigoPublicacao.equals(other.codigoPublicacao))
			return false;
		if (condRecolhimentoFinal != other.condRecolhimentoFinal)
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (preco == null) {
			if (other.preco != null)
				return false;
		} else if (!preco.equals(other.preco))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EMS0118Input [codigoPublicacao=" + codigoPublicacao
				+ ", edicao=" + edicao + ", preco=" + preco
				+ ", condRecolhimentoFinal=" + condRecolhimentoFinal + "]";
	}
	
	
	


	
	

}
