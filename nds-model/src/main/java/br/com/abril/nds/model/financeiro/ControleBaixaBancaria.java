package br.com.abril.nds.model.financeiro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "CONTROLE_BAIXA_BANCARIA",
	   uniqueConstraints = {@UniqueConstraint(columnNames = {"DATA_PAGAMENTO", "BANCO_ID"})})
@SequenceGenerator(name = "CTRL_BAIXA_BANCARIA_SEQ", initialValue = 1, allocationSize = 1)
public class ControleBaixaBancaria {
	
	@Id
	@GeneratedValue(generator = "CTRL_BAIXA_BANCARIA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PAGAMENTO", nullable = false)
	private Date dataPagamento;
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusControle status;
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID", nullable = false)
	private Banco banco;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public StatusControle getStatus() {
		return status;
	}
	
	public void setStatus(StatusControle status) {
		this.status = status;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the banco
	 */
	public Banco getBanco() {
		return banco;
	}

	/**
	 * @param banco the banco to set
	 */
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
}
