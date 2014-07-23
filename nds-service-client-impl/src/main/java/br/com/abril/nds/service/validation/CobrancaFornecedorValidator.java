package br.com.abril.nds.service.validation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.service.ParametroCobrancaCotaService;

@Component
public class CobrancaFornecedorValidator {

    @Autowired
    private ParametroCobrancaCotaService parametroCobrancaCotaService;
    
    @Autowired
    private DescontoDistribuidorRepository descontoDistribuidorRepository;
    
    @Autowired
    private DescontoCotaRepository descontoCotaRepository;
    
    @Autowired
    private DescontoProdutoRepository descontoProdutoRepository;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    public Validate filter(Long idCota, Fornecedor fornecedor, String codigoProduto) {

    	return new Validate(idCota, fornecedor, codigoProduto);
    }

    public class Validate {
    	
    	private Long idCota;
    	private Fornecedor fornecedor;
    	private String codigoProduto;

    	public Validate(Long idCota, Fornecedor fornecedor, String codigoProduto) {
    		this.idCota = idCota;
    		this.fornecedor = fornecedor;
    		this.codigoProduto = codigoProduto;
    	}
    	
    	private boolean hasDescontoGeral() {
    		
    		if (this.fornecedor == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoDTO filtro = new FiltroTipoDescontoDTO();
    		
    		filtro.setIdFornecedores(Arrays.asList(this.fornecedor.getId()));
    		
    		List<TipoDescontoDTO> desconto = CobrancaFornecedorValidator.this.descontoDistribuidorRepository.buscarDescontos(filtro);
    		
    		return desconto != null && !desconto.isEmpty();
    	}
    	
    	private boolean hasDescontoPorCota() {
    		
    		if (this.idCota == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
    		
    		filtro.setIdCota(this.idCota);
    		
    		List<TipoDescontoCotaDTO> desconto = CobrancaFornecedorValidator.this.descontoCotaRepository.obterDescontoCota(filtro);
    		
    		return desconto != null && !desconto.isEmpty();
    	}
    	
    	private boolean hasDescontoPorProduto() {

    		if (this.codigoProduto == null) {

        		return false;	
    		}
    		
    		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
    		
    		filtro.setCodigoProduto(this.codigoProduto);
    		
    		List<TipoDescontoProdutoDTO> desconto = CobrancaFornecedorValidator.this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
    		
    		return desconto != null && !desconto.isEmpty();
    	}

    	@Transactional
        public void validate() {

        	List<FormaCobrancaDTO> formasCobrancaCota = 
        			CobrancaFornecedorValidator.this.parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(this.idCota);

        	boolean fornecedorValido = false;

        	for (FormaCobrancaDTO formaCobranca : formasCobrancaCota) {

        		if (formaCobranca.getFornecedores() != null && formaCobranca.getFornecedores().contains(this.fornecedor)) {

           			fornecedorValido = true;
           			
           			if (this.codigoProduto == null) {
           				break;
           			}

        			if (this.hasDescontoGeral()) {
        				break;
        			} else if (this.hasDescontoPorCota()) {
            			break;
        			} else if (this.hasDescontoPorProduto()) {
        				break;
        			}

        			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Desconto não encontrado para o produto [Cod.: %s]", this.codigoProduto));
        		}
        	}

        	if (!fornecedorValido) {

        		Cota cota = CobrancaFornecedorValidator.this.cotaRepository.buscarPorId(this.idCota);

        		throw new ValidacaoException(TipoMensagem.WARNING, 
        				String.format("Não existem formas de cobrança cadastradas para o fornecedor %s na cota %s", 
        						this.fornecedor.getJuridica().getNomeFantasia(),
        						cota.getNumeroCota()));
        	}
        }
    }
}
