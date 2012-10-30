package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContasAPagarConsignadoVO {

	@Export(label="Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label="Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String produto;
	
	@Export(label="Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String edicao;
	
	@Export(label="Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label="Preço c/ Desc. R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String precoComDesconto;
	
	@Export(label="Reparte Sug.", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String reparteSugerido;
	
	@Export(label="Reparte Final", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String reparteFinal;
	
	@Export(label="Dif.", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String diferenca;
	
	@Export(label="Motivo", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String motivo;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String fornecedor;
	
	@Export(label="Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 11)
	private String valor;
	
	@Export(label="Valor c/ Desc. R$", alignment=Alignment.RIGHT, exhibitionOrder = 12)
	private String valorComDesconto;
	
	@Export(label="N° NF-e", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private String nfe;
	
	
	public ContasAPagarConsignadoVO()
	{}
	
	
	public ContasAPagarConsignadoVO(ContasAPagarConsignadoDTO dto) {
		
		this.codigo = dto.getCodigo();
		this.produto = dto.getProduto();
		this.edicao = dto.getEdicao().toString();
		this.precoCapa = CurrencyUtil.formatarValor(dto.getPrecoCapa());
		this.precoComDesconto = CurrencyUtil.formatarValor(dto.getPrecoComDesconto());
		this.reparteSugerido = dto.getReparteSugerido().toString();
		this.reparteFinal = dto.getReparteFinal().toString();
		this.diferenca = dto.getDiferenca().toString();
		this.motivo = dto.getMotivo();
		this.fornecedor = dto.getFornecedor();
		this.valor = CurrencyUtil.formatarValor(dto.getValor());
		this.valorComDesconto = CurrencyUtil.formatarValor(dto.getValorComDesconto());
		this.nfe = dto.getNfe();
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
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}
	public void setPrecoComDesconto(String precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	public String getReparteSugerido() {
		return reparteSugerido;
	}
	public void setReparteSugerido(String reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}
	public String getReparteFinal() {
		return reparteFinal;
	}
	public void setReparteFinal(String reparteFinal) {
		this.reparteFinal = reparteFinal;
	}
	public String getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(String diferenca) {
		this.diferenca = diferenca;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValorComDesconto() {
		return valorComDesconto;
	}
	public void setValorComDesconto(String valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}
	public String getNfe() {
		return nfe;
	}
	public void setNfe(String nfe) {
		this.nfe = nfe;
	}
}
