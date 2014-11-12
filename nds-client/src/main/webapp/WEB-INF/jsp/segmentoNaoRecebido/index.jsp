<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script type="text/javascript" src="scripts/segmentoNaoRecebido.js" /></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script language="javascript" type="text/javascript">
		var pesquisaCota = new PesquisaCota();
	
		$(function() {
			segmentoNaoRecebidoController.init();
		});
	</script>
	<style type="text/css">
		#dialog-excluir-cota, #dialog-novo-cota {
			display:none;
		}
	</style>
</head>
<body>

<div id="dialog-excluir-cota" title="Excluir Cota">
	<p>Confirma a exclusão desta Cota?</p>
</div>


<div id="dialog-novo-cota" title="Incluir Cota">
	<p>Confirma esta inclusão?</p>
</div>

<div id="dialog-excluir" title="Excluir Segmento Não Recebida">
	<p>Confirma a exclusão deste Segmento?</p>
</div>


<div id="dialog-novo" title="Incluir Segmento">
	<p>Confirma esta inclusão?</p>
</div>

<div class="areaBts">
  <div class="area">
    <div class="porSegmento" style="display:none;">
      <span class="bt_novos">
        <a href="javascript:;" isEdicao="true" onclick="segmentoNaoRecebidoController.incluirCotaSegmentoNaoRecebido()" rel="tipsy" title="Confirmar">
          <img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_novos" style="float:right;">
        <a href="javascript:;" onclick="segmentoNaoRecebidoController.limparListaCotas()" rel="tipsy" title="Limpar">
        </a>
      </span>
      <span class="bt_arq">
        <a href="${pageContext.request.contextPath}/distribuicao/segmentoNaoRecebido/exportar?fileType=XLS&tipoExportacao=cotas_nao_recebem_segmento" rel="tipsy" title="Gerar Arquivo">
          <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
        </a>
      </span>
            
      <span class="bt_arq">
        <a href="${pageContext.request.contextPath}/distribuicao/segmentoNaoRecebido/exportar?fileType=PDF&tipoExportacao=cotas_nao_recebem_segmento" rel="tipsy" title="Imprimir">
          <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
        </a>
      </span>
    </div>
    <div class="porCota" style="display:none;">
      <span class="bt_novos" >             
        <a href="javascript:;" onclick="$('#workspace').tabs('addTab', 'Exceção de Segmentos e Parciais', '${pageContext.request.contextPath}/distribuicao/segmentoNaoRecebido/chamarTelaExcecaoSegmentoParcias')" rel="tipsy" title="Exceções Segmentos e Parciais"> 
          <img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" hspace="5" border="0" />
        </a>
      </span>
      <span class="bt_novos">
        <a href="javascript:;" isEdicao="true" onclick="segmentoNaoRecebidoController.incluirSegmento()" rel="tipsy" title="Confirmar">
          <img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
        </a>
      </span>
        
    </div>
  </div>
      
</div>


