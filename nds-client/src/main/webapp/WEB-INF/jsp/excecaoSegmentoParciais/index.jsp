<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script type="text/javascript" src="scripts/autoCompleteController.js" ></script>
	<script type="text/javascript" src="scripts/excecaoSegmentoParciais.js" ></script>
	<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="scripts/utils.js"></script>
	<script type="text/javascript">

	var	pesquisaCota = new PesquisaCota(),
  pesquisaProduto = new PesquisaProduto();	

  $(function() {
    excecaoSegmentoParciaisController.init();
  });

  function excluir_produto() {

   $( "#dialog-excluir" ).dialog({
    resizable: false,
    height:170,
    width:380,
    modal: true,
    buttons: {
     "Confirmar": function() {
      $( this ).dialog( "close" );
      $("#effect").show("highlight", {}, 1000, callback);
    },
    "Cancelar": function() {
      $( this ).dialog( "close" );
    }
  }
});
 };		

 function incluirSegmento() {

   $( "#dialog-novo" ).dialog({
    resizable: false,
    height:500,
    width:650,
    modal: true,
    buttons: {
     "Confirmar": function() {
      $( this ).dialog( "close" );
      $("#effect").show("highlight", {}, 1000, callback);
    },
    "Cancelar": function() {
      $( this ).dialog( "close" );
    }
  }
});
 };		

 </script>
</head>

<body>

  <div id="dialog-excluirExcecao" title="Excluir Exceção " style="display: none;">
   <p>Confirma a exclusão desta Exceção?</p>
 </div>

 <div id="dialog-incluirExcecao" title="Incluir Exceção" style="display: none;">
   <p>Confirma a inclusão deste(s) produto(s)?</p>
 </div>

 <div id="dialog-excluirCotaDaExcecao" title="Excluir Cota da exceção " style="display: none;">
   <p>Confirma a exclusão desta cota?</p>
 </div>

 <div id="dialog-incluirCotaNaExcecao" title="Incluir Exceção" style="display: none;">
   <p>Confirma a inclusão desta(s) cota(s)?</p>
 </div>

 <div class="areaBts">
  <div class="area">
    <div class="porExcessao" style="display:none;">

      <span class="bt_novos">
        <a href="javascript:;" id="limparCotaNaExcecao" rel="tipsy" title="limpar">
          <img src="images/ico_excluir.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_novos">
        <a href="javascript:;" isEdicao="true" id="inserirCotaNaExcecao" rel="tipsy" title="Confirmar">
          <img src="images/ico_check.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_arq">
        <a id="gerarXLSPorExcecao" rel="tipsy" title="Gerar Arquivo">
          <img src="images/ico_excel.png" hspace="5" border="0" />
        </a>
      </span>

      <span class="bt_arq">
        <a id="gerarPDFPorExcecao" rel="tipsy" title="Imprimir">
          <img src="images/ico_impressora.gif" hspace="5" border="0" />
        </a>
      </span>

    </div>
    <div class='porCota' style="display:none;">
      <span class="bt_novos">
        <a href="javascript:;" id="limparExcecaoDeProdutos" rel="tipsy" title="limpar">
          <img src="images/ico_excluir.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_novos">
        <a href="javascript:;" isEdicao="true" id="inserirExcecaoDeProdutos" rel="tipsy" title="Confirmar"> 
          <img src="images/ico_check.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_arq">
        <a id="gerarXLSPorCota" rel="tipsy" title="Gerar Arquivo">
          <img src="images/ico_excel.png" hspace="5" border="0" />
        </a>
      </span>

      <span class="bt_arq">
        <a id="gerarPDFPorCota" rel="tipsy" title="Imprimir">
          <img src="images/ico_impressora.gif" hspace="5" border="0" />
        </a>
      </span>

    </div>
  </div>
</div>

