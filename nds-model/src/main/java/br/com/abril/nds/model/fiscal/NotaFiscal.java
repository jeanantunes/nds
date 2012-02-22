package br.com.abril.nds.model.fiscal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "NOTA_FISCAL")
public abstract class NotaFiscal {

	@Id
	private Long id;
	private Date dataEmissao;
	private Date dataExpedicao;
	private String numero;
	private String serie;
	private String chaveAcesso;
	private StatusNotaFiscal statusNotaFiscal;
	@ManyToOne
	private Usuario usuario;
	@ManyToOne
	private CFOP cfop;
	@Enumerated(EnumType.STRING)
	private OrigemNota origemNota;
	@ManyToOne
	private PessoaJuridica juridica;
	@ManyToOne
	private TipoNotaFiscal tipoNotaFiscal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataEmissao() {
		return dataEmissao;
	}
	
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	public Date getDataExpedicao() {
		return dataExpedicao;
	}
	
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getSerie() {
		return serie;
	}
	
	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	public StatusNotaFiscal getStatusNotaFiscal() {
		return statusNotaFiscal;
	}
	
	public void setStatusNotaFiscal(StatusNotaFiscal statusNotaFiscal) {
		this.statusNotaFiscal = statusNotaFiscal;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public CFOP getCfop() {
		return cfop;
	}

	public void setCfop(CFOP cfop) {
		this.cfop = cfop;
	}
   
	public OrigemNota getOrigemNota() {
		return origemNota;
	}

	public void setOrigemNota(OrigemNota origemNota) {
		this.origemNota = origemNota;
	}

	public PessoaJuridica getJuridica() {
		return juridica;
	}

	public void setJuridica(PessoaJuridica juridica) {
		this.juridica = juridica;
	}

	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}


}