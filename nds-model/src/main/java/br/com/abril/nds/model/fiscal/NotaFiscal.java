package br.com.abril.nds.model.fiscal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "NOTA_FISCAL")
@SequenceGenerator(name="NF_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class NotaFiscal {

	@Id
	@GeneratedValue(generator = "NF_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA_EMISSAO", nullable = false)
	private Date dataEmissao;
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	private Date dataExpedicao;
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	@Column(name = "SERIE", nullable  = false)
	private String serie;
	@Column(name = "CHAVE_ACESSO")
	private String chaveAcesso;
	@Column(name = "STATUS_NOTA_FISCAL", nullable = false)
	private StatusNotaFiscal statusNotaFiscal;
	@ManyToOne
	private Usuario usuario;
	@ManyToOne(optional = false)
	private CFOP cfop;
	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	private Origem origem;
	@ManyToOne(optional = false)
	private PessoaJuridica emitente;
	@ManyToOne(optional = false)
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

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public PessoaJuridica getEmitente() {
		return emitente;
	}
	
	public void setEmitente(PessoaJuridica emitente) {
		this.emitente = emitente;
	}
	
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}


}