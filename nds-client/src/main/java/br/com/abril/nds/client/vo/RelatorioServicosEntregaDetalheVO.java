package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.RelatorioServicosEntregaDetalheDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class RelatorioServicosEntregaDetalheVO {

	private String descricao;
	private String data;
	private String valor;
	
	
	public RelatorioServicosEntregaDetalheVO()
	{}
	
	
	public RelatorioServicosEntregaDetalheVO(RelatorioServicosEntregaDetalheDTO dto) {
		
		this.descricao = dto.getDescricao();
		this.data = DateUtil.formatarDataPTBR(dto.getData());
		this.valor = CurrencyUtil.formatarValor(dto.getValor());
	}
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}
