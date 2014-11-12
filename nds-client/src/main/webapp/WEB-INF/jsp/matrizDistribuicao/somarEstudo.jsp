<script>

function analisar() {
	//testa se registro selecionado possui estudo gerado
	if ($('#somarEstudo-estudo').html() == null || $('#somarEstudo-estudo').html() == "") {
		exibirMensagem("WARNING",["Gere o estudo antes de fazer a análise."]);
		return;
	} else {
		// Deve ir direto para EMS 2031
		matrizDistribuicao.redirectToTelaAnalise('#somarEstudoContent','#somarEstudoTelaAnalise', $('#somarEstudo-estudo').html());
	}
}

</script>

  <div id="somarEstudoTelaAnalise" />
  <div id="somarEstudoContent">
  <span id="somarEstudo-statusOperacao" style="display: none;"></span>
	
  <fieldset class="classFieldset">
  <legend>Somar Estudo</legend>
        
        
  <table width="950" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td valign="top">
      <fieldset id="selecionado" style=" width:350px; margin-top:5px; margin-left:20px">
        <legend>Estudo Original</legend>
        <table width="347" border="0" cellspacing="2" cellpadding="2" >
          <tr>
            <td><strong>Estudo:</strong></td>
            <td><span id="somarEstudo-estudo"></span></td>
          </tr>
          <tr>
            <td><strong>Código:</strong></td>
            <td><span id="somarEstudo-codigoProduto"></span></td>
          </tr>
          <tr>
            <td><strong>Produto:</strong></td>
            <td><span id="somarEstudo-nomeProduto"></span></td>
          </tr>
          <tr>
          	<td><strong>Edição:</strong></td>
            <td><span id="somarEstudo-edicao"></span></td>
          </tr>
          <tr>
            <td><strong>Classificação:</strong></td>
            <td><span id="somarEstudo-classificacao"></span></td>
          </tr>
          <tr>
            <td><strong>Data Lançamento:</strong></td>
            <td><span id="somarEstudo-dataLancto"></span></td>
          </tr>
          <tr>
            <td><strong>Reparte:</strong></td>
            <td><span id="somarEstudo-reparte"></span></td>
          </tr>
        </table>
      </fieldset>
      
      </td>
    <td style="width:20px;">&nbsp;</td>
    <td valign="top">
      <fieldset style=" width:350px; margin-left:10px;">
        <legend>Estudos a ser Somado</legend>
        <table width="347" border="0" cellspacing="2" cellpadding="2" >
          <tr>
            <td width="107"><strong>Estudo:</strong></td>
            <td colspan="3" ><input id="somarEstudo-estudoPesquisa" class="pesquisaEstudo" type="text" onchange="matrizDistribuicao.carregarProdutoPorEstudoParaSoma()" name="textfield5" id="textfield5" style="width:100px; margin-right:5px; float:left;" />
              <span title="Pesquisar" class="classPesquisar"><a href="javascript:matrizDistribuicao.pesquisarProdutos();">&nbsp;</a></span>
            </td>
          </tr>
          <tr>
            <td><strong>Código:</strong></td>
            <td width="74" ><span id="somarEstudo-somado-codigoProduto"></span></td>
          </tr>
          <tr>
            <td><strong>Produto:</strong></td>
            <td colspan="3" ><span id="somarEstudo-somado-nomeProduto"></span></td>
          </tr>
          <tr>
          	<td><strong>Edição:</strong></td>
            <td width="3" ><span id="somarEstudo-somado-edicao"></span></td>
          </tr>
          <tr>
            <td><strong>Classificação:</strong></td>
            <td colspan="3" ><span id="somarEstudo-somado-classificacao"></span></td>
          </tr>
          <tr>
            <td><strong>Data Lançamento:</strong></td>
            <td colspan="3" ><span id="somarEstudo-somado-dataLancto"></span></td>
          </tr>
          <tr>
            <td><strong>Reparte:</strong></td>
            <td colspan="3" ><span id="somarEstudo-somado-reparte"></span></td>
          </tr>
        </table>
      </fieldset>
      </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
      </table>
       
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <span class="bt_novos"><a href="javascript:;" onclick="matrizDistribuicao.mostraTelaMatrizDistribuicao()"><img src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span>
                     <span class="bt_novos"><a href="javascript:;" id="somarEstudo-gerarEstudo" onclick="matrizDistribuicao.confirmarSomaDeEstudos()" ><img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Gerar Estudo" hspace="5" border="0" />Gerar Estudo</a></span>
                     <span class="bt_novos"><a href="javascript:;" onclick="analisar()" ><img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif" alt="Confirmar" hspace="5" border="0" />Análise</a></span>
        </fieldset>

    
    </div>
    
    <div id="dialog-confirm-coincidencia-cotas" title="Confirmar Coincid&ecirc;ncia de cotas" style="display:none;">
						H&aacute; coincid&ecirc;ncia de cotas entre os estudos. Deseja continuar?
			</div>
</div>