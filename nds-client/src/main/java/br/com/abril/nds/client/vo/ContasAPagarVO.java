package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.dto.ContasAPAgarConsultaProdutoDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;

public class ContasAPagarVO implements Serializable{

	private static final long serialVersionUID = 164081271878814255L;

		@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
		private String codigo;
		
		@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
		private String produto;
		
		@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
		private String edicao;
		
		@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
		private BigDecimal precoCapa;

		@Export(label = "Fornecedor", alignment=Alignment.CENTER, exhibitionOrder = 5)
		private String fornecedor;

		@Export(label = "Editor", alignment=Alignment.CENTER, exhibitionOrder = 6)
		private String editor;
		
		
		


		public ContasAPagarVO(ContasAPAgarConsultaProdutoDTO dto) {
			this.codigo=dto.getCodigo();
			this.editor=dto.getEditor();
			this.fornecedor=dto.getFornecedor();
			this.precoCapa=dto.getPrecoCapa();
			this.produto=dto.getProduto();
			
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

		public BigDecimal getPrecoCapa() {
			return precoCapa;
		}

		public void setPrecoCapa(BigDecimal precoCapa) {
			this.precoCapa = precoCapa;
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
		
		


}
