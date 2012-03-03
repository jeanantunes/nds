<head>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/produto.js"></script>

	<script type="text/javascript">
	
		$(function() {
			$('input[id^="dataLancamento"]').datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="dataLancamento"]').mask("99/99/9999");
			$("#edicao").mask("?99999999999999999999", {placeholder:""});
		});
		
		$(function() {
			$(".consultaFaltasSobrasGrid").flexigrid({
				preProcess: getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Data',
					name : 'dataLancamento',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Código',
					name : 'codigoProduto',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Produto',
					name : 'descricaoProduto',
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
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Tipo de Diferença',
					name : 'tipoDiferenca',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nota',
					name : 'numeroNotaFiscal',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Exemplar',
					name : 'quantidade',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Status',
					name : 'statusAprovacao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'valorTotalDiferenca',
					width : 60,
					sortable : false,
					align : 'right'
				} ],
				sortname : "dataLancamento",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		});
		
		function mostrarGridConsulta() {
			var codigoProduto = $("#codigo").val();
			var numeroEdicao = $("#edicao").val();
			var idFornecedor = $("#fornecedor").val();
			var dataLancamentoDe = $("#dataLancamentoDe").val();
			var dataLancamentoAte = $("#dataLancamentoAte").val();
			var tipoDiferenca = $("#tipoDiferenca").val();
			
			$(".consultaFaltasSobrasGrid").flexOptions({
				url: "<c:url value='/estoque/diferenca/pesquisarDiferencas' />",
				params: [
				         {name:'codigoProduto', value:codigoProduto},
				         {name:'numeroEdicao', value:numeroEdicao},
				         {name:'idFornecedor', value:idFornecedor},
				         {name:'dataLancamentoDe', value:dataLancamentoDe},
				         {name:'dataLancamentoAte', value:dataLancamentoAte},
				         {name:'tipoDiferenca', value:tipoDiferenca}
				        ] ,
			});
			
			$(".consultaFaltasSobrasGrid").flexReload();
			
			$(".grids").show();
		}
		
		function getDataFromResult(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();
				//$("#btnConfirmar").hide();
				//$("#labelTotalGeral").hide();

				return resultado.tableModel;
			}
			
			$("#qtdeTotalDiferencas").html(resultado.qtdeTotalDiferencas);
			
			$("#valorTotalDiferencas").html(resultado.valorTotalDiferencas);
			

			if ($(".grids").css('display') == 'none') {	

				$(".grids").show();
				//$("#btnConfirmar").show();
				//$("#labelTotalGeral").show();
			}
			
			return resultado.tableModel;
		}
		
	</script>
</head>

<body>
	<fieldset class="classFieldset">
		<legend>Pesquisar Faltas e Sobras</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="59">Código:</td>
				<td colspan="3">
					<input type="text" name="codigo" id="codigo" style="width: 80px; float: left; margin-right: 5px;" />
					<span class="classPesquisar" title="Pesquisar Produto">
						<a href="javascript:;" onclick="pesquisarPorCodigoProduto();">&nbsp;</a>
					</span>
				</td>
				<td width="60">Produto:</td>
				<td width="220">
					<input type="text" name="produto" id="produto" style="width: 200px;"
					       onkeyup="pesquisarPorNomeProduto();" />
				</td>
				
				<td width="50" align="right">Edição:</td>
				<td width="90">
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
						   onchange="validarNumEdicao();"/>
				</td>
				
				<td width="73">Fornecedor:</td>
				<td width="230" colspan="2">
					<select name="fornecedor" id="fornecedor" style="width: 200px;">
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<table width="950" border="0" cellspacing="2" cellpadding="2"
			class="filtro">
			<tr>
				<td width="178">Período de Data Lançamento:</td>
				<td width="108">
					<input type="text" name="dataLancamentoDe" id="dataLancamentoDe" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="33" align="center">Até</td>
				<td width="147">
					<input type="text" name="dataLancamentoAte" id="dataLancamentoAte" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="134" align="right">Tipo de Diferença:</td>
				<td width="169">
					<select name="tipoDiferenca" id="tipoDiferenca" style="width: 120px;">
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
				</td>
				<td width="137"><span class="bt_pesquisar"><a
						href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a> </span></td>
			</tr>
		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Faltas e Sobras Cadastradas</legend>
		<div class="grids" style="display: none;">
			<table class="consultaFaltasSobrasGrid"></table>
			
			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="70%">
						<span class="bt_novos" title="Gerar Arquivo">
							<a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
							</a>
						</span>
						<span class="bt_novos" title="Imprimir">
							<a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
								Imprimir
							</a>
						</span>
					</td>
					<td width="5%"><strong>Total:</strong></td>
				    <td id="qtdeTotalDiferencas" width="7%" align="right" />
				    <td id="valorTotalDiferencas" width="16%" align="right" />
				    <td width="2%">&nbsp;</td>
				</tr>
			</table>
		</div>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
</body>