<div class="corpo">
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <!-- 
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Segmentação Não Recebida < evento > com < status >.</b></p>
	</div>
	 -->
    	
      <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Segmento Não Recebido</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="filtrar" id="radio" value="radio" onclick="segmentoNaoRecebidoController.filtroPorSegmento();" /></td>
            <td width="77">Segmento</td>
            <td width="20"><input type="radio" name="filtrar" id="radio2" value="radio" onclick="segmentoNaoRecebidoController.filtroPorCota();" /></td>
            <td width="35">Cota</td>
            <td width="772">
            	<table width="756" border="0" cellpadding="2" cellspacing="1" id="segmentoNaoRecebido_filtroPorSegmento" class="filtro filtroPorSegmento" style="display:none;">
          			<tr>
			            <td width="64">Segmento:</td>
			            <td width="214">
			            	<select name="select2" id="tipoSegmentoProduto" style="width:200px;">
			              		<option selected="selected" value="0">Selecione...</option>
			              		<c:forEach items="${listaTipoSegmentoProduto}" var="segmentoProduto">
			              			<option value="${segmentoProduto.key}"> ${segmentoProduto.value}</option>
			              		</c:forEach>
			            	</select>
			            </td>
            			<td width="352">
            
			            <table width="279" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td width="23"><input type="radio" name="todas" id="todasCotas" value="radio" /></td>
			                <td width="99">Todas as Cotas</td>
			                <td width="20"><input type="radio" name="todas" id="cotasAtivas" checked value="radio" /></td>
			                <td width="137">Cotas Ativas</td>
			              </tr>
			            </table>
			            </td>
			            <td width="105">
			            	<span class="bt_pesquisar">
			            		<a href="javascript:;" onclick="segmentoNaoRecebidoController.porSegmento();">Pesquisar</a>
			            	</span>
			            </td>
			          </tr>
			     </table>
        
        
        
        <table width="756" border="0" cellpadding="2" cellspacing="1" id="segmentoNaoRecebido_filtroPorCota" class="filtro filtroPorCota" style="display:none;">
            <tr>
           	  <td width="31">Cota:</td>
                <td width="90">
                	<input onchange="pesquisaCota.pesquisarPorNumeroCota('#numeroCotaFiltro1','#nomeCotaFiltro1');" type="text" name="textfield" id="numeroCotaFiltro1" style="width:80px;"/>
                </td>
                <td width="40">Nome:</td>
                <td width="465">
                	<input type="text" name="textfield2" id="nomeCotaFiltro1" onblur="pesquisaCota.pesquisarPorNomeCota('#numeroCotaFiltro1','#nomeCotaFiltro1');"
                		onkeyup="pesquisaCota.autoCompletarPorNome('#nomeCotaFiltro1')" style="width:200px;"/>
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="segmentoNaoRecebidoController.porCota();">Pesquisar</a></span></td>
            </tr>
          </table>
            </td>
          </tr>
        </table>
        
        

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids">
      
      <div class="porSegmento" id="segmentoNaoRecebido_porSegmento" style="display:none;">
        <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Cotas que Não Recebem Segmento</legend>
        
        	<table class="segmentoNaoRecebidaGrid"></table>
        </fieldset>

	<!-- GRID COTA PARA SELE�ÃO E INCLUSÃO NO SEGMENTO NÃO RECEBIDO -->
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Cota</legend>
       	  <table width="275" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	    <tr>
       	      <td width="33">Cota:</td>
       	      <td width="41">
       	      	<input type="text" name="nomeCotaFiltro2" id="numeroCotaFiltro2" 
       	      		onchange="pesquisaCota.pesquisarPorNumeroCota('#numeroCotaFiltro2','#nomeCotaFiltro2');" style="width:40px;"/>
       	      </td>
              <td width="41">Nome:</td>
       	      <td width="115">
       	      <input type="text" name="nomeCotaFiltro2" id="nomeCotaFiltro2"  style="width:140px;" 
       	      		onkeyup="pesquisaCota.autoCompletarPorNome('#nomeCotaFiltro2')"
       	      		onblur="pesquisaCota.pesquisarPorNomeCota('#numeroCotaFiltro2','#nomeCotaFiltro2');"/></td>
              <td width="19">
              	<span class="classPesquisar"><a href="javascript:;"  
              	onclick="segmentoNaoRecebidoController.pesquisarCotasNaoEstaoNoSegmento()">&nbsp;</a></span>
              </td>
   	        </tr>
   	      </table>
       	  <br />

       	<table class="segmentosGrid"></table>
             
      
        
      </fieldset>
      </div>
      
      <div class="porCota" id="segmentoNaoRecebido_porCota" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Segmentos que a Cota não Recebe</legend>
        
        	<table class="segmentoCotaGrid"></table>
            
      		 
        
      </fieldset>
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Segmentos</legend>
        	<table width="271" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
           	  <td width="60">Segmento:</td>
              <td width="210">
              	<input name="lstSegmento" type="text" onkeyup="segmentoNaoRecebidoController.autoCompletarSegmentoPorNome('#lstSegmento')"  
              	 style="width:200px;" id="lstSegmento"/>
              </td>
            </tr>
          </table>
          <br />

       	<table class="segmentosBGrid"></table>
     
      </fieldset>
      </div>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

    
    </div>
</div> 
</body>
