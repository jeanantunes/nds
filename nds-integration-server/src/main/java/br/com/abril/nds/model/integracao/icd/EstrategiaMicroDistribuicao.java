package br.com.abril.nds.model.integracao.icd;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "ESTRATEGIA_MICRO_DISTBCAO")
public class EstrategiaMicroDistribuicao {
	
	
	@Id
	@Column(name = "COD_ESTRATEGIA", nullable = false)
	private Long codEstrategia;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Transient
	private Long codigoDistribuidor;
	
	@Column(name = "DSC_ESTRATEGIA")
	private String dscEstrategia;
	
	@Column(name = "TXT_TESTES_MERCADO")
	private String txtTestesMercado;
	
	@Column(name = "TXT_PLANO_MIDIA")
	private String txtPlanoMidia;
	
	@Column(name = "TXT_AGREGADOS")
	private String txtAgregados;
	
	@Column(name = "TXT_OPORTUNIDADE_VENDA")
	private String txtOportunidadeVenda;
	
	@Column(name = "TXT_TRATAMENTO_REGIONAL")
	private String txtTratamentoRegional;
	
	@Column(name = "PCT_ABRANGENCIA_DISTBCAO")
	private Long pctAbrangenciaDistbcao;
	
	@Column(name = "COD_SENTIDO_ABRANGENCIA")
	private Long codSentidoAbrangencia;
	
	@Column(name = "QTD_REPARTE_MINIMO")
	private Long qtdReparteMinimo;
	
	@Column(name = "COD_LANCTO_EDICAO")
	private Long codLanctoEdicao;
	
	@Column(name = "IND_ESTRATEGIA_PENDENTE")
	private String indEstrategiaPendente;
	
	@Column(name = "IND_EXISTENCIA_TAB_PCO")
	private String indExistenciaTabPco;
	
	@Column(name = "COD_USU_INC")
	private String codUsuInc;
	
	@Column(name = "DAT_INC")
	private Date datInc;
	
	@Column(name = "COD_USU_ALT")
	private String codUsuAlt;
	
	@Column(name = "DAT_ALT")
	private Date datAlt;
	
	//@Column(name = "NUM_ESTRATEGIA_LACTO")
	//private Long numEstrategiaLancto;
	
	@Column(name = "COD_ESTRATEGIA_ADABAS")
	private Long codEstrategiaAdabas;
	
	@Column(name = "TXT_OPORTUNIDADE_REGIONAL")
	private String txtOportunidadeRegional;
	
	@Column(name = "TXT_OBSERVACAO")
	private String txtObservacao;
	
	@Column(name = "COD_BASE_CALCULO")
	private Long codBaseCalculo;
	
	@Column(name = "IND_ESTRATEGIA_CLUSTER")
	private String indEstrategiaCluster;
	
	@Column(name = "IND_TODOS_CLUSTER")
	private String indTodosCluster;

	@ManyToMany
	@JoinTable(name = "ESTRATEGIA_LANCTO_PRACA", 
			joinColumns = {@JoinColumn(name = "COD_ESTRATEGIA")}, 
			inverseJoinColumns = {@JoinColumn(name = "COD_PRACA")})
	private List<Praca> pracas;
	
	@ManyToMany
	@JoinTable(name = "ESTRATEGIA_LANCTO_PRACA", 
			joinColumns = {@JoinColumn(name = "COD_ESTRATEGIA")}, 
			inverseJoinColumns = {@JoinColumn(name = "COD_LANCTO_EDICAO")})
	private List<IcdLancamentoEdicaoPublicacao> edicoes;
	
	@ManyToMany
	@JoinTable(name = "ESTRATEGIA_LANCTO_PRACA", 
			joinColumns = {@JoinColumn(name = "COD_ESTRATEGIA")}, 
			inverseJoinColumns = {@JoinColumn(name = "COD_BASE_CALCULO")})
	private List<BaseCalculo> baseCalculo;

	/**
	 * Getters e Setters 
	 */
	
	public Long getCodEstrategia() {
		return codEstrategia;
	}

