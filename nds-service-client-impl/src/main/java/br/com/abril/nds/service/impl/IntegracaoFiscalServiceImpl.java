package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.repository.IntegracaoFiscalRepository;
import br.com.abril.nds.service.IntegracaoFiscalService;

@Service
public class IntegracaoFiscalServiceImpl implements IntegracaoFiscalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotaFiscalServiceImpl.class);
	
	@Autowired
	private IntegracaoFiscalRepository integracaoFiscalRepository;
	
	@Override
	public File gerarArquivoP7(Date time) {
		
		List<ExtratoEdicaoArquivoP7DTO> p7dto = integracaoFiscalRepository.inventarioP7(time);
		
		File f = new File("arquivoP7.txt");
		BufferedWriter bw = null;
		try {
			bw=new BufferedWriter(new FileWriter(f));
			FTFParser ftfParser =  new FTFParser();
			
			for (ExtratoEdicaoArquivoP7DTO dto : p7dto) {
				ftfParser.parseFTF(dto, bw);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			LOGGER.debug("Erro ao acessar o arquivo!", e);
		} catch (Exception e) {
			LOGGER.debug("Erro na geração do arquivo P7!", e);
		}
		
		return f;
		
	}

	public Integer countGeracaoArquivoP7(Date time) {

		Integer p7dto = integracaoFiscalRepository.countInventarioP7(time);
		
		return p7dto;
	}

	@Override
	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time) {
		return integracaoFiscalRepository.inventarioP7(time);
	}

}