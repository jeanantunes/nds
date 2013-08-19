package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking2Trailer {

	private String tipoRegistro;
	
	private Integer codigoCota;
	
	private BigDecimal valorTotalBruto;
	
	private BigDecimal valorTotalDesconto;
	
	private String campoFixo1;
	
	private String campoFixo2;
	
	private String campoFixo3;
	
	private String campoFixo4;
	
	private String separador1;
	
	private String separador2;
	
	private String separador3;
	
	private String separador4;
	
	private String separador5;
	
	private String separador6;
	
	private String separador7;
	
	private String separador8;
	
	
	@Field(offset = 1, length = 1, paddingChar = '3')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 1, paddingChar = ';')
	public String getSeparador1() {
		return separador1;
	}
	
	@Field(offset = 3, length = 4, align = Align.RIGHT)
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 1, paddingChar = ';')
	public String getSeparador2() {
		return separador2;
	}
	
	@Field(offset = 8, length = 10, align = Align.RIGHT)
	public BigDecimal getValorTotalBruto() {
		return valorTotalBruto;
	}
	
	@Field(offset = 18, length = 1, paddingChar = ';')
	public String getSeparador3() {
		return separador3;
	}
	
	@Field(offset = 19, length = 10, align = Align.RIGHT)
	public BigDecimal getValorTotalDesconto() {
		return valorTotalDesconto;
	}
	
	@Field(offset = 29, length = 1, paddingChar = ';')
	public String getSeparador4() {
		return separador4;
	}
	
	@Field(offset = 30, length = 10, paddingChar = '0')
	public String getCampoFixo1() {
		return campoFixo1;
	}
	
	@Field(offset = 40, length = 1, paddingChar = ';')
	public String getSeparador5() {
		return separador5;
	}
	
	@Field(offset = 41, length = 10, paddingChar = '0')
	public String getCampoFixo2() {
		return campoFixo2;
	}
	
	@Field(offset = 51, length = 1, paddingChar = ';')
	public String getSeparador6() {
		return separador6;
	}
	
	@Field(offset = 52, length = 8, paddingChar = '0')
	public String getCampoFixo3() {
		return campoFixo3;
	}
	
	@Field(offset = 60, length = 1, paddingChar = ';')
	public String getSeparador7() {
		return separador7;
	}
	
	@Field(offset = 61, length = 10, paddingChar = '0')
	public String getCampoFixo4() {
		return campoFixo4;
	}
	
	@Field(offset = 71, length = 1, paddingChar = ';')
	public String getSeparador8() {
		return separador8;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setValorTotalBruto(BigDecimal valorTotalBruto) {
		this.valorTotalBruto = valorTotalBruto;
	}

	public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}
}
