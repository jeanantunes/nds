package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VisaoEstoqueDetalheDTO implements Serializable {

	private static final long serialVersionUID = 5851726250053057711L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	private BigDecimal precoCapa;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String lcto;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String rclto;
	
	@Export(label = "Qtde", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private BigDecimal qtde;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 10)
	private BigDecimal valor;
	
	private String transferir;
	
	private String estoque;
	
	private String check;
	
	private String diferenca;
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public BigInteger getEdicao() {
		return edicao;
	}
	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
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
	public BigDecimal getQtde() {
		return qtde;
	}
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
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
