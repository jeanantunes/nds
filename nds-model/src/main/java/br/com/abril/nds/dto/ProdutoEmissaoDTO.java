package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ProdutoEmissaoDTO implements Serializable{

	private static final long serialVersionUID = -6994377052719897181L;
	
	private Long idProdutoEdicao;
	
	private Integer sequencia;
	
	private String codigoBarras;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private String notaEnvio;
	
	private Long numeroNotaEnvio;
	
	private String descricaoNotaEnvio;
	
	private Long edicao;
	
	private String desconto;
	
	private TipoLancamentoParcial tipoRecolhimento;
	
	private String dataLancamento;
	
	private String precoComDesconto;
	
	private BigInteger reparte;
	
	private BigInteger quantidadeDevolvida;
	
	private Boolean confereciaRealizada;
	
	private BigDecimal precoVenda;
	
	private BigDecimal vlrPrecoComDesconto;
	
	private BigDecimal vlrDesconto;
	
	private BigDecimal valorUnitarioProduto;
	
	private BigDecimal valorTotalProduto;
	
	private BigInteger vendido;
	
	private String vlrVendido;
	
	private Integer quantidadeReparte;
	
	private Integer quantidadeDev;

	private Long idCota;
	
	private Date dataRecolhimento;
	
	/**
	 * A flag abaixo indica se a instância
	 * em questão é um objeto duplicado para
	 * conter informações da nota de envio.
	 * Flag utilizada na lógica do relatório 
	 * de Emissao CE modelo 2.
	 */
	private boolean produtoDuplicadoDetalheNota;
	
	private boolean apresentaQuantidadeEncalhe;
	
	public String getVlrVendido() {
		return vlrVendido;
	}
	
	public void setVlrVendido(String vlrVendido) {
		this.vlrVendido = vlrVendido;
	}
	
	public BigInteger getVendido() {
		return vendido;
	}
	
	public void setVendido(BigInteger vendido) {
		this.vendido = vendido;
	}
	
	/**
	 * @return the seqStringuencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}
	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}
	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	/**
	 * @return the edicao
	 */
	public Long getEdicao() {
		return edicao;
	}
	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	/**
	 * @return the desconto
	 */
	public String getDesconto() {
		return desconto;
	}
	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		
		if (desconto != null){
		
			vlrDesconto = desconto;
			
			this.desconto = CurrencyUtil.formatarValor(desconto);
			
		} else {
			
			vlrDesconto = BigDecimal.ZERO;
			
			this.desconto = "0.00";
			
		}
	}
	/**
	 * @return the tipoRecolhimento
	 */
	public TipoLancamentoParcial getTipoRecolhimento() {
		return tipoRecolhimento;
	}
	
	public String getInicialTipoRecolhimento() {
		
		return ((this.tipoRecolhimento != null) ? (tipoRecolhimento.getDescricao().indexOf("P") > -1 ? "P": "F") : "F");
	}
	
	/**
	 * @param tipoRecolhimento the tipoRecolhimento to set
	 */
	public void setTipoRecolhimento(TipoLancamentoParcial tipoRecolhimento) {
		
		this.tipoRecolhimento = tipoRecolhimento;
		//this.tipoRecolhimento = tipoRecolhimento ? "P":"F";
	}
	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}
	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarData(dataLancamento,"dd/MM/yy");
	}
	/**
	 * @return the precoComDesconto
	 */
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}
	
	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		
		if (precoComDesconto != null){
			
			this.vlrPrecoComDesconto= precoComDesconto; 
			
			this.precoComDesconto = CurrencyUtil.formatarValorQuatroCasas(precoComDesconto);
		
		} else {
			
			this.vlrPrecoComDesconto= BigDecimal.ZERO;
			
			this.precoComDesconto = "0.00";
		
		}
		
		
	}
	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	/**
	 * @return the quantidadeDevolvida
	 */
	public BigInteger getQuantidadeDevolvida() {
		return quantidadeDevolvida;
	}
	/**
	 * @param quantidadeDevolvida the quantidadeDevolvida to set
	 */
	public void setQuantidadeDevolvida(Number quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida!=null? BigInteger.valueOf(quantidadeDevolvida.intValue()) : BigInteger.ZERO;
	}
	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}
	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	/**
	 * @return the precoVenda
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		
		this.precoVenda = (precoVenda == null)? BigDecimal.ZERO : precoVenda;
	
	}
	
	/**
	 * @return the vlrPrecoComDesconto
	 */
	public BigDecimal getVlrPrecoComDesconto() {
		return vlrPrecoComDesconto;
	}
	
	/**
	 * @param vlrPrecoComDesconto the vlrPrecoComDesconto to set
	 */
	public void setVlrPrecoComDesconto(BigDecimal vlrPrecoComDesconto) {
		this.vlrPrecoComDesconto = vlrPrecoComDesconto;
	}
	
	public BigDecimal getVlrDesconto() {
		return vlrDesconto;
	}
	
	public void setVlrDesconto(BigDecimal vlrDesconto) {
		this.vlrDesconto = vlrDesconto;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public String getNotaEnvio() {
		return notaEnvio;
	}

	public void setNotaEnvio(String notaEnvio) {
		this.notaEnvio = notaEnvio;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public boolean isApresentaQuantidadeEncalhe() {
		return apresentaQuantidadeEncalhe;
	}

	public void setApresentaQuantidadeEncalhe(boolean apresentaQuantidadeEncalhe) {
		this.apresentaQuantidadeEncalhe = apresentaQuantidadeEncalhe;
	}

	public String getDescricaoNotaEnvio() {
		return descricaoNotaEnvio;
	}

	public void setDescricaoNotaEnvio(String descricaoNotaEnvio) {
		this.descricaoNotaEnvio = descricaoNotaEnvio;
	}

	public boolean isProdutoDuplicadoDetalheNota() {
		return produtoDuplicadoDetalheNota;
	}

	public void setProdutoDuplicadoDetalheNota(boolean produtoDuplicadoDetalheNota) {
		this.produtoDuplicadoDetalheNota = produtoDuplicadoDetalheNota;
	}
	
	public Boolean getConfereciaRealizada() {
		return confereciaRealizada;
	}

	public void setConfereciaRealizada(Boolean confereciaRealizada) {
		this.confereciaRealizada = confereciaRealizada;
	}

	public Integer getQuantidadeReparte() {
		return quantidadeReparte;
	}

	public void setQuantidadeReparte(Integer quantidadeReparte) {
		this.quantidadeReparte = quantidadeReparte;
	}

	public BigDecimal getValorUnitarioProduto() {
		return valorUnitarioProduto;
	}

	public void setValorUnitarioProduto(BigDecimal valorUnitarioProduto) {
		this.valorUnitarioProduto = valorUnitarioProduto;
	}

	public BigDecimal getValorTotalProduto() {
		return valorTotalProduto;
	}

	public void setValorTotalProduto(BigDecimal valorTotalProduto) {
		this.valorTotalProduto = valorTotalProduto;
	}
	
	public Long getIdCota() {
        return idCota;
    }
    
    public void setIdCota(Long idCota) {
        this.idCota = idCota;
    }

    public Date getDataRecolhimento() {
        return dataRecolhimento;
    }

    public void setDataRecolhimento(Date dataRecolhimento) {
        this.dataRecolhimento = dataRecolhimento;
    }

	public Integer getQuantidadeDev() {
		return quantidadeDev;
	}

	public void setQuantidadeDev(Integer quantidadeDev) {
		this.quantidadeDev = quantidadeDev!=null? quantidadeDev : 0;
	}
	
}