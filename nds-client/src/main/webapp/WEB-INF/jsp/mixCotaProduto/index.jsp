<!-- mixCotaProduto -->
<br clear="all"/>
    <br />
    
     <div class="container">
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Mix de Produto</legend>
        <table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="radio" id="radio" value="cota" onclick="pesqCota();" /></td>
            <td width="39"> <label for="radio">Cota </label></td>
            <td width="20"><input type="radio" name="radio" id="radio2" value="produto"  onclick="pesqProduto();" /></td>
            <td width="82"><label for="radio2">Produto</label></td>
            <td width="757"><table width="99%" border="0" cellspacing="0" cellpadding="0" class="porCota">
              <tr>
                <td width="5%">Cota:</td>
                <td width="13%"><input type="text" name="codigoCotaMix" id="codigoCotaMix" style="width:80px;" onkeydown="onlyNumeric(event);" onchange="pesquisaCota.pesquisarPorNumeroCota('#codigoCotaMix','#nomeCotaMix',false,undefined,undefined)"/></td>
                <td width="6%">Nome:</td>
                <td width="62%"><input type="text" name="nomeCotaMix" id="nomeCotaMix" style="width:200px;"/></td>
                <td width="14%"><span class="bt_pesquisar"><a href="javascript:;" onclick="mixCotaProdutoController.pesquisarPorCota()">Pesquisar</a></span></td>
              </tr>
            </table>
            <table width="99%" border="0" cellspacing="0" cellpadding="0" class="porProduto">
              <tr>
                <td width="6%">C&oacute;digo:</td>
                <td width="13%"><input type="text" name="codigoProduto" id="codigoProdutoMix"  onkeydown="onlyNumeric(event);" style="width:80px;" onchange="pesquisaProduto.pesquisarPorCodigoProduto('#codigoProdutoMix','#nomeProdutoMix',false,undefined,undefined )"/></td>
                <td width="7%">Produto:</td>
                <td width="24%"><input type="text" name="nomeProduto" id="nomeProdutoMix" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#'+this.id);" style="width:200px;"/></td>
                <td width="11%">Classifica&ccedil;&atilde;o:</td>
                <td width="25%">
	                <select name="select" id="select" style="width:160px;">
				            <c:forEach items="${classificacao}" var="tipoProduto">
								<option value="<c:out value="${tipoProduto.descricao}"/>" ${tipoProduto.descricao eq 'NORMAL'? 'selected="selected"' : '' }><c:out value="${tipoProduto.descricao}"/></option>
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

        	
        	
          <span class="bt_novos" title="Adicionar em Lote"  id="btAddLoteMixCota"><a href="javascript:;" onclick="mixCotaProdutoController.addLoteMix();"><img src="images/ico_integrar.png" hspace="5" border="0" />Adicionar em Lote</a></span>
         
         <span class="bt_novos" title="Novo"  id="btNovoMixCota"><a href="javascript:mixCotaProdutoController.novoMixPorCota();"><img src="images/ico_salvar.gif" hspace="5" border="0" />Novo</a></span>
         <!--<span class="bt_novos" title="Histï¿½rico"><a href="javascript:;" onclick="mostra_historico();"><img src="../images/ico_msg_anteriores.gif" hspace="5" border="0" />Histï¿½rico</a></span>-->
         <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoMixCota"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridCota?fileType=XLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
         <span class="bt_novos" title="Imprimir" id="btImprimirMixCota"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridCota?fileType=PDF"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
		 <span class="bt_novos" title="Excluir Todos" id="btExcluirTudoCota"><a href="javascript:mixCotaProdutoController.excluirTodos();"><img src="images/ico_excluir.gif" alt="Excluir Todos" hspace="5" border="0" />Excluir Todos</a></span>
		 <span class="bt_novos" title="Cópia de mix" id="btCopiaMix"><a href="javascript:;" onclick="mixCotaProdutoController.abrirCopiaDialog()"><img src="images/ico_negociar.png" alt="Cópia de Mix" hspace="5" border="0" />C&oacute;pia de Mix</a></span>
            
      </fieldset>
      <form action="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/uploadArquivoLote" id="formUploadLoteMix" method="post" target="_blank" style="display:none" enctype="multipart/form-data">

      	<input type="file" id="excelFile" name="excelFile"/>
      </form>
      
      <fieldset class="classFieldset pesqProduto" style="display:none;">
       	  <legend>Produto: &nbsp;<span id="spanLegendProduto"></span></legend>
        
        	<table class="mixProdutosGrid"></table>
         <span class="bt_novos" title="Adicionar em Lote"  id="btAddLoteMixCota"><a href="javascript:;" onclick="mixCotaProdutoController.addLoteMix();"><img src="images/ico_integrar.png" hspace="5" border="0" />Adicionar em Lote</a></span>
         <span class="bt_novos" title="Novo" id="btNovoMixProduto"><a href="javascript:;" onclick="mixCotaProdutoController.novoMixPorProduto()"><img src="images/ico_salvar.gif" hspace="5" border="0" />Novo</a></span>
         <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoMixProduto"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridProduto?fileType=XLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
        <span class="bt_novos" title="Imprimir" id="btImprimirMixProduto"><a href="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/exportarGridProduto?fileType=PDF"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
        <span class="bt_novos" title="Excluir Todos" id="btExcluirTudoProduto"><a href="javascript:mixCotaProdutoController.excluirTodos();"><img src="images/ico_excluir.gif" alt="Excluir Todos" hspace="5" border="0" />Excluir Todos</a></span>
        		 <span class="bt_novos" title="Cópia de mix" id="btCopiaMix"><a href="javascript:;" onclick="mixCotaProdutoController.abrirCopiaDialog()"><img src="images/ico_negociar.png" alt="Cópia de Mix" hspace="5" border="0" />C&oacute;pia de Mix</a></span>
      </fieldset>
       </div>
       
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div id="cotaCopia-dialog" title="Cópia de Mix" style="display:none;">
      
      <fieldset >
   		<legend>Cota origem</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>Cota:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="cotaOrigemInput" 
		      	onchange="pesquisaCota.pesquisarPorNumeroCota('#'+this.id,'#nomeCotaOrigemInput',false,undefined,undefined)"/>
		      </td>
		      <td>
		      <label><strong>Nome:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeCotaOrigemInput"/>
   		  	</td>
   		  	</tr>
   		  </table>
   		  
   	  </fieldset>
   	  <fieldset >
   		<legend>Cota Destino</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>Cota:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="cotaDestinoInput"
		      onchange="pesquisaCota.pesquisarPorNumeroCota('#'+this.id,'#nomeCotaDestinoInput',false,undefined,undefined)"/>
		      </td>
		      <td>
		      <label><strong>Nome:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeCotaDestinoInput"/>
   		  	</td>
   		  	</tr>
   		  </table>
   	  </fieldset>
   	
      </div>
      
      <div id="produtoCopia-dialog" title="Cópia de Mix" style="display:none;">
	      
	       <fieldset >
   		<legend>Publica&ccedil;&atilde;o origem</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>C&oacute;digo:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="codigoProdutoOrigemInput" 
		      onchange="pesquisaProduto.pesquisarPorCodigoProduto('#'+this.id,'#nomeProdutoOrigemInput',false,undefined,undefined )"/>
		      </td>
		      <td>
		      <label><strong>Produto:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeProdutoOrigemInput" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#'+this.id);"/>
   		  	</td>
   		  	</tr>
   		  </table>
   		  
   	  </fieldset>
   	  <fieldset >
   		<legend>Publica&ccedil;&atilde;o Destino</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>C&oacute;digo:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="codigoProdutoDestinoInput" 
		      	onchange="pesquisaProduto.pesquisarPorCodigoProduto('#'+this.id,'#nomeProdutoDestinoInput',false,undefined,undefined )"/>
		      </td>
		      <td>
		      <label><strong>Produto:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeProdutoDestinoInput" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#codigoProdutoDestinoInput');"/>
   		  	</td>
   		  	</tr>
   		  </table>
   	  </fieldset>
	   
      </div>
      
      <!-- DIALOG  UPLOAD -->		
	<div id="modalUploadArquivoMix" title="Adicionar em Lote" style="display:none;">
	 <form action="${pageContext.request.contextPath}/distribuicao/mixCotaProduto/uploadArquivoLote" id="formUploadLoteMix" method="post"  enctype="multipart/form-data">
	 		<p>Utilize o modelo de exemplo para fazer upload para o sistema: </p>
	      	<p ><span class="bt_novos" title="Download Modelo"><a href="${pageContext.request.contextPath}/modelos/modelo_mix.xls"><img align="center" src="images/ico_excel.png" hspace="5" border="0" />Modelo de exemplo</a></span></p>
	      	<br><br><br>
	      	<hr>
	      	<p>Selecione um arquivo para upload:</p>
	      	<br>
	      	<p align="center"><input type="file" id="excelFile" name="excelFile" style="width:200px"/></p>
      </form>
	</div>	
	
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
          
          <td width="42"><strong>C&oacute;digo:</strong></td>
            <td width="92">&nbsp;<span id="codigoProdutoModalReparte"></td>
            <td width="44"><strong>Produto:</strong></td>
            <td width="400">&nbsp;<span id="nomeProdutoModalReparte" ></td>
            <td width="44"><strong>Classifica&ccedil;&atilde;o:</strong></td>
            <td width="155">&nbsp;<span id="classificacaoModalReparte"></td>
          
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
 
