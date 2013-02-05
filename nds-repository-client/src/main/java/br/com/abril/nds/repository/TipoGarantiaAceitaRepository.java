package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;

public interface TipoGarantiaAceitaRepository extends Repository<TipoGarantiaAceita, Long> {

	public void alterarOuCriar(TipoGarantiaAceita tipoGarantiaAceita);

}
