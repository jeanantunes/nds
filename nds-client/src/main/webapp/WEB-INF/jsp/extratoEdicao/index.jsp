<!-- 

	VIEW REFERENTE A EMS0081
	
 -->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript">



var jsExtratoEdicao = {

	pesquisarExtratoEdicao : function() {

		
		var codigoProduto = jQuery("#edicao").attr('value');
		var descProduto = jQuery("#edicao").attr('value');
		var idProdutoEdicao = jQuery("#edicao").attr('value');
		
		$(".extratoEdicaoGrid").flexOptions({
			
			url: '<c:url value="/estoque/extratoEdicao/pesquisaExtratoEdicao"/>',
			
			params:[{name:'param1', value: paramBuscaExtatoEdicao1},
				    {name:'param2', value: paramBuscaExtatoEdicao2},
				    {name:'param3', value: paramBuscaExtatoEdicao3}],
		});
		
		$(".extratoEdicaoGrid").flexReload();
		
		$(".grids").show();
	},	
		
	fazerTratamento : function(data) {
		
		//TODO usar se necessario...
		return data;
	},

	pesquisarPorNomeProduto : function(){
		
		var produto = $("#produto").val();
		
		if (produto && produto.length > 0){
			$.postJSON("<c:url value='/lancamento/furoProduto/pesquisarPorNomeProduto'/>", "nomeProduto=" + produto, jsExtratoEdicao.exibirAutoComplete);
		}
	},
	
	exibirAutoComplete : function (result){
		
		$("#produto").autocomplete({
			source: result,
			select: function(event, ui){
				jsExtratoEdicao.completarPesquisa(ui.item.chave);
			}
		});
		
	},
	
	completarPesquisa : function(chave){
		$("#codigo").val(chave.codigoProduto);
		$("#edicao").focus();
	},
	
	
	
	carregarExtratoEdicaoGrid: function() {
		
		$(".extratoEdicaoGrid")
				.flexigrid(
						{
							dataType : 'json',
							colModel : [ 
							    {
								display : 'ID',
								name : 'id',
								width : 120,
								sortable : true,
								align : 'center'
							}, {
								display : 'Data',
								name : 'dataInclusao',
								width : 370,
								sortable : true,
								align : 'left'
							}, {
								display : 'Entrada',
								name : 'entrada',
								width : 120,
								sortable : true,
								align : 'center'
							}, {
								display : 'Saída',
								name : 'saida',
								width : 120,
								sortable : true,
								align : 'center'
							}, {
								display : 'Parcial',
								name : 'parcial',
								width : 120,
								sortable : true,
								align : 'center'
							} ],
							sortname : "codigo",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 15,
							showTableToggleBtn : true,
							width : 960,
							height : 180
						});
		

	}
	
};

$(function() {
	
	$("#produto").autocomplete({
		source: ''
	});
	
	jsExtratoEdicao.carregarExtratoEdicaoGrid();
	
});

</script>

</head>

<body>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info"></span> <b>Chamadão < evento > com
					< status >.</b>
			</p>
		</div>

		<fieldset class="classFieldset">
		
			<legend> Extrato de Edição </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				
				<tr>
					<td width="96">
						Código:
					</td>
					
					<td width="123">
					
						<input 	type="text" 
								name="codigoProduto" 
								id="codigoProduto"
								maxlength="5"
								style="width: 70px; 
								float: left; 
								margin-right: 5px;" />

						<span class="classPesquisar">
							<a href="javascript:;">&nbsp;</a>
						</span>
					
					</td>
					
					<td width="85">
						Produto:
					</td>
					
					<td width="236">

						<input 	type="text" 
								name="nomeProduto" 
								id="nomeProduto" 
								style="width: 220px;" 
								maxlength="255" 
								onkeyup="jsExtratoEdicao.pesquisarPorNomeProduto();"/>
					
					</td>
					
					<td width="60">
						Edição:
					</td>
					
					<td width="186">
					
						<select name="selectEdicoes" id="selectEdicoes" style="width: 250px;">
						
							<option selected="selected">Todos</option>
							
							<c:forEach items="${edicoes}" var="edicao">
								<option value="${edicao.id}">${edicao.juridica.razaoSocial}</option>
							</c:forEach>
							
						</select>

								
					</td>
					
					<td width="128">&nbsp;</td>
				
				</tr>
				<tr>
					<td>Preço da Capa:</td>
					<td><input type="text" name="datepickerDe3" id="datepickerDe3"
						style="width: 70px; float: left; margin-right: 5px;"
						disabled="disabled" />
					</td>
					<td>Fornecedor:</td>
					<td><input type="text" name="textfield" id="textfield"
						style="width: 220px;" disabled="disabled" />
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="jsExtratoEdicao.pesquisarExtratoEdicao();">Pesquisar</a>
					</span>
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Extrato de Edição</legend>
			<div class="grids" style="display: none;">
				<table class="extratoEdicaoGrid"></table>
				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="689"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a>
						</span> <span class="bt_novos" title="Imprimir"><a
								href="javascript:;"><img src="../images/ico_impressora.gif"
									alt="Imprimir" hspace="5" border="0" />Imprimir</a>
						</span>
						</td>
						<td width="115"><strong>Saldo em Estoque:</strong>
						</td>
						<td width="126">0</td>
					</tr>
				</table>
			</div>


		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>

</html>