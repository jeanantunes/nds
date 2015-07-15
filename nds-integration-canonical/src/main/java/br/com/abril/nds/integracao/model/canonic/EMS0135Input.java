package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0135Input extends IntegracaoDocumentMaster<EMS0135InputItem> implements Serializable {

	private static final long serialVersionUID = -2655294342766612808L;
	
	private Integer distribuidor;
	
	private Date dataEmissao;
	
	private Long notaFiscal;
	
	private Long serieNotaFiscal;
	
	private String chaveAcessoNF;
	
	private String cnpjEmissor;
	
	private String numeroNotaEnvio;
	
	private List<EMS0135InputItem> item = new ArrayList<EMS0135InputItem>();
	
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
	@Field(offset = 16, length = 10)
	public Long getNotaFiscal() {
		return notaFiscal;
	}
	public void setNotaFiscal(Long notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	@Field(offset = 26, length = 3)
	public Long getSerieNotaFiscal() {
		return serieNotaFiscal;
	}
	public void setSerieNotaFiscal(Long serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}	
	
	@Field(offset = 124, length = 44)
	public String getChaveAcessoNF() {
		return chaveAcessoNF;
	}
	public void setChaveAcessoNF(String chaveAcessoNF) {
		this.chaveAcessoNF = chaveAcessoNF;
	}
	
	/**
	 * @return the cnpjEmissor
	 */
	@Field(offset = 169, length = 14)
	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	/**
	 * @param cnpjEmissor the cnpjEmissor to set
	 */
	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	/**
	 * @return the numeroNotaEnvio
	 */
	@Field(offset = 183, length = 6)
	public String getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(String numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}
	
	@Override
	public void addItem(IntegracaoDocumentDetail docD) {
		item.add((EMS0135InputItem) docD);		
	}
	
	@Override
	public List<EMS0135InputItem> getItems() {
		return item;
	}
	
	@Override
	public boolean sameObject(IntegracaoDocumentMaster<?> docM) {		
		return (null == docM || ((EMS0135Input)docM).getNotaFiscal() == null || ((EMS0135Input)docM).getNumeroNotaEnvio() == null
				? false : ( ((EMS0135Input)docM).getNotaFiscal().equals(this.notaFiscal)) && ((EMS0135Input)docM).getNumeroNotaEnvio().equals(this.numeroNotaEnvio));
	}	
}