package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ParcialDTO implements Serializable{
	
	private static final long serialVersionUID = 5323481858856643957L;
	
	private Long idProdutoEdicao;
	
	private Long idLancamentoParcial;
	
	@Export(label = "Data Lancto", alignment=Alignment.CENTER)
	private String dataLancamento;
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER)
	private String dataRecolhimento;
	@Export(label = "Código", alignment=Alignment.LEFT)
	private String codigoProduto;
	@Export(label = "Produto", alignment=Alignment.LEFT, widthPercent=30)
	private String nomeProduto;
	@Export(label = "Edição", alignment=Alignment.LEFT)
	private Integer numEdicao;
	@Export(label = "Fornecedor", alignment=Alignment.LEFT)
	private String nomeFornecedor;
	@Export(label = "Status", alignment=Alignment.LEFT)
	private String statusParcial;
	
	private Origem origem;
	
	private boolean geradoPorInterface;
	
	private String precoCapa;
	
	public ParcialDTO() {}
		
	
	public ParcialDTO(String dataLancamento, String dataRecolhimento,
			String codigoProduto, String nomeProduto, Integer numEdicao,
			String nomeFornecedor, String statusParcial) {
		super();
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numEdicao = numEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.statusParcial = statusParcial;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
	}
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
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
	public Integer getNumEdicao() {
		return numEdicao;
	}
	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao.intValue();
	}
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public String getStatusParcial() {
		return statusParcial;
	}
	public void setStatusParcial(StatusLancamentoParcial statusParcial) {
		this.statusParcial = statusParcial.toString();
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
	 * @return the geradoPorInterface
	 */
	public boolean isGeradoPorInterface() {
		return geradoPorInterface;
	}


	/**
	 * @param geradoPorInterface the geradoPorInterface to set
	 */
	public void setGeradoPorInterface(boolean geradoPorInterface) {
		this.geradoPorInterface = geradoPorInterface;
	}


	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}


	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		
		this.origem = origem;
		
		if(origem!=null && Origem.INTERFACE.equals(origem)) {
			this.geradoPorInterface = true;
		} else {
			this.geradoPorInterface = false;
		}
		
	}


	public Long getIdLancamentoParcial() {
		return idLancamentoParcial;
	}


	public void setIdLancamentoParcial(Long idLancamentoParcial) {
		this.idLancamentoParcial = idLancamentoParcial;
	}
	
	
	
}
