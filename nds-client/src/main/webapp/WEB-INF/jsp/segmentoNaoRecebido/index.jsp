<head>
	<script type="text/javascript" src="scripts/segmentoNaoRecebido.js"/>
	<script language="javascript" type="text/javascript">
		$(function() {
			segmentoNaoRecebidoController.init();
		});
	</script>
</head>
<body>

<div id="dialog-excluir" title="Excluir Segmento Não Recebida">
	<p>Confirma a exclusão desta Segmento?</p>
</div>

<div id="dialog-novo" title="Novo Segmento">
	<fieldset style="width:600px!important;">
    	<legend>Pesquisar Banca</legend>
        <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		    <td width="33">Cota:</td>
		    <td width="85"><input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
		    <td width="41">Nome:</td>
		    <td width="217"><input type="text" name="textfield2" id="textfield2" style="width:200px;"/></td>
		    <td width="98"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
		  </tr>
		</table>
	</fieldset>
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
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Segmento Não Recebido</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="filtrar" id="radio" value="radio" onclick="segmentoNaoRecebidoController.filtroPorSegmento();" /></td>
            <td width="77">Segmento</td>
            <td width="20"><input type="radio" name="filtrar" id="radio2" value="radio" onclick="segmentoNaoRecebidoController.filtroPorCota();" /></td>
            <td width="35">Cota</td>
            <td width="772">
            	<table width="756" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorSegmento" style="display:none;">
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
			            		<a href="javascript:;" onclick="mostrar(); segmentoNaoRecebidoController.porSegmento();">Pesquisar</a>
			            	</span>
			            </td>
			          </tr>
			     </table>
        
        
        
        <table width="756" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorCota" style="display:none;">
            <tr>
           	  <td width="31">Cota:</td>
                <td width="90"><input type="text" name="textfield" id="numeroCotaFiltro1" style="width:80px;"/></td>
                <td width="40">Nome:</td>
                <td width="465"><input type="text" name="textfield2" id="nomeCotaFiltro1" style="width:200px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="segmentoNaoRecebidoController.porCota();">Pesquisar</a></span></td>
            </tr>
          </table>
            </td>
          </tr>
        </table>
        
        

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:noneA;">
      
      <div class="porSegmento" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Cotas que Não Recebem Segmento</legend>
        
        	<table class="segmentoNaoRecebidaGrid"></table>
            
            
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
            
            <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
            
            <span class="bt_novos" title="Exceções Segmentos e Parciais"><a href="tratamento_excessao.htm"><img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" hspace="5" border="0" />Exceções Segmentos e Parciais</a></span>
      
        
      </fieldset>

	<!-- GRID COTA PARA SELEÇÃO E INCLUSÃO NO SEGMENTO NÃO RECEBIDO -->
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Cota</legend>
       	  <table width="275" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	    <tr>
       	      <td width="33">Cota:</td>
       	      <td width="41"><input type="text" id="numeroCotaFiltro2" style="width:40px;"/></td>
              <td width="41">Nome:</td>
       	      <td width="115"><input type="text" id="nomeCotaFiltro2" style="width:140px;"/></td>
              <td width="19"><span class="classPesquisar"><a href="javascript:;" onclick="segmentoNaoRecebidoController.pesquisarCotasNaoEstaoNoSegmento()">&nbsp;</a></span></td>
   	        </tr>
   	      </table>
       	  <br />

       	<table class="segmentosGrid"></table>
             
      <!-- Confirmar a inclusão das cotas no segmento não recebido -->
      <span class="bt_novos" title="Confirmar" style="float:right;"><a href="javascript:;" onclick="segmentoNaoRecebidoController.incluirCotaSegmentoNaoRecebido()"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Confirmar</a></span>
        
      </fieldset>
      </div>
      
      <div class="porCota" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Segmentos que Não Recebem Cota</legend>
        
        	<table class="segmentoCotaGrid"></table>
            
            
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
	
			<span class="bt_novos" title="Exceções Segmentos e Parciais"><a href="tratamento_excessao.htm"><img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" hspace="5" border="0" />Exceções Segmentos e Parciais</a></span>
      
        
      </fieldset>
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Segmentos</legend>
        	<table width="271" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
           	  <td width="60">Segmento:</td>
              <td width="210">
              	<input name="lstSegmento" type="text" style="width:200px;" id="lstSegmento"/>
              </td>
            </tr>
          </table>
          <br />

       	<table class="segmentosBGrid"></table>
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

      <span class="bt_novos" title="Confirmar" style="float:right;"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Confirmar</a></span>
        
      </fieldset>
      </div>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
</body>