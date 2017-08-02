package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroCurvaABCExportacaoDistribuidorVO {
	
	private RegistroCurvaABCDistribuidorVO registroCurvaABCDistribuidorVO;
	
	public RegistroCurvaABCExportacaoDistribuidorVO() {}
	
	public RegistroCurvaABCExportacaoDistribuidorVO(RegistroCurvaABCDistribuidorVO registroCurvaABCDistribuidorVO) {
		
		this.registroCurvaABCDistribuidorVO = registroCurvaABCDistribuidorVO;
	}
	
	@Export(label = "RK. Cota", exhibitionOrder = 2, columnType = ColumnType.NUMBER)
	public Long getRkCota(){
		return registroCurvaABCDistribuidorVO.getRkCota();
	}
	
	@Export(label = "RK. Produto", exhibitionOrder = 1, columnType = ColumnType.NUMBER)
	public Long getRkProduto(){
		return registroCurvaABCDistribuidorVO.getRkProduto();
	}
	
	@Export(label = "Número", exhibitionOrder = 3, columnType = ColumnType.NUMBER)
	public String getNumeroCota(){
		return registroCurvaABCDistribuidorVO.getNumeroCotaString();
	}
	
	@Export(label = "Cota", exhibitionOrder = 4)
	public String getNomeCota(){
		return registroCurvaABCDistribuidorVO.getNomeCota();
	}

	@Export(label = "Municipio", exhibitionOrder = 5)
	public String getMunicipio(){
		return registroCurvaABCDistribuidorVO.getMunicipio();
	}

	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 6, columnType = ColumnType.NUMBER)
	public String getQuantidadePdvs(){
		return registroCurvaABCDistribuidorVO.getQuantidadePdvsString();
	}
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 7, columnType = ColumnType.NUMBER)
	public String getVendaExemplaresFormatado(){
		return registroCurvaABCDistribuidorVO.getVendaExemplaresFormatado();
	}
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 8, columnType = ColumnType.DECIMAL)
	public String getFaturamentoCapaFormatado(){
		return registroCurvaABCDistribuidorVO.getFaturamentoCapaFormatado();
	}

	@Export(label = "% Participação", exhibitionOrder = 9, columnType = ColumnType.DECIMAL)
	public String getParticipacao(){
		return registroCurvaABCDistribuidorVO.getParticipacaoFormatado();
	}

	@Export(label = "% Participação Acumulada", exhibitionOrder = 10, columnType = ColumnType.DECIMAL)
	public String getParticipacaoAcumulada(){
		return registroCurvaABCDistribuidorVO.getParticipacaoAcumuladaFormatado();
	}
	
	@Export(label = "segmentacao", exhibitionOrder = 11, columnType = ColumnType.DECIMAL)
	public String getSegmento(){
		return registroCurvaABCDistribuidorVO.getSegmento();
	}
	
	
	@Export(label = "produtoCodigo", exhibitionOrder = 12,  columnType = ColumnType.STRING)
	public String getProdutoCodigo(){
		return registroCurvaABCDistribuidorVO.getProdutoCodigo();
	}
	
	@Export(label = "produtoNome", exhibitionOrder = 13,   columnType = ColumnType.STRING)
	public String getProdutoNome(){
		return registroCurvaABCDistribuidorVO.getProdutoNome();
	}
	
	@Export(label = "editorCodigo", exhibitionOrder = 14,   columnType = ColumnType.STRING)
	public String getEditorCodigo(){
		return registroCurvaABCDistribuidorVO.getEditorCodigo();
	}
	
	@Export(label = "codigoBarra", exhibitionOrder = 15,   columnType = ColumnType.STRING)
	public String getCodigoBarra(){
		return registroCurvaABCDistribuidorVO.getCodigoBarra();
	}
	
	@Export(label = "peb", exhibitionOrder = 16,   columnType = ColumnType.STRING)
	public String getPeb(){
		return registroCurvaABCDistribuidorVO.getPeb();
	}
	
	@Export(label = "numeroEdicao", exhibitionOrder = 17,   columnType = ColumnType.STRING)
	public String getNumeroEdicao(){
		return registroCurvaABCDistribuidorVO.getNumeroEdicao();
	}
	
	@Export(label = "dataLancamento", exhibitionOrder = 18,   columnType = ColumnType.STRING)
	public String getDataLancamento(){
		return registroCurvaABCDistribuidorVO.getDataLancamento();
	}
	
	@Export(label = "dataRecolhimento", exhibitionOrder = 19,   columnType = ColumnType.STRING)
	public String getDataRecolhimento(){
		return registroCurvaABCDistribuidorVO.getDataRecolhimento();
	}
	
	@Export(label = "classificacao", exhibitionOrder = 20,   columnType = ColumnType.STRING)
	public String getClassificacao(){
		return registroCurvaABCDistribuidorVO.getClassificacao();
	}
	
	@Export(label = "venda", exhibitionOrder = 21,   columnType = ColumnType.STRING)
	public String getVenda(){
		return registroCurvaABCDistribuidorVO.getVenda();
	}
	
	@Export(label = "preco", exhibitionOrder = 22,   columnType = ColumnType.STRING)
	public String getPreco(){
		return registroCurvaABCDistribuidorVO.getPreco();
	}
	
}
