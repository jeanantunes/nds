package br.com.abril.nds.model.fiscal;

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
	
	@Id
	@GeneratedValue(generator = "NF_ENTRADA_SEQ")
	@Column(name = "ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	private Origem origem;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_NOTA_FISCAL", nullable = false)
	private StatusNotaFiscalEntrada statusNotaFiscal;
	
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

}
