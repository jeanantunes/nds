package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;

public interface NotaFiscalService {

	List<NotaFiscal> buscarNotaFiscal(Date emissao, Date entradaRecebimento,Long valorBruto, Long valorLiquido, Long valorDescontado, String tipoNota, String cfop);
	void inserirNotaFiscal(NotaFiscal notaFiscal);
}
