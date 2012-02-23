package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;

/**
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}  
 * 
 * @author william.machado
 *
 */
public interface NotaFiscalRepository extends Repository<NotaFiscal, Long> {

	public List<NotaFiscal> buscarNotaFiscal(Date emissao, Date entradaRecebimento,Long valorBruto, Long valorLiquido, Long valorDescontado, String tipoNota, String cfop);
	void inserirNotaFiscal(NotaFiscal notaFiscal);
	
}
