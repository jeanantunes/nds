package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.model.cadastro.Transportador;

public interface TransportadorService {

	Transportador buscarTransportadorPorId(Long idTransportador);
	
	List<Transportador> buscarTransportadores();
	
	void cadastrarTransportador(Transportador transportador,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			Set<Long> listaEnderecosRemover,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover,
			List<AssociacaoVeiculoMotoristaRota> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover);
	
	void excluirTransportador(Long idTransportador);

	ConsultaTransportadorDTO consultarTransportadores(FiltroConsultaTransportadorDTO filtro);

	Transportador obterTransportadorPorCNPJ(String cnpj);
}