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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TermoAdesaoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.TitularidadeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
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
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCota;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCotaPK;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametrosCotaNotaFiscalEletronica;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
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
import br.com.abril.nds.repository.BaseReferenciaCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoPDVRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.HistoricoNumeroCotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PessoaFisicaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.ReferenciaCotaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.SocioCotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.EnderecoService;
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
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.ListUtils;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

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
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
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
	private RankingRepository rankingRepository;
	
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
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> obterCotas(FiltroCotaDTO filtro) {

		return cotaRepository.obterCotas(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro) {

		return cotaRepository.obterQuantidadeCotasPesquisadas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		return this.cotaRepository.obterPorNumeroDaCota(numeroCota);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCotaAtiva(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCota(numeroCota);
	}

	@Transactional(readOnly = true)
	public List<Cota> obterCotasPorNomePessoa(String nome) {
		
		return this.cotaRepository.obterCotasPorNomePessoa(nome);
	}

	@Transactional(readOnly = true)
	public List<Cota> obterPorNome(String nome) {
		
		List<Cota> listaCotas = this.cotaRepository.obterPorNome(nome);
		
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
	public Cota obterPorId(Long idCota) {

		if (idCota == null) {

            throw new ValidacaoException(TipoMensagem.ERROR, "Id da cota não pode ser nulo.");
		}
		
		return this.cotaRepository.buscarPorId(idCota);
	}
	
	/**
	 * ENDERECO
	 * 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public EnderecoCota obterEnderecoPrincipal(long idCota) {

		return this.cotaRepository.obterEnderecoPrincipal(idCota);
	}
	
	/**
	 * ENDERECO
	 * 
	 * @see br.com.abril.nds.service.CotaService#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if(cota == null)
            throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		
		Long idPessoa = cota.getPessoa().getId();
		
		Set<Long> endRemover = new HashSet<Long>();
		
		List<EnderecoAssociacaoDTO> listRetorno = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<EnderecoAssociacaoDTO> listaEnderecolAssoc = cotaRepository.obterEnderecosPorIdCota(idCota);
			
		if(listaEnderecolAssoc!= null && !listaEnderecolAssoc.isEmpty()){
			
			listRetorno.addAll(listaEnderecolAssoc);

			for (EnderecoAssociacaoDTO dto : listaEnderecolAssoc){
				
				endRemover.add(dto.getEndereco().getId());
			}
		}
		else{
		
			List<EnderecoAssociacaoDTO> lista = this.enderecoService.buscarEnderecosPorIdPessoa(idPessoa, endRemover);
			
			if (lista!= null && !lista.isEmpty()){
				
				listRetorno.addAll(lista);
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
	public void processarEnderecos(Long idCota,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover
								   ) {

		if (idCota == null){
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosCota(cota, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosCota(cota, listaEnderecoAssociacaoRemover);
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
	private Endereco obterEndereco(EnderecoDTO enderecoDTO, Pessoa pessoa, boolean novo){
		
		Endereco endereco = new Endereco();
		
		if (!novo){
			
			endereco = this.enderecoRepository.buscarPorId(enderecoDTO.getId());
		}

		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setCep(enderecoDTO.getCep());
		endereco.setCodigoCidadeIBGE(enderecoDTO.getCodigoCidadeIBGE());
		endereco.setCidade(enderecoDTO.getCidade());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
		endereco.setLogradouro(enderecoDTO.getLogradouro());
		endereco.setNumero(enderecoDTO.getNumero());
		endereco.setUf(enderecoDTO.getUf());
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
	private void salvarEnderecosCota(Cota cota, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		Pessoa pessoa = cota.getPessoa();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
			
			if (enderecoAssociacao.getTipoEndereco() == null){
				
				continue;
			}
            
			EnderecoDTO enderecoDTO = enderecoAssociacao.getEndereco();
			    
			this.enderecoService.validarEndereco(enderecoDTO, enderecoAssociacao.getTipoEndereco());
			
			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
			
			Endereco endereco = null;
			
			boolean novoEnderecoCota = false;
			
			if (enderecoCota == null) {

				novoEnderecoCota = true;
				
				enderecoCota = new EnderecoCota();

				enderecoCota.setCota(cota);
			}
			
			boolean novoEndereco = (novoEnderecoCota && !enderecoAssociacao.isEnderecoPessoa());
			
			endereco = this.obterEndereco(enderecoDTO, pessoa, novoEndereco);

            enderecoCota.setEndereco(endereco);

			enderecoCota.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoCota.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
				
			this.enderecoCotaRepository.merge(enderecoCota);
		}
	}

	/**
	 * ENDERECO
	 * 
	 * Remove lista de EnderecoCota
	 * @param cota
	 * @param listaEnderecoAssociacao
	 */
	private void removerEnderecosCota(Cota cota,
									  List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<EnderecoDTO> listaEndereco = new ArrayList<EnderecoDTO>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
			
			if(enderecoAssociacao!= null){
				
				listaEndereco.add(enderecoAssociacao.getEndereco());

				EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
				
				if(enderecoCota!= null){
	
					idsEndereco.add(enderecoAssociacao.getEndereco().getId());
	
					this.enderecoCotaRepository.remover(enderecoCota);
				}
			}
		}
		
		if(!idsEndereco.isEmpty()){
			
			this.enderecoService.removerEnderecos(idsEndereco);
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
	@SuppressWarnings("unused")
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		
		if (idCota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}

		Cota cota = cotaRepository.buscarPorId(idCota);
		
		Long idPessoa = cota.getPessoa().getId();
		
		Set<Long> telRemover = new HashSet<Long>();
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
			
		for (TelefoneAssociacaoDTO dto : listaTelAssoc){
				
			telRemover.add(dto.getTelefone().getId());
		}
		
		List<TelefoneAssociacaoDTO> lista = this.telefoneService.buscarTelefonesPorIdPessoa(idPessoa, telRemover);
		
		if (lista!= null && !lista.isEmpty()){
			
			if(listaTelAssoc==null)
				listaTelAssoc = new ArrayList<TelefoneAssociacaoDTO>();
			
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
	@Transactional
	public void processarTelefones(Long idCota, 
			List<TelefoneAssociacaoDTO> listaTelefonesAdicionar, 
			Collection<Long> listaTelefonesRemover){
		
		if (idCota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		this.salvarTelefonesCota(cota, listaTelefonesAdicionar);
		
		this.removerTelefonesCota(listaTelefonesRemover);
		
		List<Telefone> listaTelefone = new ArrayList<Telefone>();
		
		for (TelefoneAssociacaoDTO telefoneCota : listaTelefonesAdicionar){
		    TelefoneDTO dto = telefoneCota.getTelefone();
		    Telefone telefone = new Telefone(dto.getId(), dto.getNumero(), dto.getRamal(), dto.getDdd(), cota.getPessoa());
            listaTelefone.add(telefone);
		}
		
		cota.getPessoa().setTelefones(listaTelefone);
		
		this.cotaRepository.alterar(cota);
		
	}

	/**
	 * TELEFONE
	 * 
	 * Persiste Telefones
	 * @param cota
	 * @param listaTelefonesCota
	 */
	private void salvarTelefonesCota(Cota cota, List<TelefoneAssociacaoDTO> listaTelefonesCota) {
		
		Pessoa pessoa = cota.getPessoa();
        
		if (listaTelefonesCota != null){
			
			for (TelefoneAssociacaoDTO dto : listaTelefonesCota) {
				
				TelefoneCota telefoneCota = null;
				
				TelefoneDTO telefoneDTO = dto.getTelefone();
				
				this.telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
				
                if(telefoneDTO!= null && telefoneDTO.getId()!= null){
					telefoneCota = cotaRepository.obterTelefonePorTelefoneCota(telefoneDTO.getId(), cota.getId());
				}
				
				if(telefoneCota == null){
					telefoneCota = new TelefoneCota();
					telefoneCota.setCota(cota);
				}
				Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), pessoa);
				telefoneCota.setPrincipal(dto.isPrincipal());
				telefoneCota.setTelefone(telefone);
				telefoneCota.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneCotaRepository.merge(telefoneCota);
			}
		}
	}

	/**
	 * TELEFONE
	 * 
	 * Remove Telefones
	 * @param listaTelefonesCota
	 */
	private void removerTelefonesCota(Collection<Long> listaTelefonesCota) {
		
		if (listaTelefonesCota != null && !listaTelefonesCota.isEmpty()){
			this.telefoneCotaRepository.removerTelefonesCota(listaTelefonesCota);
			
			this.telefoneService.removerTelefones(listaTelefonesCota);
		}
	}

	@Transactional(readOnly = true)
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota) {	
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		return cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota, false, dataOperacao);
	}

	@Override
	@Transactional
	public List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario, MotivoAlteracaoSituacao.INADIMPLENCIA);
		
		List<CotaSuspensaoDTO> cotasDTO = new ArrayList<CotaSuspensaoDTO>();
		
		for(Cota cota : cotasSuspensas) {
			
			Pessoa pessoa = cota.getPessoa();
			
			String nome = pessoa instanceof PessoaFisica ? 
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
	public List<Cota> suspenderCotas(List<Long> idCotas, Long idUsuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {

		List<Cota> cotasSuspensas = new ArrayList<Cota>();
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		Date dataInicioValidade = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		for(Long id:idCotas) {	
			
			cotasSuspensas.add(suspenderCota(id, usuario, dataInicioValidade, motivoAlteracaoSituacao));
		}	
		
		return cotasSuspensas;
	}

	@Override
	@Transactional
	public Cota suspenderCota(Long idCota, Usuario usuario, Date dataInicioValidade, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {
				
		Cota cota = obterPorId(idCota);
		
		if(SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "A cota já está suspensa!");
		}
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setDataEdicao(new Date());
		historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuario);
		historico.setMotivo(motivoAlteracaoSituacao);
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		historico.setDataInicioValidade(dataInicioValidade);
		historico.setProcessado(true);
		
		this.historicoSituacaoCotaRepository.merge(historico);
		
		cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
		
		this.cotaRepository.alterar(cota);
		
		this.situacaoCotaService.removerAgendamentosAlteracaoSituacaoCota(cota.getId());
		
		return cota;
	}
	
	@Override
	@Transactional
	public List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer inicio, Integer rp) {
		
		List<CotaSuspensaoDTO> cotasSujeitasSuspensao = 
			this.cotaRepository.obterCotasSujeitasSuspensao(sortOrder, sortColumn, inicio, rp, 
					this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
		return cotasSujeitasSuspensao;		
	}

	@Override
	@Transactional
	public Long obterTotalCotasSujeitasSuspensao() {
		return cotaRepository.obterTotalCotasSujeitasSuspensao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
	}
	
	@Override
	@Transactional
	public BigDecimal obterTotalDividaCotasSujeitasSuspensao() {
		
		return cotaRepository.obterTotalDividaCotasSujeitasSuspensao(
			this.distribuidorRepository.obterDataOperacaoDistribuidor());
	}
	
	@Override
	@Transactional
	public String obterNomeResponsavelPorNumeroDaCota(Integer numeroCota){
		
		if (numeroCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório");
		}
		
		Cota cota = this.obterPorNumeroDaCota(numeroCota);
		
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
	public List<Cota> obterCotaAssociadaFiador(Long idFiador){
		return this.cotaRepository.obterCotaAssociadaFiador(idFiador);
	}

	/**
	 * @see br.com.abril.nds.service.CotaService#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
	 */
	@Override
	@Transactional
	public Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota) {
		return this.cotaRepository.obterCotaPDVPorNumeroDaCota(numeroCota);
	}
	
	/**
	 * Define parametros default com base em parametros do Distribuidor
	 * @param distribuicao
	 * @return DistribuicaoDTO
	 */
	private DistribuicaoDTO setDistribuicaoDefault(DistribuicaoDTO distribuicao){
		
		List<ParametrosDistribuidorEmissaoDocumento> listaParametrosDistribuidorEmissaoDocumentos = 
				this.distribuidorRepository.parametrosDistribuidorEmissaoDocumentos();
		
		if (listaParametrosDistribuidorEmissaoDocumentos!=null && listaParametrosDistribuidorEmissaoDocumentos.size() > 0){
		
			for (ParametrosDistribuidorEmissaoDocumento item : listaParametrosDistribuidorEmissaoDocumentos){
				
				TipoParametrosDistribuidorEmissaoDocumento tipo = item.getTipoParametrosDistribuidorEmissaoDocumento();
				
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
					}		
				}
			}
		}
		
		return distribuicao;
	}

	@Override
	@Transactional
	public DistribuicaoDTO obterDadosDistribuicaoCota(Long idCota) {
		
		if(idCota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Id da cota não informado.");
		}
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if(cota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		ParametroDistribuicaoCota parametro = cota.getParametroDistribuicao();
		
		boolean qtdePDVAutomatico = this.distribuidorService.preenchimentoAutomaticoPDV();
				
		DistribuicaoDTO dto = new DistribuicaoDTO();
		
		dto.setNumCota(cota.getNumeroCota());
		
		if(cota.getBox()!= null){
			dto.setBox(cota.getBox().getNome());
		}
		
		if(qtdePDVAutomatico) {	
			dto.setQtdePDV( (cota.getPdvs()!=null) ? cota.getPdvs().size() : 0);
			dto.setQtdeAutomatica(true);
		}
		
		
		
		
		if (parametro == null) {

			dto = this.setDistribuicaoDefault(dto);
			return dto;	
			
		} else if ((parametro.getSlipImpresso() == null || parametro.getSlipImpresso() == false) &&
					(parametro.getSlipEmail() == null || parametro.getSlipEmail() == false) &&
					(parametro.getBoletoImpresso() == null || parametro.getBoletoImpresso() == false) &&
					(parametro.getBoletoEmail() == null || parametro.getBoletoEmail() == false) &&
					(parametro.getBoletoSlipImpresso() == null || parametro.getBoletoSlipImpresso() == false) &&
					(parametro.getBoletoSlipEmail() == null || parametro.getBoletoSlipEmail() == false) &&
					(parametro.getReciboImpresso() == null || parametro.getReciboImpresso() == false) &&
					(parametro.getReciboEmail() == null || parametro.getReciboEmail() == false) &&
					(parametro.getChamadaEncalheImpresso() == null || parametro.getChamadaEncalheImpresso() == false) &&
					(parametro.getChamadaEncalheEmail() == null || parametro.getChamadaEncalheEmail() == false) &&
					(parametro.getNotaEnvioImpresso() == null || parametro.getNotaEnvioImpresso() == false) &&
					(parametro.getNotaEnvioEmail() == null || parametro.getNotaEnvioEmail() == false)) 
		{
			
			dto = this.setDistribuicaoDefault(dto);
	
		} else {
			dto.setNeImpresso(parametro.getNotaEnvioImpresso());
			dto.setNeEmail(parametro.getNotaEnvioEmail());
			dto.setCeImpresso(parametro.getChamadaEncalheImpresso());
			dto.setCeEmail(parametro.getChamadaEncalheEmail());
			dto.setSlipImpresso(parametro.getSlipImpresso());
			dto.setSlipEmail(parametro.getSlipEmail());
			dto.setBoletoImpresso(parametro.getBoletoImpresso());
			dto.setBoletoEmail(parametro.getBoletoEmail());
			dto.setBoletoSlipImpresso(parametro.getBoletoSlipImpresso());
			dto.setBoletoSlipEmail(parametro.getBoletoSlipEmail());
			dto.setReciboImpresso(parametro.getReciboImpresso());
			dto.setReciboEmail(parametro.getReciboEmail());
		}
			
		
		
		
		
		if(!qtdePDVAutomatico) {
			dto.setQtdePDV(parametro.getQtdePDV());
		}
		
		dto.setAssistComercial(parametro.getAssistenteComercial());
		dto.setGerenteComercial(parametro.getGerenteComercial());
		
		dto.setDescricaoTipoEntrega(parametro.getDescricaoTipoEntrega());
		
		dto.setObservacao(parametro.getObservacao());
		dto.setRepPorPontoVenda(parametro.getRepartePorPontoVenda());
		dto.setSolNumAtras(parametro.getSolicitaNumAtras());
		dto.setRecebeRecolhe(parametro.getRecebeRecolheParciais());
		dto.setUtilizaTermoAdesao(parametro.getUtilizaTermoAdesao());
		dto.setTermoAdesaoRecebido(parametro.getTermoAdesaoRecebido());
		dto.setUtilizaProcuracao(parametro.getUtilizaProcuracao());
		dto.setProcuracaoRecebida(parametro.getProcuracaoRecebida());
		dto.setTaxaFixa(MathUtil.round(parametro.getTaxaFixa(), 2));
		dto.setPercentualFaturamento(MathUtil.round(parametro.getPercentualFaturamento(), 2));
		dto.setBaseCalculo(parametro.getBaseCalculo());
		dto.setInicioPeriodoCarencia(DateUtil.formatarDataPTBR(parametro.getInicioPeriodoCarencia()));
		dto.setFimPeriodoCarencia(DateUtil.formatarDataPTBR(parametro.getFimPeriodoCarencia()));
		dto.setTipoDistribuicaoCota(this.cotaRepository.obterTipoDistribuicao(idCota).name());
		dto.setRecebeComplementar(parametro.getRecebeComplementar());
		
		return dto;
	}
	
	@Override
	@Transactional
	public void salvarDistribuicaoCota(DistribuicaoDTO dto) throws FileNotFoundException, IOException {
		
		if(dto==null || dto.getNumCota()==null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(dto.getNumCota());		
		
		if( cota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		
		///////////////////////////////////////////////
		
		
		        /*
         * faço a pesquisa nos parâmetros de distribuidor para comparar ao que
         * foi inserido pelo usuário. Se os parâmetros forem iguais, utilizo o
         * do distribuidor.
         */
		
		ParametroDistribuicaoCota parametrosDistribuidorConferenciaCota = new ParametroDistribuicaoCota();
		
		Distribuidor distribuidor = this.distribuidorService.obter();	
		List<ParametrosDistribuidorEmissaoDocumento> listaParametrosDeDistribuicao = distribuidor.getParametrosDistribuidorEmissaoDocumentos();
		
		
		
		
		ParametroDistribuicaoCota parametros = new ParametroDistribuicaoCota();
		
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
		parametros.setTaxaFixa(dto.getTaxaFixa());
		parametros.setPercentualFaturamento(dto.getPercentualFaturamento());
		parametros.setBaseCalculo(dto.getBaseCalculo());
		
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
		
		
		
		for(ParametrosDistribuidorEmissaoDocumento parametrosDeDistribuicao : listaParametrosDeDistribuicao) {
			
			String nomeDocumento = parametrosDeDistribuicao.getTipoParametrosDistribuidorEmissaoDocumento().name();
			
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

		this.atualizaTermoAdesao(
			cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGA_EM_BANCA);
		
		this.atualizaTermoAdesao(
			cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGADOR);
	}
	

	@Override
	@Transactional(readOnly = true)
	public CotaDTO obterDadosCadastraisCota(Long idCota){
		
		if (idCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		CotaDTO cotaDTO = new CotaDTO();
		cotaDTO.setIdCota(cota.getId());
		cotaDTO.setNumeroCota(cota.getNumeroCota());
		cotaDTO.setClassificacaoSelecionada(cota.getClassificacaoEspectativaFaturamento());
		cotaDTO.setDataInclusao(cota.getInicioAtividade());
		cotaDTO.setEmailNF((cota.getParametrosCotaNotaFiscalEletronica()!= null) ? cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica():"");
		cotaDTO.setEmiteNFE((cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica() != null) ? cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica():false);
		cotaDTO.setContribuinteICMS((cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().getContribuinteICMS() != null) ? cota.getParametrosCotaNotaFiscalEletronica().getContribuinteICMS():false);
		cotaDTO.setStatus(cota.getSituacaoCadastro());
		
		if (cota.getTipoDistribuicaoCota() != null) {
		    cotaDTO.setTipoCota(cota.getTipoDistribuicaoCota().getDescTipoDistribuicaoCota().substring(0, 1));
		    cotaDTO.setTipoDistribuicaoCota(cota.getTipoDistribuicaoCota());
		}
		
		this.atribuirDadosPessoaCota(cotaDTO, cota.getPessoa());
		this.atribuirDadosBaseReferencia(cotaDTO, cota.getBaseReferenciaCota());
		
		processarTitularidadeCota(cota, cotaDTO);
		
		cotaDTO.setCotasBases(atribuirCotaBase(cota.getNumeroCota()));
		cotaDTO.setRecebeComplementar(cota.getParametroDistribuicao() == null || cota.getParametroDistribuicao().getRecebeComplementar() == null ? false : cota.getParametroDistribuicao().getRecebeComplementar());
		
		return cotaDTO;
	}
	
	@Transactional
	private List<CotaBaseDTO> atribuirCotaBase(Integer numeroCota) {
	
		List<CotaBaseDTO> listaCotaBase = new ArrayList<CotaBaseDTO>();

		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota, true);
		
		if(cotaBase != null){
			listaCotaBase = this.cotaBaseService.obterCotasBases(cotaBase, null);			
		}
		
		for (CotaBaseDTO itemCotaBase : listaCotaBase) {
			
			if(itemCotaBase.getDtInicio() == null)
				itemCotaBase.setDtInicio(cotaBase.getDataInicio());
			
			if(itemCotaBase.getDtFinal() == null)
				itemCotaBase.setDtFinal(cotaBase.getDataFim());
			
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
	private void processarTitularidadeCota(Cota cota, CotaDTO cotaDTO) {
        List<HistoricoTitularidadeCota> titulares = new ArrayList<HistoricoTitularidadeCota>();
		if (cota.getTitularesCota() != null) {
		    titulares.addAll(cota.getTitularesCota());
		}
		Collections.sort(titulares, new Comparator<HistoricoTitularidadeCota>() {

            @Override
            public int compare(HistoricoTitularidadeCota o1, HistoricoTitularidadeCota o2) {
                return o2.getFim().compareTo(o1.getFim());
            }
        });
		
        for (HistoricoTitularidadeCota historico : titulares) {
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
	private void atribuirDadosPessoaCota(CotaDTO cotaDTO, Pessoa pessoa){
		
		if(pessoa == null){
			return;
		}
		
		if(pessoa instanceof PessoaJuridica){
			
			PessoaJuridica pessoaJ = (PessoaJuridica) pessoa;
			cotaDTO.setInscricaoEstadual(pessoaJ.getInscricaoEstadual());
			cotaDTO.setInscricaoMunicipal(pessoaJ.getInscricaoMunicipal());
			cotaDTO.setEmail(pessoaJ.getEmail());
			cotaDTO.setNumeroCnpj(pessoaJ.getCnpj());
			cotaDTO.setNomeFantasia(pessoaJ.getNomeFantasia());
			cotaDTO.setRazaoSocial(pessoaJ.getRazaoSocial());
			cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);
		}
		
		else if (pessoa instanceof PessoaFisica){
			
			PessoaFisica pessoaF = (PessoaFisica) pessoa;
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
	private void atribuirDadosBaseReferencia(CotaDTO cotaDTO, BaseReferenciaCota baseReferenciaCota){
		
		if(baseReferenciaCota == null){
			return;
		}
		
		cotaDTO.setInicioPeriodo(baseReferenciaCota.getInicioPeriodo());
		cotaDTO.setFimPeriodo(baseReferenciaCota.getFinalPeriodo());
		
		if(baseReferenciaCota.getReferenciasCota()!= null && !baseReferenciaCota.getReferenciasCota().isEmpty()){
			
			List<ReferenciaCota> referenicasCota = new ArrayList<ReferenciaCota>();
			referenicasCota.addAll(baseReferenciaCota.getReferenciasCota());
			
			if(referenicasCota.size() > 0){
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
	
	@Override
	@Transactional
	public void excluirCota(Long idCota){
		
		if(idCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada para exclusão inválida!");
		}
		
		if (cotaComDebitos(idCota)){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "A [Cota] possui dívidas em aberto e não pode ser excluída!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
	
		try{	
			
			for (HistoricoSituacaoCota hist : cota.getHistoricos()){
				
				this.historicoSituacaoCotaRepository.remover(hist);
			}
			
			cotaRepository.remover(cota);
			
		}catch (RuntimeException e) {
			
			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Exclusão não permitida, registro possui dependências!");
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
		if(cota == null){
			cota = new Cota();
			cota.setInicioAtividade(dataOperacao);
			cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
			cota.setTipoCota(TipoCota.CONSIGNADO);
			incluirPDV = true;
			newCota = true;
		}
		
        // Flag indica a mudança de número da cota
		boolean mudancaNumero = false;
		if (!newCota) {
		    Integer numeroCota = cota.getNumeroCota();
		    Integer novoNumeroCota = cotaDto.getNumeroCota();
		    mudancaNumero = !numeroCota.equals(novoNumeroCota);
		}
        // Se é uma nova cota ou alteração de número, processa o novo número
		if (newCota || mudancaNumero) {
		    processarNovoNumeroCota(cotaDto.getNumeroCota(),cota.getId());
		}
		
	    cota.setNumeroCota(cotaDto.getNumeroCota());
	    
	    cota.setParametrosCotaNotaFiscalEletronica(getParamNFE(cota, cotaDto));
	    
	    cota.setClassificacaoEspectativaFaturamento(cotaDto.getClassificacaoSelecionada());
	    
	    cota.setPessoa(persistePessoaCota(cota, cotaDto));
	    
	    cota.setTipoDistribuicaoCota(cotaDto.getTipoDistribuicaoCota());
	    
	    cota  = cotaRepository.merge(cota);
	    
	    if(newCota) {
	    	
		    HistoricoSituacaoCota hsc = new HistoricoSituacaoCota();
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
		
	    BaseReferenciaCota baseReferenciaCota = processarDadosBaseReferenciaCota(cota, cotaDto);
	    
	    processarDadosReferenciaCota(baseReferenciaCota, cotaDto);
	    
	    if(incluirPDV){
	    	persisteDadosPDV(cota, cotaDto);
	    }
	   
	    return cota.getId();
	}
	
	@Transactional
	public void atualizaTermoAdesao(String numCota, DescricaoTipoEntrega descricaoTipoEntrega) throws FileNotFoundException, IOException {
		
		ParametroSistema pathDocumento = null;
		
		switch(descricaoTipoEntrega) {
		case ENTREGA_EM_BANCA:
			pathDocumento = this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_TERMO_ADESAO);								
			break;
		case ENTREGADOR:
			pathDocumento = this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_PROCURACAO);								
			break;
		default:
			return;
		}
		
		ParametroSistema raiz = 
				this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);					
		
		if(	raiz == null || raiz.getValor() == null || 
			pathDocumento == null || pathDocumento.getValor() == null) {
			
			return;
			
		}
		
		String path = (raiz.getValor() + pathDocumento.getValor() + numCota).replace("\\", "/");
		
		fileService.persistirTemporario(path);
	}

	    /**
     * Valida os dados referente histórico cota base
     * 
     * @param cotaDto
     */
	private void validarHistoricoCotaBase(CotaDTO cotaDto) {
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
			
			if(DateUtil.removerTimestamp(cotaDto.getInicioPeriodo()).compareTo(DateUtil.removerTimestamp(Calendar.getInstance().getTime()))!=0){

                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Campo [Período] referente à cota base deve ser igual a data da alteração!");
			}
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
	private void validarFormatoDados(CotaDTO cotaDto) {
		
		if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
			
			this.pessoaService.validarCNPJ(cotaDto.getNumeroCnpj());
		}
		
		if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
			
			this.pessoaService.validarCPF(cotaDto.getNumeroCPF());
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
	    
	    if(paramNFE == null){
	    	paramNFE = new ParametrosCotaNotaFiscalEletronica();
	    }
	    		
	    paramNFE.setEmailNotaFiscalEletronica(cotaDto.getEmailNF());
	    paramNFE.setEmiteNotaFiscalEletronica(cotaDto.isEmiteNFE());
	    paramNFE.setContribuinteICMS(cotaDto.isContribuinteICMS());
	    return paramNFE;
	}
	
	    /**
     * Validas as informações referente ao cadasto de uma nova cota.
     * 
     * @param cotaDto
     */
	private void validarParametrosObrigatoriosCota(CotaDTO cotaDto) {
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
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
		
		if(cotaDto.isEmiteNFE()){
			if((cotaDto.getEmailNF() == null || cotaDto.getEmailNF().isEmpty())){
                mensagensValidacao.add("O preenchimento do campo [E-mail NF-e] é obrigatório!");
			}
		}
		
		if(cotaDto.getTipoDistribuicaoCota() == null){
            mensagensValidacao.add("O preenchimento do campo [Tipo] é obrigatório!");
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
	private Pessoa persistePessoaCota(Cota cota, CotaDTO cotaDto){
		
		Pessoa pessoa = null;
		
		if (cotaDto.isAlteracaoTitularidade()) {
			
			pessoa = getPessoa(cotaDto, null) ;
		
		} else {
		
			pessoa = getPessoa(cotaDto, cota.getPessoa()) ;
		}
	    
	    pessoa.setEmail(cotaDto.getEmail());
	    
    	if ( pessoa instanceof  PessoaJuridica  ){
    		
    		((PessoaJuridica) pessoa).setCnpj(cotaDto.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
    		((PessoaJuridica) pessoa).setInscricaoEstadual(cotaDto.getInscricaoEstadual());
    		((PessoaJuridica) pessoa).setInscricaoMunicipal(cotaDto.getInscricaoMunicipal());
    		((PessoaJuridica) pessoa).setNomeFantasia(cotaDto.getNomeFantasia());
    		((PessoaJuridica) pessoa).setRazaoSocial(cotaDto.getRazaoSocial());
    		
    		pessoa = pessoaJuridicaRepository.merge(((PessoaJuridica) pessoa));
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
    		
    		pessoa = pessoaFisicaRepository.merge(((PessoaFisica) pessoa));
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
	private Pessoa getPessoa(CotaDTO cotaDTO, Pessoa pessoa){
		
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
	private BaseReferenciaCota processarDadosBaseReferenciaCota(Cota cota , CotaDTO cotaDto){
		
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
		    baseReferenciaCota.setCota(cota);
		    
		    baseReferenciaCota = baseReferenciaCotaRepository.merge(baseReferenciaCota);
		}
		
	    return  baseReferenciaCota;
		
	}
	
	/**
	 * Persiste um conjunto de dados referente a entidade ReferenciaCota, associados a entidade BaseReferenciaCota
	 * @param baseReferenciaCota
	 * @param cotaDto
	 */
	private void processarDadosReferenciaCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
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
	
				for(ReferenciaCota ref : referenciasCota){
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
	private void validarParametrosBaseReferenciaCota(CotaDTO cotaDto){
		
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
	private Set<ReferenciaCota> getReferenciasCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
		Set<ReferenciaCota> referenciasCota = new HashSet<ReferenciaCota>();
	    
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
	private ReferenciaCota getReferencaiCota(Integer numeroCota,BigDecimal porcentagem, BaseReferenciaCota baseReferenciaCota){
		
		Cota cota = null;
		
		if(numeroCota != null){
			cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		}
		
		ReferenciaCota referenciaCota = new ReferenciaCota();
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
	private void validarCotaBaseIgual(CotaDTO cotaDTO){
		
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
	private void validarCotaIguais(Integer param,Integer param2, Integer param3){
		
		if(param!= null){
			if(param.equals(param2)|| param.equals(param3)){
                throw new ValidacaoException(TipoMensagem.WARNING, "A cota " + param + "está incorreta! A cota "
                        + param + " está duplicada!");
			}
		}
	}
	
	    /**
     * Valida se a porcentagem informada nas cotas histórico base obedecem 100
     * porcento.
     * 
     * @param cotaDto
     */
	private void validarPorcentagemCotaBase(CotaDTO cotaDto) {
		
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
		
		if(existeValor && (valor.intValue() != 100)){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Porcentagem histórico cota base inválido! A porcentagem do histórico cota base deve ser 100%  ");
		}
		
	}

	private boolean tratarValorReferenciaCota(Integer numeroCota, BigDecimal porcentagem){
		return (numeroCota != null && porcentagem != null);
	}
	
	/**
	 * Verifica se a cota possui dividas em aberto
	 * @param idCota
	 * @return boolean
	 */
	private boolean cotaComDebitos(Long idCota){
		
        BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(idCota);
		
		return (dividasEmAberto!=null && dividasEmAberto.floatValue() > 0);
	}
	
	    /**
     * Verifica se o número da cota existente pode ser utilizado por uma nova
     * cota
     * 
     * @param numeroCota - número da nova cota
     */
	private void processarNovoNumeroCota(Integer numeroCota, Long idCota){
		
		Cota cota  = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
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
					throw new ValidacaoException(TipoMensagem.WARNING,"Número da cota não pode ser utilizado.");
				}
			}	
			
            // Verifica se cota possui dívidas em aberto
			BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(cota.getId());
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
	private void alteraNumeroCota(Cota cota){
		
		Integer novoNumeroCota = getNovoNumeroCota(cota.getNumeroCota(), null,1);
		
		Integer numeroCotaAntigo = cota.getNumeroCota();
		
		cota.setNumeroCota(novoNumeroCota);
		cotaRepository.merge(cota);
		
		HistoricoNumeroCotaPK pk = new HistoricoNumeroCotaPK();
		pk.setDataAlteracao(new Date());
		pk.setIdCota(cota.getId());
		

		HistoricoNumeroCota historicoNumeroCota = new HistoricoNumeroCota();
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
	private Integer getNovoNumeroCota(Integer numeroCota, Integer novoNumeroCota ,Integer numero){
		
		Cota cota  = cotaRepository.obterPorNumeroDaCota( (novoNumeroCota == null) ?numeroCota :novoNumeroCota);
		
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
	private boolean isParametroDistribuidoNumeroCotaValido(Integer  numeroCota){
		
		HistoricoSituacaoCota historicoSituacaoCota  = 
			this.historicoSituacaoCotaRepository.obterUltimoHistorico(numeroCota, SituacaoCadastro.INATIVO);
		
		if (historicoSituacaoCota == null) {
			
			return true;
		}
		
	    Long qntDiasInativo =  
	    	DateUtil.obterDiferencaDias(
	    		historicoSituacaoCota.getDataInicioValidade(), 
	    			this.distribuidorRepository.obterDataOperacaoDistribuidor());
	    
	    Long qntDiasInativoPermitidoParaReutilizacao =
	    	this.distribuidorService.qntDiasReutilizacaoCodigoCota();
	    
		return (qntDiasInativo > qntDiasInativoPermitidoParaReutilizacao);
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
		List<ItemDTO<TipoCota,String>> comboTiposCota =  new ArrayList<ItemDTO<TipoCota,String>>();
		for (TipoCota itemTipoCota: TipoCota.values()){
			comboTiposCota.add(new ItemDTO<TipoCota,String>(itemTipoCota, itemTipoCota.getDescTipoCota()));
		}
		return comboTiposCota;
	}
		
	@Transactional
	@Override
	public void alterarCota(Cota cota) {
		this.cotaRepository.alterar(cota);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaService#obterCotasEntre(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.model.cadastro.SituacaoCadastro)
	 */
	@Override
	@Transactional
	public List<Cota> obterCotasEntre(Intervalo<Integer> intervaloCota,
			Intervalo<Integer> intervaloBox, SituacaoCadastro situacao) {
		
		List<Cota> listaCotas = new ArrayList<Cota>();
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(situacao);
		
		List<Long> idCotas = this.cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox, situacoesCadastro, null, null, null, null, null, null);
		
		for(Long idCota : idCotas ) {
			
			Cota cota = this.cotaRepository.buscarPorId(idCota);
			
			if (cota != null) 
				listaCotas.add(cota);
		}
		
		return listaCotas;
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
	public List<Cota> obterCotasEntre(Integer cotaDe, 
			                          Integer cotaAte, 
			                          List<SituacaoCadastro> situacoesCadastro) {
		
		List<Cota> cotas = this.cotaRepository.obterCotasIntervaloNumeroCota(cotaDe, cotaAte, situacoesCadastro);

		return cotas;
	}
	
	    /**
     * Descarta Enderecos, Telefones, Garantias e Sócios da Cota na Troca de
     * Titularidade
     * 
     * @param cota
     */
	private void excluiEnderecosTelefonesGarantiasSociosCota(Cota cota){
		
		Set<EnderecoCota> enderecos = cota.getEnderecos();
		
		if (enderecos!=null && enderecos.size() > 0){
			
			List<Endereco> listaEnderecos = new ArrayList<Endereco>();
			for (EnderecoCota endereco : enderecos){
				
				listaEnderecos.add(endereco.getEndereco());
			}
			
			this.enderecoCotaRepository.removerEnderecosCota(cota.getId(), listaEnderecos);
		}
		
		
		Set<TelefoneCota> telefones = cota.getTelefones();
		
		if (telefones!=null && telefones.size() > 0){
	
			List<Long> idsTelefones = new ArrayList<Long>();
			for (TelefoneCota telefoneCota : telefones){
				
				idsTelefones.add(telefoneCota.getTelefone().getId());
			}
			
			this.telefoneCotaRepository.removerTelefonesCota(idsTelefones);
		}

		
		CotaGarantia garantia = cota.getCotaGarantia();
		
		if (garantia!=null){
	 
			this.cotaGarantiaRepository.deleteListaImoveis(garantia.getId());
			
			this.cotaGarantiaRepository.deleteListaOutros(garantia.getId());
			
			this.cotaGarantiaRepository.deleteByCota(cota.getId());
		}
		

		Set<SocioCota> socios = cota.getSociosCota();
		
		if (socios!=null && socios.size() > 0){
	
			this.socioCotaRepository.removerSociosCota(cota.getId());
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public CotaDTO criarCotaTitularidade(CotaDTO cotaDTO) {

		Cota cotaAntiga = this.cotaRepository.buscarPorId(cotaDTO.getIdCota());

		HistoricoTitularidadeCota historicoCota = this.historicoTitularidadeService.gerarHistoricoTitularidadeCota(cotaAntiga);
		
		List<PDV> pdvs = cotaAntiga.getPdvs();
		Set<Fornecedor> fornecedores = cotaAntiga.getFornecedores();
		Set<DescontoProdutoEdicao> descontosProdutoEdicao = cotaAntiga.getDescontosProdutoEdicao();
		ParametroCobrancaCota parametrosCobrancaCota = cotaAntiga.getParametroCobranca();
		ParametroDistribuicaoCota parametroDistribuicaoCota = cotaAntiga.getParametroDistribuicao();

		Set<HistoricoTitularidadeCota> titularesCota = cotaAntiga.getTitularesCota();
		titularesCota.add(historicoCota);

		Long idCotaNova = this.salvarCota(cotaDTO);

		Cota cotaNova = this.cotaRepository.buscarPorId(idCotaNova);
		cotaNova.setInicioTitularidade(this.distribuidorService.obterDataOperacaoDistribuidor());
		cotaNova.setPdvs(pdvs);
		cotaNova.setFornecedores(fornecedores);
		cotaNova.setDescontosProdutoEdicao(descontosProdutoEdicao);
		cotaNova.setParametroCobranca(parametrosCobrancaCota);
		cotaNova.setParametroDistribuicao(parametroDistribuicaoCota);
		cotaNova.setTitularesCota(titularesCota);
		cotaNova.setSituacaoCadastro(SituacaoCadastro.ATIVO);
		cotaNova.setTipoCota(TipoCota.CONSIGNADO);
		
		this.cotaRepository.merge(cotaNova);
		processarTitularidadeCota(cotaAntiga, cotaDTO);
		
		this.excluiEnderecosTelefonesGarantiasSociosCota(cotaNova);
		
		cotaDTO.setStatus(SituacaoCadastro.ATIVO);
		
		return cotaDTO;
	}
	
	@Override
	@Transactional
	public byte[] getDocumentoProcuracao(Integer numeroCota) throws Exception {

		Cota cota = this.cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		ProcuracaoImpressaoDTO dto = new ProcuracaoImpressaoDTO();
		
		if (cota.getPessoa() instanceof PessoaFisica){
			
			PessoaFisica pessoa = (PessoaFisica) cota.getPessoa();
			
			dto.setNomeJornaleiro(pessoa.getNome());
			dto.setNacionalidade(pessoa.getNacionalidade());
			dto.setEstadoCivil(pessoa.getEstadoCivil() == null ? "" : pessoa.getEstadoCivil().getDescricao());
			dto.setRgJornaleiro(pessoa.getRg());
			dto.setCpfJornaleiro(pessoa.getCpf());
		}
		
		dto.setBoxCota(cota.getBox().getNome());
		
		EnderecoCota enderecoDoProcurado = this.enderecoCotaRepository.getPrincipal(cota.getId());
		
		if (enderecoDoProcurado != null){
			
			dto.setEnderecoDoProcurado(enderecoDoProcurado.getEndereco().getLogradouro());
			dto.setBairroProcurado(enderecoDoProcurado.getEndereco().getBairro());
		}
		
		PDV pdv = this.pdvRepository.obterPDVPrincipal(cota.getId());
		
		if (pdv != null){
			
			dto.setNumeroPermissao(pdv.getLicencaMunicipal() != null ? 
					pdv.getLicencaMunicipal().getNumeroLicenca() : "");
			
			Endereco enderecoPDV = this.enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
			
			if (enderecoPDV != null){
				
				dto.setEnderecoPDV(enderecoPDV.getLogradouro());
				dto.setCidadePDV(enderecoPDV.getCidade());
			}
			
			Rota rota = this.rotaRepository.obterRotaPorPDV(pdv.getId(), cota.getId());
			
			if (rota != null){
				
				Entregador entregador = this.entregadorRepository.obterEntregadorPorRota(rota.getId());
				
				if (entregador != null){
					
					if (entregador.getPessoa() instanceof PessoaFisica){
						
						PessoaFisica pessoaFisica = (PessoaFisica) entregador.getPessoa();
						
						dto.setEstadoCivilProcurador(pessoaFisica.getEstadoCivil() == null ? "" : pessoaFisica.getEstadoCivil().getDescricao());
						dto.setNacionalidadeProcurador(pessoaFisica.getNacionalidade());
						dto.setNomeProcurador(pessoaFisica.getNome());
						dto.setRgProcurador(pessoaFisica.getRg());
					}
				}
			}
		}
		
		List<ProcuracaoImpressaoDTO> listaDTO = new ArrayList<ProcuracaoImpressaoDTO>();
		listaDTO.add(dto);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cidade", dto.getCidadePDV());
		
		String informacoesComplementares = this.distribuidorRepository.obterInformacoesComplementaresTermoAdesao();
		parameters.put("infoComp", informacoesComplementares!=null?informacoesComplementares:"");
		parameters.put("LOGO",JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor()));
		parameters.put("nomeDistribuidor", parametrosDistribuidorService.getParametrosDistribuidor()!=null?parametrosDistribuidorService.getParametrosDistribuidor().getNomeFantasia():"");
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
		
		URL url = 
			Thread.currentThread().getContextClassLoader().getResource("/reports/procuracao_subreport1.jasper");
		
		String path = url.toURI().getPath();
		 
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] getDocumentoTermoAdesao(Integer numeroCota, BigDecimal valorDebito, BigDecimal percentualDebito) throws Exception {
		
		TermoAdesaoDTO dto = new TermoAdesaoDTO();
		dto.setNumeroCota(numeroCota);
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		dto.setNomeCota(cota.getPessoa().getNome());
		dto.setNomeDistribuidor(this.distribuidorRepository.obterRazaoSocialDistribuidor());
		dto.setValorDebito(valorDebito);
		dto.setPorcentagemDebito(percentualDebito);
		
		dto.setPeriodicidade("Semanal");
		
		EnderecoCota enderecoCota = 
				this.enderecoCotaRepository.obterEnderecoPorTipoEndereco(
						cota.getId(), TipoEndereco.LOCAL_ENTREGA);
		
		if (enderecoCota != null){
			
			String numeroEndereco = (enderecoCota.getEndereco().getNumero()!= null)?enderecoCota.getEndereco().getNumero():"";
			
			dto.setLogradouroEntrega(enderecoCota.getEndereco().getLogradouro() + ", N&deg; " + numeroEndereco);
			dto.setBairroEntrega(enderecoCota.getEndereco().getBairro());
			dto.setCEPEntrega(Util.adicionarMascaraCEP(enderecoCota.getEndereco().getCep()));
			dto.setCidadeEntrega(enderecoCota.getEndereco().getCidade());
		} else {
			
			PDV pdv = this.pdvRepository.obterPDVPrincipal(cota.getId());
			
			if (pdv != null){
				
				dto.setReferenciaEndereco(pdv.getPontoReferencia());
				dto.setHorariosFuncionamento(pdv.getPeriodos()!=null?pdv.getPeriodos().size()>0?pdv.getPeriodos():null:null);
				
				Endereco enderecoPDV = this.enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
				
				if (enderecoPDV != null){
					
					String numeroEndereco =  enderecoPDV.getNumero()!=null ? enderecoPDV.getNumero():"";
					
					dto.setLogradouroEntrega(enderecoPDV.getLogradouro() + ", N&deg; " + numeroEndereco);
					dto.setBairroEntrega(enderecoPDV.getBairro());
					dto.setCEPEntrega(Util.adicionarMascaraCEP(enderecoPDV.getCep()));
					dto.setCidadeEntrega(enderecoPDV.getCidade());
				}
			}
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SUBREPORT_DIR",
				Thread.currentThread().getContextClassLoader().getResource("/reports/").toURI().getPath());
		
		String informacoesComplementares = this.distribuidorRepository.obterInformacoesComplementaresTermoAdesao();
		parameters.put("infoComp", informacoesComplementares!=null?informacoesComplementares:"");
		parameters.put("LOGO",JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor()));
		
		List<TermoAdesaoDTO> listaDTO = new ArrayList<TermoAdesaoDTO>();
		listaDTO.add(dto);
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
		
		URL url = 
			Thread.currentThread().getContextClassLoader().getResource("/reports/termo_adesao.jasper");
		
		String path = url.toURI().getPath();
		 
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	@Transactional(readOnly = true)
	public DistribuicaoDTO carregarValoresEntregaBanca(Integer numCota) {
		
		DistribuicaoDTO dto = new DistribuicaoDTO();
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(numCota);
		
		if (cota == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada!");
		}
		
		this.obterPercentualFaturamentoTaxaFixa(cota.getId(), dto);
		
		dto.setUtilizaTermoAdesao(this.distribuidorRepository.utilizaTermoAdesao());
		
		return dto;
	}
	
	private DistribuicaoDTO obterPercentualFaturamentoTaxaFixa(Long idCota, DistribuicaoDTO dto) {
		
		PDV pdv = this.pdvRepository.obterPDVPrincipal(idCota);
		
		if (pdv != null) {
		
			Rota rota = this.rotaRepository.obterRotaPorPDV(pdv.getId(), idCota);
			
			if (rota != null) {
				
				Entregador entregador = this.entregadorRepository.obterEntregadorPorRota(rota.getId());
				
				if (entregador != null) {
				
					dto.setTaxaFixa(entregador.getTaxaFixa());
					dto.setPercentualFaturamento(entregador.getPercentualFaturamento());
				}
			}
		}
		
		return dto;
	}
	
	@Override
	@Transactional
	public List<Fornecedor> obterFornecedoresCota(Long idCota) {
		Cota cota = this.obterPorId(idCota);
		Set<Fornecedor> fornecedores = cota.getFornecedores();
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		for(Fornecedor itemFornecedor:fornecedores){
			listaFornecedores.add(itemFornecedor);
		}
		return listaFornecedores;
	}

	/**
    * {@inheritDoc}
    */
	@Override
    @Transactional(readOnly = true)
    public CotaDTO obterHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
	    HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        CotaDTO dto = HistoricoTitularidadeCotaDTOAssembler.toCotaDTO(historico);
        return dto;
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
	    HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<EnderecoAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toEnderecoAssociacaoDTOCollection(historico.getEnderecos()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<TelefoneAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toTelefoneAssociacaoDTOCollection(historico.getTelefones()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<FornecedorDTO> obterFornecedoresHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<FornecedorDTO>(HistoricoTitularidadeCotaDTOAssembler.toFornecedorDTOCollection(historico.getFornecedores()));
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TipoDescontoProdutoDTO> obterDescontosProdutoHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        List<TipoDescontoProdutoDTO> dtos = new ArrayList<TipoDescontoProdutoDTO>();
        for (HistoricoTitularidadeCotaDescontoProduto desconto : historico.getDescontosProduto()) {
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
    public List<TipoDescontoCotaDTO> obterDescontosCotaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        List<TipoDescontoCotaDTO> dtos = new ArrayList<TipoDescontoCotaDTO>();
        for (HistoricoTitularidadeCotaDescontoCota desconto : historico.getDescontosCota()) {
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
    public DistribuicaoDTO obterDistribuicaoHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        HistoricoTitularidadeCotaDistribuicao distribuicao = historico.getDistribuicao();
        if (distribuicao != null) {
            distribuicao.setHistoricoTitularidadeCota(historico);
            return HistoricoTitularidadeCotaDTOAssembler.toDistribuicaoDTO(distribuicao);
        }
        return new DistribuicaoDTO(); 
    }

	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro) {

		return this.cotaRepository.obterQuantidadeCotas(situacaoCadastro);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaResumoDTO> obterCotas(SituacaoCadastro situacaoCadastro) {

		return this.cotaRepository.obterCotas(situacaoCadastro);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaResumoDTO> obterCotasComInicioAtividadeEm(Date dataInicioAtividade) {

		return this.cotaRepository.obterCotasComInicioAtividadeEm(dataInicioAtividade);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte) {

		return this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataExpedicaoReparte);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe) {

		return this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataRecolhimentoEncalhe);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasQueEnquadramNoRangeDeReparte(BigInteger qtdReparteInicial, BigInteger qtdReparteFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas){
		return cotaRepository.buscarCotasQuePossuemRangeReparte(qtdReparteInicial, qtdReparteFinal, listProdutoEdicaoDto, cotasAtivas);
	}
	
	@Transactional(readOnly = true)
	public boolean isCotaOperacaoDiferenciada(Integer numeroCota){
		
		List<DiaSemana> diasSemanaOperacaoDiferenciada = grupoRepository.obterDiasOperacaoDiferenciadaCota(numeroCota);

		return (diasSemanaOperacaoDiferenciada != null && !diasSemanaOperacaoDiferenciada.isEmpty());
		
	}
	
	@Transactional(readOnly = true)
	@Override
	public boolean isTipoCaracteristicaSegmentacaoConvencional(Long idCota) {
		
		return TipoDistribuicaoCota.CONVENCIONAL.equals(this.cotaRepository.obterTipoDistribuicao(idCota));
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasQueEnquadramNoRangeVenda(BigInteger qtdVendaInicial, BigInteger qtdVendaFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		return cotaRepository.buscarCotasQuePossuemRangeVenda(qtdVendaInicial, qtdVendaFinal, listProdutoEdicaoDto, cotasAtivas);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(BigDecimal percentVenda, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		return cotaRepository.buscarCotasQuePossuemPercentualVendaSuperior(percentVenda, listProdutoEdicaoDto, cotasAtivas);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasPorNomeOuNumero(CotaDTO cotaDto, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		 return cotaRepository.buscarCotasPorNomeOuNumero(cotaDto, listProdutoEdicaoDto, cotasAtivas);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasPorComponentes(ComponentesPDV componente, String elemento, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		return cotaRepository.buscarCotasPorComponentes(componente, elemento, listProdutoEdicaoDto, cotasAtivas);
	}

	@Transactional(readOnly = true)
	@Override
	public List<AnaliseHistoricoDTO> buscarHistoricoCotas(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, 
			List<Cota> cotas, final String sortorder, final String sortname) {
		Collections.sort(listProdutoEdicaoDto);
		
		List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO = cotaRepository.buscarHistoricoCotas(listProdutoEdicaoDto, cotas);
		
		for (AnaliseHistoricoDTO analiseHistoricoDTO : listAnaliseHistoricoDTO) {
			
			int qtdEdicaoVendida = 0;
			
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				ProdutoEdicaoDTO produtoEdicaoDTO = listProdutoEdicaoDto.get(i);
				
				ProdutoEdicaoDTO dto = produtoEdicaoRepository.obterHistoricoProdutoEdicao(
					produtoEdicaoDTO.getCodigoProduto(), produtoEdicaoDTO.getNumeroEdicao(), analiseHistoricoDTO.getNumeroCota());
				
				if (dto != null) {
					qtdEdicaoVendida++;
					if (i == 0) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd1Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd1Venda(dto.getQtdeVendas().toString());
						}
					}
					
					if (i == 1) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd2Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd2Venda(dto.getQtdeVendas().toString());
						}
					}
					
					if (i == 2) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd3Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd3Venda(dto.getQtdeVendas().toString());
						}
					}
					
					if (i == 3) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd4Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd4Venda(dto.getQtdeVendas().toString());
						}
					}
					
					if (i == 4) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd5Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd5Venda(dto.getQtdeVendas().toString());
						}
					}
					
					if (i == 5) {
						if(dto.getReparte() != null){
							analiseHistoricoDTO.setEd6Reparte(dto.getReparte().toString());
						}
						
						if(dto.getQtdeVendas() != null){
							analiseHistoricoDTO.setEd6Venda(dto.getQtdeVendas().toString());
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
	
	private void formatarListaHistoricoVenda(List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO) {
		for (AnaliseHistoricoDTO dto : listAnaliseHistoricoDTO) {			

			if(dto.getEd1Reparte().equals("0")){
				dto.setEd1Reparte("");
                if(dto.getEd1Venda().equals("0")){
                    dto.setEd1Venda("");
                }
            }
			if(dto.getEd2Reparte().equals("0")){
				dto.setEd2Reparte("");
                if(dto.getEd2Venda().equals("0")){
                    dto.setEd2Venda("");
                }
            }
			if(dto.getEd3Reparte().equals("0")){
				dto.setEd3Reparte("");
                if(dto.getEd3Venda().equals("0")){
                    dto.setEd3Venda("");
                }
            }
			if(dto.getEd4Reparte().equals("0")){
				dto.setEd4Reparte("");
                if(dto.getEd4Venda().equals("0")){
                    dto.setEd4Venda("");
                }
            }
			if(dto.getEd5Reparte().equals("0")){
				dto.setEd5Reparte("");
                if(dto.getEd5Venda().equals("0")){
                    dto.setEd5Venda("");
                }
            }
			if(dto.getEd6Reparte().equals("0")){
				dto.setEd6Reparte("");
                if(dto.getEd6Venda().equals("0")){
                    dto.setEd6Venda("");
                }
            }
			if(dto.getReparteMedio() == 0){
				dto.setReparteMedio(null);
			}
			if(dto.getVendaMedia() == 0){
				dto.setVendaMedia(null);
			}
		}
	}

	private void ordenarListaHistoricoVenda(final String sortorder, final String sortname, List<AnaliseHistoricoDTO> listAnaliseHistoricoDTO) {
		if(!StringUtils.equals(sortorder, "undefined")){
			
			if(sortname != null){
				if(sortname.equals("ed1Reparte")){
					ListUtils.orderList(sortorder, "ed1Reparte", listAnaliseHistoricoDTO, Integer.class);					
				}else if(sortname.equals("ed1Venda")){
					ListUtils.orderList(sortorder, "ed1Venda", listAnaliseHistoricoDTO, Integer.class);	
				}else if(sortname.equals("ed2Reparte")){
					ListUtils.orderList(sortorder, "ed2Reparte", listAnaliseHistoricoDTO, Integer.class);						
				}else if(sortname.equals("ed2Venda")){
					ListUtils.orderList(sortorder, "ed2Venda", listAnaliseHistoricoDTO, Integer.class);	
				}else if(sortname.equals("ed3Reparte")){
					ListUtils.orderList(sortorder, "ed3Reparte", listAnaliseHistoricoDTO, Integer.class);					
				}else if(sortname.equals("ed3Venda")){
					ListUtils.orderList(sortorder, "ed3Venda", listAnaliseHistoricoDTO, Integer.class);	
				}else if(sortname.equals("ed4Reparte")){
					ListUtils.orderList(sortorder, "ed4Reparte", listAnaliseHistoricoDTO, Integer.class);						
				}else if(sortname.equals("ed4Venda")){
					ListUtils.orderList(sortorder, "ed4Venda", listAnaliseHistoricoDTO, Integer.class);
				}else if(sortname.equals("ed5Reparte")){
					ListUtils.orderList(sortorder, "ed5Reparte", listAnaliseHistoricoDTO, Integer.class);						
				}else if(sortname.equals("ed5Venda")){
					ListUtils.orderList(sortorder, "ed5Venda", listAnaliseHistoricoDTO, Integer.class);
				}else if(sortname.equals("ed6Reparte")){
					ListUtils.orderList(sortorder, "ed6Reparte", listAnaliseHistoricoDTO, Integer.class);					
				}else if(sortname.equals("ed6Venda")){
					ListUtils.orderList(sortorder, "ed6Venda", listAnaliseHistoricoDTO, Integer.class);
				}else if(sortname.equals("reparteMedio")){
					ListUtils.orderList(sortorder, "reparteMedio", listAnaliseHistoricoDTO);
				}else if(sortname.equals("vendaMedia")){
					ListUtils.orderList(sortorder, "vendaMedia", listAnaliseHistoricoDTO);
				}
			}
		}
	}

	private void setMediaVendaEReparte(int qtdEdicoes, AnaliseHistoricoDTO analiseHistoricoDTO){
		Double reparteMedio = 0.0;
		Double vendaMedia = 0.0;
		
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd1Reparte());
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd2Reparte());
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd3Reparte());
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd4Reparte());
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd5Reparte());
		reparteMedio += Integer.parseInt(analiseHistoricoDTO.getEd6Reparte());
		
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd1Venda());
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd2Venda());
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd3Venda());
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd4Venda());
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd5Venda());
		vendaMedia += Integer.parseInt(analiseHistoricoDTO.getEd6Venda());
		
		analiseHistoricoDTO.setReparteMedio(reparteMedio / qtdEdicoes);
		analiseHistoricoDTO.setVendaMedia(vendaMedia / qtdEdicoes);
	}

	@Transactional(readOnly = true)
	@Override
	public HistoricoVendaPopUpCotaDto buscarCota(Integer numero) {
		return cotaRepository.buscarCota(numero);
	}
	
	@Transactional
	@Override
	public void apagarTipoCota(Long idCota, String TipoCota) {
		LOGGER.info("CotaServiceImpl.apagarTipoCota");
		
		if(idCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada inválida!");
		}
		
		if (TipoCota==null || TipoCota.isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de Cota inválida");
		}

		try{	
			if(TipoCota.equalsIgnoreCase("A")){
				mixCotaProdutoService.excluirMixPorCota(idCota);
			}

			if(TipoCota.equalsIgnoreCase("C")){
				fixacaoReparteService.excluirFixacaoPorCota(idCota);
			}
			
		}catch (RuntimeException e) {

			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Exclusão não permitida, registro possui dependências!");
			}
		}
	}

	@Transactional
	@Override
	public List<DistribuidorClassificacaoCota> obterListaClassificacao() {
		return distribuidorClassificacaoCotaRepository.buscarTodos();
	}

	@Transactional
	@Override
	public List<Integer> numeroCotaExiste(TipoDistribuicaoCota tipoDistribuicaoCota, Integer... cotaIdArray) {
		return cotaRepository.numeroCotaExiste(tipoDistribuicaoCota, cotaIdArray);
	}

	@Transactional
	@Override
	public boolean cotaVinculadaCotaBase(Long idCota) {
		return cotaRepository.cotaVinculadaCotaBase(idCota);
	}

	@Transactional
	@Override
	public List<CotaDTO> obterPorNomeAutoComplete(String nome) {
	    return cotaRepository.obterCotasPorNomeAutoComplete(nome);
	}
	
	@Transactional(readOnly = true)
	@Override
	public TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(Integer numeroCota) {

		return cotaRepository.obterTipoDistribuicaoCotaPorNumeroCota(numeroCota);
	}
	
	@Transactional
	@Override
	public boolean isTipoDistribuicaoCotaEspecifico(Integer numeroCota, TipoDistribuicaoCota tipoDistribuicaoCota) {

		TipoDistribuicaoCota tpDistribuicaoCota = obterTipoDistribuicaoCotaPorNumeroCota(numeroCota);

		return (tpDistribuicaoCota != null && tpDistribuicaoCota.equals(tipoDistribuicaoCota));
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> buscarCotasHistorico(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		
		return this.cotaRepository.buscarCotasHistorico(listProdutoEdicaoDto, cotasAtivas);
	}
	
	@Transactional(readOnly = true)
	@Override
	public void verificarCotasSemRoteirizacao(Intervalo<Integer> intervaloCota, Intervalo<Date> intervaloDataLancamento,
			Intervalo<Date> intervaloDataRecolhimento){
		
		List<CotaDTO> cotasSemRoteirizacao = this.cotaRepository.obterCotasSemRoteirizacao(intervaloCota,
				intervaloDataLancamento, intervaloDataRecolhimento);
		
		if (cotasSemRoteirizacao == null || !cotasSemRoteirizacao.isEmpty()){
			
			List<String> msgs = new ArrayList<String>();
            msgs.add("Cotas sem roteirização:");
			for (CotaDTO c : cotasSemRoteirizacao){
				msgs.add(+ c.getNumeroCota() + " - " + c.getNomePessoa());
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
    public List<Cota> obterCotasPorNumeros(List<Integer> numerosCota){
    
        List<Cota> cotas = new ArrayList<Cota>();
    
        for (Integer numeroCota : numerosCota){
      
            Cota cota = this.obterPorNumeroDaCota(numeroCota);
      
            cotas.add(cota);
        }
        
        return cotas;
    }
    
    @Transactional
    @Override
	public boolean salvarTipoCota(long idCota, TipoCota tipoCota){
	
		Cota cota = this.obterPorId(idCota);
		
		if (!cota.getTipoCota().equals(tipoCota)){
		
			cota.setTipoCota(tipoCota);
			
			cota.setAlteracaoTipoCota(this.distribuidorService.obterDataOperacaoDistribuidor());
			
			this.alterarCota(cota);
			
			return true;
		}
		
		return false;
	}

  
  /**
   * Verifica se a cota teve seu tipo alterado na data informada
   * @param cota
   * @param data
   * @return boolean
   */
  @Override  
  @Transactional
  public boolean isCotaAlteradaNaData(Cota cota, Date data){
	  
      boolean isAlteracaoTipoCotaNaDataAtual = (cota.getAlteracaoTipoCota()!=null && 
      		                                    cota.getAlteracaoTipoCota().compareTo(data)==0);
      
      return isAlteracaoTipoCotaNaDataAtual;
  }
  
	@Override
	@Transactional(readOnly=true)
	public Long obterIdPorNumeroCota(Integer numeroCota) {
		
		return this.cotaRepository.obterIdPorNumeroCota(numeroCota);
	}
}
