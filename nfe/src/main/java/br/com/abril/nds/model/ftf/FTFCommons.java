package br.com.abril.nds.model.ftf;

import java.util.List;

public interface FTFCommons  {

	public void setCodigoEstabelecimentoEmissor(String codigoCentroEmissor);

	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora);

	public void setCodLocal(String codLocal);

	public void setTipoPedido(String tipoPedido);

	public void setNumeroDocOrigem(String numeroDocOrigem);
	
	public List<String> validateBean();

}
