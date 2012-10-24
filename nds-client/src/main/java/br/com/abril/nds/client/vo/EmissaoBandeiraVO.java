package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class EmissaoBandeiraVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2350932999027090788L;

	
	@Export(label="Data", alignment=Export.Alignment.RIGHT, exhibitionOrder=1)
	private String data;
	
	@Export(label="Cód. Produto", alignment=Export.Alignment.LEFT, exhibitionOrder=2)
	private String codProduto;
	
	@Export(label="Edição", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private String edProduto;
	
	@Export(label="Produto", alignment=Export.Alignment.LEFT, exhibitionOrder=4)
	private String nomeProduto;
	
	@Export(label="Destino", alignment=Export.Alignment.RIGHT, exhibitionOrder=5)
	private String destino;
	
	@Export(label="Prioridade", alignment=Export.Alignment.RIGHT, exhibitionOrder=6)
	private String prioridade;
	
	
	@Export(label="Qtde. Solicitada", alignment=Export.Alignment.RIGHT, exhibitionOrder=7)
	private String qtde;
	
	private String pctPadrao;
		
	

	
	public EmissaoBandeiraVO()
	{}
	
	
	public EmissaoBandeiraVO(BandeirasDTO dto) {
		this.setCodProduto(dto.getCodProduto());
		this.setNomeProduto(dto.getNomeProduto());
		this.setEdProduto(dto.getEdProduto()==null? "":dto.getEdProduto().toString());
		this.setPctPadrao(dto.getPctPadrao()==null? "":dto.getPctPadrao().toString());
		this.setData(DateUtil.formatarDataPTBR(dto.getData()));
		this.setDestino(dto.getDestino());
		this.setPrioridade(dto.getPrioridade()==null? "":dto.getPrioridade().toString());
		this.setQtde(dto.getQtde()==null? "":dto.getQtde().toString());
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getCodProduto() {
		return codProduto;
	}


	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}


	public String getEdProduto() {
		return edProduto;
	}


	public void setEdProduto(String edProduto) {
		this.edProduto = edProduto;
	}


	public String getNomeProduto() {
		return nomeProduto;
	}


	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}


	public String getDestino() {
		return destino;
	}


	public void setDestino(String destino) {
		this.destino = destino;
	}


	public String getPrioridade() {
		return prioridade;
	}


	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}


	public String getQtde() {
		return qtde;
	}


	public void setQtde(String qtde) {
		this.qtde = qtde;
	}


	public String getPctPadrao() {
		return pctPadrao;
	}


	public void setPctPadrao(String pctPadrao) {
		this.pctPadrao = pctPadrao;
	}




	

}
