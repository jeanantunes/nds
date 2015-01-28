<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}"/>
<head>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/autoCompleteController.js"></script>
<script type="text/javascript" src="scripts/classificacaoNaoRecebida.js" ></script>
<script language="javascript" type="text/javascript">

var	pesquisaCota = new PesquisaCota();

$(function() {

	classificacaoNaoRecebidaController.init();
});
</script>

</head>

<body>

<div id="dialog-incluirCotaNaClassificacaoNaoRecebida" style="display: none;" title="Incluir Cota">
	<p>Confirma a inclus&atilde;o desta cota?</p>
</div>

<div id="dialog-excluirCotaDaClassificacaoNaoRecebida" style="display: none;" title="Excluir Cota">
	<p>Confirma a exclus&atilde;o desta cota?</p>
</div>

<div id="dialog-incluirClassificacaoNaCota" style="display: none;" title="Incluir Classifica&ccedil;&atilde;o">
	<p>Confirma a inclus&atilde;o desta classifica&ccedil;&atilde;o?</p>
</div>

<div id="dialog-excluirCotaDaClassificacaoNaoRecebida" style="display: none;" title="Excluir Classifica&ccedil;&atilde;o">
	<p>Confirma a exclus&atilde;o desta classifica&ccedil;&atilde;o?</p>
</div>

<div class="areaBts">
		<div class="area">
			<div class='porCota' style="display: none;">
				<span class="bt_novos" >
					<a href="javascript:;" isEdicao="true" id="confirmarInclusaoDaClassificacaoNaCota" rel='tipsy' title="Confirmar">
						<img src="images/ico_check.gif" hspace="5" border="0" />
					</a>
				</span>
			
				<span class="bt_arq" >
					<a href="javascript:;" id="porCotaGerarXLS" rel='tipsy' title="Gerar Arquivo">
						<img src="images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq" >
					<a href="javascript:;" id="porCotaGerarPDF" rel='tipsy' title="Imprimir">
						<img src="images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
			</div>
			<div class='porClassificacao' style="display: none;">
				<span class="bt_novos">
					<a href="javascript:;" isEdicao="true" id="confirmarInclusaoDaCotaNaClassificacaoNaoRecebida" rel='tipsy' title="Confirmar">
						<img src="images/ico_check.gif" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq">
					<a href="javascript:;" id="porClassificacaoGerarXLS" rel='tipsy' title="Gerar Arquivo">
						<img src="images/ico_excel.png" hspace="5" border="0" />
						
					</a>
				</span>
				<span class="bt_arq">
					<a href="javascript:;" id="porClassificacaoGerarPDF" rel='tipsy' title="Imprimir">
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
    
    <!-- <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Classificação Não Recebida < evento > com < status >.</b></p>
	</div-->
    	
      <fieldset class="classFieldset fieldFiltroItensNaoBloqueados" style="width: 953px!important;">
   	    <legend> Pesquisar Classifica&ccedil;&atilde;o N&atilde;o Recebida</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="radio" id="radio" value="radio" onclick="classificacaoNaoRecebidaController.mostraFiltroPorClassificacao();" /></td>
            <td width="90">Classifica&ccedil;&atilde;o</td>
            <td width="20"><input type="radio" name="radio" id="radio2" value="radio" onclick="classificacaoNaoRecebidaController.mostraFiltroPorCota();" /></td>
            <td width="37">Cota</td>
            <td width="757">
            <form id="filtroPrincipalClassificacao">
           	  <table width="756" border="0" cellpadding="2" cellspacing="1" id="classificacaoNaoRecebida_filtroPorClassificacao" class="filtro filtroPorClassificacao" style="display:none;">
            <tr>
            	<td width="76">Classifica&ccedil;&atilde;o:</td>
                <td width="560"><select name="filtro.idTipoClassificacaoProduto" id="classificacao-selectClassificacao" style="width:200px;">
                  <option selected="selected">Selecione...</option>
                  <c:forEach items="${listaTipoClassificacao}" var="tipoClassificacao">
                  	<option value="${tipoClassificacao.key}">${tipoClassificacao.value}</option>
                  </c:forEach>
                </select></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisarPorClassificacao" >Pesquisar</a></span></td>
            </tr>
          </table>
          </form>
          <form id="filtroPrincipalPorCota">
          	<table width="756" border="0" cellpadding="2" cellspacing="1" id="classificacaoNaoRecebida_filtroPorCota" class="filtro filtroPorCota" style="display:none;">
	          <tr>
	            <td width="42">Cota:</td>
	            <td width="122"><input type="text" name="filtro.cotaDto.numeroCota" id="porCota_numeroCota" style="width:80px; float:left; margin-right:5px;"/>
	              <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	            <td width="50">Nome:</td>
	            <td width="412"><input type="text" name="filtro.cotaDto.nomePessoa" id="porCota_nomeCota" style="width:200px;"
	            onblur="pesquisaCota.pesquisarPorNomeCota('#porCota_numeroCota', '#porCota_nomeCota');" /></td>
	            <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisarPorCota">Pesquisar</a></span></td>
	          </tr>
	        </table>
          </form>
          
            </td>
          </tr>
        </table>
        
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:block;">
      
      <div class="porClassificacao" id="classificacaoNaoRecebida_porClassificacao" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Cotas que  N&atilde;o Recebem</legend>
        
        	<table class="classificaNaoRecebidaGrid"></table>
                    
      </fieldset>

      
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Cotas que Recebem</legend>
       	  <table width="275" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	    <tr>
       	      <td width="33">Cota:</td>
       	      <td width="41"><input type="text" id="cotasQueRecebem_numeroCota" style="width:40px;"/></td>
              <td width="41">Nome:</td>
       	      <td width="115"><input type="text" id="cotasQueRecebem_nomeCota" style="width:140px;"/></td>
              <td width="19"><span class="classPesquisar"><a href="javascript:;" id="pesquisarCotaQueRecebeClassificacao">&nbsp;</a></span></td>
   	        </tr>
   	      </table>
       	  <br />
        	<table class="classificacaoGrid"></table>
             
        
      </fieldset>
      </div>
      
      
      <div class="porCota" id="classificacaoNaoRecebida_porCota" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Classifica&ccedil;&otilde;es N&atilde;o Recebidas</legend>
        
        	<table class="classificaCotaGrid"></table>
                
      </fieldset>
     
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Classifica&ccedil;&otilde;es Recebidas</legend>
       	  <br />
        	<table class="classificacaoBGrid"></table>
              
        
      </fieldset>
      </div>
      
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
</body>
