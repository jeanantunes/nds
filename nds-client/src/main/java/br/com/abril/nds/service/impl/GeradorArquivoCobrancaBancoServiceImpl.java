package br.com.abril.nds.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.export.cnab.cobranca.Header;
import br.com.abril.nds.export.cnab.cobranca.Trailer;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

@Service
public class GeradorArquivoCobrancaBancoServiceImpl implements GeradorArquivoCobrancaBancoService {

	@Override
	public void gerarArquivoCobranca(Map<Banco, List<DetalheSegmentoP>> mapaArquivoCobranca) {
		
		FixedFormatManager manager = new FixedFormatManagerImpl();
		
		Header header = new Header();
		
		header.setCodigoBanco(11L);
		header.setLote(22L);
		
		String retornoHeader = manager.export(header);
		
		Trailer trailer = new Trailer();
		
		trailer.setCodigoBanco(3L);
		
		String retornoTrailer = manager.export(trailer);
	}
	
}
