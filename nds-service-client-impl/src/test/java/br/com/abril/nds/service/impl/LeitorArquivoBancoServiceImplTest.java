package br.com.abril.nds.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB;
import br.com.abril.nds.util.cnab.UtilitarioCNAB.TipoRegistroCNAB;

@RunWith( MockitoJUnitRunner.class )
public class LeitorArquivoBancoServiceImplTest {

	
	
	private File obterFileCnab() {
		
		try {
			File arquivoCnab = new File(this.getClass().getResource("/cnab_mock.dat").getPath());
			return arquivoCnab;
		} catch (Exception e) {
			throw new RuntimeException("Arquivo de registros não encontrado");
		}
		
	}
	
	
	private String obterTipoRegistroDeCNAB(String line) {
		
		if(line.length() == 400) {
			return PadraoCNAB.CNAB400.obterTipoRegistro(line);
		} else if (line.length() == 240) {
			return PadraoCNAB.CNAB240.obterTipoRegistro(line);
		} else {
			return "-";
		}
		
	}

	private Map<String, Integer> obterQuantidadePorTipoDeSegmentoCNAB240(File arquivoCNAB) {
		
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(arquivoCNAB);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Map<String, Integer> mapQtd = new HashMap<String, Integer>();
		
		for(String line : lines) {
			
			if(!PadraoCNAB.CNAB240.isDetalhe(line)){
				continue;
			}

			String tipoSegmento = PadraoCNAB.CNAB240.obterTipoSegmento(line);
			
			if(mapQtd.containsKey(tipoSegmento)) {
				Integer qtd = mapQtd.get(tipoSegmento);
				mapQtd.put(tipoSegmento, qtd++);
			} else {
				mapQtd.put(tipoSegmento, 1);
			}
		}
		
		return mapQtd;

	}
	
	private Map<String, Integer> obterQuantidadePorTipoRegistro(File arquivoCNAB) {

		List<String> lines = null;
		try {
			lines = FileUtils.readLines(arquivoCNAB);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Map<String, Integer> mapQtd = new HashMap<String, Integer>();
		
		for(String line : lines) {
			String tipoRegistro = obterTipoRegistroDeCNAB(line);
			if(mapQtd.containsKey(tipoRegistro)) {
				Integer qtd = mapQtd.get(tipoRegistro);
				mapQtd.put(tipoRegistro, qtd++);
			} else {
				mapQtd.put(tipoRegistro, 1);
			}
		}
		
		return mapQtd;
		
	}
	
	
	@Test
	public void test_obter_pagamentos_banco_cnab240() {
		
		Map<String, Integer> mapaQtdTipoRegistro = obterQuantidadePorTipoRegistro(obterFileCnab());
		
		Map<String, Integer> mapaQtdTipoSegmento = obterQuantidadePorTipoDeSegmentoCNAB240(obterFileCnab());
		
		LeitorArquivoBancoServiceImpl service = new LeitorArquivoBancoServiceImpl();
		
		ArquivoPagamentoBancoDTO arquivoPagamento = service.obterPagamentosBanco(obterFileCnab(), "cnab");
		
		Integer qtdDetalhesEncontrados =  mapaQtdTipoRegistro.get(TipoRegistroCNAB.TipoRegistroCNAB240.getDetalhe());
		
		Assert.assertTrue(arquivoPagamento.getListaPagemento().size() == qtdDetalhesEncontrados);
		
		
	}
	
	
	
	
}
