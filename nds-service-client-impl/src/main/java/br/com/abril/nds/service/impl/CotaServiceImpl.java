package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.AnaliseHistoricoXLSDTO;
import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.ParametroDistribuicaoEntregaCotaDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TermoAdesaoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.TitularidadeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.Flag;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorClassificacaoCota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.FlagPendenteAtivacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCota;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCotaPK;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroCobrancaDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametrosCotaNotaFiscalEletronica;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoProduto;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;
import br.com.abril.nds.repository.BaseReferenciaCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaBaseRepository;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoPDVRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.repository.FlagPendenteAtivacaoRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.HistoricoNumeroCotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.ParametroCobrancaDistribuicaoCotaRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PessoaFisicaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ReferenciaCotaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.SocioCotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.HistoricoTitularidadeService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.ListUtils;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class CotaServiceImpl implements CotaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotaServiceImpl.class);
    
    @Autowired
    private SituacaoCotaService situacaoCotaService;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private EnderecoCotaRepository enderecoCotaRepository;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
    
    @Autowired
    private CobrancaRepository cobrancaRepository;
    
    @Autowired
    private TelefoneCotaRepository telefoneCotaRepository;
    
    @Autowired
    private TelefoneService telefoneService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private BaseReferenciaCotaRepository baseReferenciaCotaRepository;
    
    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    
    @Autowired
    private PdvRepository pdvRepository;
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private ReferenciaCotaRepository referenciaCotaRepository;
    
    @Autowired
    private HistoricoNumeroCotaRepository historicoNumeroCotaRepository;
    
    @Autowired
    private EnderecoService enderecoService;
    
    @Autowired
    private DividaService dividaService;
    
    @Autowired
    private EnderecoPDVRepository enderecoPDVRepository;
    
    @Autowired
    private CotaBaseRepository cotaBaseRepository;
    
    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private RotaRepository rotaRepository;
    
    @Autowired
    private EntregadorRepository entregadorRepository;
    
    @Autowired
    private DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private HistoricoTitularidadeService historicoTitularidadeService;
    
    @Autowired
    private ParametrosDistribuidorService parametrosDistribuidorService;
    
    @Autowired
    private CotaGarantiaRepository cotaGarantiaRepository;
    
    @Autowired
    private SocioCotaRepository socioCotaRepository;
    
    @Autowired
    private PessoaService pessoaService;
    
    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorClassificacaoCotaRepository distribuidorClassificacaoCotaRepository;
    
    @Autowired
    private MixCotaProdutoService mixCotaProdutoService;
    
    @Autowired
    private FixacaoReparteService fixacaoReparteService;
    
    @Autowired
    private CotaBaseService cotaBaseService;
    
    @Autowired
    private FlagPendenteAtivacaoRepository flagPendenteAtivacaoRepository;

    @Autowired
    private ParametroCobrancaDistribuicaoCotaRepository parametroCobrancaDistribuicaoCotaRepository;
    
    @Autowired
    private AssociacaoVeiculoMotoristaRotaRepository motoristaRotaRepository;
    
    @Autowired
    private EstoqueProdutoService estoqueProdutoService;
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> obterCotas(final FiltroCotaDTO filtro) {
        
        return cotaRepository.obterCotas(filtro);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Long obterQuantidadeCotasPesquisadas(final FiltroCotaDTO filtro) {
        
        return cotaRepository.obterQuantidadeCotasPesquisadas(filtro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cota obterPorNumeroDaCota(final Integer numeroCota) {
    	
        return cotaRepository.obterPorNumeroDaCota(numeroCota);
    }
    
    @Override
	@Transactional(readOnly = true)
    public Map<Integer, EstudoCotaDTO> obterPorNumeroDaCota(List<Integer> numeroCota) {
    	
        return cotaRepository.obterPorNumeroDaCota(numeroCota);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cota obterPorNumeroDaCotaAtiva(final Integer numeroCota) {
        
        return cotaRepository.obterPorNumerDaCota(numeroCota);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cota> obterCotasPorNomePessoa(final String nome) {
        
        return cotaRepository.obterCotasPorNomePessoa(nome);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cota> obterPorNome(final String nome) {
        
        final List<Cota> listaCotas = cotaRepository.obterPorNome(nome);
        
        if  (listaCotas == null || listaCotas.isEmpty()) {
            
            return null;
        }
        /*
		if (listaCotas.size() > 1) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Mais de um resultado encontrado para a cota com nome \"" + nome + "\"");
		}
         */
        return listaCotas;
    }
    
    /**
     * @see br.com.abril.nds.service.CotaService#obterPorId(java.lang.Long)
     */
    @Override
    @Transactional
    public Cota obterPorId(final Long idCota) {
        
        if (idCota == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Id da cota não pode ser nulo.");
        }
        
        return cotaRepository.buscarPorId(idCota);
    }
    
    /**
     * ENDERECO
     * 
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EnderecoCota obterEnderecoPrincipal(final long idCota) {
        
        return cotaRepository.obterEnderecoPrincipal(idCota);
    }
    
    /**
     * ENDERECO
     * 
     * @see br.com.abril.nds.service.CotaService#obterEnderecosPorIdCota(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(final Long idCota) {
        
        final Set<Long> endRemover = new HashSet<Long>();
        
        final List<EnderecoAssociacaoDTO> listRetorno = new ArrayList<EnderecoAssociacaoDTO>();
        
        final List<EnderecoAssociacaoDTO> listaEnderecolAssoc = cotaRepository.obterEnderecosPorIdCota(idCota);
        
        if(listaEnderecolAssoc!= null && !listaEnderecolAssoc.isEmpty()){
            
            listRetorno.addAll(listaEnderecolAssoc);
            
            for (final EnderecoAssociacaoDTO dto : listaEnderecolAssoc){
                
                endRemover.add(dto.getEndereco().getId());
            }
        }
        
        return listRetorno;
    }
    
    /**
     * ENDERECO
     * 
     * @see br.com.abril.nds.service.CotaService#processarEnderecos(java.util.Long, java.util.List, java.util.List)
     */
    @Override
    @Transactional
    public void processarEnderecos(final Long idCota,
            final List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
            final List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover
            ) {
        
        if (idCota == null){
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
        }
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if (cota == null){
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
        }
        
        if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {
            
            this.salvarEnderecosCota(cota, listaEnderecoAssociacaoSalvar);
        }
        
        if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
            
            this.removerEnderecosCota(listaEnderecoAssociacaoRemover);
        }
    }
    
    /**
     * ENDERECO
     * 
     * Retorna um Endereco à ser editado ou cadastrado
     * 
     * @param enderecoDTO
     * @param pessoa
     * @param novo
     * @return Endereco
     */
    private Endereco obterEndereco(final EnderecoDTO enderecoDTO, final Pessoa pessoa, final boolean novo){
        
        Endereco endereco = new Endereco();
        
        if (!novo){
            
            endereco = enderecoRepository.buscarPorId(enderecoDTO.getId());
        }
        
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setCodigoCidadeIBGE(enderecoDTO.getCodigoCidadeIBGE());
        
        if(enderecoDTO.getCodigoUf() != null) {        	
        	endereco.setCodigoUf(enderecoDTO.getCodigoUf());
        } else {
         if ( enderecoDTO.getCodigoCidadeIBGE() != null ) {
        	 
        	String codigoUF = enderecoDTO.getCodigoCidadeIBGE().toString();
        	
        	enderecoDTO.setCodigoUf(Integer.valueOf(codigoUF.substring(0, 2)));
           }
        }
        
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
        endereco.setLogradouro(enderecoDTO.getLogradouro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setUf(enderecoDTO.getUf());
        endereco.setCodigoUf(enderecoDTO.getCodigoUf());
        endereco.setCodigoUf(enderecoDTO.getCodigoUf());
        
        endereco.setPessoa(pessoa);
        
        return endereco;
    }
    
    /**
     * ENDERECO
     * 
     * Persiste EnderecoCota e Endereco Valida apenas endereços vinculados à
     * cota
     * 
     * @param cota
     * @param listaEnderecoAssociacao
     */
    private void salvarEnderecosCota(final Cota cota, final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
        
        final Pessoa pessoa = cota.getPessoa();
        
        for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
            
            if (enderecoAssociacao.getTipoEndereco() == null){
                
                continue;
            }
            
            final EnderecoDTO enderecoDTO = enderecoAssociacao.getEndereco();
            
            enderecoService.validarEndereco(enderecoDTO, enderecoAssociacao.getTipoEndereco());
            
            EnderecoCota enderecoCota = enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
            
            Endereco endereco = null;
            
            boolean novoEnderecoCota = false;
            
            if (enderecoCota == null) {
                
                novoEnderecoCota = true;
                
                enderecoCota = new EnderecoCota();
                
                enderecoCota.setCota(cota);
            }
            
            final boolean novoEndereco = novoEnderecoCota && !enderecoAssociacao.isEnderecoPessoa();
            
            endereco = this.obterEndereco(enderecoDTO, pessoa, novoEndereco);
            
            enderecoCota.setEndereco(endereco);
            
            enderecoCota.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());
            
            enderecoCota.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
            
            enderecoCotaRepository.merge(enderecoCota);
        }
    }
    
    /**
     * ENDERECO
     * 
     * Remove lista de EnderecoCota
     * 
     * @param listaEnderecoAssociacao
     */
    private void removerEnderecosCota(final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
        
        final List<EnderecoDTO> listaEndereco = new ArrayList<EnderecoDTO>();
        
        final List<Long> idsEndereco = new ArrayList<Long>();
        
        for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
            
            if(enderecoAssociacao!= null){
                
                listaEndereco.add(enderecoAssociacao.getEndereco());
                
                final EnderecoCota enderecoCota = enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
                
                if(enderecoCota!= null){
                    
                    idsEndereco.add(enderecoAssociacao.getEndereco().getId());
                    
                    enderecoCotaRepository.remover(enderecoCota);
                }
            }
        }
        
        if(!idsEndereco.isEmpty()){
            
            enderecoService.removerEnderecos(idsEndereco);
        }
    }
    
    /**
     * TELEFONE
     * 
     * Obtém telefones da Cota
     * 
     * @param idCota
     * @param idsIgnorar
     * @return List<TelefoneAssociacaoDTO>
     */
    @Transactional(readOnly = true)
    @Override
    public List<TelefoneAssociacaoDTO> buscarTelefonesCota(final Long idCota, final Set<Long> idsIgnorar) {
        
        if (idCota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
        }
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        final Long idPessoa = cota.getPessoa().getId();
        
        final Set<Long> telRemover = new HashSet<Long>();
        
        List<TelefoneAssociacaoDTO> listaTelAssoc =
                telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
        if (listaTelAssoc == null) {
            listaTelAssoc = new ArrayList<TelefoneAssociacaoDTO>();
        } else {
            for (final TelefoneAssociacaoDTO dto : listaTelAssoc){
                telRemover.add(dto.getTelefone().getId());
            }
        }
        final List<TelefoneAssociacaoDTO> lista = telefoneService.buscarTelefonesPorIdPessoa(idPessoa, telRemover);
        
        if (lista!= null && !lista.isEmpty()){
            
            listaTelAssoc.addAll(lista);
        }
        
        return listaTelAssoc;
    }
    
    /**
     * TELEFONE
     * 
     * Processa Telefones para Alteracao e Remocao
     * @param idCota
     * @param listaTelefonesAdicionar
     * @param listaTelefonesRemover
     */
    @Override
    @Transactional
    public void processarTelefones(final Long idCota,
            final List<TelefoneAssociacaoDTO> listaTelefonesAdicionar,
            final Collection<Long> listaTelefonesRemover){
        
        if (idCota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
        }
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if (cota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
        }
        
        this.salvarTelefonesCota(cota, listaTelefonesAdicionar);
        
        this.removerTelefonesCota(listaTelefonesRemover);
        
        final List<Telefone> listaTelefone = new ArrayList<Telefone>();
        
        for (final TelefoneAssociacaoDTO telefoneCota : listaTelefonesAdicionar){
            final TelefoneDTO dto = telefoneCota.getTelefone();
            final Telefone telefone = new Telefone(dto.getId(), dto.getNumero(), dto.getRamal(), dto.getDdd(), cota.getPessoa());
            listaTelefone.add(telefone);
        }
        
        cota.getPessoa().setTelefones(listaTelefone);
        
        cotaRepository.alterar(cota);
        
    }
    
    /**
     * TELEFONE
     * 
     * Persiste Telefones
     * @param cota
     * @param listaTelefonesCota
     */
    private void salvarTelefonesCota(final Cota cota, final List<TelefoneAssociacaoDTO> listaTelefonesCota) {
        
        final Pessoa pessoa = cota.getPessoa();
        
        if (listaTelefonesCota != null){
            
            for (final TelefoneAssociacaoDTO dto : listaTelefonesCota) {
                
                TelefoneCota telefoneCota = null;
                
                final TelefoneDTO telefoneDTO = dto.getTelefone();
                
                telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
                
                if(telefoneDTO!= null && telefoneDTO.getId()!= null){
                    telefoneCota = cotaRepository.obterTelefonePorTelefoneCota(telefoneDTO.getId(), cota.getId());
                    
                    telefoneCota = popularTelefone(cota, pessoa, dto, telefoneCota, telefoneDTO);
                    
                    telefoneCotaRepository.merge(telefoneCota);
                }else{
                	
                	telefoneCota = popularTelefone(cota, pessoa, dto, telefoneCota, telefoneDTO);
                	
                	telefoneCotaRepository.adicionar(telefoneCota);
                }
            }
        }
    }

	private TelefoneCota popularTelefone(final Cota cota, final Pessoa pessoa, final TelefoneAssociacaoDTO dto, TelefoneCota telefoneCota, final TelefoneDTO telefoneDTO) {
		
		if(telefoneCota == null){
		    telefoneCota = new TelefoneCota();
		    telefoneCota.setCota(cota);
		}
		
		final Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), pessoa);

		telefoneCota.setPrincipal(dto.isPrincipal());
		telefoneCota.setTelefone(telefone);
		telefoneCota.setTipoTelefone(dto.getTipoTelefone());
		
		return telefoneCota;
	}
    
    /**
     * TELEFONE
     * 
     * Remove Telefones
     * @param listaTelefonesCota
     */
    private void removerTelefonesCota(final Collection<Long> listaTelefonesCota) {
        
        if (listaTelefonesCota != null && !listaTelefonesCota.isEmpty()){
            telefoneCotaRepository.removerTelefonesCota(listaTelefonesCota);
            
            telefoneService.removerTelefones(listaTelefonesCota);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cobranca> obterCobrancasDaCotaEmAberto(final Long idCota) {
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        return cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota, false, dataOperacao);
    }
    
    @Override
    @Transactional
    public List<CotaSuspensaoDTO> suspenderCotasGetDTO(final List<Long> idCotas, final Long idUsuario) {
        
        final List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario, MotivoAlteracaoSituacao.INADIMPLENCIA);
        
        final List<CotaSuspensaoDTO> cotasDTO = new ArrayList<CotaSuspensaoDTO>();
        
        for(final Cota cota : cotasSuspensas) {
            
            final Pessoa pessoa = cota.getPessoa();
            
            final String nome = pessoa instanceof PessoaFisica ?
                    ((PessoaFisica)pessoa).getNome() : ((PessoaJuridica)pessoa).getRazaoSocial();
                    
                    if(cota.getContratoCota() != null && cota.getContratoCota().isExigeDocumentacaoSuspencao()) {
                        
                        cotasDTO.add(new CotaSuspensaoDTO(
                                cota.getId(),
                                cota.getNumeroCota(),
                                nome,
                                null,
                                null,
                                null,
                                null,
                                null));
                    }
        }
        
        return cotasDTO;
    }
    
    @Override
    @Transactional
    public List<Cota> suspenderCotas(final List<Long> idCotas, final Long idUsuario, final MotivoAlteracaoSituacao motivoAlteracaoSituacao) {
        
        final List<Cota> cotasSuspensas = new ArrayList<Cota>();
        
        final Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        
        final Date dataInicioValidade = distribuidorRepository.obterDataOperacaoDistribuidor();
        
        for(final Long id:idCotas) {
            
            cotasSuspensas.add(suspenderCota(id, usuario, dataInicioValidade, motivoAlteracaoSituacao));
        }
        
        return cotasSuspensas;
    }
    
    @Override
    @Transactional
    public Cota suspenderCota(final Long idCota, final Usuario usuario, final Date dataInicioValidade, final MotivoAlteracaoSituacao motivoAlteracaoSituacao) {
        
        final Cota cota = obterPorId(idCota);
        
        if(SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "A cota já está suspensa!");
        }
        
        final HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
        historico.setCota(cota);
        historico.setDataEdicao(new Date());
        historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
        historico.setSituacaoAnterior(cota.getSituacaoCadastro());
        historico.setResponsavel(usuario);
        historico.setMotivo(motivoAlteracaoSituacao);
        historico.setTipoEdicao(TipoEdicao.ALTERACAO);
        historico.setDataInicioValidade(dataInicioValidade);
        historico.setProcessado(true);
        
        historicoSituacaoCotaRepository.merge(historico);
        
        cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
        
        cotaRepository.alterar(cota);
        
        situacaoCotaService.removerAgendamentosAlteracaoSituacaoCota(cota.getId());
        
        return cota;
    }
    
    @Override
    @Transactional
    public List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(final String sortOrder, final String sortColumn, final Integer inicio, final Integer rp) {
        
        final List<CotaSuspensaoDTO> cotasSujeitasSuspensao =
                cotaRepository.obterCotasSujeitasSuspensao(sortOrder, sortColumn, inicio, rp,
                        distribuidorRepository.obterDataOperacaoDistribuidor());
        
        return cotasSujeitasSuspensao;
    }
    
    @Override
    @Transactional
    public Long obterTotalCotasSujeitasSuspensao() {
        return cotaRepository.obterTotalCotasSujeitasSuspensao(distribuidorRepository.obterDataOperacaoDistribuidor());
    }
    
    @Override
    @Transactional
    public BigDecimal obterTotalDividaCotasSujeitasSuspensao() {
        
        return cotaRepository.obterTotalDividaCotasSujeitasSuspensao(
                distribuidorRepository.obterDataOperacaoDistribuidor());
    }
    
    @Override
    @Transactional
    public String obterNomeResponsavelPorNumeroDaCota(final Integer numeroCota){
        
        if (numeroCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório");
        }
        
        final Cota cota = this.obterPorNumeroDaCota(numeroCota);
        
        if (cota != null){
            if (cota.getPessoa() instanceof PessoaFisica){
                return ((PessoaFisica) cota.getPessoa()).getNome();
            } else {
                return ((PessoaJuridica) cota.getPessoa()).getRazaoSocial();
            }
        }
        
        return null;
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<Cota> obterCotaAssociadaFiador(final Long idFiador){
        return cotaRepository.obterCotaAssociadaFiador(idFiador);
    }
    
    /**
     * @see br.com.abril.nds.service.CotaService#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
     */
    @Override
    @Transactional
    public Cota obterCotaPDVPorNumeroDaCota(final Integer numeroCota) {
        return cotaRepository.obterCotaPDVPorNumeroDaCota(numeroCota);
    }
    
    /**
     * Define parametros default com base em parametros do Distribuidor
     * @param distribuicao
     * @return DistribuicaoDTO
     */
    private DistribuicaoDTO setDistribuicaoDefault(final DistribuicaoDTO distribuicao){
        
        final List<ParametrosDistribuidorEmissaoDocumento> listaParametrosDistribuidorEmissaoDocumentos =
                distribuidorRepository.parametrosDistribuidorEmissaoDocumentos();
        
        if (listaParametrosDistribuidorEmissaoDocumentos != null
                && !listaParametrosDistribuidorEmissaoDocumentos.isEmpty()) {
            
            for (final ParametrosDistribuidorEmissaoDocumento item : listaParametrosDistribuidorEmissaoDocumentos){
                
                final TipoParametrosDistribuidorEmissaoDocumento tipo = item.getTipoParametrosDistribuidorEmissaoDocumento();
                
                if (tipo!=null){
                    
                    switch (tipo){
                    
                    case SLIP:
                        
                        distribuicao.setSlipEmail(item.isUtilizaEmail());
                        distribuicao.setSlipImpresso(item.isUtilizaImpressao());
                        
                        break;
                    case BOLETO:
                        
                        distribuicao.setBoletoEmail(item.isUtilizaEmail());
                        distribuicao.setBoletoImpresso(item.isUtilizaImpressao());
                        
                        break;
                    case BOLETO_SLIP:
                        
                        distribuicao.setBoletoSlipEmail(item.isUtilizaEmail());
                        distribuicao.setBoletoSlipImpresso(item.isUtilizaImpressao());
                        
                        break;
                    case RECIBO:
                        
                        distribuicao.setReciboEmail(item.isUtilizaEmail());
                        distribuicao.setReciboImpresso(item.isUtilizaImpressao());
                        
                        break;
                    case NOTA_ENVIO:
                        
                        distribuicao.setNeEmail(item.isUtilizaEmail());
                        distribuicao.setNeImpresso(item.isUtilizaImpressao());
                        
                        break;
                    case CHAMADA_ENCALHE:
                        
                        distribuicao.setCeEmail(item.isUtilizaEmail());
                        distribuicao.setCeImpresso(item.isUtilizaImpressao());
                        
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        
        return distribuicao;
    }
    
    @Override
    @Transactional(readOnly=true)
    public DistribuicaoDTO obterDadosDistribuicaoCota(final Long idCota) {
        
        if(idCota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Id da cota não informado.");
        }
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if(cota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
        }
        
        final ParametroDistribuicaoCota parametroDistribuicaoCota = cota.getParametroDistribuicao();
        
        final boolean qtdePDVAutomatico = distribuidorService.preenchimentoAutomaticoPDV();
        
        DistribuicaoDTO dto = new DistribuicaoDTO();
        
        dto.setNumCota(cota.getNumeroCota());
        
        if(cota.getBox()!= null){
            dto.setBox(cota.getBox().getNome());
        }
        
        if(qtdePDVAutomatico) {
            dto.setQtdePDV( cota.getPdvs()!=null ? cota.getPdvs().size() : 0);
            dto.setQtdeAutomatica(true);
        }
        
        if (parametroDistribuicaoCota == null) {
            return this.setDistribuicaoDefault(dto);
            
        } else if ((parametroDistribuicaoCota.getSlipImpresso() == null || !parametroDistribuicaoCota.getSlipImpresso())
                && (parametroDistribuicaoCota.getSlipEmail() == null || !parametroDistribuicaoCota.getSlipEmail())
                && (parametroDistribuicaoCota.getBoletoImpresso() == null || !parametroDistribuicaoCota.getBoletoImpresso())
                && (parametroDistribuicaoCota.getBoletoEmail() == null || !parametroDistribuicaoCota.getBoletoEmail())
                && (parametroDistribuicaoCota.getBoletoSlipImpresso() == null || !parametroDistribuicaoCota.getBoletoSlipImpresso())
                && (parametroDistribuicaoCota.getBoletoSlipEmail() == null || !parametroDistribuicaoCota.getBoletoSlipEmail())
                && (parametroDistribuicaoCota.getReciboImpresso() == null || !parametroDistribuicaoCota.getReciboImpresso())
                && (parametroDistribuicaoCota.getReciboEmail() == null || !parametroDistribuicaoCota.getReciboEmail())
                && (parametroDistribuicaoCota.getChamadaEncalheImpresso() == null || !parametroDistribuicaoCota.getChamadaEncalheImpresso())
                && (parametroDistribuicaoCota.getChamadaEncalheEmail() == null || !parametroDistribuicaoCota.getChamadaEncalheEmail())
                && (parametroDistribuicaoCota.getNotaEnvioImpresso() == null || !parametroDistribuicaoCota.getNotaEnvioImpresso())
                && (parametroDistribuicaoCota.getNotaEnvioEmail() == null || !parametroDistribuicaoCota.getNotaEnvioEmail()))
        {
            
            dto = this.setDistribuicaoDefault(dto);
            
        } else {
            dto.setNeImpresso(parametroDistribuicaoCota.getNotaEnvioImpresso());
            dto.setNeEmail(parametroDistribuicaoCota.getNotaEnvioEmail());
            dto.setCeImpresso(parametroDistribuicaoCota.getChamadaEncalheImpresso());
            dto.setCeEmail(parametroDistribuicaoCota.getChamadaEncalheEmail());
            dto.setSlipImpresso(parametroDistribuicaoCota.getSlipImpresso());
            dto.setSlipEmail(parametroDistribuicaoCota.getSlipEmail());
            dto.setBoletoImpresso(parametroDistribuicaoCota.getBoletoImpresso());
            dto.setBoletoEmail(parametroDistribuicaoCota.getBoletoEmail());
            dto.setBoletoSlipImpresso(parametroDistribuicaoCota.getBoletoSlipImpresso());
            dto.setBoletoSlipEmail(parametroDistribuicaoCota.getBoletoSlipEmail());
            dto.setReciboImpresso(parametroDistribuicaoCota.getReciboImpresso());
            dto.setReciboEmail(parametroDistribuicaoCota.getReciboEmail());
        }
        
        if(!qtdePDVAutomatico) {
            dto.setQtdePDV(parametroDistribuicaoCota.getQtdePDV());
        }
        
        dto.setAssistComercial(parametroDistribuicaoCota.getAssistenteComercial());
        dto.setGerenteComercial(parametroDistribuicaoCota.getGerenteComercial());
        
        dto.setDescricaoTipoEntrega(parametroDistribuicaoCota.getDescricaoTipoEntrega());
        
        dto.setObservacao(parametroDistribuicaoCota.getObservacao());
        dto.setRepPorPontoVenda(parametroDistribuicaoCota.getRepartePorPontoVenda());
        dto.setSolNumAtras(parametroDistribuicaoCota.getSolicitaNumAtras());
        dto.setRecebeRecolhe(parametroDistribuicaoCota.getRecebeRecolheParciais());
        dto.setUtilizaTermoAdesao(parametroDistribuicaoCota.getUtilizaTermoAdesao());
        dto.setTermoAdesaoRecebido(parametroDistribuicaoCota.getTermoAdesaoRecebido());
        dto.setUtilizaProcuracao(parametroDistribuicaoCota.getUtilizaProcuracao());
        dto.setProcuracaoRecebida(parametroDistribuicaoCota.getProcuracaoRecebida());
        dto.setInicioPeriodoCarencia(DateUtil.formatarDataPTBR(parametroDistribuicaoCota.getInicioPeriodoCarencia()));
        dto.setFimPeriodoCarencia(DateUtil.formatarDataPTBR(parametroDistribuicaoCota.getFimPeriodoCarencia()));
        dto.setTipoDistribuicaoCota(cotaRepository.obterTipoDistribuicao(idCota).name());
        dto.setRecebeComplementar(parametroDistribuicaoCota.getRecebeComplementar());
        
        ParametroCobrancaDistribuicaoCota parametro = cota.getParametroCobrancaDistribuicaoCota();
        
        if(parametro != null) {
        	  
        	  dto.setDiaCobranca(parametro.getDiaCobranca());
              dto.setDiaSemanaCobranca(parametro.getDiaSemanaCobranca());
              dto.setModalidadeCobranca(parametro.getModalidadeCobranca());
              dto.setPercentualFaturamento(parametro.getPercentualFaturamento());
              dto.setPeriodicidadeCobranca(parametro.getPeriodicidadeCobranca());
              dto.setPorEntrega(parametro.isPorEntrega());
              dto.setTaxaFixa(parametro.getTaxaFixa());
              dto.setBaseCalculo(parametro.getBaseCalculo());
        }

        return dto;
    }
    
    @Override
    @Transactional
    public void salvarDistribuicaoCota(final DistribuicaoDTO dto) throws FileNotFoundException, IOException {
        
        if(dto==null || dto.getNumCota()==null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
        }
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(dto.getNumCota());
        
        if( cota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
        }
        
        
        ///////////////////////////////////////////////
        
        
        /*
         * faço a pesquisa nos parâmetros de distribuidor para comparar ao que
         * foi inserido pelo usuário. Se os parâmetros forem iguais, utilizo o
         * do distribuidor.
         */
        
        final ParametroDistribuicaoCota parametrosDistribuidorConferenciaCota = new ParametroDistribuicaoCota();
        
        final Distribuidor distribuidor = distribuidorService.obter();
        final List<ParametrosDistribuidorEmissaoDocumento> listaParametrosDeDistribuicao = distribuidor.getParametrosDistribuidorEmissaoDocumentos();
        
        
        
        
        final ParametroDistribuicaoCota parametros = new ParametroDistribuicaoCota();
        
        parametros.setQtdePDV(dto.getQtdePDV());
        parametros.setAssistenteComercial(dto.getAssistComercial());
        parametros.setGerenteComercial(dto.getGerenteComercial());
        parametros.setDescricaoTipoEntrega(dto.getDescricaoTipoEntrega());
        parametros.setObservacao(dto.getObservacao());
        parametros.setRepartePorPontoVenda(dto.getRepPorPontoVenda());
        parametros.setSolicitaNumAtras(dto.getSolNumAtras());
        parametros.setRecebeRecolheParciais(dto.getRecebeRecolhe());
        parametros.setRecebeComplementar(dto.getRecebeComplementar());
        parametros.setUtilizaTermoAdesao(dto.getUtilizaTermoAdesao());
        parametros.setTermoAdesaoRecebido(dto.getTermoAdesaoRecebido());
        parametros.setUtilizaProcuracao(dto.getUtilizaProcuracao());
        parametros.setProcuracaoRecebida(dto.getProcuracaoRecebida());
        
        if (dto.getInicioPeriodoCarencia() != null) {
            parametros.setInicioPeriodoCarencia(DateUtil.parseDataPTBR(dto.getInicioPeriodoCarencia()));
        } else {
            parametros.setInicioPeriodoCarencia(null);
        }
        
        if (dto.getFimPeriodoCarencia() != null) {
            parametros.setFimPeriodoCarencia(DateUtil.parseDataPTBR(dto.getFimPeriodoCarencia()));
        } else {
            parametros.setFimPeriodoCarencia(null);
        }
        
        
        
        for(final ParametrosDistribuidorEmissaoDocumento parametrosDeDistribuicao : listaParametrosDeDistribuicao) {
            
            final String nomeDocumento = parametrosDeDistribuicao.getTipoParametrosDistribuidorEmissaoDocumento().name();
            
            switch (nomeDocumento) {
            case "BOLETO":
                parametrosDistribuidorConferenciaCota.setBoletoEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setBoletoImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            case "BOLETO_SLIP":
                parametrosDistribuidorConferenciaCota.setBoletoSlipEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setBoletoSlipImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            case "CHAMADA_ENCALHE":
                parametrosDistribuidorConferenciaCota.setChamadaEncalheEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setChamadaEncalheImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            case "NOTA_ENVIO":
                parametrosDistribuidorConferenciaCota.setNotaEnvioEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setNotaEnvioImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            case "RECIBO":
                parametrosDistribuidorConferenciaCota.setReciboEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setReciboImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            case "SLIP":
                parametrosDistribuidorConferenciaCota.setSlipEmail(parametrosDeDistribuicao.isUtilizaEmail());
                parametrosDistribuidorConferenciaCota.setSlipImpresso(parametrosDeDistribuicao.isUtilizaImpressao());
                break;
                
            default:
                break;
            }
            
        }
        
        if(              
                parametrosDistribuidorConferenciaCota.getBoletoEmail().equals(dto.getBoletoEmail()) &&
                parametrosDistribuidorConferenciaCota.getBoletoImpresso().equals(dto.getBoletoImpresso()) &&
                
                parametrosDistribuidorConferenciaCota.getBoletoSlipEmail().equals(dto.getBoletoSlipEmail()) &&
                parametrosDistribuidorConferenciaCota.getBoletoSlipImpresso().equals(dto.getBoletoSlipImpresso()) &&
                
                parametrosDistribuidorConferenciaCota.getChamadaEncalheEmail().equals(dto.getCeEmail()) &&
                parametrosDistribuidorConferenciaCota.getChamadaEncalheImpresso().equals(dto.getCeImpresso()) &&
                
                parametrosDistribuidorConferenciaCota.getNotaEnvioEmail().equals(dto.getNeEmail()) &&
                parametrosDistribuidorConferenciaCota.getNotaEnvioImpresso().equals(dto.getNeImpresso()) &&
                
                parametrosDistribuidorConferenciaCota.getReciboEmail().equals(dto.getReciboEmail()) &&
                parametrosDistribuidorConferenciaCota.getReciboImpresso().equals(dto.getReciboImpresso()) &&
                
                parametrosDistribuidorConferenciaCota.getSlipEmail().equals(dto.getSlipEmail()) &&
                parametrosDistribuidorConferenciaCota.getSlipImpresso().equals(dto.getSlipImpresso())
                
                ) {
            parametros.setSlipImpresso(null);
            parametros.setSlipEmail(null);
            parametros.setBoletoImpresso(null);
            parametros.setBoletoEmail(null);
            parametros.setBoletoSlipImpresso(null);
            parametros.setBoletoSlipEmail(null);
            parametros.setReciboImpresso(null);
            parametros.setReciboEmail(null);
            parametros.setChamadaEncalheImpresso(null);
            parametros.setChamadaEncalheEmail(null);
            parametros.setNotaEnvioImpresso(null);
            parametros.setNotaEnvioEmail(null);
        } else {
            parametros.setSlipImpresso(dto.getSlipImpresso());
            parametros.setSlipEmail(dto.getSlipEmail());
            parametros.setBoletoImpresso(dto.getBoletoImpresso());
            parametros.setBoletoEmail(dto.getBoletoEmail());
            parametros.setBoletoSlipImpresso(dto.getBoletoSlipImpresso());
            parametros.setBoletoSlipEmail(dto.getBoletoSlipEmail());
            parametros.setReciboImpresso(dto.getReciboImpresso());
            parametros.setReciboEmail(dto.getReciboEmail());
            parametros.setChamadaEncalheImpresso(dto.getCeImpresso());
            parametros.setChamadaEncalheEmail(dto.getCeEmail());
            parametros.setNotaEnvioImpresso(dto.getNeImpresso());
            parametros.setNotaEnvioEmail(dto.getNeEmail());
        }
        
        cota.setParametroDistribuicao(parametros);
        
        cotaRepository.merge(cota);
        
        this.atualizaTermoAdesao(cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGA_EM_BANCA);
        
        this.atualizaTermoAdesao(cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGADOR);
        
        this.processarParametrosCobrancaDistribuicao(dto,cota);
    }
    
    @Transactional
    public void processarParametrosCobrancaDistribuicao(DistribuicaoDTO dto, Cota cota) {
    	
    	ParametroCobrancaDistribuicaoCota parametro = cota.getParametroCobrancaDistribuicaoCota();
    	
    	if(DescricaoTipoEntrega.COTA_RETIRA.equals(dto.getDescricaoTipoEntrega())){
    		
    		if (parametro!= null){
             	
            	parametroCobrancaDistribuicaoCotaRepository.remover(parametro);
            }
    		
    		return;
    	}
    	
    	if(parametro == null){
    		parametro = new ParametroCobrancaDistribuicaoCota();
    	}
    	
    	ModalidadeCobranca modalidadeCobranca = dto.getModalidadeCobranca();
    	
    	parametro.setDiaCobranca(dto.getDiaCobranca());
    	parametro.setDiaSemanaCobranca(dto.getDiaSemanaCobranca());
    	parametro.setModalidadeCobranca(modalidadeCobranca);
    	parametro.setPercentualFaturamento(dto.getPercentualFaturamento());
    	parametro.setPeriodicidadeCobranca(dto.getPeriodicidadeCobranca());
    	parametro.setPorEntrega(dto.isPorEntrega());
    	parametro.setTaxaFixa(dto.getTaxaFixa());
    	parametro.setBaseCalculo(dto.getBaseCalculo());
 
    	cota.setParametroCobrancaDistribuicaoCota(parametro);
    	parametroCobrancaDistribuicaoCotaRepository.merge(parametro);
	}

	@Override
    @Transactional(readOnly = true)
    public CotaDTO obterDadosCadastraisCota(final Long idCota){
        
        if (idCota == null) {
        	
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
        }
        
        final Cota cota  = cotaRepository.buscarPorId(idCota);
        
        if (cota == null) {
        	
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		final CotaDTO cotaDTO = new CotaDTO();
		cotaDTO.setIdCota(cota.getId());
		cotaDTO.setNumeroCota(cota.getNumeroCota());
		cotaDTO.setClassificacaoSelecionada(cota.getClassificacaoEspectativaFaturamento());
		cotaDTO.setDataInclusao(cota.getInicioAtividade());
		cotaDTO.setEmailNF((cota.getParametrosCotaNotaFiscalEletronica()!= null) ? cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica() : "");
		
		FlagPendenteAtivacao flagContribuinte = flagPendenteAtivacaoRepository.obterPor(Flag.COTA_CONTRIBUINTE_ICMS, cotaDTO.getIdCota());
		FlagPendenteAtivacao flagExigeNFe = flagPendenteAtivacaoRepository.obterPor(Flag.COTA_EXIGE_NF_E, cotaDTO.getIdCota());
		
		if(flagExigeNFe != null) {
			cotaDTO.setExigeNFE(flagExigeNFe.isValor());
		} else {
			cotaDTO.setExigeNFE((cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica() != null) ? cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica() : false);
		}
		
		if(flagContribuinte != null) {
			cotaDTO.setContribuinteICMS(flagContribuinte.isValor());
		} else {
			cotaDTO.setContribuinteICMS((cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS() != null) ? cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS() : false);
		}
		
		cotaDTO.setStatus(cota.getSituacaoCadastro());
		cotaDTO.setTipoCotaFinanceiro(cota.getTipoCota());
		cotaDTO.setUtilizaIPV(cota.isUtilizaIPV());
		
		if (cota.getTipoDistribuicaoCota() != null) {
		    cotaDTO.setTipoCota(cota.getTipoDistribuicaoCota().getDescTipoDistribuicaoCota().substring(0, 1));
		    cotaDTO.setTipoDistribuicaoCota(cota.getTipoDistribuicaoCota());
		}
		
		this.atribuirInicioEFimPeriodoCota(cotaDTO, cota.getId());
		this.atribuirDadosPessoaCota(cotaDTO, cota.getPessoa());
		this.atribuirDadosBaseReferencia(cotaDTO, cota.getBaseReferenciaCota());
		
		processarTitularidadeCota(cota, cotaDTO);
		
		cotaDTO.setCotasBases(atribuirCotaBase(cota.getNumeroCota()));
		cotaDTO.setRecebeComplementar(cota.getParametroDistribuicao() == null ? false : cota.getParametroDistribuicao().getRecebeComplementar());
		
        return cotaDTO;
    }
    
    @Transactional
    private List<CotaBaseDTO> atribuirCotaBase(final Integer numeroCota) {
        
        List<CotaBaseDTO> listaCotaBase = new ArrayList<CotaBaseDTO>();
        
        final CotaBase cotaBase = cotaBaseService.obterCotaNova(numeroCota, true);
        
        if(cotaBase != null){
            listaCotaBase = cotaBaseService.obterCotasBases(cotaBase, null);
        }
        
        for (final CotaBaseDTO itemCotaBase : listaCotaBase) {
            
            if(itemCotaBase.getDtInicio() == null) {
                itemCotaBase.setDtInicio(cotaBase.getDataInicio());
            }
            
            if(itemCotaBase.getDtFinal() == null) {
                itemCotaBase.setDtFinal(cotaBase.getDataFim());
            }
            
        }
        
        return listaCotaBase;
        
    }
    
    /**
     * Processa os registros de titularidade da cota para criação dos titulares
     * no DTO
     * 
     * @param cota cota com as informações de titularidade
     * @param cotaDTO DTO com as incormações da cota
     */
    private void processarTitularidadeCota(final Cota cota, final CotaDTO cotaDTO) {
        final List<HistoricoTitularidadeCota> titulares = new ArrayList<HistoricoTitularidadeCota>();
        if (cota.getTitularesCota() != null) {
            titulares.addAll(cota.getTitularesCota());
        }
        Collections.sort(titulares, new Comparator<HistoricoTitularidadeCota>() {
            
            @Override
            public int compare(final HistoricoTitularidadeCota o1, final HistoricoTitularidadeCota o2) {
                return o2.getFim().compareTo(o1.getFim());
            }
        });
        
        for (final HistoricoTitularidadeCota historico : titulares) {
            cotaDTO.addProprietario(new TitularidadeCotaDTO(historico.getId(),
                    cota.getId(), historico.getInicio(), historico.getFim(),
                    historico.getPessoa().getNome(), historico.getPessoa()
                    .getDocumento()));
        }
    }
    
    /**
     *  Atribui os dados da pessoa relacionada a cota ao objeto CotaDTO
     * @param cotaDTO
     * @param pessoa
     */
    private void atribuirDadosPessoaCota(final CotaDTO cotaDTO, final Pessoa pessoa){
        
        if(pessoa == null){
            return;
        }
        
        if(pessoa instanceof PessoaJuridica){
            
            final PessoaJuridica pessoaJ = (PessoaJuridica) pessoa;
            cotaDTO.setInscricaoEstadual(pessoaJ.getInscricaoEstadual());
            cotaDTO.setInscricaoMunicipal(pessoaJ.getInscricaoMunicipal());
            cotaDTO.setEmail(pessoaJ.getEmail());
            cotaDTO.setNumeroCnpj(pessoaJ.getCnpj());
            cotaDTO.setNomeFantasia(pessoaJ.getNomeFantasia());
            cotaDTO.setRazaoSocial(pessoaJ.getRazaoSocial());
            cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);
        }
        
        else if (pessoa instanceof PessoaFisica){
            
            final PessoaFisica pessoaF = (PessoaFisica) pessoa;
            cotaDTO.setNomePessoa(pessoaF.getNome());
            cotaDTO.setEmail(pessoaF.getEmail());
            cotaDTO.setNumeroRG(pessoaF.getRg());
            cotaDTO.setDataNascimento(pessoaF.getDataNascimento());
            cotaDTO.setOrgaoEmissor(pessoaF.getOrgaoEmissor());
            cotaDTO.setEstadoSelecionado(pessoaF.getUfOrgaoEmissor());
            cotaDTO.setEstadoCivilSelecionado(pessoaF.getEstadoCivil());
            cotaDTO.setSexoSelecionado(pessoaF.getSexo());
            cotaDTO.setNacionalidade(pessoaF.getNacionalidade());
            cotaDTO.setNatural(pessoaF.getNatural());
            cotaDTO.setNumeroCPF(pessoaF.getCpf());
            cotaDTO.setTipoPessoa(TipoPessoa.FISICA);
        }
    }
    
    /**
     * Atribui os dados das referencias histórico cota base ao objeto CotaDTO
     * 
     * @param cotaDTO
     * @param baseReferenciaCota
     */
    private void atribuirDadosBaseReferencia(final CotaDTO cotaDTO, final BaseReferenciaCota baseReferenciaCota){
        
        if(baseReferenciaCota == null){
            return;
        }
        
        if(baseReferenciaCota.getReferenciasCota()!= null && !baseReferenciaCota.getReferenciasCota().isEmpty()){
            
            final List<ReferenciaCota> referenicasCota = new ArrayList<ReferenciaCota>();
            referenicasCota.addAll(baseReferenciaCota.getReferenciasCota());
            
            if (!referenicasCota.isEmpty()) {
                cotaDTO.setHistoricoPrimeiraCota(referenicasCota.get(0).getCota().getNumeroCota());
                cotaDTO.setHistoricoPrimeiraPorcentagem(referenicasCota.get(0).getPercentual());
            }
            
            if(referenicasCota.size() > 1){
                cotaDTO.setHistoricoSegundaCota(referenicasCota.get(1).getCota().getNumeroCota());
                cotaDTO.setHistoricoSegundaPorcentagem(referenicasCota.get(1).getPercentual());
            }
            
            if(referenicasCota.size() > 2){
                cotaDTO.setHistoricoTerceiraCota(referenicasCota.get(2).getCota().getNumeroCota());
                cotaDTO.setHistoricoTerceiraPorcentagem(referenicasCota.get(2).getPercentual());
            }
        }
    }
    
    @Transactional
    private void atribuirInicioEFimPeriodoCota(CotaDTO cotaDTO, Long idCota){
    	
    	CotaBase cotaBase = cotaBaseRepository.obterSituacaoCota(idCota);
    	
    	if(cotaBase != null) {
    		cotaDTO.setInicioPeriodo(cotaBase.getDataInicio());
    		cotaDTO.setFimPeriodo(cotaBase.getDataFim());
    	}
    }
    
    @Override
    @Transactional
    public void excluirCota(final Long idCota){
        
        if(idCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada para exclusão inválida!");
        }
        
        if (cotaComDebitos(idCota)){

            throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota] possui dívidas em aberto e não pode ser excluída!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
	
		try{	
			
			for (HistoricoSituacaoCota hist : cota.getHistoricos()){
				
				this.historicoSituacaoCotaRepository.remover(hist);
			}
			
			cotaRepository.remover(cota);
			
		}catch (RuntimeException e) {
			
			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
                throw new ValidacaoException(TipoMensagem.ERROR, "Exclusão não permitida, registro possui dependências!");
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer gerarNumeroSugestaoCota(){
	
		return cotaRepository.gerarSugestaoNumeroCota();
	}
	
	@Override
	@Transactional
	public Long salvarCota(CotaDTO cotaDto){
		
		validarParametrosObrigatoriosCota(cotaDto);
		
		validarFormatoDados(cotaDto);
		
		validarHistoricoCotaBase(cotaDto);
		
		Cota cota  = null;
		
		if(cotaDto.getIdCota()!= null){
			cota = cotaRepository.buscarPorId(cotaDto.getIdCota());
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		boolean incluirPDV = false;


        // Flag indica criação de uma nova cota
        boolean newCota = false;
        if(cota == null) {
            cota = new Cota();
            cota.setInicioAtividade(dataOperacao);
            cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
            incluirPDV = true;
            newCota = true;
        }
        
        // Flag indica a mudança de número da cota
        boolean mudancaNumero = false;
        if (!newCota) {
            final Integer numeroCota = cota.getNumeroCota();
            final Integer novoNumeroCota = cotaDto.getNumeroCota();
            mudancaNumero = !numeroCota.equals(novoNumeroCota);
        }
        // Se é uma nova cota ou alteração de número, processa o novo número
        if (newCota || mudancaNumero) {
            processarNovoNumeroCota(cotaDto.getNumeroCota(),cota.getId());
        }
        
        cota.setNumeroCota(cotaDto.getNumeroCota());
        
        cota.setTipoCota(cotaDto.getTipoCotaFinanceiro());
        
        cota.setUtilizaIPV(cotaDto.isUtilizaIPV());
        
        
        cota.setClassificacaoEspectativaFaturamento(cotaDto.getClassificacaoSelecionada());
        
        cota.setPessoa(persistePessoaCota(cota, cotaDto));
        
        cota.setTipoDistribuicaoCota(cotaDto.getTipoDistribuicaoCota());
        
        this.atibuirDadosDistribuicaoDaCota(cota);
        
        cota  = cotaRepository.merge(cota);
        
        // correcao de problema de flag_pendente_ativacao.id_alterado=null
        if(cotaDto.getIdCota()== null){
			cotaDto.setIdCota(cota.getId());
		}
        
        cota.setParametrosCotaNotaFiscalEletronica(getParamNFE(cota, cotaDto));
        
        cota  = cotaRepository.merge(cota);
        
      //
        if(newCota) {
            
            final HistoricoSituacaoCota hsc = new HistoricoSituacaoCota();
            hsc.setCota(cota);
            hsc.setDataEdicao(dataOperacao);
            hsc.setDataInicioValidade(dataOperacao);
            hsc.setTipoEdicao(TipoEdicao.INCLUSAO);
            hsc.setResponsavel(usuarioService.getUsuarioLogado());
            hsc.setSituacaoAnterior(cota.getSituacaoCadastro());
            hsc.setNovaSituacao(cota.getSituacaoCadastro());
            hsc.setDescricao("Cota nova.");
            hsc.setProcessado(true);
            
            historicoSituacaoCotaRepository.adicionar(hsc);
            
        }
        
        final BaseReferenciaCota baseReferenciaCota = processarDadosBaseReferenciaCota(cota, cotaDto);
        
        processarDadosReferenciaCota(baseReferenciaCota, cotaDto);
        
        if(incluirPDV) {
        	
            persisteDadosPDV(cota, cotaDto);
        }
        
        return cota.getId();
    }
    
    private void atibuirDadosDistribuicaoDaCota(final Cota cota) {
		
    	if(cota.getId()!= null){
    		return;
    	}
    	
    	if(cota.getParametroDistribuicao() == null){
    		cota.setParametroDistribuicao(new ParametroDistribuicaoCota());
    	}
    	
    	cota.getParametroDistribuicao().setRecebeRecolheParciais(true);;
    	
		if(TipoDistribuicaoCota.CONVENCIONAL.equals(cota.getParametroDistribuicao())){
			
			cota.getParametroDistribuicao().setRecebeComplementar(true);
		}
	}

	@Override
    @Transactional
    public void atualizaTermoAdesao(final String numCota, final DescricaoTipoEntrega descricaoTipoEntrega) throws FileNotFoundException, IOException {
        
        ParametroSistema pathDocumento = null;
        
        switch(descricaoTipoEntrega) {
        case ENTREGA_EM_BANCA:
            pathDocumento = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_TERMO_ADESAO);
            break;
        case ENTREGADOR:
            pathDocumento = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_PROCURACAO);
            break;
        default:
            return;
        }
        
        final ParametroSistema raiz =
                parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
        
        if(	raiz == null || raiz.getValor() == null ||
                pathDocumento == null || pathDocumento.getValor() == null) {
            
            return;
            
        }
        
        final String path = (raiz.getValor() + pathDocumento.getValor() + numCota).replace("\\", "/");
        
        fileService.persistirTemporario(path);
    }
    
    /**
     * Valida os dados referente histórico cota base
     * 
     * @param cotaDto
     */
    private void validarHistoricoCotaBase(final CotaDTO cotaDto) {
        
        if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null && DateUtil.removerTimestamp(cotaDto.getInicioPeriodo()).compareTo(DateUtil.removerTimestamp(Calendar.getInstance().getTime()))!=0){
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Campo [Período] referente à cota base deve ser igual a data da alteração!");
        }
        
        
        if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
            return;
        }
        
        if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() == null ){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O preenchimento do campo [Até] referente à  cota base é obrigatório!");
        }
        
        if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() != null ){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O preenchimento do campo [Período] referente à  cota base é obrigatório!");
        }
        
        if(DateUtil.isDataInicialMaiorDataFinal(cotaDto.getInicioPeriodo(), cotaDto.getFimPeriodo())){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O período preenchido nos campos [Período] [Até] referente à  cota base está inválido!");
        }
    }
    
    
    
    /**
     * Valida o formato das inforamções referente ao cadastro de uma cota
     * 
     * @param cotaDto
     */
    private void validarFormatoDados(final CotaDTO cotaDto) {
        
        if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
            
            pessoaService.validarCNPJ(cotaDto.getNumeroCnpj());
        }
        
        if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
            
            pessoaService.validarCPF(cotaDto.getNumeroCPF());
        }
        
        if( cotaDto.getEmail()!= null && !cotaDto.getEmail().isEmpty() && !Util.validarEmail(cotaDto.getEmail())){
            throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [E-mail] está inválido!");
        }
        
        if( cotaDto.getEmailNF()!= null && !cotaDto.getEmailNF().isEmpty() && !Util.validarEmail(cotaDto.getEmailNF())){
            throw new ValidacaoException(TipoMensagem.WARNING, " O preenchimento do campo [E-mail NF-e] está inválido!");
        }
        
    }
    
    /**
     * Persiste dados básicos de um PDV referente a nova cota
     * 
     * @param cota
     * @param cotaDTO
     */
	private void persisteDadosPDV(Cota cota,CotaDTO cotaDTO) {
		
		if(cota.getPdvs() == null || cota.getPdvs().isEmpty()){
			
			PDV pdv = new PDV();
			pdv.setCaracteristicas(new CaracteristicasPDV());
			pdv.getCaracteristicas().setPontoPrincipal(Boolean.TRUE);
			pdv.setDataInclusao(new Date());
			pdv.setCota(cota);
			
			if( TipoPessoa.JURIDICA.equals(cotaDTO.getTipoPessoa())){
				
				pdv.setNome(cotaDTO.getRazaoSocial());
			}
			else {
				
				pdv.setNome(cotaDTO.getNomePessoa());
			}
			
			pdvRepository.adicionar(pdv);
		}
	}
	
	/**
     * Retorna os parâmetros de notas fiscais eletrônicas referente a uma cota
     * 
     * @param cota
     * @param cotaDto
     * @return ParametrosCotaNotaFiscalEletronica
     */
	private ParametrosCotaNotaFiscalEletronica getParamNFE(Cota cota, CotaDTO cotaDto){
		
		ParametrosCotaNotaFiscalEletronica paramNFE = cota.getParametrosCotaNotaFiscalEletronica(); 
	    
	    if(paramNFE == null) {
	    	paramNFE = new ParametrosCotaNotaFiscalEletronica();
	    }
	    		
	    paramNFE.setEmailNotaFiscalEletronica(cotaDto.getEmailNF());
	    // Parametro ajustado ao fechar o dia para nao impactar em cenarios do decorrer do dia
	    //paramNFE.setExigeNotaFiscalEletronica(cotaDto.isExigeNFE());
	    //paramNFE.setContribuinteICMS(cotaDto.isContribuinteICMS());
	    
	    
	    FlagPendenteAtivacao flagCotaExigeNFe = 
	    		flagPendenteAtivacaoRepository.obterPor(Flag.COTA_EXIGE_NF_E, cotaDto.getIdCota());
	    
	    FlagPendenteAtivacao flagCotaContribuinteICMS = 
	    		flagPendenteAtivacaoRepository.obterPor(Flag.COTA_CONTRIBUINTE_ICMS, cotaDto.getIdCota());
	    
	    if(flagCotaExigeNFe == null) {
	    	
	    	flagCotaExigeNFe = new FlagPendenteAtivacao(Flag.COTA_EXIGE_NF_E, Flag.COTA_EXIGE_NF_E.getDescricao()
	    					, Dominio.COTA, cotaDto.isExigeNFE(), cotaDto.getIdCota());
	    	paramNFE.setExigeNotaFiscalEletronica(false);
	    } else {
	    	
	    	flagCotaExigeNFe.setValor(cotaDto.isExigeNFE());
	    	paramNFE.setExigeNotaFiscalEletronica(cotaDto.isExigeNFE());
	    }
	    
	    
	    if(flagCotaContribuinteICMS == null) {
	    	
	    	flagCotaContribuinteICMS = 
	    			new FlagPendenteAtivacao(Flag.COTA_CONTRIBUINTE_ICMS, Flag.COTA_CONTRIBUINTE_ICMS.getDescricao()
	    					, Dominio.COTA, cotaDto.isContribuinteICMS(), cotaDto.getIdCota());
	    	paramNFE.setContribuinteICMS(false);
	    } else {
	    	
	    	flagCotaContribuinteICMS.setValor(cotaDto.isContribuinteICMS());
	    	paramNFE.setContribuinteICMS(cotaDto.isContribuinteICMS());
	    }
	    
	    flagPendenteAtivacaoRepository.merge(flagCotaExigeNFe);
	    flagPendenteAtivacaoRepository.merge(flagCotaContribuinteICMS);
	    
	    return paramNFE;
	}
    
	/**
     * Validas as informações referente ao cadasto de uma nova cota.
     * 
     * @param cotaDto
     */
    private void validarParametrosObrigatoriosCota(final CotaDTO cotaDto) {
        
        final List<String> mensagensValidacao = new ArrayList<String>();
        
        if(cotaDto.getNumeroCota() == null){
            mensagensValidacao.add("O preenchimento do campo [Cota] é obrigatório!");
        }
        
        if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
            
            if(cotaDto.getRazaoSocial() == null || cotaDto.getRazaoSocial().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [Razão Social] é obrigatório!");
            }
            
            if(cotaDto.getNumeroCnpj() == null || cotaDto.getNumeroCnpj().trim().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [CNPJ] é obrigatório!");
            }
            
            CNPJValidator cnpjValidator = new CNPJValidator(true);
            try {
            	
            	cnpjValidator.assertValid(cotaDto.getNumeroCnpj());
            } catch(Exception e) {
            	mensagensValidacao.add("CNPJ inválido: "+ cotaDto.getNumeroCnpj());
            }
            
            if(cotaDto.getInscricaoEstadual() == null || cotaDto.getInscricaoEstadual().isEmpty() ){
                mensagensValidacao.add("O preenchimento do campo [Inscrição Estadual] é obrigatório!");
            }
        }
        
        if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
            
            if(cotaDto.getNomePessoa() == null || cotaDto.getNomePessoa().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [Nome] é obrigatório!");
            }
            
            if(cotaDto.getNumeroCPF() == null || cotaDto.getNumeroCPF().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [CPF] é obrigatório!");
            }
            
            CPFValidator cpfValidator = new CPFValidator(true);
            try {
            	
            	cpfValidator.assertValid(cotaDto.getNumeroCPF());
            } catch(Exception e) {
            	mensagensValidacao.add("CPF inválido: "+ cotaDto.getNumeroCPF());
            }
            
            if(cotaDto.getNumeroRG() == null || cotaDto.getNumeroRG().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [R. G.] é obrigatório!");
            }
            
            if(cotaDto.getDataNascimento() == null){
                mensagensValidacao.add("O preenchimento do campo [Data Nascimento] é obrigatório!");
            }
            
            if(cotaDto.getOrgaoEmissor() == null ||  cotaDto.getOrgaoEmissor().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [Orgão Emissor] é obrigatório!");
            }
            
            if(cotaDto.getEstadoSelecionado() == null || cotaDto.getEstadoSelecionado().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [UF] é obrigatório!");
            }
            
            if(cotaDto.getEstadoCivilSelecionado() == null){
                mensagensValidacao.add("O preenchimento do campo [Estado Civil] é obrigatório!");
            }
        }
        
        if(cotaDto.isExigeNFE()){
            if(cotaDto.getEmailNF() == null || cotaDto.getEmailNF().isEmpty()){
                mensagensValidacao.add("O preenchimento do campo [E-mail NF-e] é obrigatório!");
            }
        }
        
        if(cotaDto.getTipoDistribuicaoCota() == null){
            mensagensValidacao.add("O preenchimento do campo [Tipo] é obrigatório!");
        }
        
        if(cotaDto.getTipoCotaFinanceiro() == null){
            mensagensValidacao.add("O preenchimento do campo [Forma de Pagamento] é obrigatório!");
        }
        
        if (!mensagensValidacao.isEmpty()){
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
        }
    }
    
    /**
     * Persiste e retorna os dados da pessoa referente a cota Física ou Jurídica
     * 
     * @param cota
     * @param cotaDto
     * @return Pessoa
     */
    private Pessoa persistePessoaCota(final Cota cota, final CotaDTO cotaDto){
        
        Pessoa pessoa = null;
        
        if (cotaDto.isAlteracaoTitularidade()) {
            
            pessoa = getPessoa(cotaDto, null) ;
            
        } else {
            
            pessoa = getPessoa(cotaDto, cota.getPessoa()) ;
        }
        
        pessoa.setEmail(cotaDto.getEmail());
        
        if ( pessoa instanceof  PessoaJuridica  ){
            
            ((PessoaJuridica) pessoa).setCnpj(cotaDto.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
            ((PessoaJuridica) pessoa).setInscricaoEstadual(cotaDto.getInscricaoEstadual() != null ? cotaDto.getInscricaoEstadual().trim() : "");
            ((PessoaJuridica) pessoa).setInscricaoMunicipal(cotaDto.getInscricaoMunicipal());
            ((PessoaJuridica) pessoa).setNomeFantasia(cotaDto.getNomeFantasia());
            ((PessoaJuridica) pessoa).setRazaoSocial(cotaDto.getRazaoSocial());
            
            pessoa = pessoaJuridicaRepository.merge((PessoaJuridica) pessoa);
        }
        else if (pessoa instanceof  PessoaFisica){
            
            ((PessoaFisica) pessoa).setNome(cotaDto.getNomePessoa());
            ((PessoaFisica) pessoa).setEmail(cotaDto.getEmail());
            ((PessoaFisica) pessoa).setRg(cotaDto.getNumeroRG());
            ((PessoaFisica) pessoa).setDataNascimento(cotaDto.getDataNascimento());
            ((PessoaFisica) pessoa).setOrgaoEmissor(cotaDto.getOrgaoEmissor());
            ((PessoaFisica) pessoa).setUfOrgaoEmissor(cotaDto.getEstadoSelecionado());
            ((PessoaFisica) pessoa).setEstadoCivil(cotaDto.getEstadoCivilSelecionado());
            ((PessoaFisica) pessoa).setSexo(cotaDto.getSexoSelecionado());
            ((PessoaFisica) pessoa).setNacionalidade(cotaDto.getNacionalidade());
            ((PessoaFisica) pessoa).setNatural(cotaDto.getNatural());
            ((PessoaFisica) pessoa).setCpf(cotaDto.getNumeroCPF().replace(".", "").replace("-", "").trim());
            
            pessoa = pessoaFisicaRepository.merge((PessoaFisica) pessoa);
        }
        
        return pessoa;
    }
    
    /**
     * Retona uma instância de Pessoa Jurídica ou Física
     * 
     * @param cotaDTO
     * @param pessoa
     * @return Pessoa
     */
    private Pessoa getPessoa(final CotaDTO cotaDTO, Pessoa pessoa){
        
        if(pessoa == null){
            
            if( TipoPessoa.JURIDICA.equals(cotaDTO.getTipoPessoa())){
                
                pessoa  = pessoaJuridicaRepository.buscarPorCnpj(cotaDTO.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
                
                if(pessoa == null){
                    pessoa = new PessoaJuridica();
                }
            }
            else if (TipoPessoa.FISICA.equals(cotaDTO.getTipoPessoa())) {
                
                pessoa  = pessoaFisicaRepository.buscarPorCpf(cotaDTO.getNumeroCPF().replace(".", "").replace("-", "").trim());
                
                if(pessoa == null){
                    pessoa = new PessoaFisica();
                }
            }
        }
        return pessoa;
    }
    
    /**
     * Persiste e retorna os dados referente a entidade BaseReferenciaCota.
     * @param cota
     * @param cotaDto
     * @return BaseReferenciaCota
     */
    private BaseReferenciaCota processarDadosBaseReferenciaCota(final Cota cota , final CotaDTO cotaDto){
        
        BaseReferenciaCota baseReferenciaCota  = cota.getBaseReferenciaCota();
        
        if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
            
            if(baseReferenciaCota!= null){
                baseReferenciaCotaRepository.remover(baseReferenciaCota);
                return null;
            }
        }
        
        if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
            
            if (baseReferenciaCota == null ){
                baseReferenciaCota = new BaseReferenciaCota();
            }
            
            baseReferenciaCota.setInicioPeriodo(cotaDto.getInicioPeriodo());
            baseReferenciaCota.setFinalPeriodo(cotaDto.getFimPeriodo());
            
            cota.setBaseReferenciaCota(baseReferenciaCota);
            baseReferenciaCota = baseReferenciaCotaRepository.merge(baseReferenciaCota);
        }
        
        return  baseReferenciaCota;
        
    }
    
    /**
     * Persiste um conjunto de dados referente a entidade ReferenciaCota, associados a entidade BaseReferenciaCota
     * @param baseReferenciaCota
     * @param cotaDto
     */
    private void processarDadosReferenciaCota(final BaseReferenciaCota baseReferenciaCota, final CotaDTO cotaDto){
        
        if(baseReferenciaCota == null){
            
            validarParametrosBaseReferenciaCota(cotaDto);
        }
        else{
            
            validarCotaBaseIgual(cotaDto);
            
            validarPorcentagemCotaBase(cotaDto);
            
            Set<ReferenciaCota> referenciasCota = baseReferenciaCota.getReferenciasCota();
            
            if(referenciasCota != null && !referenciasCota.isEmpty()){
                
                referenciaCotaRepository.excluirReferenciaCota(baseReferenciaCota.getId());
            }
            
            referenciasCota = getReferenciasCota(baseReferenciaCota, cotaDto);
            
            if(!referenciasCota.isEmpty()){
                
                for(final ReferenciaCota ref : referenciasCota){
                    referenciaCotaRepository.merge(ref);
                }
            }
        }
    }
    
    /**
     * Valida os parâmetros referente ao objeto BaseReferenciaCota
     * 
     * @param cotaDto
     */
    private void validarParametrosBaseReferenciaCota(final CotaDTO cotaDto){
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(),cotaDto.getHistoricoPrimeiraPorcentagem())){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(),cotaDto.getHistoricoSegundaPorcentagem())){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(),cotaDto.getHistoricoTerceiraPorcentagem())){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
        }
    }
    
    /**
     * Retorna as Refencias de uma base referencia cota associada a uma cota
     * @param baseReferenciaCota
     * @param cotaDto
     * @return Set<ReferenciaCota>
     */
    private Set<ReferenciaCota> getReferenciasCota(final BaseReferenciaCota baseReferenciaCota, final CotaDTO cotaDto){
        
        final Set<ReferenciaCota> referenciasCota = new HashSet<ReferenciaCota>();
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(), cotaDto.getHistoricoPrimeiraPorcentagem())){
            
            referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoPrimeiraCota(),
                    cotaDto.getHistoricoPrimeiraPorcentagem(),
                    baseReferenciaCota) );
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(), cotaDto.getHistoricoSegundaPorcentagem())){
            
            referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoSegundaCota(),
                    cotaDto.getHistoricoSegundaPorcentagem(),
                    baseReferenciaCota) );
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(), cotaDto.getHistoricoTerceiraPorcentagem())){
            
            referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoTerceiraCota(),
                    cotaDto.getHistoricoTerceiraPorcentagem(),
                    baseReferenciaCota) );
            
        }
        
        return referenciasCota;
    }
    
    /**
     * Retorna um objeto do tipo ReferenciaCota
     * 
     * @param numeroCota - número da cota
     * @param porcentagem - porcentagem
     * @param baseReferenciaCota - objeto BaseReferenciaCota
     * @return ReferenciaCota
     */
    private ReferenciaCota getReferencaiCota(final Integer numeroCota,final BigDecimal porcentagem, final BaseReferenciaCota baseReferenciaCota){
        
        Cota cota = null;
        
        if(numeroCota != null){
            cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
        }
        
        final ReferenciaCota referenciaCota = new ReferenciaCota();
        referenciaCota.setCota(cota);
        referenciaCota.setPercentual(porcentagem);
        referenciaCota.setBaseReferenciaCota(baseReferenciaCota);
        
        return referenciaCota;
    }
    
    /**
     * Valida se os números de cota informado para histórico cota base são
     * iguais
     * 
     * @param cotaDTO
     */
    private void validarCotaBaseIgual(final CotaDTO cotaDTO){
        
        validarCotaIguais(cotaDTO.getHistoricoPrimeiraCota(), cotaDTO.getHistoricoSegundaCota(), cotaDTO.getHistoricoTerceiraCota());
        
        validarCotaIguais(cotaDTO.getHistoricoSegundaCota(),cotaDTO.getHistoricoPrimeiraCota(), cotaDTO.getHistoricoTerceiraCota());
        
        validarCotaIguais(cotaDTO.getHistoricoTerceiraCota(),cotaDTO.getHistoricoSegundaCota(),cotaDTO.getHistoricoPrimeiraCota());
    }
    
    /**
     * Verifica números de cotas iguais
     * 
     * @param param
     * @param param2
     * @param param3
     */
    private void validarCotaIguais(final Integer param,final Integer param2, final Integer param3){
        
        if (param != null && (param.equals(param2) || param.equals(param3))) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A cota " + param + "está incorreta! A cota " + param
                + " está duplicada!");
        }
        
    }
    
    /**
     * Valida se a porcentagem informada nas cotas histórico base obedecem 100
     * porcento.
     * 
     * @param cotaDto
     */
    private void validarPorcentagemCotaBase(final CotaDTO cotaDto) {
        
        boolean existeValor = false;
        BigDecimal valor  = BigDecimal.ZERO;
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(),
                cotaDto.getHistoricoPrimeiraPorcentagem())){
            
            valor = valor.add(cotaDto.getHistoricoPrimeiraPorcentagem());
            existeValor = true;
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(),
                cotaDto.getHistoricoSegundaPorcentagem())){
            
            valor = valor.add(cotaDto.getHistoricoSegundaPorcentagem());
            existeValor = true;
        }
        
        if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(),
                cotaDto.getHistoricoTerceiraPorcentagem())){
            
            valor = valor.add(cotaDto.getHistoricoTerceiraPorcentagem());
            existeValor = true;
        }
        
        if(existeValor && valor.intValue() != 100){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Porcentagem histórico cota base inválido! A porcentagem do histórico cota base deve ser 100%  ");
        }
        
    }
    
    private boolean tratarValorReferenciaCota(final Integer numeroCota, final BigDecimal porcentagem){
        return numeroCota != null && porcentagem != null;
    }
    
    /**
     * Verifica se a cota possui dividas em aberto
     * @param idCota
     * @return boolean
     */
    private boolean cotaComDebitos(final Long idCota){
        
        final BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(idCota);
        
        return dividasEmAberto!=null && dividasEmAberto.floatValue() > 0;
    }
    
    /**
     * Verifica se o número da cota existente pode ser utilizado por uma nova
     * cota
     * 
     * @param numeroCota - número da nova cota
     */
    private void processarNovoNumeroCota(final Integer numeroCota, final Long idCota){
        
        final Cota cota  = cotaRepository.obterPorNumeroDaCota(numeroCota);
        
        if(cota!= null){
            
            if(SituacaoCadastro.INATIVO.equals(cota.getSituacaoCadastro())){
                
                if(!isParametroDistribuidoNumeroCotaValido(numeroCota)){
                    
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Número da cota está inativo mas não pode ser utilizado.");
                }
                else{
                    // Alterar Numero Cota e registra histórico
                    alteraNumeroCota(cota);
                }
            }
            else{
                // Verifica se é edicao da cota
                if(!cota.getId().equals(idCota)){
                    throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota não pode ser utilizado.");
                }
            }
            
            // Verifica se cota possui dívidas em aberto
            final BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(cota.getId());
            if (dividasEmAberto!=null && dividasEmAberto.floatValue() > 0){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "O [Número] pertence à uma [Cota] que possui dívidas em aberto e não pode ser utilizado!");
            }
        }
    }
    
    /**
     * Altera o número da cota e gera um histporico com o número antigo
     * 
     * @param cota - objeto que sofrera a alteração de número de cota
     */
    private void alteraNumeroCota(final Cota cota){
        
        final Integer novoNumeroCota = getNovoNumeroCota(cota.getNumeroCota(), null,1);
        
        final Integer numeroCotaAntigo = cota.getNumeroCota();
        
        cota.setNumeroCota(novoNumeroCota);
        cotaRepository.merge(cota);
        
        final HistoricoNumeroCotaPK pk = new HistoricoNumeroCotaPK();
        pk.setDataAlteracao(new Date());
        pk.setIdCota(cota.getId());
        
        
        final HistoricoNumeroCota historicoNumeroCota = new HistoricoNumeroCota();
        historicoNumeroCota.setNumeroCota(numeroCotaAntigo);
        historicoNumeroCota.setPk(pk);
        
        historicoNumeroCotaRepository.merge(historicoNumeroCota);
        
    }
    
    /**
     * Retorna um número de sugestão para o cadastro de uma nova cota
     * 
     * @param numeroCota
     * @param novoNumeroCota
     * @param numero
     * @return Integer
     */
    private Integer getNovoNumeroCota(final Integer numeroCota, Integer novoNumeroCota ,Integer numero){
        
        final Cota cota  = cotaRepository.obterPorNumeroDaCota( novoNumeroCota == null ?numeroCota :novoNumeroCota);
        
        if(cota != null){
            novoNumeroCota = numero * 10000 + numeroCota;
            return getNovoNumeroCota(numeroCota,novoNumeroCota, ++numero);
        }
        
        return novoNumeroCota;
    }
    
    /**
     * Verifica se o numero da cota informado pode ser reaproveitado por outra cota em um novo cadastro
     * 
     * @param numeroCota
     * 
     * @return boolean
     */
    private boolean isParametroDistribuidoNumeroCotaValido(final Integer  numeroCota){
        
        final HistoricoSituacaoCota historicoSituacaoCota  =
                historicoSituacaoCotaRepository.obterUltimoHistorico(numeroCota, SituacaoCadastro.INATIVO);
        
        if (historicoSituacaoCota == null) {
            
            return true;
        }
        
        final Long qntDiasInativo =
                DateUtil.obterDiferencaDias(
                        historicoSituacaoCota.getDataInicioValidade(),
                        distribuidorRepository.obterDataOperacaoDistribuidor());
        
        final Long qntDiasInativoPermitidoParaReutilizacao =
                distribuidorService.qntDiasReutilizacaoCodigoCota();
        
        return qntDiasInativo > qntDiasInativoPermitidoParaReutilizacao;
    }
    
    /**
     * Método responsável por obter tipos de cota para preencher combo da camada
     * view
     * 
     * @return comboTiposCota: Tipos de cota padrão.
     */
    @Override
    @Transactional
    public List<ItemDTO<TipoCota, String>> getComboTiposCota() {
        final List<ItemDTO<TipoCota,String>> comboTiposCota =  new ArrayList<ItemDTO<TipoCota,String>>();
        for (final TipoCota itemTipoCota: TipoCota.values()){
            comboTiposCota.add(new ItemDTO<TipoCota,String>(itemTipoCota, itemTipoCota.getDescTipoCota()));
        }
        return comboTiposCota;
    }
    
    @Transactional
    @Override
    public void alterarCota(final Cota cota) {
        cotaRepository.alterar(cota);
    }
    
    /* (non-Javadoc)
     * @see br.com.abril.nds.service.CotaService#obterCotasEntre(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.model.cadastro.SituacaoCadastro)
     */
    @Override
    @Transactional
    public List<Cota> obterCotasEntre(final Intervalo<Integer> intervaloCota,
            final Intervalo<Integer> intervaloBox, final SituacaoCadastro situacao) {
        
        final List<Cota> listaCotas = new ArrayList<Cota>();
        final List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
        situacoesCadastro.add(situacao);
        
        final List<Long> idCotas = cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox, situacoesCadastro, null, null, null, null, null, null);
        
        for(final Long idCota : idCotas ) {
            
            final Cota cota = cotaRepository.buscarPorId(idCota);
            
            if (cota != null) {
                listaCotas.add(cota);
            }
        }
        
        return listaCotas;
    }
    
    @Override
    @Transactional
    public List<Cota> obterConjuntoCota(FiltroNFeDTO filtro) {
        return cotaRepository.obterConjuntoCota(filtro);
    }
    
    /**
     * Obtem cotas por intervalo de numero de cotas
     * @param cotaDe
     * @param cotaAte
     * @param situacoesCadastro
     * @return List<Cota>
     */
    @Override
    @Transactional
    public List<Cota> obterCotasEntre(final Integer cotaDe,
            final Integer cotaAte,
            final List<SituacaoCadastro> situacoesCadastro) {
        
        final List<Cota> cotas = cotaRepository.obterCotasIntervaloNumeroCota(cotaDe, cotaAte, situacoesCadastro);
        
        return cotas;
    }
    
    /**
     * Descarta Enderecos, Telefones, Garantias e Sócios da Cota na Troca de
     * Titularidade
     * 
     * @param cota
     */
    private void excluiEnderecosTelefonesGarantiasSociosCota(final Cota cota){
        
        final Set<EnderecoCota> enderecos = cota.getEnderecos();
        
        if (enderecos != null && !enderecos.isEmpty()) {
            
            final List<Endereco> listaEnderecos = new ArrayList<Endereco>();
            for (final EnderecoCota endereco : enderecos){
                
                listaEnderecos.add(endereco.getEndereco());
            }
            
            enderecoCotaRepository.removerEnderecosCota(cota.getId(), listaEnderecos);
        }
        
        
        final Set<TelefoneCota> telefones = cota.getTelefones();
        
        if (telefones != null && !telefones.isEmpty()) {
            
            final List<Long> idsTelefones = new ArrayList<Long>();
            for (final TelefoneCota telefoneCota : telefones){
                
                idsTelefones.add(telefoneCota.getTelefone().getId());
            }
            
            telefoneCotaRepository.removerTelefonesCota(idsTelefones);
        }
        
        
        final CotaGarantia garantia = cota.getCotaGarantia();
        
        if (garantia!=null){
            
            cotaGarantiaRepository.deleteListaImoveis(garantia.getId());
            
            cotaGarantiaRepository.deleteListaOutros(garantia.getId());
            
            cotaGarantiaRepository.deleteByCota(cota.getId());
        }
        
        
        final Set<SocioCota> socios = cota.getSociosCota();
        
        if (socios != null && !socios.isEmpty()) {
            
            socioCotaRepository.removerSociosCota(cota.getId());
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CotaDTO criarCotaTitularidade(final CotaDTO cotaDTO) {
        
        final Cota cotaAntiga = cotaRepository.buscarPorId(cotaDTO.getIdCota());
        
        final HistoricoTitularidadeCota historicoCota = historicoTitularidadeService.gerarHistoricoTitularidadeCota(cotaAntiga);
        
        final List<PDV> pdvs = cotaAntiga.getPdvs();
        final Set<Fornecedor> fornecedores = cotaAntiga.getFornecedores();
        final Set<DescontoProdutoEdicao> descontosProdutoEdicao = cotaAntiga.getDescontosProdutoEdicao();
        final ParametroCobrancaCota parametrosCobrancaCota = cotaAntiga.getParametroCobranca();
        final ParametroDistribuicaoCota parametroDistribuicaoCota = cotaAntiga.getParametroDistribuicao();
        
        final Set<HistoricoTitularidadeCota> titularesCota = cotaAntiga.getTitularesCota();
        titularesCota.add(historicoCota);
        
        final Long idCotaNova = this.salvarCota(cotaDTO);
        
        final Cota cotaNova = cotaRepository.buscarPorId(idCotaNova);
        cotaNova.setInicioTitularidade(distribuidorService.obterDataOperacaoDistribuidor());
        cotaNova.setPdvs(pdvs);
        cotaNova.setFornecedores(fornecedores);
        cotaNova.setDescontosProdutoEdicao(descontosProdutoEdicao);
        cotaNova.setParametroCobranca(parametrosCobrancaCota);
        cotaNova.setParametroDistribuicao(parametroDistribuicaoCota);
        cotaNova.setTitularesCota(titularesCota);
        cotaNova.setSituacaoCadastro(SituacaoCadastro.ATIVO);
        cotaNova.setTipoCota(TipoCota.CONSIGNADO);
        
        cotaRepository.merge(cotaNova);
        processarTitularidadeCota(cotaAntiga, cotaDTO);
        
        this.excluiEnderecosTelefonesGarantiasSociosCota(cotaNova);
        
        cotaDTO.setStatus(SituacaoCadastro.ATIVO);
        
        return cotaDTO;
    }
    
    @Override
    @Transactional
    public byte[] getDocumentoProcuracao(final Integer numeroCota) throws Exception {
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
        
        final ProcuracaoImpressaoDTO dto = new ProcuracaoImpressaoDTO();
        
        if (cota.getPessoa() instanceof PessoaFisica){
            
            final PessoaFisica pessoa = (PessoaFisica) cota.getPessoa();
            
            dto.setNomeJornaleiro(pessoa.getNome());
            dto.setNacionalidade(pessoa.getNacionalidade());
            dto.setEstadoCivil(pessoa.getEstadoCivil() == null ? "" : pessoa.getEstadoCivil().getDescricao());
            dto.setRgJornaleiro(pessoa.getRg());
            dto.setCpfJornaleiro(pessoa.getCpf());
        }
        
        dto.setBoxCota(cota.getBox().getNome());
        
        final EnderecoCota enderecoDoProcurado = enderecoCotaRepository.getPrincipal(cota.getId());
        
        if (enderecoDoProcurado != null){
            
            dto.setEnderecoDoProcurado(enderecoDoProcurado.getEndereco().getLogradouro());
            dto.setBairroProcurado(enderecoDoProcurado.getEndereco().getBairro());
        }
        
        final PDV pdv = pdvRepository.obterPDVPrincipal(cota.getId());
        
        if (pdv != null){
            
            dto.setNumeroPermissao(pdv.getLicencaMunicipal() != null ?
                    pdv.getLicencaMunicipal().getNumeroLicenca() : "");
            
            final Endereco enderecoPDV = enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
            
            if (enderecoPDV != null){
                
                dto.setEnderecoPDV(enderecoPDV.getLogradouro());
                dto.setCidadePDV(enderecoPDV.getCidade());
            }
            
            final Rota rota = rotaRepository.obterRotaPorPDV(pdv.getId(), cota.getId());
            
            if (rota != null){
                
                final Entregador entregador = entregadorRepository.obterEntregadorPorRota(rota.getId());
                
                if (entregador != null){
                    
                    if (entregador.getPessoa() instanceof PessoaFisica){
                        
                        final PessoaFisica pessoaFisica = (PessoaFisica) entregador.getPessoa();
                        
                        dto.setEstadoCivilProcurador(pessoaFisica.getEstadoCivil() == null ? "" : pessoaFisica.getEstadoCivil().getDescricao());
                        dto.setNacionalidadeProcurador(pessoaFisica.getNacionalidade());
                        dto.setNomeProcurador(pessoaFisica.getNome());
                        dto.setRgProcurador(pessoaFisica.getRg());
                    }
                }
            }
        }
        
        final List<ProcuracaoImpressaoDTO> listaDTO = new ArrayList<ProcuracaoImpressaoDTO>();
        listaDTO.add(dto);
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("cidade", dto.getCidadePDV());
        
        final String informacoesComplementares = distribuidorRepository.obterInformacoesComplementaresTermoAdesao();
        parameters.put("infoComp", informacoesComplementares!=null?informacoesComplementares:"");
        parameters.put("LOGO",JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor()));
        parameters.put("nomeDistribuidor", parametrosDistribuidorService.getParametrosDistribuidor()!=null?parametrosDistribuidorService.getParametrosDistribuidor().getNomeFantasia():"");
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
        
        final URL url =
                Thread.currentThread().getContextClassLoader().getResource("/reports/procuracao_subreport1.jasper");
        
        final String path = url.toURI().getPath();
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] getDocumentoTermoAdesao(final Integer numeroCota, final BigDecimal valorDebito, final BigDecimal percentualDebito) throws Exception {
        
        final TermoAdesaoDTO dto = new TermoAdesaoDTO();
        dto.setNumeroCota(numeroCota);
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
        
        dto.setNomeCota(cota.getPessoa().getNome());
        dto.setNomeDistribuidor(distribuidorRepository.obterRazaoSocialDistribuidor());
        dto.setValorDebito(valorDebito);
        dto.setPorcentagemDebito(percentualDebito);
        
        dto.setPeriodicidade("Semanal");
        
        final EnderecoCota enderecoCota =
                enderecoCotaRepository.obterEnderecoPorTipoEndereco(
                        cota.getId(), TipoEndereco.LOCAL_ENTREGA);
        
        if (enderecoCota != null) {
            
            final String numeroEndereco = enderecoCota.getEndereco().getNumero()!= null?enderecoCota.getEndereco().getNumero():"";
            
            dto.setLogradouroEntrega(enderecoCota.getEndereco().getLogradouro() + ", N&deg; " + numeroEndereco);
            dto.setBairroEntrega(enderecoCota.getEndereco().getBairro());
            dto.setCEPEntrega(Util.adicionarMascaraCEP(enderecoCota.getEndereco().getCep()));
            dto.setCidadeEntrega(enderecoCota.getEndereco().getCidade());
            
        } else {
            
            final PDV pdv = pdvRepository.obterPDVPrincipal(cota.getId());
            
            if (pdv != null) {
                
                dto.setReferenciaEndereco(pdv.getPontoReferencia());
                dto.setHorariosFuncionamento(pdv.getPeriodos()!=null?pdv.getPeriodos().size()>0?pdv.getPeriodos():null:null);
                
                final Endereco enderecoPDV = enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
                
                if (enderecoPDV != null) {
                    
                    final String numeroEndereco =  enderecoPDV.getNumero()!=null ? enderecoPDV.getNumero():"";
                    
                    dto.setLogradouroEntrega(enderecoPDV.getLogradouro() + ", N&deg; " + numeroEndereco);
                    dto.setBairroEntrega(enderecoPDV.getBairro());
                    dto.setCEPEntrega(Util.adicionarMascaraCEP(enderecoPDV.getCep()));
                    dto.setCidadeEntrega(enderecoPDV.getCidade());
                }
            }
        }
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("SUBREPORT_DIR", Thread.currentThread().getContextClassLoader().getResource("/reports/").toURI().getPath());
        
        final String informacoesComplementares = distribuidorRepository.obterInformacoesComplementaresTermoAdesao();
        parameters.put("infoComp", informacoesComplementares != null ? informacoesComplementares : "");
        parameters.put("LOGO",JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor()));
        
        final List<TermoAdesaoDTO> listaDTO = new ArrayList<TermoAdesaoDTO>();
        listaDTO.add(dto);
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
        
        final URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/termo_adesao.jasper");
        
        final String path = url.toURI().getPath();
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }
    
    @Override
    @Transactional
    public List<Fornecedor> obterFornecedoresCota(final Long idCota) {
        final Cota cota = this.obterPorId(idCota);
        final Set<Fornecedor> fornecedores = cota.getFornecedores();
        final List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
        for(final Fornecedor itemFornecedor:fornecedores){
            listaFornecedores.add(itemFornecedor);
        }
        return listaFornecedores;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CotaDTO obterHistoricoTitularidade(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        final CotaDTO dto = HistoricoTitularidadeCotaDTOAssembler.toCotaDTO(historico);
        return dto;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidade(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<EnderecoAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toEnderecoAssociacaoDTOCollection(historico.getEnderecos()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidade(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<TelefoneAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toTelefoneAssociacaoDTOCollection(historico.getTelefones()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<FornecedorDTO> obterFornecedoresHistoricoTitularidadeCota(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<FornecedorDTO>(HistoricoTitularidadeCotaDTOAssembler.toFornecedorDTOCollection(historico.getFornecedores()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TipoDescontoProdutoDTO> obterDescontosProdutoHistoricoTitularidadeCota(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        final List<TipoDescontoProdutoDTO> dtos = new ArrayList<TipoDescontoProdutoDTO>();
        for (final HistoricoTitularidadeCotaDescontoProduto desconto : historico.getDescontosProduto()) {
            dtos.add(new TipoDescontoProdutoDTO(desconto.getCodigo(), desconto
                    .getNome(), desconto.getNumeroEdicao(), desconto
                    .getDesconto(), desconto.getAtualizacao(), null, false));
        }
        return dtos;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TipoDescontoCotaDTO> obterDescontosCotaHistoricoTitularidadeCota(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        final List<TipoDescontoCotaDTO> dtos = new ArrayList<TipoDescontoCotaDTO>();
        for (final HistoricoTitularidadeCotaDescontoCota desconto : historico.getDescontosCota()) {
            dtos.add(new TipoDescontoCotaDTO(desconto.getDesconto(), desconto
                    .getFornecedor(), desconto.getAtualizacao(), desconto
                    .getTipoDesconto().getDescricao()));
        }
        return dtos;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public DistribuicaoDTO obterDistribuicaoHistoricoTitularidade(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        
        final HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        final HistoricoTitularidadeCotaDistribuicao distribuicao = historico.getDistribuicao();
        if (distribuicao != null) {
            distribuicao.setHistoricoTitularidadeCota(historico);
            return HistoricoTitularidadeCotaDTOAssembler.toDistribuicaoDTO(distribuicao);
        }
        return new DistribuicaoDTO();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeCotas(final SituacaoCadastro situacaoCadastro) {
        
        return cotaRepository.obterQuantidadeCotas(situacaoCadastro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CotaResumoDTO> obterCotas(final SituacaoCadastro situacaoCadastro) {
        
        return cotaRepository.obterCotas(situacaoCadastro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CotaResumoDTO> obterCotasComInicioAtividadeEm(final Date dataInicioAtividade) {
        
        return cotaRepository.obterCotasComInicioAtividadeEm(dataInicioAtividade);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(final Date dataExpedicaoReparte) {
        
        return cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataExpedicaoReparte);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(final Date dataRecolhimentoEncalhe) {
        
        return cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataRecolhimentoEncalhe);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasQueEnquadramNoRangeDeReparte(final BigInteger qtdReparteInicial, final BigInteger qtdReparteFinal, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas){
        return cotaRepository.buscarCotasQuePossuemRangeReparte(qtdReparteInicial, qtdReparteFinal, listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isCotaOperacaoDiferenciada(final Integer numeroCota, Date dataOperacao){
        
        final List<DiaSemana> diasSemanaOperacaoDiferenciada = grupoRepository.obterDiasOperacaoDiferenciadaCota(numeroCota, dataOperacao);
        
        return diasSemanaOperacaoDiferenciada != null && !diasSemanaOperacaoDiferenciada.isEmpty();
        
    }
    
    @Transactional(readOnly = true)
    @Override
    public boolean isTipoCaracteristicaSegmentacaoConvencional(final Long idCota) {
        
        return TipoDistribuicaoCota.CONVENCIONAL.equals(cotaRepository.obterTipoDistribuicao(idCota));
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasQueEnquadramNoRangeVenda(final BigInteger qtdVendaInicial, final BigInteger qtdVendaFinal, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        return cotaRepository.buscarCotasQuePossuemRangeVenda(qtdVendaInicial, qtdVendaFinal, listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(final BigDecimal percentVenda, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        return cotaRepository.buscarCotasQuePossuemPercentualVendaSuperior(percentVenda, listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasPorNomeOuNumero(final CotaDTO cotaDto, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        return cotaRepository.buscarCotasPorNomeOuNumero(cotaDto, listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasPorComponentes(final ComponentesPDV componente, final String elemento, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        return cotaRepository.buscarCotasPorComponentes(componente, elemento, listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<AnaliseHistoricoDTO> buscarHistoricoCotas(final List<ProdutoEdicaoDTO> listProdutoEdicaoDto,
            final List<Integer> cotas, final String sortorder, final String sortname) {
        
        final List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO = cotaRepository.buscarHistoricoCotas(listProdutoEdicaoDto, cotas);
        
        for (final AnaliseHistoricoDTO analiseHistoricoDTO : listAnaliseHistoricoDTO) {
            
            int qtdEdicaoVendida = 0;
            
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                final ProdutoEdicaoDTO produtoEdicaoDTO = listProdutoEdicaoDto.get(i);
                
                ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
                
                if(produtoEdicaoDTO.isParcial() && produtoEdicaoDTO.isParcialConsolidado() == false){
                	dto = produtoEdicaoRepository.obterHistoricoProdutoEdicaoParcial(produtoEdicaoDTO.getId(), produtoEdicaoDTO.getPeriodo(), 
                													analiseHistoricoDTO.getNumeroCota(), produtoEdicaoDTO.getNumeroEdicao());
                }else{
                	dto = produtoEdicaoRepository.obterHistoricoProdutoEdicao(produtoEdicaoDTO.getCodigoProduto(), produtoEdicaoDTO.getNumeroEdicao(), 
            														analiseHistoricoDTO.getNumeroCota());
                }
                
                if (dto != null && dto.getId() != null) {

                	if(dto.getStatusLancamento() != null && dto.getStatusLancamento().equalsIgnoreCase("FECHADO") && 
                			(dto.getQtdeVendas() == null || dto.getQtdeVendas().compareTo(BigInteger.ZERO) <= 0) && 
                			 produtoEdicaoDTO.isParcial() == false){
                		
                		BigDecimal venda = estoqueProdutoService.obterVendaCotaBaseadoNoEstoque(dto.getId(), analiseHistoricoDTO.getNumeroCota()); 
                		
                		if(venda != null){
                			dto.setQtdeVendas(venda.toBigInteger());
                		}
                		
                	}
                	
                    qtdEdicaoVendida++;
                    
                    if (i == 0) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd1Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd1Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                    
                    if (i == 1) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd2Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd2Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                    
                    if (i == 2) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd3Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd3Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                    
                    if (i == 3) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd4Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd4Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                    
                    if (i == 4) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd5Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd5Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                    
                    if (i == 5) {
                        if(dto.getReparte() != null){
                            analiseHistoricoDTO.setEd6Reparte(dto.getReparte().intValue());
                        }
                        
                        if(dto.getQtdeVendas() != null){
                            analiseHistoricoDTO.setEd6Venda(dto.getQtdeVendas().intValue());
                        }
                    }
                }
            }
            
            setMediaVendaEReparte(qtdEdicaoVendida, analiseHistoricoDTO);
        }
        
        formatarListaHistoricoVenda(listAnaliseHistoricoDTO);
        ordenarListaHistoricoVenda(sortorder, sortname, listAnaliseHistoricoDTO);
        
        return listAnaliseHistoricoDTO;
    }
    
    private void formatarListaHistoricoVenda(final List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO) {
        
        for (final AnaliseHistoricoDTO dto : listAnaliseHistoricoDTO) {
            
            if(dto.getReparteMedio() == 0){
                
                dto.setReparteMedio(null);
            }
            
            if(dto.getVendaMedia() == 0){
                
                dto.setVendaMedia(null);
            }
        }
    }
    
    private void ordenarListaHistoricoVenda(final String sortorder, final String sortname, final List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO) {
        if(!StringUtils.equals(sortorder, "undefined")){
            
            if(sortname != null){
                if ("ed1Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed1Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed1Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed1Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed2Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed2Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed2Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed2Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed3Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed3Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed3Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed3Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed4Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed4Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed4Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed4Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed5Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed5Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed5Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed5Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed6Reparte".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed6Reparte", listAnaliseHistoricoDTO, Integer.class);
                } else if ("ed6Venda".equals(sortname)) {
                    ListUtils.orderList(sortorder, "ed6Venda", listAnaliseHistoricoDTO, Integer.class);
                } else if ("reparteMedio".equals(sortname)) {
                    ListUtils.orderList(sortorder, "reparteMedio", listAnaliseHistoricoDTO);
                } else if ("vendaMedia".equals(sortname)) {
                    ListUtils.orderList(sortorder, "vendaMedia", listAnaliseHistoricoDTO);
                }
            }
        }
    }
    
    private void setMediaVendaEReparte(final int qtdEdicoes, final AnaliseHistoricoDTO analiseHistoricoDTO){
        Double reparteMedio = 0.0;
        Double vendaMedia = 0.0;
        
        reparteMedio += analiseHistoricoDTO.getEd1Reparte() == null ? 0 : analiseHistoricoDTO.getEd1Reparte();
        reparteMedio += analiseHistoricoDTO.getEd2Reparte() == null ? 0 : analiseHistoricoDTO.getEd2Reparte();
        reparteMedio += analiseHistoricoDTO.getEd3Reparte() == null ? 0 : analiseHistoricoDTO.getEd3Reparte();
        reparteMedio += analiseHistoricoDTO.getEd4Reparte() == null ? 0 : analiseHistoricoDTO.getEd4Reparte();
        reparteMedio += analiseHistoricoDTO.getEd5Reparte() == null ? 0 : analiseHistoricoDTO.getEd5Reparte();
        reparteMedio += analiseHistoricoDTO.getEd6Reparte() == null ? 0 : analiseHistoricoDTO.getEd6Reparte();
        
        vendaMedia += analiseHistoricoDTO.getEd1Venda() == null ? 0 : analiseHistoricoDTO.getEd1Venda();
        vendaMedia += analiseHistoricoDTO.getEd2Venda() == null ? 0 : analiseHistoricoDTO.getEd2Venda();
        vendaMedia += analiseHistoricoDTO.getEd3Venda() == null ? 0 : analiseHistoricoDTO.getEd3Venda();
        vendaMedia += analiseHistoricoDTO.getEd4Venda() == null ? 0 : analiseHistoricoDTO.getEd4Venda();
        vendaMedia += analiseHistoricoDTO.getEd5Venda() == null ? 0 : analiseHistoricoDTO.getEd5Venda();
        vendaMedia += analiseHistoricoDTO.getEd6Venda() == null ? 0 : analiseHistoricoDTO.getEd6Venda();
        
        analiseHistoricoDTO.setReparteMedio((double)Math.round(reparteMedio / qtdEdicoes));
        analiseHistoricoDTO.setVendaMedia((double)Math.round(vendaMedia / qtdEdicoes));
    }
    
    @Transactional(readOnly = true)
    @Override
    public HistoricoVendaPopUpCotaDto buscarCota(final Integer numero) {
        return cotaRepository.buscarCota(numero);
    }
    
    @Transactional
    @Override
    public void apagarTipoCota(final Long idCota, final String TipoCota) {
        LOGGER.info("CotaServiceImpl.apagarTipoCota");
        
        if(idCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada inválida!");
        }
        
        if (TipoCota==null || TipoCota.isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de Cota inválida");
        }
        
        try{
            if ("ALTERNATIVO".equalsIgnoreCase(TipoCota)) {
            	
            	fixacaoReparteService.excluirFixacaoPorCota(idCota);
            	
            } else if ("CONVENCIONAL".equalsIgnoreCase(TipoCota)) {
            	
            	mixCotaProdutoService.excluirMixPorCota(idCota);
            }
            
        } catch (final DataIntegrityViolationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.ERROR, "Exclusão não permitida, registro possui dependências!");
        }
    }
    
    @Transactional
    @Override
    public List<DistribuidorClassificacaoCota> obterListaClassificacao() {
        return distribuidorClassificacaoCotaRepository.buscarTodos();
    }
    
    @Transactional
    @Override
    public List<Integer> numeroCotaExiste(final TipoDistribuicaoCota tipoDistribuicaoCota, final Integer... cotaIdArray) {
        return cotaRepository.numeroCotaExiste(tipoDistribuicaoCota, cotaIdArray);
    }
    
    @Transactional
    @Override
    public boolean cotaVinculadaCotaBase(final Long idCota) {
        return cotaRepository.cotaVinculadaCotaBase(idCota);
    }
    
    @Transactional
    @Override
    public List<CotaDTO> obterPorNomeAutoComplete(final String nome) {
        return cotaRepository.obterCotasPorNomeAutoComplete(nome);
    }
    
    @Transactional(readOnly = true)
    @Override
    public TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(final Integer numeroCota) {
        
        return cotaRepository.obterTipoDistribuicaoCotaPorNumeroCota(numeroCota);
    }
    
    @Transactional
    @Override
    public boolean isTipoDistribuicaoCotaEspecifico(final Integer numeroCota, final TipoDistribuicaoCota tipoDistribuicaoCota) {
        
        final TipoDistribuicaoCota tpDistribuicaoCota = obterTipoDistribuicaoCotaPorNumeroCota(numeroCota);
        
        return tpDistribuicaoCota != null && tpDistribuicaoCota.equals(tipoDistribuicaoCota);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CotaDTO> buscarCotasHistorico(final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        
        return cotaRepository.buscarCotasHistorico(listProdutoEdicaoDto, cotasAtivas);
    }
    
    @Transactional(readOnly = true)
    @Override
    public void verificarCotasSemRoteirizacao(final Intervalo<Integer> intervaloCota, final Intervalo<Date> intervaloDataLancamento,
            final Intervalo<Date> intervaloDataRecolhimento){
        
        final List<CotaDTO> cotasSemRoteirizacao = cotaRepository.obterCotasSemRoteirizacao(intervaloCota, intervaloDataLancamento, intervaloDataRecolhimento);
        
        if (cotasSemRoteirizacao != null && !cotasSemRoteirizacao.isEmpty()) {
            
            final List<String> msgs = new ArrayList<String>();
            msgs.add("Cotas sem roteirização:");
            for (final CotaDTO cota : cotasSemRoteirizacao) {
                msgs.add(cota.getNumeroCota() + " - " + cota.getNomePessoa());
            }
            
            throw new ValidacaoException(TipoMensagem.WARNING, msgs);
        }
    }
    
    /**
     * Obtem lista de Cotas dos numeros de cotas passados como parametro
     * @param numerosCota
     * @return List<Cota>
     */
    @Transactional(readOnly = true)
    @Override
    public List<Cota> obterCotasPorNumeros(final List<Integer> numerosCota){
        
        final List<Cota> cotas = new ArrayList<Cota>();
        
        for (final Integer numeroCota : numerosCota){
            
            final Cota cota = this.obterPorNumeroDaCota(numeroCota);
            
            cotas.add(cota);
        }
        
        return cotas;
    }
    
    /**
     * Altera politica de suspensão da Cota
     * 
     * @param cota
     * @param sugereSuspensao
     * @param sugereSuspensaoDistribuidor
     * @param qtdDividasAberto
     * @param vrDividasAberto
     * @return boolean
     */
    private boolean salvarPoliticaSuspensaoCota(Cota cota,
    		                                    boolean sugereSuspensao,
    		                                    boolean sugereSuspensaoDistribuidor,
                                                Integer qtdDividasAberto,
                                                BigDecimal vrDividasAberto){
    	
    	if (sugereSuspensaoDistribuidor){

    		if (cota.isSugereSuspensaoDistribuidor() != sugereSuspensaoDistribuidor){
    			
    			cota.setSugereSuspensaoDistribuidor(sugereSuspensaoDistribuidor);
    			
    			return true;
    		}
    		
    		return false;
    	}
    	
    	cota.setSugereSuspensaoDistribuidor(sugereSuspensaoDistribuidor);
    	
    	PoliticaSuspensao ps = cota.getPoliticaSuspensao();
    	
    	if (ps==null){
    		
    		ps = new PoliticaSuspensao();
    		
    		cota.setPoliticaSuspensao(ps);
    	}
    	
    	BigInteger qtdAcumuloAtual = BigIntegerUtil.valueOfInteger(ps.getNumeroAcumuloDivida());
    			
    	BigInteger qtdAcumuloInformado = BigIntegerUtil.valueOfInteger(qtdDividasAberto);	
    	
    	if ((cota.isSugereSuspensao() != sugereSuspensao) ||
    		(BigIntegerUtil.compareToTryNull(qtdAcumuloAtual, qtdAcumuloInformado) != 0) ||
            (BigDecimalUtil.compareToTryNull(ps.getValor(), vrDividasAberto) != 0)){
    		
    		ps.setNumeroAcumuloDivida(qtdDividasAberto);
    		
    		ps.setValor(vrDividasAberto);
    		
    		cota.setSugereSuspensao(sugereSuspensao);
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Salva as caracteristicas financeiras especificas da Cota
     * 
     * @param parametroCobranca
     * @return boolean
     */
    @Transactional
    @Override
	public boolean salvarCaracteristicasFinanceirasEspecificasCota(final ParametroCobrancaCotaDTO parametroCobranca){
        
    	boolean alterado = false;
    	
        final Cota cota = this.obterPorId(parametroCobranca.getIdCota());
        
        BigDecimal valorMinimoCobranca = parametroCobranca.getValorMinimoBigDecimal();
        
        if (BigDecimalUtil.compareToTryNull(cota.getValorMinimoCobranca(), valorMinimoCobranca) != 0){
            
            cota.setValorMinimoCobranca(valorMinimoCobranca);
            
            alterado = true;
        }
        
        if ((cota.isDevolveEncalhe()==null) || (parametroCobranca.isDevolveEncalhe() != cota.isDevolveEncalhe())){
                
            cota.setDevolveEncalhe(parametroCobranca.isDevolveEncalhe());

            alterado = true;
        }
        
        boolean politicaSuspensaoAlterado = this.salvarPoliticaSuspensaoCota(cota, 
        		                                                             parametroCobranca.isSugereSuspensao(), 
        		                                                             parametroCobranca.isSugereSuspensaoDistribuidor(),
        		                                                             parametroCobranca.getQtdDividasAberto(), 
        		                                                             parametroCobranca.getVrDividasAbertoBigDecimal());
        
        if (politicaSuspensaoAlterado){
        	
        	alterado = true;
        }
        
        if (alterado){
        	
        	this.alterarCota(cota);
        }
        
        return alterado;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Long obterIdPorNumeroCota(final Integer numeroCota) {
        
        return cotaRepository.obterIdPorNumeroCota(numeroCota);
    }

    @Override
    @Transactional(readOnly=true)
    public String obterEmailCota(Integer numeroCota) {
        
        if (numeroCota == null){
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota é obritatório.");
        }
        
        return this.cotaRepository.obterEmailCota(numeroCota);
    }
    
    @Override
    @Transactional
    public List<ParametroDistribuicaoEntregaCotaDTO> obterParametrosDistribuicaoEntregaCota() {
    	
    	return cotaRepository.obterParametrosDistribuicaoEntregaCota();
    }
    
    @Override
    @Transactional
    public void validarTipoEntrega(Integer numeroCota,DescricaoTipoEntrega tipoEntrega) {
    	
    	if(tipoEntrega == null ||
    			DescricaoTipoEntrega.COTA_RETIRA.equals(tipoEntrega)){
    		return;
    	}
    	
    	Long quantidadeEntregaTransportador = motoristaRotaRepository.obterQuantidadeAssociaoesDaCota(numeroCota);
    	
    	if(DescricaoTipoEntrega.ENTREGA_EM_BANCA.equals(tipoEntrega)){
    		
    		if(quantidadeEntregaTransportador < 1){
    			
    			 throw new ValidacaoException(
    					 TipoMensagem.WARNING, 
    					 "Tipo de Entrega não pode ser selecionado. A roteirização da cota não está associada a um Transportador");
    		}
    		
    	}
    	else if(DescricaoTipoEntrega.ENTREGADOR.equals(tipoEntrega)){
    		
    		if(quantidadeEntregaTransportador > 0){
    			
    			 throw new ValidacaoException(
    					 TipoMensagem.WARNING, 
    					 "Tipo de Entrega não pode ser selecionado. A roteirização da cota está associada a um Transportador");
    		}
    	}
    }
    
    @Override
    @Transactional
    public Boolean validarNumeroCota(Integer numeroCota, TipoDistribuicaoCota tipoDistribuicaoCota){
    	return cotaRepository.validarNumeroCota(numeroCota, tipoDistribuicaoCota);
    }

	@Override
	@Transactional
	public List<AbastecimentoBoxCotaDTO> obterCotasExpedicao(Intervalo<Date> intervaloData) {
		return cotaRepository.obterCotasExpedicao(intervaloData);
	}
	
	@Transactional
	@Override
	public Map<Integer, AnaliseHistoricoXLSDTO> dadosPDVhistoricoXLS(List<Integer> cotas){
		return cotaRepository.buscarDadosPdvParaXLS(cotas);
	}
}
