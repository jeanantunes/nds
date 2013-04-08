package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesReparteTotalEPromocionalDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface InformacoesProdutoService {
	List<TipoClassificacaoProduto> buscarClassificacao();
	
	List<InformacoesProdutoDTO> buscarProduto (FiltroInformacoesProdutoDTO filtro);
	
	List<EdicaoBaseEstudoDTO> buscarBases (Long idEstudo);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	
	List<InfoProdutosItemRegiaoEspecificaDTO> buscarItemRegiao ();
	
	List<ProdutoBaseSugeridaDTO> buscarBaseSugerida(Long idEstudo);
	
	InformacoesAbrangenciaEMinimoProdDTO buscarAbrangenciaEMinimo (Long idEstudo);
	
	BigDecimal buscarAbrangenciaApurada(String codProduto, Long numEdicao);
	
	InformacoesReparteTotalEPromocionalDTO buscarReparteTotalEPromocional (String codProduto, Long numEdicao);
	
	BigInteger buscarSobra (Long idEstudo);
	
	BigInteger buscarTotalVenda (String codProduto, Long numEdicao);
	
	InformacoesVendaEPerceDeVendaDTO buscarVendas (String codProduto, Long numEdicao);
	
	BigInteger obterReparteDistribuido (String codProduto);
}
