package br.com.abril.nds.model.cadastro;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.HistoricoEdicao;

@Entity
@Table(name = "HISTORICO_SITUACAO_COTA")
@SequenceGenerator(name="HIST_SIT_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoSituacaoCota extends HistoricoEdicao implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1015863608070260283L;
	
	@Id
	@GeneratedValue(generator = "HIST_SIT_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_ANTERIOR")
	private SituacaoCadastro situacaoAnterior;
	@Enumerated(EnumType.STRING)
	@Column(name = "NOVA_SITUACAO", nullable = false)
	private SituacaoCadastro novaSituacao;
	@Enumerated(EnumType.STRING)
	@Column(name = "MOTIVO", nullable = true)
	private MotivoAlteracaoSituacao motivo;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO_VALIDADE")
	private Date dataInicioValidade;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM_VALIDADE")
	private Date dataFimValidade;
	@Column(name = "DESCRICAO", nullable = true)
	private String descricao;
	@Column(name = "PROCESSADO", nullable = true)
	private boolean processado;
	@Column(name = "RESTAURADO", nullable = true)
	private Boolean restaurado;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public SituacaoCadastro getSituacaoAnterior() {
		return situacaoAnterior;
	}
	
	public void setSituacaoAnterior(SituacaoCadastro situacaoAnterior) {
		this.situacaoAnterior = situacaoAnterior;
	}
	
	public SituacaoCadastro getNovaSituacao() {
		return novaSituacao;
	}
	
	public void setNovaSituacao(SituacaoCadastro novaSituacao) {
		this.novaSituacao = novaSituacao;
	}
	
	public MotivoAlteracaoSituacao getMotivo() {
		return motivo;
	}
	
	public void setMotivo(MotivoAlteracaoSituacao motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the dataInicioValidade
	 */
	public Date getDataInicioValidade() {
		return dataInicioValidade;
	}

	/**
	 * @param dataInicioValidade the dataInicioValidade to set
	 */
	public void setDataInicioValidade(Date dataInicioValidade) {
		this.dataInicioValidade = dataInicioValidade;
	}

	/**
	 * @return the dataFimValidade
	 */
	public Date getDataFimValidade() {
		return dataFimValidade;
	}

	/**
	 * @param dataFimValidade the dataFimValidade to set
	 */
	public void setDataFimValidade(Date dataFimValidade) {
		this.dataFimValidade = dataFimValidade;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isProcessado() {
		return processado;
	}

	public void setProcessado(boolean processado) {
		this.processado = processado;
	}

	public Boolean isRestaurado() {
		return restaurado;
	}

	public void setRestaurado(Boolean restaurado) {
		this.restaurado = restaurado;
	}

}
