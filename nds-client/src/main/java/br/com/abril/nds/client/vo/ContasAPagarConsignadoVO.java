package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.util.CurrencyUtil;

public class ContasAPagarConsignadoVO {

	private String codigo;
	private String produto;
	private String edicao;
	private String precoCapa;
	private String precoComDesconto;
	private String reparteSugerido;
	private String reparteFinal;
	private String diferenca;
	private String motivo;
	private String fornecedor;
	private String valor;
	private String valorComDesconto;
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
