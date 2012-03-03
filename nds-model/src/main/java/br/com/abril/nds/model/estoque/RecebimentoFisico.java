package br.com.abril.nds.model.estoque;
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

import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "RECEBIMENTO_FISICO")
@SequenceGenerator(name="REC_FISICO_SEQ", initialValue = 1, allocationSize = 1)
public class RecebimentoFisico {

	@Id
	@GeneratedValue(generator = "REC_FISICO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA_RECEBIMENTO", nullable = false)
	private Date dataRecebimento;
	@Column(name = "DATA_CONFIRMACAO")
	private Date dataConfirmacao;
	@OneToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscal notaFiscal;
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
	
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
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

}