package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VisaoEstoqueConferenciaCegaVO implements Serializable {

	private static final long serialVersionUID = -9048402471089783027L;

	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String lcto;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String rclto;
	
	
	private String qtde;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 8)
	private String valor;
	
	@Export(label = "Estoque", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String estoque;

	
	
	public VisaoEstoqueConferenciaCegaVO(VisaoEstoqueDetalheDTO dto) {
		this.setCodigo(dto.getCodigo());
		this.setProduto(dto.getProduto());
		this.setEdicao(dto.getEdicao());
		this.setPrecoCapa(dto.getPrecoCapa());
		this.setLcto(dto.getLcto());
		this.setRclto(dto.getRclto());
		this.setQtde(dto.getQtde());
		this.setValor(dto.getValor());
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

	public void setEdicao(String edicao) {
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

	public String getQtde() {
		return qtde;
	}

	public void setQtde(String qtde) {
		this.qtde = qtde;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getEstoque() {
		return estoque;
	}

	public void setEstoque(String estoque) {
		this.estoque = estoque;
	}
}
