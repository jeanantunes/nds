package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.seguranca.Usuario;


public interface NegociacaoDividaService {

	NegociacaoDividaPaginacaoDTO obterDividasPorCotaPaginado(FiltroConsultaNegociacaoDivida filtro);

	List<NegociacaoDividaDTO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro);
	
	Long criarNegociacao(Integer numeroCota, List<ParcelaNegociacao> parcelas, BigDecimal valorDividaPagaComissao, 
			List<Long> idsCobrancasOriginarias, Usuario usuarioResponsavel, boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos, FormaCobranca formaCobranca, Long idBanco);
	
	Negociacao obterNegociacaoPorId(Long idNegociacao);
	
	List<byte[]> gerarBoletosNegociacao(Long idNegociacao);
}