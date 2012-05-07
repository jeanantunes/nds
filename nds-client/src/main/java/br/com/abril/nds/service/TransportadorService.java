package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;

public interface TransportadorService {

	Transportador buscarTransportadorPorId(Long idTransportador);
	
	List<Transportador> buscarTransportadores();
	
	void cadastrarTransportador(Transportador transportador,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			Set<Long> listaEnderecosRemover,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover,
			List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover);
	
	void excluirTransportador(Long idTransportador);

	ConsultaTransportadorDTO consultarTransportadores(FiltroConsultaTransportadorDTO filtro);

	Transportador obterTransportadorPorCNPJ(String cnpj);

	List<Veiculo> buscarVeiculos();

	Veiculo buscarVeiculoPorId(Long idVeiculo);

	void cadastrarVeiculo(Veiculo veiculo);
	
	void cadastrarVeiculos(List<Veiculo> veiculos);

	void excluirVeiculo(Long idVeiculo);

	List<Motorista> buscarMotoristas();

	Motorista buscarMotoristaPorId(Long idMotorista);

	void cadastarMotorista(Motorista motorista);

	void excluirMotorista(Long idMotorista);

	List<Rota> buscarRotas();

	void cadastrarMotoristas(List<Motorista> motoristas);
}