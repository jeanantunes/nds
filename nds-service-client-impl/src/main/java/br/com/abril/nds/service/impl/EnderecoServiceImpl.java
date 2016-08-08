package br.com.abril.nds.service.impl;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.EnderecoUniqueConstraintViolationException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.vo.EnderecoVO;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class EnderecoServiceImpl implements EnderecoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoServiceImpl.class);


	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EnderecoCotaRepository enderecoCotaRepository;
	
	private static final String DB_NAME  =  "correios";

	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbConnector couchDbConnector;
	
	@PostConstruct
	public void initCouchDbClient() throws MalformedURLException {
		HttpClient authenticatedHttpClient = new StdHttpClient.Builder()
                .url(
                		new URL(
                		couchDbProperties.getProtocol(), 
                		couchDbProperties.getHost(), 
                		couchDbProperties.getPort(), 
                		"")
                	)
                .username(couchDbProperties.getUsername())
                .password(couchDbProperties.getPassword())
                .build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
		this.couchDbConnector = dbInstance.createConnector(DB_NAME, true);
				
	}

	
	@Override
	@Transactional
	public void removerEndereco(Endereco endereco) {

		this.enderecoRepository.remover(endereco);
	}

	@Override
	@Transactional
	public Endereco salvarEndereco(Endereco endereco) {

		return this.enderecoRepository.merge(endereco);
	}

	@Override
	@Transactional
	public Endereco obterEnderecoPorId(Long idEndereco) {

		return this.enderecoRepository.buscarPorId(idEndereco);
	}

	@Override
	public void validarEndereco(EnderecoDTO endereco, TipoEndereco tipoEndereco) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (endereco == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Endereço é obrigatório.");
		}
		
		if (tipoEndereco == null) {
			
            listaMensagens.add("O preenchimento do campo [Tipo Endereço] é obrigatório.");
		}
		
		if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [CEP] é obrigatório.");
		}

		if (endereco.getTipoLogradouro() == null || endereco.getTipoLogradouro().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [Tipo Logradouro] é obrigatório.");
		}
		
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [Logradouro] é obrigatório.");
		}

		if (endereco.getNumero() == null || endereco.getNumero().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [Número] é obrigatório.");
		}
		
		if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [Bairro] é obrigatório.");
		}		

		if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [Cidade] é obrigatório.");
		}
		
		if (endereco.getUf() == null || endereco.getUf().isEmpty()) {
			
            listaMensagens.add("O preenchimento do campo [UF] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
		
	}

	@Override
	@Transactional
	public void removerEnderecos(Collection<Long> idsEndereco) {
		
		if (idsEndereco != null && !idsEndereco.isEmpty()){
			
			try{
				
				this.enderecoRepository.removerEnderecos(idsEndereco);
				
			}catch (RuntimeException e) {
				
				if( e instanceof org.springframework.dao.DataIntegrityViolationException){
                    throw new EnderecoUniqueConstraintViolationException(
                            "Exclusão de endereço não permitida, registro possui dependências!");
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Endereco buscarEnderecoPorId(Long idEndereco) {
		
		return this.enderecoRepository.buscarPorId(idEndereco);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.EnderecoService#buscarEnderecosPorIdPessoa(java.lang.Long, java.util.Set)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> buscarEnderecosPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar){
		
		List<EnderecoAssociacaoDTO> ret = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<Endereco> lista = this.enderecoRepository.buscarEnderecosPessoa(idPessoa, idsIgnorar);
		
		for (Endereco endereco : lista){
			
			EnderecoAssociacaoDTO dto = new EnderecoAssociacaoDTO(false, null, null, endereco);
			ret.add(dto);
		}
		
		return ret;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<EnderecoAssociacaoDTO> buscarEnderecosPorPessoaCotaPDVs(Long idPessoa, Set<Long> idsIgnorar){
	    
	    List<EnderecoAssociacaoDTO> ret = new ArrayList<EnderecoAssociacaoDTO>();
        
        for (Endereco endereco : this.enderecoRepository.buscarEnderecosPorPessoaCotaPDVs(idPessoa, idsIgnorar)){
            
            ret.add(new EnderecoAssociacaoDTO(false, null, null, endereco));
        }
        
        return ret;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> obterMunicipiosCotas() {
		return enderecoRepository.obterMunicipiosCotas();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> obterUnidadeFederativaPDVSemRoteirizacao() {
		return enderecoRepository.obterUFsPDVSemRoteirizacao();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> obterBairrosPDVSemRoteirizacao(String uf, String cidade) {
		return this.enderecoRepository.obterBairrosPDVSemRoteirizacao(uf, cidade);
	}
	

	/**
	 * {@inheritDoc} 
	 */
	@Override
	@Transactional(readOnly = true)
	public List<String> obterUnidadeFederacaoBrasil() {

		ViewQuery query = new ViewQuery()
	    	.designDocId("_design/cep")
	    	.viewName("ufs")
	    	.includeDocs(true);
	
		List<JsonNode> list = couchDbConnector.queryView(query, JsonNode.class);
		List<String> ret = null;
		if (!list.isEmpty()) {
			
			try {
				ObjectMapper om = new ObjectMapper();
				
				ret = new ArrayList<String>();
	
				for (JsonNode jsonUf: list) {
					ret.add( om.treeToValue(jsonUf, UnidadeFederacao.class).getSigla() );
				}			
				
			} catch (JsonParseException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
				
		}		
		return ret;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	@Transactional(readOnly = true)
	public List<String> obterLocalidadesPorUFNome(String nome, String siglaUF) {

		if (siglaUF == null || siglaUF.isEmpty()) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "A escolha da UF é obrigatória.");
		}
		
		return this.enderecoRepository.obterLocalidadesPorUFNome(nome, siglaUF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public EnderecoVO obterEnderecoPorCep(String cep) {
		if (cep == null || cep.trim().isEmpty()) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "O CEP é obrigatório para a pesquisa.");
		}		
		
		ViewQuery query = new ViewQuery()
        	.designDocId("_design/cep")
        	.viewName("full")
        	.includeDocs(true)
        	.startKey(ComplexKey.of(cep))
        	.endKey(ComplexKey.of(cep, ComplexKey.emptyObject()));        	
		
		List<JsonNode> list = couchDbConnector.queryView(query, JsonNode.class);
		EnderecoVO ret = null;
		if (!list.isEmpty()) {
			
			Logradouro logradouro = null;
			Localidade localidade = null;
			Bairro bairroInicial = null;
//			Bairro bairroFinal = null;
			try {
				ObjectMapper om = new ObjectMapper();
				
				ret = new EnderecoVO();

				if (list.size() == 3) {
					logradouro = om.treeToValue(list.get(0), Logradouro.class);
					localidade = om.treeToValue(list.get(1), Localidade.class);
					bairroInicial = om.treeToValue(list.get(2), Bairro.class);
					ret.setBairro(bairroInicial.getNome());
					ret.setUf(logradouro.getUf());
					ret.setCep(logradouro.getCep());
					ret.setLogradouro(logradouro.getNome());				
					ret.setTipoLogradouro(logradouro.getTipoLogradouro());				
					ret.setBairro(bairroInicial.getNome());	
					
				} else if (list.size() == 4) {
						logradouro = om.treeToValue(list.get(0), Logradouro.class);
						localidade = om.treeToValue(list.get(1), Localidade.class);
						bairroInicial = om.treeToValue(list.get(2), Bairro.class);
						ret.setBairro(bairroInicial.getNome());
						ret.setUf(logradouro.getUf());
						ret.setCep(logradouro.getCep());
						ret.setLogradouro(logradouro.getNome());				
						ret.setTipoLogradouro(logradouro.getTipoLogradouro());				
						ret.setBairro(bairroInicial.getNome());				
					 	
				} else {
					localidade = om.treeToValue(list.get(0), Localidade.class);
					ret.setUf(localidade.getUnidadeFederacao().get_id().replace("uf/", ""));
				}
//				bairroFinal = om.treeToValue(list.get(3), Bairro.class);
				
								
				ret.setCodigoCidadeIBGE(localidade.getCodigoMunicipioIBGE());		
				if ( ret.getCodigoCidadeIBGE() != null )
			     ret.setCodigoUf(new Long((""+ret.getCodigoCidadeIBGE()).substring(0,2)));
				ret.setIdLocalidade(localidade.get_id());
				ret.setLocalidade(localidade.getNome());				
				
			} catch (JsonParseException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<String> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE) {

		return this.enderecoRepository.obterBairrosPorCodigoIBGENome(nome, codigoIBGE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<String> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro) {

		return this.enderecoRepository.obterLogradourosPorCodigoBairroNome(codigoBairro, nomeLogradouro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> pesquisarLogradouros(String nomeLogradouro) {
		
		return this.enderecoRepository.pesquisarLogradouros(nomeLogradouro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> pesquisarBairros(String nomeBairro) {
		
		return this.enderecoRepository.pesquisarBairros(nomeBairro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> pesquisarTodosBairros() {
		
		//return this.enderecoRepository.buscarTodos();
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> pesquisarLocalidades(String nomeLocalidade) {
		
		return this.enderecoRepository.pesquisarLocalidades(nomeLocalidade);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> obterBairrosPorCidade(String cidade) {
		return this.enderecoRepository.obterBairrosPorCidade(cidade);
	}


	@Override
	@Transactional(readOnly = true)
	public List<String> obterListaLocalidadePdv() {
		return this.enderecoRepository.obterListaLocalidadePdv();
	}


	@Override
	@Transactional(readOnly = true)	
	public List<String> obterLocalidadesPorUFPDVSemRoteirizacao(String uf) {
		if (uf == null || uf.isEmpty()) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "A escolha da UF é obrigatória.");
		}
		
		return this.enderecoRepository.obterLocalidadesPorUFPDVSemRoteirizacao(uf);
	}


	/**
	 * @return
	 * @see br.com.abril.nds.repository.EnderecoRepository#obterBairrosCotas()
	 */
	@Override
	@Transactional(readOnly = true)	
	public List<String> obterBairrosCotas() {
		return enderecoRepository.obterBairrosCotas();
	}


	@Override
	@Transactional(readOnly = true)
	public Long obterQtdEnderecoAssociadoCota(Long idCota) {
		
		return this.enderecoCotaRepository.obterQtdEnderecoAssociadoCota(idCota);
	}


	@Override
	@Transactional
	public List<String> obterLocalidadesPorUFPDVBoxEspecial(String uf) {
		
		if (uf == null || uf.isEmpty()) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "A escolha da UF é obrigatória.");
		}
		
		return this.enderecoRepository.obterLocalidadesPorUFPDVBoxEspecial(uf);
	}


	@Override
	@Transactional
	public List<String> obterBairrosPDVBoxEspecial(String uf, String cidade) {
		return this.enderecoRepository.obterBairrosPDVBoxEspecial(uf, cidade);
	}
	
	@Override
	public List<ItemDTO<String, String>> buscarMunicipioAssociadasCota(){
		return enderecoCotaRepository.buscarMunicipio();
	}


	@Override
	public List<Endereco> enderecosIncompletos() {
		return enderecoCotaRepository.enderecosIncompletos();
	}
	
}