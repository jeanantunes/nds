package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.util.ComponentesPDV;

public class FiltroHistoricoVendaDTO extends FiltroDTO {

	private static final long serialVersionUID = 6967582317485942551L;

	private ProdutoDTO produtoDto;
	private Long tipoClassificacaoProdutoId;
	private Long numeroEdicao;

	// Filtro Pesquisa por Histórico venda
	private boolean cotasAtivas;
	private BigInteger qtdReparteInicial;
	private BigInteger qtdReparteFinal;
	private BigInteger qtdVendaInicial;
	private BigInteger qtdVendaFinal;
	private BigDecimal percentualVenda;
	private CotaDTO cotaDto;
	private ComponentesPDV componentesPdv;
	private List<ProdutoEdicaoDTO> listProdutoEdicaoDTO = new ArrayList<>();
	private String elemento;
	
	private boolean buscarPeriodosParciais;
	
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		CODIGO("codigoProduto"),
		CLASSIFICACAO("tipoClassificacaoFormatado"),
		EDICAO("numeroEdicao"),		
		PERIODO("codPeriodoProd"),		
		DT_LANCAMENTO("dataLancamentoFormatada"),
		REPARTE("repartePrevisto"),
		VENDA("qtdVendasFormatada"),
		STATUS("situacaoLancamento");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
			return this.descricao;
		}
	}
	
	
	// utilizado para retorno de mensagem
	private String validationMsg;
	
	/**
	 * Validação de dados
	 * 
	 */
	
	public boolean validarEntradaFiltroProduto() {
		boolean isValid = true;
		
		if((produtoDto.getCodigoProduto() == null || produtoDto.getCodigoProduto().trim().isEmpty()) && 
				(produtoDto.getNomeProduto() == null || produtoDto.getNomeProduto().trim().isEmpty())) {
			validationMsg = "Código ou nome do produto é obrigatório.";
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean validarPorQtdReparte() {
		boolean isValid = true;
		if (qtdReparteInicial == null || qtdReparteInicial.intValue() < 0 ) {
			validationMsg = "Informe a quantidade de reparte inicial.";
			isValid = false;
		}
		
		else if (qtdReparteFinal == null || qtdReparteFinal.intValue() < 0 ) {
			validationMsg = "Informe a quantidade de reparte final.";
			isValid = false;
		}
		
		else if (qtdReparteFinal.intValue() < qtdReparteInicial.intValue()) {
			validationMsg = "A quantidade de reparte final não pode ser inferior a quantidade inicial.";
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean validarPorComponentes() {
		boolean isValid = true;
		if (componentesPdv == null) {
			validationMsg = "Nenhum componente foi selecionado.";			
			isValid = false;
		}
		return isValid;	
	}
	
	public boolean validarPorCota(){
		boolean isValid = true;
		if((cotaDto.getNumeroCota() == null || cotaDto.getNumeroCota() == 0) ||
				(cotaDto.getNomePessoa() == null || cotaDto.getNomePessoa().trim().isEmpty())){
			validationMsg = "Código ou nome da cota é obrigatório.";
			isValid = false;
		}
		return isValid;
	}
	
	public boolean validarListaProduto(){
		boolean isValid = true;
		
		if (listProdutoEdicaoDTO == null || listProdutoEdicaoDTO.size() == 0) {
			validationMsg = "Nenhum produto foi selecionado.";
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean validarPorQtdVenda() {
		boolean isValid = true;
		
		if (qtdVendaInicial == null || qtdVendaInicial.intValue() < 0 ) {
			validationMsg = "Informe a quantidade de venda inicial.";
			isValid = false;
		}
		else if (qtdVendaFinal == null || qtdVendaFinal.intValue() < 0 ) {
			validationMsg = "Informe a quantidade de venda final.";
			isValid = false;
		}
		else if (qtdVendaFinal.intValue() < qtdVendaInicial.intValue()) {
			validationMsg = "A quantidade de venda final não pode ser inferior a quantidade inicial.";
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean validarPorPercentualVenda(){
		boolean isValid = true;
		
		if (percentualVenda == null || percentualVenda.doubleValue() == 0) {
			validationMsg = "Informe um percentual de vendas.";
			isValid = false;
		}
		return isValid;
	}
	
	public ProdutoDTO getProdutoDto() {
		return produtoDto;
	}

	public void setProdutoDto(ProdutoDTO produtoDto) {
		this.produtoDto = produtoDto;
	}

	public Long getTipoClassificacaoProdutoId() {
		return tipoClassificacaoProdutoId;
	}

	public void setTipoClassificacaoProdutoId(Long tipoClassificacaoProdutoId) {
		this.tipoClassificacaoProdutoId = tipoClassificacaoProdutoId;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public boolean isCotasAtivas() {
		return cotasAtivas;
	}

	public void setCotasAtivas(boolean cotasAtivas) {
		this.cotasAtivas = cotasAtivas;
	}

	public BigInteger getQtdReparteInicial() {
		return qtdReparteInicial;
	}

	public void setQtdReparteInicial(BigInteger qtdReparteInicial) {
		this.qtdReparteInicial = qtdReparteInicial;
	}

	public BigInteger getQtdReparteFinal() {
		return qtdReparteFinal;
	}

	public void setQtdReparteFinal(BigInteger qtdReparteFinal) {
		this.qtdReparteFinal = qtdReparteFinal;
	}

	public BigInteger getQtdVendaInicial() {
		return qtdVendaInicial;
	}

	public void setQtdVendaInicial(BigInteger qtdVendaInicial) {
		this.qtdVendaInicial = qtdVendaInicial;
	}

	public BigInteger getQtdVendaFinal() {
		return qtdVendaFinal;
	}

	public void setQtdVendaFinal(BigInteger qtdVendaFinal) {
		this.qtdVendaFinal = qtdVendaFinal;
	}

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(BigDecimal percentualVenda) {
		this.percentualVenda = percentualVenda;
	}

	public CotaDTO getCotaDto() {
		return cotaDto;
	}

	public void setCotaDto(CotaDTO cotaDto) {
		this.cotaDto = cotaDto;
	}

	public ComponentesPDV getComponentesPdv() {
		return componentesPdv;
	}

	public void setComponentesPdv(ComponentesPDV componentesPdf) {
		this.componentesPdv = componentesPdf;
	}

	public List<ProdutoEdicaoDTO> getListProdutoEdicaoDTO() {
		return listProdutoEdicaoDTO;
	}

	public void setListProdutoEdicaoDTO(List<ProdutoEdicaoDTO> listProdutoEdicaoDTO) {
		this.listProdutoEdicaoDTO = listProdutoEdicaoDTO;
	}

	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}

	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}

	public boolean isBuscarPeriodosParciais() {
		return buscarPeriodosParciais;
	}

	public void setBuscarPeriodosParciais(boolean buscarPeriodosParciais) {
		this.buscarPeriodosParciais = buscarPeriodosParciais;
	}

}
