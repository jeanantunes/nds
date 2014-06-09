package br.com.abril.nds.client.assembler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.abril.nds.dto.chamadaencalhe.ChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ChamadasEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.IdentificacaoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ItemChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ItemResumoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.PessoaJuridicaChamadaEncalheFornecedorDTO;
import br.com.abril.nds.dto.chamadaencalhe.ResumoChamadaEncalheFornecedorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;


/**
 * Classe utilitária para auxílio na criação dos DTO's com informações das
 * chamadas de encalhe do fornecedor
 * 
 * @author francisco.garcia
 * 
 */
public class ChamadaEncalheFornecedorDTOAssembler {
    
    private ChamadaEncalheFornecedorDTOAssembler() {
    }
    
    public static List<ChamadasEncalheFornecedorDTO> criarChamadasEncalheFornecedorDTO(Collection<ChamadaEncalheFornecedor> chamadas,
            Distribuidor distribuidor) {
        PessoaJuridicaChamadaEncalheFornecedorDTO distribuidorDTO = criarDistribuidor(distribuidor);
        Map<Long, ChamadasEncalheFornecedorDTO> mapaChamadasDTO = new HashMap<Long, ChamadasEncalheFornecedorDTO>();

        for (ChamadaEncalheFornecedor cef : chamadas) {
            Fornecedor fornecedor = cef.getFornecedor();
            Long numeroChamadaEncalhe = cef.getNumeroChamadaEncalhe();
            ChamadasEncalheFornecedorDTO chamadasDTO = mapaChamadasDTO.get(numeroChamadaEncalhe);
            if (chamadasDTO == null) {
                chamadasDTO = new ChamadasEncalheFornecedorDTO(criarFornecedor(fornecedor), distribuidorDTO, criarIdentificacao(cef));
                mapaChamadasDTO.put(numeroChamadaEncalhe, chamadasDTO);
            }
            Map<Long, List<ItemChamadaEncalheFornecedor>> mapaItensPorDocumento = separarPorNumeroDocumento(cef.getItens());
            for (Entry<Long, List<ItemChamadaEncalheFornecedor>> documento : mapaItensPorDocumento.entrySet()) {
                BigDecimal totalBrutoDocumento = BigDecimal.ZERO;
                BigDecimal totalDescontoDocumento = BigDecimal.ZERO;
                Long totalVendas = 0L;
                Long totalItensDevolvidos = 0L;
                ChamadaEncalheFornecedorDTO cefDTO = chamadasDTO.newDocumento();
                cefDTO.setNumeroDocumento(documento.getKey());
                for (ItemChamadaEncalheFornecedor icef : documento.getValue()) {
                    processarItem(cefDTO, icef);

                    totalBrutoDocumento = totalBrutoDocumento.add(Util.nvl(icef.getValorVendaApurado(), BigDecimal.ZERO));
                    totalDescontoDocumento = totalDescontoDocumento.add(Util.nvl(icef.getValorMargemApurado(), BigDecimal.ZERO));
                    totalVendas += Util.nvl(icef.getQtdeVendaApurada(), 0L);
                    totalItensDevolvidos += Util.nvl(icef.getQtdeDevolucaoApurada(), 0L);
                    
                }
                cefDTO.setTotalBruto(totalBrutoDocumento);
                cefDTO.setTotalDesconto(totalDescontoDocumento);
                cefDTO.setTotalLiquido(totalBrutoDocumento.subtract(totalDescontoDocumento));
                cefDTO.setTotalQtdeDevolvido(totalItensDevolvidos);
                cefDTO.setTotalQtdeVenda(totalVendas);

                BigDecimal porcentagemDesconto = BigDecimal.ZERO;
                if (BigDecimal.ZERO.compareTo(totalBrutoDocumento) != 0) {
                    porcentagemDesconto = MathUtil.round(MathUtil.divide(totalDescontoDocumento, totalBrutoDocumento).multiply(BigDecimal.valueOf(100)), 1);
                }
                
                cefDTO.setPorcentagemDesconto(porcentagemDesconto);

                BigDecimal margemDistribuidor = fornecedor != null ? fornecedor.getMargemDistribuidor() : BigDecimal.ZERO;
                
                cefDTO.setMargemDistribuidor(margemDistribuidor);

                BigDecimal totalMargemDistribuidor = MathUtil.round(MathUtil.calculatePercentageValue(totalBrutoDocumento, margemDistribuidor), 2);
                cefDTO.setTotalMargemDistribuidor(totalMargemDistribuidor);
  
                processarResumo(chamadasDTO, cefDTO);
            }
        }
        
        List<ChamadasEncalheFornecedorDTO> chamadasEncalhe = new ArrayList<ChamadasEncalheFornecedorDTO>(mapaChamadasDTO.values());
        
        return chamadasEncalhe;
        
    }
    

