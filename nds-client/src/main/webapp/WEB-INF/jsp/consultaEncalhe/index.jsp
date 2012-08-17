<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Consulta Encalhe</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">

var pesquisaCotaConsultaEncalhe = new PesquisaCota();

var ConsultaEncalhe = {
	
		pesquisar: function() {
			
			var dataRecolhimento 	= $("#dataRecolhimento").val();
			var idFornecedor		= $("#idFornecedor").val();
			var numeroCota			= $("#cota").val();
			
			var formData = [
			        
			        {name:'dataRecolhimento', value: dataRecolhimento},
			        {name:'idFornecedor', value: idFornecedor},
			        {name:'numeroCota', value: numeroCota }
			];
			
			$("#gridConsultaEncalhe").flexOptions({
				url: "<c:url value='/devolucao/consultaEncalhe/pesquisar'/>",
				params: formData
			});
			
			$("#gridConsultaEncalhe").flexReload();

		},
	
		executarPreProcessamento: function(resultado) {
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return resultado.tableModel;
			}
			
			$(".grids").show();
			
			$("#qtdExemplarDemaisRecolhimentos").val(resultado.qtdExemplarDemaisRecolhimentos);
			
			$("#qtdExemplarPrimeiroRecolhimento").val(resultado.qtdExemplarPrimeiroRecolhimento);
			
			$("#qtdProdutoDemaisRecolhimentos").val(resultado.qtdProdutoDemaisRecolhimentos);
			
			$("#qtdProdutoPrimeiroRecolhimento").val(resultado.qtdProdutoPrimeiroRecolhimento);
			
			return resultado.tableModel;
			
		},
		
		obterColModel : function() {


				var colModel = [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoVenda',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço com Desc. R$',
					name : 'precoComDesconto',
					width : 110,
					sortable : true,
					align : 'right'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Recolhimento',
					name : 'recolhimento',
					width : 80,
					sortable : true,
					align : 'center'
				}];	
				
				return colModel;
		}
	
		

		
}
	
	
$(function() {
	
	var colunas = ConsultaEncalhe.obterColModel();
	
	$("#cota").numeric();
	
	$("#gridConsultaEncalhe").flexigrid({
		
		dataType : 'json',
		preProcess:ConsultaEncalhe.executarPreProcessamento,
		//onSuccess:function(){$('input[id^="valorExemplarNota"]').numeric();},
		colModel : colunas,
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
	
	$('#dataRecolhimento').datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('#dataRecolhimento').mask("99/99/9999");
	
});

</script>

</head>

<body>
	<div class="container">

		<fieldset class="classFieldset">

			<legend> Consulta Encalhe </legend>

			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			
				<tr>
				
					<td width="30">Data:</td>
					
					<td colspan="3">
						<input type="text" id="dataRecolhimento"
						style="width: 80px; float: left; margin-right: 5px;" />
					</td>
					
					<td width="68">Fornecedor:</td>
					
					<td width="264">
					
						<select name="idFornecedor" id="idFornecedor" style="width:260px;">
						    <option value="-1"  selected="selected">Todos</option>
						    <c:forEach items="${listaFornecedores}" var="fornecedor">
						      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
						    </c:forEach>
					    </select>
					
					</td>
					
					<td width="30">
						Cota:
					</td>
					
					<td width="90">

						<input 	type="text" 
								maxlength="17"
								id="cota" onchange="pesquisaCotaConsultaEncalhe.pesquisarPorNumeroCota('#cota', '#nomeCota');" 
								style="width: 60px; float:left; margin-right:5px;"/>
					
					</td>
					
					<td width="36">Nome:</td>
					
					<td width="160">

			            <input type="text"
			            maxlength="255" 
			            name="nomeCota" 
			            id="nomeCota" 
			            onkeyup="pesquisaCotaConsultaEncalhe.autoCompletarPorNome('#nomeCota');" 
			            onblur="pesquisaCotaConsultaEncalhe.pesquisarPorNomeCota('#cota', '#nomeCota');" 
			            style="width:160px;"/>
						
					</td>
					
					<td width="104">
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="ConsultaEncalhe.pesquisar()">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>

		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
		
			<legend>Encalhe</legend>
			
			<div class="grids" style="display: none;">
			
				<table id="gridConsultaEncalhe"></table>
				
				<table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="280" valign="top">
						
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/devolucao/consultaEncalhe/exportar?fileType=XLS">
									<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a> 
							</span> 
							
							<span class="bt_novos" title="Imprimir"> 
								<a href="${pageContext.request.contextPath}/devolucao/consultaEncalhe/exportar?fileType=PDF">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
									Imprimir 
								</a>
							</span> 
						
						</td>

						<td width="190">&nbsp;&nbsp;&nbsp;&nbsp;</td>

						<td width="226">

							<fieldset class="box_field">
								<legend>Primeiro Recolhimento</legend>
								<table width="200" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="8">&nbsp;</td>
										<td width="178">
											<div class="box_resumo">
												<table width="150" border="0" cellspacing="1"
													cellpadding="1">
													
													<tr>
														<td width="83" height="23">
															<strong>Produtos:</strong>
														</td>
														<td width="60">
															<input id="qtdProdutoPrimeiroRecolhimento"
															disabled="disabled" type="text" style="width: 60px;" />
														</td>
													</tr>
													<tr>
														<td>
															<strong>Exemplares:</strong>
														</td>
														<td>
															<input id="qtdExemplarPrimeiroRecolhimento" 
															disabled="disabled" type="text" style="width: 60px;" />
														</td>
													</tr>
												</table>
											</div></td>
									</tr>
								</table>



							</fieldset>
						</td>
						
						<td width="10">&nbsp;&nbsp;</td>
						
						<td width="228">
						
							<fieldset class="box_field">
							
								<legend>Demais Recolhimento</legend>
							
								<table width="200" border="0" cellspacing="2" cellpadding="2">
								
									<tr>
										<td width="8">&nbsp;</td>
										<td width="178">
											<div class="box_resumo">
												<table width="150" border="0" cellspacing="1"
													cellpadding="1">
													
													<tr>
														<td width="83" height="23">
															<strong>Produtos:</strong>
														</td>
														<td width="60">
															<input id="qtdProdutoDemaisRecolhimentos" 
															disabled="disabled" type="text" style="width: 60px;" />
														</td>
													</tr>
													<tr>
														<td>
															<strong>Exemplares:</strong>
														</td>
														<td>
															<input id="qtdExemplarDemaisRecolhimentos" 
															disabled="disabled" type="text" style="width: 60px;" />
														</td>
													</tr>
												</table>
											</div>
										</td>
										
									</tr>
									
								</table>

							</fieldset>

						</td>

					</tr>
				
				</table>

			</div>

		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>
</body>