
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="scripts/fixacaoReparte.js"></script>
<script type="text/javascript" src="scripts/mixCotaProduto.js"></script>

<script type="text/javascript">
var pesquisaProduto = new PesquisaProduto();
var pesquisaCota = new PesquisaCota();

$(function(){
	mixCotaProdutoController.init();
});

function pesqCota(){
	$('.porCota').show();
	$('.porProduto').hide();
	$('.pesqCota').hide();
	$('.pesqProduto').hide();
	limpaCamposRadioProduto();
}
function pesqProduto(){
	$('.porCota').hide();
	$('.porProduto').show();
	$('.pesqCota').hide();
	$('.pesqProduto').hide();
	limpaCamposRadioCota();
}

function limpaCamposRadioCota(){
	$('#codigoCota').val("");
	$('#nomeCota').val("");
}
function limpaCamposRadioProduto(){
	$('#codigoProduto').val("");
	$('#nomeProduto').val("");
}
</script>


<body>


<br clear="all"/>
    <br />
    
     <div class="container">
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Mix de Produto</legend>
        <table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="radio" id="radio" value="radio" onclick="pesqCota();" /></td>
            <td width="39">Cota</td>
            <td width="20"><input type="radio" name="radio" id="radio2" value="radio"  onclick="pesqProduto();" /></td>
            <td width="82">Produto</td>
            <td width="757"><table width="99%" border="0" cellspacing="0" cellpadding="0" class="porCota">
              <tr>
                <td width="5%">Cota:</td>
                <td width="13%"><input type="text" name="codigoCota" id="codigoCota" style="width:80px;" onchange="pesquisaCota.pesquisarPorNumeroCota('#codigoCota','#nomeCota',false,undefined,undefined)"/></td>
                <td width="6%">Nome:</td>
                <td width="62%"><input type="text" name="nomeCota" id="nomeCota" style="width:200px;"/></td>
                <td width="14%"><span class="bt_pesquisar"><a href="javascript:;" onclick="mixCotaProdutoController.pesquisarPorCota()">Pesquisar</a></span></td>
              </tr>
            </table>
            <table width="99%" border="0" cellspacing="0" cellpadding="0" class="porProduto">
              <tr>
                <td width="6%">Código:</td>
                <td width="13%"><input type="text" name="codigoProduto" id="codigoProduto"  style="width:80px;" onchange="pesquisaProduto.pesquisarPorCodigoProduto('#codigoProduto','#nomeProduto',false,undefined,undefined )"/></td>
                <td width="7%">Produto:</td>
                <td width="24%"><input type="text" name="nomeProduto" id="nomeProduto" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#nomeProduto');" style="width:200px;"/></td>
                <td width="11%">Classificação:</td>
                <td width="25%">
	                <select name="select" id="select" style="width:160px;">
				            <c:forEach items="${classificacao}" var="tipoProduto">
								<option value="<c:out value="${tipoProduto.descricao}"/>"><c:out value="${tipoProduto.descricao}"/></option>
							</c:forEach>
	          		</select>
                </td>
                <td width="14%"><span class="bt_pesquisar"><a href="javascript:;" onclick="mixCotaProdutoController.pesquisarPorProduto()">Pesquisar</a></span></td>
              </tr>
            </table></td>
          </tr>
        </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:block;">
      <fieldset class="classFieldset pesqCota" style="display:none;">
       	  <legend >Cota: &nbsp;<span id="spanLegendCota"></span></legend>
        
        	<table class="mixCotasGrid"></table>
         <span class="bt_novos" title="Adicionar em Lote"  id="btAddLoteMixCota"><a href="javascript:;" onclick="add_lote();"><img src="images/ico_integrar.png" hspace="5" border="0" />Adicionar em Lote</a></span>
         <span class="bt_novos" title="Novo"  id="btNovoMixCota"><a href="javascript:;" onclick="novoProduto();"><img src="images/ico_salvar.gif" hspace="5" border="0" />Novo</a></span>
         <!--<span class="bt_novos" title="Histórico"><a href="javascript:;" onclick="mostra_historico();"><img src="../images/ico_msg_anteriores.gif" hspace="5" border="0" />Histórico</a></span>-->
         <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoMixCota"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridCota?fileType=XLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
         <span class="bt_novos" title="Imprimir" id="btImprimirMixCota"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridCota?fileType=PDF"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>

            
      </fieldset>
      
      
      
      <fieldset class="classFieldset pesqProduto" style="display:none;">
       	  <legend>Produto: &nbsp;<span id="spanLegendProduto">lalalal</span></legend>
        
        	<table class="mixProdutosGrid"></table>
         <span class="bt_novos" title="Adicionar em Lote" id="btAddLoteMixProduto"><a href="javascript:;" onclick="add_lote();"><img src="images/ico_integrar.png" hspace="5" border="0" />Adicionar em Lote</a></span>
         <span class="bt_novos" title="Novo" id="btNovoMixProduto"><a href="javascript:;" onclick="novaCota();"><img src="images/ico_salvar.gif" hspace="5" border="0" />Novo</a></span>
         <!--<span class="bt_novos" title="Histórico" id=""><a href="javascript:;" onclick="mostra_historico();"><img src="../images/ico_msg_anteriores.gif" hspace="5" border="0" />Histórico</a></span>-->
         <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoMixProduto"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridProduto?fileType=XLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
        <span class="bt_novos" title="Imprimir" id="btImprimirMixProduto"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridProduto?fileType=PDF"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
      </fieldset>
       </div>
       
      <div class="linha_separa_fields">&nbsp;</div>
      
   <!-- MODAL REPARTE POR PDV -->	
	<div id="dialog-defineReparte" title="Definir reparte por PDV" style="display:none;">
  <fieldset style="width:605px!important;">
   		<legend>Dados da Cota</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Cota:</strong></td>
            <td width="92"><span id="codigoCotaModalReparte"></span></td>
            <td width="44"><strong>Nome:</strong></td>
            <td width="400"><span id="nomeCotaModalReparte" ></span></td>
            <td width="151">&nbsp;</td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>Dados do Produto</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Código:</strong></td>
            <td width="92">&nbsp;<span id="codigoProdutoModalReparte"></td>
            <td width="44"><strong>Produto:</strong></td>
            <td width="400">&nbsp;<span id="nomeProdutoModalReparte" ></td>
            <td width="44"><strong>Classificação:</strong></td>
            <td width="155">&nbsp;<span id="classificacaoModalReparte"></td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>PDV da Cota</legend>
    	<table class="pdvCotaGrid" id="pdvCotaGrid"></table>
        <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="312">&nbsp;</td>
    <td width="224">&nbsp;</td>
    <td width="71" align="center"></td>
    <td width="23">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="right">Manter Fixa&nbsp; </td>
    <td><input name="input2" type="checkbox" value="" /></td>
  </tr>
</table>
	</fieldset>

</div>
	
	<!-- DIALOG EXCLUSAO -->	
	<div id="dialog-excluir" title="Excluir Fixação" style="display:none;">
	<p>Confirma a exclusão desta Fixação?</p>
	</div>
	
 </body>     
      