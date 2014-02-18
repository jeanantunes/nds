<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numberformatter-1.2.3.min.js"></script>
<script>

function analisar(){
	if ($('#copiarEstudo-estudo').html() == null || $('#copiarEstudo-estudo').html() == "") {
		exibirMensagem("WARNING",["Gere o estudo antes de fazer a an&aacute;lise."]);
		return;
	} else {
		var idEstudoComplementar = $('#copiarEstudo-estudo').html(); 
		matrizDistribuicao.redirectToTelaAnalise('#copiarEstudoContent', '#copiarEstudoTelaAnalise', idEstudoComplementar)
	}
}

</script>

	<span id="copiarEstudo-idLancamento" style="display: none;"></span>
	<span id="copiarEstudo-idCopia" style="display: none;"></span>

<div id="copiarEstudoTelaAnalise" />
<div id="copiarEstudoContent">
	
  <fieldset class="fieldFiltro">
  <legend>C&oacute;pia Proporcional de Estudo</legend>
        
  <table width="950" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td valign="top">
      <fieldset id="selecionado" style=" width:350px; margin-top:5px; margin-left:20px">
        <legend>Edi&ccedil;&atilde;o Selecionada</legend>
        <table width="347" border="0" cellspacing="2" cellpadding="2" >
          <tr>
            <td width="109"><strong>Estudo:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-estudo"></span></td>
          </tr>
          <tr>
            <td><strong>C&oacute;digo:</strong></td>
            <td width="109" ><span id="copiarEstudo-codigoProduto"></span></td>
            <td width="40" ><strong>Edi&ccedil;&atilde;o:</strong></td>
            <td width="63" ><span id="copiarEstudo-edicao"></span></td>
          </tr>
          <tr>
            <td><strong>Produto:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-nomeProduto"></span></td>
          </tr>
          <tr>
            <td><strong>Classifica&ccedil;&atilde;o:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-classificacao"></span></td>
          </tr>
          <tr>
            <td><strong>Data Lan&ccedil;amento:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-dataLancto"></span></td>
          </tr>
          <tr>
            <td><strong>Reparte:</strong></td>
            <td colspan="3" >
                <span id="copiarEstudo-reparte" style="display: none;"></span>
                <span id="copiarEstudo-reparteDistribuido"></span>
            </td>
          </tr>
        </table>
      </fieldset>
      
      </td>
    <td style="width:20px;">&nbsp;</td>
    <td valign="top">
      <fieldset style=" width:350px; margin-left:10px;">
        <legend>Estudos a ser Copiado</legend>
        <table width="347" border="0" cellspacing="2" cellpadding="2" >
          <tr>
            <td width="107"><strong>Estudo:</strong></td>
            <td colspan="3" ><input id="copiarEstudo-estudoPesquisa" onChange="matrizDistribuicao.carregarProdutoPorEstudo()" type="text" name="textfield5" id="textfield5" onkeydown='onlyNumeric(event);' style="width:100px; margin-right:5px; float:left;" />
               <span title="Pesquisar" class="classPesquisar"><a href="javascript:matrizDistribuicao.pesquisarProdutosCopiaProporcional();">&nbsp;</a></span>
              
              </td>
          </tr>
          <tr>
            <td><strong>C&oacute;digo:</strong></td>
            <td width="74" ><span id="copiarEstudo-copia-codigoProduto"></span></td>
            <td width="40" ><strong>Edi&ccedil;&atilde;o:</strong></td>
            <td width="100" ><span id="copiarEstudo-copia-edicao"></span></td>
          </tr>
          <tr>
            <td><strong>Produto:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-copia-nomeProduto"></span></td>
          </tr>
          <tr>
            <td><strong>Classifica&ccedil;&atilde;o:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-copia-classificacao"></span></td>
          </tr>
          <tr>
            <td><strong>Data Lan&ccedil;amento:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-copia-dataLancto"></span></td>
          </tr>
          <tr>
            <td><strong>Reparte:</strong></td>
            <td colspan="3" ><span id="copiarEstudo-copia-reparte"></span></td>
          </tr>
        </table>
        <table width="347" border="0" cellspacing="0" cellpadding="0" >
          <tr>
            <td width="26"><input id="copiarEstudo-fixacao" name="checkbox" type="checkbox" id="checkbox" checked="checked" /></td>
            <td width="134" >Usar Fixação</td>
            <td width="187" >&nbsp;</td>
          </tr>
          <tr>
            <td><input type="checkbox" id="copiarEstudo-multiplos-check" name="checkbox" id="checkbox2" onclick="$('.porMultiplos').toggle();" /></td>
            <td ><label for="checkbox2">Distribui&ccedil;&atilde;o por m&uacute;ltiplos:</label></td>
            <td ><input type="text" id="copiarEstudo-pctPadrao" name="textfield6" id="textfield6" style="width:80px; text-align:center; display:none;" class="porMultiplos" value="10" /></td>
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
                     <span class="bt_novos"><a href="javascript:;" onclick="matrizDistribuicao.cancelarCopiaProporcionalDeEstudo()"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Cancelar" hspace="5" border="0" />Cancelar</a></span>
                     <span class="bt_novos"><a href="javascript:;" onclick="matrizDistribuicao.confirmarCopiarProporcionalDeEstudo()" ><img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Gerar Estudo" hspace="5" border="0" />Gerar Estudo</a></span>
                     <span class="bt_novos"><a href="javascript:;" onclick="matrizDistribuicao.analisarCopiaProporcional()" ><img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif" alt="An&aacute;lise" hspace="5" border="0" />An&aacute;lise</a></span>
        </fieldset>

    
    </div>
