<head>
<title>Consulta de Notas</title>
<script language="javascript" type="text/javascript">
	
		function processarResultadoConsultaNF(data) {
			
			if ($("#datepickerDe").val() == "" && $("#datepickerAte").val() == "") {

				var dataAtual = $.format.date(new Date(), "dd/MM/yyyy");

				$("#datepickerDe").val(dataAtual);
				$("#datepickerAte").val(dataAtual);
			} 

			if (data.mensagens) {

				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);

				$(".grids").hide();

				return;
			}

			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length - 1;
				console.log(data.rows);
				data.rows[i].cell[lastIndex-1] = 
					'<a href="javascript:;" onclick="pesquisarDetalhesNota(' + data.rows[i].cell[lastIndex] + ')" ' +
					' style="cursor:pointer;border:0px" title="Visualizar Detalhes">' +
					'<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" border="0px"/>' +
					'</a>';
			}

			if ($(".grids").css('display') == 'none') {
					
				$(".grids").show();
			}

			return data;
		} 

		function pesquisarNotas() { 
			
			var formData = $('#formPesquisaNotas').serializeArray();
			
			$("#notasSemFisicoGrid").flexOptions({
				url : '<c:url value="/estoque/consultaNotas/pesquisarNotas" />',
				params: formData,
				newp: 1
			});

			$("#notasSemFisicoGrid").flexReload();
		}
		
		function pesquisarDetalhesNota(idNota) {

			$("#notasSemFisicoDetalheGrid").flexigrid({
				url : '<c:url value="/estoque/consultaNotas/pesquisarDetalhesNotaFiscal" />',
				preProcess: montarGridComRodape,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoItem',
					width : 40,
					sortable : true,
					align : 'left'
				},{
					display : 'Produto',
					name : 'nomeProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço de Venda R$',
					name : 'precoCapa',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Exemplares',
					name : 'quantidadeExemplares',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Sobras / Faltas',
					name : 'sobrasFaltas',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 60,
					sortable : true,
					align : 'right'
				}],
				width : 600,
				height : 200,
				params: [{ name: 'idNota', value: idNota }],
				resizable:false
			});
			
			$("#notasSemFisicoDetalheGrid").flexOptions({url : '<c:url value="/estoque/consultaNotas/pesquisarDetalhesNotaFiscal" />', 
				params: [{ name: 'idNota', value: idNota }]});

			$("#notasSemFisicoDetalheGrid").flexReload();
		}
		
		function montarGridComRodape(data) {

			if (data.mensagens) {
				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);

				return;
			}
			
			var jsonData = jQuery.toJSON(data);

			var result = jQuery.evalJSON(jsonData);

			$("#totalExemplares").html(result.totalExemplares);
			$("#totalSumarizado").html("R$ " + result.totalSumarizado);

			popup();
			
			return result.tableModel;
		}
		
		function popup() {

			$("#dialog-novo").dialog({
				resizable: false,
				height:370,
				width:630,
				modal : true,
				buttons : {
					"Fechar" : function() {
						$(this).dialog("close");
					},
				}
			});
		};

		$("#btnPesquisar").keypress(function(event) {
			
			var keynum;  
	          
	        if(window.event) {   
	        	
	            keynum = event.keyCode  
	        
	        } else if(event.which) {   
	            
	        	keynum = event.which  
	        }
	        
			if (keynum == 13) {
				pesquisarNotas();
			}
		});
		
		$(function() {
			
			$("#notasSemFisicoGrid").flexigrid({
				preProcess: processarResultadoConsultaNF,
				dataType : 'json',
				colModel : [ {
					display : 'Número da Nota',
					name : 'numero',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data de Emissão',
					name : 'dataEmissao',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data de Expedição',
					name : 'dataExpedicao',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo',
					name : 'descricao',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'razaoSocial',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'valor',
					width : 80,
					sortable : true,
					align : 'right'
				},{
					display : 'Nota Recebida',
					name : 'statusNotaFiscal',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : "Ação",
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				sortname : "numero, dataEmissao",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180,
				singleSelect: true
			});
			
			$("#datepickerDe").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});
			
			$("#datepickerDe").mask("99/99/9999");
	
			$("#datepickerAte").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});
			
			$("#datepickerAte").mask("99/99/9999");
		});
	</script>

<style type="text/css">
fieldset label 
{
	width: auto;
	margin-bottom: 0px !important;
}
</style>

</head>

<body>
	<div id="dialog-novo" title="Detalhes da Nota">
	    <table id="notasSemFisicoDetalheGrid" class="notasSemFisicoDetalheGrid"></table>
		<br />

		<table width="569" border="0" cellspacing="2" cellpadding="2">
	      <tr style="font-size:11px;">
	        <td width="275" align="right"><strong>Total:</strong></td>
	        <td width="106" align="right">
	        	<span id="totalExemplares"></span>
	        </td>
	        <td width="168" align="right"> 
	        	<span id="totalSumarizado"></span>
	        </td>
	      </tr>
	    </table>
	</div>
	
	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>

		<form name="formPesquisaNotas" id="formPesquisaNotas">
			
			<fieldset class="classFieldset">
				<legend> Pesquisar Nota </legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					   class="filtro">
					<tr>
						<td>Fornecedor:</td>
						<td>
							<select name="filtroConsultaNotaFiscal.idFornecedor" id="selectFornecedores" style="width: 250px;">
								<option selected="selected" value="-1"></option>
								<c:forEach items="${fornecedores}" var="fornecedor">
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
							</select>
						</td>
	
						<td>Período</td>
						<td width="46">de:</td>
						<td width="120">
							<input name="dataInicial" type="text" id="datepickerDe"
								   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
						<td align="center">Até</td>
						<td>
							<input name="dataFinal" type="text" id="datepickerAte"
							 	   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
					</tr>
					<tr>
						<td width="107">Tipo de Nota:</td>
						<td width="293">
						<select name="filtroConsultaNotaFiscal.idTipoNotaFiscal" id="selectTiposNotaFiscal" style="width: 250px;">
							<option selected="selected" value="-1"></option>
							<c:forEach items="${tiposNotaFiscal}" var="tipoNotaFiscal">
								<option value="${tipoNotaFiscal.id}">${tipoNotaFiscal.descricao}</option>
							</c:forEach>
							
						</select></td>
						<td width="95"><label for="notaRecebida" style="margin:0px">Nota Recebida</label></td>
						<td colspan="2">
						<select name="isNotaRecebida" id="selectNotaRecebida"
								style="width: 135px;">
								<option value="-1"></option>
								<option value="1">Sim</option>
								<option value="0">Não</option>
						</select></td>
						<td width="31">&nbsp;</td>
						<td width="222">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="pesquisarNotas()" id="btnPesquisar">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Notas Cadastradas</legend>
			<div class="grids" style="display: none;">
				<table class="notasSemFisicoGrid" id="notasSemFisicoGrid"></table>
				<span class="bt_arquivo">
					<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=XLS">
						Arquivo
					</a>
				</span>
				<span class="bt_imprimir">
					<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=PDF">
						Imprimir
					</a>
				</span>
			</div>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
	
</body>