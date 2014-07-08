package br.com.abril.nds.model.estoque;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "RECEBIMENTO_FISICO")
@SequenceGenerator(name="REC_FISICO_SEQ", initialValue = 1, allocationSize = 1)
public class RecebimentoFisico implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1901557922790741971L;
	
	@Id
	@GeneratedValue(generator = "REC_FISICO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_RECEBIMENTO", nullable = false)
	private Date dataRecebimento;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name = "DATA_CONFIRMACAO")
	private Date dataConfirmacao;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscalEntrada notaFiscal;
	
	@ManyToOne(optional = false)
	private Usuario recebedor;
	
	@ManyToOne
	private Usuario conferente;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_CONFIRMACAO", nullable = false)
	private StatusConfirmacao statusConfirmacao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	public Date getDataConfirmacao() {
		return dataConfirmacao;
	}
	
	public void setDataConfirmacao(Date dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}
	
	public NotaFiscalEntrada getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscalEntrada notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	public Usuario getRecebedor() {
		return recebedor;
	}
	
	public void setRecebedor(Usuario recebedor) {
		this.recebedor = recebedor;
	}
	
	public Usuario getConferente() {
		return conferente;
	}

	public void setConferente(Usuario conferente) {
		this.conferente = conferente;
	}
	
	public StatusConfirmacao getStatusConfirmacao() {
		return statusConfirmacao;
	}
	
	public void setStatusConfirmacao(StatusConfirmacao statusConfirmacao) {
		this.statusConfirmacao = statusConfirmacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getNotaFiscal() == null) ? 0 : this.getNotaFiscal().hashCode());
		result = prime * result + ((this.getStatusConfirmacao() == null) ? 0 : this.getStatusConfirmacao().hashCode());
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
		RecebimentoFisico other = (RecebimentoFisico) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getNotaFiscal() == null) {
			if (other.getNotaFiscal() != null)
				return false;
		} else if (!this.getNotaFiscal().equals(other.getNotaFiscal()))
			return false;
		if (this.getStatusConfirmacao() != other.getStatusConfirmacao())
			return false;
		return true;
	}

	
	
}