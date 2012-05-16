package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaService {

	/**
	 * 
	 * Retorna uma lista de Cotas em função dos filtros informado.
	 * 
	 * @param filtro - filtro com as opções de consulta
	 * 
	 * @return List<CotaDTO>
	 */
	List<CotaDTO> obterCotas(FiltroCotaDTO filtro);
	
	/**
	 * 
	 * Retorna a quantidade de Cotas em função dos filtros informado.
	 * 
	 * @param filtro - filtro com as opções de consulta
	 * 
	 * @return Long
	 */
	Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro);
	
	Cota obterPorNumeroDaCota(Integer numeroCota);

	List<Cota> obterCotasPorNomePessoa(String nome);

	Cota obterPorNome(String nome);
	
	Cota obterPorId(Long idCota);
	
	/**
	 * Obtém uma lista dos endereços cadastrados para uma determinada cota.
	 * 
	 * @param idCota - Id da cota.
	 * 
	 * @return List<EnderecoAssociacaoDTO> 
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota);
	/**
	 * Persiste um conjunto de endereços para uma determinada cota.
	 * 
	 * @param cota
	 * 
	 * @param listaEnderecoAssociacaoSalvar
	 * 
	 * @param listaEnderecoAssociacaoRemover
	 */
	void processarEnderecos(Long idCota, 
							List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar, 
							List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover);
	
	void processarTelefones(Long idCota, 
			List<TelefoneAssociacaoDTO> listaTelefonesAdicionar, 
			Collection<Long> listaTelefonesRemover);
	
	/**
	 * Obtém cobranças não pagas em nome da cota
	 * 
	 * @param idCota -  Código da cota
	 * @return Lista de Cobranças
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota);
	
	/**
	 * Suspende lista de cotas
	 * 
	 * @param idCotas - código das cotas a serem suspensas
	 * @param idUsuario - identificador do usuário
	 * @param motivoAlteracaoSituacao - motivo de alteração da situação
	 * 
	 * @return Lista de Cotas suspensas
	 */
	List<Cota>suspenderCotas(List<Long> idCotas, Long idUsuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao);
	
	/**
	 * Suspende cota
	 * 
	 * 
	 * @param idCota - Código da cota a ser suspensa
	 * @param usuario - usuário
	 * @param motivoAlteracaoSituacao - motivo de alteração da situação
	 * 
	 * @return Cota - cota suspensa
	 */
	Cota suspenderCota(Long idCota, Usuario usuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao);

	/**
	 * Suspende lista de cotas e retorna dados básicos das cota suspensas com 
	 * necessidade de devolução de contrato
	 * 
	 * @param idCotas - Códigos das cotas
	 * @param usuario - usuário
	 * @return
	 */
	List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario);
	
	/**
	 * Obtém sugestão de cotas a serem suspensas com base 
	 * @param rp 
	 * @param page 
	 * 
	 * @param limiteInadimplencia - Quantidade de inadimplencias que define a cota como sujeita a suspensão
	 * 
	 * @param paginacaoVO - Dados referentes a paginação e ordenação
	 * @return lista de CotaSuspensaoDTO
	 */
	List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer inicio, Integer rp);

	Long obterTotalCotasSujeitasSuspensao();
	
	String obterNomeResponsavelPorNumeroDaCota(Integer numeroCota);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar);
	
	Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota);
	
	//void cadastrarTelefonesCota(List<TelefoneCota> listaTelefonesAdicionar, Collection<Long> listaTelefonesRemover);

	List<Cota> obterCotaAssociadaFiador(Long idFiador);

	/**
	 * Obtém dados de Distribuição da cota
	 * 
	 * @param idCota - Código da cota
	 * @return DTO com dados de distribuiçãos
	 */
	DistribuicaoDTO obterDadosDistribuicaoCota(Long idCota);
	
	
	List<Fornecedor> obterFornecedoresCota(Long idCota);

	/**
	 * Salvar dados de Distribuição da cota
	 * 
	 * @param distribuicao - DTO que representa os dados de Distribuição da cota
	 */
	void salvarDistribuicaoCota(DistribuicaoDTO distribuicao);
	

	/**
	 * Salva os dados básicos da cota 
	 * 
	 * @param cotaDto
	 */
	Long salvarCota(CotaDTO cotaDto);
	
	void excluirCota(Long idCota);
	
	CotaDTO obterDadosCadastraisCota(Long idCota);
	
	Integer gerarNumeroSugestaoCota();
	
	List<TipoDesconto> obterDescontos(Long idCota);
	
	List<TipoDesconto> obterDescontosCota(Long idCota);
	
	void salvarDescontosCota(List<Long> descontos, Long idCota);
	
	void salvarSociosCota(List<SocioCota> sociosCota, Long idCota );
	
	List<SocioCota> obterSociosCota(Long idCota);
	
	Cota obterPorNumeroDaCotaAtiva(Integer numeroCota);
	
	/**
	 * Método responsável por obter tipos de cota para preencher combo da camada view
	 * @return comboTiposCota: Tipos de cota padrão.
	 */
	public List<ItemDTO<TipoCota, String>> getComboTiposCota();

	
}