</table>
	</fieldset>

</div>

	<!-- DIALOG EXCLUSAO TODOS REGISTROS-->	
	<div id="dialog-excluirTodos" title="Excluir FixaÃ§Ã£o" style="display:none;">
	<p>Confirma a exclus&atilde;o de todos os registros?</p>
	</div>
	
	<!-- DIALOG EXCLUSAO -->	
	<div id="dialog-excluir" title="Excluir FixaÃ§Ã£o" style="display:none;">
	<p>Confirma a exclus&atilde;o desta Fixa&ccedil;&atilde;o?</p>
	</div>
	
	<!-- DIALOG REPARTE -->	
	<div id="dialog-confirma-reparte" title="Confirma Reparte PDV" style="display:none;">
	<p>A soma dos valores dos repartes definidos est&aacute; maior que o valor de reparte m&aacute;ximo. Deseja prosseguir?</p>
	</div>
	
	<!-- DIALOG NOVO MIX POR COTA -->	
	<div id="dialog-incluir-mix-cota" style="display:none;" title="Adicionar">
	<table id="tableNovoCota" border="0" cellspacing="1" cellpadding="1" >
         <thead>
        		<tr>
         		<td><strong>C&oacute;digo:</strong></td>
         		<td><strong>Produto:</strong></td>
         		<td><strong>Rep. Minimo:</strong></td>
         		<td><strong>Rep. Maximo:</strong></td>
         		<td><a href="javascript:mixCotaProdutoController.addLinha();"><img src="images/ico_add.gif"/></a></td>
        		</tr>
         </thead>
         <tbody>
           <tr> 
           		<td><input id="codigoModal" class="codigoId" type="text"  style="width:80px;" onblur="pesquisaProduto.pesquisarPorCodigoProduto('#'+this.id,'#'+$(this).parent().parent().find('.target').attr('id'),null,false,undefined,undefined )"></td>
           		<td><input id="produtoModal" type="text"  style="width:80px;" class="target"></td>
           		<td><input type="text"  style="width:80px;" maxlength="5"></td>
           		<td><input type="text"  style="width:80px;" maxlength="5"></td>
           		<td><input type="image" src="images/ico_excluir.gif" onclick="$(this).parent().parent().remove();mixCotaProdutoController.definirIdInput();" style="display:none"/></td>
   			</tr>
   			</tbody>
        </table>
	</div>
	
	<!-- DIALOG NOVO MIX POR PRODUTO -->	
	<div id="dialog-incluir-mix-produto" style="display:none;" title="Adicionar">
	<table id="tableNovoProduto" border="0" cellspacing="1" cellpadding="1" >
         <thead>
        		<tr>
         		<td><strong>Cota:</strong></td>
         		<td><strong>Nome PDV:</strong></td>
         		<td><strong>Rep. Minimo:</strong></td>
         		<td><strong>Rep. Maximo:</strong></td>
         		<td><a href="javascript:mixCotaProdutoController.addLinhaProduto();"><img src="images/ico_add.gif"/></a></td>
        		</tr>
         </thead>
         <tbody>
           <tr> 
           		<td><input id="numeroCotaModal" type="text"  style="width:80px;" onblur="pesquisaCota.pesquisarPorNumeroCota('#'+this.id,'#'+$(this).parent().parent().find('.target').attr('id'),null,false,undefined,undefined )"></td>
           		<td><input id="cotaModal" type="text"  style="width:80px;" class="target"></td>
           		<td><input type="text" max="5" style="width:80px;"></td>
           		<td><input type="text" max="5" style="width:80px;"></td>
           		<td><input type="image" src="images/ico_excluir.gif" onclick="$(this).parent().parent().remove();mixCotaProdutoController.definirIdInputProduto();" style="display:none"/></td>
   			</tr>
   			</tbody>
        </table>
	</div>
	
	
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>	
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
	$('#codigoCotaMix').val("");
	$('#nomeCotaMix').val("");
}
function limpaCamposRadioProduto(){
	$('#codigoProdutoMix').val("");
	$('#nomeProdutoMix').val("");
}
</script>
      