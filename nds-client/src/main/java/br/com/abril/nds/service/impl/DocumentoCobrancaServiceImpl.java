package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class DocumentoCobrancaServiceImpl implements DocumentoCobrancaService {

	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Override
	public File gerarDocumentoCobranca(String nossoNumero) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		try {
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:
					File arquivo = new File("_boleto");
					FileOutputStream out = new FileOutputStream(arquivo);
					out.write(this.boletoService.gerarImpressaoBoleto(nossoNumero));
					out.flush();
					out.close();
					
					return arquivo;
				
				case CHEQUE:
					return null;

				case DEPOSITO:
					return null;
				
				case DINHEIRO:
					return null;

				case TRANSFERENCIA_BANCARIA:
					return null;
				
				default:
					return null;
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar arquivo de cobrança para nosso número: " + nossoNumero + " - " + e.getMessage());
		}
	}

}