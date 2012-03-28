package br.com.abril.nds.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.GeradorBoleto;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
public interface BoletoService {
    
	List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);

	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	Boleto obterBoletoPorNossoNumero(String nossoNumero);
	
	GeradorBoleto geraBoleto(String nossoNumero);
	
	String obterEmailCota(String nossoNumero);

	ResumoBaixaBoletosDTO baixarBoletos(ArquivoPagamentoBancoDTO arquivoPagamento,
					   					BigDecimal valorFinanceiro, Usuario usuario);
	
	void baixarBoleto(ResumoBaixaBoletosDTO resumoBaixaBoletos, PagamentoDTO pagamento,
					  Date dataOperacao, Usuario usuario, String nomeArquivo,
					  PoliticaCobranca politicaCobranca, Distribuidor distribuidor,
					  Date dataNovoMovimento);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException;
	File gerarAnexoBoleto(String nossoNumero) throws IOException;
	
	CobrancaVO obterDadosCobranca(String nossoNumero);
	
}
