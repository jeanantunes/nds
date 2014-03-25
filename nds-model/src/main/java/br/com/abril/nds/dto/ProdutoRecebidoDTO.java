package br.com.abril.nds.dto;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * @author - InfoA2 - Samuel Mendes
 * 
 *         <h1>DTO será utilizado para exibição do grid (Produtos Recebidos)
 *         dentro do sub menu Excecao Segmento Parciais</h1>
 * 
 */
@Exportable
public class ProdutoRecebidoDTO extends UsuarioLogDTO {

	private static final long serialVersionUID = 3943113104970461137L;

	private Long idExcecaoProdutoCota;
	
	@Export(label="Código", alignment=Alignment.LEFT,exhibitionOrder=1)
	private String codigoProduto;
	
	@Export(label="Produto", alignment=Alignment.LEFT,exhibitionOrder=2)
	private String nomeProduto;
	
	private String classificacaoProduto;

	@Export(label="Usuário", alignment=Alignment.LEFT,exhibitionOrder=3)
	@Override
	public String getNomeUsuario() {
		return super.getNomeUsuario();
	}

	private String codigoProdin;
	
	@Export(label="Data", alignment=Alignment.LEFT,exhibitionOrder=4)
	@Override
	public String getDataAlteracaoFormatada() {
		// TODO Auto-generated method stub
		return super.getDataAlteracaoFormatada();
	}
	
	@Export(label="Hora", alignment=Alignment.LEFT,exhibitionOrder=5)
	@Override
	public String getHoraAlteracaoFormatada() {
		// TODO Auto-generated method stub
		return super.getHoraAlteracaoFormatada();
	}
	
	public Long getIdExcecaoProdutoCota() {
		return idExcecaoProdutoCota;
	}

	public void setIdExcecaoProdutoCota(Long idExcessaoProdutoCota) {
		this.idExcecaoProdutoCota = idExcessaoProdutoCota;
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

	public String getCodigoProdin() {
		return codigoProdin;
	}

	public void setCodigoProdin(String codigoProdin) {
		this.codigoProdin = codigoProdin;
	}

	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}

	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}

	
	
}
