package br.com.abril.nds.model.distribuicao;
import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.TipoAjusteReparte;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "AJUSTE_REPARTE")
@SequenceGenerator(name="AJUSTE_REPARTE_SEQ", initialValue = 1, allocationSize = 1)
public class AjusteReparte implements Serializable {

	private static final long serialVersionUID = -8904481295932974198L;
	@Id
	@GeneratedValue(generator = "AJUSTE_REPARTE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMA_AJUSTE", nullable = true)
	private TipoAjusteReparte formaAjuste;
	
	@Column(name = "AJUSTE_APLICADO", precision=18, scale=4)
	private BigDecimal ajusteAplicado;
	
	@Column (name = "DATA_INICIO")
	private Date dataInicio;
	
	@Column (name = "DATA_FIM")
	private Date dataFim;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MOTIVO", nullable = true)
	private MotivoAlteracaoSituacao motivo;
	
	@ManyToOne (optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Column (name = "DATA_ALTERACAO")
	private Date dataAlteracao;
	
	@ManyToOne
	@JoinColumn (name = "TIPO_SEGMENTO_AJUSTE_ID")
	private TipoSegmentoProduto tipoSegmentoAjuste;
	
	public AjusteReparte() {
		this.dataAlteracao = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public TipoAjusteReparte getFormaAjuste() {
		return formaAjuste;
	}

	public void setFormaAjuste(TipoAjusteReparte formaAjuste) {
		this.formaAjuste = formaAjuste;
	}

	public BigDecimal getAjusteAplicado() {
		return ajusteAplicado;
	}

	public void setAjusteAplicado(BigDecimal ajusteAplicado) {
		this.ajusteAplicado = ajusteAplicado;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public MotivoAlteracaoSituacao getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoAlteracaoSituacao motivo) {
		this.motivo = motivo;
	}

	public TipoSegmentoProduto getTipoSegmentoAjuste() {
		return tipoSegmentoAjuste;
	}

	public void setTipoSegmentoAjuste(TipoSegmentoProduto tipoSegmentoAjuste) {
		this.tipoSegmentoAjuste = tipoSegmentoAjuste;
	}

}
