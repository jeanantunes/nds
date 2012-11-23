package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class RoteirizacaoServiceImpl implements RoteirizacaoService {
	
	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;
	
	@Autowired
	private RoteiroRepository roteiroRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao) {
		return roteiroRepository.buscarRoteiro(sortname, ordenacao);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRota(String sortname, Ordenacao ordenacao) {
		return rotaRepository.buscarRota(sortname, ordenacao);
	}

	@Override
	@Transactional
	public void incluirRoteiro(Roteiro roteiro) {
		roteiroRepository.adicionar(roteiro);
		roteiroRepository.atualizaOrdenacao(roteiro);
		
	}
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroPorDescricao(String descricao,  MatchMode matchMode ){
		return  roteiroRepository.buscarRoteiroPorDescricao(descricao, matchMode);
	}
	
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaPorRoteiro(Long idRoteiro){
		return  rotaRepository.buscarRotaPorRoteiro(idRoteiro, "ordem", Ordenacao.ASC);
	}
	
	@Override
	@Transactional
	public void incluirRota(Rota rota) {
		
		rotaRepository.adicionar(rota); 
		
		rotaRepository.atualizaOrdenacao(rota);
		
	}
	
	@Override
	@Transactional
	public void  excluirListaRota(List<Long> rotasId, Long roteiroId) {

		for (Long rotaId : rotasId ){
			Rota rota = new Rota();
			Roteirizacao roteirizacao = new Roteirizacao();
			//roteirizacao.setRota(rota);
			roteirizacaoRepository.remover(roteirizacao);
			Roteiro roteiro = new Roteiro();
			roteiro.setId(roteiroId);
			rota.setRoteiro(roteiro);
			rota.setId(rotaId);
			rota.setOrdem(0);
			rotaRepository.remover(rota);
		}	
	}

	@Override
	@Transactional
	public void transferirListaRota(List<Long> rotasId, Long roteiroId) {
		Roteiro roteiro  = new Roteiro();
		roteiro.setId(roteiroId);
		for (Long rotaId : rotasId ){
			Rota rota = rotaRepository.buscarPorId(rotaId);
			//Rota rota = new Rota();
			rota.setRoteiro(roteiro);
			rota.setId(rotaId);
			rota.setOrdem(0);
			rotaRepository.merge(rota);
		}	
		
	}

	@Override
	@Transactional
	public void transferirListaRotaComNovoRoteiro(List<Long> rotasId,
			Roteiro roteiro) {
		 incluirRoteiro(roteiro);
		 transferirListaRota(rotasId, roteiro.getId());
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome, MatchMode matchMode) {
		return rotaRepository.buscarRotaPorNome(roteiroId, rotaNome, matchMode);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotas() {
		
		return rotaRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiros() {
		
		return roteiroRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroDeBox(Long idBox){
		
		return roteiroRepository.buscarRoteiroDeBox(idBox);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaDeBox(Long idBox){
		
		return rotaRepository.buscarRotaDeBox(idBox);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota){
		
		return roteirizacaoRepository.buscarRoteirizacaoDeCota(numeroCota);
	}

	
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaPorRoteiro(String descRoteiro){
		return  rotaRepository.buscarRotaDeRoteiro(descRoteiro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaDisponivelRoteirizacaoDTO> buscarPvsPorCota(Integer numeroCota, Long rotaId ,  Long roteiroId ) {
		Roteiro roteiro = roteiroRepository.buscarPorId(roteiroId);
		
		List<PDV> listaPDV = roteirizacaoRepository.buscarPdvRoteirizacaoNumeroCota(numeroCota, rotaId,  roteiro  );
		List<CotaDisponivelRoteirizacaoDTO> lista =
				new ArrayList<CotaDisponivelRoteirizacaoDTO>();
		 Integer ordem = roteirizacaoRepository.buscarMaiorOrdem(rotaId);
		 if ( ordem == null ){
			 ordem = 0;
		 } else {
			 ordem++;
		 }
		for ( PDV pdv : listaPDV ){
			
			CotaDisponivelRoteirizacaoDTO cotaDisponivelRoteirizacaoDTO = new CotaDisponivelRoteirizacaoDTO();
			Cota cota = pdv.getCota();
			cotaDisponivelRoteirizacaoDTO.setNome(cota.getPessoa().getNome());
			cotaDisponivelRoteirizacaoDTO.setNumeroCota(cota.getNumeroCota());
			cotaDisponivelRoteirizacaoDTO.setPontoVenda(pdv.getNome());
			cotaDisponivelRoteirizacaoDTO.setOrigemEndereco("Cota");
			cotaDisponivelRoteirizacaoDTO.setIdPontoVenda(pdv.getId());
			cotaDisponivelRoteirizacaoDTO.setOrdem(ordem);
			
			for (EnderecoPDV endereco : pdv.getEnderecos()){ 
				if (endereco.isPrincipal()){
					
					String enderecoFormatado = endereco.getEndereco().getTipoLogradouro()+" "+endereco.getEndereco().getLogradouro()+", "+
					endereco.getEndereco().getBairro()+" "+endereco.getEndereco().getCidade()+" "+endereco.getEndereco().getUf()+" CEP: "+endereco.getEndereco().getCep();
					cotaDisponivelRoteirizacaoDTO.setEndereco(enderecoFormatado);
				}
			}
			
			lista.add(cotaDisponivelRoteirizacaoDTO);
		}
		return lista;
	}

	
	@Transactional(readOnly=true)
	public Rota buscarRotaPorId(Long idRota){
		return rotaRepository.buscarPorId(idRota);
	}
	

	@Transactional(readOnly=true)
	public Roteiro buscarRoteiroPorId(Long idRoteiro){
		return roteiroRepository.buscarPorId(idRoteiro);
	}
	
	
	@Transactional(readOnly=true)
	public Integer buscarMaiorOrdemRoteiro() {
		return roteiroRepository.buscarMaiorOrdemRoteiro();
	}
	
	
	@Transactional(readOnly=true)
	public Integer buscarMaiorOrdemRota(Long idRoteiro) {
		return rotaRepository.buscarMaiorOrdemRota(idRoteiro);
	}	
	
	@Override
	@Transactional
	public void excluirRoteirizacao(List<Long> roteirizacaoId) {

		for (Long id : roteirizacaoId ){
			
			Roteirizacao roteirizacao = this.roteirizacaoRepository.buscarPorId(id);
			
			roteirizacaoRepository.remover(roteirizacao);
		}	
		
	}

	@Override
	@Transactional
	public List<Rota> obterRotasPorCota(Integer numeroCota) {
		return rotaRepository.obterRotasPorCota(numeroCota);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaDisponivelRoteirizacaoDTO> buscarRoteirizacaoPorEndereco(String CEP, String uf, String municipio, String bairro, Long rotaId ,  Long roteiroId) {
		Roteiro roteiro = roteiroRepository.buscarPorId(roteiroId);
		List<PDV> listaPDV = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(CEP, uf, municipio, bairro, rotaId, roteiro  );
		List<CotaDisponivelRoteirizacaoDTO> lista =
				new ArrayList<CotaDisponivelRoteirizacaoDTO>();
		for ( PDV pdv : listaPDV ){
			CotaDisponivelRoteirizacaoDTO cotaDisponivelRoteirizacaoDTO = new CotaDisponivelRoteirizacaoDTO();
			Cota cota = pdv.getCota();
			cotaDisponivelRoteirizacaoDTO.setNome(cota.getPessoa().getNome());
			cotaDisponivelRoteirizacaoDTO.setNumeroCota(cota.getNumeroCota());
			cotaDisponivelRoteirizacaoDTO.setPontoVenda(pdv.getNome());
			cotaDisponivelRoteirizacaoDTO.setOrigemEndereco("Cota");
			cotaDisponivelRoteirizacaoDTO.setIdPontoVenda(pdv.getId());
			for (EnderecoPDV endereco : pdv.getEnderecos()){ 
				if (endereco.isPrincipal()){
					String enderecoFormatado = endereco.getEndereco().getTipoLogradouro()+" "+endereco.getEndereco().getLogradouro()+", "+
					endereco.getEndereco().getBairro()+" "+endereco.getEndereco().getCidade()+" "+endereco.getEndereco().getUf()+" CEP: "+endereco.getEndereco().getCep();
					cotaDisponivelRoteirizacaoDTO.setEndereco(enderecoFormatado);
				}
			}
			
			lista.add(cotaDisponivelRoteirizacaoDTO);
		}
		return lista;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroEspecial() {
		return roteiroRepository.buscarRoteiroEspecial();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro){
		
		return roteirizacaoRepository.buscarRoteirizacaoSumarizadoPorCota(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro){
		
		return roteirizacaoRepository.buscarRoteirizacao(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro){
		
		return roteirizacaoRepository.obterCotasParaBoxRotaRoteiro(idBox, idRota, idRoteiro);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro) {
		
		if(filtro.getIdRota()!= null || filtro.getNumeroCota()!= null){
			
			return roteirizacaoRepository.buscarQuantidadeRoteirizacao(filtro);
		}
		
		return roteirizacaoRepository.buscarQuantidadeRoteirizacaoSumarizadoPorCota(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, 
																		 String  orderBy, Ordenacao ordenacao, 
																		 int initialResult, int maxResults) {
		
		return roteirizacaoRepository.buscarRoteirizacaoPorNumeroCota(numeroCota, tipoRoteiro,  orderBy,  ordenacao,  initialResult,  maxResults);
	}
	
	@Override
	@Transactional
	public void atualizaOrdenacao(Roteirizacao roteirizacao ){
	
	}

	@Override
	@Transactional
	public List<BoxRoteirizacaoDTO> obterBoxesPorNome(String nome) {
		return roteirizacaoRepository.obterBoxesPorNome(nome);
	}

	@Override
	@Transactional
	public List<RoteiroRoteirizacaoDTO> obterRoteirosPorNomeEBoxes(String nome,
			List<Long> idsBoxes) {
		return roteirizacaoRepository.obterRoteirosPorNomeEBoxes(nome, idsBoxes);
	}

	@Override
	@Transactional
	public List<RotaRoteirizacaoDTO> obterRotasPorNomeERoteiros(String nome,
			List<Long> idsRoteiros) {
		return roteirizacaoRepository.obterRotasPorNomeERoteiros(nome, idsRoteiros);
	}

	@Override
	@Transactional
	public Roteirizacao buscarRoteirizacaoPorId(Long idRoteirizacao){
		
		Roteirizacao roteirizacao = this.roteirizacaoRepository.buscarPorId(idRoteirizacao);
		return roteirizacao;
	}
	
	
	
	
	//NOVA ROTEIRIZAÇÃO
	
	/**
	 * Obtém um Roteiro do box considerando a ordem
	 * @param idBox
	 * @return Roteiro
	 */
	@Override
	@Transactional(readOnly=true)
	public Roteiro obterRoteiroDeBoxPorOrdem(Long idBox){
		List<Roteiro> listaRoteiro = roteiroRepository.buscarRoteiroDeBox(idBox);
		Roteiro roteiro = null;
		for (Roteiro item:listaRoteiro){
			if (roteiro==null || roteiro.getOrdem()>item.getOrdem()){
			    roteiro = item;
			}
		}
		return roteiro;
	}
	
	/**
	 * Obtém um Rota do Roteiro considerando a ordem
	 * @param idRoteiro
	 * @return Rota
	 */
	@Override
	@Transactional(readOnly=true)
	public Rota obterRotaDeRoteiroPorOrdem(Long idRoteiro){
		List<Rota> listaRota = rotaRepository.buscarRotaPorRoteiro(idRoteiro, null, null);
		Rota rota = null;
		for (Rota item:listaRota){
			if (rota==null || rota.getOrdem()>item.getOrdem()){
			    rota = item;
			}
		}
		return rota;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<Box> obterListaBoxLancamento(String nomeBox){
		return boxRepository.obterListaBox(nomeBox, TipoBox.LANCAMENTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<Roteiro> obterListaRoteiroPorBox(Long idBox, String descricaoRoteiro){
		List<Roteiro> listaRoteiro = new ArrayList<Roteiro>();
		listaRoteiro = this.roteiroRepository.buscarRoteiroDeBox(idBox, descricaoRoteiro);
		return listaRoteiro;
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Transactional
	@Override
	public List<Rota> obterListaRotaPorRoteiro(Long idRoteiro, String descricaoRota){
		return  rotaRepository.buscarRotaPorRoteiro(idRoteiro, null, null);
	}

	@Override
	@Transactional
	public List<Roteiro> obterRoteirosPorCota(Integer numeroCota) {
		
		return roteiroRepository.obterRoteirosPorCota(numeroCota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public RoteirizacaoDTO obterRoteirizacaoPorId(Long id){
	    Roteirizacao roteirizacao = roteirizacaoRepository.buscarPorId(id);
	    List<Box> disponiveis = new ArrayList<Box>();
	    Box box = roteirizacao.getBox();
        if (box != null) {
            disponiveis.add(box);
        }
		return RoteirizacaoDTO.toDTO(roteirizacao, disponiveis, false);
	}
  	
	/**
	 * Obtém PDVS's disponiveis
	 * @return List<PdvRoteirizacaoDTO>
	 */
	@Override
	@Transactional
	public List<PdvRoteirizacaoDTO> obterPdvsDisponiveis(Integer numCota, String municipio, String uf, String bairro, String cep) {
		
		List<PdvRoteirizacaoDTO> listaPdvDTO = new ArrayList<PdvRoteirizacaoDTO>();
		
		List<PDV> listaPdv = this.pdvRepository.obterPDVPorCotaEEndereco(numCota, municipio, uf, bairro, cep);
		
		PdvRoteirizacaoDTO pdvDTO;
		
		Integer ordem=0;
		
		for(PDV itemPdv:listaPdv){
		    
			ordem++;
			
			pdvDTO = new PdvRoteirizacaoDTO();
			
			pdvDTO.setId(itemPdv.getId());
			
			pdvDTO.setNome(itemPdv.getCota().getPessoa().getNome());
		
			pdvDTO.setOrdem(ordem);
			pdvDTO.setCota(itemPdv.getCota().getNumeroCota());
			
			Endereco endereco = null;
			EnderecoPDV enderecoPdvEntrega  = itemPdv.getEnderecoEntrega();
			if (enderecoPdvEntrega !=null){
				endereco = enderecoPdvEntrega .getEndereco();
				pdvDTO.setOrigem(OrigemEndereco.PDV.getDescricao());
			}
			else{
				EnderecoCota enderecoPrincipalCota = itemPdv.getCota().getEnderecoPrincipal();
				if (enderecoPrincipalCota !=null){
				    endereco = enderecoPrincipalCota.getEndereco();
				}    
				pdvDTO.setOrigem(OrigemEndereco.COTA.getDescricao());
			}
			
			pdvDTO.setEndereco(endereco!=null?endereco.getLogradouro()+", "+endereco.getCidade()+", CEP:"+endereco.getCep():"");

			pdvDTO.setPdv(itemPdv.getNome());

			listaPdvDTO.add(pdvDTO);
		}
		
		return listaPdvDTO;
	}
	
	/**
	 * Verifica se pdv esta disponivel (não vinculado a um box roteirizado)
	 * @param idPdv identificador do PDV
	 * @param idBox identificador do BOX
	 * @return boolean true box está disponível para roteirização, false
	 * caso contrário
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean verificaDisponibilidadePdv(Long idPdv, Long idBox) {
		if (idBox == null) {
		    return true;
		}
	    Box box = this.roteirizacaoRepository.obterBoxDoPDV(idPdv);
		return box == null || box.getId().equals(idBox);
	}
	
	/**
	 * Inclui Cota Pdv na Roteirização
	 * @param List<Long> idPdvs
	 */
	@Override
	@Transactional
	public void incluirCotaPdv(List<Long> idPdvs, Long idRota) {
		
		Rota rota = rotaRepository.buscarPorId(idRota);
		List<PDV> pdvs = pdvRepository.obterPDVPorRota(rota.getId());
		
		for (Long itemId:idPdvs){
			PDV pdv = pdvRepository.buscarPorId(itemId);
			if (pdv!=null){
			    pdvs.add(pdv);
			}
		}

	    rotaRepository.merge(rota);
	}
	
	/**
	 * Exclui Cota Pdv na Roteirização
	 * @param List<Long> idPdvs
	 */
	@Override
	@Transactional
	public void excluirCotaPdv(List<Long> idPdvs, Long idRota) {
		
		Rota rota = rotaRepository.buscarPorId(idRota);
		List<PDV> pdvs = pdvRepository.obterPDVPorRota(rota.getId());
		
		for (Long itemId:idPdvs){
			PDV pdv = pdvRepository.buscarPorId(itemId);
			if (pdv!=null){
			    pdvs.remove(pdv);
			}
		}

    	rotaRepository.merge(rota);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    @Transactional(readOnly = true)
    public RoteirizacaoDTO obterRoteirizacaoPorBox(Long idBox) {
        Roteirizacao roteirizacao = roteirizacaoRepository.obterRoteirizacaoPorBox(idBox);
        List<Box> disponiveis = obterListaBoxLancamento(null);
        if (roteirizacao != null) {
            return RoteirizacaoDTO.toDTO(roteirizacao, disponiveis, true);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional
    public Roteirizacao confirmarRoteirizacao(RoteirizacaoDTO dto) {
	    ValidacaoVO validacao = validarRoteirizacao(dto);
	    if (!TipoMensagem.SUCCESS.equals(validacao.getTipoMensagem())) {
	        throw new ValidacaoException(validacao);  
	    }
	    Roteirizacao roteirizacao;
	    if (dto.isNovo()) {
	        roteirizacao = processarNovaRoteirizacao(dto);
	    } else {
	        roteirizacao = processarRoteirizacaoExistente(dto);
	    }
	    processarTransferenciaRoteiros(dto);
	    return roteirizacao;
    }


    /**
     * Processa as informações de uma nova roteirização
     * 
     * @param dto
     *            dto com as informações da nova roteirização
     * @return Roteirizacao criada com as informações do DTO
     */
	private Roteirizacao processarNovaRoteirizacao(RoteirizacaoDTO dto) {
        Roteirizacao roteirizacao = new Roteirizacao();
        TipoRoteiro tipoRoteiro = dto.isBoxEspecial() ? TipoRoteiro.ESPECIAL : TipoRoteiro.NORMAL;
        processarBoxRoteirizacao(dto, roteirizacao); 
        for (RoteiroRoteirizacaoDTO roteiroDTO : dto.getTodosRoteiros()) {
            Roteiro roteiro = novoRoteiroRoteirizacao(roteirizacao, tipoRoteiro, roteiroDTO);
            for (RotaRoteirizacaoDTO rotaDTO : roteiroDTO.getTodasRotas()) {
                Rota rota = novaRotaRoteiro(roteiro, rotaDTO);
                for (PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
                    novoPDVRota(rota, pdvDTO, roteirizacao.getBox());
                } 
            }
        }
        roteirizacaoRepository.adicionar(roteirizacao);
        return roteirizacao;
    }

	 /**
     * Processa as informações de uma roteirização existente
     * 
     * @param dto
     *            dto com as informações da roteirização existente
     * @return Roteirizacao alterada com as informações do DTO
     */
	private Roteirizacao processarRoteirizacaoExistente(RoteirizacaoDTO dto) {
        Roteirizacao roteirizacao = roteirizacaoRepository.buscarPorId(dto.getId());
        TipoRoteiro tipoRoteiro = dto.isBoxEspecial() ? TipoRoteiro.ESPECIAL : TipoRoteiro.NORMAL;
        Set<Long> roteirosExclusao = dto.getRoteirosExclusao();
        roteirizacao.desassociarRoteiros(roteirosExclusao);
        for (RoteiroRoteirizacaoDTO roteiroDTO : dto.getTodosRoteiros()) {
            Roteiro roteiro;
            if (roteiroDTO.isNovo()) {
                roteiro = novoRoteiroRoteirizacao(roteirizacao, tipoRoteiro, roteiroDTO);
            } else {
                roteiro = roteirizacao.getRoteiro(roteiroDTO.getId());
                roteiro.desassociarRotas(roteiroDTO.getRotasExclusao());
            }
            for (RotaRoteirizacaoDTO rotaDTO : roteiroDTO.getTodasRotas()) {
                Rota rota;
                if (rotaDTO.isNovo()) {
                    rota = novaRotaRoteiro(roteiro, rotaDTO);
                } else {
                    rota = roteiro.getRota(rotaDTO.getId());
                    rota.desassociarPDVs(rotaDTO.getPdvsExclusao());
                }
                for (PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
                    RotaPDV rotaPDVExistente = rota.getRotaPDVPorPDV(pdvDTO.getId());
                    if (rotaPDVExistente == null) {
                        novoPDVRota(rota, pdvDTO, roteirizacao.getBox());
                    } else {
                        rota.alterarOrdemPdv(pdvDTO.getId(), pdvDTO.getOrdem());
                    }
                }
            }
        }
        roteirizacaoRepository.alterar(roteirizacao);
        return roteirizacao;
    }
	
    /**
     * Processa as transferências de roteiro da roteirização
     * 
     * @param dto
     *            dto com as informações de transferência de roteiro
     */
    private void processarTransferenciaRoteiros(RoteirizacaoDTO dto) {
        Map<Long, Set<RoteiroRoteirizacaoDTO>> roteirosTransferidos = dto.getRoteirosTransferidos();
        for (Entry<Long, Set<RoteiroRoteirizacaoDTO>> entry : roteirosTransferidos.entrySet()) {
            Long idBox = entry.getKey();
            Box box = boxRepository.buscarPorId(idBox);
            Set<RoteiroRoteirizacaoDTO> roteirosTransferencia = entry.getValue();
            RoteirizacaoDTO dtoTransferencia = null;
            Roteirizacao existente = roteirizacaoRepository.obterRoteirizacaoPorBox(idBox);
            if (existente != null) {
                dtoTransferencia = RoteirizacaoDTO.toDTO(existente, Arrays.asList(box), false);
            } else {
                BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
                dtoTransferencia = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
                dtoTransferencia.setBox(boxDTO);
            }
            for (RoteiroRoteirizacaoDTO roteiro : roteirosTransferencia) {
                RoteiroRoteirizacaoDTO roteiroTransferido = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), roteiro.getOrdem(), roteiro.getNome());
                dtoTransferencia.addRoteiroAposMaiorOrdem(roteiroTransferido);
                for (RotaRoteirizacaoDTO rota : roteiro.getTodasRotas()) {
                    RotaRoteirizacaoDTO rotaTransferida = new RotaRoteirizacaoDTO(Long.valueOf(-1), rota.getOrdem(), rota.getNome());
                    rotaTransferida.addAllPdv(rota.getPdvs());
                    roteiroTransferido.addRota(rotaTransferida);
                }
            }
            if (dtoTransferencia.isNovo()) {
                processarNovaRoteirizacao(dtoTransferencia);
            } else {
                processarRoteirizacaoExistente(dtoTransferencia);
            }
        }
    }
 
    /**
     * Associa o box a uma nova roteirização
     * 
     * @param dto
     *            dto com as informações da roteirização
     * @param roteirizacao
     *            Roteirizacao para associação do Box
     */
    private void processarBoxRoteirizacao(RoteirizacaoDTO dto, Roteirizacao roteirizacao) {
        if (!dto.isNovo()) {
            throw new IllegalArgumentException("Associação de Box permitida apenas para uma nova roteirização!");
        }
        if (!dto.isBoxEspecial()) {
            Box box = boxRepository.buscarPorId(dto.getBox().getId());
            Roteirizacao existente = roteirizacaoRepository.obterRoteirizacaoPorBox(box.getId());
            if (existente != null) {
                throw new ValidacaoException(TipoMensagem.ERROR, "Box já está associado a uma Roteirização!");
            }
            roteirizacao.setBox(box);
        }
    }
   
    /**
     * Adiciona um novo PDV à Rota
     * 
     * @param rota
     *            Rota para associação
     * @param pdvDTO
     *            PDV para associação
     * @param box Box ao qual a roteirização está associada 
     **/
    private void novoPDVRota(Rota rota, PdvRoteirizacaoDTO pdvDTO, Box box) {
        if (box != null) {
            boolean pdvDisponivel = verificaDisponibilidadePdv(pdvDTO.getId(), box.getId());
            if (!pdvDisponivel) {
                throw new ValidacaoException(
                        TipoMensagem.ERROR,
                        String.format(
                                "O PDV [%s] já pertence a uma Roteirização associada a um Box!",
                                pdvDTO.getPdv()));
            }
        }
        PDV pdv = pdvRepository.buscarPorId(pdvDTO.getId());
        rota.addPDV(pdv, pdvDTO.getOrdem(), box);
    }
    
    /**
     * Adiciona uma nova Rota à Roteirização
     * @param roteiro roteiro para associação
     * @param rotaDTO rota para associação
     * @return Nova Rota associada
     */
    private Rota novaRotaRoteiro(Roteiro roteiro, RotaRoteirizacaoDTO rotaDTO) {
        Rota rota = new Rota(rotaDTO.getNome(), rotaDTO.getOrdem());
        roteiro.addRota(rota);
        return rota;
    }
    
    /**
     * Adiciona um novo roteiro à roteirização
     * @param roteirizacao roteirização para associação
     * @param tipoRoteiro Tipo do Roteiro para associação
     * @param roteiroDTO roteiro para associação
     * @return Novo Roteiro Associado
     */
    private Roteiro novoRoteiroRoteirizacao(Roteirizacao roteirizacao, TipoRoteiro tipoRoteiro, RoteiroRoteirizacaoDTO roteiroDTO) {
        Roteiro roteiro = new Roteiro(roteiroDTO.getNome(), roteiroDTO.getOrdem(), tipoRoteiro);
        roteirizacao.addRoteiro(roteiro);
        return roteiro;
    }

    @Override
    public ValidacaoVO validarRoteirizacao(RoteirizacaoDTO dto) {
        List<String> erros = new ArrayList<String>();
        if (dto.getBox() == null) {
            erros.add("É necessário selecionar um Box para Roteirização!");
        } else {
            if (dto.getTodosRoteiros().isEmpty()) {
                erros.add("É necessário ao menos um Roteiro para a Roteirização!");
            } else {
                for (RoteiroRoteirizacaoDTO roteiro : dto.getTodosRoteiros()) {
                    if (roteiro.getTodasRotas().isEmpty()) {
                        erros.add(String.format("Roteiro [%s] sem Rota associada!", roteiro.getNome()));
                    } else {
                        for (RotaRoteirizacaoDTO rota : roteiro.getTodasRotas()) {
                            if (rota.getPdvs().isEmpty()) {
                                erros.add(String.format("Rota [%s] sem PDV associado!", rota.getNome()));
                            }
                        }
                    }
                }
            }
        }
        if (erros.isEmpty()) {
            return new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização válida!");
        } else {
            return new ValidacaoVO(TipoMensagem.ERROR, erros);
        }
    }
}
