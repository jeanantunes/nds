package br.com.abril.nds.service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaFuroDTO;

public interface FuroProdutoService {

	void validarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario);
	
	boolean verificarProdutoExpedido(Long idLancamento);
	
	void efetuarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario);
	
	/**
	 * Verifica se Data não é feriado e é dia de operação do Distribuidor
	 * 
	 * @param codigoProduto
	 * @param idProdutoEdicao
	 * @param c
	 * @return boolean
	 */
	boolean isDiaOperante(String codigoProduto, Long idProdutoEdicao, Calendar c);
	
	/**
     * Obtem a proxima data, considerando Feriados e Dia de Operação do Distribuidor
     * 
     * @param codigoProduto
     * @param idProdutoEdicao
     * @param data
     * @return Date
     */
    public Date obterProximaDataDiaOperante(String codigoProduto, Long idProdutoEdicao, Date data);

    List<CotaFuroDTO> obterCobrancaRealizadaParaCotaVista(Long idProdutoEdicao, Date dataFuro, Long idLancamento);
}
