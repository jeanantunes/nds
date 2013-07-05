package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.FornecedoresBandeiraDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referentes a serviços de chamada encalhe. 
 *   
 * @author Discover Technology
 */

@Service
public class ChamadaEncalheServiceImpl implements ChamadaEncalheService {
	
	
	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;

	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final Integer CODIGO_DINAP_INTERFACE = 9999999;
	private static final Integer CODIGO_FC_INTERFACE = 9999998;
	
	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		List<CotaEmissaoDTO> listaChamadaEncalhe = chamadaEncalheRepository.obterDadosEmissaoChamadasEncalhe(filtro);
		
		if (listaChamadaEncalhe==null){
			
			return null;
		}
		
		for(int i = 0; i < listaChamadaEncalhe.size(); i++){
			
			if(listaChamadaEncalhe.get(i).getQtdeExemplares() <= 0){
				
				listaChamadaEncalhe.remove(i);
			}
		}
		return listaChamadaEncalhe;
	}

    /**
     * Obtem endereço do PDV principal da Cota, caso não encontre, obtem o endereço principal da Cota.
     * @param cota
     * @return Endereco
     */
	private Endereco obterEnderecoImpressaoCE(Cota cota){
		
		Endereco endereco = null;
		
		PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());

		EnderecoPDV enderecoPdv = pdvPrincipal!=null?pdvPrincipal.getEnderecoEntrega():null;
		
		if (enderecoPdv == null) {
		
			for (EnderecoPDV ePdv : pdvPrincipal.getEnderecos()){
			    
				if (ePdv.isPrincipal()){
				    
					enderecoPdv = ePdv;
				}
			}
		}

		if (enderecoPdv != null) {
			
			return enderecoPdv.getEndereco();
		}
		
		for(EnderecoCota enderecoCota : cota.getEnderecos()){
			
			if (enderecoCota.isPrincipal()){
				
				endereco = enderecoCota.getEndereco();
				
				break;
			}
		}
		
		return endereco;
	}
	
	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) {
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		//verificar essa lista para ver a quiantidade de exemplares
		Cota cota = null;
		
		for(CotaEmissaoDTO dto:lista) {
			
			cota = cotaRepository.obterPorNumerDaCota( dto.getNumCota());

			Endereco endereco = this.obterEnderecoImpressaoCE(cota);

			if( endereco!= null) {
				dto.setEndereco( (endereco.getTipoLogradouro()!= null?endereco.getTipoLogradouro().toUpperCase() + ": " :"")
									+ endereco.getLogradouro().toUpperCase()  + ", " + endereco.getNumero());
				dto.setUf(endereco.getUf());
				dto.setCidade(endereco.getCidade());
				dto.setUf(endereco.getUf());
				dto.setCep(endereco.getCep());
			}
			
			if(cota.getPessoa() instanceof PessoaJuridica)
				dto.setInscricaoEstadual(((PessoaJuridica)cota.getPessoa()).getInscricaoEstadual());
			
			
			dto.setNumeroNome(dto.getNumCota()+ " " + ((dto.getNomeCota()!= null)?dto.getNomeCota().toUpperCase():""));
		
			if(cota.getPessoa() instanceof PessoaJuridica){
				dto.setCnpj(Util.adicionarMascaraCNPJ(cota.getPessoa().getDocumento()));
			}else{
				dto.setCnpj(Util.adicionarMascaraCPF(cota.getPessoa().getDocumento()));
			}
												
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));
			
			dto.setProdutos(chamadaEncalheRepository.obterProdutosEmissaoCE(filtro,dto.getIdCota()));
			
			BigDecimal vlrReparte = BigDecimal.ZERO;	
			BigDecimal vlrDesconto = BigDecimal.ZERO;
			BigDecimal vlrEncalhe = BigDecimal.ZERO;	
			
			for(ProdutoEmissaoDTO produtoDTO : dto.getProdutos()) {
				
				produtoDTO.setReparte( (produtoDTO.getReparte()==null) ? BigInteger.ZERO : produtoDTO.getReparte());
				
				produtoDTO.setVlrDesconto( (produtoDTO.getVlrDesconto() == null) ? BigDecimal.ZERO :  produtoDTO.getVlrDesconto());
				
				produtoDTO.setQuantidadeDevolvida(  (produtoDTO.getQuantidadeDevolvida() == null) ? BigInteger.ZERO : produtoDTO.getQuantidadeDevolvida());
				
				produtoDTO.setVendido( produtoDTO.getReparte().subtract(produtoDTO.getQuantidadeDevolvida()));
				
				produtoDTO.setVlrVendido(CurrencyUtil.formatarValor(produtoDTO.getVlrPrecoComDesconto().multiply(BigDecimal.valueOf(produtoDTO.getVendido().longValue()))));
				
				vlrReparte = vlrReparte.add( produtoDTO.getPrecoVenda().multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));

				vlrDesconto = vlrDesconto.add(produtoDTO.getPrecoVenda().subtract(produtoDTO.getVlrPrecoComDesconto())
						.multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));
				
				vlrEncalhe = vlrEncalhe.add( vlrDesconto.multiply( BigDecimal.valueOf( produtoDTO.getQuantidadeDevolvida().longValue()) ));
				
				
			}
			
			BigDecimal vlrReparteLiquido = vlrReparte.subtract(vlrDesconto);
			
			BigDecimal totalLiquido = vlrReparteLiquido.subtract(vlrEncalhe);
			
			dto.setVlrReparte(CurrencyUtil.formatarValor(vlrReparte));
			dto.setVlrComDesconto(CurrencyUtil.formatarValor(vlrDesconto));
			dto.setVlrReparteLiquido(CurrencyUtil.formatarValor(vlrReparteLiquido));
			dto.setVlrEncalhe(CurrencyUtil.formatarValor(vlrEncalhe));
			dto.setVlrTotalLiquido(CurrencyUtil.formatarValor(totalLiquido));			
		}
		
		
		for(CotaEmissaoDTO cotasEmissao : lista) {
			
			List<ProdutoEmissaoDTO> listaProdutos = cotasEmissao.getProdutos();
			
			for(int i = 0; i < listaProdutos.size(); i++ ) {
				
				if(listaProdutos.get(i).getVendido().intValue() <= 0) {
					listaProdutos.remove(i);
				}
			}
		}
		
		
		return lista;
	}

	@Override
	@Transactional
	public List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dtRecolhimentoDe,
			Date dtRecolhimentoAte) {
		
		return chamadaEncalheRepository.obterIdsCapasChamadaEncalhe(dtRecolhimentoDe, dtRecolhimentoAte);
	}
	
	@Override
	@Transactional
	public List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, PaginacaoVO paginacaoVO) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.obterBandeirasNoIntervalo(periodoRecolhimento, paginacaoVO);
	}
	
	@Override
	@Transactional
	public Long countObterBandeirasDaSemana(Integer semana) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.countObterBandeirasNoIntervalo(periodoRecolhimento);
	}
	
	@Override
	@Transactional
	public List<FornecedoresBandeiraDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		List<FornecedoresBandeiraDTO>  fornecedores = chamadaEncalheRepository.obterDadosFornecedoresParaImpressaoBandeira(periodoRecolhimento);
		
		for(FornecedoresBandeiraDTO dto: fornecedores) {
			
			dto.setPraca(this.distribuidorService.cidadeDistribuidor());
			
			if(dto.getCodigoInterface().equals(CODIGO_DINAP_INTERFACE))
				dto.setCodigoPracaNoProdin(this.distribuidorService.codigoDistribuidorDinap());
			
			else if(dto.getCodigoInterface().equals(CODIGO_FC_INTERFACE))				
				dto.setCodigoPracaNoProdin(this.distribuidorService.codigoDistribuidorFC());
			
			dto.setSemana(semana);
		}
		
		return fornecedores;
	}
}