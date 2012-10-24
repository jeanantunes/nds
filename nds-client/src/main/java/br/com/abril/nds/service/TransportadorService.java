package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.model.cadastro.Motorista;
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
			List<Veiculo> listaVeiculosAdicionar,
			Set<Long> listaVeiculosRemover,
			List<Motorista> listaMotoristasAdicionar,
			Set<Long> listaMotoristasRemover,
			List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover);
	
	void excluirTransportador(Long idTransportador);

	ConsultaTransportadorDTO consultarTransportadores(FiltroConsultaTransportadorDTO filtro);

	Transportador obterTransportadorPorCNPJ(String cnpj);

	List<Veiculo> buscarVeiculosPorTransportador(Long idTransportador, Set<Long> idsIgnorar, 
			String sortname, String sortorder);

	Veiculo buscarVeiculoPorId(Long idVeiculo);

	void cadastrarVeiculo(Veiculo veiculo);
	
	void cadastrarVeiculos(List<Veiculo> veiculos);

	void excluirVeiculo(Long idVeiculo);

	List<Motorista> buscarMotoristasPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder);

	Motorista buscarMotoristaPorId(Long idMotorista);

	void cadastarMotorista(Motorista motorista);

	void excluirMotorista(Long idMotorista);

	void cadastrarMotoristas(List<Motorista> motoristas);

	List<TelefoneAssociacaoDTO> buscarTelefonesTransportador(Long id, Set<Long> idsIgnorar);

	List<EnderecoAssociacaoDTO> buscarEnderecosTransportador(Long id, Set<Long> idsIgnorar);

	List<AssociacaoVeiculoMotoristaRota> buscarAssociacoesTransportador(
			Long idTransportador, Set<Long> idsIgnorar, String sortname, String sortorder);

	List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(String sortname, String sortorder);

	List<Long> buscarIdsRotasPorAssociacao(Set<Long> assocRemovidas);

	boolean verificarAssociacaoMotorista(Long idMotorista, Set<Long> idsIgnorar);

	boolean verificarAssociacaoVeiculo(Long referencia, Set<Long> idsIgnorar);

	boolean verificarAssociacaoRotaRoteiro(Long idRota);

	List<CotaAtendidaTransportadorVO> buscarCotasAtendidadas(
			Long idTransportador, String sortorder, String sortname);
	
	/**
	 * Obtém dados de Envio de Reparte de determinada cota para determinado transportador
	 * 
	 * @param dataDe - Data de início da pesquisa
	 * @param dataAte - Data de fim da pesquisa
	 * @param idTransportador - Identificador do Transportador
	 * @param paginacaoVO - VO com dados de paginação e ordenação
	 * @return
	 */
	FlexiGridDTO<CotaTransportadorDTO> obterResumoTransportadorCota(FiltroRelatorioServicosEntregaDTO filtro);
	
	/**
	 * Obtém detalhes Financeiros do envio de Reparte das Cotas de determinado Transportador
	 * 
	  * @param dataDe - Data de início da pesquisa
	 * @param dataAte - Data de fim da pesquisa
	 * @param idTransportador - Identificador do Transportador
	 * @param idCota - Identificador da cota
	 * @return
	 */
	List<MovimentoFinanceiroDTO> obterDetalhesTrasportadorPorCota(FiltroRelatorioServicosEntregaDTO filtro);
	
}