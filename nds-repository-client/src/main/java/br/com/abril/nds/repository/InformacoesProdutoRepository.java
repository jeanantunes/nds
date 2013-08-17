package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesReparteTotalEPromocionalDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;

public interface InformacoesProdutoRepository extends Repository<InformacoesProdutoDTO, Long> {

	List<InformacoesProdutoDTO> buscarProdutos (FiltroInformacoesProdutoDTO dto);
	
	List<InfoProdutosItemRegiaoEspecificaDTO> buscarItensRegiao (Long idEstudo);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	
	InformacoesAbrangenciaEMinimoProdDTO buscarAbrangenciaEMinimo (Long estudoId);
	
	BigDecimal buscarAbrangenciaApurada (String codProduto, Long numEdicao);
	
	InformacoesReparteTotalEPromocionalDTO buscarRepartes (String codProduto, Long numEdicao);
	
	BigInteger buscarReparteDaEdica_Sobra (Long estudoID);
	
	BigInteger buscarVendaTotal	(String codProduto, Long numEdicao);
	
	InformacoesVendaEPerceDeVendaDTO buscarVendas (String codProduto, Long numEdicao);
}
