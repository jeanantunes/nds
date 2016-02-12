package br.com.abril.nds.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MapaRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.OrdenacaoUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

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
	
	@Autowired
	private EntregadorRepository entregadorRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RotaService rotaService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
	public List<Rota> buscarRotasPorRoteiro(Long idRoteiro){
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
	@Transactional(readOnly = true)
	public Long obterQtdRotasPorCota(Integer numeroCota){
		
		return this.rotaRepository.obterQtdRotasPorCota(numeroCota);
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
	@Transactional
	public List<RotaRoteirizacaoDTO> buscarRotasEspeciais() {
		
		List<Rota> listaRotasEspeciais = this.rotaRepository.buscarRotaDeBox(null);
		
		return RotaRoteirizacaoDTO.getDTOFrom(listaRotasEspeciais);
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
	public List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro,
			String sortname, String sortorder){
		
		return roteirizacaoRepository.obterCotasParaBoxRotaRoteiro(idBox, idRota, idRoteiro, sortname, sortorder);
		
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
	
	
	
	
    // NOVA ROTEIRIZAÇÃO
	
	    /**
     * Obtém um Roteiro do box considerando a ordem
     * 
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
     * 
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
	public List<Box> obterListaBoxLancamento(String nomeBox) {
		return boxRepository.obterListaBox(nomeBox, Arrays.asList(TipoBox.LANCAMENTO));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<Roteiro> obterListaRoteiroPorBox(Long idBox, String descricaoRoteiro){
		return this.roteiroRepository.buscarRoteiroDeBox(idBox, descricaoRoteiro);
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
     * 
     * @return List<PdvRoteirizacaoDTO>
     */
	@Override
	@Transactional
	public List<PdvRoteirizacaoDTO> obterPdvsDisponiveis(Integer numCota, String municipio, String uf, String bairro, String cep, boolean pesquisaPorCota, Long boxID) {
		
		List<PdvRoteirizacaoDTO> listaPdvDTO = new ArrayList<PdvRoteirizacaoDTO>();
		
		List<PDV> listaPdv = this.pdvRepository.obterCotasPDVsDisponiveisPor(numCota, municipio, uf, bairro, cep, boxID, boxRepository.obterIdBoxEspecial());
		
		PdvRoteirizacaoDTO pdvDTO;
		
		Integer ordem=0;
		
		for(PDV itemPdv : listaPdv) {
			
			Endereco endereco = null;
			ordem++;
			pdvDTO = new PdvRoteirizacaoDTO();
			pdvDTO.setId(itemPdv.getId());
			pdvDTO.setNome(itemPdv.getNome());
			pdvDTO.setOrdem(ordem);
			pdvDTO.setCota(itemPdv.getCota().getNumeroCota());
			
			//especial
			if (boxID == -1){
				
				EnderecoPDV endPdv = itemPdv.getEnderecoEntrega();
				
				if (endPdv != null){
					
					endereco = endPdv.getEndereco();
					pdvDTO.setOrigem(OrigemEndereco.PDV.getDescricao());
				} else {
					
					if (itemPdv.getCaracteristicas().isPontoPrincipal()){
					
						endPdv = itemPdv.getEnderecoPrincipal();
						endPdv = (endPdv == null ? itemPdv.getEnderecos().iterator().next() : endPdv);
						
						if (endPdv != null){
							
							endereco = endPdv.getEndereco();
							pdvDTO.setOrigem(OrigemEndereco.PDV.getDescricao());
						}
					} else {
						
						endPdv = (endPdv == null ? itemPdv.getEnderecos().iterator().next() : endPdv);
						
						if (endPdv != null){
							
							endereco = endPdv.getEndereco();
							pdvDTO.setOrigem(OrigemEndereco.PDV.getDescricao());
						}
					}
				}
			//normal
			} else {
				
				EnderecoCota endCota = itemPdv.getCota().getEnderecoPorTipoEndereco(TipoEndereco.LOCAL_ENTREGA);
				
				if (endCota != null){
					
					endereco = endCota.getEndereco();
				    pdvDTO.setOrigem(OrigemEndereco.COTA.getDescricao());
				}
				
				if (endereco == null){
					
					EnderecoPDV endPdv = itemPdv.getEnderecoPrincipal();	
					if (endPdv != null){
						
						endereco = endPdv.getEndereco();
						pdvDTO.setOrigem(OrigemEndereco.PDV.getDescricao());
					}
				}
				
				if (endereco == null){
					
					endCota = itemPdv.getCota().getEnderecoPrincipal();
					
					if (endCota != null){
						endereco = endCota.getEndereco();
						pdvDTO.setOrigem(OrigemEndereco.COTA.getDescricao());
					}
				}
				
			}
			
			pdvDTO.setEndereco(endereco != null ? 
					endereco.getTipoLogradouro() + " " +
					endereco.getLogradouro() + " " + 
 "nº.: " + endereco.getNumero() + ", " +
					endereco.getCidade() +
					", CEP: " + Util.adicionarMascaraCEP(endereco.getCep()) : "");

			pdvDTO.setPdv(itemPdv.getNome());

			listaPdvDTO.add(pdvDTO);
		}
		
		return listaPdvDTO;
	}
	
	    /**
     * Verifica se pdv esta disponivel (não vinculado a um box roteirizado)
     * 
     * @param idPdv identificador do PDV
     * @param idBox identificador do BOX
     * @return boolean true box está disponível para roteirização, false caso
     *         contrário
     */
	@Override
	@Transactional(readOnly = true)
	public boolean verificaDisponibilidadePdv(Long idPdv, Long idBox) {
		
		if (idBox == null) {
		    return true;
		}
		
		Box box = this.roteirizacaoRepository.obterBoxDoPDV(idPdv);
		
		if(box == null){
			
			Cota cota  = cotaRepository.obterPorPDV(idPdv);
			
			return (cota.getBox() == null);
		}
		
		return box == null;
	}
		
	/**
     * Inclui Cota Pdv na Roteirização
     * 
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
     * 
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
        
		List<Box> boxDisponiveis = boxService.buscarPorTipo(Arrays.asList(TipoBox.ESPECIAL, TipoBox.LANCAMENTO));
        
		if (roteirizacao != null) {
            
			RoteirizacaoDTO dto = RoteirizacaoDTO.toDTO(roteirizacao, boxDisponiveis, true);
							
			this.carregarRotasEntregadores(dto);
			
            return dto;
        }
        
		return null;
    }

	@Override
	@Transactional
	public void carregarRotasEntregadores(RoteirizacaoDTO roteirizacao) {
		
		for(RoteiroRoteirizacaoDTO roteiro : roteirizacao.getRoteiros()) {
			this.carregarRotasEntregadores(roteiro);
		}
		
	}
	
	@Override
	@Transactional
	public void carregarRotasEntregadores(RoteiroRoteirizacaoDTO roteiroDTO) {
		
		int ordem = roteiroDTO.getMaiorOrdemRota();
		
		List<RotaRoteirizacaoDTO> entregadoresRota = new ArrayList<RotaRoteirizacaoDTO>();
		
		List<Entregador> entregadores = this.entregadorRepository.obterEntregadoresSemRota();
		
		for (Entregador entregador : entregadores) {
		
			String nome = entregador.getPessoa().getNome();
										
			ordem++;
				
			RotaRoteirizacaoDTO rotaDTO = new RotaRoteirizacaoDTO(null, ordem, nome, entregador.getId());
										
			entregadoresRota.add(rotaDTO);
			
		}
		
		roteiroDTO.addAllRota(entregadoresRota);
		entregadoresRota.clear();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional
    public Roteirizacao confirmarRoteirizacao(RoteirizacaoDTO roteirizacaoDTO) {
	    
		ValidacaoVO validacao = validarRoteirizacao(roteirizacaoDTO);
	   
	    if (!TipoMensagem.SUCCESS.equals(validacao.getTipoMensagem())) {
	        throw new ValidacaoException(validacao);  
	    }
	    
	    Roteirizacao roteirizacao;
	    
	    if (roteirizacaoDTO.isNovo()) {
	    
	    	roteirizacao = salvarNovaRoteirizacao(roteirizacaoDTO);
	    
	    } else {
	        
	    	roteirizacao = atualizarRoteirizacaoExistente(roteirizacaoDTO);
	    }
	    
	    processarTransferenciaRoteiros(roteirizacaoDTO);
	    
	    processarTransferenciaRotas(roteirizacaoDTO.getRoteirosNovasRotasTransferidas());
	    
	    processarTranfererenciaPDVs(roteirizacaoDTO.getRotasNovosPDVsTransferidos());
	    
	    return roteirizacao;
    }


    /**
     * atualiza os pdvs transferidos para uma rota pertencente a outra
     * roteirização
     * 
     * @param rotasNovosPDVsTransferidos
     */
    private void processarTranfererenciaPDVs(List<RotaRoteirizacaoDTO> rotasNovosPDVsTransferidos) {
    	
    	for(RotaRoteirizacaoDTO rotaDTO : rotasNovosPDVsTransferidos) {
    		
    		Rota rota = this.rotaRepository.buscarPorId(rotaDTO.getId());
    		
    		Box box = this.boxRepository.obterBoxPorRota(rota.getId());
    		
    		List<PdvRoteirizacaoDTO> pdvsExistentes = PdvRoteirizacaoDTO.getDTOFrom(rota.getRotaPDVs());
    		
    		OrdenacaoUtil.reordenarListas(pdvsExistentes,rotaDTO.getPdvs());
    		
    		for(PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
    			
                novoPDVRota(rota, pdvDTO, box);
                atribuirBoxCota(pdvDTO, box);
    		}
    		
    		this.rotaRepository.alterar(rota);
    	}
	}
    
    /**
     * atualiza as rotas transferidas para um roteiro pertencente a outra
     * roteirização
     * 
     * @param roteirosNovasRotasTransferidas
     */
    private void processarTransferenciaRotas(List<RoteiroRoteirizacaoDTO> roteirosNovasRotasTransferidas) {
    	
    	for (RoteiroRoteirizacaoDTO roteiroDTO : roteirosNovasRotasTransferidas) {

    		Roteiro roteiro = this.roteiroRepository.buscarPorId(roteiroDTO.getId());
    		
    		Box box = this.boxRepository.obterBoxPorRoteiro(roteiroDTO.getId());
    		
    		for(RotaRoteirizacaoDTO rotaDTO : roteiroDTO.getRotas()) {
    			
    			Rota rota;
                
				if (rotaDTO.isNovo()) {
					
					if(isRotaEntregadorSemPDVs(rotaDTO))
						continue;
					
					rota = novaRotaRoteiro(roteiro, rotaDTO);
					
					if (rotaDTO.isEntregador() && rotaDTO.hasPDVsAssociados()) {
					
						Entregador entregador = this.entregadorRepository.buscarPorId(rotaDTO.getEntregadorId());

						rota.setEntregador(entregador);

						entregador.setRota(rota);
						this.entregadorRepository.merge(entregador);
					}
					
				} else {
                
					rota = this.rotaRepository.buscarPorId(rotaDTO.getId());
                    
					rota.setRoteiro(roteiro);
					
					if (rota != null) {
						
						rota.desassociarPDVs(rotaDTO.getPdvsExclusao());
					}
                }
                
				for (PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
                    
                	RotaPDV rotaPDVExistente = rota.getRotaPDVPorPDV(pdvDTO.getId());
                    
                	if (rotaPDVExistente == null) {
                        novoPDVRota(rota, pdvDTO, box);
                       
                    } else {
                        rota.alterarOrdemPdv(pdvDTO.getId(), pdvDTO.getOrdem());
                    }
                	
                	atribuirBoxCota(pdvDTO, box);
                	this.rotaRepository.alterar(rota);
                }
            }
    	}
    }
    
	/**
     * Salva as informações de uma nova roteirização
     * 
     * @param roteirizacaoDTO dto com as informações da nova roteirização
     * @return Roteirizacao criada com as informações do DTO
     */
	private Roteirizacao salvarNovaRoteirizacao(RoteirizacaoDTO roteirizacaoDTO) {
       
		Roteirizacao roteirizacao = new Roteirizacao();
        
		TipoRoteiro tipoRoteiro = roteirizacaoDTO.isBoxEspecial() ? TipoRoteiro.ESPECIAL : TipoRoteiro.NORMAL;
        
		associarBoxRoteirizacao(roteirizacaoDTO, roteirizacao); 
        
		for (RoteiroRoteirizacaoDTO roteiroDTO : roteirizacaoDTO.getTodosRoteiros()) {
			
			Roteiro roteiro = novoRoteiroRoteirizacao(roteirizacao, tipoRoteiro, roteiroDTO);
            
			for (RotaRoteirizacaoDTO rotaDTO : roteiroDTO.getTodasRotas()) {
				
				if (isRotaEntregadorSemPDVs(rotaDTO))	
					continue;
				
				Rota rota = novaRotaRoteiro(roteiro, rotaDTO);
				
				if (rotaDTO.isEntregador() && rotaDTO.hasPDVsAssociados()) {
					
					Entregador entregador = this.entregadorRepository.buscarPorId(rotaDTO.getEntregadorId());

					rota.setEntregador(entregador);

					entregador.setRota(rota);
					this.entregadorRepository.merge(entregador);
				}
                
				for (PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
                 
					novoPDVRota(rota, pdvDTO, roteirizacao.getBox());
                    
					atribuirBoxCota(pdvDTO, roteirizacao.getBox());
                } 
            }
        }
		
        roteirizacaoRepository.adicionar(roteirizacao);
        
        return roteirizacao;
    }
	
	    /**
     * Valida se a Rota é um entregador sem PDVs
     * 
     * @param rotaDTO
     * @return
     */
	private boolean isRotaEntregadorSemPDVs(RotaRoteirizacaoDTO rotaDTO) {
		return (rotaDTO.isEntregador() && !rotaDTO.hasPDVsAssociados());
	}

	    /**
     * Atribui o box da roteirização para cota para facilitar as consultas
     * 
     * @param pdvDTO
     * @param box
     */
	private void atribuirBoxCota(PdvRoteirizacaoDTO pdvDTO, Box box) {
		
		if (box != null) {
			
			 PDV pdv = pdvRepository.buscarPorId(pdvDTO.getId());
			 
			 Cota cota  = pdv.getCota();
			 
			 if(pdv.getCaracteristicas() != null && pdv.getCaracteristicas().isPontoPrincipal() && !TipoBox.ESPECIAL.equals(box.getTipoBox())) {
				 cota.setBox(box);
				 cotaRepository.merge(cota);
			 }
		}
	}

	/**
     * Atualiza as informações de uma roteirização existente
     * 
     * @param roteirizacaoDTO dto com as informações da roteirização existente
     * @return Roteirizacao alterada com as informações do DTO
     */
	private Roteirizacao atualizarRoteirizacaoExistente(RoteirizacaoDTO roteirizacaoDTO) {
        
		Roteirizacao roteirizacaoExistente = roteirizacaoRepository.buscarPorId(roteirizacaoDTO.getId());
        		
		TipoRoteiro tipoRoteiro = roteirizacaoDTO.isBoxEspecial() ? TipoRoteiro.ESPECIAL : TipoRoteiro.NORMAL;
        
		Set<Long> roteirosExclusao = roteirizacaoDTO.getRoteirosExclusao();
        
		processarRoteirosExcluidos(roteirizacaoExistente, roteirosExclusao);
       		
		for (RoteiroRoteirizacaoDTO roteiroDTO : roteirizacaoDTO.getTodosRoteiros()) {
        
			Roteiro roteiro;
            
			if (roteiroDTO.isNovo()) {
            
				roteiro = novoRoteiroRoteirizacao(roteirizacaoExistente, tipoRoteiro, roteiroDTO);
            
			} else {
            
				roteiro = this.roteiroRepository.buscarPorId(roteiroDTO.getId());
				roteiro.setRoteirizacao(roteirizacaoExistente);
				roteiro.setOrdem(roteiroDTO.getOrdem());
				
				for(Long idRotaDTO : roteiroDTO.getRotasExclusao()){
					
					Rota rota = this.rotaRepository.buscarPorId(idRotaDTO);
				            	
					Entregador entregador = rota.getEntregador();
					
					if (entregador != null) {
						entregador.setRota(null);
						this.entregadorRepository.merge(entregador);
					}
					

					List listaPdvs = this.pdvRepository.obterPDVPorRota(idRotaDTO);
					
					Set <Long> setPdvsID = new HashSet<Long>();
					
					for(int i=0; i<listaPdvs.size(); i++) {
						setPdvsID.add(((RotaPDV)listaPdvs.get(i)).getPdv().getId());
					}
				    
					rota.desassociarPDVs(setPdvsID);
						
					if(!rota.getRoteiro().getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
					  desassociarBoxCota(setPdvsID);
					}
				}
				
				roteiro.desassociarRotas(roteiroDTO.getRotasExclusao());
            }
            
			for (RotaRoteirizacaoDTO rotaDTO : roteiroDTO.getTodasRotas()) {
                
				Rota rota;
                
				if (rotaDTO.isNovo()) {
					
					if(isRotaEntregadorSemPDVs(rotaDTO))
						continue;
					
					rota = novaRotaRoteiro(roteiro, rotaDTO);
					
					if (rotaDTO.isEntregador() && rotaDTO.hasPDVsAssociados()) {
					
						Entregador entregador = this.entregadorRepository.buscarPorId(rotaDTO.getEntregadorId());

						entregador.setRota(rota);
						
						this.entregadorRepository.merge(entregador);
						
						rota.setEntregador(entregador);
					}
					
				} else {
                
					rota = this.rotaRepository.buscarPorId(rotaDTO.getId());
                    
					if (rota != null) {
						
						rota.setRoteiro(roteiro);
						rota.setOrdem(rotaDTO.getOrdem());
						
						
						if (rotaDTO.getPdvsExclusao() != null && !rotaDTO.getPdvsExclusao().isEmpty()) {
							
							rota.desassociarPDVs(rotaDTO.getPdvsExclusao());
							
							if(!rota.getRoteiro().getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
								desassociarBoxCota(rotaDTO.getPdvsExclusao());
							}
							for (Long pdv : rotaDTO.getPdvsExclusao()) {
								this.rotaRepository.removerPDV(pdv, rotaDTO.getId());
								this.rotaRepository.flush();
							}
						}
					}
                }
                
				for (PdvRoteirizacaoDTO pdvDTO : rotaDTO.getPdvs()) {
                    
                	RotaPDV rotaPDVExistente = rota.getRotaPDVPorPDV(pdvDTO.getId());
                    
                	Box box = this.boxRepository.buscarPorId(roteirizacaoDTO.getBox().getId());
                	
                	if (rotaPDVExistente == null) {
                        novoPDVRota(rota, pdvDTO, box);
                    } else {
                    	
                        rota.alterarOrdemPdv(pdvDTO.getId(), pdvDTO.getOrdem());
                    }
                	
                	atribuirBoxCota(pdvDTO, box);
                }
            }
        }
		
		roteirizacaoRepository.alterar(roteirizacaoExistente);
		
		//if(roteirizacaoDTO.getRoteiros() == null || roteirizacaoDTO.getRoteiros().isEmpty()) {
			
			//roteirizacaoRepository.removerPorId(roteirizacaoDTO.getId());
		//}
		
		return roteirizacaoExistente;
    }
	
	private void desassociarBoxCota(Collection<Long> pdvsExclusao) {
		
		for (Long idPDV : pdvsExclusao) {

			Cota cota = this.pdvRepository.buscarPorId(idPDV).getCota();
			 
			if(cota == null || cota.getId() == null) {
			 cota = this.cotaRepository.obterPorPDV(idPDV);
			}
			 
			cota.setBox(null);
			
			this.cotaRepository.merge(cota);
		}
	}

	private void processarRoteirosExcluidos(Roteirizacao roteirizacaoExistente,
			Set<Long> roteirosExclusao) {
		
		roteirizacaoExistente.desassociarRoteiros(roteirosExclusao);
		
		for (Long idRoteiroExclusao : roteirosExclusao) {
			
			Roteiro roteiro = this.roteiroRepository.buscarPorId(idRoteiroExclusao);
			
			if (roteiro != null) {
			
				for(Rota rota : roteiro.getRotas()) {
					
					Entregador entregador = rota.getEntregador();
					
					rota.setRoteiro(null);
					
					this.rotaRepository.merge(rota);
				
					if(entregador != null){
						
						entregador.setRota(null);
					
						this.entregadorRepository.merge(entregador);
					}
					
					for(RotaPDV rotaPDV : rota.getRotaPDVs()){
						
						PDV pdv = rotaPDV.getPdv();
						
						if(pdv != null){
							
							Cota cota = pdv.getCota();
							
							final boolean tipoDeRoteiroNaoEspecial = !rotaPDV.isTipoRoteiroEspecial();
							
							if(cota != null && tipoDeRoteiroNaoEspecial) {
								
								cota.setBox(null);
								
								this.cotaRepository.merge(cota);
							}
						}
					}
				}
			}
			
			this.roteiroRepository.removerPorId(idRoteiroExclusao);
		}
		
	}

	    /**
     * Processa as transferências de roteiro da roteirização
     * 
     * @param roteirizacaoDTO dto com as informações de transferência de roteiro
     */
    private void processarTransferenciaRoteiros(RoteirizacaoDTO roteirizacaoDTO) {
        
    	Map<Long, Set<RoteiroRoteirizacaoDTO>> mapRoteirosTransferidos = roteirizacaoDTO.getRoteirosTransferidos();
        
    	for (Entry<Long, Set<RoteiroRoteirizacaoDTO>> entry : mapRoteirosTransferidos.entrySet()) {
           
    		Long idBox = entry.getKey();
            
    		Box box = boxRepository.buscarPorId(idBox);
            
    		Set<RoteiroRoteirizacaoDTO> roteirosTransferidosDTO = entry.getValue();
            
    		RoteirizacaoDTO roteirizacaoDTOTransferencia = null;
            
    		Roteirizacao roteirizacaoExistente = roteirizacaoRepository.obterRoteirizacaoPorBox(idBox);
            
            if (roteirizacaoExistente != null) {
                roteirizacaoDTOTransferencia = RoteirizacaoDTO.toDTO(roteirizacaoExistente, Arrays.asList(box), false);
            } else {
                BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
                roteirizacaoDTOTransferencia = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
                roteirizacaoDTOTransferencia.setBox(boxDTO);
            }
            
            for (RoteiroRoteirizacaoDTO roteiro : roteirosTransferidosDTO) {
               
            	RoteiroRoteirizacaoDTO roteiroTransferido = new RoteiroRoteirizacaoDTO(roteiro.getId(), roteiro.getOrdem(), roteiro.getNome());
              
            
                for (RotaRoteirizacaoDTO rota : roteiro.getTodasRotas()) {
                   
                	RotaRoteirizacaoDTO rotaTransferida = new RotaRoteirizacaoDTO(rota.getId(), rota.getOrdem(), rota.getNome());
                    rotaTransferida.addAllPdv(rota.getPdvs());
                    rotaTransferida.setEntregadorId(rota.getEntregadorId());
                    roteiroTransferido.addRota(rotaTransferida);
                }
                
                roteirizacaoDTOTransferencia.addRoteiroAposMaiorOrdem(roteiroTransferido);
            	
            }
            
            if (roteirizacaoDTOTransferencia.isNovo()) {
            	Roteirizacao roteirizacao = new Roteirizacao();
                
        		associarBoxRoteirizacao(roteirizacaoDTOTransferencia, roteirizacao);
        		
        		Long idRoteirizacao = this.roteirizacaoRepository.adicionar(roteirizacao);
        		
        		roteirizacaoDTOTransferencia.setId(idRoteirizacao);
            } 
            
            atualizarRoteirizacaoExistente(roteirizacaoDTOTransferencia);
            
        }
    }
 
    /**
     * Associa o box a uma nova roteirização
     * 
     * @param roteirizacaoDTO dto com as informações da roteirização
     * @param novaRoteirizacao Roteirizacao para associação do Box
     */
    private void associarBoxRoteirizacao(RoteirizacaoDTO roteirizacaoDTO, Roteirizacao novaRoteirizacao) {
       
    	if (!roteirizacaoDTO.isNovo()) {
            throw new IllegalArgumentException("Associação de Box permitida apenas para uma nova roteirização!");
        }
        
		Box box = boxRepository.buscarPorId(roteirizacaoDTO.getBox().getId());
        
		validarAssociacaoBoxRoteirizacao(box);
        
		novaRoteirizacao.setBox(box);
        
    }

	    /**
     * Valida se o box informado já possui uma roteiriação associada
     * 
     * @param box
     */
	private void validarAssociacaoBoxRoteirizacao(Box box) {
		Roteirizacao existente = roteirizacaoRepository.obterRoteirizacaoPorBox(box.getId());
		
		if (existente != null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Box já está associado a uma Roteirização!");
		}
	}
   
    /**
     * Adiciona um novo PDV à Rota
     * 
     * @param rota Rota para associação
     * @param pdvDTO PDV para associação
     * @param box Box ao qual a roteirização está associada
     **/
    private void novoPDVRota(Rota rota, PdvRoteirizacaoDTO pdvDTO, Box box) {

        PDV pdv = pdvRepository.buscarPorId(pdvDTO.getId());
      
        rota.addPDV(pdv, pdvDTO.getOrdem(), box);
    }
    
    /**
     * Adiciona uma nova Rota à Roteirização
     * 
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
     * 
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
    public ValidacaoVO validarRoteirizacao(RoteirizacaoDTO roteirizacaoDTO) {
        
    	List<String> erros = new ArrayList<String>();
        
    	if (roteirizacaoDTO.getBox() == null) {
            erros.add("É necessário selecionar um Box para Roteirização!");
        
    	}        
        if (erros.isEmpty()) {
            return new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização válida!");
        } else {
            return new ValidacaoVO(TipoMensagem.ERROR, erros);
        }
    }

	@Override
	@Transactional
	public List<RotaRoteirizacaoDTO> obterRotasNaoAssociadasAoRoteiro(Long roteiroID) {

		List<Rota> rotas = this.rotaRepository.obterRotasNaoAssociadasAoRoteiro(roteiroID);
		
		List<RotaRoteirizacaoDTO> rotasDTO = RotaRoteirizacaoDTO.getDTOFrom(rotas);
		
		return rotasDTO;
	}

	/**
	 * @see br.com.abril.nds.service.RoteirizacaoService#obterRoteiroPorRota(java.lang.Long)
	 */
	@Override
	@Transactional
	public Roteiro obterRoteiroPorRota(Long rotaID) {
		
		return this.roteiroRepository.obterRoteiroPorRota(rotaID);
		
	}

	@Override
	@Transactional(readOnly=true)
	public void validarAssociacaoRotaTransportador(Long rotaId, Long roteiroId) {
		
		if(rotaId == null || rotaId < 1) 
			return;

		Rota rota = this.buscarRotaPorId(rotaId);
		
		if (rota.getAssociacoesVeiculoMotoristaRota() != null && !rota.getAssociacoesVeiculoMotoristaRota().isEmpty() ) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não é possível modificar esta rota, pois ele esta associada a um transportador."));
		}

	}

	@Override
	@Transactional(readOnly=true)
	public void validarAssociacaoRoteiroTransportador(Long roteiroId) {
		
		if (roteiroId == null || roteiroId < 1) return;
		
		Roteiro roteiro = this.buscarRoteiroPorId(roteiroId);
		
		for (Rota rota : roteiro.getRotas()) {
			
			try {
			
				this.validarAssociacaoRotaTransportador(rota.getId(), roteiroId);
			
			}catch(ValidacaoException ve) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não é possível modificar este roteiro, pois ele esta associado a um transportador. "));
			}
		}
		
	}

	@Override
	@Transactional
	public List<RoteiroRoteirizacaoDTO> buscarRoteirosNaoAssociadosAoBox(Long idBox) {
		
		List<Roteiro> roteiros = this.roteiroRepository.obterRoteirosNaoAssociadosAoBox(idBox);
		
		List<RoteiroRoteirizacaoDTO> roteirosDTO = RoteiroRoteirizacaoDTO.getDTOFrom(roteiros);
		
		return roteirosDTO;
	}

	/**
	 * Obtem combo com todos os Roteiros
	 * @return List<ItemDTO<Long, String>>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboTodosRoteiros() {
		
        List<Roteiro> roteiros = this.buscarRoteiro(null, null);
		
		List<ItemDTO<Long, String>> listRoteiro = new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros) {
			
			listRoteiro.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		return listRoteiro;
	}
	
	/**
	 * Obtem combo com todas as Rotas
	 * @return List<ItemDTO<Long, String>>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboTodosRotas() {
		
        List<Rota> rotas = this.buscarRota(null, null);
		
		List<ItemDTO<Long, String>> listRota = new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas) {
			
			listRota.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		return listRota;
	}
    
    /**
     * Obtem combo com todos os Boxes
     * @return List<ItemDTO<Long, String>>
     */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboTodosBoxes() {
		
        List<Box> boxs = this.boxService.buscarPorTipo(Arrays.asList(TipoBox.ESPECIAL, TipoBox.LANCAMENTO));
		
		List<ItemDTO<Long, String>> listaBox = new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : boxs) {
			
			listaBox.add(new ItemDTO<Long, String>(box.getCodigo().longValue(), box.getCodigo().toString()+"-"+box.getNome()));
		}
		
		return listaBox;
	}
	
    /**
	 * Carrega o combo Box por Rota
	 * @param idRota
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboBoxPorRota(Long idRota) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if (idRota != null && idRota > 0) {
		
			List<Box> boxes = this.boxService.buscarBoxPorRota(idRota);
	
			for (Box box : boxes) {
	    		
	    		lista.add(new ItemDTO<Long, String>(box.getCodigo().longValue(), box.getCodigo().toString()+"-"+box.getNome()));
	    	}
		} else {
		    
			lista = this.getComboTodosBoxes();
		}
		
		return lista;
	}
	
	/**
	 * Carrega o combo Box por Roteiro
	 * @param idRoteiro
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboBoxPorRoteiro(Long idRoteiro) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if (idRoteiro != null && idRoteiro > 0) {
			
			List<Box> boxes = this.boxService.buscarBoxPorRoteiro(idRoteiro);
	
	    	for (Box box : boxes) {
	    		
	    		lista.add(new ItemDTO<Long, String>(box.getCodigo().longValue(), box.getCodigo().toString()+"-"+box.getNome()));
	    	}
		} else {
			
			lista = this.getComboTodosBoxes();
		}
		
		return lista;
	}
	
	/**
	 * Carrega o combo Rota por intervalo de Box
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboRotaPorBox(Integer codigoBoxDe, Integer codigoBoxAte) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if ((codigoBoxDe != null && codigoBoxDe > 0)||(codigoBoxAte != null && codigoBoxAte > 0)){
		
			List<Box> boxes = this.boxService.obterBoxPorIntervaloCodigo(codigoBoxDe, codigoBoxAte);
			
			for (Box box : boxes) {
			
		        List<Rota> rotas = this.buscarRotaDeBox(box.getId());
				
				for (Rota rota : rotas) {
					
					lista.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
				}
			}
		} else {
			
			lista = this.getComboTodosRotas();
		}
		
		return lista;
	}
	
	/**
	 * Carrega o combo Rota por Roteiro
	 * @param idRoteiro
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboRotaPorRoteiro(Long idRoteiro) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if(idRoteiro != null && idRoteiro > 0) {
		
			List<Rota> rotas = this.rotaService.buscarRotaPorRoteiro(idRoteiro);

			for (Rota rota : rotas) {
				
				lista.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
			}
		} else {
			
			lista = this.getComboTodosRotas();
		}
		return lista;
	}
	
	/**
	 * Carrega o combo Roteiro por Rota
	 * @param idRota
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboRoteiroPorRota(Long idRota) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if (idRota != null && idRota > 0) {
		
			Rota rota = this.buscarRotaPorId(idRota);
		
			Roteiro roteiro = rota.getRoteiro();
			
			lista.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		} else {
			
			lista = this.getComboTodosRoteiros();
		}

		return lista;
	}
	
	/**
	 * Carrega o combo Roteiro por intervalo de Box
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO<Long, String>> getComboRoteiroPorBox(Integer codigoBoxDe, Integer codigoBoxAte) {
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		if ((codigoBoxDe != null )||(codigoBoxAte != null )){
		
			List<Box> boxes = this.boxService.obterBoxPorIntervaloCodigo(codigoBoxDe, codigoBoxAte);
				
			for (Box box : boxes) {
			
				List<Roteiro> roteiros = this.buscarRoteiroDeBox(box.getId());
				
				for (Roteiro roteiro : roteiros) {
				
					lista.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
				}
			}
		} else {
			
			lista = this.getComboTodosRoteiros();
		}

		return lista;
	}

	@Override
	@Transactional
	public List<Roteiro> buscarRoteiroCodigoBox(Long codigoBoxDe, Long codigoBoxAte) {
		
        if (codigoBoxDe == null && codigoBoxAte == null)
            return new ArrayList<Roteiro>();
		
		return roteiroRepository.buscarRoteiroCodigoBox(codigoBoxDe, codigoBoxAte);
		
	}

	@Override
	@Transactional
	public List<MapaRoteirizacaoDTO> obterDetalheRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro) {
		
		List<MapaRoteirizacaoDTO> roteirizacoes = roteirizacaoRepository.sumarizadoRelPorCota(filtro);
		
		for (MapaRoteirizacaoDTO mapa : roteirizacoes) {
			filtro.setIdBox(mapa.getIdBox());
			filtro.setIdRota(mapa.getIdRota());
			filtro.setIdRoteirizacao(mapa.getIdRoteirizacao());
			
			mapa.setItens(roteirizacaoRepository.obterDetalheRoteiricao(filtro));
		}
		
		
		return roteirizacoes;
	}
	
	@Transactional
	public void roteiroTest() {
		
		Roteiro roteiro = this.roteiroRepository.buscarPorId(16L);
		Roteiro roteiroEspecial = popularRoteiroEspecial(roteiro);
		System.out.println(roteiroEspecial.toString());
	}

	private Roteiro popularRoteiroEspecial(Roteiro roteiro) {
		
		Box box = boxService.buscarPorId(179L);
		
		List<Rota> rotasEpeciais = new ArrayList<Rota>();
		
		Roteiro roteiroEspecial = this.roteiroRepository.buscarPorId(50L);
		
		for (Rota rota :  roteiro.getRotas()) {
			
			Rota rotaEspecial = new Rota();
			
			rotaEspecial.setDescricaoRota(rota.getDescricaoRota());
			rotaEspecial.setOrdem(rota.getOrdem());
			rotaEspecial.setEntregador(rota.getEntregador());
			rotaEspecial.setRoteiro(roteiroEspecial);
			rotasEpeciais.add(rotaEspecial);
			
			for(RotaPDV rotaPdv : rota.getRotaPDVs()) {
				novoPDVRotaEspecial(rotaEspecial, rota, rotaPdv, box);
			}
			
			rotaRepository.adicionar(rotaEspecial);
			
		}
		
		roteiroEspecial.setRotas(rotasEpeciais);
		
		return roteiroEspecial;
	}

	private void novoPDVRotaEspecial(Rota rotaEspecial, Rota rota, RotaPDV rotaPdv, Box box) {
      
		rotaEspecial.addPDV(rotaPdv.getPdv(), rotaPdv.getOrdem(), box);
    }
	
	public byte[] gerarRelatorio(List<MapaRoteirizacaoDTO> lista, String tipo) {
		
		InputStream logoDistribuidor = this.parametrosDistribuidorService.getLogotipoDistribuidor();
		
		String razaoSocial = this.distribuidorService.obterRazaoSocialDistribuidor();
		
		if(tipo.equals("PDF")) {
			return this.gerarDocumentoIreport(lista, obterDiretorioReports(), logoDistribuidor, razaoSocial);			
		} else { 
			return this.exportReportToRtf(lista, obterDiretorioReports(), logoDistribuidor, razaoSocial);
		}	
		
	}
	
	public byte[] gerarDocumentoIreport(List<MapaRoteirizacaoDTO> list, URL diretorioReports, InputStream logoTipoDistribuidor, String razaoSocial) {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		String path;
		try {
			path = diretorioReports.toURI().getPath() + "/rel_roteirizacao_principal.jasper";
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
			parameters.put("IMAGEM", logoTipoDistribuidor);
			parameters.put("NOME_RELATORIO", "Extração das Roteirizações!");
			parameters.put("NOME_DISTRIBUIDOR", razaoSocial);
			
			return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
		} catch (JRException jre) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ jre.getMessage());
		} catch (URISyntaxException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ e.getMessage());
		}
	}
	
	public byte[] exportReportToRtf(List<MapaRoteirizacaoDTO> list, URL diretorioReports, InputStream logoTipoDistribuidor, String razaoSocial) {
		
		String path;
		
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		try {			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
			
			parameters.put("IMAGEM", logoTipoDistribuidor);
			parameters.put("NOME_RELATORIO", "Extração das Roteirizações!");
			parameters.put("NOME_DISTRIBUIDOR", razaoSocial);
			
			path = diretorioReports.toURI().getPath() + "/rel_roteirizacao_principal.jasper";			

			JasperPrint jasperPrint = JasperFillManager.fillReport(path, parameters, jrDataSource);
			
			JRXlsExporter exporter = new JRXlsExporter();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			
			exporter.exportReport(); 
			
			return baos.toByteArray();
			
		} catch (JRException jre) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ jre.getMessage());	
		} catch (URISyntaxException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ e.getMessage());
		}		
	}
	
	protected URL obterDiretorioReports() {
		
		URL urlRoteirizacao = Thread.currentThread().getContextClassLoader().getResource("reports");
		
		return urlRoteirizacao;
	}	
}