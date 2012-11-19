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
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;
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
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final Integer CODIGO_DINAP_INTERFACE = 9999999;
	private static final Integer CODIGO_FC_INTERFACE = 9999998;
	
	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		
		List<CotaEmissaoDTO> chamadasEncalheCota = chamadaEncalheRepository.obterDadosEmissaoChamadasEncalhe(filtro);
		
		return chamadasEncalheCota;
	}


	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) {
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		
		Cota cota = null;
		
		for(CotaEmissaoDTO dto:lista) {
			
			cota = cotaRepository.obterPorNumerDaCota( dto.getNumCota());
			
			Endereco endereco = null;
			
			for(EnderecoCota enderecoCota : cota.getEnderecos()){
				
				if (enderecoCota.isPrincipal()){
					endereco = enderecoCota.getEndereco();
					break;
				}
			}
			
			if( endereco!= null) {
				dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
				dto.setUf(endereco.getUf());
				dto.setCidade(endereco.getCidade());
				dto.setUf(endereco.getUf());
				dto.setCep(endereco.getCep());
			}
			
			if(cota.getPessoa() instanceof PessoaJuridica)
				dto.setInscricaoEstadual(((PessoaJuridica)cota.getPessoa()).getInscricaoEstadual());
			
			
			dto.setNumeroNome(dto.getNumCota()+ " " + ((dto.getNomeCota()!= null)?dto.getNomeCota().toUpperCase():""));
			dto.setCnpj(cota.getPessoa().getDocumento());
									
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));
			
			dto.setProdutos(chamadaEncalheRepository.obterProdutosEmissaoCE(filtro,dto.getIdCota()));
			
			Double vlrReparte = 0.0;	
			Double vlrDesconto = 0.0;
			Double vlrEncalhe = 0.0;	
			
			for(ProdutoEmissaoDTO produtoDTO : dto.getProdutos()) {
				
				produtoDTO.setReparte( (produtoDTO.getReparte()==null) ? BigInteger.ZERO : BigInteger.valueOf(produtoDTO.getReparte()));
				produtoDTO.setVlrDesconto( (produtoDTO.getVlrDesconto() == null) ? 0.0D :  produtoDTO.getVlrDesconto());
				
				
				produtoDTO.setVendido(produtoDTO.getReparte() - produtoDTO.getQuantidadeDevolvida());
				
				produtoDTO.setVlrVendido(CurrencyUtil.formatarValor(produtoDTO.getVendido() * produtoDTO.getVlrPrecoComDesconto()));
				
				vlrReparte += produtoDTO.getPrecoVenda() * produtoDTO.getReparte();
				
				vlrDesconto +=  produtoDTO.getVlrDesconto() / 100 * produtoDTO.getReparte();
				
				vlrEncalhe += produtoDTO.getQuantidadeDevolvida() * (produtoDTO.getPrecoVenda() - produtoDTO.getVlrDesconto());
				
			}
			
			Double vlrReparteLiquido = vlrReparte - vlrDesconto;
			
			Double totalLiquido = vlrReparteLiquido - vlrEncalhe;
			
			dto.setVlrReparte(CurrencyUtil.formatarValor(vlrReparte));
			
			dto.setVlrComDesconto(CurrencyUtil.formatarValor(vlrDesconto));
			
			dto.setVlrReparteLiquido(CurrencyUtil.formatarValor(vlrReparteLiquido));
			
			dto.setVlrEncalhe(CurrencyUtil.formatarValor(vlrEncalhe));
			
			dto.setVlrTotalLiquido(CurrencyUtil.formatarValor(totalLiquido));			
		}
		
		return lista;
	}

	private BigDecimal getZeroForNullValue(BigDecimal value) {
		return (value == null) ? BigDecimal.ZERO : value;
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
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(distribuidor, semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.obterBandeirasNoIntervalo(periodoRecolhimento, paginacaoVO);
	}
	
	@Override
	@Transactional
	public Long countObterBandeirasDaSemana(Integer semana) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(distribuidor, semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.countObterBandeirasNoIntervalo(periodoRecolhimento);
	}
	
	@Override
	@Transactional
	public List<FornecedoresBandeiraDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(distribuidor, semana, new Date());
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		List<FornecedoresBandeiraDTO>  fornecedores = chamadaEncalheRepository.obterDadosFornecedoresParaImpressaoBandeira(periodoRecolhimento);
		
		for(FornecedoresBandeiraDTO dto: fornecedores) {
			
			dto.setPraca(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
			
			if(dto.getCodigoInterface().equals(CODIGO_DINAP_INTERFACE))
				dto.setCodigoPracaNoProdin(distribuidor.getCodigoDistribuidorDinap());
			
			else if(dto.getCodigoInterface().equals(CODIGO_FC_INTERFACE))				
				dto.setCodigoPracaNoProdin(distribuidor.getCodigoDistribuidorFC());
			
			dto.setSemana(semana);
		}
		
		return fornecedores;
	}
	
}
