package br.com.abril.nds.model.fiscal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "NOTA_FISCAL_ENTRADA")
@SequenceGenerator(name="NF_ENTRADA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class NotaFiscalEntrada extends NotaFiscal {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "NF_ENTRADA_SEQ")
	@Column(name = "ID")
	protected Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	protected Origem origem;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_NOTA_FISCAL", nullable = false)
	protected StatusNotaFiscalEntrada statusNotaFiscal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_RECEBIMENTO")
	protected StatusRecebimento statusRecebimento;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "CFOP_ID")
	protected CFOP cfop;
	
	@OneToMany(mappedBy="notaFiscal", cascade={CascadeType.ALL})
	protected List<ItemNotaFiscalEntrada> itens = new ArrayList<ItemNotaFiscalEntrada>();	
	
	@Column(name = "DATA_RECEBIMENTO")
	protected Date dataRecebimento;

	@Column(name="NUMERO_NOTA_ENVIO")
	protected Long numeroNotaEnvio;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Origem getOrigem() {
		return origem;
	}
	
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public StatusNotaFiscalEntrada getStatusNotaFiscal() {
		return statusNotaFiscal;
	}
	
	public void setStatusNotaFiscal(StatusNotaFiscalEntrada statusNotaFiscal) {
		this.statusNotaFiscal = statusNotaFiscal;
	}

	public CFOP getCfop() {
		return cfop;
	}

	public void setCfop(CFOP cfop) {
		this.cfop = cfop;
	}

	/**
	 * @return the itens
	 */
	public List<ItemNotaFiscalEntrada> getItens() {
		return itens;
	}

	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<ItemNotaFiscalEntrada> itens) {
		this.itens = itens;
	}
	
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public StatusRecebimento getStatusRecebimento() {
		return statusRecebimento;
	}

	public void setStatusRecebimento(StatusRecebimento statusRecebimento) {
		this.statusRecebimento = statusRecebimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getNumeroNotaEnvio() == null) ? 0 : this.getNumeroNotaEnvio().hashCode());
		result = prime * result + ((this.getOrigem() == null) ? 0 : this.getOrigem().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotaFiscalEntrada other = (NotaFiscalEntrada) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getNumeroNotaEnvio() == null) {
			if (other.getNumeroNotaEnvio() != null)
				return false;
		} else if (!this.getNumeroNotaEnvio().equals(other.getNumeroNotaEnvio()))
			return false;
		if (this.getOrigem() != other.getOrigem())
			return false;
		return true;
	}
	
	
	
}
