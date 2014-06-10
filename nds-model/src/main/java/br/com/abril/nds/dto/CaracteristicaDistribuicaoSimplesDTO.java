package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
@Exportable
public class CaracteristicaDistribuicaoSimplesDTO implements Serializable{

	@Export(label="Codigo",exhibitionOrder=1)
	private String codigoProduto;
	
	@Export(label="Produto",exhibitionOrder=2)
	private String nomeProduto;
	
	@Export(label="Editor",exhibitionOrder=3)
	private String nomeEditor;
	
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
	public String getNomeEditor() {
		return nomeEditor;
	}
	public void setNomeEditor(String nomeEditor) {
		if(nomeEditor==null){
			this.nomeEditor="";
		}else{
			this.nomeEditor = nomeEditor;
		}
	}
		
	
	
}
