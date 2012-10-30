<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/relatorioGarantias.js"></script>
	<style type="text/css">
		#dialog-detalhe-garantia fieldset{width:800px!important;}
	</style>

</head>

<body>


<!-- FILTRO DA BUSCA-->
<!-- <div class="container"> -->

<form id="relatorioGarantiasForm">
     <fieldset class="classFieldset">
   	    <legend> Pesquisar Garantias</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="98">Tipo de Garantia:</td>
	                <td width="203">
	                	<select name="filtro.tipoGarantia" id="selectTipoGarantia" onchange="relatorioGarantiasController.hideGrids()" style="width:180px;">
		                	<option>Selecione...</option>
		                	<option value="TODAS">Todas</option>
		                	<c:forEach varStatus="counter" var="tipoGarantia"
	                                        items="${listaTiposGarantia}">
	                                        <option value="${tipoGarantia.key}">${tipoGarantia.value}</option>
	                        </c:forEach>
	                	</select>
	                </td>
	                <td width="39">Status:</td>
	                <td width="478">
	                	<select name="filtro.statusGarantia" id="selectStatusGarantia" style="width:180px;">
			                <option selected="selected">Selecionar...</option>
			                <option value="TODAS">Todas</option>
		                	<c:forEach varStatus="counter" var="tipoStatusGarantia"
	                                        items="${listaTiposStatusGarantia}">
	                                        <option value="${tipoStatusGarantia.key}">${tipoStatusGarantia.value}</option>
	                        </c:forEach>
	              		</select>
	              </td>
	              <td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioGarantiasController.pesquisar()">Pesquisar</a></span></td>
	            </tr>
	        </table>
      </fieldset>
</form>      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <!-- RESULTADO DA BUSCA-->
      <fieldset class="classFieldset">
       	  <legend>Garantias Cadastradas</legend>
        	<div class="grids">
            	<div id="todasGarantias" style="display:none;">
           			<table class="relatorioTodasGarantiasGrid"></table>	
	            	<table width="950" border="0" cellspacing="1" cellpadding="1">
						<tr>
		                	<td width="277">
					            <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarTodasGarantias?fileType=XLS">Arquivo</a></span>
								<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarTodasGarantias?fileType=PDF">Imprimir</a></span>
	          				</td>
	          			</tr>
	          		</table>
          		</div>
          	</div>
          	
          	<div class="grids">
            	<div id="garantiasEspecificas" style="display:none;">
            		<table class="relatorioGarantiaGrid"></table>
           			<table width="950" border="0" cellspacing="1" cellpadding="1">
						<tr>
		                	<td width="277">
					            <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=XLS">Arquivo</a></span>
								<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=PDF">Imprimir</a></span>
	          				</td>
	          			</tr>
	          		</table>
           		</div>
         	</div>
   
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
<!-- </div> -->

<!--Detalhe Garantia-->

<div id="dialog-detalhe-garantia" title="Detalhes da Garantia" style="display:none;">
	<fieldset>
    	<legend>Garantia Selecionada: Cheque Cau&ccedil;&atilde;o</legend>
        <table class="garantiaDetalheGrid"></table>
        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=XLS">Arquivo</a></span>
		<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=PDF">Imprimir</a></span>
    
    <div style="float:right; margin-top:10px; margin-right:270px;"><strong>Total R$:&nbsp;</strong><span id="totalGarantia" ></span></div>
    </fieldset>
</div>



<script type="text/javascript">
		$(function(){
			relatorioGarantiasController.init();
		});
</script>
</body>