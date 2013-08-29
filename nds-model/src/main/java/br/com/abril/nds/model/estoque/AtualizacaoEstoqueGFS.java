package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SuppressWarnings("serial")
@Table(name = "ATUALIZACAO_ESTOQUE_GFS")
@SequenceGenerator(name="ATUALIZACAO_ESTOQUE_GFS_SEQ", initialValue = 1, allocationSize = 1)
public class AtualizacaoEstoqueGFS implements Serializable {

	@Id
	@GeneratedValue(generator = "ATUALIZACAO_ESTOQUE_GFS_SEQ")
	private Long id;
	
	@Column(name = "DATA_ATUALIZACAO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataAtualizacao;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoque movimentoEstoque;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "DIFERENCA_ID")
	private Diferenca diferenca;
	
	public AtualizacaoEstoqueGFS() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	public Diferenca getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}
	
}
