<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="scripts/ajusteReparte.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCota = new PesquisaCota();

$(function(){
	ajusteReparteController.init();
});
</script>

 
<style>
#effect {
	z-index: 1;
}
</style>
 

</head>

<body>
	<div id="dialog-excluir" title="Excluir Segmento Não Recebida">
		<p>Confirma a exclusão desta Ajuste de Reparte?</p>
	</div>
	
	<form action="/ajuste" id="excluir_form">
	<div id="dialog-confirmacao" title="Inserir cota" style="display: none;">
		<p>Confirma a inserção desta cota para ajuste?</p>
	</div>
	</form>

	<div id="dialog-segmentos" title="Selecionar Segmentos"
		style="display: none;">
		<fieldset style="width: 400px !important;">
			<legend>Lista de Segmentos</legend>

			<table class="lstSegmentosGrid">
				<tr>
				<td>
				<select name="tipoSegmento1" id="tipoSegmento1" style="width: 200px;" >
					<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
					<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>	
				</select>
				</td>
				<td>
				<input name="segmento1" id="segmento1"
						onblur="ajusteReparteController.formatarAjusteAplicadoSegmento1();"
						type="text" style="width: 60px; float: left; margin-right: 5px;" />
					</td>
				</tr>

				<tr>
				<td>
				<select name="tipoSegmento2" id="tipoSegmento2" style="width: 200px;" >
					<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
					<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>	
				</select>
				</td>				
				<td>
				<input name="segmento2" id="segmento2" onblur="ajusteReparteController.formatarAjusteAplicadoSegmento2();" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
				</td>				
				</tr>

				<tr>
				<td>
				<select name="tipoSegmento3" id="tipoSegmento3" style="width: 200px;" >
					<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
					<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>	
				</select>
				</td>
				<td>
				<input name="segmento3" id="segmento3" onblur="ajusteReparteController.formatarAjusteAplicadoSegmento3();" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
				</td>
				</tr>	
			</table>
		</fieldset>
	</div>

	<div id="dialog-novo" title="Ajuste" style="display: none;">
		<fieldset style="width: 585px !important;">
			<legend>Ajuste de Reparte</legend>
			<table width="575" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<td width="25">Cota:</td>
					<td width="99">
						<input name="numeroCota" id="numeroCota" type="text" style="width: 60px; float: left; margin-right: 5px;" 
						 	   onkeydown='onlyNumeric(event);'
						 	   onchange="pesquisaCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota');"/>
						 	   
						<span class="classPesquisar">
							<a href="javascript:;">
								&nbsp;
							</a>
						</span>
					</td>
					<td width="30">Nome:</td>
					<td width="398"><input name="nomeCota" id="nomeCota" type="text" style="width: 160px;" 
										   onkeyup="pesquisaCota.autoCompletarPorNome('#nomeCota');" 
		 	   			   				   onblur="pesquisaCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota');"/>
					</td>
				</tr>
			</table>
			
			<table width="575" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<br>
					<br>
					<td colspan="8"><strong>Formas de Ajuste</strong></td>
				</tr>
				<tr>
					<td width="20">
						<input type="radio" name="formaAjuste" value="AJUSTE_HISTORICO" 
							   onclick = "ajusteReparteController.filtroPorHistorico();" />
					</td>
					<td width="102">Ajuste Histórico</td>
					
					<td width="20">
						<input type="radio" name="formaAjuste" value="AJUSTE_VENDA_MEDIA" 
							   onclick = "ajusteReparteController.filtroPorVenda();" />
					</td>
					<td width="88">Venda Média +</td>
					
					<td width="20">
						<input type="radio" name="formaAjuste" value="AJUSTE_ENCALHE_MAX" 
							   onclick = "ajusteReparteController.filtroPorEncalhe();" />
					</td>
					<td width="139">% de Encalhe Máximo </td>
					
					<td width="20">
						<input type="radio" name="formaAjuste" id="formaAjusteAjusteSegmento" value="AJUSTE_SEGMENTO" />
					</td>
					<td width="125">Ajuste por Segmento</td>
				</tr>
			</table>
			
			
			<table width="360" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<!-- 
					 -->
					<td width="10"></td>
					<td width="102"><span class="vlrPerc" >
							<input name="AJUSTE_HISTORICO_input" id="AJUSTE_HISTORICO_input" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoHistorico();" type="text" style="width: 50px;" />
					</span></td>
					
					<!-- 
					 -->
					<td width="400"></td>
					<td width="100">
					<span class="vdaMedia" >
							<input name="AJUSTE_VENDA_MEDIA_input" id="AJUSTE_VENDA_MEDIA_input" value="1" style="display: none;" type="text" style="width: 50px;" /> 
					</span>
					</td>
					<!-- 
					 -->
					<td width="350"></td>
					<td width="250">
						<span class="encalheMaximo"	> 
							<input name="AJUSTE_ENCALHE_MAX_input" id="AJUSTE_ENCALHE_MAX_input" value="1" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoEncalhe();" type="text" style="width: 50px;" />  
						</span>
					</td>
				</tr>
			</table>

			<table width="575" border="0" cellpadding="2" cellspacing="1">
				 <tr>
					<br>
					<br>
					<td width="89">Motivo do Ajuste:</td>
					<td width="466">
						<select name="motivoAjuste" id="motivoAjuste" style="width: 200px;" >
							<option selected="selected">Selecione...</option>
								<c:forEach items="${listaMotivosStatusCota}" var="motivo">
									<option value="${motivo.key}">${motivo.value}</option>
								</c:forEach>	
						</select>
					</td>
				</tr>
				
				<tr>
					<td>Período:</td>
							 <tr>
								<td width="100">
									<input name="periodo1" id="dataInicio" type="text" value="${dataAtual}" style="width: 60px;"/>
								<td width="100">
									Até: 
									<input name="periodo2" id="dataFim" type="text" value="${dataAtual}" style="width: 60px;"/>
								</td>
							</tr>
				</tr>
				
			</table>
		</fieldset>
	</div>
	
		<div id="dialog-editar" title="Ajuste" style="display: none;">
		<fieldset style="width: 585px !important;">
			<legend>Editar Ajuste de Reparte</legend>
			<table width="575" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<td width="25">Cota:</td>
					<td width="99">
						<input name="numeroCota" id="numeroCotaEditar" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
						<span class="classPesquisar">
							<a href="javascript:;">
								&nbsp;
							</a>
						</span>
					</td>
					<td width="30">Nome:</td>
					<td width="398"><input name="nomeCota" id="nomeCotaEditar" type="text" style="width: 160px;" 
					</td>
				</tr>
			</table>
			
			<table width="575" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<br>
					<br>
					<td colspan="8"><strong>Formas de Ajuste</strong></td>
				</tr>
				<tr>
					<td width="20">
						<input type="radio" name="formaAjusteEditar" value="AJUSTE_HISTORICO" 
							   onclick = "ajusteReparteController.filtroPorHistoricoEditar();" />
					</td>
					<td width="102">Ajuste Histórico</td>
					
					<td width="20">
						<input type="radio" name="formaAjusteEditar" value="AJUSTE_VENDA_MEDIA" 
							   onclick = "ajusteReparteController.filtroPorVendaEditar();" />
					</td>
					<td width="88">Venda Média +</td>
					
					<td width="20">
						<input type="radio" name="formaAjusteEditar" value="AJUSTE_ENCALHE_MAX" 
							   onclick = "ajusteReparteController.filtroPorEncalheEditar();" />
					</td>
					<td width="139">% de Encalhe Máximo </td>
					
					<td width="20">
						<input type="radio" name="formaAjusteEditar" value="AJUSTE_SEGMENTO"   
							   onclick = "ajusteReparteController.filtroPorSegmentoEditar();" />
					</td>
					<td width="125">Ajuste por Segmento</td>
				</tr>
			</table>
			
			
			<table width="360" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<!-- 
					 -->
					<td width="10"></td>
					<td width="102"><span class="vlrPerc" >
							<input name="AJUSTE_HISTORICO_input" id="AJUSTE_HISTORICO_input_editar" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoHistorico();" type="text" style="width: 50px;" />
					</span></td>
					
					<!-- 
					 -->
					<td width="400"></td>
					<td width="100">
					<span class="vdaMedia" >
							<input name="AJUSTE_VENDA_MEDIA_input" id="AJUSTE_VENDA_MEDIA_input_editar" value="1" style="display: none;" type="text" style="width: 50px;" /> 
					</span>
					</td>
					<!-- 
					 -->
					<td width="350"></td>
					<td width="250">
						<span class="encalheMaximo"	> 
							<input name="AJUSTE_ENCALHE_MAX_input" id="AJUSTE_ENCALHE_MAX_input_editar" value="1" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoEncalhe();" type="text" style="width: 50px;" />  
						</span>
					</td>
				</tr>
			</table>

			<table width="575" border="0" cellpadding="2" cellspacing="1">
				 <tr>
					<br>
					<br>
					<td width="89">Motivo do Ajuste:</td>
					<td width="466">
						<select name="motivoAjuste" id="motivoAjusteEditar" style="width: 200px;" >
							<option selected="selected">Selecione...</option>
								<c:forEach items="${listaMotivosStatusCota}" var="motivo">
									<option value="${motivo.key}">${motivo.value}</option>
								</c:forEach>	
						</select>
					</td>
				</tr>
				
				<tr>
					<td>Período:</td>
							 <tr>
								<td width="100">
									<input name="periodo1" id="dataInicioEditar" type="text" value="${dataAtual}" style="width: 60px;"/>
								<td width="100">
									Até: 
									<input name="periodo2" id="dataFimEditar" type="text" value="${dataAtual}" style="width: 60px;"/>
								</td>
							</tr>
				</tr>
				
			</table>
		</fieldset>
	</div>


	
			<div class="grids" style="display: block;">

				<div class="porSegmento" style="display: block;">
					<!--<fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">-->
					<fieldset class="classFieldset">
						<legend>Cotas em Ajuste Reparte</legend>

						<table class="cotasAjusteGrid"></table>

						<span class="bt_novos" title="Novo">
							<a href="javascript:;" onclick="ajusteReparteController.incluirAjuste();">
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
									Novo
							</a>
						</span> 
						
						<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
							</a>
						</span> 
						
						<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
									Imprimir
							</a>
						</span>

					</fieldset>

					<!--<fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Cota</legend>
       	  <table width="270" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	    <tr>
       	      <td width="30">Cota:</td>
       	      <td width="229"><input type="text" name="lstCotas" id="lstCotas" style="width:260px;"/></td>
   	        </tr>
   	      </table>
       	  <br />

       	<table class="segmentosGrid"></table>
            
      <span class="bt_novos" title="Confirmar" style="float:right;"><a href="javascript:;"><img src="../images/ico_check.gif" hspace="5" border="0" />Confirmar</a></span>
        
      </fieldset>-->
				</div>

			<!-- 
				<div class="porCota" style="display: none;">
					<fieldset class="classFieldset"
						style="float: left; width: 631px !important; margin-right: 10px !important;">
						<legend>Segmentos que Não Recebem Cota</legend>

						<table class="segmentoCotaGrid"></table>

						<span class="bt_novos" title="Gerar Arquivo">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
							</a>
						</span> 
						
						<span class="bt_novos" title="Imprimir">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
									Imprimir
							</a>
						</span>


					</fieldset>
					<fieldset class="classFieldset"
						style="float: left; width: 300px !important;">
						<legend>Segmentos</legend>
						<table width="271" border="0" cellpadding="2" cellspacing="1"
							class="filtro">
							<tr>
								<td width="60">Segmento:</td>
								<td width="210"><select name="select" id="select"
									style="width: 200px;">
										<option selected="selected">Selecione...</option>
								</select></td>
							</tr>
						</table>
						<br />

						<table class="segmentosBGrid"></table>
						<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a>
						</span> 
						
						<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
									Imprimir
								</a>
						</span>
						
						<span class="bt_novos" title="Confirmar" style="float: right;">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
									Confirmar
							</a>
						</span>

					</fieldset>
				</div>

		 -->
			</div>
			<div class="linha_separa_fields">&nbsp;</div>

</body>