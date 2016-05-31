package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.AnaliseHistoricoXLSDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.ParametroDistribuicaoEntregaCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.DistribuidorClassificacaoCota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 *
 * @author Discover Technology
 *
 */
public interface CotaService {

	/**
	 * Retorna true se a cota possuir operação diferenciada
	 * 
	 * @param numeroCota
	 * @param dataOperacao TODO
	 * 
	 * @return boolean
	 */
	boolean isCotaOperacaoDiferenciada(Integer numeroCota, Date dataOperacao);
	
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

	List<Cota> obterPorNome(String nome);

	Cota obterPorId(Long idCota);

	List<CotaDTO> obterPorNomeAutoComplete(String nome);
	
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
	Cota suspenderCota(Long idCota, Usuario usuario, Date dataInicioValidade, MotivoAlteracaoSituacao motivoAlteracaoSituacao);

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
	
	BigDecimal obterTotalDividaCotasSujeitasSuspensao();

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
	void salvarDistribuicaoCota(DistribuicaoDTO distribuicao) throws FileNotFoundException, IOException ;


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
	
	public EnderecoCota obterEnderecoPrincipal(long idCota);
	
	/**
	 * Retorna todas as cotas que estão entre o itervalo de cotas parametrizado;
	 * 
	 * @param intervaloCota intevalo de id das cotas
	 * @param intervaloBox
	 * @param situacao
	 * @return ids das cotas
	 */
	List<Cota> obterCotasEntre(Intervalo<Integer> intervaloCota, Intervalo<Integer> intervaloBox, SituacaoCadastro situacao);

	/**
	 * Cria uma nova cota, através do processo de alteração de titularidade.
	 * 
	 * @param cotaDTO
	 */
	CotaDTO criarCotaTitularidade(CotaDTO cotaDTO);

	/**
     * Obtém o histórico de titularidade da cota de acordo com o identificador
     * da cota e o identificador do histórico
     * @param idCota identificador da cota
     * @param idHistorico identificador da cota
     * @return {@link CotaDTO} com as informações do histórico de titularidade da cota
     */
    CotaDTO obterHistoricoTitularidade(Long idCota, Long idHistorico);
    
    /**
     * Obtém a lista de endereços associados ao histórico de titularidade 
     * da cota
     * @param idCota identificador da cota
     * @param idHistorico identificador do histórico de titularidade
     * @return {@link List<EnderecoAssociacaoDTO>} com os endereços associados ao 
     * histórico de titularidade da cota
     */
    List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidade(Long idCota, Long idHistorico);

    /**
     * Obtém a lista de telefones associados ao histórico de titularidade 
     * da cota
     * @param idCota identificador da cota
     * @param idHistorico identificador do histórico de titularidade
     * @return {@link List<TelefoneAssociacaoDTO>} com os telefones associados ao 
     * histórico de titularidade da cota
     */
    List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidade(Long idCota, Long idHistorico);

    /**
     * Obtém a lista de fornecedores associados ao histórico de titularidade da
     * cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico de titularidade
     * @return Lista de fornecedores associados ao histórico de titularidade da
     *         cota
     */
    List<FornecedorDTO> obterFornecedoresHistoricoTitularidadeCota(Long idCota, Long idHistorico);
    
    /**
     * Recupera os descontos de produto do histórico de titularidade da cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico de titularidade
     * @return Lista de {@link TipoDescontoProdutoDTO} com as informações de
     *         desconto associados ao histórico de titularidade da cota
     */
    List<TipoDescontoProdutoDTO> obterDescontosProdutoHistoricoTitularidadeCota(Long idCota, Long idHistorico);
    
    /**
     * Recupera os descontos de cota do histórico de titularidade da cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico de titularidade
     * @return Lista de {@link TipoDescontoCotaDTO} com as informações de
     *         desconto associados ao histórico de titularidade da cota
     */
    List<TipoDescontoCotaDTO> obterDescontosCotaHistoricoTitularidadeCota(Long idCota, Long idHistorico);

    byte[] getDocumentoProcuracao(Integer numeroCota) throws Exception;

	void atualizaTermoAdesao(String numCota, DescricaoTipoEntrega descricaoTipoEntrega) throws FileNotFoundException, IOException ;
	
	byte[] getDocumentoTermoAdesao(Integer numeroCota, BigDecimal valorDebito, BigDecimal percentualDebito) throws Exception;

