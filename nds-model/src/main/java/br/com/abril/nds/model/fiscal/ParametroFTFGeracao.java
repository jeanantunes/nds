package br.com.abril.nds.model.fiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="PARAMETROS_FTF_GERACAO")
public class ParametroFTFGeracao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -235863705621160503L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private long id;
	
	@Column(name="NOME")
	private String nome;
	
	@Column(name="CENTRO_EMISSOR")
	private String centroEmissor;
	
	@Column(name="CNPJ_EMISSOR")
	private String cnpjEmissor;
	
	@Column(name="CNPJ_DESTINATARIO")
	private String cnpjDestinatario; 
	
	@Column(name="ESTABELECIMENTO")
	private String estabelecimento; 
	
	@Column(name="TIPO_PEDIDO")
	private String tipoPedido; 
	
	@Column(name="CODIGO_SOLICITANTE")
	private String codigoSolicitante; 
	
	@Column(name="MENSAGEM")
	private String mensagem; 
	
	@OneToOne
	@JoinColumn(name="NATUREZA_OPERACAO_ID", updatable=true, insertable=true)
	private NaturezaOperacao naturezaOperacao; 
	
	@OneToOne
	@JoinColumn(name="CFOP_ID", updatable=true, insertable=true)
	private CFOP cfop; 
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCentroEmissor() {
		return centroEmissor;
	}

	public void setCentroEmissor(String centroEmissor) {
		this.centroEmissor = centroEmissor;
	}

	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public String getCodigoSolicitante() {
		return codigoSolicitante;
	}

	public void setCodigoSolicitante(String codigoSolicitante) {
		this.codigoSolicitante = codigoSolicitante;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public NaturezaOperacao getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public CFOP getCfop() {
		return cfop;
	}

	public void setCfop(CFOP cfop) {
		this.cfop = cfop;
	}

}