package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0140Input extends IntegracaoDocumentMaster<EMS0140InputItem> implements Serializable {

	private static final long serialVersionUID = -1611339433305392806L;

	private Integer distribuidor;
	
	private Date dataEmissao;
	
	private Long notaFiscal;
	
	private Long serieNotaFiscal;
	
	private String chaveAcessoNF;
	
	private String cnpjEmissor;
	
	private String numeroNotaEnvio;
	
	private List<EMS0140InputItem> itens = new ArrayList<EMS0140InputItem>();
	
	@Field(offset = 1, length = 7)
	public Integer getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Integer distribuidor) {
		this.distribuidor = distribuidor;
	}

	@Field(offset = 8, length = 8)
	@FixedFormatPattern("yyyyMMdd")  
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Master
	@Field(offset = 16, length = 8)
	public Long getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(Long notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	@Field(offset = 24, length = 3)
	public Long getSerieNotaFiscal() {
		return serieNotaFiscal;
	}

	public void setSerieNotaFiscal(Long serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}

	@Field(offset = 123, length = 44)
	public String getChaveAcessoNF() {
		return chaveAcessoNF;
	}

	public void setChaveAcessoNF(String chaveAcessoNF) {
		this.chaveAcessoNF = chaveAcessoNF;
	}

	@Field(offset = 167, length = 14)
	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	@Field(offset = 181, length = 8)
	public String getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(String numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public void setItem(List<EMS0140InputItem> item) {
		this.itens = item;
	}

	@Override
	public void addItem(IntegracaoDocumentDetail docD) {
		this.itens.add((EMS0140InputItem)docD);
		
	}

	@Override
	public List<EMS0140InputItem> getItems() {
		return this.itens;
	}

	@Override
	public boolean sameObject(IntegracaoDocumentMaster<?> docM) {
		return (null == docM ? false : ( ((EMS0140Input)docM).getNotaFiscal().equals(this.notaFiscal)) && ((EMS0140Input)docM).getNumeroNotaEnvio().equals(this.numeroNotaEnvio));
	}

}
