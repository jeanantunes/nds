package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ConsultaConsignadoCotaPeloFornecedorDTO implements Serializable {

	private static final long serialVersionUID = -6080837731839488665L;
	
	@Export(label = "Cota" , alignment= Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome" , alignment= Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Consignado Total" , alignment= Alignment.CENTER, exhibitionOrder = 3)
	private BigInteger consignado;
	
	private BigDecimal total;
	private BigDecimal totalDesconto;
	
	@Export(label = "Fornecedor" , alignment= Alignment.CENTER, exhibitionOrder = 7)
	private String nomeFornecedor;
	private Long idFornecedor;
	
	private String totalFormatado;
	private String totalDescontoFormatado;
	
	public ConsultaConsignadoCotaPeloFornecedorDTO() { }
	
	public ConsultaConsignadoCotaPeloFornecedorDTO(Integer numeroCota,
			String nomeCota, BigInteger consignado, BigDecimal total,
			BigDecimal totalDesconto, String nomeFornecedor, Long idFornecedor) {
		super();
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.consignado = consignado;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.nomeFornecedor = nomeFornecedor;
		this.idFornecedor = idFornecedor;
	}




	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigInteger getConsignado() {
		return consignado;
	}

	public void setConsignado(BigInteger consignado) {
		this.consignado = consignado;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
		if(total != null){
			totalFormatado = CurrencyUtil.formatarValor(total);
		}
	}
	
	@Export(label = "Total $", alignment= Alignment.RIGHT, exhibitionOrder = 5)
	public String getTotalFormatado(){
		return totalFormatado;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}
	
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
		if(totalDesconto != null){
			totalDescontoFormatado = CurrencyUtil.formatarValor(totalDesconto);
		}
	}
	
	@Export(label = "Total Desc $", alignment= Alignment.RIGHT, exhibitionOrder = 6)
	public String getTotalDescontoFormatado(){
		return totalDescontoFormatado;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
}
