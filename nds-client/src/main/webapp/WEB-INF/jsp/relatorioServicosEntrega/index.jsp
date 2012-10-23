<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/relatorioServicosEntrega.js"></script>
</head>

<body>

	<form id="relatorioServicosEntregaForm">
	<input type="hidden" name="filtro.transportadorDetalhe" id="relatorioServicosEntrega_transportadorDetalhe"/>
	
	<fieldset class="classFieldset">
   	    <legend>Pesquisar Transportador</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              	<td width="114">Per&iacute;odo de Entrega  :</td>
              	<td width="108"><input type="text" name="filtro.entregaDataInicio" id="relatorioServicosEntrega_filtro_dataDe" style="width:80px;" onchange="relatorioServicosEntregaController.escondeGrid();"/></td>
              	<td width="23">At&eacute;</td>
              	<td width="111"><input type="text" name="filtro.entregaDataFim" id="relatorioServicosEntrega_filtro_dataAte" style="width:80px;" onchange="relatorioServicosEntregaController.escondeGrid();"/></td> 
              	<td width="86">Transportador:</td>
              	<td width="368">
              		<select name="filtro.idTransportador" id="relatorioServicosEntrega_filtro_transportador" style="width:300px;" onchange="relatorioServicosEntregaController.escondeGrid();">
                		<option selected="selected" value="-1"></option>
						<c:forEach items="${listTransportadores}" var="transportador">
							<option value="${transportador.id}">${transportador.pessoaJuridica.razaoSocial}</option>
						</c:forEach>
              		</select>
              	</td>
              	<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioServicosEntregaController.pesquisar();"></a></span></td>
            </tr>
        </table>

    </fieldset>
    
    </form>
    
    <div class="linha_separa_fields">&nbsp;</div>
    
    <fieldset class="classFieldset">
    	<legend>Transportadores Cadastrados</legend>
        <div class="grids" style="display:none;">
       		<table class="relatorioServicosEntrega_transportadoresGrid"></table>
        	<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/administracao/relatorioServicosEntrega/exportar?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/administracao/relatorioServicosEntrega/exportar?fileType=PDF">Imprimir</a></span>
        </div>
    </fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>

	<div id="relatorioServicosEntrega_dialogDetalhe" title="Detalhes" style="width: 450px!important; display: none;">
  		<fieldset>
  			<legend>Transportador 1</legend>
    		<table class="relatorioServicosEntrega_detalheGrid"></table>
  		</fieldset>
	</div>
	
	<script type="text/javascript">
		$(function(){
			relatorioServicosEntregaController.init();
		});
	</script>
</body>	