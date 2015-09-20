package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.util.DateUtil;

public class ImpressaoBandeiraVO implements Serializable  {
	
	private static final long serialVersionUID = -8669011482729203456L;
	
	private String fornecedor;
	
	private String editor;
	
	private String semana;
	
	private String praca;
	
	private String canal;
	
	private String volumes;
	
	private String titulo;
	
	private String dataEnvio;
	
	private String chaveNFe;
	
	private String destino;
	
	public String getDestino() {
		if ( this.destino == null )
			return "";
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
		
	}

	public ImpressaoBandeiraVO(FornecedorDTO fornecedor, String volumes, Integer semana, Date dataEnvio, String editor, String chaveNFe,
			String destino) {
		
		this.titulo = "DEVOLUÇÃO AO FORNECEDOR";
		this.fornecedor = fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia() : fornecedor.getRazaoSocial();
		this.semana = "SEMANA " + (semana.toString().length() > 4 ? semana.toString().substring(4) : semana.toString());
		this.praca = fornecedor.getPraca();
		this.canal = fornecedor.getCanalDistribuicao() != null ? fornecedor.getCanalDistribuicao().getDescricao() : null;
		this.volumes = volumes;
		this.dataEnvio = DateUtil.formatarDataPTBR(dataEnvio);
		this.editor = editor;
		this.chaveNFe = chaveNFe;
		this.destino = destino;
	}

	public ImpressaoBandeiraVO(String fornecedor, String semana,
			String praca, String canal, String volumes, String dataEnvio, String titulo) {
		
		this.fornecedor = fornecedor == null? "" : fornecedor;
		this.semana = semana == null? "" : semana;
		this.praca = praca == null? "" : praca;
		this.canal = canal == null? "" : canal;
		this.volumes = volumes == null? "" : volumes;
		this.dataEnvio = dataEnvio;
		this.titulo = titulo;
		
	}
	
	public ImpressaoBandeiraVO(){}
	
	public String getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getSemana() {
		return semana;
	}
	
	public void setSemana(String semana) {
		this.semana = semana;
	}
	
	public String getPraca() {
		return praca;
	}
	
	public void setPraca(String praca) {
		this.praca = praca;
	}
	
	public String getCanal() {
		return canal;
	}
	
	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getVolumes() {
		return volumes;
	}

	public void setVolumes(String volumes) {
		this.volumes = volumes;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(String dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getForncedor() {
		return fornecedor;
	}

	public void setForncedor(String forncedor) {
		this.fornecedor = forncedor;
	}

	public String getChaveNFe() {
		return chaveNFe;
	}

	public void setChaveNFe(String chaveNFe) {
		this.chaveNFe = chaveNFe;
	}
	
}