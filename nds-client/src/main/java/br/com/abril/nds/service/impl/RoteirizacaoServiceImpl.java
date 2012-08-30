package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class RoteirizacaoServiceImpl implements RoteirizacaoService {
	
	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;
	
	@Autowired
	private RoteiroRepository roteiroRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
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
			roteirizacao.setRota(rota);
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
		return  rotaRepository.buscarRotaPorNome(roteiroId, rotaNome, matchMode);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotas() {
		
		return rotaRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaDisponivelRoteirizacaoDTO> buscarRoterizacaoPorRota(Long rotaId) {
	
		List<Roteirizacao> roteirizacaoLista = roteirizacaoRepository.buscarRoterizacaoPorRota(rotaId);
		List<CotaDisponivelRoteirizacaoDTO> lista =
				new ArrayList<CotaDisponivelRoteirizacaoDTO>();
		for (Roteirizacao roteirizacao : roteirizacaoLista ){
			    CotaDisponivelRoteirizacaoDTO  cotaDisponivelRoteirizacaoDTO = new CotaDisponivelRoteirizacaoDTO();
			    PDV  pdv = roteirizacao.getPdv();
			    Cota  cota = pdv.getCota();
			    cotaDisponivelRoteirizacaoDTO.setIdRoteirizacao(roteirizacao.getId());
				cotaDisponivelRoteirizacaoDTO.setNome(cota.getPessoa().getNome());
				cotaDisponivelRoteirizacaoDTO.setNumeroCota(cota.getNumeroCota());
				cotaDisponivelRoteirizacaoDTO.setPontoVenda(pdv.getNome());
				cotaDisponivelRoteirizacaoDTO.setOrigemEndereco("Cota");
				cotaDisponivelRoteirizacaoDTO.setIdPontoVenda(pdv.getId());
				cotaDisponivelRoteirizacaoDTO.setOrdem(roteirizacao.getOrdem());
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
	
	@Transactional
	public void gravaRoteirizacao(List<CotaDisponivelRoteirizacaoDTO> lista,  Long idRota){
		for (CotaDisponivelRoteirizacaoDTO dto : lista ){
			Roteirizacao roteirizacao = new Roteirizacao();
			PDV pdv = new PDV();
			pdv.setId(dto.getIdPontoVenda());
			roteirizacao.setPdv(pdv);
			Rota rota = new Rota();
			rota.setId(idRota);
			roteirizacao.setRota(rota);
			roteirizacao.setOrdem(dto.getOrdem());
			roteirizacaoRepository.adicionar(roteirizacao);
		}
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
	public void transferirRoteirizacao(List<Long> roteirizacaoId, Rota rota) {
		Integer ordem = roteirizacaoRepository.buscarMaiorOrdem(rota.getId());
		if ( ordem == null ){
			ordem = 0;
		}
		for (Long id : roteirizacaoId ){
			Roteirizacao roteirizacao = roteirizacaoRepository.buscarPorId(id);
			roteirizacao.setRota(rota);
			roteirizacao.setOrdem(ordem++);
			roteirizacaoRepository.merge(roteirizacao);
		}	
		
	}
	
	
	@Override
	@Transactional
	public void excluirRoteirizacao(List<Long> roteirizacaoId) {

		for (Long id : roteirizacaoId ){
			roteirizacaoRepository.removerPorId(id);
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
	public List<String> buscarUF() {
		return roteirizacaoRepository.buscarUF();
	}

	@Override
	@Transactional(readOnly=true)
	public List<LogLocalidade> buscarMunicipioPorUf(String uf) {
		return roteirizacaoRepository.buscarMunicipioPorUf(uf);
	}

	@Override
	@Transactional(readOnly=true)
	public List<LogBairro> buscarBairroPorMunicipio(Long municipio, String uf) {
		return roteirizacaoRepository.buscarBairroPorMunicipio(municipio, uf);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroEspecial() {
		return roteiroRepository.buscarRoteiroEspecial();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro){
		
		if(filtro.getIdRota()!= null || filtro.getNumeroCota()!= null){
			
			return roteirizacaoRepository.buscarRoteirizacao(filtro);
		}
		
		return roteirizacaoRepository.buscarRoteirizacaoSumarizadoPorCota(filtro);
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
	public void transferirRoteirizacaoComNovaRota(List<Long> roteirizacaoId, Rota rota) {
		this.incluirRota(rota);
		Integer ordem = 0;
		for (Long id : roteirizacaoId ){
			Roteirizacao roteirizacao = roteirizacaoRepository.buscarPorId(id);
			roteirizacao.setRota(rota);
			roteirizacao.setOrdem(ordem++);
			roteirizacaoRepository.merge(roteirizacao);
		}	
		
	}
	@Override
	@Transactional
	public void atualizaOrdenacao(Roteirizacao roteirizacao ){
	
	}
	@Override
	@Transactional
	public void atualizaOrdenacaoAsc(Roteirizacao roteirizacao){
		roteirizacaoRepository.atualizaOrdenacaoAsc(roteirizacao);
	}
	
	@Override
	@Transactional
	public void atualizaOrdenacaoDesc(Roteirizacao roteirizacao ){
		roteirizacaoRepository.atualizaOrdenacaoDesc(roteirizacao);
	}

	@Override
	public List<BoxRoteirizacaoDTO> obterBoxesPorNome(String nome) {
		return roteirizacaoRepository.obterBoxesPorNome(nome);
	}

	@Override
	public List<RoteiroRoteirizacaoDTO> obterRoteirosPorNomeEBoxes(String nome,
			List<Long> idsBoxes) {
		return roteirizacaoRepository.obterRoteirosPorNomeEBoxes(nome, idsBoxes);
	}

	@Override
	public List<RotaRoteirizacaoDTO> obterRotasPorNomeERoteiros(String nome,
			List<Long> idsRoteiros) {
		return roteirizacaoRepository.obterRotasPorNomeERoteiros(nome, idsRoteiros);
	}


}
