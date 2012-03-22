package br.com.abril.nds.model.cadastro;

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

import br.com.abril.nds.model.HistoricoEdicao;

@Entity
@Table(name = "HISTORICO_SITUCAO_COTA")
@SequenceGenerator(name="HIST_SIT_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoSituacaoCota extends HistoricoEdicao {
	
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
	@Column(name = "MOTIVO", nullable = false)
	private MotivoAlteracaoSituacao motivo;
	
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

}
