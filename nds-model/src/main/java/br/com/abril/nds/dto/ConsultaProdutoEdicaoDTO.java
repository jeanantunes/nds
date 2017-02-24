package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.dto.ProdutoEdicaoDTO.ModoTela;
import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TemaProduto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.upload.XlsMapper;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaProdutoEdicaoDTO implements Serializable {
	

	private static final long serialVersionUID = -8072285478313345444L;

	private Long id;
	
	@Export(label="Código", alignment=Alignment.CENTER, widthPercent=6)
	private String codigoProduto;
	
	@Export(label="Nome Comercial", alignment=Alignment.CENTER, widthPercent=12)
	private String nomeComercial;
	
	@Export(label="Edição", alignment=Alignment.CENTER, widthPercent=5)
	private Long numeroEdicao;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT, widthPercent=11)
	private String nomeFornecedor;
	
	@Export(label="Parcial", alignment=Alignment.LEFT, widthPercent=4)
	private Boolean parcial;
	
	@Export(label="Tipo Lançamento", alignment=Alignment.CENTER, widthPercent=5)
	private String lancamento;
	
	@Export(label="Lançamento", alignment=Alignment.CENTER, widthPercent=6)
	private String statusLancamento;
	
	@Export(label="Preço Capa", alignment=Alignment.CENTER, widthPercent=5)
	private BigDecimal precoVenda;
	
	@Export(label="Desconto", alignment=Alignment.CENTER, widthPercent=6)
	private BigDecimal desconto;
	
	@Export(label="Data Lancamento", alignment=Alignment.CENTER, widthPercent=8)
	private Date dataLancamento;
	
	@Export(label="Data Recolhimento", alignment=Alignment.CENTER, widthPercent=8)
	private Date dataRecolhimentoReal;
	
	@Export(label="Situação", alignment=Alignment.CENTER, widthPercent=9)
	private String statusSituacao;
	
	@Export(label="Brinde", alignment=Alignment.CENTER, widthPercent=5)
	private Boolean temBrinde;
	
	@Export(label="Segmento", alignment=Alignment.CENTER, widthPercent=10)
	private String segmento;
	
	
	/**
	 * Construtor Padrão
	 */
	public ConsultaProdutoEdicaoDTO() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCodigoProduto() {
		return codigoProduto;
	}


	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}


	public String getNomeComercial() {
		return nomeComercial;
	}


	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}


	public Long getNumeroEdicao() {
		return numeroEdicao;
	}


	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}


	public String getNomeFornecedor() {
		return nomeFornecedor;
	}


	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}


	public Boolean getParcial() {
		return parcial;
	}


	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
	}


	public String getLancamento() {
		return lancamento;
	}


	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}


	public String getStatusLancamento() {
		return statusLancamento;
	}


	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}

	public String getStatusSituacao() {
		return statusSituacao;
	}


	public void setStatusSituacao(String statusSituacao) {
		this.statusSituacao = statusSituacao;
	}

	public Boolean getTemBrinde() {
		return temBrinde;
	}


	public void setTemBrinde(Boolean temBrinde) {
		this.temBrinde = temBrinde;
	}
	
	public String getSegmento() {
		return segmento;
	}


	public void setSegmento(String segmento) {
		this.segmento = segmento;
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


	public Date getDataLancamento() {
		return dataLancamento;
	}


	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}


	public Date getDataRecolhimentoReal() {
		return dataRecolhimentoReal;
	}


	public void setDataRecolhimentoReal(Date dataRecolhimentoReal) {
		this.dataRecolhimentoReal = dataRecolhimentoReal;
	}

	
}