package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.util.CurrencyUtil;

public class ProdutoAbastecimentoDTO implements Serializable{

	private static final long serialVersionUID = -2951289520494037916L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private Integer reparte;
	private String precoCapa;
	private String total;
	private String codigoRota;	
	private Integer codigoBox;
	private String nomeBox;
	private Long idProdutoEdicao;
	private Integer codigoCota;
	private String nomeCota;
	private Integer materialPromocional;
	private Integer sequenciaMatriz;
	private String totalBox;
	private Integer totalProduto;
	private String codigoBarra;
	private Integer pacotePadrao;
	private Integer qtdeExms;
	private Cota cota;
	private List<String> cotasSemRoteirizacao;
	private String descRoteiro;
	private Long idEntregador;
	private String nomeEntregador;
	private String descRota;
	
	public ProdutoAbastecimentoDTO() {
	
	}
	
	public ProdutoAbastecimentoDTO(String codigoProduto, String nomeProduto,
		Long numeroEdicao, Integer reparte, String precoCapa, String total) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.reparte = reparte;
		this.precoCapa = precoCapa;
		this.total = total;
	}

	/**
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param numeroEdicao
	 * @param reparte
	 * @param precoCapa
	 * @param codigoBox
	 * @param idProdutoEdicao
	 * @param materialPromocional
	 * @param sequenciaMatriz
	 * @param totalBox
	 * @param codigoBarra
	 */
	public ProdutoAbastecimentoDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, Integer reparte, String precoCapa,
			Integer codigoBox, Long idProdutoEdicao,
			Integer materialPromocional, Integer sequenciaMatriz,
			String totalBox, String codigoBarra) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.reparte = reparte;
		this.precoCapa = precoCapa;
		this.codigoBox = codigoBox;
		this.idProdutoEdicao = idProdutoEdicao;
		this.materialPromocional = materialPromocional;
		this.sequenciaMatriz = sequenciaMatriz;
		this.totalBox = totalBox;
		this.codigoBarra = codigoBarra;
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
	* @return the numeroEdicao
	*/
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	* @param numeroEdicao the numeroEdicao to set
	*/
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	/**
	* @return the reparte
	*/
	public Integer getReparte() {
		return reparte;
	}
	/**
	* @param reparte the reparte to set
	*/
	public void setReparte(BigInteger reparte) {
		if(reparte!=null)
		this.reparte = reparte.intValue();
	}
	/**
	* @return the precoCapa
	*/
	public String getPrecoCapa() {
		return precoCapa;
	}
	/**
	* @param precoCapa the precoCapa to set
	*/
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = CurrencyUtil.formatarValor(precoCapa);
	}
	/**
	* @return the total
	*/
	public String getTotal() {
		return total;
	}
	/**
	* @param total the total to set
	*/
	public void setTotal(BigDecimal total) {
		this.total = CurrencyUtil.formatarValor(total);
	}
	
	public Integer getCodigoBox() {
		return codigoBox;
	}
	
	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}
	
	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	public String getCodigoRota() {
		return codigoRota;
	}
	
	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}
	
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	/**
	* @return the materialPromocional
	*/
	public Integer getMaterialPromocional() {
		return materialPromocional;
	}
	
	/**
	* @param materialPromocional the materialPromocional to set
	*/
	public void setMaterialPromocional(BigInteger materialPromocional) {
		this.materialPromocional = materialPromocional == null ? 0 : materialPromocional.intValue();
	}
	
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}
	
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}
	
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	/**
	* @return the nomeCota
	*/
	public String getNomeCota() {
		return nomeCota;
	}
	
	/**
	* @param nomeCota the nomeCota to set
	*/
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
	/**
	* @return the totalBox
	*/
	public String getTotalBox() {
		return totalBox;
	}
	
	/**
	* @param totalBox the totalBox to set
	*/
	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = CurrencyUtil.formatarValor(totalBox);
	}
	
	/**
	* @return the totalProduto
	*/
	public Integer getTotalProduto() {
		return totalProduto;
	}
	
	/**
	* @param totalProduto the totalProduto to set
	*/
	public void setTotalProduto(Long totalProduto) {
		this.totalProduto = totalProduto.intValue();
	}
	
	/**
	* @return the codigoBarra
	*/
	public String getCodigoBarra() {
		return codigoBarra;
	}
	
	/**
	* @param codigoBarra the codigoBarra to set
	*/
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	
	/**
	* @return the pacotePadrao
	*/
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	
	/**
	* @param pacotePadrao the pacotePadrao to set
	*/
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	/**
	* @return the qtdeExms
	*/
	public Integer getQtdeExms() {
		return qtdeExms;
	}
	
	/**
	* @param qtdeExms the qtdeExms to set
	*/
	public void setQtdeExms(BigInteger qtdeExms) {
		if(qtdeExms!=null)
			this.qtdeExms = qtdeExms.intValue();
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		 this.cota = cota;
	}

	public List<String> getCotasSemRoteirizacao() {
		return cotasSemRoteirizacao;
	}

	public void setCotasSemRoteirizacao(List<String> cotasSemRoteirizacao) {
		this.cotasSemRoteirizacao = cotasSemRoteirizacao;
	}
    
    public String getDescRoteiro() {
        return descRoteiro;
    }
    
    public void setDescRoteiro(String descRoteiro) {
        this.descRoteiro = descRoteiro;
    }
    
    public Long getIdEntregador() {
        return idEntregador;
    }
    
    public void setIdEntregador(Long idEntregador) {
        this.idEntregador = idEntregador;
    }

    
    public String getNomeEntregador() {
        return nomeEntregador;
    }
    
    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    
    public String getDescRota() {
        return descRota;
    }

    
    public void setDescRota(String descRota) {
        this.descRota = descRota;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCota == null) ? 0 : codigoCota.hashCode());
		result = prime * result + ((idProdutoEdicao == null) ? 0 : idProdutoEdicao.hashCode());
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
		ProdutoAbastecimentoDTO other = (ProdutoAbastecimentoDTO) obj;
		if (codigoCota == null) {
			if (other.codigoCota != null)
				return false;
		} else if (!codigoCota.equals(other.codigoCota))
			return false;
		if (idProdutoEdicao == null) {
			if (other.idProdutoEdicao != null)
				return false;
		} else if (!idProdutoEdicao.equals(other.idProdutoEdicao))
			return false;
		return true;
	}
}