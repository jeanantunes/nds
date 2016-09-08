package br.com.abril.nds.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.ValidationException;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.BoletoCotaDTO;
import br.com.abril.nds.dto.BoletoEmBrancoDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroBoletoAvulsoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.TipoBaixaCobranca;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 */
public interface BoletoService {
    
	byte[] gerarImpressaoBoletosDistribuidor(List<BoletoDistribuidor> listaBoletoDistribuidor) throws IOException, ValidationException;
	
	List<BoletoCotaDTO> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	Boleto obterBoletoPorNossoNumero(String nossoNumero, Boolean dividaAcumulada);

	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);

	ResumoBaixaBoletosDTO obterResumoBaixaFinanceiraBoletos(Date data);
	
	void baixarBoletosAutomatico(ArquivoPagamentoBancoDTO arquivoPagamento,
					   			 BigDecimal valorFinanceiro, Usuario usuario, Date dataPagamento);
	
	void baixarBoleto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento, Usuario usuario,
			 		  String nomeArquivo,  
			 		  Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos,
			 		  Banco banco, Date dataPagamento);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException, ValidationException;
	
	byte[] gerarImpressaoBoleto(Boleto boleto, List<PoliticaCobranca> politicasCobranca) throws IOException, ValidationException;

	void enviarBoletoEmail(String nossoNumero);
	
	CobrancaVO obterDadosBoletoPorNossoNumero(String nossoNumero, Date dataPagamento);
	
	byte[] gerarImpressaoBoletos(Collection<String> nossoNumeros) throws IOException, ValidationException;
	
	void incrementarVia(String... nossoNumero);
	
	/**
	 * Obtém os boletos previstos para baixa a partir de determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos previstos.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém lista de boleto baixados por data de vencimento.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém os boletos que foram rejeitados na baixa em determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos rejeitados.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém os boletos que foram baixados com divergência por data e/ou valor em determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos baixados com divergência de data e/ou valor.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém lista de boletos inadimplentes por data de vencimento.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	public List<DetalheBaixaBoletoDTO> obterBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém os valores totais de boletos baixados de cada banco a partir de determinada data.
	 * 
	 * @param filtro - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Total bancário.
	 */
	List<DetalheBaixaBoletoDTO> obterTotalBancario(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de boletos previstos.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de boletos baixados.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de boletos rejeitados.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de boletos baixados com divergência.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de boletos inadimplentes.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de totais bancários.
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeTotalBancario(FiltroDetalheBaixaBoletoDTO filtro);

	public List<BoletoCotaDTO> verificaEnvioDeEmail(List<BoletoCotaDTO> boletosDTO);

	/**
	 * Gera movimentos para a próxima data, a partir dos boletos não pagos no dia.
	 * 
	 * @param dataPagamento
	 * 
	 * @param usuario
	 */
	void adiarDividaBoletosNaoPagos(Usuario usuario, Date dataPagamento);
	
	/**
	 * Obtem dados de boleto em Branco utilizando dados de CE e periodo de recolhimento do filtro
	 * @param ceDTO
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @return BoletoEmBrancoDTO
	 */
	BoletoEmBrancoDTO obterDadosBoletoEmBrancoPorCE(CotaEmissaoDTO ceDTO,Date dataRecolhimentoCEDe, Date dataRecolhimentoCEAte);
	
	/**
	 * Método responsável por gerar impressao de Boleto Antecipado (Em Branco) em formato PDF
	 * @param nossoNumero
	 * @return b: Boleto PDF em Array de bytes
	 * @throws IOException
	 * @throws ValidationException 
	 */
	byte[] gerarImpressaoBoletoEmBranco(String nossoNumero) throws IOException, ValidationException;

	/**
	 * Gera Impressão de Boletos em Branco apenas para a impressão -  Sem Cobrança e Sem Financeiro Cadastrado
	 * @param boletosEmBrancoDTO
	 * @return byte[]
	 * @throws ValidationException 
	 */
	byte[] geraImpressaoBoletosEmBranco(List<BoletoEmBrancoDTO> boletosEmBrancoDTO) throws ValidationException;

	/**
	 * Salva Boleto Antecipado - Em Branco
	 * @param bbDTO
	 */
	void salvaBoletoAntecipado(BoletoEmBrancoDTO bbDTO);

	/**
     * Salva Boletos Antecipados - Em Branco
     * @param listaBbDTO
     */
	void salvaBoletosAntecipado(List<BoletoEmBrancoDTO> listaBbDTO);

	/**
	 * Verifica se existe boleto antecipado para a cota
	 * Data de recolhimento dentro do periodo de emissao CE do Boleto antecipado
	 * Boletos em Branco sem reimpressão
	 * @param idCota
	 * @param dataRecolhimento
	 * @return boolean
	 */
	boolean existeBoletoAntecipadoCotaDataRecolhimento(Long idCota,Date dataRecolhimento);

	/**
	 * Verifica se existe Boleto Antecipado emitido para a faixa de cotas no periodo de recolhimento
	 * @param numeroCotaDe
	 * @param numeroCotaAte
	 * @param dataRecolhimentoDe
	 * @param dataRecolhimentoAte
	 * @return boolean
	 */
	boolean existeBoletoAntecipadoPeriodoRecolhimentoECota(Integer numeroCotaDe, 
			                                               Integer numeroCotaAte,
			                                               Date dataRecolhimentoDe, 
			                                               Date dataRecolhimentoAte);
	/**
	 * Método responsável por obter boleto Antecipado (Em Branco) por nossoNumero
	 * @param nossoNumero
	 * @return Boletos encontrado
	 */
	BoletoAntecipado obterBoletoEmBrancoPorNossoNumero(String nossoNumero);
	
	BoletoAntecipado obterBoletoEmBrancoPorId(Long idBoletoAntecipado);
	
	byte[] gerarImpressaoBoleto(final Boleto boleto, Pessoa cedente, boolean aceitaPagamentoVencido,
            final List<PoliticaCobranca> politicasCobranca) throws IOException, ValidationException;
	
	byte[] gerarArquivo(final FiltroDividaGeradaDTO filtro) throws Exception, ValidationException;
	
	List<BoletoAvulsoDTO> obterDadosBoletoAvulso(final FiltroBoletoAvulsoDTO boletoAvulso);
	
}