	public void setCodEstrategia(Long codEstrategia) {
		this.codEstrategia = codEstrategia;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getBaseDeDados() {
		return baseDeDados;
	}

	public void setBaseDeDados(String baseDeDados) {
		this.baseDeDados = baseDeDados;
	}

	public String getUsuarioBaseDeDados() {
		return usuarioBaseDeDados;
	}

	public void setUsuarioBaseDeDados(String usuarioBaseDeDados) {
		this.usuarioBaseDeDados = usuarioBaseDeDados;
	}

	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public String getDscEstrategia() {
		return dscEstrategia;
	}

	public void setDscEstrategia(String dscEstrategia) {
		this.dscEstrategia = dscEstrategia;
	}

	public String getTxtTestesMercado() {
		return txtTestesMercado;
	}

	public void setTxtTestesMercado(String txtTestesMercado) {
		this.txtTestesMercado = txtTestesMercado;
	}

	public String getTxtPlanoMidia() {
		return txtPlanoMidia;
	}

	public void setTxtPlanoMidia(String txtPlanoMidia) {
		this.txtPlanoMidia = txtPlanoMidia;
	}

	public String getTxtAgregados() {
		return txtAgregados;
	}

	public void setTxtAgregados(String txtAgregados) {
		this.txtAgregados = txtAgregados;
	}

	public String getTxtOportunidadeVenda() {
		return txtOportunidadeVenda;
	}

	public void setTxtOportunidadeVenda(String txtOportunidadeVenda) {
		this.txtOportunidadeVenda = txtOportunidadeVenda;
	}

	public String getTxtTratamentoRegional() {
		return txtTratamentoRegional;
	}

	public void setTxtTratamentoRegional(String txtTratamentoRegional) {
		this.txtTratamentoRegional = txtTratamentoRegional;
	}

	public Long getPctAbrangenciaDistbcao() {
		return pctAbrangenciaDistbcao;
	}

	public void setPctAbrangenciaDistbcao(Long pctAbrangenciaDistbcao) {
		this.pctAbrangenciaDistbcao = pctAbrangenciaDistbcao;
	}

	public Long getCodSentidoAbrangencia() {
		return codSentidoAbrangencia;
	}

	public void setCodSentidoAbrangencia(Long codSentidoAbrangencia) {
		this.codSentidoAbrangencia = codSentidoAbrangencia;
	}

	public Long getQtdReparteMinimo() {
		return qtdReparteMinimo;
	}

	public void setQtdReparteMinimo(Long qtdReparteMinimo) {
		this.qtdReparteMinimo = qtdReparteMinimo;
	}

	public Long getCodLanctoEdicao() {
		return codLanctoEdicao;
	}

	public void setCodLanctoEdicao(Long codLanctoEdicao) {
		this.codLanctoEdicao = codLanctoEdicao;
	}

	public String getIndEstrategiaPendente() {
		return indEstrategiaPendente;
	}

	public void setIndEstrategiaPendente(String indEstrategiaPendente) {
		this.indEstrategiaPendente = indEstrategiaPendente;
	}

	public String getIndExistenciaTabPco() {
		return indExistenciaTabPco;
	}

	public void setIndExistenciaTabPco(String indExistenciaTabPco) {
		this.indExistenciaTabPco = indExistenciaTabPco;
	}

	public String getCodUsuInc() {
		return codUsuInc;
	}

	public void setCodUsuInc(String codUsuInc) {
		this.codUsuInc = codUsuInc;
	}

	public Date getDatInc() {
		return datInc;
	}

	public void setDatInc(Date datInc) {
		this.datInc = datInc;
	}

	public String getCodUsuAlt() {
		return codUsuAlt;
	}

	public void setCodUsuAlt(String codUsuAlt) {
		this.codUsuAlt = codUsuAlt;
	}

	public Date getDatAlt() {
		return datAlt;
	}

	public void setDatAlt(Date datAlt) {
		this.datAlt = datAlt;
	}
/*
	public Long getNumEstrategiaLancto() {
		return numEstrategiaLancto;
	}

	public void setNumEstrategiaLancto(Long numEstrategiaLancto) {
		this.numEstrategiaLancto = numEstrategiaLancto;
	}
*/
	public Long getCodEstrategiaAdabas() {
		return codEstrategiaAdabas;
	}

	public void setCodEstrategiaAdabas(Long codEstrategiaAdabas) {
		this.codEstrategiaAdabas = codEstrategiaAdabas;
	}

	public String getTxtOportunidadeRegional() {
		return txtOportunidadeRegional;
	}

	public void setTxtOportunidadeRegional(String txtOportunidadeRegional) {
		this.txtOportunidadeRegional = txtOportunidadeRegional;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public Long getCodBaseCalculo() {
		return codBaseCalculo;
	}

	public void setCodBaseCalculo(Long codBaseCalculo) {
		this.codBaseCalculo = codBaseCalculo;
	}

	public String getIndEstrategiaCluster() {
		return indEstrategiaCluster;
	}

	public void setIndEstrategiaCluster(String indEstrategiaCluster) {
		this.indEstrategiaCluster = indEstrategiaCluster;
	}

	public String getIndTodosCluster() {
		return indTodosCluster;
	}

	public void setIndTodosCluster(String indTodosCluster) {
		this.indTodosCluster = indTodosCluster;
	}

	public List<Praca> getPracas() {
		return pracas;
	}

	public void setPracas(List<Praca> pracas) {
		this.pracas = pracas;
	}

	public List<IcdLancamentoEdicaoPublicacao> getEdicoes() {
		return edicoes;
	}

	public void setEdicoes(List<IcdLancamentoEdicaoPublicacao> edicoes) {
		this.edicoes = edicoes;
	}

	public List<BaseCalculo> getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(List<BaseCalculo> baseCalculo) {
		this.baseCalculo = baseCalculo;
	}	
	
}