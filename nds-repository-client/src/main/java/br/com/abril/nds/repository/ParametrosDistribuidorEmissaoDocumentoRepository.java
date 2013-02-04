package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;

public interface ParametrosDistribuidorEmissaoDocumentoRepository extends Repository<ParametrosDistribuidorEmissaoDocumento, Long> {

	public void alterarOuCriar(ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumento);

}
