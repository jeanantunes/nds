package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroEstoqueProdutosRecolhimento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2012855602062631670L;

	@Export(label="Data Recolhimento")
	private Date dataRecolhimento;
	
	@Export(label="Tipo Estoque")
	private TipoEstoque tipoEstoque;
	
	private PaginacaoVO paginacaoVO;
	
	public FiltroEstoqueProdutosRecolhimento(Date dataRecolhimento, TipoEstoque tipoEstoque, 
			int page, int rp, String sortName, String sortOrder){
		
		this.dataRecolhimento = dataRecolhimento;
		this.tipoEstoque = tipoEstoque;
		this.paginacaoVO = new PaginacaoVO(page, rp, sortOrder, sortName);
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}
