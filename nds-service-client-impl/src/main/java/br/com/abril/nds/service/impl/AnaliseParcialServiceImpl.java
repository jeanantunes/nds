package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import br.com.abril.nds.dto.*;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoPDV;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstudoPDVRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.AnaliseParcialService;

@Service
public class AnaliseParcialServiceImpl implements AnaliseParcialService {

    @Autowired
    private EstudoPDVRepository estudoPDVRepository;

    @Autowired
    private AnaliseParcialRepository analiseParcialRepository;

    @Autowired
    private EstudoRepository estudoRepository;

    @Autowired
    private CotaRepository cotaRepository;

    private static final Logger log = LoggerFactory.getLogger(AnaliseParcialServiceImpl.class);
    private Map<String, String> mapClassificacaoCota;

    @Override
    @Transactional
    public EstudoCota buscarPorId(Long id) {
        EstudoCota estudo = new EstudoCota();
        estudo.setEstudo(estudoRepository.buscarPorId(id));
        return estudo;
    }

    @Override
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId) {
        return analiseParcialRepository.carregarEdicoesBaseEstudo(estudoId, true);
    }

    @Override
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
        List<AnaliseParcialDTO> lista = analiseParcialRepository.buscaAnaliseParcialPorEstudo(queryDTO);
        if (queryDTO.getModoAnalise() != null && queryDTO.getModoAnalise().equalsIgnoreCase("PARCIAL")) {
            for (AnaliseParcialDTO item : lista) {
                item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                List<EdicoesProdutosDTO> temp = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    temp.addAll(analiseParcialRepository.getEdicoesBaseParciais((long) item.getCota(), queryDTO.getNumeroEdicao(), queryDTO.getCodigoProduto(), (long) i));
                }
                item.setEdicoesBase(temp);
            }
        } else {
            if (queryDTO.getEdicoesBase() == null) {
                queryDTO.setEdicoesBase(analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoId(), true));
            }
            for (AnaliseParcialDTO item : lista) {
                item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                List<Long> idsProdutoEdicao = new LinkedList<>();
                List<EdicoesProdutosDTO> edicoesComVenda = new LinkedList<>();
                for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                    idsProdutoEdicao.add(edicao.getProdutoEdicaoId());
                }
                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>());
                if(idsProdutoEdicao.size() > 0){
                	edicoesComVenda.addAll(analiseParcialRepository.getEdicoesBase((long) item.getCota(), idsProdutoEdicao));
                	for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                		for (EdicoesProdutosDTO ed : edicoesComVenda) {
                			if (ed.getProdutoEdicaoId().equals(edicao.getProdutoEdicaoId())) {
                				BeanUtils.copyProperties(edicao, ed, new String[] {"reparte", "venda"});
                				item.getEdicoesBase().add(ed);
                			}
                		}
                	}                	
                }
            }
        }
        return lista;
    }

    @Override
    public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte) {
        analiseParcialRepository.atualizaReparteCota(estudoId, numeroCota, reparte);
        analiseParcialRepository.atualizaReparteEstudo(estudoId, reparte);
    }

    @Override
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota) {
        return analiseParcialRepository.carregarDetalhesPdv(numeroCota);
    }

    @Override
    public void liberar(Long id) {
        analiseParcialRepository.liberar(id);
    }

    @Override
    public List<AnaliseEstudoDetalhesDTO> buscarDetalhesAnalise(ProdutoEdicao produtoEdicao) {
        return analiseParcialRepository.buscarDetalhesAnalise(produtoEdicao);
    }

    @Override
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        List<CotaQueNaoEntrouNoEstudoDTO> cotaQueNaoEntrouNoEstudoDTOList = analiseParcialRepository.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        for (CotaQueNaoEntrouNoEstudoDTO cotaQueNaoEntrouNoEstudoDTO : cotaQueNaoEntrouNoEstudoDTOList) {
            cotaQueNaoEntrouNoEstudoDTO.setDescricaoMotivo(traduzClassificacaoCota(cotaQueNaoEntrouNoEstudoDTO.getMotivo()));
        }
        return cotaQueNaoEntrouNoEstudoDTOList;
    }

    @Override
    public BigDecimal calcularPercentualAbrangencia(Long estudoId) {
        int cotasAtivas = cotaRepository.obterCotasAtivas();
        int cotasComReparte = estudoRepository.obterCotasComRepartePorIdEstudo(estudoId);
        return cotasAtivas == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(cotasComReparte)
                .divide(BigDecimal.valueOf(cotasAtivas)).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap) {

        Estudo estudo = estudoRepository.buscarPorId(estudoId);
        Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);

        Map<Long, PDV> pdvMap = new HashMap<>();
        for (PDV pdv : cota.getPdvs()) {
            pdvMap.put(pdv.getId(), pdv);
        }

        for (PdvDTO pdvDTO : reparteMap) {
            EstudoPDV estudoPDV = new EstudoPDV();
            estudoPDV.setEstudo(estudo);
            estudoPDV.setCota(cota);
            estudoPDV.setPdv(pdvMap.get(pdvDTO.getId()));
            estudoPDV.setReparte(BigInteger.valueOf(pdvDTO.getReparte()));
            estudoPDVRepository.merge(estudoPDV);
        }
    }

    @Override
    public CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto) {
        return cotaRepository.buscarCotaPorNumero(numeroCota, codigoProduto);
    }

    @Override
    public List<AnaliseEstudoDetalhesDTO> historicoEdicoesBase(Long[] idsProdutoEdicao) {
        List<AnaliseEstudoDetalhesDTO> list = new LinkedList<>();

        for (Long id : idsProdutoEdicao) {
            list.add(analiseParcialRepository.historicoEdicaoBase(id));
        }
        return list;
    }

    private String traduzClassificacaoCota(String motivo) {
        if (mapClassificacaoCota == null) {
            populaMapClassificacaoCota();
        }
        return mapClassificacaoCota.get(motivo);
    }

    private void populaMapClassificacaoCota() {
        mapClassificacaoCota = new HashMap<>();
        for (ClassificacaoCota classificacaoCota : ClassificacaoCota.values()) {
            mapClassificacaoCota.put(classificacaoCota.getCodigo(), classificacaoCota.getTexto());
        }
    }
}
