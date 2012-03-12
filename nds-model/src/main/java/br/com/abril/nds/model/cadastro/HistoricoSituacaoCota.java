package br.com.abril.nds.model.cadastro;

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

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_SITUCAO_COTA")
@SequenceGenerator(name="HIST_SIT_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoSituacaoCota {
	
	@Id
	@GeneratedValue(generator = "HIST_SIT_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA", nullable = false)
	private Date data;
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID", nullable = false)
	private Usuario responsavel;
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
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
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
