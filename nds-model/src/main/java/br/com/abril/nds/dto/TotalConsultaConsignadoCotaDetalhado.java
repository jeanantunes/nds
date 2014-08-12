package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TotalConsultaConsignadoCotaDetalhado implements Serializable {
	
	private static final long serialVersionUID = -7690195221366930002L;
	
	private BigDecimal total;
	private String nomeFornecedor;
	
	public TotalConsultaConsignadoCotaDetalhado() {}

	public TotalConsultaConsignadoCotaDetalhado(BigDecimal total,
			String nomeFornecedor) {
		super();
		this.total = total;
		this.nomeFornecedor = nomeFornecedor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getTotalFormatado() {
		return CurrencyUtil.formatarValor(total);
	}


}
