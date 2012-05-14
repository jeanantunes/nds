package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;

public interface DistribuidorService {
	
	Distribuidor obter();

	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(Date dataDe,
			Date dataAte, String codigoFornecedor, String codigoProduto,
			String nomeProduto, String edicaoProduto, String codigoEditor,
			String codigoCota, String nomeCota, String municipio);

}
