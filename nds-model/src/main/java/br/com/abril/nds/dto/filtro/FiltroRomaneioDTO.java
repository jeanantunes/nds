package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroRomaneioDTO implements Serializable {

	private static final long serialVersionUID = -3783996689743491442L;
	
	private Long idBox;
	private List<Long> idRoteiro;
	private Long idRota;
	private String nomeRota;
	private String nomeRoteiro;
	private String nomeBox;
	
	@Export(label = "Data", exhibitionOrder = 1)
	private Date data;
	private List<Long> produtos;
	
	@Export(label = "Produtos", exhibitionOrder = 5)
	private String nomesProduto;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoRomaneio ordenacaoColuna;
	
	private Boolean isImpressao = false;
	
	public enum ColunaOrdenacaoRomaneio {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoRomaneio(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoRomaneio getPorDescricao(String descricao) {
			for(ColunaOrdenacaoRomaneio coluna: ColunaOrdenacaoRomaneio.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}


	public Long getIdBox() {
		return idBox;
	}

	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	public List<Long> getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(List<Long> idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoRomaneio getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoRomaneio ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	@Export(label = "Roteiro" , exhibitionOrder = 3, alignment = Alignment.LEFT)
	public String getRoteiro() {
		
		if(nomeRoteiro != null){
			
			return nomeRoteiro;
		}else{
			
			return "Todos";
		}
		
	}
	
	@Export(label = "Box", exhibitionOrder = 2)
	public String getBox() {
		
		if(nomeBox != null){
			
			return nomeBox;
		}else{
			
			return "Todos";
		}
	}
	
	@Export(label = "Rota", exhibitionOrder = 4)
	public String getRota() {
		
		if(nomeRota != null){
						
			return nomeRota;		
		}else{
			return "Todos";
		}
		
	}
	
	public String getDataAtual(){
		Date dataAtual = new Date();		
		return DateUtil.formatarData(dataAtual, Constantes.DATE_PATTERN_PT_BR);
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

	public String getNomeRoteiro() {
		return nomeRoteiro;
	}

	public void setNomeRoteiro(String nomeRoteiro) {
		this.nomeRoteiro = nomeRoteiro;
	}

	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<Long> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Long> produtos) {
		this.produtos = produtos;
	}

	public String getNomesProduto() {
		return nomesProduto;
	}

	public void setNomesProduto(String nomesProduto) {
		this.nomesProduto = nomesProduto;
	}

	public Boolean getIsImpressao() {
		return isImpressao;
	}

	public void setIsImpressao(Boolean isImpressao) {
		this.isImpressao = isImpressao;
	}
	
}