    /**
     * Processa o item da chamada de encalhe do fornecedor
     * 
     * @param cefDTO
     *            DTO com informações da chamada de encalhe do fornecedor
     * @param icef
     *            item da chamada de encalhe do fornecedor
     */
    private static void processarItem(ChamadaEncalheFornecedorDTO cefDTO, ItemChamadaEncalheFornecedor icef) {
        ItemChamadaEncalheFornecedorDTO icefDTO = new ItemChamadaEncalheFornecedorDTO();

        ProdutoEdicao produtoEdicao = icef.getProdutoEdicao();
        Produto produto = produtoEdicao.getProduto();
        Editor editor = produto.getEditor();
        String formaDevolucao = icef.getFormaDevolucao() == null ? null : icef.getFormaDevolucao().getDescricao();
        String tipoRecolhimento = icef.getRegimeRecolhimento() == null ? null : icef.getRegimeRecolhimento().getCodigo();

        Long codigoEditor = editor == null ? Long.valueOf(0) : editor.getCodigo();                     
        icefDTO.setEditor(codigoEditor);
        icefDTO.setDescricao(produto.getNomeComercial());
        icefDTO.setNome(produto.getNome());
        icefDTO.setCodigo(produto.getCodigo());
        icefDTO.setEdicao(produtoEdicao.getNumeroEdicao());
        icefDTO.setFormaDevolucao(formaDevolucao);
        icefDTO.setItem(icef.getNumeroItem());
        icefDTO.setDataRecolhimento(icef.getDataRecolhimento());
        icefDTO.setNotaEnvio(icef.getNumeroNotaEnvio());
        icefDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
        icefDTO.setPrecoCapa(icef.getPrecoUnitario());
        icefDTO.setQtdeDevolvida(icef.getQtdeDevolucaoApurada());
        icefDTO.setQtdeEnviada(icef.getQtdeEnviada());
        icefDTO.setQtdeVenda(icef.getQtdeVendaApurada());
        icefDTO.setTipoRecolhimento(tipoRecolhimento);
        icefDTO.setValorVenda(icef.getValorVendaApurado());
        icefDTO.setCodigoNotaEnvioMultipla(icef.getCodigoNotaEnvioMultipla());
        cefDTO.addItem(icefDTO);
    }

    /**
     * Processa o resumo das chamdas de encalhe do fornecedor
     * 
     * @param chamadasDTO
     *            DTO com o agrupamento das chamadas de encalhe do fornecedor
     * @param cefDTO
     *            Chamada de encalhe do fornecedor para criação do resumo
     */
    private static void processarResumo(ChamadasEncalheFornecedorDTO chamadasDTO, ChamadaEncalheFornecedorDTO cefDTO) {
        ResumoChamadaEncalheFornecedorDTO resumoDTO = chamadasDTO.getResumo();
        ItemResumoChamadaEncalheFornecedorDTO itemResumoDTO = resumoDTO.newItem();
        itemResumoDTO.setNumeroDocumento(cefDTO.getNumeroDocumento());
        itemResumoDTO.setValorDocumento(cefDTO.getTotalLiquido());
        itemResumoDTO.setValorMargem(cefDTO.getTotalMargemDistribuidor());
        
        resumoDTO.setSubTotalVendas(resumoDTO.getSubTotalVendas().add(cefDTO.getTotalLiquido()));
        resumoDTO.setValorPagar(resumoDTO.getSubTotalVendas().subtract(resumoDTO.getValorProjetosEspeciais()));
        resumoDTO.setTotalMargemDistribuidor(resumoDTO.getTotalMargemDistribuidor().add(cefDTO.getTotalMargemDistribuidor()));
    }
    
    /**
     * Cria o DTO com as informações do distribuidor
     * 
     * @param distribuidor
     *            distribuidor para criação do DTO
     * @return DTO com as informações do distribuidor
     */
    private static PessoaJuridicaChamadaEncalheFornecedorDTO criarDistribuidor(final Distribuidor distribuidor) {
        Endereco endereco = null;
        if (distribuidor.getEnderecoDistribuidor() != null) {
            endereco = distribuidor.getEnderecoDistribuidor().getEndereco();
        }
        PessoaJuridicaChamadaEncalheFornecedorDTO distribuidorDTO = criarPessoaJuridicaChamadaEncalheFornecedor(distribuidor.getId(), 
                distribuidor.getJuridica(), endereco);
        return distribuidorDTO;
    }
    
