package br.com.abril.nds.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;

import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.TipoBaixa;
import br.com.abril.nds.model.seguranca.Usuario;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoService {
    
	List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);

	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	void baixarBoletos(ArquivoPagamentoBancoDTO arquivoPagamento, Usuario usuario);

	
	void baixarBoleto(PagamentoDTO pagamento, Date dataOperacao, TipoBaixa tipoBaixa,
					  String nomeArquivo, PoliticaCobranca politicaCobranca);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException;

	
}
