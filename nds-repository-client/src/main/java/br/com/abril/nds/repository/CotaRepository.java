package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ParametroDistribuicaoEntregaCotaDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 * 
 */
public interface CotaRepository extends Repository<Cota, Long> {
	
	
	/**
	 * Realiza um select for update no registro de cota
	 * 
	 * @param numeroCota
	 * 
	 * @return Cota
	 */
    public Cota selectForUpdate(Long numeroCota);

	
    /**
     * Obtém uma Cota pelo seu número.
     * 
     * @param numeroCota - nÃºmero da cota
     * 
     * @return {@link Cota}
     */
    Cota obterPorNumeroDaCota(Integer numeroCota);
    
    /**
     * Obtém uma lista de cotas através da comparação por nome.
     * 
     * @param nome - nome da cota
     * 
     * @return Lista de {@link Cota}
     */
    List<Cota> obterCotasPorNomePessoa(String nome);
    
    List<CotaDTO> obterCotasPorNomeAutoComplete(String nome);
    
    /**
     * Obtém uma lista de cotas pelo nome.
     * 
     * @param nome - nome da cota (pessoa)
     * 
     * @return Lista de {@link Cota}
     */
    List<Cota> obterPorNome(String nome);
    
    /**
     * Obtém uma lista dos endereços cadastrados para uma determinada cota.
     * 
     * @param idCota - Id da cota.
     * 
     * @return List<Endereco>
     */
    List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota);
    
    /**
     * Obtém sugestão de cotas a serem suspensas com base
     * 
     * @param rp
     * @param page
     * @param paginacaoVO
     * 
     * @param limiteInadimplencia - Quantidade de inadimplencias que define a
     *            cota como sujeita a suspensão
     * @param paginacaoVO - Dados referentes a paginação e ordenação
     * 
     * @return Cotas
     */
    List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder,
            String sortColumn, Integer page, Integer rp, Date dataOperacao);
    
    /**
     * Obtém valor dos repartes Consignados a cota em determinado dia
     * 
     * @param idCota - código da cota
     * @param date - data
     * @return
     */
    List<ProdutoValorDTO> obterReparteDaCotaNoDia(Long idCota, Date date);
    
    /**
     * Obtém valor total de consignados da cota
     * 
     * @param idCota
     * @return
     */
    List<ProdutoValorDTO> obterValorConsignadoDaCota(Long idCota);
    
    List<Integer> obterDiasConcentracaoPagamentoCota(Long idCota);
    
    Long obterTotalCotasSujeitasSuspensao(Date dataOperacao);
    
    List<Cota> obterCotaAssociadaFiador(Long idFiador);
    
    /**
     * 
     * Obtem cotas sujeitas a antecipação de recolhimento de encalhe.
     * 
     * @param filtro - filtro coma as opções de consulta
     * 
     * @return List<ChamdaAntecipadaEncalheDTO>
     */
    List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(
            FiltroChamadaAntecipadaEncalheDTO filtro);
    
    /**
     * Obtem a quantidade de exemplares de uma cota sujeita a antecipação de
     * recolhimento de encalhe.
     * 
     * @param filtro - filtro coma as opções de consulta
     * 
     * @return BigDecimal
     */
    BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(
            FiltroChamadaAntecipadaEncalheDTO filtro);
    
    Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota);
    
    /**
     * Obtem o endereço principal da cota.
     * 
     * @param idCota Id da Cota
     * @return endereço principal da cota.
     */
    public abstract EnderecoCota obterEnderecoPrincipal(long idCota);
    
    /**
     * Retorna uma lista de Cotas cadastradas para manutenção de edição e
     * exclusão
     * 
     * @param filtro - opções de filtro para consulta
     * 
     * @return List<CotaDTO>
     */
    List<CotaDTO> obterCotas(FiltroCotaDTO filtro);
    
    /**
     * Retorna a quantidade de Cotas cadastradas para manutenção de edição e
     * exclusão
     * 
     * @param filtro - opções de filtro para consulta
     * 
     * @return Long
     */
    Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro);
    
    Integer gerarSugestaoNumeroCota();
    
    /**
     * Retorna uma cota ativa referente o número da cota informado
     * 
     * @param numeroCota - número da cota
     * @return Cota
     */
    Cota obterPorNumerDaCota(Integer numeroCota);
    
    /**
     * Retorna um telefone associado a uma cota
     * 
     * @param idTelefone - identificador da associação
     * @param idCota - identificador da cota
     * @return TelefoneCota
     */
    TelefoneCota obterTelefonePorTelefoneCota(Long idTelefone, Long idCota);
    

    
    /**
     * Retorna todos os ids das cotas que estão entre o itervalo de cotas
     * parametrizado;
     * 
     * @param intervaloCota intevalo de id das cotas
     * @param intervaloBox TODO
     * @param situacoesCadastro TODO
     * @param sortName TODO
     * @param sortOrder TODO
     * @param maxResults TODO
     * @param page TODO
     * @return ids das cotas
     */
    List<Long> obterIdCotasEntre(Intervalo<Integer> intervaloCota,
            Intervalo<Integer> intervaloBox, List<SituacaoCadastro> situacoesCadastro, Long idRoteiro, Long idRota, String sortName, String sortOrder, Integer maxResults, Integer page);
    
    Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro);
    
    List<CotaResumoDTO> obterCotas(SituacaoCadastro situacaoCadastro);
    
    /**
     * Obtém cotas relacionadas a um fornecedor.
     * 
     * @param idFornecedor - id do fornecedor
     * 
     * @return {@link Set} de {@link Cota}
     */
    Set<Cota> obterCotasPorFornecedor(Long idFornecedor);
    
    List<CotaTipoDTO> obterCotaPorTipo(
            TipoDistribuicaoCota tipoCota, Integer page,
            Integer rp, String sortname, String sortorder);
    
    int obterCountCotaPorTipo(TipoDistribuicaoCota tipoCota);
    
    /**
     * Retorna Municipios e a quantidade de cotas para cada - resultado paginado
     * 
     * @param page
     * @param rp
     * @param sortname
     * @param sortorder
     * @return
     */
    List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp,
            String sortname, String sortorder);
    
    /**
     * Count da pesquisa "obterQtdeCotaMunicipio"
     * 
     * @return
     */
    int obterCountQtdeCotaMunicipio();
    
    /**
     * Recupera o histórico de titularidade da cota pelo identificador da cota e
     * identificador do histórico
     * 
     * @param idCota identificador da cota
     * @param idHistorico identificador do histórico
     * @return {@link HistoricoTitularidadeCota} que correspopnde aos
     *         identificadores recebidos ou null caso não seja encontrado
     * @throws IllegalArgumentException se os identificadores recebidos forem
     *             nulos
     * 
     */
    HistoricoTitularidadeCota obterHistoricoTitularidade(Long idCota, Long idHistorico);
    
    
    /**
     * Recupera a forma de pagamento do histórico de titularidade da cota pelo
     * indentificador
     * 
     * @param idFormaPagto identificador da forma de pagamento do histórico de
     *            titularidade da cota
     * @return forma de pagamento do histórico de titularidade da cota com o
     *         identificador recebido
     */
    HistoricoTitularidadeCotaFormaPagamento obterFormaPagamentoHistoricoTitularidade(Long idFormaPagto);
    
    /**
     * Recupera o sócio do histórico de titularidade da cota
     * 
     * @param idSocio identificador do sócio
     * @return sócio do histórico de titularidade da cota
     */
    HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidade(Long idSocio);
    
    void ativarCota(Integer numeroCota);
    
    List<CotaResumoDTO> obterCotasComInicioAtividadeEm(Date dataInicioAtividade);
    
    List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte);
    
    List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe);
    
    Cota obterPorPDV(Long idPDV);
    
    Cota buscarCotaPorID(Long id);
    
	/**
     * 
     * Obtém todas as cotas que possuem a média de reparte dentro do range
     * inicial + final. Se for informado a lista de ProdutoEdicaoDTO, será
     * considerado somente esses produtos e edições MÁXIMO 6 PRODUTOS
     * 
     * @param qtdReparteInicial
     * @param qtdReparteFinal
     * @param produtoEdicaoDto
     * @param cotasAtivas
     * @return
     */
    List<CotaDTO> buscarCotasQuePossuemRangeReparte(BigInteger qtdReparteInicial, BigInteger qtdReparteFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
    List<CotaDTO> buscarCotasQuePossuemRangeVenda(BigInteger qtdVendaInicial, BigInteger qtdVendaFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
    List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioAEmitir(FiltroConsultaNotaEnvioDTO filtro);
    
    List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidasEAEmitir(FiltroConsultaNotaEnvioDTO filtro);
    
    List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidas(FiltroConsultaNotaEnvioDTO filtro);
    
    Integer obterDadosCotasComNotaEnvioEmitidasCount(FiltroConsultaNotaEnvioDTO filtro);
    
    Integer obterDadosCotasComNotaEnvioAEmitirCount(FiltroConsultaNotaEnvioDTO filtro);
    
    Integer obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(FiltroConsultaNotaEnvioDTO filtro);
    
    List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(BigDecimal percentVenda, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
    List<CotaDTO> buscarCotasPorNomeOuNumero(CotaDTO cotaDto, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
    List<CotaDTO> buscarCotasPorComponentes(ComponentesPDV componente, String elemento, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
    List<AnaliseHistoricoDTO> buscarHistoricoCotas(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, List<Integer> numeroCotas);
    
    HistoricoVendaPopUpCotaDto buscarCota(Integer numero);
    
    boolean cotaVinculadaCotaBase(Long idCota);
    
    List<Integer> numeroCotaExiste(TipoDistribuicaoCota tipoDistribuicaoCota, Integer... cotaIdArray);
    
    Cota obterCotaComBaseReferencia(Long idCota);
    
    TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(Integer numeroCota);
    
    int obterCotasAtivas();
    
    CotaDTO buscarCotaPorNumero(Integer numeroCota, String codigoProduto, Long idClassifProdEdicao);
    
    List<CotaEstudo> getInformacoesCotaEstudo(ProdutoEdicao produtoEdicao);
    
    List<CotaDTO> buscarCotasHistorico(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas);
    
	    /**
     * Obtém o número da cota, através de seu ID.
     * 
     * @param idCota - ID da cota que deseja obter o número.
     * 
     * @return Número da cota.
     */
    Integer buscarNumeroCotaPorId(Long idCota);
    
    List<Long> obterIdsCotasPorMunicipio(String municipio);
    
    List<CotaDTO> obterCotasSemRoteirizacao(Intervalo<Integer> intervaloCota,
            Intervalo<Date> intervaloDataLancamento,
            Intervalo<Date> intervaloDateRecolhimento);
    List<ProdutoAbastecimentoDTO> obterCotaPorProdutoEdicaoData(FiltroMapaAbastecimentoDTO filtro);
    
    BigDecimal obterTotalDividaCotasSujeitasSuspensao(Date dataOperacaoDistribuidor);
    
	    /**
     * Obtem Cotas do tipo À Vista, com data de alteração de status menor que a
     * data atual
     * 
     * @return List<Cota>
     */
    List<Cota> obterCotasTipoAVista();
    
    Long countCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte);
    
    Long countCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe);
    
    Long countCotasComInicioAtividadeEm(Date dataInicioAtividade);
    
    Long countCotas(SituacaoCadastro situacaoCadastro);
    
    /**
     * Obtem cotas por intervalo de numero de cotas
     * @param cotaDe
     * @param cotaAte
     * @param situacoesCadastro
     * @return List<Cota>
     */
    List<Cota> obterCotasIntervaloNumeroCota(Integer cotaDe, Integer cotaAte, List<SituacaoCadastro> situacoesCadastro);
    
    SituacaoCadastro obterSituacaoCadastroCota(Integer numeroCota);
    
    Long obterIdPorNumeroCota(Integer numeroCota);
    
    CotaVO obterDadosBasicosCota(Integer numeroCota);
    
    TipoDistribuicaoCota obterTipoDistribuicao(Long idCota);

    Cota obterPorNumerDaCota(Integer numeroCota, SituacaoCadastro situacao);

    String obterEmailCota(Integer numeroCota);
    
    List<ParametroDistribuicaoEntregaCotaDTO> obterParametrosDistribuicaoEntregaCota();

	Fornecedor obterFornecedorPadrao(Long idCota);

	Boolean validarNumeroCota(Integer numCota, TipoDistribuicaoCota tipoDistribuicaoCota);
	
	List<ItemDTO<String, String>> obterCotasSemRoterizacao(List<Long> listaIdCotas);
}
