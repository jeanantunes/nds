package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NegociacaoDividaDTO implements Serializable {
	
	private static final long serialVersionUID = 8273101897954671949L;

	private Date dtEmissao;
	
	private Date dtVencimento;
	
	private Integer prazo;

	private BigDecimal vlDivida;

	private BigDecimal encargos;

	private BigDecimal total;
	
	private Long idCobranca;

	public Date getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public BigDecimal getVlDivida() {
		return vlDivida;
	}

	public void setVlDivida(BigDecimal vlDivida) {
		this.vlDivida = vlDivida;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getIdCobranca() {
		return idCobranca;
	}

	public void setIdCobranca(Long idCobranca) {
		this.idCobranca = idCobranca;
	}
}