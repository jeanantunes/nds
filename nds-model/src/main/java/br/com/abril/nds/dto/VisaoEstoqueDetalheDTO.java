package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VisaoEstoqueDetalheDTO implements Serializable {

	private static final long serialVersionUID = 5851726250053057711L;
	
	private Long produtoEdicaoId;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	private String precoCapa;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String lcto;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String rclto;
	
	@Export(label = "Qtde", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String qtde;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 10)
	private String valor;
	
	private String transferir;
	
	private String estoque;
	
	private String check;
	
	private String diferenca;
	
	
	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}
	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}
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
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao.toString();
	}
	public String getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = CurrencyUtil.formatarValor(precoCapa);
	}
	public String getLcto() {
		return lcto;
	}
	public void setLcto(Date lcto) {
		this.lcto = DateUtil.formatarDataPTBR(lcto);
	}
	public String getRclto() {
		return rclto;
	}
	public void setRclto(Date rclto) {
		this.rclto = DateUtil.formatarDataPTBR(rclto);
	}
	public String getQtde() {
		return qtde;
	}
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde.toString();
	}
	public String getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = CurrencyUtil.formatarValor(valor);
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
