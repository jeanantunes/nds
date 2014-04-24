package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.dto.IntegracaoFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaIntegracaoFiscal;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.repository.IntegracaoFiscalRepository;
import br.com.abril.nds.service.IntegracaoFiscalService;

@Service
public class IntegracaoFiscalServiceImpl implements IntegracaoFiscalService {

	@Autowired
	private IntegracaoFiscalRepository integracaoFiscalRepository;
	
	@Override
	@Transactional
	public List<IntegracaoFiscalDTO> pesquisarPorMesAno(FiltroConsultaIntegracaoFiscal filtro) {
		return  integracaoFiscalRepository.pesquisarPorMesAno(filtro);
	}

	@Override
	public List<IntegracaoFiscalDTO> obterFixacoesRepartePorProduto(
			FiltroConsultaIntegracaoFiscal filtro) {
		
		integracaoFiscalRepository.obterFixacoesRepartePorProduto(filtro);
		return null;
	}

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
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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


