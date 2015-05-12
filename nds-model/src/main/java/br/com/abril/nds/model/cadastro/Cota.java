package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.distribuicao.RankingSegmento;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;

@Entity
@Table(name = "COTA")
@SequenceGenerator(name="COTA_SEQ", initialValue = 1, allocationSize = 1)
public class Cota implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4962347006238498224L;

	@Id
	@GeneratedValue(generator = "COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO_COTA", nullable = false)
	private Integer numeroCota;
	
	@Fetch(FetchMode.JOIN)
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Column(name = "SUGERE_SUSPENSAO", nullable = false)
	private boolean sugereSuspensao = true;
	
	@Column(name = "UTILIZA_IPV", nullable = false)
	private boolean utilizaIPV;
	
	@Embedded
	private PoliticaSuspensao politicaSuspensao;
	
	@Column(name = "SUGERE_SUSPENSAO_DISTRIBUIDOR", nullable = false)
	private boolean sugereSuspensaoDistribuidor = true;
	
	@Column(name = "POSSUI_CONTRATO", nullable = false)
	private boolean possuiContrato;
	
	@OneToMany(mappedBy = "cota", cascade={CascadeType.REMOVE})
	private List<PDV> pdvs = new ArrayList<PDV>();

	@OneToMany(mappedBy = "cota")
	private List<ChamadaEncalheCota> chamadaEncalheCotas = new ArrayList<ChamadaEncalheCota>();

	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;
		
	@OneToMany(mappedBy = "cota")
	private Set<EnderecoCota> enderecos = new HashSet<EnderecoCota>();
	
	@OneToMany(mappedBy = "cota")
	private Set<TelefoneCota> telefones = new HashSet<TelefoneCota>();
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="CONTRATO_ID")
	private ContratoCota contratoCota;
	
	@OneToOne
	@JoinColumn(name = "BOX_ID")
	private Box box;
	
	@Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
	@OneToMany(mappedBy = "cota")
	private List<HistoricoSituacaoCota> historicos = new ArrayList<HistoricoSituacaoCota>();
	
	@OneToMany(mappedBy = "cota")
	private Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "PARAMETRO_COBRANCA_ID")
	private ParametroCobrancaCota parametroCobranca;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COTA", columnDefinition = "VARCHAR(255)")
	private TipoCota tipoCota;
	
	@Column(name = "DEVOLVE_ENCALHE")
	private Boolean devolveEncalhe;
		
	@Embedded
	private ParametroDistribuicaoCota parametroDistribuicao;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_ID")
	private ParametroCobrancaDistribuicaoCota parametroCobrancaDistribuicaoCota;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = true)
	@JoinColumn(name="ID_FIADOR")
	private Fiador fiador;
	
	@OneToMany(mappedBy = "cota")
	private Set<EstoqueProdutoCota> estoqueProdutoCotas = new HashSet<EstoqueProdutoCota>();

	@OneToMany(mappedBy = "cota", fetch=FetchType.LAZY)
	private Set<MovimentoEstoqueCota> movimentoEstoqueCotas = new HashSet<MovimentoEstoqueCota>();
	
	@Column(name = "TIPO_DISTRIBUICAO_COTA", columnDefinition = "VARCHAR(255)")
	@Enumerated(EnumType.STRING)
	private TipoDistribuicaoCota tipoDistribuicaoCota;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="cota")
	private List<RankingSegmento> rankingSegmento;
	
	@OneToMany(mappedBy="cota", fetch=FetchType.LAZY)
	private List<EstoqueProdutoCota> estoqueProdutoCota;
	
	/**
	 * Data de início de atividade da cota
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO_ATIVIDADE", nullable = false)
	private Date inicioAtividade;
	
	/**
	 * Data de início do titular da cota
	 */
	@Temporal(TemporalType.DATE)
    @Column(name = "INICIO_TITULARIDADE", nullable = false)
	private Date inicioTitularidade;
	
	/**
	 * Fornecedores associados à Cota
	 */
	@ManyToMany
	@JoinTable(name = "COTA_FORNECEDOR", joinColumns = {@JoinColumn(name = "COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CLASSIFICACAO_ESPECTATIVA_FATURAMENTO")
	private ClassificacaoEspectativaFaturamento classificacaoEspectativaFaturamento;
	
	@Embedded
	private ParametrosCotaNotaFiscalEletronica parametrosCotaNotaFiscalEletronica;
	
	@OneToOne(cascade={CascadeType.REMOVE})
	@JoinColumn(name="BASE_REFERENCIA_COTA_ID")
	private BaseReferenciaCota baseReferenciaCota;
	
	@OneToMany(mappedBy = "cota",cascade={CascadeType.REMOVE})
	private Set<SocioCota> sociosCota = new HashSet<SocioCota>();
	
	@OneToMany(mappedBy="cota", cascade={CascadeType.REMOVE})
	private Set<HistoricoNumeroCota> historicoNumeroCota;
	
	@OneToMany(mappedBy="cota", cascade={CascadeType.REMOVE})
	private Set<DescontoProdutoEdicao> descontosProdutoEdicao = new HashSet<DescontoProdutoEdicao>();

	@ManyToMany(mappedBy="cotas", targetEntity=GrupoCota.class)
	private Set<GrupoCota> grupos;
	
	/**
	 * Histórico de titulares da cota
	 */
	@OneToMany(mappedBy = "cota", cascade = {CascadeType.ALL})
	private Set<HistoricoTitularidadeCota> titularesCota =new HashSet<HistoricoTitularidadeCota>();
	
	/** 
	 * Referente a garantias da cota.
	 */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="COTA_GARANTIA_ID")
	private CotaGarantia cotaGarantia;
	
	@Column(name = "VALOR_MINIMO_COBRANCA", precision=18, scale=4)
	private BigDecimal valorMinimoCobranca;
	
	@ManyToMany(mappedBy = "cotas", fetch = FetchType.LAZY)
	private Set<CotaUnificacao> cotasUnificacao;
	
	@Column(name = "RECEBE_RECOLHE_PARCIAIS", insertable=false,  updatable=false)
	private Integer recebeRecolheParciais;
	///insertable = false, updatable = false
	
	public Cota() {
        this.inicioAtividade = new Date();
        this.inicioTitularidade = new Date();
    }
	
	
	public Cota(Long id) {
		this.id = id;
	}

	public Set<HistoricoNumeroCota> getHistoricoNumeroCota() {
		return historicoNumeroCota;
	}

	public void setHistoricoNumeroCota(Set<HistoricoNumeroCota> historicoNumeroCota) {
		this.historicoNumeroCota = historicoNumeroCota;
	}
	
	/**
	 * @return the descontosProdutoEdicao
	 */
	public Set<DescontoProdutoEdicao> getDescontosProdutoEdicao() {
		return descontosProdutoEdicao;
	}

	/**
	 * @param descontosProdutoEdicao the descontosProdutoEdicao to set
	 */
	public void setDescontosProdutoEdicao(
			Set<DescontoProdutoEdicao> descontosProdutoEdicao) {
		this.descontosProdutoEdicao = descontosProdutoEdicao;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}
	
	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
	}
	
	public boolean isUtilizaIPV() {
		return utilizaIPV;
	}


	public void setUtilizaIPV(boolean utilizaIPV) {
		this.utilizaIPV = utilizaIPV;
	}


	public PoliticaSuspensao getPoliticaSuspensao() {
		return politicaSuspensao;
	}

	public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
		this.politicaSuspensao = politicaSuspensao;
	}

	public boolean isSugereSuspensaoDistribuidor() {
		return sugereSuspensaoDistribuidor;
	}

	public void setSugereSuspensaoDistribuidor(boolean sugereSuspensaoDistribuidor) {
		this.sugereSuspensaoDistribuidor = sugereSuspensaoDistribuidor;
	}

	public boolean isPossuiContrato() {
		return possuiContrato;
	}

	public void setPossuiContrato(boolean possuiContrato) {
		this.possuiContrato = possuiContrato;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<PDV> getPdvs() {
		return pdvs;
	}
	
	public void setPdvs(List<PDV> pdvs) {
		this.pdvs = pdvs;
	}
	
	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}
	
	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
		
	public Set<EnderecoCota> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(Set<EnderecoCota> enderecos) {
		this.enderecos = enderecos;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}
	
	public ContratoCota getContratoCota() {
		return contratoCota;
	}
	
	public void setContratoCota(ContratoCota contratoCota) {
		this.contratoCota = contratoCota;
	}
	
	public List<HistoricoSituacaoCota> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<HistoricoSituacaoCota> historicos) {
		this.historicos = historicos;
	}

	public Set<EstudoCota> getEstudoCotas() {
		return estudoCotas;
	}

	public void setEstudoCotas(Set<EstudoCota> estudoCotas) {
		this.estudoCotas = estudoCotas;
	}
	
	public ParametroCobrancaCota getParametroCobranca() {
		return parametroCobranca;
	}
	
	public void setParametroCobranca(ParametroCobrancaCota parametroCobranca) {
		this.parametroCobranca = parametroCobranca;
	}

	public TipoCota getTipoCota() {
		return tipoCota;
	}

	public void setTipoCota(TipoCota tipoCota) {
		this.tipoCota = tipoCota;
	}
	
	public Boolean isDevolveEncalhe() {
		return devolveEncalhe;
	}

	public void setDevolveEncalhe(Boolean devolveEncalhe) {
		this.devolveEncalhe = devolveEncalhe;
	}

	public Fiador getFiador() {
		return fiador;
	}

	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}
	
	public Set<EstoqueProdutoCota> getEstoqueProdutoCotas() {
		return estoqueProdutoCotas;
	}

	public void setEstoqueProdutoCotas(Set<EstoqueProdutoCota> estoqueProdutoCotas) {
		this.estoqueProdutoCotas = estoqueProdutoCotas;
	}
	
	public Date getInicioAtividade() {
		return inicioAtividade;
	}
	
	public void setInicioAtividade(Date inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}
	
    public Date getInicioTitularidade() {
        return inicioTitularidade;
    }

    public void setInicioTitularidade(Date inicioTitularidade) {
        this.inicioTitularidade = inicioTitularidade;
    }
    
	public List<ChamadaEncalheCota> getChamadaEncalheCotas() {
		return chamadaEncalheCotas;
	}

	public void setChamadaEncalheCotas(List<ChamadaEncalheCota> chamadaEncalheCotas) {
		this.chamadaEncalheCotas = chamadaEncalheCotas;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Set<TelefoneCota> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<TelefoneCota> telefones) {
		this.telefones = telefones;
	}
	
	/**
	 * @return the parametrosCotaNotaFiscalEletronica
	 */
	public ParametrosCotaNotaFiscalEletronica getParametrosCotaNotaFiscalEletronica() {
		return parametrosCotaNotaFiscalEletronica;
	}

	/**
	 * @param parametrosCotaNotaFiscalEletronica the parametrosCotaNotaFiscalEletronica to set
	 */
	public void setParametrosCotaNotaFiscalEletronica(
			ParametrosCotaNotaFiscalEletronica parametrosCotaNotaFiscalEletronica) {
		this.parametrosCotaNotaFiscalEletronica = parametrosCotaNotaFiscalEletronica;
	}

	/**
	 * @return the classificacaoEspectativaFaturamento
	 */
	public ClassificacaoEspectativaFaturamento getClassificacaoEspectativaFaturamento() {
		return classificacaoEspectativaFaturamento;
	}

	/**
	 * @param classificacaoEspectativaFaturamento the classificacaoEspectativaFaturamento to set
	 */
	public void setClassificacaoEspectativaFaturamento(
			ClassificacaoEspectativaFaturamento classificacaoEspectativaFaturamento) {
		this.classificacaoEspectativaFaturamento = classificacaoEspectativaFaturamento;
	}
	
	/**
	 * @return the baseReferenciaCota
	 */
	public BaseReferenciaCota getBaseReferenciaCota() {
		return baseReferenciaCota;
	}

	/**
	 * @param baseReferenciaCota the baseReferenciaCota to set
	 */
	public void setBaseReferenciaCota(BaseReferenciaCota baseReferenciaCota) {
		this.baseReferenciaCota = baseReferenciaCota;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cota other = (Cota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the parametroDistribuicao
	 */
	public ParametroDistribuicaoCota getParametroDistribuicao() {
		return parametroDistribuicao;
	}

	/**
	 * @param parametroDistribuicao the parametroDistribuicao to set
	 */
	public void setParametroDistribuicao(ParametroDistribuicaoCota parametroDistribuicao) {
		this.parametroDistribuicao = parametroDistribuicao;
	}

	/**
	 * @return the sociosCota
	 */
	public Set<SocioCota> getSociosCota() {
		return sociosCota;
	}

	/**
	 * @param sociosCota the sociosCota to set
	 */
	public void setSociosCota(Set<SocioCota> sociosCota) {
		this.sociosCota = sociosCota;
	}

	public Set<MovimentoEstoqueCota> getMovimentoEstoqueCotas() {
		return movimentoEstoqueCotas;
	}

	public void setMovimentoEstoqueCotas(Set<MovimentoEstoqueCota> movimentoEstoqueCotas) {
		this.movimentoEstoqueCotas = movimentoEstoqueCotas;
	}

	/**
	 * @return the grupos
	 */
	public Set<GrupoCota> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void setGrupos(Set<GrupoCota> grupos) {
		this.grupos = grupos;
	}

    /**
     * @return the titularesCota
     */
    public Set<HistoricoTitularidadeCota> getTitularesCota() {
        return titularesCota;
    }

    /**
     * @param titularesCota the titularesCota to set
     */
    public void setTitularesCota(Set<HistoricoTitularidadeCota> titularesCota) {
        this.titularesCota = titularesCota;
    }
    
	/**
     * Adiciona um novo histórico de titularidade da cota
     * 
     * @param titularCota
     *            histórico de titularidade da cota para adição
     * @throws IllegalArgumentException
     *             caso o histórico de titularidade da cota seja nulo
     */
    public void addTitularCota(HistoricoTitularidadeCota titularCota) {
        Validate.notNull(titularCota, "Titular da Cota não deve ser nulo!");
        if (titularesCota == null) {
            titularesCota = new HashSet<HistoricoTitularidadeCota>();
        }
        titularesCota.add(titularCota);
    }
    
    /**
     * Verifica se o titular da cota é uma pessoa física
     * 
     * @return true se o titular é pessoa física, false caso contrário
     */
    public boolean isTitularPessoaFisica() {
        return pessoa instanceof PessoaFisica;
    }

	/**
	 * @return the cotaGarantia
	 */
	public CotaGarantia getCotaGarantia() {
		return cotaGarantia;
	}

	/**
	 * @param cotaGarantia the cotaGarantia to set
	 */
	public void setCotaGarantia(CotaGarantia cotaGarantia) {
		this.cotaGarantia = cotaGarantia;
	}
	
	public EnderecoCota getEnderecoPrincipal(){
		for(EnderecoCota item:this.getEnderecos()){
			if(item.isPrincipal()){
				return item;
			}
		}
		return null;
	}
	
	public PDV getPDVPrincipal() {
		for(PDV item : this.getPdvs()) {
			if(item.getCaracteristicas().isPontoPrincipal()) {
				return item;
			}
		}
		return null;
	}
	
	public TelefoneCota getTefefonePrincipal() {
		
		for(TelefoneCota telefoneCota : this.getTelefones()) {
			if(telefoneCota.isPrincipal()){
				return telefoneCota;
			}
		}
		
		return null;
	}
	
	
	public List<PDV> getPDVSecundarios(){
		
		List<PDV> pdvs = new ArrayList<>();
		
		for(PDV item : this.getPdvs()){
			
			if(!item.getCaracteristicas().isPontoPrincipal()){
				
				pdvs.add(item);
			}
		}
		return pdvs;
	}
	
	public EnderecoCota getEnderecoPorTipoEndereco(TipoEndereco tipoEndereco){
		for(EnderecoCota item : this.getEnderecos()){
			if(item.getTipoEndereco() == tipoEndereco){
				return item;
			}
		}
		return null;
	}

	public TipoDistribuicaoCota getTipoDistribuicaoCota() {
		return tipoDistribuicaoCota;
	}

	public void setTipoDistribuicaoCota(TipoDistribuicaoCota tipoDistribuicaoCota) {
		this.tipoDistribuicaoCota = tipoDistribuicaoCota;
	}

	public List<RankingSegmento> getRankingSegmento() {
		return rankingSegmento;
	}

	public void setRankingSegmento(List<RankingSegmento> rankingSegmento) {
		this.rankingSegmento = rankingSegmento;
	}

	public List<EstoqueProdutoCota> getEstoqueProdutoCota() {
		return estoqueProdutoCota;
	}

	public void setEstoqueProdutoCota(List<EstoqueProdutoCota> estoqueProdutoCota) {
		this.estoqueProdutoCota = estoqueProdutoCota;
	}

	public Integer getRecebeRecolheParciais() {
		return recebeRecolheParciais;
	}

	public void setRecebeRecolheParciais(Integer recebeRecolheParciais) {
		this.recebeRecolheParciais = recebeRecolheParciais;
	}

	public BigDecimal getValorMinimoCobranca() {
		return valorMinimoCobranca;
	}

	public void setValorMinimoCobranca(BigDecimal valorMinimoCobranca) {
		this.valorMinimoCobranca = valorMinimoCobranca;
	}

	public Set<CotaUnificacao> getCotasUnificacao() {
		return cotasUnificacao;
	}

	public void setCotasUnificacao(Set<CotaUnificacao> cotasUnificacao) {
		this.cotasUnificacao = cotasUnificacao;
	}

	public ParametroCobrancaDistribuicaoCota getParametroCobrancaDistribuicaoCota() {
		return parametroCobrancaDistribuicaoCota;
	}

	public void setParametroCobrancaDistribuicaoCota(
			ParametroCobrancaDistribuicaoCota parametroCobrancaDistribuicaoCota) {
		this.parametroCobrancaDistribuicaoCota = parametroCobrancaDistribuicaoCota;
	}
	
}