package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import br.com.abril.nds.model.DiaSemana;

/**
 * Cadastro do Distribuidor
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "DISTRIBUIDOR")
@SequenceGenerator(name="DISTRIB_SEQ", initialValue = 1, allocationSize = 1)
public class Distribuidor {

	@Id
	@GeneratedValue(generator = "DISTRIB_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO", nullable = false)
	private Integer codigo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "PJ_ID")
	private PessoaJuridica juridica;
	
	@Column(name = "FATOR_DESCONTO")
	private BigDecimal fatorDesconto;
	
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OneToOne(optional = false)
	@JoinColumn(name = "POLITICA_COBRANCA_ID")
	private PoliticaCobranca politicaCobranca;
	
	@OneToMany
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
	
	@Embedded
	private PoliticaSuspensao politicaSuspensao;
	
	@OneToMany(mappedBy = "distribuidor")
	private List<EnderecoDistribuidor> enderecos = new ArrayList<EnderecoDistribuidor>();
	
	@OneToMany(mappedBy = "distribuidor")
	private List<TelefoneDistribuidor> telefones = new ArrayList<TelefoneDistribuidor>();
	
	/**
	 * Capacidade de distribuição diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_DISTRIBUICAO", nullable = false)
	private BigDecimal capacidadeDistribuicao;
	
	/**
	 * Capacidade de recolhimento diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_RECOLHIMENTO", nullable = false)
	private BigDecimal capacidadeRecolhimento;
	
	/**
	 * Número de reprogramações permitidas no lançamento
	 */
	@Column(name = "NUM_REPROG_LANCAMENTO", nullable = false)
	private int numeroReprogramacoesLancamento = 3;
	
	/**
	 * Dia de inicio da semana do distribuidor
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "INICIO_SEMANA", nullable = false)
	private DiaSemana inicioSemana = DiaSemana.QUARTA_FEIRA;
	
	/**
	 * Flag indicando se o distribuidor executa recolhimento de
	 * lançamento parcial
	 */
	@Column(name = "EXECUTA_RECOLHIMENTO_PARCIAL", nullable = false)
	private boolean executaRecolhimentoParcial;
	
	/**
	 * Fator em número de dias para o cálculo do relançamento parcial
	 */
	@Column(name = "FATOR_RELANCAMENTO_PARCIAL", nullable = false)
	private int fatorRelancamentoParcial;
	
	/**
	 * Parametrização do contrato entre cota e distribuidor
	 */
	@OneToOne
	@JoinColumn(name = "PARAMETRO_CONTRATO_COTA_ID")
	private ParametroContratoCota parametroContratoCota;
	
	
	@OneToMany(mappedBy="distribuidor")
	private List<TipoGarantiaAceita> tiposGarantiasAceita;
	
	@Column(name = "REQUER_AUTORIZACAO_ENCALHE_SUPERA_REPARTE", nullable = false)
	private boolean requerAutorizacaoEncalheSuperaReparte;

	
	/**
	 * Parâmetro relativo a quantidade de dias após a data de recolhimento 
	 * que um envio de encalhe pode ser aceito pelo distribuidor.
	 */
	@Column(name = "QTD_DIAS_ENCALHE_ATRASADO_ACEITAVEL", nullable = false)
	private int qtdDiasEncalheAtrasadoAceitavel = 4;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataOperacao() {
		return dataOperacao;
	}
	
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	public PessoaJuridica getJuridica() {
		return juridica;
	}
	
	public void setJuridica(PessoaJuridica juridica) {
		this.juridica = juridica;
	}
	
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}
	
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
	}
	
	public PoliticaCobranca getPoliticaCobranca() {
		return politicaCobranca;
	}
	
	public void setPoliticaCobranca(PoliticaCobranca politicaCobranca) {
		this.politicaCobranca = politicaCobranca;
	}
	
	public Set<FormaCobranca> getFormasCobranca() {
		return formasCobranca;
	}
	
	public void setFormasCobranca(Set<FormaCobranca> formasCobranca) {
		this.formasCobranca = formasCobranca;
	}
	
	public PoliticaSuspensao getPoliticaSuspensao() {
		return politicaSuspensao;
	}
	
	public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
		this.politicaSuspensao = politicaSuspensao;
	}
	
	public List<EnderecoDistribuidor> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(List<EnderecoDistribuidor> enderecos) {
		this.enderecos = enderecos;
	}
	
	public List<TelefoneDistribuidor> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<TelefoneDistribuidor> telefones) {
		this.telefones = telefones;
	}

	public BigDecimal getCapacidadeDistribuicao() {
		return capacidadeDistribuicao;
	}

	public void setCapacidadeDistribuicao(BigDecimal capacidadeDistribuicao) {
		this.capacidadeDistribuicao = capacidadeDistribuicao;
	}

	public BigDecimal getCapacidadeRecolhimento() {
		return capacidadeRecolhimento;
	}

	public void setCapacidadeRecolhimento(BigDecimal capacidadeRecolhimento) {
		this.capacidadeRecolhimento = capacidadeRecolhimento;
	}
	
	public int getNumeroReprogramacoesLancamento() {
		return numeroReprogramacoesLancamento;
	}
	
	public void setNumeroReprogramacoesLancamento(
			int numeroReprogramacoesLancamento) {
		this.numeroReprogramacoesLancamento = numeroReprogramacoesLancamento;
	}
	
	public DiaSemana getInicioSemana() {
		return inicioSemana;
	}
	
	public void setInicioSemana(DiaSemana inicioSemana) {
		this.inicioSemana = inicioSemana;
	}
	
	public boolean isExecutaRecolhimentoParcial() {
		return executaRecolhimentoParcial;
	}
	
	public void setExecutaRecolhimentoParcial(boolean executaRecolhimentoParcial) {
		this.executaRecolhimentoParcial = executaRecolhimentoParcial;
	}
	
	public int getFatorRelancamentoParcial() {
		return fatorRelancamentoParcial;
	}
	
	public void setFatorRelancamentoParcial(int fatorRelancamentoParcial) {
		this.fatorRelancamentoParcial = fatorRelancamentoParcial;
	}

	/**
	 * @return the parametroContratoCota
	 */
	public ParametroContratoCota getParametroContratoCota() {
		return parametroContratoCota;
	}

	/**
	 * @param parametroContratoCota the parametroContratoCota to set
	 */
	public void setParametroContratoCota(ParametroContratoCota parametroContratoCota) {
		this.parametroContratoCota = parametroContratoCota;
	}

	/**
	 * @return the tiposGarantiasAceita
	 */
	public List<TipoGarantiaAceita> getTiposGarantiasAceita() {
		return tiposGarantiasAceita;
	}

	/**
	 * @param tiposGarantiasAceita the tiposGarantiasAceita to set
	 */
	public void setTiposGarantiasAceita(
			List<TipoGarantiaAceita> tiposGarantiasAceita) {
		this.tiposGarantiasAceita = tiposGarantiasAceita;
	}
	

	public boolean isRequerAutorizacaoEncalheSuperaReparte() {
		return requerAutorizacaoEncalheSuperaReparte;
	}

	public void setRequerAutorizacaoEncalheSuperaReparte(
			boolean requerAutorizacaoEncalheSuperaReparte) {
		this.requerAutorizacaoEncalheSuperaReparte = requerAutorizacaoEncalheSuperaReparte;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public int getQtdDiasEncalheAtrasadoAceitavel() {
		return qtdDiasEncalheAtrasadoAceitavel;
	}

	public void setQtdDiasEncalheAtrasadoAceitavel(
			int qtdDiasEncalheAtrasadoAceitavel) {
		this.qtdDiasEncalheAtrasadoAceitavel = qtdDiasEncalheAtrasadoAceitavel;
	}
	
}