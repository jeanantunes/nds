package br.com.abril.nds.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.TipoBaixaCobranca;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 */
public interface BoletoService {
    
	List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	Boleto obterBoletoPorNossoNumero(String nossoNumero, Boolean dividaAcumulada);

	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);

	ResumoBaixaBoletosDTO obterResumoBaixaFinanceiraBoletos(Date data);
	
	void baixarBoletosAutomatico(ArquivoPagamentoBancoDTO arquivoPagamento,
					   			 BigDecimal valorFinanceiro, Usuario usuario);
	
	void baixarBoleto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento, Usuario usuario,
			 		  String nomeArquivo, PoliticaCobranca politicaCobranca, Distribuidor distribuidor,
			 		  Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException;

	void enviarBoletoEmail(String nossoNumero);
	
	CobrancaVO obterDadosBoletoPorNossoNumero(String nossoNumero);
	
	byte[] gerarImpressaoBoletos(List<String> nossoNumeros) throws IOException;
	
	void incrementarVia(String... nossoNumero);
	
	/**
	 * Obtém os boletos que foram baixados com divergência por data e/ou valor em determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos baixados com divergência de data e/ou valor.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro);
	
    /**
	 * Obtém os boletos que foram rejeitados na baixa em determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos rejeitados.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém os boletos previstos para baixa a partir de determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos previstos.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro);
    
    /**
	 * Obtém lista de Inadimplentes por data de vencimento
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	public List<DetalheBaixaBoletoDTO> obterInadimplentesPorData(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém lista de Baixados por data de vencimento
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	public List<DetalheBaixaBoletoDTO> obterBaixadosPorData(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de Inadimplentes por data de vencimento
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeInadimplentesPorData(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém quantidade de Baixados por data de vencimento
	 * 
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeBaixadosPorData(FiltroDetalheBaixaBoletoDTO filtro);

}
