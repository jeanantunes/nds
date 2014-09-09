package br.com.abril.nds.service.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;

@Component
public class CobrancaFornecedorValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CobrancaFornecedorValidator.class);
	
    @Autowired
    private ParametroCobrancaCotaService parametroCobrancaCotaService;
    
    @Autowired
    private DescontoDistribuidorRepository descontoDistribuidorRepository;
    
    @Autowired
    private DescontoCotaRepository descontoCotaRepository;
    
    @Autowired
    private DescontoProdutoRepository descontoProdutoRepository;
    
    @Autowired
    private DescontoService descontoService;  
    
    @Autowired
    private CotaRepository cotaRepository;
    
    public Validate filter(Map<String, DescontoDTO> descontos, Cota cota, Fornecedor fornecedor, Produto produto) {

    	return new Validate(descontos, cota, fornecedor, produto);
    }

    public class Validate {
    	
    	private Cota cota;
    	private Fornecedor fornecedor;
    	private Produto produto;
    	private Map<String, DescontoDTO> descontos;

    	public Validate(Map<String, DescontoDTO> descontos, Cota cota, Fornecedor fornecedor, Produto produto) {
    		this.descontos = descontos;
    		this.cota = cota;
    		this.fornecedor = fornecedor;
    		this.produto = produto;
    	}
    	
    	private boolean hasDescontoGeral() {
    		
    		if (this.fornecedor == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoDTO filtro = new FiltroTipoDescontoDTO();
    		
    		filtro.setIdFornecedores(Arrays.asList(this.fornecedor.getId()));
    		
    		//List<TipoDescontoDTO> desconto = CobrancaFornecedorValidator.this.descontoDistribuidorRepository.buscarDescontos(filtro);
    		//return desconto != null && !desconto.isEmpty();
    		
    		try {
    			
    			DescontoDTO desc = descontoService.obterDescontoPor(this.descontos, this.cota.getId(), this.fornecedor.getId(), null, null);
    			return desc != null;
    			
			} catch (Exception e) {
				
				LOGGER.error("Erro ao obter desconto para validar Desconto Geral da Cota.");
			}
    		
    		return false;
    	}
    	
    	private boolean hasDescontoPorCota() {
    		
    		if (this.cota == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
    		
    		filtro.setIdCota(this.cota.getId());
    		
    		//List<TipoDescontoCotaDTO> desconto = CobrancaFornecedorValidator.this.descontoCotaRepository.obterDescontoCota(filtro);    		
    		//return desconto != null && !desconto.isEmpty();
    		
    		try {
    			
    			DescontoDTO desc = descontoService.obterDescontoPor(this.descontos, this.cota.getId(), this.fornecedor.getId(), null, null);
    			return desc != null;
    			
			} catch (Exception e) {
				
				LOGGER.error("Erro ao obter desconto para validar Desconto da Cota.");
			}
    		
    		return false;
    	}
    	
    	private boolean hasDescontoPorProduto() {

    		if (this.produto == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
    		
    		filtro.setCodigoProduto(this.produto.getCodigo());
    		
    		//List<TipoDescontoProdutoDTO> desconto = CobrancaFornecedorValidator.this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
    		//return desconto != null && !desconto.isEmpty();
    		
    		try {
    			
    			DescontoDTO desc = descontoService.obterDescontoPor(this.descontos, this.cota.getId(), this.fornecedor.getId(), this.produto.getId(), null);
    			return desc != null;
    			
			} catch (Exception e) {
				
				LOGGER.error("Erro ao obter desconto para validar Desconto do Produto.");
			}
    		
    		return false;
    	}

    	@Transactional
        public void validate() {

        	List<FormaCobrancaDTO> formasCobrancaCota = 
        			CobrancaFornecedorValidator.this.parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(this.cota.getId());

        	boolean fornecedorValido = false;

        	for (FormaCobrancaDTO formaCobranca : formasCobrancaCota) {

        		if (formaCobranca.getFornecedores() != null && formaCobranca.getFornecedores().contains(this.fornecedor)) {

           			fornecedorValido = true;
           			
           			if (this.produto == null) {
           				break;
           			}
           			
           			if (this.descontos != null) {
	           			try {
							if (CobrancaFornecedorValidator.this.descontoService.obterDescontoPor(
								this.descontos, this.cota.getId(), this.fornecedor.getId(), 
									this.produto.getId(), null) != null) {
								
								break;
							}
						} catch (Exception e) {
							
							LOGGER.error("Erro ao obter desconto para validar Desconto do Produto.", e);
						}
           			} 
           			
           			//else {
        			
           				LOGGER.error("Erro ao obter desconto para validar Desconto do Produto/Cota/Fornecedor.");
           				
           				/*
           				if (this.hasDescontoGeral()) {
           					break;
           				} else if (this.hasDescontoPorCota()) {
           					break;
           				} else if (this.hasDescontoPorProduto()) {
           					break;
           				}
           				*/
           			//}

        			throw new ValidacaoException(
        				TipoMensagem.WARNING, 
        					String.format("Desconto não encontrado para o produto [Cod.: %s] / [Cota: %s]", 
        						this.produto.getCodigo(), this.cota != null ? this.cota.getNumeroCota() : ""));
        		}
        	}

        	if (!fornecedorValido) {

        		Cota cota = CobrancaFornecedorValidator.this.cotaRepository.buscarPorId(this.cota.getId());

        		throw new ValidacaoException(TipoMensagem.WARNING, 
        				String.format("Não existem formas de cobrança cadastradas para o fornecedor %s na cota %s", 
        						this.fornecedor.getJuridica().getNomeFantasia(),
        						cota.getNumeroCota()));
        	}
        }
    }
}
