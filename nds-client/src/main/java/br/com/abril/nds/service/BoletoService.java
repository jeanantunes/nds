package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import java.io.IOException;
import java.util.List;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.util.BoletoImpressao;

//import br.com.abril.nds.model.cadastro.Boleto;

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
	
	void baixarBoleto(PagamentoDTO pagamentoDTO);
	
	byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException;
	
}
