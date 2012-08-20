<head>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
			
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript">
	
		var pesquisaProdutoConsultaFaltasSobras = new PesquisaProduto();
	
		$(function() {			
			
			$('input[id^="data"]').datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="data"]').mask("99/99/9999");
			
			$("#edicao").numeric();
			
			$("#produto").autocomplete({source: ""});
		});
		
		$(function() {
			$(".consultaFaltasSobrasGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'Data',
					name : 'dataLancamento',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
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
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desconto R$',
					name : 'precoDesconto',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Tipo de Diferença',
					name : 'tipoDiferenca',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nota',
					name : 'numeroNotaFiscal',
					width : 104,
					sortable : true,
					align : 'left'
				}, {
					display : 'Exemplar',
					name : 'quantidade',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : 'Status',
					name : 'statusAprovacao',
					width : 45,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'valorTotalDiferenca',
					width : 50,
					sortable : true,
					align : 'right'
				} ],
				sortname : "dataLancamentoNumeroEdicao",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		});
		
		$(function() {
			$("#codigo").focus();
		});
		
		function pesquisarProdutosSuccessCallBack() {
			pesquisarFornecedores();
		}
		
		function pesquisarProdutosErrorCallBack() {
			pesquisarFornecedores();
		}
		
		function pesquisarFornecedores() {
			var data = "codigoProduto=" + $("#codigo").val();
			
			$.postJSON("${pageContext.request.contextPath}/estoque/diferenca/pesquisarFonecedores",
					   data, montarComboFornecedores);
		}
		
		function montarComboFornecedores(result) {
			var comboFornecedores =  montarComboBox(result, true);
			
			$("#fornecedor").html(comboFornecedores);
		}
		
		function pesquisar() {
			var codigoProduto = $("#codigo").val();
			var numeroEdicao = $("#edicao").val();
			var idFornecedor = $("#fornecedor").val();
			var dataInicial = $("#dataInicial").val();
			var dataFinal = $("#dataFinal").val();
			var tipoDiferenca = $("#tipoDiferenca").val();
			
			$(".consultaFaltasSobrasGrid").flexOptions({
				url: "${pageContext.request.contextPath}/estoque/diferenca/pesquisarDiferencas",
				onSuccess: executarAposProcessamento,
				params: [
				         {name:'codigoProduto', value:codigoProduto},
				         {name:'numeroEdicao', value:numeroEdicao},
				         {name:'idFornecedor', value:idFornecedor},
				         {name:'dataInicial', value:dataInicial},
				         {name:'dataFinal', value:dataFinal},
				         {name:'tipoDiferenca', value:tipoDiferenca}
				        ] ,
		        newp: 1
			});
			
			$(".consultaFaltasSobrasGrid").flexReload();
		}
		
		function executarAposProcessamento() {
			$("span[name='statusAprovacao']").tooltip();
		}
		
		function executarPreProcessamento(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return resultado.tableModel;
			}
			
			$("#qtdeTotalDiferencas").html(resultado.qtdeTotalDiferencas);
			
			$("#valorTotalDiferencas").html(resultado.valorTotalDiferencas);
			
			$.each(resultado.tableModel.rows, function(index, row) {
				
				if (row.cell.motivoAprovacao) {
				
					var spanAprovacao = "<span name='statusAprovacao' title='" + row.cell.motivoAprovacao + "'>"
										+ row.cell.statusAprovacao + "</span>";
					
					row.cell.statusAprovacao = spanAprovacao;
				}
			});
				
			$(".grids").show();
			
			return resultado.tableModel;
		}
				
	</script>
</head>

<body>
	<fieldset class="classFieldset">
		<legend>Pesquisar Faltas e Sobras</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td id="teste" width="59" title="tooltip teste">Código:</td>
				<td colspan="3">
					<input type="text" name="codigo" id="codigo"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="pesquisaProdutoConsultaFaltasSobras.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', false,
								   									   pesquisarProdutosSuccessCallBack,
								   									   pesquisarProdutosErrorCallBack);" />
					
				</td>
				<td width="60">Produto:</td>
				<td width="220">
					<input type="text" name="produto" id="produto" style="width: 200px;" maxlength="255"
					       onkeyup="pesquisaProdutoConsultaFaltasSobras.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="pesquisaProdutoConsultaFaltasSobras.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', false,
					       											  pesquisarProdutosSuccessCallBack,
					       											  pesquisarProdutosErrorCallBack);"/>
				</td>
				
				<td width="50" align="right">Edição:</td>
				<td width="90">
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
						   onchange="pesquisaProdutoConsultaFaltasSobras.validarNumEdicao('#codigo', '#edicao', false);"/>
				</td>
				
				<td width="73">Fornecedor:</td>
				<td width="230" colspan="2">
					<select name="fornecedor" id="fornecedor" style="width: 200px;">
						<option selected="selected" value="">Todos</option>
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
				<td width="100">Período de Data:</td>
				<td width="108">
					<input type="text" name="dataInicial" id="dataInicial" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="33" align="center">Até</td>
				<td width="147">
					<input type="text" name="dataFinal" id="dataFinal" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="134" align="right">Tipo de Diferença:</td>
				<td width="169">
					<select name="tipoDiferenca" id="tipoDiferenca" style="width: 120px;">
						<option selected="selected" value="">Todos</option>
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
				</td>
				<td width="137">
					<span class="bt_pesquisar" title="Pesquisar">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
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
							<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
							</a>
						</span>
						<span class="bt_novos" title="Imprimir">
							<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
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