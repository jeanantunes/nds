<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Painel Monitor NFe</title>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">

var PainelMonitorNFE = {
		
		pesquisar: function() {
			
			var box = $("#box").val();
			var dataInicial = $("#dataInicial").val();
			var dataFinal = $("#dataFinal").val();
			var tipoDocumento = $('input:radio[name=radioTipoDoc]:checked').val();
			var documento = $("#documento").val();
			var tipoNfe = $("#tipoNfe").val();
			var numeroInicial = $("#numeroInicial").val();
			var numeroFinal = $("#numeroFinal").val();
			var chaveAcesso = $("#chaveAcesso").val();
			var situacaoNfe = $("#situacaoNfe").val();
			
			var formData = [
			        {name:'box', value: box },
			        {name:'dataInicial', value: dataInicial },
			        {name:'dataFinal', value: dataFinal },
			        {name:'tipoDocumento', value: tipoDocumento },
			        {name:'documento', value: documento },
			        {name:'tipoNfe', value: tipoNfe },
			        {name:'numeroInicial', value: numeroInicial },
			        {name:'numeroFinal', value: numeroFinal },
			        {name:'chaveAcesso', value: chaveAcesso },
			        {name:'situacaoNfe', value: situacaoNfe }
			];
			
			
			$("#nfeGrid").flexOptions({
				url: "<c:url value='/nfe/painelMonitorNFe/pesquisar'/>",
				params: formData
			});
			
			$("#nfeGrid").flexReload();
			

		},
	
		
		limparCheck:function (id){
			
			$('#'+id).attr("checked",false);	
		},
		
		checkAll: function (todos) {
			
			if(todos.checked == false) {
				
				PainelMonitorNFE.limparAll();
			}		
			else {										
				
				PainelMonitorNFE.selecionarAll();
			}	
		},
		
		selecionarAll: function(){
			
			var linhasDaGrid = $("#nfeGrid tr");
			
			$.each(linhasDaGrid, function(index, value) {
	
				var linha = $(value);
				
				var colunaSelecao = linha.find("td")[10];
				
				$(colunaSelecao).find("div").find('input[name="checkgroup"]').attr("checked",true);
				
			});
		},
		
		limparAll: function (){
			
			var linhasDaGrid = $("#nfeGrid tr");
			
			$.each(linhasDaGrid, function(index, value) {
	
				var linha = $(value);
	
				var colunaSelecao = linha.find("td")[10];
				
				$(colunaSelecao).find("div").find('input[name="checkgroup"]').attr("checked",false);
				
			});
		},
		
		executarPreProcessamento: function(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return {total: 0, rows: {}};
			}
			
			$.each(resultado.rows, function(index, value){
				
				var hiddenField = '<input type="hidden" name="lineId" value="'+value.id+'" />';
				
				value.cell.imprimirLinha = '<a href="javascript:;" onclick="PainelMonitorNFE.imprimirDanfeUnica('+value.id+')">'+
				'<img title="Imprimir" src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0"/></a>';
				
				value.cell.sel = '<input type="checkbox" name="checkgroup" style="float: left; margin-right: 25px;"/>'+hiddenField;
				
			});
			
			$('.grids').show();
			
			return resultado;
			
		},
		
		imprimirDanfes : function() {
			
			var nomeLista = 'listaLineIdsImpressaoDanfes';
			
			var selecionados = '';
			
			var linhasDaGrid = $("#nfeGrid tr");
			
			var contador = 0;
			
			$.each(linhasDaGrid, function(index, value) {
				
				var linha = $(value);
				
				var colunaSelecao = linha.find("td")[10];
			
				var inputSelecao = $(colunaSelecao).find("div").find('input[name="checkgroup"]');
				
				var lineId = $(colunaSelecao).find("div").find('input[name="lineId"]').val();
				
				if(inputSelecao.attr('checked')) {
					
					var indiceAtual = '['+contador+']=';
					
					if(selecionados == '') {
						selecionados = ( nomeLista + indiceAtual + lineId )  ;
					} else {
						selecionados = ( selecionados + '&' + nomeLista + indiceAtual + lineId )  ;
					}
					
					contador = (contador + 1);
				}
				
			});
			
			$.postJSON("<c:url value='/nfe/painelMonitorNFe/prepararDanfesImpressao'/>", selecionados, 
				function(){
				window.location ="<c:url value='/nfe/painelMonitorNFe/imprimirDanfes'/>" ;
			});
			
		},
		
		imprimirDanfeUnica : function(lineId) {
			
			var params = 'lineIdImpressaoDanfe='+lineId;
			
			$.postJSON("<c:url value='/nfe/painelMonitorNFe/prepararDanfeUnicaImpressao'/>",
				params,
				function(){
					window.location ="<c:url value='/nfe/painelMonitorNFe/imprimirDanfes'/>" ;
				}		
			);
			
		},
		
		obterColModel : function() {


				var colModel = [ {
					display : 'Nota',
					name : 'numero',
					width : 55,
					sortable : true,
					align : 'left'
				}, {
					display : 'Série',
					name : 'serie',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Emissão',
					name : 'emissao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo Emissão',
					name : 'tipoEmissao',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ Destinatário',
					name : 'cnpjDestinatario',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'CPF Destinatário',
					name : 'cpfDestinatario',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ Remetente',
					name : 'cnpjRemetente',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'CPF Remetente',
					name : 'cpfRemetente',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status NF-e',
					name : 'statusNfe',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo NF-e',
					name : 'tipoNfe',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Movimento Integração',
					name : 'movimentoIntegracao',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : ' ',
					name : 'imprimirLinha',
					width : 30,
					sortable : true,
					align : 'center'
				}, {
					display : ' ',
					name : 'sel',
					width : 30,
					sortable : true,
					align : 'center'
				} ];	
				
				return colModel;
		}
	
		

		
}
	
	
$(function() {
	
	var colunas = PainelMonitorNFE.obterColModel();
	
	$("#nfeGrid").flexigrid({
		colModel : colunas,
		preProcess: PainelMonitorNFE.executarPreProcessamento,
		dataType : 'json',
		sortname : "codigo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
	
	$('#dataInicial').datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('#dataInicial').mask("99/99/9999");	

	
	$('#dataFinal').datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('#dataFinal').mask("99/99/9999");	

	
	
});
	
</script>

</head>

<body>

	<br clear="all" />
	
	<br />

	<div class="container">

		<fieldset class="classFieldset">
			
			<legend> Painel Monitor NF-e</legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			
				<tr>
				
					<td width="94">Box:</td>

					<td width="129">
					
						<input type="text" id="box" style="width: 80px;" />

					</td>

					<td width="68">Período de:</td>

					<td width="107"><input type="text"
						id="dataInicial" style="width: 80px;" />
					</td>

					<td width="29">Até:</td>

					<td width="107"><input type="text"
						id="dataFinal" style="width: 80px;" />
					</td>

					<td colspan="3">Destinatário:</td>

					<td width="135">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="15%">
									<input type="radio" name="radioTipoDoc" value="cpf" />
								</td>
								<td width="34%">
									<label for="cpf">CPF</label>
								</td>
								
								<td width="15%">
									<input type="radio" name="radioTipoDoc" checked="checked" value="cnpj" />
								</td>
								
								<td width="36%">
									<label for="cnpj">CNPJ</label>
								</td>
							</tr>
						</table>
					</td>
					<td width="160">
						<input type="text" id="documento" style="width: 160px;" />
					
				</tr>
				<tr>
					<td>Tipo de Nf-e:</td>
					<td>	
						<select name="tipoNfe" id="tipoNfe" style="width: 120px;">
							<option selected="selected"></option>
							<option>Todos</option>
							<option>Entrada</option>
							<option>Saída</option>
						</select>
					</td>
					<td>Número:</td>
					<td>
						<input type="text" id="numeroInicial" style="width: 80px;" />
					</td>
					<td>Até:</td>
					<td>
						<input type="text" id="numeroFinal" style="width: 80px;" />
					</td>
					<td colspan="3">&nbsp;</td>
					<td>
						Chave de Acesso NF-e:
					</td>
					<td>
						<input type="text" id="chaveAcesso" style="width: 160px;" />
				</tr>
				<tr>
					<td>Situação NF-e:</td>
					<td colspan="3">
					
					
						<select name="situacaoNfe" id="situacaoNfe" style="width:290px;">
						    <option value=""  selected="selected"></option>
						    <c:forEach items="${comboStatusNfe}" var="statusNfe">
						      		<option value="${statusNfe.key}">${statusNfe.value}</option>	
						    </c:forEach>
					    </select>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="PainelMonitorNFE.pesquisar();">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>
		
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>NF-e</legend>
			<div class="grids" style="display: none;">
				<table id="nfeGrid"></table>
				<!--<span class="bt_novos" title="Gerar Arquivo XML"><a href="javascript:;"><img src="../images/ico_xml.gif" hspace="5" border="0" />XML</a></span>-->
				
				<span class="bt_novos" title="Gerar Arquivo">
					<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=XLS">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a> 
				</span> 
				
				<span class="bt_novos" title="Imprimir"> 
					<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=PDF">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
						Imprimir 
					</a>
				</span>				
				
				<span class="bt_novos" title="Imprimir Seleção">
					<a 	onclick="PainelMonitorNFE.imprimirDanfes()"	href="javascript:;">
						<img 	src="${pageContext.request.contextPath}/images/ico_impressora.gif"
								alt="Imprimir Seleção" 
								hspace="5" 
								border="0" />
						Imprimir Seleção
					</a>
				</span> 
				
				<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>
					<input 	type="checkbox" id="sel" name="Todos" 
							onclick="PainelMonitorNFE.checkAll(this);"
							style="float: left; margin-right: 25px;" />
				</span>
				
			</div>


		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

	</div>


</body>