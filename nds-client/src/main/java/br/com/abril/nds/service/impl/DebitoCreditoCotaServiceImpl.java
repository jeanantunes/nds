package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.util.DateUtil;

@Service
public class DebitoCreditoCotaServiceImpl implements DebitoCreditoCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Transactional
	public MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(
			DebitoCreditoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setIdMovimentoFinanceiroCota(idMovimento);
		
		movimentoFinanceiroCotaDTO.setDataCriacao(DateUtil.removerTimestamp(new Date()));

		Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
		
		movimentoFinanceiroCotaDTO.setDataVencimento(dataVencimento);

		movimentoFinanceiroCotaDTO.setValor(new BigDecimal(debitoCredito.getValor()));

		movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
				this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(debitoCredito.getNumeroCota());
		
		movimentoFinanceiroCotaDTO.setCota(cota);

		Usuario usuario = this.usuarioRepository.buscarPorId(debitoCredito.getIdUsuario());
		
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		
		movimentoFinanceiroCotaDTO.setLancamentoManual(true);

		return movimentoFinanceiroCotaDTO;
	}

	
	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamentos de débitos e/ou créditos
	 * @return List<DebitoCreditoDTO>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota) {
		List<DebitoCreditoDTO> listaDC = new ArrayList<DebitoCreditoDTO>();
		List<Cota> cotas = this.boxRepository.obterCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
		Long indice=0l;
		for (Cota itemCota:cotas){
			indice++;
			listaDC.add(new DebitoCreditoDTO(indice,null,null,itemCota.getNumeroCota(),itemCota.getPessoa().getNome(),null,null,null,null));
		}
		return listaDC;
	}
}
