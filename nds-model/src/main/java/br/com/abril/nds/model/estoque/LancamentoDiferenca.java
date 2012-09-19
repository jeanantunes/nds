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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;

@Entity
@Table(name = "LANCAMENTO_DIFERENCA")
@SequenceGenerator(name="LANCAMENTO_DIFERENCA_SEQ", initialValue = 1, allocationSize = 1)
public class LancamentoDiferenca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6282178267441122898L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "LANCAMENTO_DIFERENCA_SEQ")
	private Long id;

	@OneToOne(mappedBy = "lancamentoDiferenca")
	private Diferenca diferenca;
	
	@Column(name = "DATA_PROCESSAMENTO")
	private Date dataProcessamento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusAprovacao status;

	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoque movimentoEstoque;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Diferenca getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public StatusAprovacao getStatus() {
		return status;
	}

	public void setStatus(StatusAprovacao status) {
		this.status = status;
	}

	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}
}
