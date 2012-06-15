package br.com.abril.nds.model.fiscal;

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

import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Exportable
@Entity
@Table(name = "TIPO_NOTA_FISCAL")
@SequenceGenerator(name="TP_NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class TipoNotaFiscal {

	@Id
	@GeneratedValue(generator = "TP_NOTA_FISCAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_NOTA_FISCAL", nullable = false)
	private GrupoNotaFiscal grupoNotaFiscal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CFOP_ESTADO")
	private CFOP cfopEstado;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CFOP_OUTROS_ESTADOS")
	private CFOP cfopOutrosEstados;
	
	// Código natureza da operação
	@Column(name = "NOP_CODIGO", nullable = false)
	private Long nopCodigo;

	// Descrição natureza da operação
	@Export(label = "Tipo de Nota", exhibitionOrder = 1)
	@Column(name = "NOP_DESCRICAO", nullable = false)
	private String nopDescricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ATIVIDADE", nullable = false)
	private TipoAtividade tipoAtividade;
	
	@Export(label = "Emitente", exhibitionOrder = 2)
	@Enumerated(EnumType.STRING)
	@Column(name = "EMITENTE", nullable = false)
	private TipoUsuarioNotaFiscal emitente;
	
	@Export(label = "Destinatário", exhibitionOrder = 3)
	@Enumerated(EnumType.STRING)
	@Column(name = "DESTINATARIO", nullable = false)
	private TipoUsuarioNotaFiscal destinatario;
	
	@Column(name = "CONTRIBUINTE", nullable = false)
	private boolean contribuinte;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public GrupoNotaFiscal getGrupoNotaFiscal() {
		return grupoNotaFiscal;
	}
	
	public void setGrupoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		this.grupoNotaFiscal = grupoNotaFiscal;
		this.tipoOperacao = grupoNotaFiscal.getTipoOperacao();
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public CFOP getCfopEstado() {
		return cfopEstado;
	}

	public void setCfopEstado(CFOP cfopEstado) {
		this.cfopEstado = cfopEstado;
	}

	public CFOP getCfopOutrosEstados() {
		return cfopOutrosEstados;
	}

	public void setCfopOutrosEstados(CFOP cfopOutrosEstados) {
		this.cfopOutrosEstados = cfopOutrosEstados;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public TipoUsuarioNotaFiscal getEmitente() {
		return emitente;
	}

	public void setEmitente(TipoUsuarioNotaFiscal emitente) {
		this.emitente = emitente;
	}

	public TipoUsuarioNotaFiscal getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(TipoUsuarioNotaFiscal destinatario) {
		this.destinatario = destinatario;
	}

	public boolean isContribuinte() {
		return contribuinte;
	}

	public void setContribuinte(boolean contribuinte) {
		this.contribuinte = contribuinte;
	}

	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public Long getNopCodigo() {
		return nopCodigo;
	}

	public void setNopCodigo(Long nopCodigo) {
		this.nopCodigo = nopCodigo;
	}

	public String getNopDescricao() {
		return nopDescricao;
	}

	public void setNopDescricao(String nopDescricao) {
		this.nopDescricao = nopDescricao;
	}
	
}