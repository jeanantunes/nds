package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.ResultadoCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
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
     * Obtém uma Cota pelo seu número.
     * 
     * @param numeroCota
     *            - nÃºmero da cota
     * 
     * @return {@link Cota}
     */
    Cota obterPorNumerDaCota(Integer numeroCota);

    /**
     * Obtém uma lista de cotas através da comparação por nome.
     * 
     * @param nome
     *            - nome da cota
     * 
     * @return Lista de {@link Cota}
     */
    List<Cota> obterCotasPorNomePessoa(String nome);

    /**
     * Obtém uma lista de cotas pelo nome.
     * 
     * @param nome
     *            - nome da cota (pessoa)
     * 
     * @return Lista de {@link Cota}
     */
    List<Cota> obterPorNome(String nome);

    /**
     * Obtém uma lista dos endereços cadastrados para uma determinada cota.
     * 
     * @param idCota
     *            - Id da cota.
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
     * @param limiteInadimplencia
     *            - Quantidade de inadimplencias que define a cota como sujeita
     *            a suspensão
     * @param paginacaoVO
     *            - Dados referentes a paginação e ordenação
     * 
     * @return Cotas
     */
    List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder,
            String sortColumn, Integer page, Integer rp);

    /**
     * Obtém valor dos repartes Consignados a cota em determinado dia
     * 
     * @param idCota
     *            - código da cota
     * @param date
     *            - data
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

    Long obterTotalCotasSujeitasSuspensao();

    List<Cota> obterCotaAssociadaFiador(Long idFiador);

    /**
     * 
     * Obtem cotas sujeitas a antecipação de recolhimento de encalhe.
     * 
     * @param filtro
     *            - filtro coma as opções de consulta
     * 
     * @return List<ChamdaAntecipadaEncalheDTO>
     */
    List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(
            FiltroChamadaAntecipadaEncalheDTO filtro);

    /**
     * Obtem a quantidade de cotas sujeitas a antecipação de recolhimento de
     * encalhe.
     * 
     * @param filtro
     *            - filtro coma as opções de consulta
     * 
     * @return Long
     */
    Long obterQntCotasSujeitasAntecipacoEncalhe(
            FiltroChamadaAntecipadaEncalheDTO filtro);

    /**
     * Obtem a quantidade de exemplares de uma cota sujeita a antecipação de
     * recolhimento de encalhe.
     * 
     * @param filtro
     *            - filtro coma as opções de consulta
     * 
     * @return BigDecimal
     */
    BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(
            FiltroChamadaAntecipadaEncalheDTO filtro);

    Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota);

    /**
     * Obtem o endereço principal da cota.
     * 
     * @param idCota
     *            Id da Cota
     * @return endereço principal da cota.
     */
    public abstract EnderecoCota obterEnderecoPrincipal(long idCota);

    /**
     * Retorna uma lista de Cotas cadastradas para manutenção de edição e
     * exclusão
     * 
     * @param filtro
     *            - opções de filtro para consulta
     * 
     * @return List<CotaDTO>
     */
    List<CotaDTO> obterCotas(FiltroCotaDTO filtro);

    /**
     * Retorna a quantidade de Cotas cadastradas para manutenção de edição e
     * exclusão
     * 
     * @param filtro
     *            - opções de filtro para consulta
     * 
     * @return Long
     */
    Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro);

    Integer gerarSugestaoNumeroCota();

    /**
     * Retorna uma cota ativa referente o número da cota informado
     * 
     * @param numeroCota
     *            - número da cota
     * @return Cota
     */
    Cota obterPorNumerDaCotaAtiva(Integer numeroCota);

    /**
     * Retorna um telefone associado a uma cota
     * 
     * @param idTelefone
     *            - identificador da associação
     * @param idCota
     *            - identificador da cota
     * @return TelefoneCota
     */
    TelefoneCota obterTelefonePorTelefoneCota(Long idTelefone, Long idCota);

    ResultadoCurvaABCCotaDTO obterCurvaABCCotaTotal(
            FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);

    List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro);

    List<Cota> obterCotasPorIDS(List<Long> idsCotas);

    /**
     * Retorna todos os ids das cotas que estão entre o itervalo de cotas
     * parametrizado;
     * 
     * @param intervaloCota
     *            intevalo de id das cotas
     * @param intervaloBox
     *            TODO
     * @param situacoesCadastro
     *            TODO
     * @param sortName TODO
     * @param sortOrder TODO
     * @param maxResults TODO
     * @param page TODO
     * @return ids das cotas
     */
    Set<Long> obterIdCotasEntre(Intervalo<Integer> intervaloCota,
            Intervalo<Integer> intervaloBox, List<SituacaoCadastro> situacoesCadastro, Long idRoteiro, Long idRota, String sortName, String sortOrder, Integer maxResults, Integer page);
    
    /**
     * Retorna todos os ids das cotas que estão entre o itervalo de cotas e possuam Notas de Envio
     * 
     * @param intervaloCota
     * @param intervaloBox
     * @param listIdsFornecedores TODO
     * @param situacao
     * @param idRoteiro
     * @param idRota
     * @param sortName
     * @param sortOrder
     * @param maxResults
     * @param page
     * @return
     */
    Set<Long> obterIdsCotasComNotaEnvioEntre(FiltroConsultaNotaEnvioDTO filtro);

    Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro);
    
    List<Cota> obterCotas(SituacaoCadastro situacaoCadastro);

    /**
     * Obtém cotas relacionadas a um fornecedor.
     * 
     * @param idFornecedor
     *            - id do fornecedor
     * 
     * @return {@link Set} de {@link Cota}
     */
    Set<Cota> obterCotasPorFornecedor(Long idFornecedor);

    List<CotaTipoDTO> obterCotaPorTipo(
            TipoCaracteristicaSegmentacaoPDV tipoCota, Integer page,
            Integer rp, String sortname, String sortorder);

    int obterCountCotaPorTipo(TipoCaracteristicaSegmentacaoPDV tipoCota);

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
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico
     * @return {@link HistoricoTitularidadeCota} que correspopnde aos
     *         identificadores recebidos ou null caso não seja encontrado
     * @throws IllegalArgumentException
     *             se os identificadores recebidos forem nulos
     * 
     */
    HistoricoTitularidadeCota obterHistoricoTitularidade(Long idCota, Long idHistorico);
    
    
    /**
     * Recupera a forma de pagamento do histórico de titularidade da cota pelo
     * indentificador
     * 
     * @param idFormaPagto
     *            identificador da forma de pagamento do histórico de
     *            titularidade da cota
     * @return forma de pagamento do histórico de titularidade da cota com o
     *         identificador recebido
     */
    HistoricoTitularidadeCotaFormaPagamento obterFormaPagamentoHistoricoTitularidade(Long idFormaPagto);
    
    /**
     * Recupera o sócio do histórico de titularidade da cota
     * 
     * @param idSocio
     *            identificador do sócio
     * @return sócio do histórico de titularidade da cota
     */
    HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidade(Long idSocio);

	void ativarCota(Integer numeroCota);
	
	List<Cota> obterCotasComInicioAtividadeEm(Date dataInicioAtividade);
	
	List<Cota> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte);
	
	List<Cota> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe);
	
	Cota obterPorPDV(Long idPDV);
	
	Cota buscarCotaPorID(Long id);

}