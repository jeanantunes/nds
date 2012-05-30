package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.client.vo.RegistroCurvaABCCotaVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
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
	
	/**
	 * Persiste um conjunto de telefones para uma determinada cota.
	 *
	 * @param idCota
	 *
	 * @param listaTelefonesAdicionar
	 *
	 * @param listaTelefonesRemover
	 */
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

	List<Cota> obterCotaAssociadaFiador(Long idFiador);

	/**
	 * Obtém dados de Distribuição da cota
	 *
	 * @param idCota - Código da cota
	 * @return DTO com dados de distribuiçãos
	 */
	DistribuicaoDTO obterDadosDistribuicaoCota(Long idCota);

	/**
	 * Retorna uma lista de fornecedores associados a uma determinda cota
	 * @param idCota
	 * @return List<Fornecedor> 
	 */
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

	/**
	 * Exclui uma cota referente o parâmetro informado
	 * @param idCota
	 */
	void excluirCota(Long idCota);

	/**
	 * Retorna os dados cadastrais de uma determinada cota
	 * @param idCota
	 * @return CotaDTO
	 */
	CotaDTO obterDadosCadastraisCota(Long idCota);

	/**
	 * Retorna o número de sugestão para inclusão de uma nova cota
	 * @return Integer
	 */
	Integer gerarNumeroSugestaoCota();
	
	/**
	 *  Retorna uma lista de tipos de desconto não associadas a uma cota
	 * @param idCota
	 * @return List<TipoDesconto>
	 */
	List<TipoDesconto> obterDescontos(Long idCota);
	
	/**
	 * Retorna uma lista de tipos de desconto associadas a uma cota
	 * @param idCota
	 * @return List<TipoDesconto>
	 */
	List<TipoDesconto> obterDescontosCota(Long idCota);

	/**
	 * Persiste os dados de tipo de desconto referente a cota informada
	 * @param descontos
	 * @param idCota
	 */
	void salvarDescontosCota(List<Long> descontos, Long idCota);
	
	/**
	 * Persiste os dados de sócio referente o identificador da cota informado
	 * @param sociosCota
	 * @param idCota
	 */
	void salvarSociosCota(List<SocioCota> sociosCota, Long idCota );
	
	/**
	 * Retorna uma lista de sócios referente o código da cota informada
	 * @param idCota - identificador da cota
	 * @return  List<SocioCota>
	 */
	List<SocioCota> obterSociosCota(Long idCota);
	
	/**
	 * Retorna uma cota ativa referente ao número de cota informado
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return Cota
	 */
	Cota obterPorNumeroDaCotaAtiva(Integer numeroCota);

	/**
	 * Método responsável por obter tipos de cota para preencher combo da camada view
	 * @return comboTiposCota: Tipos de cota padrão.
	 */
	public List<ItemDTO<TipoCota, String>> getComboTiposCota();
	
	void alterarCota(Cota cota);

	/**
	 * Método responsável por obter o total do relatório de vendas ABC por cota
	 * @param filtroCurvaABCCotaDTO
	 * @return
	 */
	public ResultadoCurvaABC obterCurvaABCCotaTotal(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);
	
	/**
	 * Método responsável por obter o relatório de vendas ABC por cota
	 * @param filtroCurvaABCCotaDTO
	 * @return
	 */
	public List<RegistroCurvaABCCotaVO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);

}
