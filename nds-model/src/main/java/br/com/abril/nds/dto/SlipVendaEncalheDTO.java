package br.com.abril.nds.dto;

public class SlipVendaEncalheDTO {

	//CABEÇALHO SLIP
	String numeroCota;
	String nomeCota;
	String numeroBox;
	String descricaoBox;
	String data;
	String hora;
	String usuario;
	
	//ITEM SLIP
	String codigo;
	String produto;
	String edicao;
	String preco;
	String quantidade;
	String total;
	
	//TOTALIZAÇÕES
	String quantidadeTotalVista;
	String valorTotalVista;
	String quantidadeTotalPrazo;
	String valorTotalPrazo;
	String quantidadeTotalGeral;
	String valorTotalGeral;
	
	public String getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getNomeCota() {
		return nomeCota;
	}
	
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
	public String getNumeroBox() {
		return numeroBox;
	}
	
	public void setNumeroBox(String numeroBox) {
		this.numeroBox = numeroBox;
	}
	
	public String getDescricaoBox() {
		return descricaoBox;
	}
	
	public void setDescricaoBox(String descricaoBox) {
		this.descricaoBox = descricaoBox;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	
	public String getPreco() {
		return preco;
	}
	
	public void setPreco(String preco) {
		this.preco = preco;
	}
	
	public String getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getTotal() {
		return total;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}

	public String getQuantidadeTotalVista() {
		return quantidadeTotalVista;
	}

	public void setQuantidadeTotalVista(String quantidadeTotalVista) {
		this.quantidadeTotalVista = quantidadeTotalVista;
	}

	public String getValorTotalVista() {
		return valorTotalVista;
	}

	public void setValorTotalVista(String valorTotalVista) {
		this.valorTotalVista = valorTotalVista;
	}

	public String getQuantidadeTotalPrazo() {
		return quantidadeTotalPrazo;
	}

	public void setQuantidadeTotalPrazo(String quantidadeTotalPrazo) {
		this.quantidadeTotalPrazo = quantidadeTotalPrazo;
	}

	public String getValorTotalPrazo() {
		return valorTotalPrazo;
	}

	public void setValorTotalPrazo(String valorTotalPrazo) {
		this.valorTotalPrazo = valorTotalPrazo;
	}

	public String getQuantidadeTotalGeral() {
		return quantidadeTotalGeral;
	}

	public void setQuantidadeTotalGeral(String quantidadeTotalGeral) {
		this.quantidadeTotalGeral = quantidadeTotalGeral;
	}

	public String getValorTotalGeral() {
		return valorTotalGeral;
	}

	public void setValorTotalGeral(String valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}
	
}