    /**
     * Obtém as informações de distribuição do histórico de titularidade da cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico
     * @return dto com as informações de distribuição do histórico de
     *         titularidade da cota
     */
	DistribuicaoDTO obterDistribuicaoHistoricoTitularidade(Long idCota, Long idHistorico);
	
	Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro);
    
    List<CotaResumoDTO> obterCotas(SituacaoCadastro situacaoCadastro);
	
	List<CotaResumoDTO> obterCotasComInicioAtividadeEm(Date dataInicioAtividade);
	
	List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte);
	
	List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe);

	List<CotaDTO> buscarCotasQueEnquadramNoRangeDeReparte(BigInteger qtdReparteInicial, BigInteger qtdReparteFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
	
	List<CotaDTO> buscarCotasQueEnquadramNoRangeVenda(BigInteger qtdVendaInicial, BigInteger qtdVendaFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
	
	List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(BigDecimal percentVenda, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
	
	List<CotaDTO> buscarCotasPorNomeOuNumero(CotaDTO cotaDto, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
	
	List<CotaDTO> buscarCotasPorComponentes(ComponentesPDV componente, String elemento, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
	
	List<AnaliseHistoricoDTO> buscarHistoricoCotas(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, 
			List<Integer> numeroCotas, final String sortorder, final String sortname);
	
	HistoricoVendaPopUpCotaDto buscarCota(Integer numero);
	
	/**
	 * 
	 * @param idCota
	 * @return
	 */
	boolean isTipoCaracteristicaSegmentacaoConvencional(Long idCota);

	public void apagarTipoCota(Long idCota, String TipoCota);
	
	public List<DistribuidorClassificacaoCota> obterListaClassificacao();

	public abstract boolean cotaVinculadaCotaBase(Long idCota);
	
	void verificarCotasSemRoteirizacao(Intervalo<Integer> intervaloCota,
			Intervalo<Date> intervaloDataLancamento,
			Intervalo<Date> intervaloDataRecolhimento);
	
	List<Integer> numeroCotaExiste(TipoDistribuicaoCota tipoDistribuicaoCota, Integer... cotaIdArray);
	
	public TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(Integer numeroCota);
	
	/**
	 * Obtem lista de Cotas dos numeros de cotas passados como parametro
	 * @param numerosCota
	 * @return List<Cota>
	 */
	List<Cota> obterCotasPorNumeros(List<Integer> numerosCota);

	public abstract boolean isTipoDistribuicaoCotaEspecifico(Integer numeroCota,
			TipoDistribuicaoCota tipoDistribuicaoCota);
	
	/**
     * Salva as caracteristicas financeiras especificas da Cota
     * 
     * @param parametroCobranca
     * @return boolean
     */
	boolean salvarCaracteristicasFinanceirasEspecificasCota(final ParametroCobrancaCotaDTO parametroCobranca);
	
	List<CotaDTO> buscarCotasHistorico(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);

	/**
	 * Obtem cotas por intervalo de numero de cotas
	 * @param cotaDe
	 * @param cotaAte
	 * @param situacoesCadastro
	 * @return List<Cota>
	 */
	List<Cota> obterCotasEntre(Integer cotaDe,
			                   Integer cotaAte,
			                   List<SituacaoCadastro> situacoesCadastro);
	
	Long obterIdPorNumeroCota(Integer numeroCota);
	
	List<Cota> obterConjuntoCota(FiltroNFeDTO filtro);
	
	String obterEmailCota(Integer numeroCota);
	
	void processarParametrosCobrancaDistribuicao(DistribuicaoDTO dto, Cota cota);
	
	List<ParametroDistribuicaoEntregaCotaDTO> obterParametrosDistribuicaoEntregaCota();

	void validarTipoEntrega(Integer numeroCota, DescricaoTipoEntrega tipoEntrega);

	Boolean validarNumeroCota(Integer numeroCota, TipoDistribuicaoCota tipoDistribuicaoCota);

	List<AbastecimentoBoxCotaDTO> obterCotasExpedicao(Intervalo<Date> intervaloData);

	Map<Integer, EstudoCotaDTO> obterPorNumeroDaCota(List<Integer> numeroCota);

	Map<Integer, AnaliseHistoricoXLSDTO> dadosPDVhistoricoXLS(List<Integer> cotas);

	boolean isCotaRecebeFornecedor(Long idProdutoEdicao, Long idCota);

	boolean isCotaVarejo(Long idCota);
}
