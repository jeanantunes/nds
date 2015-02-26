package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.EnderecoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela lógica de visualização referentes a
 * funcionalidade de cadastro de endereços.
 * 
 * @author Discover Technology
 * 
 */
@Resource
@Path("/cadastro/endereco")
public class EnderecoController extends BaseController {
    
	                    /**
     * Constante que representa o nome do atributo com a lista de endereços
     * armazenado na sessão para serem persistidos na base.
     */
    private static String ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR = "ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR ";
    
	                    /**
     * Constante que representa o nome do atributo com a lista de endereços
     * armazenado na sessão para serem persistidos na base.
     */
    private static String ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER = "ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER";
    
    private static String ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR = "ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR";
    
	                    /**
     * Atributo armazenado na sessão que, indica se existe endereços pendentes
     * de persistência Mantendo o status caso o usuário troque de aba e ainda
     * não tenha confirmado as suas atividades na aba endereços
     */
    public static String ENDERECO_PENDENTE = "ENDERECO_PENDENTE";
    
    @Autowired
    private Result result;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private EnderecoService enderecoService;
    
    public enum Tela{
        
        ENDERECO_FIADOR,
        ENDERECO_COTA,
        ENDERECO_ENTREGADOR,
        ENDERECO_PDV,
        ENDERECO_TRANSPORTADOR,
        ENDERECO_FORNECEDOR;
        
        public void setarParametros(){
            
            switch (this){
            case ENDERECO_FIADOR:
                EnderecoController.setarParametros(
                        FiadorController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        FiadorController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        FiadorController.LISTA_ENDERECOS_EXIBICAO);
                break;
            case ENDERECO_COTA:
                EnderecoController.setarParametros(
                        CotaController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        CotaController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        CotaController.LISTA_ENDERECOS_EXIBICAO);
                
                break;
            case ENDERECO_ENTREGADOR:
                EnderecoController.setarParametros(
                        EntregadorController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        EntregadorController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        EntregadorController.LISTA_ENDERECOS_EXIBICAO);
                break;
                
            case ENDERECO_PDV:
                EnderecoController.setarParametros(
                        PdvController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        PdvController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        PdvController.LISTA_ENDERECOS_EXIBICAO);
                break;
                
            case ENDERECO_TRANSPORTADOR:
                EnderecoController.setarParametros(
                        TransportadorController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        TransportadorController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        TransportadorController.LISTA_ENDERECOS_EXIBICAO);
                
                break;
                
            case ENDERECO_FORNECEDOR:
                EnderecoController.setarParametros(
                        FornecedorController.LISTA_ENDERECOS_SALVAR_SESSAO,
                        FornecedorController.LISTA_ENDERECOS_REMOVER_SESSAO,
                        FornecedorController.LISTA_ENDERECOS_EXIBICAO);
                
                break;
            }
        }
    }
    
    @Path("/")
    public void index() {
        
        session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);
        
        session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
        
