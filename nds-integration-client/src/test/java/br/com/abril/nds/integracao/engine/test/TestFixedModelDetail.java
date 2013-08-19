package br.com.abril.nds.integracao.engine.test;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class TestFixedModelDetail {
	private Integer number;
	private String campo1;
	private String campo2;
	private String campo3;
	private String campo4;
	private TestFixedModelHeader header;
	
	@Field(offset=2, length=8)
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@Field(offset=10, length=3)
	public String getCampo1() {
		return campo1;
	}
	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}
	
	@Field(offset=13, length=3)
	public String getCampo2() {
		return campo2;
	}
	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}
	
	@Field(offset=16, length=6)
	public String getCampo3() {
		return campo3;
	}
	public void setCampo3(String campo3) {
		this.campo3 = campo3;
	}
	
	@Field(offset=22, length=1)
	public String getCampo4() {
		return campo4;
	}
	public void setCampo4(String campo4) {
		this.campo4 = campo4;
	}
	
	public TestFixedModelHeader getHeader() {
		return header;
	}
	public void setHeader(TestFixedModelHeader header) {
		this.header = header;
	}
}