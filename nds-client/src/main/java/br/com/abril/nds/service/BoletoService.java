package br.com.abril.nds.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
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

	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);

	ResumoBaixaBoletosDTO baixarBoletosAutomatico(ArquivoPagamentoBancoDTO arquivoPagamento,
					   							  BigDecimal valorFinanceiro, Usuario usuario);
	
	void baixarBoleto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento, Usuario usuario,
			 		  String nomeArquivo, PoliticaCobranca politicaCobranca, Distribuidor distribuidor,
			 		  Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException;

	void enviarBoletoEmail(String nossoNumero);
	
	CobrancaVO obterDadosCobranca(String nossoNumero);
	
	byte[] gerarImpressaoBoletos(List<String> nossoNumeros) throws IOException;
	
}
