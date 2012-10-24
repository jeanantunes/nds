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
	private String codigoProduto;
	
	@Export(label="Edição", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private String edicao;
	
	@Export(label="Produto", alignment=Export.Alignment.LEFT, exhibitionOrder=4)
	private String nomeProduto;
	
	@Export(label="Destino", alignment=Export.Alignment.RIGHT, exhibitionOrder=5)
	private String destino;
	
	@Export(label="Prioridade", alignment=Export.Alignment.RIGHT, exhibitionOrder=6)
	private String prioridade;
	
	
	@Export(label="Qtde. Solicitada", alignment=Export.Alignment.RIGHT, exhibitionOrder=7)
	private String pacote;
		
	
	

	
	public EmissaoBandeiraVO()
	{}
	
	
	public EmissaoBandeiraVO(BandeirasDTO dto) {
		this.setCodigoProduto(dto.getCodProduto());
		this.setNomeProduto(dto.getNomeProduto());
		this.setEdicao(dto.getEdProduto().toString());
		this.setPacote(dto.getPctPadrao().toString());
		this.setData(DateUtil.formatarDataPTBR(dto.getData()));
		this.setDestino(dto.getDestino());
		this.setPrioridade(dto.getPrioridade().toString());
	}


	public String getCodigoProduto() {
		return codigoProduto;
	}


	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}


	public String getNomeProduto() {
		return nomeProduto;
	}


	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}


	public String getEdicao() {
		return edicao;
	}


	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}


	public String getPacote() {
		return pacote;
	}


	public void setPacote(String pacote) {
		this.pacote = pacote;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
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



	

}