        session.removeAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR);
    }
    
    public static void setarParametros(final String listaSalvar, final String listaRemover, final String listaExibir){
        
        ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR = listaSalvar;
        
        ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER = listaRemover;
        
        ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR = listaExibir;
    }
    
	                    /**
     * ENDERECO - SESSAO
     * 
     * Método que realiza a pesquisa dos endereços cadastrados na sessão.
     * 
     * @param tela
     * @param sortname
     * @param sortorder
     */
    @Post
    public void pesquisarEnderecos( final Tela tela, final String sortname, String sortorder) {
        
        tela.setarParametros();
        
        final List<EnderecoAssociacaoDTO> listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();
        
        final List<EnderecoAssociacaoDTO> listaEnderecosExibir = this.obterEnderecosSessaoExibir();
        
        listaEndereco.addAll(listaEnderecosExibir);
        
        final List<EnderecoAssociacaoDTO> enderecosRemovidos = this.obterEnderecosSessaoRemover();
        
        if (enderecosRemovidos != null){
            
            listaEndereco.removeAll(enderecosRemovidos);
        }
        
        if (sortname != null) {
            
            sortorder = sortorder == null ? "asc" : sortorder;
            
            final Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
            
            PaginacaoUtil.ordenarEmMemoria(listaEndereco, ordenacao, sortname);
        }
        
        final TableModel<CellModel> tableModelEndereco = getTableModelListaEndereco(listaEndereco);
        
        result.use(Results.json()).from(tableModelEndereco, "result").recursive().serialize();
    }
    
    /**
     * ENDERECO - SESSAO
     * 
     * Atualiza sessao ao incluir ou alterar um endereco
     * @param enderecoAssociacao
     * @param listaEnderecoAssociacao
     * @param listaExibir
     */
    private void atualizaSessao(final EnderecoAssociacaoDTO enderecoAssociacao,
            final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao,
            final List<EnderecoAssociacaoDTO> listaExibir){
        
        final boolean principal = enderecoAssociacao.isEnderecoPrincipal();
        
        for (final EnderecoAssociacaoDTO item : listaExibir){
            
            if (principal){
                
                if (!item.equals(enderecoAssociacao)){
                    
                    item.setEnderecoPrincipal(false);
                    
                    boolean alteracao = false;
                    
                    for (EnderecoAssociacaoDTO itemAssociacao : listaEnderecoAssociacao){
                        
                        itemAssociacao.setEnderecoPrincipal((principal && !itemAssociacao.equals(enderecoAssociacao))?false:itemAssociacao.isEnderecoPrincipal());
                        
                        if (itemAssociacao.getId()!=null){
                            
                            if (itemAssociacao.getId().equals(item.getId())){
                                
                                itemAssociacao = item;
                                
                                alteracao = !alteracao?true:alteracao;
                            }
                        }
                    }
                    
                    if (!alteracao){
                        
                        listaEnderecoAssociacao.add(item);
                    }
                }
            }
        }
        
        for (final EnderecoAssociacaoDTO itemAssociacao : listaEnderecoAssociacao){
            
            itemAssociacao.setEnderecoPrincipal((principal && !itemAssociacao.equals(enderecoAssociacao))?false:itemAssociacao.isEnderecoPrincipal());
        }
        
        session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR, listaExibir);
        
        session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
    }
    
	                    /**
     * ENDERECO - SESSAO
     * 
     * Método responsável por incluir/alterar um endereço para a pessoa em
     * questão.
     * 
     * @param enderecoAssociacao
     */
    public void incluirNovoEndereco(final Tela tela, final EnderecoAssociacaoDTO enderecoAssociacao) {
        
        tela.setarParametros();
        
        validarDadosEndereco(enderecoAssociacao);
        
        validarExistenciaEnderecoPrincipal(enderecoAssociacao);
        
        
        if (enderecoAssociacao.getEndereco() != null && enderecoAssociacao.getEndereco().getCep() != null) {
            
            enderecoAssociacao.getEndereco().setCep(retirarFormatacaoCep(enderecoAssociacao.getEndereco().getCep()));
        }
        
        final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
        
        final List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
        
        
        if (enderecoAssociacao.getId() != null){
            
            boolean alteracao = false;
            
            for (int index = 0 ; index < listaEnderecoAssociacao.size() ; index++){
                
                if (listaEnderecoAssociacao.get(index).getId().equals(enderecoAssociacao.getId())){
                    
                    listaEnderecoAssociacao.set(index, enderecoAssociacao);
                    
                    alteracao = true;
                    
                    break;
                }
            }
            
            if (!alteracao){
                
                listaEnderecoAssociacao.add(enderecoAssociacao);
            }
            
            alteracao = false;
            
            for (int index = 0 ; index < listaExibir.size() ; index++){
                
                if (listaExibir.get(index).getId().equals(enderecoAssociacao.getId())){
                    
                    listaExibir.set(index, enderecoAssociacao);
                    
                    alteracao = true;
                    
                    break;
                }
            }
            
            if (!alteracao){
                
                listaExibir.add(enderecoAssociacao);
            }
            
        } else {
            
    		if (tela.equals(Tela.ENDERECO_PDV)) {
    			validarUnicoEnderecoPDV(enderecoAssociacao);
    		}
        	
            validarDuplicidadeEndereco(enderecoAssociacao);
            
            listaEnderecoAssociacao.add(enderecoAssociacao);
            
            listaExibir.add(enderecoAssociacao);
        }
        
        this.atualizaSessao(enderecoAssociacao, listaEnderecoAssociacao, listaExibir);
        
        session.setAttribute(ENDERECO_PENDENTE, Boolean.TRUE);
        
        this.pesquisarEnderecos(tela,null, null);
    }
    
	                    /**
     * ENDERECO - SESSAO
     * 
     * Método que irá remover um endereço a partir de seu ID.
     * 
     * @param idEnderecoAssociacao
     */
    @Post
    public void removerEndereco(final Tela tela,final Long idEnderecoAssociacao) {
        
        tela.setarParametros();
        
        EnderecoAssociacaoDTO enderecoRemover = null;
        
        final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
        
        for (int index = 0 ; index < listaEnderecoAssociacao.size() ; index++){
            
            if (listaEnderecoAssociacao.get(index).getId().equals(idEnderecoAssociacao)){
                
                enderecoRemover = listaEnderecoAssociacao.remove(index);
                
                break;
            }
        }
        
        final List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
        
        for (int index = 0 ; index < listaExibir.size() ; index++){
            
            if (listaExibir.get(index).getId().equals(idEnderecoAssociacao)){
                
                if (enderecoRemover == null){
                    enderecoRemover = listaExibir.remove(index);
                } else {
                    listaExibir.remove(index);
                }
                
                break;
            }
        }
        
        final List<EnderecoAssociacaoDTO> listaRemover = this.obterEnderecosSessaoRemover();
        
        listaRemover.add(enderecoRemover);
        
        session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER, listaRemover);
        
        this.pesquisarEnderecos(tela,null, null);
    }
    
	                    /**
     * ENDERECO - SESSAO
     * 
     * Método responsável por preparar um endereço para a edição.
     * 
     * @param idEnderecoAssociacao
     */
    @Post
    public void editarEndereco(final Tela tela,final Long idEnderecoAssociacao) {
        
        tela.setarParametros();
        
        EnderecoAssociacaoDTO enderecoAssociacao = null;
        
        final List<EnderecoAssociacaoDTO> listaEndereco =	this.obterEnderecosSessaoSalvar();
        
        for (final EnderecoAssociacaoDTO dto : listaEndereco){
            
            if (dto.getId().equals(idEnderecoAssociacao)){
                
                enderecoAssociacao = dto;
                
                break;
            }
        }
        
        if (enderecoAssociacao == null){
            
            final List<EnderecoAssociacaoDTO> listaEnderecoExibir =	this.obterEnderecosSessaoExibir();
            
            for (final EnderecoAssociacaoDTO dto : listaEnderecoExibir){
                
                if (dto.getId().equals(idEnderecoAssociacao)){
                    
                    enderecoAssociacao = dto;
                    
                    break;
                }
            }
        }
        
        if (enderecoAssociacao == null){
            
            final Endereco endereco = enderecoService.buscarEnderecoPorId(idEnderecoAssociacao);
            
            if (endereco != null){
                if (endereco.getCep() != null) {
                    
                    endereco.setCep(retirarFormatacaoCep(endereco.getCep()));
                }
                enderecoAssociacao = new EnderecoAssociacaoDTO(
                        false,
                        endereco,
                        null,
                        null);
                
                enderecoAssociacao.setId(System.currentTimeMillis());
                
                final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.obterEnderecosSessaoSalvar();
                
                listaEnderecoAssociacao.add(enderecoAssociacao);
                
                session.setAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR, listaEnderecoAssociacao);
            }
        }
        
        result.use(Results.json()).from(enderecoAssociacao, "result").recursive().serialize();
    }
    
	                    /**
     * Método responsável pela obtenção dos dados que irão preencher o combo de
     * UF's.
     * 
     * @param tela
     */
    public void obterDadosComboUF() {
        
        final List<String> ufs = enderecoService.obterUnidadeFederacaoBrasil();
        
        result.use(Results.json()).from(ufs, "result").serialize();
    }
    
    private void validarUnicoEnderecoPDV(final EnderecoAssociacaoDTO enderecoAssociacao) {
        
        final List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
        
        if (listaExibir!=null && listaExibir.size() > 0){
            
            // throw new ValidacaoException(TipoMensagem.WARNING, "Já existe associação de endereço para este PDV.");
        }
    }
    
    private void validarDuplicidadeEndereco(final EnderecoAssociacaoDTO enderecoAssociacao) {
        
        final List<EnderecoAssociacaoDTO> listaExibir = this.obterEnderecosSessaoExibir();
        for (final EnderecoAssociacaoDTO item : listaExibir){
            
            if (item.getEndereco().getCep().equals(enderecoAssociacao.getEndereco().getCep())&&(item.getEndereco().getNumero().equals(enderecoAssociacao.getEndereco().getNumero()))){
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Endereço já cadastrado.");
            }
        }
    }
    
	                    /**
     * Método responsável pela obtenção dos dados que irão preencher o combo de
     * Cidades.
     * 
     * @param tela
     * 
     * @param siglaUF
     */
    public void autoCompletarLocalidadePorNome(final String nomeLocalidade, final String siglaUF) {
        
        if (siglaUF == null || siglaUF.isEmpty()) {
            
            result.use(Results.json()).from("", "result").serialize();
            
            return;
        }
        
        final List<String> localidades = enderecoService.obterLocalidadesPorUFNome(nomeLocalidade, siglaUF);
        
        final List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
        
        if (localidades != null && !localidades.isEmpty()) {
            
            for (final String localidade : localidades) {
                
                
                listaAutoComplete.add(new ItemAutoComplete(localidade, null, null));
            }
        }
        
        result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
    }
    
    public void autoCompletarBairroPorNome(final Long codigoIBGE, final String nomeBairro) {
        
        if (codigoIBGE == null) {
            
            result.use(Results.json()).from("", "result").serialize();
            
            return;
        }
        
        final List<String> bairros = enderecoService.obterBairrosPorCodigoIBGENome(nomeBairro, codigoIBGE);
        
        final List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
        
        if (bairros != null && !bairros.isEmpty()) {
            
            for (final String bairro : bairros) {
                
                listaAutoComplete.add(new ItemAutoComplete(bairro, null, null));
            }
        }
        
        result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
    }
    
    public void autoCompletarLogradourosPorNome(final Long codigoBairro, final String nomeLogradouro) {
        
        if (codigoBairro == null) {
            
            result.use(Results.json()).from("", "result").serialize();
            
            return;
        }
        
        final List<String> logradouros = enderecoService.obterLogradourosPorCodigoBairroNome(codigoBairro, nomeLogradouro);
        
        final List<ItemAutoComplete> listaAutoComplete = new ArrayList<ItemAutoComplete>();
        
        if (logradouros != null && !logradouros.isEmpty()) {
            
            for (final String logradouro : logradouros) {
                
                listaAutoComplete.add(new ItemAutoComplete(logradouro, null, null));
            }
        }
        
        result.use(Results.json()).from(listaAutoComplete, "result").include("value", "chave").serialize();
    }
    
	                    /**
     * Método que realiza a busca do endereço pelo CEP
     * 
     * @param cep
     */
    @Post
    public void obterEnderecoPorCep(String cep) {
        
        if (StringUtil.isEmpty(cep)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "O CEP deve ser informado!");
        }
        
        cep = retirarFormatacaoCep(cep);
        
        final EnderecoVO enderecoRetornado = enderecoService.obterEnderecoPorCep(cep);
        
        if (enderecoRetornado == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "CEP não encontrado!");
            
        } else {
            
            result.use(Results.json()).from(enderecoRetornado, "result").recursive().serialize();
        }
    }
    
	                    /*
     * Método para popular autocomplete de logradouros da tela de pesquisa de
     * cotas
     */
    @Post
    public void pesquisarLogradouros(final String nomeLogradouro){
        
        final List<ItemAutoComplete> autoCompleteLogradouros =
                new ArrayList<ItemAutoComplete>();
        
        final List<String> logradouros =
                enderecoService.pesquisarLogradouros(nomeLogradouro);
        
        for (final String logradouro : logradouros){
            
            final ItemAutoComplete itemAutoComplete =
                    new ItemAutoComplete(logradouro, null, null);
            
            autoCompleteLogradouros.add(itemAutoComplete);
        }
        
        result.use(Results.json()).from(autoCompleteLogradouros, "result").include("value", "chave").serialize();
    }
    
	                    /*
     * Método para popular autocomplete de bairros da tela de pesquisa de cotas
     */
    @Post
    public void pesquisarBairros(final String nomeBairro){
        
        final List<ItemAutoComplete> autoCompleteBairros =
                new ArrayList<ItemAutoComplete>();
        
        final List<String> bairros =
                enderecoService.pesquisarBairros(nomeBairro);
        
        for (final String bairro : bairros){
            
            final ItemAutoComplete itemAutoComplete =
                    new ItemAutoComplete(bairro, null, null);
            
            autoCompleteBairros.add(itemAutoComplete);
        }
        
        result.use(Results.json()).from(autoCompleteBairros, "result").include("value", "chave").serialize();
    }
    
	                    /*
     * Método para popular autocomplete de municipios da tela de pesquisa de
     * cotas
     */
    @Post
    public void pesquisarLocalidades(final String nomeLocalidade){
        
        final List<ItemAutoComplete> autoCompleteLocalidades =
                new ArrayList<ItemAutoComplete>();
        
        final List<String> localidades =
                enderecoService.pesquisarLocalidades(nomeLocalidade);
        
        for (final String localidade : localidades){
            
            final ItemAutoComplete itemAutoComplete =
                    new ItemAutoComplete(localidade, null, null);
            
            autoCompleteLocalidades.add(itemAutoComplete);
        }
        
        result.use(Results.json()).from(autoCompleteLocalidades, "result").include("value", "chave").serialize();
    }
    
    private String retirarFormatacaoCep(final String cep) {
        
        return cep.replaceAll("-", "");
    }
    
    /**
     * 
     * @param enderecoAssociacao
     */
    private void validarDadosEndereco(final EnderecoAssociacaoDTO enderecoAssociacao) {
        
        final EnderecoDTO endereco = enderecoAssociacao.getEndereco();
        
        final List<String> listaMensagens = new ArrayList<String>();
        
        if (enderecoAssociacao.getTipoEndereco() == null) {
            
            listaMensagens.add("O preenchimento do campo [Tipo Endereco] é obrigatório.");
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
        
        if (endereco.getNumero() == null) {
            
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
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
        }
    }
    
	                    /**
     * Método que retorna um Table Model de acordo com a lista de Endereços
     * desejada.
     * 
     * @param listaEndereco
     * @return TableModel<CellModel>
     */
    private TableModel<CellModel> getTableModelListaEndereco(final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
        
        final TableModel<CellModel> tableModel = new TableModel<CellModel>();
        
        final List<CellModel> listaCellModel = new ArrayList<CellModel>();
        
        long idCellModel = 0;
        
        long inc = 0;
        
        for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
            
            enderecoAssociacao.setEnderecoPessoa((enderecoAssociacao.isEnderecoPessoa()) || enderecoAssociacao.getTipoEndereco() == null);
            
            if (enderecoAssociacao.getId() == null) {
                
                idCellModel = (inc + (int) System.currentTimeMillis()) * -1;
                
                inc ++;
                
                enderecoAssociacao.setEnderecoPessoa(false);
                
                enderecoAssociacao.setId(idCellModel);
            }
            
            final CellModel cellModel = getCellModelEndereco(enderecoAssociacao);
            
            listaCellModel.add(cellModel);
        }
        
        tableModel.setPage(1);
        tableModel.setRows(listaCellModel);
        tableModel.setTotal(listaCellModel.size());
        
        return tableModel;
    }
    
	                    /**
     * Método que retorna um Cell Model de acordo com o Endereço desejado.
     * 
     * @param endereco
     * @return CellModel
     */
    private CellModel getCellModelEndereco(final EnderecoAssociacaoDTO enderecoAssociacao) {
        
        return new CellModel(
                enderecoAssociacao.getId().intValue(),
                enderecoAssociacao.getTipoEndereco() == null ? "": enderecoAssociacao.getTipoEndereco().getTipoEndereco(),
                        (enderecoAssociacao.getEndereco().getTipoLogradouro() != null ? enderecoAssociacao.getEndereco().getTipoLogradouro() : "" )+ " " + enderecoAssociacao.getEndereco().getLogradouro()
            + ", nº: "
                        + enderecoAssociacao.getEndereco().getNumero(),
                        enderecoAssociacao.getEndereco().getBairro(),
                        Util.adicionarMascaraCEP(enderecoAssociacao.getEndereco().getCep()),
                        enderecoAssociacao.getEndereco().getCidade(),
                        String.valueOf(enderecoAssociacao.isEnderecoPrincipal())
                );
    }
    
	                    /**
     * Método que realiza a verificação de endereço principal na lista corrente.
     * 
     * @param listaEnderecoAssociacao
     */
    private void validarExistenciaEnderecoPrincipal(final EnderecoAssociacaoDTO enderecoAssociacaoAtual) {
        
        final List<EnderecoAssociacaoDTO> listaEnderecos = new ArrayList<EnderecoAssociacaoDTO>();
        
        final List<EnderecoAssociacaoDTO> listaEnderecosSalvar = this.obterEnderecosSessaoSalvar();
        
        final List<EnderecoAssociacaoDTO> listaEnderecosExibir = this.obterEnderecosSessaoExibir();
        
        listaEnderecos.addAll(listaEnderecosExibir);
        listaEnderecos.addAll(listaEnderecosSalvar);
        
        boolean hasPrincipal = false;
        boolean enderecoNovo = false;
        
        final EnderecoDTO endereco = (enderecoAssociacaoAtual != null) ? enderecoAssociacaoAtual.getEndereco() : null;
        
        for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecosExibir) {
            if(!enderecoAssociacao.getEndereco().getId().equals(endereco.getId())
                    || !enderecoAssociacao.getEndereco().getLogradouro().equals(endereco.getLogradouro())
                    || !enderecoAssociacao.getEndereco().getNumero().equals(endereco.getNumero())) {
                enderecoNovo = true;
                break;
            }
        }
        
        if (enderecoAssociacaoAtual != null && !enderecoAssociacaoAtual.isEnderecoPrincipal()) {
            for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecos) {
                
                if(enderecoAssociacao.isEnderecoPrincipal()) {
                    if(listaEnderecosSalvar != null && listaEnderecosSalvar.size() == 1
                            && enderecoAssociacao.getEndereco() != null && enderecoAssociacao.getEndereco().getId() != null
                            && enderecoAssociacao.getEndereco().getId().equals(endereco.getId())) {
                        hasPrincipal = false;
                    }
                } else {
                    hasPrincipal = false;
                }
                
            }
            
        } else {
            hasPrincipal = true;
        }
        
        if (!enderecoNovo && !hasPrincipal && !enderecoAssociacaoAtual.isEnderecoPrincipal()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "É necessário pelo menos um endereço principal.");
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<EnderecoAssociacaoDTO> obterEnderecosSessaoSalvar(){
        
        List<EnderecoAssociacaoDTO> listaEndereco =
                (List<EnderecoAssociacaoDTO>) session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR);
        
        if (listaEndereco == null) {
            
            listaEndereco = new ArrayList<EnderecoAssociacaoDTO>();
        }
        
        return listaEndereco;
    }
    
    @SuppressWarnings("unchecked")
    private List<EnderecoAssociacaoDTO> obterEnderecosSessaoRemover(){
        
        List<EnderecoAssociacaoDTO> lista = (List<EnderecoAssociacaoDTO>)
                session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER);
        if (lista == null){
            
            lista = new ArrayList<EnderecoAssociacaoDTO>();
        }
        
        return lista;
    }
    
    @SuppressWarnings("unchecked")
    private List<EnderecoAssociacaoDTO> obterEnderecosSessaoExibir(){
        
        List<EnderecoAssociacaoDTO> lista = (List<EnderecoAssociacaoDTO>)
                session.getAttribute(ATRIBUTO_SESSAO_LISTA_ENDERECOS_EXIBIR);
        
        if (lista == null){
            
            lista = new ArrayList<EnderecoAssociacaoDTO>();
        }
        
        return lista;
    }
    
}
