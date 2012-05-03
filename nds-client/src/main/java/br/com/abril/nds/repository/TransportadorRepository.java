package br.com.abril.nds.repository;

import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Transportador;

public interface TransportadorRepository extends
		Repository<Transportador, Long> {

	ConsultaTransportadorDTO pesquisarTransportadoras(FiltroConsultaTransportadorDTO filtro);

	Transportador buscarTransportadorPorCNPJ(String cnpj);
}