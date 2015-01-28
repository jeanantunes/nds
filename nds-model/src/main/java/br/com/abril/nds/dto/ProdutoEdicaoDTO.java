package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.google.common.base.Strings;

import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TemaProduto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.upload.XlsMapper;

public class ProdutoEdicaoDTO implements Serializable, Comparable<ProdutoEdicaoDTO> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
    // Atributos que também são utilizados para a funcionalidade Add_em_lote

	@XlsMapper(value="cod_produto_prodin")
	private String codigoProduto;
	
	@XlsMapper(value="num_edicao")
	private Long numeroEdicao;
	
	@XlsMapper(value="classificacao")
	private String classificacao;
	
	@XlsMapper(value="pacote_padrao")
	private Integer pacotePadrao;
	
	@XlsMapper(value="nome_comercial_produto")
	private String nomeComercial;
	
	@XlsMapper(value="tipo_distribuicao")
	private String lancamento;
	
	@XlsMapper(value="preco_previsto")
	private String preco;
	
	@XlsMapper(value="data_lancamento_previsto")
	private String dtLancPrevisto;
	
	@XlsMapper(value="data_recolhimento_previsto")
	private String dtRecPrevisto;
	
	@XlsMapper(value="reparte_previsto")
	private BigInteger repartePrevisto;
	
	@XlsMapper(value="reparte_promocional")
	private BigInteger repartePromocional;
	
	@XlsMapper(value="parcial")
	private String recolhimentoParcial;
	
	@XlsMapper(value="cod_barras")
	private String codigoDeBarras;
	
	@XlsMapper(value="cod_corporativo")
	private String codigoDeBarrasCorporativo;
	
	private boolean contagemPacote;
	private BigDecimal precoPrevisto;
	private Long id;
	private Integer sequenciaMatriz;
	private BigDecimal precoVenda;
	private BigDecimal desconto;
	private BigDecimal precoComDesconto;
	private Integer peb;
	private BigDecimal precoCusto;
	private Long peso;
	private Long idProduto;
	private PeriodicidadeProduto periodicidade;
	private Integer numeroPeriodicidade;
	private TipoClassificacaoProduto tipoClassificacaoProduto;
	private String tipoClassificacaoFormatado;
	private String nomeProduto;
	private GrupoProduto grupoProduto;
	private boolean possuiBrinde;
	private String descricaoBrinde;
	private Long idBrinde;
	private BigDecimal expectativaVenda;
	private boolean permiteValeDesconto;
	private boolean parcial;
	private Integer periodo;
	private String periodoString;
	private Integer dia;
	private Date dataRecolhimentoDistribuidor;
	
    // Usados na listagem de Edições:
	private Date dataLancamento;
    private Long idFornecedor;
	private String nomeFornecedor;
	private TipoLancamento tipoLancamento;
	private StatusLancamento situacaoLancamento;
	private String statusLancamento;
	private String statusSituacao;
	private String temBrinde;
	private String status;
	
    // Campos para cadastrar uma nova Edição:
	// codigoProduto;
	private String nomeComercialProduto;
	private String fase;
	private Integer numeroLancamento;	
	// numeroEdicao; pacotePadrao; precoPrevisto
	// precoVenda; (Real)
	// repartePrevisto; 
	// repartePromocional
	// codigoDeBarras
	// codigoDeBarrasCorporativo
	private String descricaoDesconto;
	// desconto;
	private String chamadaCapa;
	// parcial;	-- Regime de Recolhimento;
	// brinde;
	// peso;
	private float largura;
	private float comprimento;
	private float espessura;
	private String boletimInformativo;
	
	// Lancamento:
	// tipoLancamento; 
	private Date dataLancamentoPrevisto;
	private Date dataRecolhimentoPrevisto;
	private Date dataRecolhimentoReal;
	private Integer semanaRecolhimento;
	private Boolean origemInterface;
	private Boolean lancamentoExcluido;
	private String editor;
	private String caracteristicaProduto;
	
    // Segmentação
	private ClasseSocial classeSocial;
	private Sexo sexo;
	private FaixaEtaria faixaEtaria;
	private TemaProduto temaPrincipal;
	private TemaProduto temaSecundario;

	private String dataLancamentoFormatada;
	private BigInteger qtdeVendas;
	private BigInteger reparte;
	private String qtdVendasFormatada;
	
	private String precoVendaFormatado;
	private String precoPrevistoFormatado;
	
	private ModoTela modoTela;

	private String segmentacao;
	
	private Double venda;
	private Double percentualVenda;
	private Long reparteEstudo;
	private Long tipoSegmentoProdutoId;
	
	private FormaComercializacao formaComercializacao;
	
	private boolean parcialConsolidado;
	
	private String descricaoClassificacao;
	private String descricaoSituacaoLancamento;
	
	public ProdutoEdicaoDTO() {};
	
	public ProdutoEdicaoDTO(
			Long id, 
			String codigoProduto, 
			String nomeComercial, 
			Long numeroEdicao, 
			String nomeFornecedor, 
			String statusLancamento, 
			String statusSituacao, 
			String temBrinde) {
		this.id = id;
		this.codigoProduto  = codigoProduto;
		this.nomeComercial  = nomeComercial;
		this.numeroEdicao = numeroEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.statusLancamento  = statusLancamento;
		this.statusSituacao = statusSituacao; 
		this.temBrinde = temBrinde;		
	};
	
	
	/**
	 * Tipo de chamada de encalhe deste produtoEdicao
	 */
	private TipoChamadaEncalhe tipoChamadaEncalhe;

	private String dataRecolhimentoDistribuidorFormatada;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	public Integer getPeb() {
		return peb;
	}
	public void setPeb(Integer peb) {
		this.peb = peb;
	}
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}
	public Long getPeso() {
		return peso;
	}
	public void setPeso(Long peso) {
		this.peso = peso;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}
	public BigDecimal getExpectativaVenda() {
		return expectativaVenda;
	}
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		this.expectativaVenda = expectativaVenda;
	}
	public boolean isPermiteValeDesconto() {
		return permiteValeDesconto;
	}
	public void setPermiteValeDesconto(boolean permiteValeDesconto) {
		this.permiteValeDesconto = permiteValeDesconto;
	}
	public boolean isParcial() {
		return parcial;
	}
	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
		if (dataRecolhimentoDistribuidor != null) {
			this.dataRecolhimentoDistribuidorFormatada = DateUtil.formatarDataPTBR(dataRecolhimentoDistribuidor); 
		}
	}
	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}
	public void setTipoChamadaEncalhe(TipoChamadaEncalhe tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
	}
	
	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
		
		if (dataLancamento != null) {
			this.dataLancamentoFormatada = DateUtil.formatarDataPTBR(dataLancamento); 
		}
	}
	
	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor
	 *            the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor == null ? "" : nomeFornecedor;
	}

	/**
	 * @return the tipoLancamento
	 */
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	/**
	 * @return the statusLancamento
	 */
	public String getStatusLancamento() {
		return this.statusLancamento;
	}
	
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	
	/**
	 * @return the situacaoLancamento
	 */
	public String getStatusSituacao() {
		return this.statusSituacao;
	}

	public void setStatusSituacao(StatusLancamento statusSituacao) {
		this.statusSituacao = "";
    	if (null != statusSituacao) {
	       this.statusSituacao = statusSituacao.getDescricao();
    	}
   }
	
	public void setStatusSituacao(String statusSituacao) {
		this.statusSituacao = "";
		if (null != statusSituacao) {
			this.statusSituacao = statusSituacao;
		}
	}
	
	/**
	 * 
	 * @return the possuiBrindeDescricao;
	 */
	public String getTemBrinde() {
		return this.temBrinde;
	}
	public void setTemBrinde(Boolean temBrinde) {
        this.temBrinde = temBrinde.booleanValue() ? "Sim" : "Não";
	}

	/**
	 * @return the nomeComercialProduto
	 */
	public String getNomeComercialProduto() {
		return nomeComercialProduto;
	}
	/**
	 * @param nomeComercialProduto the nomeComercialProduto to set
	 */
	public void setNomeComercialProduto(String nomeComercialProduto) {
		this.nomeComercialProduto = nomeComercialProduto;
	}

	/**
	 * @return the precoPrevisto
	 */
	public BigDecimal getPrecoPrevisto() {
		return precoPrevisto;
	}
	
	
    public String getPreco() {
        return preco;
    }

    
    public void setPreco(String preco) {
        this.preco = preco;
        if(Strings.isNullOrEmpty(preco))
            return;        
        setPrecoPrevisto(BigDecimal.valueOf(Double.parseDouble(preco.replace(",", "."))));
    }

    /**
	 * @param precoPrevisto the precoPrevisto to set
	 */
	public void setPrecoPrevisto(BigDecimal precoPrevisto) {
		this.precoPrevisto = precoPrevisto;
		this.precoPrevistoFormatado = CurrencyUtil.formatarValor(this.precoPrevisto);
	}
	/**
	 * @return the dataLancamentoPrevisto
	 */
	public Date getDataLancamentoPrevisto() {
		return dataLancamentoPrevisto;
	}
	/**
	 * @param dataLancamentoPrevisto the dataLancamentoPrevisto to set
	 */
	public void setDataLancamentoPrevisto(Date dataLancamentoPrevisto) {
		this.dataLancamentoPrevisto = dataLancamentoPrevisto;
	}
	/**
	 * @return the repartePrevisto
	 */
	public BigInteger getRepartePrevisto() {
		return repartePrevisto;
	}
	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(BigInteger repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * @return the repartePromocional
	 */
	public BigInteger getRepartePromocional() {
		return repartePromocional;
	}
	/**
	 * @param repartePromocional the repartePromocional to set
	 */
	public void setRepartePromocional(BigInteger repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
	/**
	 * @return the codigoDeBarrasCorporativo
	 */
	public String getCodigoDeBarrasCorporativo() {
		return codigoDeBarrasCorporativo;
	}
	/**
	 * @param codigoDeBarrasCorporativo the codigoDeBarrasCorporativo to set
	 */
	public void setCodigoDeBarrasCorporativo(String codigoDeBarrasCorporativo) {
		this.codigoDeBarrasCorporativo = codigoDeBarrasCorporativo;
	}
	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}
	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}
	/**
	 * @return the boletimInformativo
	 */
	public String getBoletimInformativo() {
		return boletimInformativo;
	}
	/**
	 * @param boletimInformativo the boletimInformativo to set
	 */
	public void setBoletimInformativo(String boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}
	/**
	 * @return the fase
	 */
	public String getFase() {
		return fase;
	}
	/**
	 * @param fase the fase to set
	 */
	public void setFase(String fase) {
		this.fase = fase;
	}
	/**
	 * @return the numeroLancamento
	 */
	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}
	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}
	/**
	 * @return the largura
	 */
	public float getLargura() {
		return largura;
	}
	/**
	 * @param largura the largura to set
	 */
	public void setLargura(float largura) {
		this.largura = largura;
	}
	/**
	 * @return the comprimento
	 */
	public float getComprimento() {
		return comprimento;
	}
	/**
	 * @param comprimento the comprimento to set
	 */
	public void setComprimento(float comprimento) {
		this.comprimento = comprimento;
	}
	/**
	 * @return the espessura
	 */
	public float getEspessura() {
		return espessura;
	}
	/**
	 * @param espessura the espessura to set
	 */
	public void setEspessura(float espessura) {
		this.espessura = espessura;
	}
	/**
	 * @return the origemInterface
	 */
	public Boolean isOrigemInterface() {
		return origemInterface;
	}
	/**
	 * @param origemInterface the origemInterface to set
	 */
	public void setOrigemInterface(Boolean origemInterface) {
		this.origemInterface = origemInterface;
	}
	
	public Boolean isLancamentoExcluido() {
		return lancamentoExcluido;
	}

	public void setLancamentoExcluido(Boolean lancamentoExcluido) {
		this.lancamentoExcluido = lancamentoExcluido;
	}

	/**
	 * @return the descricaoDesconto
	 */
	public String getDescricaoDesconto() {
		return descricaoDesconto;
	}
	/**
	 * @param descricaoDesconto the descricaoDesconto to set
	 */
	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}
	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * @return the caracteristicaProduto
	 */
	public String getCaracteristicaProduto() {
		return caracteristicaProduto;
	}
	
	/**
	 * @param caracteristicaProduto the caracteristicaProduto to set
	 */
	public void setCaracteristicaProduto(String caracteristicaProduto) {
		this.caracteristicaProduto = caracteristicaProduto;
	}
	
	/**
	 * @return the dataRecolhimentoPrevisto
	 */
	public Date getDataRecolhimentoPrevisto() {
		return dataRecolhimentoPrevisto;
	}
	/**
	 * @param dataRecolhimentoPrevisto the dataRecolhimentoPrevisto to set
	 */
	public void setDataRecolhimentoPrevisto(Date dataRecolhimentoPrevisto) {
		this.dataRecolhimentoPrevisto = dataRecolhimentoPrevisto;
	}
	/**
	 * @return the dataRecolhimentoReal
	 */
	public Date getDataRecolhimentoReal() {
		return dataRecolhimentoReal;
	}
	/**
	 * @param dataRecolhimentoReal the dataRecolhimentoReal to set
	 */
	public void setDataRecolhimentoReal(Date dataRecolhimentoReal) {
		this.dataRecolhimentoReal = dataRecolhimentoReal;
	}
	/**
	 * @return the semanaRecolhimento
	 */
	public Integer getSemanaRecolhimento() {
		return semanaRecolhimento;
	}
	/**
	 * @param semanaRecolhimento the semanaRecolhimento to set
	 */
	public void setSemanaRecolhimento(Integer semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}
	/**
	 * @return the descricaoBrinde
	 */
	public String getDescricaoBrinde() {
		return descricaoBrinde;
	}
	/**
	 * @param descricaoBrinde the descricaoBrinde to set
	 */
	public void setDescricaoBrinde(String descricaoBrinde) {
		this.descricaoBrinde = descricaoBrinde;
	}
	/**
	 * @return the idBrinde
	 */
	public Long getIdBrinde() {
		return idBrinde;
	}
	/**
	 * @param idBrinde the idBrinde to set
	 */
	public void setIdBrinde(Long idBrinde) {
		this.idBrinde = idBrinde;
	}
	/**
	 * @return the descricaoProduto
	 */
	public String getNomeComercial() {
		return nomeComercial;
	}
	/**
	 * @param nomeComercial the descricaoProduto to set
	 */
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	
	/**
	 * @return the classeSocial
	 */
	public ClasseSocial getClasseSocial() {
		return classeSocial;
	}
	/**
	 * @param classeSocial the classeSocial to set
	 */
	public void setClasseSocial(ClasseSocial classeSocial) {
		this.classeSocial = classeSocial;
	}
	/**
	 * @return the sexo
	 */
	public Sexo getSexo() {
		return sexo;
	}
	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	/**
	 * @return the faixaEtaria
	 */
	public FaixaEtaria getFaixaEtaria() {
		return faixaEtaria;
	}
	/**
	 * @param faixaEtaria the faixaEtaria to set
	 */
	public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}
	/**
	 * @return the temaPrincipal
	 */
	public TemaProduto getTemaPrincipal() {
		return temaPrincipal;
	}
	/**
	 * @param temaPrincipal the temaPrincipal to set
	 */
	public void setTemaPrincipal(TemaProduto temaPrincipal) {
		this.temaPrincipal = temaPrincipal;
	}
	/**
	 * @return the temaSecundario
	 */
	public TemaProduto getTemaSecundario() {
		return temaSecundario;
	}
	/**
	 * @param temaSecundario the temaSecundario to set
	 */
	public void setTemaSecundario(TemaProduto temaSecundario) {
		this.temaSecundario = temaSecundario;
	}
	/**
	 * @return the grupoProduto
	 */
	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}
	/**
	 * @param grupoProduto the grupoProduto to set
	 */
	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public String getDataLancamentoFormatada() {
		return dataLancamentoFormatada;
	}

	public void setDataLancamentoFormatada(String dataLancamentoFormatada) {
		this.dataLancamentoFormatada = dataLancamentoFormatada;
	}

	public BigInteger getQtdeVendas() {
		return qtdeVendas;
	}

	public void setQtdeVendas(BigInteger qtdeVendas) {
		this.qtdeVendas = qtdeVendas;
		
		if (qtdeVendas != null) {
			this.qtdVendasFormatada = qtdeVendas.toString();
		}
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	@Override
    public int compareTo(ProdutoEdicaoDTO o) {  
        return o.getNumeroEdicao().compareTo(this.getNumeroEdicao());
    }

	public PeriodicidadeProduto getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeProduto periodicidade) {
		this.periodicidade = periodicidade;
		this.numeroPeriodicidade = periodicidade.getOrdem();
	}

	public Integer getNumeroPeriodicidade() {
		return numeroPeriodicidade;
	}

	public String getQtdVendasFormatada() {
		return qtdVendasFormatada;
	}

	public String getDataRecolhimentoDistribuidorFormatada() {
		return dataRecolhimentoDistribuidorFormatada;
	}

	public void setDataRecolhimentoDistribuidorFormatada(
			String dataRecolhimentoDistribuidorFormatada) {
		this.dataRecolhimentoDistribuidorFormatada = dataRecolhimentoDistribuidorFormatada;
	}

	public String getSegmentacao() {
		return segmentacao;
	}

	public void setSegmentacao(String segmentacao) {
		this.segmentacao = segmentacao;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public Double getVenda() {
		return venda;
	}

	public void setVenda(Double venda) {
		this.venda = venda;
	}

	public Long getReparteEstudo() {
		return reparteEstudo;
	}

	public void setReparteEstudo(Long reparteEstudo) {
		this.reparteEstudo = reparteEstudo;
	}

	public Double getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(Double percentualVenda) {
		this.percentualVenda = percentualVenda;
	}

	public Long getTipoSegmentoProdutoId() {
		return tipoSegmentoProdutoId;
	}

	public void setTipoSegmentoProdutoId(Long tipoSegmentoProdutoId) {
		this.tipoSegmentoProdutoId = tipoSegmentoProdutoId;
	}

	public TipoClassificacaoProduto getTipoClassificacaoProduto() {
		return tipoClassificacaoProduto;
	}

	public void setTipoClassificacaoProduto(TipoClassificacaoProduto tipoClassificacaoProduto) {
		this.tipoClassificacaoProduto = tipoClassificacaoProduto;
		this.tipoClassificacaoFormatado = tipoClassificacaoProduto == null ? "" : tipoClassificacaoProduto.getDescricao();
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	public Integer getPeriodo() {
	    return periodo;
	}

	public void setPeriodo(Integer periodo) {
	    this.periodo = periodo;
	    
	    if(periodo != null){
	    	this.periodoString = periodo.toString();
	    }else{
	    	this.periodoString = "";
	    }
	    
	}

	public Long getIdProduto() {
	    return idProduto;
	}

	public void setIdProduto(Long idProduto) {
	    this.idProduto = idProduto;
	}
	
	public String getLancamento() {
		return lancamento;
	}

	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}

	public String getDtLancPrevisto() {
		return dtLancPrevisto;
	}

	public void setDtLancPrevisto(String dtLancPrevisto) {
		this.dtLancPrevisto = dtLancPrevisto;
	}

	public String getDtRecPrevisto() {
		return dtRecPrevisto;
	}

	public void setDtRecPrevisto(String dtRecPrevisto) {
		this.dtRecPrevisto = dtRecPrevisto;
	}

	public String getRecolhimentoParcial() {
		return recolhimentoParcial;
	}

	public void setRecolhimentoParcial(String recolhimentoParcial) {
		this.recolhimentoParcial = recolhimentoParcial;
	}
	

	/**
	 * @return the modoTela
	 */
	public ModoTela getModoTela() {
		return modoTela;
	}

	/**
	 * @param modoTela the modoTela to set
	 */
	public void setModoTela(ModoTela modoTela) {
		this.modoTela = modoTela;
	}

	public enum ModoTela {
		NOVO, EDICAO, REDISTRIBUICAO;
	}
	
	/**
	 * @return the precoVendaFormatado
	 */
	public String getPrecoVendaFormatado() {
		return precoVendaFormatado;
	}

	/**
	 * @param precoVendaFormatado the precoVendaFormatado to set
	 */
	public void setPrecoVendaFormatado(String precoVendaFormatado) {
		this.precoVendaFormatado = precoVendaFormatado;
	}

	/**
	 * @return the precoPrevistoFormatado
	 */
	public String getPrecoPrevistoFormatado() {
		return precoPrevistoFormatado;
	}

	/**
	 * @param precoPrevistoFormatado the precoPrevistoFormatado to set
	 */
	public void setPrecoPrevistoFormatado(String precoPrevistoFormatado) {
		this.precoPrevistoFormatado = precoPrevistoFormatado;
	}

	public StatusLancamento getSituacaoLancamento() {
		return situacaoLancamento;
	}

	public void setSituacaoLancamento(StatusLancamento situacaoLancamento) {
		this.situacaoLancamento = situacaoLancamento;
	}

	public String getTipoClassificacaoFormatado() {
		return tipoClassificacaoFormatado;
    }
    
    /**
     * @return the idFornecedor
     */
    public Long getIdFornecedor() {
        return idFornecedor;
    }
    
    /**
     * @param idFornecedor the idFornecedor to set
     */
    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

	public boolean isContagemPacote() {
		return contagemPacote;
	}

	public void setContagemPacote(boolean contagemPacote) {
		this.contagemPacote = contagemPacote;
	}

	public FormaComercializacao getFormaComercializacao() {
		return formaComercializacao;
	}

	public void setFormaComercializacao(FormaComercializacao formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

	public String getPeriodoString() {
		return periodoString;
	}

	public void setPeriodoString(String periodoString) {
		this.periodoString = periodoString;
	}

	public boolean isParcialConsolidado() {
		return parcialConsolidado;
	}

	public void setParcialConsolidado(boolean parcialConsolidado) {
		this.parcialConsolidado = parcialConsolidado;
	}

	public String getDescricaoClassificacao() {
		return descricaoClassificacao;
	}

	public void setDescricaoClassificacao(String descricaoClassificacao) {
		this.descricaoClassificacao = descricaoClassificacao;
		this.tipoClassificacaoFormatado = descricaoClassificacao;  
	}

	public String getDescricaoSituacaoLancamento() {
		return descricaoSituacaoLancamento;
	}

	public void setDescricaoSituacaoLancamento(String descricaoSituacaoLancamento) {
		this.descricaoSituacaoLancamento = descricaoSituacaoLancamento;
		this.situacaoLancamento = StatusLancamento.valueOf(descricaoSituacaoLancamento);
	}
	

}