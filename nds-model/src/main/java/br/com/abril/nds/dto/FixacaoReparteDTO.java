package br.com.abril.nds.dto;




public class FixacaoReparteDTO {
	
	private Long id;
	private Long cotaFixada;
	private Long produtoFixado;
	private String usuario;
	private Integer qtdeEdicoes;
	private Integer qtdeExemplares;
	private String data;
	private String hora;
	private String nomeCota;
	private String edicaoInicial;
	private String edicaoFinal;
	private String edicoesAtendidas;
	
	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(Integer qtdeEdicoes) {
		this.qtdeEdicoes = qtdeEdicoes;
	}

	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}

	public void setQtdeExemplares(Integer qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCotaFixada() {
		return cotaFixada;
	}

	public void setCotaFixada(Long cotaFixada) {
		this.cotaFixada = cotaFixada;
	}

	public Long getProdutoFixado() {
		return produtoFixado;
	}

	public void setProdutoFixado(Long produtoFixado) {
		this.produtoFixado = produtoFixado;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getEdicaoInicial() {
		return edicaoInicial;
	}

	public void setEdicaoInicial(String edicaoInicial) {
		this.edicaoInicial = edicaoInicial;
	}

	public String getEdicaoFinal() {
		return edicaoFinal;
	}

	public void setEdicaoFinal(String edicaoFinal) {
		this.edicaoFinal = edicaoFinal;
	}

	public String getEdicoesAtendidas() {
		return edicoesAtendidas;
	}

	public void setEdicoesAtendidas(String edicoesAtendidas) {
		this.edicoesAtendidas = edicoesAtendidas;
	}
	

}
