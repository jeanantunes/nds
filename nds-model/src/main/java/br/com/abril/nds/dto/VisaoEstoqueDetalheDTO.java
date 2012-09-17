package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VisaoEstoqueDetalheDTO implements Serializable {

	private static final long serialVersionUID = 5851726250053057711L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private Long edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	private String precoCapa;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String lcto;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String rclto;
	
	@Export(label = "Qtde", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private Long qtde;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 10)
	private String valor;
	
	private String transferir;
	
	private String estoque;
	
	private String check;
	
	private String diferenca;
	
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public String getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}
	public String getLcto() {
		return lcto;
	}
	public void setLcto(String lcto) {
		this.lcto = lcto;
	}
	public String getRclto() {
		return rclto;
	}
	public void setRclto(String rclto) {
		this.rclto = rclto;
	}
	public Long getQtde() {
		return qtde;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTransferir() {
		return transferir;
	}
	public void setTransferir(String transferir) {
		this.transferir = transferir;
	}
	public String getEstoque() {
		return estoque;
	}
	public void setEstoque(String estoque) {
		this.estoque = estoque;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(String diferenca) {
		this.diferenca = diferenca;
	}
}
