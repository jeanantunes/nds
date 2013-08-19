package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;

public class ContasAPagarConsultaProdutoVO implements Serializable{

	private static final long serialVersionUID = 164081271878814255L;

		@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
		private String codigo;
		
		@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
		private String produto;
		
		@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
		private String edicao;
		
		@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
		private String precoCapa;

		@Export(label = "Fornecedor", alignment=Alignment.CENTER, exhibitionOrder = 5)
		private String fornecedor;

		@Export(label = "Editor", alignment=Alignment.CENTER, exhibitionOrder = 6)
		private String editor;
		
		private String produtoEdicaoID;
		
		
		


		public ContasAPagarConsultaProdutoVO(ContasAPagarConsultaProdutoDTO dto) {
			this.codigo=dto.getCodigo();
			this.editor=dto.getEditor();
			this.fornecedor=dto.getFornecedor();
			this.precoCapa = CurrencyUtil.formatarValor(dto.getPrecoCapa());
			this.produto=dto.getProduto();
			this.edicao=dto.getEdicao().toString();
			this.produtoEdicaoID=dto.getProdutoEdicaoID().toString();
			
			
		}

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getProduto() {
			return produto;
		}

		public void setProduto(String produto) {
			this.produto = produto;
		}

		public String getEdicao() {
			return edicao;
		}

		public void setEdicao(String edicao) {
			this.edicao = edicao;
		}

		public String getFornecedor() {
			return fornecedor;
		}

		public void setFornecedor(String fornecedor) {
			this.fornecedor = fornecedor;
		}

		public String getEditor() {
			return editor;
		}

		public void setEditor(String editor) {
			this.editor = editor;
		}

		public String getProdutoEdicaoID() {
			return produtoEdicaoID;
		}

		public void setProdutoEdicaoID(String produtoEdicaoID) {
			this.produtoEdicaoID = produtoEdicaoID;
		}

		public void setPrecoCapa(String precoCapa) {
			this.precoCapa = precoCapa;
		}

		public String getPrecoCapa() {
			return precoCapa;
		}
		
		
		


}