    /**
     * Cria o DTO com as informações do fornecedor
     * 
     * @param fornecedor
     *            fornecedor para criação do DTO
     * @return DTO com as informações do fornecedor
     */
    private static PessoaJuridicaChamadaEncalheFornecedorDTO criarFornecedor(final Fornecedor fornecedor) {
        
    	if (fornecedor == null) {

    		return null;
    	}
    	
    	Endereco endereco = null;
        if (fornecedor.getEnderecoPrincipal() != null) {
            endereco = fornecedor.getEnderecoPrincipal().getEndereco();
        }
        
        PessoaJuridicaChamadaEncalheFornecedorDTO distribuidorDTO = criarPessoaJuridicaChamadaEncalheFornecedor(fornecedor.getId(), 
                fornecedor.getJuridica(), endereco);
        return distribuidorDTO;
    }
 
    /**
     * Cria o DTO com as informações da pessoa jurídica para a chamda de encalhe
     * do fornecedor
     * 
     * @param id
     *            identificador
     * @param pj
     *            pessoa jurídica para criação do DTO
     * @param endereco
     *            endereço para criação do DTO
     * @return DTO criador com as informações da Pessoa Jurídica
     */
    private static PessoaJuridicaChamadaEncalheFornecedorDTO criarPessoaJuridicaChamadaEncalheFornecedor(Long id, PessoaJuridica pj, Endereco endereco) {
        String logradouro = endereco == null ? "" : String.format("%s %s",
                Util.nvl(endereco.getTipoLogradouro(), ""),
                Util.nvl(endereco.getLogradouro(), "")).trim();
        String numero = endereco == null ? "" : endereco.getNumero();
        String cidade = endereco == null ? "" : endereco.getCidade();
        String uf = endereco == null ? "" : endereco.getUf();
        String cep = endereco == null ? "" : endereco.getCep();
        
        PessoaJuridicaChamadaEncalheFornecedorDTO pjDTO = new PessoaJuridicaChamadaEncalheFornecedorDTO(id, 
                pj.getRazaoSocial(), pj.getCnpj(), pj.getInscricaoEstadual(), logradouro, numero, cidade, uf, cep);
        
        return pjDTO;
    }
    
    /**
     * Cria o DTO com as informações de identificação da chamada de encalhe
     * 
     * @param ceFornecedor
     *            chamada de encalhe do fornecedor
     * @return DTO com as informações de identificação da chamda de encalhe
     */
    private static IdentificacaoChamadaEncalheFornecedorDTO criarIdentificacao(ChamadaEncalheFornecedor ceFornecedor) {
        String codigoCFOP = "";
        String descricaoCFOP = ""; 
        if (ceFornecedor.getCfop() != null) {
            codigoCFOP = ceFornecedor.getCfop().getCodigo();
            descricaoCFOP = ceFornecedor.getCfop().getDescricao();
        }
        
        IdentificacaoChamadaEncalheFornecedorDTO identificacaoDTO = new IdentificacaoChamadaEncalheFornecedorDTO(
                ceFornecedor.getTipoChamadaEncalhe(),
                ceFornecedor.getCodigoDistribuidor(),
                ceFornecedor.getNumeroChamadaEncalhe(),
                ceFornecedor.getControle(), codigoCFOP,
                descricaoCFOP, ceFornecedor.getDataVencimento(),
                ceFornecedor.getDataEmissao(), ceFornecedor.getNumeroSemana(),
                ceFornecedor.getDataLimiteRecebimento());
        return identificacaoDTO;
    }
    
    /**
     * Separa os itens da chamda de encalhe por número de documento
     * 
     * @param itens
     *            itens para separação
     * @return mapa com os itens separados por número de documento
     */
    private static Map<Long, List<ItemChamadaEncalheFornecedor>> separarPorNumeroDocumento(List<ItemChamadaEncalheFornecedor> itens) {
        Map<Long, List<ItemChamadaEncalheFornecedor>> mapa = new TreeMap<Long, List<ItemChamadaEncalheFornecedor>>();
        for (ItemChamadaEncalheFornecedor item : itens) {
            Long key = item.getNumeroDocumento();
            if (mapa.containsKey(key)) {
                mapa.get(key).add(item);
            } else {
                List<ItemChamadaEncalheFornecedor> lista = new ArrayList<ItemChamadaEncalheFornecedor>();
                lista.add(item);
                mapa.put(key, lista);
            }
        }
        return mapa;
    }
    


}
