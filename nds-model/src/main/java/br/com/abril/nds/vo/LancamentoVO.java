package br.com.abril.nds.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class LancamentoVO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;

	
	private Long id;
	@Export(label="Codigo")
	private String codigoProduto;
	@Export(label="Produto")
	private String nomeProduto;
	@Export(label="Edição")
	private Long numEdicao;
	@Export(label="Preço Capa R$")
	private String preco;
	@Export(label="Pcte Padrão")
	private int pacotePadrao;
	@Export(label="Reparte")
	private String reparte;
	@Export(label="Lançamento")
	private String lancamento;
	@Export(label="Recolhimento")
	private String dataRecolhimento;
	private Long idFornecedor;
	@Export(label="Fornecedor")
	private String nomeFornecedor;
	@Export(label="Previsto")
	private String dataPrevisto;
	@Export(label="Matriz/Distrib")
	private String novaData;
	@Export(label="TotalR$")
	private String total;
	private String dataLancamentoDistribuidor;
	private Integer fisico;
	private String qtdeEstudo;
	private String qtdeReprogramacoes;
	private String distribuicao;
	private boolean possuiRecebimentoFisico;
	private boolean cancelamentoGD;
	private boolean estudoFechado;
	private boolean reprogramacoesExcedidas;
	private boolean bloquearData;
	
	
	
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
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public Long getNumEdicao() {
		return numEdicao;
	}
	
	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao;
	}
	
	public String getPreco() {
		return preco;
	}
	
	public void setPreco(String preco) {
		this.preco = preco;
	}
	
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	public String getReparte() {
		return reparte;
	}
	
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	
	public String getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}
	
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
	public String getDataPrevisto() {
		return dataPrevisto;
	}
	
	public void setDataPrevisto(String dataPrevisto) {
		this.dataPrevisto = dataPrevisto;
	}
	
	public String getNovaData() {
		return novaData;
	}
	
	public void setNovaData(String novaData) {
		this.novaData = novaData;
	}
	
	public String getTotal() {
		return total;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public Integer getFisico() {
		return fisico;
	}
	
	public void setFisico(Integer fisico) {
		this.fisico = fisico;
	}
	
	public String getQtdeEstudo() {
		return qtdeEstudo;
	}
	
	public void setQtdeEstudo(String qtdeEstudo) {
		this.qtdeEstudo = qtdeEstudo;
	}
		
	public boolean isCancelamentoGD() {
		return cancelamentoGD;
	}
	
	public void setCancelamentoGD(boolean cancelamentoGD) {
		this.cancelamentoGD = cancelamentoGD;
	}
		
	public boolean isEstudoFechado() {
		return estudoFechado;
	}
	
	public void setEstudoFechado(boolean estudoFechado) {
		this.estudoFechado = estudoFechado;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LancamentoVO other = (LancamentoVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getQtdeReprogramacoes() {
		return qtdeReprogramacoes;
	}

	public void setQtdeReprogramacoes(String qtdeReprogramacoes) {
		this.qtdeReprogramacoes = qtdeReprogramacoes;
	}

	public boolean isReprogramacoesExcedidas() {
		return reprogramacoesExcedidas;
	}

	public void setReprogramacoesExcedidas(boolean reprogramacoesExcedidas) {
		this.reprogramacoesExcedidas = reprogramacoesExcedidas;
	}

	public String getDistribuicao() {
		return distribuicao;
	}

	public void setDistribuicao(String distribuicao) {
		this.distribuicao = distribuicao;
	}

	public boolean isPossuiRecebimentoFisico() {
		return possuiRecebimentoFisico;
	}

	public void setPossuiRecebimentoFisico(boolean possuiRecebimentoFisico) {
		this.possuiRecebimentoFisico = possuiRecebimentoFisico;
	}

	public String getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}

	public void setDataLancamentoDistribuidor(String dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}

	public boolean isBloquearData() {
		return bloquearData;
	}

	public void setBloquearData(boolean bloquearData) {
		this.bloquearData = bloquearData;
	}

}
