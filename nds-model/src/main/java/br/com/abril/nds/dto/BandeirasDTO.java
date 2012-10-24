package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class BandeirasDTO  implements Serializable{

	private static final long serialVersionUID = -7847418746083619011L;
	
	private String codProduto;
	private String nomeProduto;
	private Long edProduto;
	private Integer pctPadrao;
	
	//TODO De onde vem?
	private String destino;
	//TODO De onde vem?
	private Integer prioridade;

	private BigInteger qtde;
	
	private Date data;

	
	public BandeirasDTO() {
		
	}
	
	public BandeirasDTO(String codProduto, String nomeProduto,
			Long edProduto, Integer pctPadrao) {
		super();
		this.codProduto = codProduto;
		this.nomeProduto = nomeProduto;
		this.edProduto = edProduto;
		this.pctPadrao = pctPadrao;
	}

	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public Integer getPctPadrao() {
		return pctPadrao;
	}
	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}

	public Long getEdProduto() {
		return edProduto;
	}

	public void setEdProduto(Long edProduto) {
		this.edProduto = edProduto;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Integer getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}	
}
