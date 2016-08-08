package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ProdutoDistribuicaoVO  implements Serializable, Comparable<ProdutoDistribuicaoVO> {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private BigInteger idLancamento;
	
	@Export(label="Codigo", exhibitionOrder = 0)
	private String codigoProduto;
	
	@Export(label="Produto", exhibitionOrder = 1)
	private String nomeProduto;
	
	@Export(label="Edição", exhibitionOrder = 2)
	private BigInteger numeroEdicao;

	@Export(label="Periodo", exhibitionOrder = 3)
	private String periodo;
	
	@Export(label="Preço RS", exhibitionOrder = 4)
	private String precoVenda;

	@Export(label="Clas.", exhibitionOrder = 5)
	private String classificacao;
	
	@Export(label="Pct. Padrão", exhibitionOrder = 6)
	private Integer pctPadrao;
	
	@Export(label="Fornecedor", exhibitionOrder = 7)
	private String nomeFornecedor;
	
	@Export(label="Juram.", exhibitionOrder = 8)
	private BigDecimal juram;
	
	@Export(label="Suplem..", exhibitionOrder = 9)
	private BigDecimal suplem;
	
	@Export(label="Lancto.", exhibitionOrder = 10)
	private BigDecimal lancto;
	
	@Export(label="Promo.", exhibitionOrder = 11)
	private BigDecimal promo;
	
	@Export(label="Lib.", exhibitionOrder = 12)
	private String liberado;

	@Export(label="Estudo", exhibitionOrder = 13)
	private BigInteger idEstudo;
	
	private BigInteger idProdutoEdicao;
	
	private String dataLancto;
	
	private BigDecimal reparte;
    private BigDecimal estoque;
	
	private BigInteger repDistrib;
	
	private Date dataFinMatDistrib;
	
	private Long idUsuario;
	
	private Integer idCopia;
	
	private Date dataLancamentoEstudo;
	
	private String dataLancamentoEstudoFormatado;
	
	//Usado para controlar cor sim cor não entre linhas originais e copias
	//Na matriz de distribuição
	private Integer idRow;

	private Boolean estudoLiberado;
	
	private BigInteger qtdeReparteEstudo;
	
	private List<ProdutoDistribuicaoVO> produtoDistribuicoesDuplicados =  new ArrayList<ProdutoDistribuicaoVO>();
	
	private Boolean geracaoAutomatica;
	
	private BigInteger idSegmento;
	
	private String modoAnaliseDefault;
	
	public void addItemDuplicado(ProdutoDistribuicaoVO produtoDistribuicaoVO, Integer row) {
		
		produtoDistribuicoesDuplicados.add(produtoDistribuicaoVO);
		
		int index = 0; 
		
		for (ProdutoDistribuicaoVO pdVO: produtoDistribuicoesDuplicados) {
			pdVO.setIdCopia(index++);
			pdVO.setIdRow(row);
		}
	}
	
	public BigInteger getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(BigInteger idLancamento) {
		this.idLancamento = idLancamento;
	}

	public String getLiberado() {
		return liberado;
	}

	public void setLiberado(String liberado) {
		this.liberado = liberado;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
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

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Object periodo) {
		if(periodo == null){
			this.periodo = "";
		}else{
			this.periodo = periodo.toString();			
		}
	}

	public String getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
	    this.precoVenda = CurrencyUtil.formatarValor(precoVenda);
	}

	public Integer getPctPadrao() {
		return pctPadrao;
	}

	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}

	public BigDecimal getJuram() {
		return juram;
	}

	public void setJuram(BigDecimal juram) {
		this.juram = juram;
	}

	public BigDecimal getSuplem() {
		return suplem;
	}

	public void setSuplem(BigDecimal suplem) {
		this.suplem = suplem;
	}

	public BigDecimal getLancto() {
		return lancto;
	}

	public void setLancto(BigDecimal lancto) {
		this.lancto = lancto;
	}

	public BigDecimal getPromo() {
		return promo;
	}

	public void setPromo(BigDecimal promo) {
		this.promo = promo;
	}

	public BigInteger getIdEstudo() {
		return idEstudo;
	}

	public void setIdEstudo(BigInteger idEstudo) {
		this.idEstudo = idEstudo;
	}
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
	public Date getDataLanctoSemFormatacao() {
	    try {
		return new SimpleDateFormat("dd/MM/yyyy").parse(dataLancto);
	    } catch (Exception ex) {}
	    return null;
	}
	
	public void setDataLanctoSemFormatacao(Date dataLancto) {
		this.dataLancto = new SimpleDateFormat("dd/MM/yyyy").format(dataLancto);
	}
	
	public String getDataLancto() {
		return dataLancto;
	}

	public void setDataLancto(String dataLancto) {
		this.dataLancto = dataLancto;
	}
	
	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
		this.estoque = reparte;
	}
	
	public boolean isItemFinalizado() {
		
		return (this.getDataFinMatDistrib() != null);
	}

	public Date getDataFinMatDistrib() {
		return dataFinMatDistrib;
	}

	public void setDataFinMatDistrib(Date dataFinMatDistrib) {
		this.dataFinMatDistrib = dataFinMatDistrib;
	}

	public BigInteger getRepDistrib() {
		return repDistrib;
	}

	public void setRepDistrib(BigInteger repDistrib) {
		this.repDistrib = repDistrib;
	}
	
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	

	public BigInteger getIdProdutoEdicao() {
	    return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(BigInteger idProdutoEdicao) {
	    this.idProdutoEdicao = idProdutoEdicao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLancamento == null) ? 0 : idLancamento.hashCode());
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
		ProdutoDistribuicaoVO other = (ProdutoDistribuicaoVO) obj;
		
		boolean equal = (((this.getIdLancamento() == null && other.getIdLancamento() == null) ||
                (this.getIdLancamento() != null && this.getIdLancamento().equals(other.getIdLancamento()))) &&
		 this.getCodigoProduto().equals(other.getCodigoProduto()) &&
		 this.getNumeroEdicao().equals(other.getNumeroEdicao())) &&
		 ((this.getIdCopia() == null && other.getIdCopia() == null) || 
		  (this.getIdCopia() != null && this.getIdCopia().equals(other.getIdCopia())));

	   return equal;
	}

	
	private Long getComparatorHash(ProdutoDistribuicaoVO prodDistribVO) {
		
		long hash = 0l;
		
		hash += (prodDistribVO.getNomeProduto().length() * 100);
		
		if (prodDistribVO.getIdEstudo() != null) {
			
			hash += (prodDistribVO.getIdEstudo().longValue() * 1000);
			
			if (!StringUtils.isEmpty(prodDistribVO.getLiberado())) {
				
				hash *= 10000l;
			}
		}
		
		return hash;
	}
	
	@Override
	public int compareTo(ProdutoDistribuicaoVO prodDistribVO) {
		
		return getComparatorHash(this).compareTo(getComparatorHash(prodDistribVO));
	}

	public Integer getIdCopia() {
		return idCopia;
	}

	public void setIdCopia(Integer idCopia) {
		this.idCopia = idCopia;
	}

	public Integer getIdRow() {
		return idRow;
	}

	public void setIdRow(Integer idRow) {
		this.idRow = idRow;
	}

	public Boolean getEstudoLiberado() {
		return estudoLiberado;
	}

	public void setEstudoLiberado(Boolean estudoLiberado) {
		this.estudoLiberado = estudoLiberado;
	}

	public BigInteger getQtdeReparteEstudo() {
		return qtdeReparteEstudo;
	}

	public void setQtdeReparteEstudo(BigInteger qtdeReparteEstudo) {
		this.qtdeReparteEstudo = qtdeReparteEstudo;
	}

	public List<ProdutoDistribuicaoVO> getProdutoDistribuicoesDuplicados() {
		return produtoDistribuicoesDuplicados;
	}


	public void setProdutoDistribuicoesDuplicados(
			List<ProdutoDistribuicaoVO> produtoDistribuicoesDuplicados) {
		this.produtoDistribuicoesDuplicados = produtoDistribuicoesDuplicados;
	}
	
	public Date getDataLancamentoEstudo() {
		return dataLancamentoEstudo;
	}

	public void setDataLancamentoEstudo(Date dataLancamentoEstudo) {
		this.dataLancamentoEstudo = dataLancamentoEstudo;
		if(dataLancamentoEstudo!=null)
			this.dataLancamentoEstudoFormatado = new SimpleDateFormat("dd/MM/yyyy").format(dataLancamentoEstudo);
	}

	public String getDataLancamentoEstudoFormatado() {
		return dataLancamentoEstudoFormatado;
	}

	public void setDataLancamentoEstudoFormatado(String dataLancamentoEstudoFormatado) {
		this.dataLancamentoEstudoFormatado = dataLancamentoEstudoFormatado;
	}

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

	public Boolean getGeracaoAutomatica() {
		return geracaoAutomatica;
	}

	public void setGeracaoAutomatica(Boolean geracaoAutomatica) {
		this.geracaoAutomatica = geracaoAutomatica;
	}

	public BigInteger getIdSegmento() {
		return idSegmento;
	}

	public void setIdSegmento(BigInteger idSegmento) {
		this.idSegmento = idSegmento;
	}

	public String getModoAnaliseDefault() {
		return modoAnaliseDefault;
	}

	public void setModoAnaliseDefault(String modoAnaliseDefault) {
		this.modoAnaliseDefault = modoAnaliseDefault;
	}
	
}