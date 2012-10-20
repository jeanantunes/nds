package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author erick.dzadotz
 * @version 1.0
 */
@Record
public class EMS0110Input extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codDistrib;				
	private String dataGeracaoArq;
	private String horaGeracaoArq;
	private String mnemonicoTab;
	private Integer contextoProd;			
	private Long codForncProd;				
	private String codProd;  				
	private Long edicaoProd; 				
	private String nomeProd; 				
	private String codBarra;				
	private Long pesoUni; 			
	private String tipoProd;				
	private int peb;						
 	private Float largura;
	private Float comprimento;
	private Float expessura;
	private String codSitTributaria;
	private String codSitFiscal;
	private int pactPadrao;					
	private String tipoMaterialPromocional;
	private String tipoMaterialDivulgacao;
	private String tipoMaterialTroca;
	private String valorValeDesconto;
	private String valorMaterialTroca;
	private boolean contemBrinde;			
	private String codNBM;
	private String descBrinde;
	private Boolean condVendeSeparado;
	private boolean statusProd;
	private Date dataDesativacao;
	private String chamadaCapa;
	private String edicao;
	private String regimeRecolhimento;
	private String segmentacaoClasseSocial;
	private String segmentacaoPeriodicidade;
	private String segmentacaoFormaFiscal;
	private String segmentacaoSexo;
	private String segmentacaoIdade;
	private String segmentacaoLancamento;
	private String segmentacaoTemaPrincipal;
	private String segmentacaoTemaSecundario;
	private String codCategoria;
	private Integer contextoProdReferencia;
	private Long codFornecProdReferencia;	
	private String codProdReferencia;
	private BigDecimal tipoDesconto;		
	private Integer contextoEditor;			
	private Long codEditor;					
	private Integer contextoPublicacao;
	private Long codFornecPublicacao;
	private String codColecao;
	private String formaInclusao;
	private String codPublicacao;
	private String campoObscuro;
	private String nomeComercial;
	
	@Field(offset = 1, length = 7)
	public String getCodDistrib() {
		return codDistrib;
	}
	
	public void setCodDistrib(String codDistrib) {
		this.codDistrib = codDistrib;
	}
	
	public String getDataGeracaoArq() {
		return dataGeracaoArq;
	}
	
	public void setDataGeracaoArq(String dataGeracaoArq) {
		this.dataGeracaoArq = dataGeracaoArq;
	}
	
	public String getHoraGeracaoArq() {
		return horaGeracaoArq;
	}
	
	public void setHoraGeracaoArq(String horaGeracaoArq) {
		this.horaGeracaoArq = horaGeracaoArq;
	}
	
	public String getMnemonicoTab() {
		return mnemonicoTab;
	}
	
	public void setMnemonicoTab(String mnemonicoTab) {
		this.mnemonicoTab = mnemonicoTab;
	}
	
	@Field(offset = 26, length = 1)
	public Integer getContextoProd() {
		return contextoProd;
	}
	
	public void setContextoProd(Integer contextoProd) {
		this.contextoProd = contextoProd;
	}
	
	@Field(offset = 27, length = 7)
	public Long getCodForncProd() {
		return codForncProd;
	}
	
	public void setCodForncProd(Long codForncProd) {
		this.codForncProd = codForncProd;
	}
	
	@Field(offset = 34, length = 8)
	public String getCodProd() {
		return codProd;
	}
	
	public void setCodProd(String codProd) {
		this.codProd = codProd;
	}
	
	@Field(offset = 42, length = 4)
	public Long getEdicaoProd() {
		return edicaoProd;
	}
	
	public void setEdicaoProd(Long edicaoProd) {
		this.edicaoProd = edicaoProd;
	}
	
	@Field(offset = 46, length = 30)
	public String getNomeProd() {
		return nomeProd;
	}
	
	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}
	
	@Field(offset = 76, length = 18)
	public String getCodBarra() {
		return codBarra;
	}
	
	public void setCodBarra(String codBarra) {
		this.codBarra = codBarra;
	}
	
	@Field(offset = 94, length = 5)
	public Long getPesoUni() {
		return pesoUni;
	}
	
	public void setPesoUni(Long pesoUni) {
		this.pesoUni = pesoUni;
	}
	
	@Field(offset = 99, length = 3)
	public String getTipoProd() {
		return tipoProd;
	}
	
	public void setTipoProd(String tipoProd) {
		this.tipoProd = tipoProd;
	}
	
	@Field(offset = 102, length = 3)
	public int getPeb() {
		return peb;
	}
	
	public void setPeb(int peb) {
		this.peb = peb;
	}
	
	@Field(offset = 105, length = 5)
	public Float getLargura() {
		return largura;
	}
	
	public void setLargura(Float largura) {
		this.largura = largura;
	}
	
	@Field(offset = 110, length = 5)
	public Float getComprimento() {
		return comprimento;
	}
	
	public void setComprimento(Float comprimento) {
		this.comprimento = comprimento;
	}
	
	@Field(offset = 115, length = 5)
	public Float getExpessura() {
		return expessura;
	}
	
	public void setExpessura(Float expessura) {
		this.expessura = expessura;
	}
	
	@Field(offset = 120, length = 1)
	public String getCodSitTributaria() {
		return codSitTributaria;
	}
	
	public void setCodSitTributaria(String codSitTributaria) {
		this.codSitTributaria = codSitTributaria;
	}
	
	@Field(offset = 121, length = 1)
	public String getCodSitFiscal() {
		return codSitFiscal;
	}
	
	public void setCodSitFiscal(String codSitFiscal) {
		this.codSitFiscal = codSitFiscal;
	}
	
	@Field(offset = 122, length = 8)
	public int getPactPadrao() {
		return pactPadrao;
	}
	
	public void setPactPadrao(int pactPadrao) {
		this.pactPadrao = pactPadrao;
	}
	
	@Field(offset = 130, length = 3)
	public String getTipoMaterialPromocional() {
		return tipoMaterialPromocional;
	}
	
	public void setTipoMaterialPromocional(String tipoMaterialPromocional) {
		this.tipoMaterialPromocional = tipoMaterialPromocional;
	}
	
	@Field(offset = 133, length = 3)
	public String getTipoMaterialDivulgacao() {
		return tipoMaterialDivulgacao;
	}
	
	public void setTipoMaterialDivulgacao(String tipoMaterialDivulgacao) {
		this.tipoMaterialDivulgacao = tipoMaterialDivulgacao;
	}
	
	@Field(offset = 136, length = 3)
	public String getTipoMaterialTroca() {
		return tipoMaterialTroca;
	}
	
	public void setTipoMaterialTroca(String tipoMaterialTroca) {
		this.tipoMaterialTroca = tipoMaterialTroca;
	}
	
	@Field(offset = 139, length = 10)
	public String getValorValeDesconto() {
		return valorValeDesconto;
	}
	
	public void setValorValeDesconto(String valorValeDesconto) {
		this.valorValeDesconto = valorValeDesconto;
	}
	
	@Field(offset = 149, length = 10)
	public String getValorMaterialTroca() {
		return valorMaterialTroca;
	}
	
	public void setValorMaterialTroca(String valorMaterialTroca) {
		this.valorMaterialTroca = valorMaterialTroca;
	}
	
	@Field(offset = 186, length = 1)
	@FixedFormatBoolean(trueValue = "S", falseValue = "N")
	public boolean isContemBrinde() {
		return contemBrinde;
	}
	
	public void setContemBrinde(boolean contemBrinde) {
		this.contemBrinde = contemBrinde;
	}
	
	@Field(offset = 187, length = 9)
	public String getCodNBM() {
		return codNBM;
	}
	
	public void setCodNBM(String codNBM) {
		this.codNBM = codNBM;
	}
	
	@Field(offset = 199, length = 300)
	public String getDescBrinde() {
		return descBrinde;
	}
	
	public void setDescBrinde(String descBrinde) {
		this.descBrinde = descBrinde;
	}
	
	@FixedFormatBoolean(falseValue = "N", trueValue = "S")	
	@Field(offset = 499, length = 1)
	public Boolean getCondVendeSeparado() {
		return condVendeSeparado;
	}
	
	public void setCondVendeSeparado(Boolean condVendeSeparado) {
		this.condVendeSeparado = condVendeSeparado;
	}
	
	@FixedFormatBoolean(trueValue = "A")
	@Field(offset = 501, length = 1)
	public boolean getStatusProd() {
		return statusProd;
	}
	
	public void setStatusProd(boolean statusProd) {
		this.statusProd = statusProd;
	}
	
	@Field(offset = 502, length = 8)
	public Date getDataDesativacao() {
		return dataDesativacao;
	}
	
	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}
	
	@Field(offset = 513, length = 30)
	public String getChamadaCapa() {
		return chamadaCapa;
	}
	
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}
	
	@Field(offset = 543, length = 4)
	public String getEdicao() {
		return edicao;
	}
	
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 552, length = 1)
	public String getRegimeRecolhimento() {
		return regimeRecolhimento;
	}
	
	public void setRegimeRecolhimento(String regimeRecolhimento) {
		this.regimeRecolhimento = regimeRecolhimento;
	}
	
	@Field(offset = 553, length = 2)
	public String getSegmentacaoClasseSocial() {
		return segmentacaoClasseSocial;
	}
	
	public void setSegmentacaoClasseSocial(String segmentacaoClasseSocial) {
		this.segmentacaoClasseSocial = segmentacaoClasseSocial;
	}
	
	@Field(offset = 555, length = 3)
	public String getSegmentacaoPeriodicidade() {
		return segmentacaoPeriodicidade;
	}
	
	public void setSegmentacaoPeriodicidade(String segmentacaoPeriodicidade) {
		this.segmentacaoPeriodicidade = segmentacaoPeriodicidade;
	}
	
	@Field(offset = 558, length = 2)
	public String getSegmentacaoFormaFiscal() {
		return segmentacaoFormaFiscal;
	}
	
	public void setSegmentacaoFormaFiscal(String segmentacaoFormaFiscal) {
		this.segmentacaoFormaFiscal = segmentacaoFormaFiscal;
	}
	
	@Field(offset = 560, length = 1)
	public String getSegmentacaoSexo() {
		return segmentacaoSexo;
	}
	
	public void setSegmentacaoSexo(String segmentacaoSexo) {
		this.segmentacaoSexo = segmentacaoSexo;
	}
	
	@Field(offset = 561, length = 1)
	public String getSegmentacaoIdade() {
		return segmentacaoIdade;
	}
	
	public void setSegmentacaoIdade(String segmentacaoIdade) {
		this.segmentacaoIdade = segmentacaoIdade;
	}
	
	@Field(offset = 562, length = 1)
	public String getSegmentacaoLancamento() {
		return segmentacaoLancamento;
	}
	
	public void setSegmentacaoLancamento(String segmentacaoLancamento) {
		this.segmentacaoLancamento = segmentacaoLancamento;
	}
	
	@Field(offset = 563, length = 2)
	public String getSegmentacaoTemaPrincipal() {
		return segmentacaoTemaPrincipal;
	}
	
	public void setSegmentacaoTemaPrincipal(String segmentacaoTemaPrincipal) {
		this.segmentacaoTemaPrincipal = segmentacaoTemaPrincipal;
	}
	
	@Field(offset = 565, length = 2)
	public String getSegmentacaoTemaSecundario() {
		return segmentacaoTemaSecundario;
	}
	
	public void setSegmentacaoTemaSecundario(String segmentacaoTemaSecundario) {
		this.segmentacaoTemaSecundario = segmentacaoTemaSecundario;
	}
	
	@Field(offset = 567, length = 3)
	public String getCodCategoria() {
		return codCategoria;
	}
	
	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}
	
	@Field(offset = 570, length = 1)
	public Integer getContextoProdReferencia() {
		return contextoProdReferencia;
	}
	
	public void setContextoProdReferencia(Integer contextoProdReferencia) {
		this.contextoProdReferencia = contextoProdReferencia;
	}
	
	@Field(offset = 571, length = 1)
	public Long getCodFornecProdReferencia() {
		return codFornecProdReferencia;
	}
	
	public void setCodFornecProdReferencia(Long codFornecProdReferencia) {
		this.codFornecProdReferencia = codFornecProdReferencia;
	}
	
	@Field(offset = 578, length = 12)
	public String getCodProdReferencia() {
		return codProdReferencia;
	}
	
	public void setCodProdReferencia(String codProdReferencia) {
		this.codProdReferencia = codProdReferencia;
	}
	
	@Field(offset = 590, length = 2)
	public BigDecimal getTipoDesconto() {
		return tipoDesconto;
	}
	
	public void setTipoDesconto(BigDecimal tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
	
	@Field(offset = 592, length = 1)
	public Integer getContextoEditor() {
		return contextoEditor;
	}
	
	public void setContextoEditor(Integer contextoEditor) {
		this.contextoEditor = contextoEditor;
	}
	
	@Field(offset = 593, length = 7)
	public Long getCodEditor() {
		return codEditor;
	}
	
	public void setCodEditor(Long codEditor) {
		this.codEditor = codEditor;
	}
	
	@Field(offset = 600, length = 1)
	public Integer getContextoPublicacao() {
		return contextoPublicacao;
	}
	
	public void setContextoPublicacao(Integer contextoPublicacao) {
		this.contextoPublicacao = contextoPublicacao;
	}
	
	@Field(offset = 601, length = 7)
	public Long getCodFornecPublicacao() {
		return codFornecPublicacao;
	}
	
	public void setCodFornecPublicacao(Long codFornecPublicacao) {
		this.codFornecPublicacao = codFornecPublicacao;
	}
	
	@Field(offset = 608, length = 3)
	public String getCodColecao() {
		return codColecao;
	}
	
	public void setCodColecao(String codColecao) {
		this.codColecao = codColecao;
	}
	
	@Field(offset = 611, length = 1)
	public String getFormaInclusao() {
		return formaInclusao;
	}
	
	public void setFormaInclusao(String formaInclusao) {
		this.formaInclusao = formaInclusao;
	}
	
	@Field(offset = 612, length = 8)
	public String getCodPublicacao() {
		return codPublicacao;
	}
	
	public void setCodPublicacao(String codPublicacao) {
		this.codPublicacao = codPublicacao;
	}

	@Field(offset = 620, length = 6)
	public String getCampoObscuro() {
		return campoObscuro;
	}
	
	public void setCampoObscuro(String campoObscuro) {
		this.campoObscuro = campoObscuro;
	}

	@Field(offset = 626, length = 45)
	public String getNomeComercial() {
		return nomeComercial;
	}
	
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chamadaCapa == null) ? 0 : chamadaCapa.hashCode());
		result = prime * result
				+ ((codBarra == null) ? 0 : codBarra.hashCode());
		result = prime * result
				+ ((codCategoria == null) ? 0 : codCategoria.hashCode());
		result = prime * result
				+ ((codColecao == null) ? 0 : codColecao.hashCode());
		result = prime * result
				+ ((codDistrib == null) ? 0 : codDistrib.hashCode());
		result = prime * result
				+ ((codEditor == null) ? 0 : codEditor.hashCode());
		result = prime * result
				+ ((codForncProd == null) ? 0 : codForncProd.hashCode());
		result = prime
				* result
				+ ((codFornecProdReferencia == null) ? 0
						: codFornecProdReferencia.hashCode());
		result = prime
				* result
				+ ((codFornecPublicacao == null) ? 0 : codFornecPublicacao
						.hashCode());
		result = prime * result + ((codNBM == null) ? 0 : codNBM.hashCode());
		result = prime * result + ((codProd == null) ? 0 : codProd.hashCode());
		result = prime
				* result
				+ ((codProdReferencia == null) ? 0 : codProdReferencia
						.hashCode());
		result = prime * result
				+ ((codPublicacao == null) ? 0 : codPublicacao.hashCode());
		result = prime * result
				+ ((codSitFiscal == null) ? 0 : codSitFiscal.hashCode());
		result = prime
				* result
				+ ((codSitTributaria == null) ? 0 : codSitTributaria.hashCode());
		result = prime * result
				+ ((comprimento == null) ? 0 : comprimento.hashCode());
		result = prime
				* result
				+ ((condVendeSeparado == null) ? 0 : condVendeSeparado
						.hashCode());
		result = prime * result + (contemBrinde ? 1231 : 1237);
		result = prime * result
				+ ((contextoEditor == null) ? 0 : contextoEditor.hashCode());
		result = prime * result
				+ ((contextoProd == null) ? 0 : contextoProd.hashCode());
		result = prime
				* result
				+ ((contextoProdReferencia == null) ? 0
						: contextoProdReferencia.hashCode());
		result = prime
				* result
				+ ((contextoPublicacao == null) ? 0 : contextoPublicacao
						.hashCode());
		result = prime * result
				+ ((dataDesativacao == null) ? 0 : dataDesativacao.hashCode());
		result = prime * result
				+ ((dataGeracaoArq == null) ? 0 : dataGeracaoArq.hashCode());
		result = prime * result
				+ ((descBrinde == null) ? 0 : descBrinde.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result
				+ ((edicaoProd == null) ? 0 : edicaoProd.hashCode());
		result = prime * result
				+ ((expessura == null) ? 0 : expessura.hashCode());
		result = prime * result
				+ ((formaInclusao == null) ? 0 : formaInclusao.hashCode());
		result = prime * result
				+ ((horaGeracaoArq == null) ? 0 : horaGeracaoArq.hashCode());
		result = prime * result + ((largura == null) ? 0 : largura.hashCode());
		result = prime * result
				+ ((mnemonicoTab == null) ? 0 : mnemonicoTab.hashCode());
		result = prime * result
				+ ((nomeProd == null) ? 0 : nomeProd.hashCode());
		result = prime * result + pactPadrao;
		result = prime * result + peb;
		result = prime * result + ((pesoUni == null) ? 0 : pesoUni.hashCode());
		result = prime
				* result
				+ ((regimeRecolhimento == null) ? 0 : regimeRecolhimento
						.hashCode());
		result = prime
				* result
				+ ((segmentacaoClasseSocial == null) ? 0
						: segmentacaoClasseSocial.hashCode());
		result = prime
				* result
				+ ((segmentacaoFormaFiscal == null) ? 0
						: segmentacaoFormaFiscal.hashCode());
		result = prime
				* result
				+ ((segmentacaoIdade == null) ? 0 : segmentacaoIdade.hashCode());
		result = prime
				* result
				+ ((segmentacaoLancamento == null) ? 0 : segmentacaoLancamento
						.hashCode());
		result = prime
				* result
				+ ((segmentacaoPeriodicidade == null) ? 0
						: segmentacaoPeriodicidade.hashCode());
		result = prime * result
				+ ((segmentacaoSexo == null) ? 0 : segmentacaoSexo.hashCode());
		result = prime
				* result
				+ ((segmentacaoTemaPrincipal == null) ? 0
						: segmentacaoTemaPrincipal.hashCode());
		result = prime
				* result
				+ ((segmentacaoTemaSecundario == null) ? 0
						: segmentacaoTemaSecundario.hashCode());
		result = prime * result + (statusProd ? 1231 : 1237);
		result = prime * result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
		result = prime
				* result
				+ ((tipoMaterialDivulgacao == null) ? 0
						: tipoMaterialDivulgacao.hashCode());
		result = prime
				* result
				+ ((tipoMaterialPromocional == null) ? 0
						: tipoMaterialPromocional.hashCode());
		result = prime
				* result
				+ ((tipoMaterialTroca == null) ? 0 : tipoMaterialTroca
						.hashCode());
		result = prime * result
				+ ((tipoProd == null) ? 0 : tipoProd.hashCode());
		result = prime
				* result
				+ ((valorMaterialTroca == null) ? 0 : valorMaterialTroca
						.hashCode());
		result = prime
				* result
				+ ((valorValeDesconto == null) ? 0 : valorValeDesconto
						.hashCode());
		result = prime
				* result
				+ ((campoObscuro == null) ? 0 : campoObscuro
						.hashCode());
		result = prime
				* result
				+ ((nomeComercial == null) ? 0 : nomeComercial
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EMS0110Input other = (EMS0110Input) obj;
		if (chamadaCapa == null) {
			if (other.chamadaCapa != null)
				return false;
		} else if (!chamadaCapa.equals(other.chamadaCapa))
			return false;
		if (codBarra == null) {
			if (other.codBarra != null)
				return false;
		} else if (!codBarra.equals(other.codBarra))
			return false;
		if (codCategoria == null) {
			if (other.codCategoria != null)
				return false;
		} else if (!codCategoria.equals(other.codCategoria))
			return false;
		if (codColecao == null) {
			if (other.codColecao != null)
				return false;
		} else if (!codColecao.equals(other.codColecao))
			return false;
		if (codDistrib == null) {
			if (other.codDistrib != null)
				return false;
		} else if (!codDistrib.equals(other.codDistrib))
			return false;
		if (codEditor == null) {
			if (other.codEditor != null)
				return false;
		} else if (!codEditor.equals(other.codEditor))
			return false;
		if (codForncProd == null) {
			if (other.codForncProd != null)
				return false;
		} else if (!codForncProd.equals(other.codForncProd))
			return false;
		if (codFornecProdReferencia == null) {
			if (other.codFornecProdReferencia != null)
				return false;
		} else if (!codFornecProdReferencia
				.equals(other.codFornecProdReferencia))
			return false;
		if (codFornecPublicacao == null) {
			if (other.codFornecPublicacao != null)
				return false;
		} else if (!codFornecPublicacao.equals(other.codFornecPublicacao))
			return false;
		if (codNBM == null) {
			if (other.codNBM != null)
				return false;
		} else if (!codNBM.equals(other.codNBM))
			return false;
		if (codProd == null) {
			if (other.codProd != null)
				return false;
		} else if (!codProd.equals(other.codProd))
			return false;
		if (codProdReferencia == null) {
			if (other.codProdReferencia != null)
				return false;
		} else if (!codProdReferencia.equals(other.codProdReferencia))
			return false;
		if (codPublicacao == null) {
			if (other.codPublicacao != null)
				return false;
		} else if (!codPublicacao.equals(other.codPublicacao))
			return false;
		if (codSitFiscal == null) {
			if (other.codSitFiscal != null)
				return false;
		} else if (!codSitFiscal.equals(other.codSitFiscal))
			return false;
		if (codSitTributaria == null) {
			if (other.codSitTributaria != null)
				return false;
		} else if (!codSitTributaria.equals(other.codSitTributaria))
			return false;
		if (comprimento == null) {
			if (other.comprimento != null)
				return false;
		} else if (!comprimento.equals(other.comprimento))
			return false;
		if (condVendeSeparado == null) {
			if (other.condVendeSeparado != null)
				return false;
		} else if (!condVendeSeparado.equals(other.condVendeSeparado))
			return false;
		if (contemBrinde != other.contemBrinde)
			return false;
		if (contextoEditor == null) {
			if (other.contextoEditor != null)
				return false;
		} else if (!contextoEditor.equals(other.contextoEditor))
			return false;
		if (contextoProd == null) {
			if (other.contextoProd != null)
				return false;
		} else if (!contextoProd.equals(other.contextoProd))
			return false;
		if (contextoProdReferencia == null) {
			if (other.contextoProdReferencia != null)
				return false;
		} else if (!contextoProdReferencia.equals(other.contextoProdReferencia))
			return false;
		if (contextoPublicacao == null) {
			if (other.contextoPublicacao != null)
				return false;
		} else if (!contextoPublicacao.equals(other.contextoPublicacao))
			return false;
		if (dataDesativacao == null) {
			if (other.dataDesativacao != null)
				return false;
		} else if (!dataDesativacao.equals(other.dataDesativacao))
			return false;
		if (dataGeracaoArq == null) {
			if (other.dataGeracaoArq != null)
				return false;
		} else if (!dataGeracaoArq.equals(other.dataGeracaoArq))
			return false;
		if (descBrinde == null) {
			if (other.descBrinde != null)
				return false;
		} else if (!descBrinde.equals(other.descBrinde))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (edicaoProd == null) {
			if (other.edicaoProd != null)
				return false;
		} else if (!edicaoProd.equals(other.edicaoProd))
			return false;
		if (expessura == null) {
			if (other.expessura != null)
				return false;
		} else if (!expessura.equals(other.expessura))
			return false;
		if (formaInclusao == null) {
			if (other.formaInclusao != null)
				return false;
		} else if (!formaInclusao.equals(other.formaInclusao))
			return false;
		if (horaGeracaoArq == null) {
			if (other.horaGeracaoArq != null)
				return false;
		} else if (!horaGeracaoArq.equals(other.horaGeracaoArq))
			return false;
		if (largura == null) {
			if (other.largura != null)
				return false;
		} else if (!largura.equals(other.largura))
			return false;
		if (mnemonicoTab == null) {
			if (other.mnemonicoTab != null)
				return false;
		} else if (!mnemonicoTab.equals(other.mnemonicoTab))
			return false;
		if (nomeProd == null) {
			if (other.nomeProd != null)
				return false;
		} else if (!nomeProd.equals(other.nomeProd))
			return false;
		if (pactPadrao != other.pactPadrao)
			return false;
		if (peb != other.peb)
			return false;
		if (pesoUni == null) {
			if (other.pesoUni != null)
				return false;
		} else if (!pesoUni.equals(other.pesoUni))
			return false;
		if (regimeRecolhimento == null) {
			if (other.regimeRecolhimento != null)
				return false;
		} else if (!regimeRecolhimento.equals(other.regimeRecolhimento))
			return false;
		if (segmentacaoClasseSocial == null) {
			if (other.segmentacaoClasseSocial != null)
				return false;
		} else if (!segmentacaoClasseSocial
				.equals(other.segmentacaoClasseSocial))
			return false;
		if (segmentacaoFormaFiscal == null) {
			if (other.segmentacaoFormaFiscal != null)
				return false;
		} else if (!segmentacaoFormaFiscal.equals(other.segmentacaoFormaFiscal))
			return false;
		if (segmentacaoIdade == null) {
			if (other.segmentacaoIdade != null)
				return false;
		} else if (!segmentacaoIdade.equals(other.segmentacaoIdade))
			return false;
		if (segmentacaoLancamento == null) {
			if (other.segmentacaoLancamento != null)
				return false;
		} else if (!segmentacaoLancamento.equals(other.segmentacaoLancamento))
			return false;
		if (segmentacaoPeriodicidade == null) {
			if (other.segmentacaoPeriodicidade != null)
				return false;
		} else if (!segmentacaoPeriodicidade
				.equals(other.segmentacaoPeriodicidade))
			return false;
		if (segmentacaoSexo == null) {
			if (other.segmentacaoSexo != null)
				return false;
		} else if (!segmentacaoSexo.equals(other.segmentacaoSexo))
			return false;
		if (segmentacaoTemaPrincipal == null) {
			if (other.segmentacaoTemaPrincipal != null)
				return false;
		} else if (!segmentacaoTemaPrincipal
				.equals(other.segmentacaoTemaPrincipal))
			return false;
		if (segmentacaoTemaSecundario == null) {
			if (other.segmentacaoTemaSecundario != null)
				return false;
		} else if (!segmentacaoTemaSecundario
				.equals(other.segmentacaoTemaSecundario))
			return false;
		if (statusProd != other.statusProd)
			return false;
		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;
		if (tipoMaterialDivulgacao == null) {
			if (other.tipoMaterialDivulgacao != null)
				return false;
		} else if (!tipoMaterialDivulgacao.equals(other.tipoMaterialDivulgacao))
			return false;
		if (tipoMaterialPromocional == null) {
			if (other.tipoMaterialPromocional != null)
				return false;
		} else if (!tipoMaterialPromocional
				.equals(other.tipoMaterialPromocional))
			return false;
		if (tipoMaterialTroca == null) {
			if (other.tipoMaterialTroca != null)
				return false;
		} else if (!tipoMaterialTroca.equals(other.tipoMaterialTroca))
			return false;
		if (tipoProd == null) {
			if (other.tipoProd != null)
				return false;
		} else if (!tipoProd.equals(other.tipoProd))
			return false;
		if (valorMaterialTroca == null) {
			if (other.valorMaterialTroca != null)
				return false;
		} else if (!valorMaterialTroca.equals(other.valorMaterialTroca))
			return false;
		if (valorValeDesconto == null) {
			if (other.valorValeDesconto != null)
				return false;
		} else if (!valorValeDesconto.equals(other.valorValeDesconto))
			return false;
		if (campoObscuro == null) {
			if (other.campoObscuro != null)
				return false;
		} else if (!campoObscuro.equals(other.campoObscuro))
			return false;
		if (nomeComercial == null) {
			if (other.nomeComercial != null)
				return false;
		} else if (!nomeComercial.equals(other.nomeComercial))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "EMS0110Input [codDistrib=" + codDistrib + ", dataGeracaoArq="
				+ dataGeracaoArq + ", horaGeracaoArq=" + horaGeracaoArq
				+ ", mnemonicoTab=" + mnemonicoTab + ", contextoProd="
				+ contextoProd + ", codForncProd=" + codForncProd
				+ ", codProd=" + codProd + ", edicaoProd=" + edicaoProd
				+ ", nomeProd=" + nomeProd + ", codBarra=" + codBarra
				+ ", pesoUni=" + pesoUni + ", tipoProd=" + tipoProd + ", peb="
				+ peb + ", largura=" + largura + ", comprimento=" + comprimento
				+ ", expessura=" + expessura + ", codSitTributaria="
				+ codSitTributaria + ", codSitFiscal=" + codSitFiscal
				+ ", pactPadrao=" + pactPadrao + ", tipoMaterialPromocional="
				+ tipoMaterialPromocional + ", tipoMaterialDivulgacao="
				+ tipoMaterialDivulgacao + ", tipoMaterialTroca="
				+ tipoMaterialTroca + ", valorValeDesconto="
				+ valorValeDesconto + ", valorMaterialTroca="
				+ valorMaterialTroca + ", contemBrinde=" + contemBrinde
				+ ", codNBM=" + codNBM + ", descBrinde=" + descBrinde
				+ ", condVendeSeparado=" + condVendeSeparado + ", statusProd="
				+ statusProd + ", dataDesativacao=" + dataDesativacao
				+ ", chamadaCapa=" + chamadaCapa + ", edicao=" + edicao
				+ ", regimeRecolhimento=" + regimeRecolhimento
				+ ", segmentacaoClasseSocial=" + segmentacaoClasseSocial
				+ ", segmentacaoPeriodicidade=" + segmentacaoPeriodicidade
				+ ", segmentacaoFormaFiscal=" + segmentacaoFormaFiscal
				+ ", segmentacaoSexo=" + segmentacaoSexo
				+ ", segmentacaoIdade=" + segmentacaoIdade
				+ ", segmentacaoLancamento=" + segmentacaoLancamento
				+ ", segmentacaoTemaPrincipal=" + segmentacaoTemaPrincipal
				+ ", segmentacaoTemaSecundario=" + segmentacaoTemaSecundario
				+ ", codCategoria=" + codCategoria
				+ ", contextoProdReferencia=" + contextoProdReferencia
				+ ", codFornecProdReferencia=" + codFornecProdReferencia
				+ ", codProdReferencia=" + codProdReferencia
				+ ", tipoDesconto=" + tipoDesconto + ", contextoEditor="
				+ contextoEditor + ", codEditor=" + codEditor
				+ ", contextoPublicacao=" + contextoPublicacao
				+ ", codFornecPublicacao=" + codFornecPublicacao
				+ ", codColecao=" + codColecao + ", formaInclusao="
				+ formaInclusao + ", codPublicacao=" + codPublicacao 
				+ ", campoObscuro=" + campoObscuro 
				+ ", nomeComercial=" + nomeComercial + "]";
	}	
}