<div class="corpo">
  <br clear="all"/>
  <br />

  <div class="container">

   <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
    <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
      <b>Classificação Não Recebida < evento > com < status >.</b></p>
    </div>

    <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
      <legend> Pesquisar Exceções de Segmentos e Parciais</legend>


      <table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
       <tr>
         <td width="20"><input type="radio" name="tipoExcecao" id="tipoExcecaoSegmento" checked="checked" class="pesquisaPorSegmento"/></td>
         <td width="188"> <label for="tipoExcecaoSegmento"> Por Exce&ccedil;&atilde;o Segmento </label></td> 
         <td width="20"><input type="radio" name="tipoExcecao" id="tipoExcecaoSegmento" class="pesquisaPorParcial" onclick="excecaoSegmentoParciaisController.limparCampos();"/></td>
         <td width="696"><label for="tipoExcecaoSegmento">Por Exce&ccedil;&atilde;o de Parciais</label></td>
       </tr>
     </table>

     <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
      <tr>
        <td width="22" align="right">
         <input type="radio" name="filtroPor" id="radio" value="radio" onclick="excecaoSegmentoParciaisController.filtroPorCota();" /></td>
         <td width="50"><label for="radio" >Cota</label></td>
         <td width="22"><input type="radio" name="filtroPor" id="radio2" value="radio" onclick="excecaoSegmentoParciaisController.filtroPorProduto()" /></td>
         <td width="49"><label for="radio2" >Produto</label></td>
         <td width="781">
          <form id="filtroPrincipalCota">
           <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorCota" id="excecaoSegmentoParciais_filtroPorCota" style="display:none;">
            <tr>
              <td width="30">Cota:</td>
              <td width="88">
               <input type="text" name="filtro.cotaDto.numeroCota" id="numeroCotaFiltroPrincipal" style="width:80px;"/>
             </td>
             <td width="37">Nome:</td>
             <td width="100">
               <input type="text" name="filtro.cotaDto.nomePessoa" id="nomeCotaFiltroPrincipal" style="width:200px;"/>
             </td>
             <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="2005_pesquisaPorCota" >Pesquisar</a></span></td>
           </tr>
         </table>
       </form>
       <form id="filtroPrincipalProduto">
        <table width="680" border="0" cellpadding="2" cellspacing="1" id="excecaoSegmentoParciais_filtroPorProduto" class="filtro filtroPorProduto" style="display:none;">
          <tr>
            <td width="42">Código:</td>
            <td width="65"><input type="text" name="filtro.produtoDto.codigoProduto" id="codigoProdutoPrincipal" onkeydown='onlyNumeric(event);' style="width:60px; float:left; margin-right:5px;"  /></td>
            <td width="47">Produto:</td>
            <td width="100"><input type="text" name="filtro.produtoDto.nomeProduto" id="nomeProdutoPrincipal" style="width:120px;"/></td>
            <td width="49">Fornecedor:</td>
            <td width="110"><input type="text" name="fornecedorPrincipal" id="fornecedorPrincipal" style="width:110px;" disabled="disabled"/></td>
          </tr>
          <tr>
          	<td width="100">Segmento:</td>
            <td width="180"><input type="text" name="segmentoProdutoPrincipal" id="segmentoProdutoPrincipal" style="width:110px;" disabled="disabled"/></td>
            <td width="150">Classificação:</td>
            <td width="180">
             <select name="filtro.produtoDto.idClassificacaoProduto" id="2005_comboClassificacao" style="width:140px;">
              <c:forEach items="${listaClassificacao}" var="classificacao">
              <option value="${classificacao.id}">${classificacao.descricao}</option>
            </c:forEach>
          </select>
          <td width="80"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisaPorExcecao" >Pesquisar</a></span></td>
        </tr>
      </table>
    </form>
  </td>
</tr>
</table>


</fieldset>
<div class="linha_separa_fields">&nbsp;</div>
<div class="grids" style="display:block;">

  <div class="porExcessao" id="excecaoSegmentoParciais_porExcessao" style="display:none;">
    <fieldset class="classFieldset" style="float:left; width:585px!important; margin-right:10px!important;">
      <legend id="legendPorExcecaoRecebe"></legend>

      <table class="excessaoNaoRecebidaGrid"></table>

    </fieldset>
    <fieldset class="classFieldset" style="float:left; width:330px!important;">
      <legend id="legendPorExcecaoNaoRecebe"></legend>
      <table width="276" border="0" cellpadding="2" cellspacing="1" class="filtro">
        <tr>
          <td width="33">Cota:</td>
          <td width="60">
           <input type="text" id="cotasQueNaoRecebemNumeroCota" style="width:40px;"/></td>
           <td width="41">Nome:</td>
           <td width="121">
             <input type="text" id="cotasQueNaoRecebemNomeCota" style="width:160px;"/>
           </td>
           <td width="19">
             <span class="classPesquisar"><a href="javascript:;" id="pesquisarCotaQueNaoRecebem">&nbsp;</a></span>
           </td>
         </table>
         <br />
         <table class="excessaoGrid"></table>


       </fieldset>
     </div>


     <div class="porCota" id="excecaoSegmentoParciais_porCota" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:510px!important; margin-right:10px!important;">
        <legend id="legendPorCotaRecebe"></legend>
        
        <table class="excessaoCotaGrid" ></table>

      </fieldset>
      
      <fieldset class="classFieldset" style="float:left; width:430px!important;">
        <legend id="legendPorCotaNaoRecebe"></legend>
        <table width="312" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="45">
             Código        	      </td>
             <td width="60">
               <input type="text" name="filtro.produtoDto.codigoProduto" id="2005_codigoProduto" onkeydown='onlyNumeric(event);' style="width:60px;"/></td>
               <td width="54">
                Produto:
              </td>
              <td width="132">
                <input type="text" name="filtro.produtoDto.nomeProduto" id="2005_nomeProduto" style="width:120px;"/>
              </td>
            </tr>
            <tr>
             <td width="78">Classifica&ccedil;&atilde;o:</td>
             <td width="152">
               <select name="filtro.produtoDto.idClassificacaoProduto" id="comboClassificacaoProdNRec" style="width:140px;">
                <c:forEach items="${listaClassificacao}" var="classificacao">
                <option value="${classificacao.id}">${classificacao.descricao}</option>
              </c:forEach>
            </select>
            <td width="19">
             <span class="classPesquisar"><a href="javascript:;" id="pesquisarProdutosParciaisNaoRecebidos">&nbsp;</a></span>
           </td>
         </tr>
       </table>
       <br />
       <table class="excessaoBGrid"></table>


     </fieldset>
   </div>

 </div>
 <div class="linha_separa_fields">&nbsp;</div>

</div>
</div> 
</body>
