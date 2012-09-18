package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Transportador;

public interface TransportadorRepository extends
		Repository<Transportador, Long> {

	ConsultaTransportadorDTO pesquisarTransportadoras(FiltroConsultaTransportadorDTO filtro);

	Transportador buscarTransportadorPorCNPJ(String cnpj);

	List<CotaAtendidaTransportadorVO> buscarCotasAtendidadas(
			Long idTransportador, String sortorder, String sortname);
}