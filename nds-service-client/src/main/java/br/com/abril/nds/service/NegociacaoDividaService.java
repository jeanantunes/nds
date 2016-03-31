package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.ConsultaNegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacoesDTO;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.TipoNegociacao;
import br.com.abril.nds.model.seguranca.Usuario;


public interface NegociacaoDividaService {

	NegociacaoDividaPaginacaoDTO obterDividasPorCotaPaginado(FiltroConsultaNegociacaoDivida filtro);

	List<NegociacaoDividaDTO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro);
	
	Long criarNegociacao(Integer numeroCota, List<ParcelaNegociacao> parcelas, TipoNegociacao tipoNegociacao, BigDecimal valorDividaPagaComissao, 
			List<Long> idsCobrancasOriginarias, Usuario usuarioResponsavel, boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos, FormaCobranca formaCobranca, Long idBanco);
	
	Negociacao obterNegociacaoPorId(Long idNegociacao);
	
	List<byte[]> gerarBoletosNegociacao(Long idNegociacao);
	
	List<byte[]> imprimirRecibos(final Long idNegociacao);
	
	byte[] imprimirNegociacao(Long idNegociacao, String valorDividaSelecionada) throws Exception;
	
	List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca);

	public abstract List<CalculaParcelasVO> calcularParcelas(FiltroCalculaParcelas filtro);

	public abstract List<CalculaParcelasVO> recalcularParcelas(FiltroCalculaParcelas filtro, List<CalculaParcelasVO> parcelas);

	/**
	 * Obtem quantidade de parcelas para que o valor das parcelas esteja de acordo com o valor mínimo dos parametros de cobrança
	 * @param filtro
	 * @return Integer
	 */
	Integer obterQuantidadeParcelasConformeValorMinimo(FiltroCalculaParcelas filtro);
	
	void abaterNegociacaoPorComissao(Long idCota, BigDecimal valorTotalReparte, BigDecimal valorTotalEncalhe, Usuario usuario);

    void verificarAtivacaoCotaAposPgtoParcela(Cobranca cobranca, Usuario usuario);

	List<ConsultaNegociacaoDividaDTO> buscarNegociacoesDividas(FiltroConsultaNegociacoesDTO filtro);
}