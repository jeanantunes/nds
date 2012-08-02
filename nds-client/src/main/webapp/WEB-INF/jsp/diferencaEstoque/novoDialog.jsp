<div id="dialogNovasDiferencas" 
	 title="Lançamento Faltas e Sobras - Produto"
	 style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />
	
	<fieldset style="width:585px;">
   		<legend>Tipo de Diferença</legend>
		
		<table width="575" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td width="122">Tipo de Diferença:</td>
    			<td width="239">
					<select id="tipoDiferenca" style="width:200px;">
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
    			</td>
    			<td width="20">
    				<input type="checkbox" name="checkbox" id="checkboxLancCota" onclick="lanctoPorCotaProduto();" />
    			</td>
    			<td width="181">Lançamento por Cota</td>
  			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
	
	<div class="prodSemCota" style="display:block;">
    	
    	<fieldset style="width:585px!important;">
    		<legend>Produto</legend>
    		<table class="lanctoFaltasSobras_2Grid" border="0" cellspacing="1" cellpadding="1" style="width:585px;">
	    		<tr>
					<td width="64" bgcolor="#F5F5F5">
						<strong>Código</strong>
					</td>
					<td width="180" bgcolor="#F5F5F5">
						<strong>Produto</strong>
					</td>
					<td width="96" align="center" bgcolor="#F5F5F5">
						<strong>Edição</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Preço Capa R$</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Reparte</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Diferença</strong>
					</td>
				</tr>
				<tr>
					<td>
						<input type="text" id="codigoProdutoInput" style="width:60px;" 
							onblur="produto.pesquisarPorCodigoProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td>
						<input type="text" id="nomeProdutoInput" style="width:180px;" 
							onkeyup="produto.autoCompletarPorNomeProduto(nomeProdutoInput, true);"
							onblur="produto.pesquisarPorNomeProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td align="center">
						<input type="text" id="edicaoProdutoInput" onblur="buscarPrecoProdutoEdicao();" style="width:50px;" />
					</td>
					<td align="right" id="precoCapaProduto"></td>
					<td align="center" id="reparteProduto"></td>
					<td>
						<input id="diferencaProdutoInput" style="width: 60px; text-align: center;" />
					</td>
				</tr>
    		</table>
    	</fieldset> 
    	
    	<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
    	
     	<fieldset style="width:585px!important;">
    		<legend>Direcionar para:</legend>
        	
        	<table width="220" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="20"><input name="direcionar" type="radio" id="paraEstoque" onchange="paraEstoque(true);"/></td>
					<td width="72">Estoque</td>
					<td width="20"><input name="direcionar" type="radio" id="paraCota" onchange="paraEstoque(false);" /></td>
					<td width="95">Cota</td>
				</tr>
			</table>
     	</fieldset>
     	
		<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
		
		<div id="fieldCota" style="display:none;">
			
			<fieldset style="width:585px!important;">
				<legend>Cotas</legend>
				<div style="overflow: auto; height: 150px;">
	    			<table border="0" cellspacing="1" cellpadding="1" style="width:565px;" id="grid_1">
						<tr>
							<td width="64" bgcolor="#F5F5F5">
								<strong>Cota</strong>
							</td>
							<td width="180" bgcolor="#F5F5F5">
								<strong>Nome</strong>
							</td>
							<td width="96" align="center" bgcolor="#F5F5F5">
								<strong>Reparte</strong>
							</td>
							<td bgcolor="#F5F5F5" align="center">
								<strong>Diferença</strong>
							</td>
							<td bgcolor="#F5F5F5" align="center">
								<strong>Reparte Atual</strong>
							</td>
						</tr>
						<tr id="trCota1">
							<td>
								<input type="text" name="cotaInput" id="cotaInput1" style="width:60px;"
									onblur="cota.pesquisarPorNumeroCota(cotaInput1, nomeInput1, true, buscarReparteAtualCota(1));"/>
							</td>
							<td>
								<input type="text" name="nomeInput" id="nomeInput1" style="width:180px;" 
									onkeyup="cota.autoCompletarPorNome(nomeInput1);" 
									onblur="cota.pesquisarPorNomeCota(cotaInput1, nomeInput1, buscarReparteAtualCota(1));"/>
							</td>
							<td align="center" id="reparteText1"></td>
							<td align="center">
								<input type="text" name="diferencaInput" id="diferencaInput1" style="width:80px; text-align:center;" 
									onblur="adicionarLinhaCota(1);"
									onchange="calcularReparteAtual(1);"/>
							</td>
							<td id="reparteAtualText1" align="center"></td>
						</tr>
					</table>
				</div>
			</fieldset>
		</div>
	</div>

	<div class="prodComCota" style="display:none;">
    	
    	<fieldset style="width:585px!important;">
   	  		<legend>Cota</legend>
   	  		
    		<table width="579" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="73">Nota de Envio:</td>
					<td width="124">
						<input name="dateNotaEnvio" type="text" style="width:80px;" id="dateNotaEnvio" />
					</td>
					<td width="27">Cota:</td>
					<td width="118">
						<input type="text" style="width:80px; float:left; margin-right:5px;" id="cotaInputNota"
							onblur="cota.pesquisarPorNumeroCota(cotaInputNota, nomeCotaNota, true);" />
					</td>
					<td width="32">Nome:</td>
					<td width="167">
						<input type="text" style="width:160px;" id="nomeCotaNota"
							onkeyup="cota.autoCompletarPorNome(nomeCotaNota);" 
							onblur="cota.pesquisarPorNomeCota(cotaInputNota, nomeCotaNota);" />
					</td>
					<td width="16">
						<img src="${pageContext.request.contextPath}/images/ico_add.gif" width="16" height="16" alt="Incluir"
							onclick="pesquisarProdutosNota();"/>
					</td>
          		</tr>
        	</table>
		</fieldset>

	    <div class="linha_separa_fields" style="width:595px!important;">&nbsp;</div>
	    
	    <fieldset style="width:585px!important;">
			<legend>Produtos</legend>
			
			<div style="display: none;" id="divPesquisaProdutosNota">
	    		<table class="lanctoFaltasSobrasCota_3Grid"></table>
	    	</div>
		</fieldset>
		
		<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
	</div>

	<br />
	
	<script language="javascript" type="text/javascript">
		
		$(function(){
			$("#dateNotaEnvio").datepicker({
				showOn : "button",
				buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: "dd/mm/yy"
			});
			
			$(".lanctoFaltasSobrasCota_3Grid").flexigrid({
				preProcess: executarPreProcessamentoNovo,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : false,
					align : 'left'
				},{
					display : 'Produto',
					name : 'descricaoProduto',
					width : 90,
					sortable : false,
					align : 'left'
				},{
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoVenda',
					width : 75,
					sortable : false,
					align : 'right'
				}, {
					display : 'Reparte Total',
					name : 'reparte',
					width : 70,
					sortable : false,
					align : 'center'
				}, {
					display : 'Diferença',
					name : 'valorTotalDiferenca',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Reparte Atual',
					name : 'qtdeEstoqueAtual',
					width : 80,
					sortable : false,
					align : 'center'
				}],
				width : 585,
				height : 180,
				disableSelect: true
			});
		});
	
		function popupNovasDiferencas(idDiferenca) {
			
			$("#codigoProdutoInput").val("");
			$("#nomeProdutoInput").val("");
			$("#edicaoProdutoInput").val("");
			$("#precoCapaProduto").text("");
			$("#reparteProduto").text("");
			$("#diferencaProdutoInput").val("");
			$("#paraEstoque").check();
			
			$(".trCotas").remove();
			$("#cotaInput1").val("");
			$("#nomeInput1").val("");
			$("#reparteText1").text("");
			$("#diferencaInput1").val("");
			$("#reparteAtualText1").text("");
			$("#checkboxLancCota").uncheck();
			
			$(".prodSemCota").show();
			$(".prodComCota").hide();
			
			$("#dateNotaEnvio").val("");
			$("#cotaInputNota").val("");
			$("#nomeCotaNota").val("");
			
			$("#divPesquisaProdutosNota").hide();
			
			if (idDiferenca){
				
				$("#checkboxLancCota").attr("disabled", "disabled");
			} else {
				
				$("#checkboxLancCota").removeAttr("disabled");
			}
			
			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/rateio/buscarDiferenca' />", 
				[{name:"idDiferenca", value:idDiferenca}],
				function(result) {
					
					var readonly = false;
					
					if (result.codigoProduto){
						
						$("#codigoProdutoInput").val(result.codigoProduto);
						$("#codigoProdutoInput").attr("readonly", "readonly");
						
						readonly = true;
					}
					
					if (result.descricaoProduto){
						
						$("#nomeProdutoInput").val(result.descricaoProduto);
						
						if (readonly){
							$("#nomeProdutoInput").attr("readonly", "readonly");
						}
					}
					
					if (result.numeroEdicao && result.numeroEdicao){
						
						$("#edicaoProdutoInput").val(result.numeroEdicao);
						
						if (readonly){
							$("#edicaoProdutoInput").attr("readonly", "readonly");
						}
					}
					
					if (!result.precoVenda){
						
						$("#precoCapaProduto").text("0,00");
					} else {
						
						$("#precoCapaProduto").text(result.precoVenda);
					}
					
					if (result.qtde){
						
						$("#reparteProduto").text(parseInt(result.quantidade).toFixed(0));
					} else {
						
						$("#reparteProduto").text("0");
					}
					
					if (result.diferenca){
						
						$("#diferencaProdutoInput").val(result.diferenca);
					} else {
						
						$("#diferencaProdutoInput").val(0);
					}
				},
				null, 
				true
			);
			
			$("#dialogNovasDiferencas").dialog({
				resizable: false,
				height:570,
				width:640,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrarNovasDiferencas();
					},
					"Cancelar": function() {
						$("#gridNovasDiferencas").flexAddData({rows:[]});
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}
		
		function lanctoPorCotaProduto(){
			
			if ($(".prodComCota").css("display") == "block"){
				
				$(".prodComCota").hide();
			} else {
				
				$(".prodComCota").show();
				
				$("#ui-dialog-title-dialogNovasDiferencas").text("Lançamento Faltas e Sobras - Cota");
			}
			
			if ($(".prodSemCota").css("display") == "block"){
				
				$(".prodSemCota").hide();
			} else {
				
				$(".prodSemCota").show();
				
				$("#ui-dialog-title-dialogNovasDiferencas").text("Lançamento Faltas e Sobras - Produto");
			}
		}

		var ultimaLinhaPreenchida;

		function executarPreProcessamentoNovo(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);

				return resultado;
			}

			$.each(resultado.rows, function(index, row) {
				
				row.cell.codigoProduto = '<div name="codigoProdutoNota">'+ row.cell.codigoProduto +'</div>';
				
				row.cell.reparte = '<div id="reparte'+ index +'">'+ row.cell.quantidade +'</div>';
				row.cell.qtdeEstoqueAtual = '<div id="qtdTotal'+ index +'">'+ row.cell.quantidade +'</div>';
				
				row.cell.valorTotalDiferenca = 
					'<input type="text" name="diferencaProduto" style="width:50px; value="0" text-align: center; margin-right:10px;" maxlenght="255" '+
					' id="inputDiferencaProduto'+ index +'" onchange="alterarReparteAtual('+ index +');" />';
			});
			
			return resultado;
		}

		function cadastrarNovasDiferencas() {
			
			var tipoDiferenca = $("#tipoDiferenca").val();
			
			var lancamentoPorCota = $('#checkboxLancCota').attr('checked') ? true : false;
			
			//lançamento por produto
			var codigoProduto = $("#codigoProdutoInput").val();
			
			var edicaoProduto = $("#edicaoProdutoInput").val();
			
			var diferenca = $("#diferencaProdutoInput").val();
			
			var direcionadoParaEstoque = $('#paraEstoque').attr('checked') ? true : false;
			
			//lançamento por cota
			var dataNotaEnvio = $("#dateNotaEnvio").val();
			
			var numeroCota = $("#cotaInputNota").val();
			
			var data = [
					 {name: "tipoDiferenca", value: tipoDiferenca},
					 {name: "lancamentoPorCota", value: lancamentoPorCota},
					 {name: "codigoProduto", value: codigoProduto},
					 {name: "edicaoProduto", value: edicaoProduto},
					 {name: "diferenca", value: diferenca},
					 {name: "direcionadoParaEstoque", value: direcionadoParaEstoque},
					 {name: "dataNotaEnvio", value: dataNotaEnvio},
					 {name: "numeroCota", value: numeroCota},
					 
			 ];
			 
			var _numerosCotas = $("[name=cotaInput]");
			
			$.each(_numerosCotas, function(index, row) {
				
				if (row.value != ""){
					
					data.push({name: "listaNumeroCota", value: row.value});
				}
			});
			
			var _diferencas = $("[name=diferencaInput]");
			
			$.each(_diferencas, function(index, row) {
				
				if (row.value != ""){
					
					data.push({name: "diferencas", value: row.value});
				}
			});
			
			var _codigoProdutoProduto = $("[name=codigoProdutoNota]");
			
			$.each(_codigoProdutoProduto, function(index, row) {
				
				if (row.textContent != ""){
					
					data.push({name: "listaCodigoProduto", value: row.textContent});
				}
			});
			
			var _diferencasProdutosNota = $("[name=diferencaProduto]");
			
			$.each(_diferencasProdutosNota, function(index, row) {
				
				if (row.value != ""){
					
					data.push({name: "valorDiferencasProdNota", value: row.value});
				}
			});
			
			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/cadastrarNovasDiferencas' />", 
				data,
				function(result) {

					$("#gridLancamentos").flexOptions({
						url : '<c:url value="/estoque/diferenca/lancamento/pesquisa/novos" />',
						params: "dataMovimento=" + $("#datePickerDataMovimento").val() + "&" + tipoDiferenca
					});
					
					$("#gridLancamentos").flexReload();

					$("#dialogNovasDiferencas").dialog("close");
				},
				 
				true
			);
		}
		
		function tratarErroCadastroNovasDiferencas(jsonData) {

			if (!jsonData || !jsonData.mensagens) {

				return;
			}

			var dadosValidacao = jsonData.mensagens.dados;
			
			var linhasDaGrid = $(".gridNovasDiferencas tr");

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				if (dadosValidacao 
						&& ($.inArray(index, dadosValidacao) > -1)) {

					linha.removeClass('erow').addClass('linhaComErro');
					
				} else {

					linha.removeClass('linhaComErro');					
				}
			});
		}

		function obterDadosProduto(idCodigoProduto, idEdicaoProduto) {

			codigoProduto = $(idCodigoProduto).val();

			edicaoProduto = $(idEdicaoProduto).val();

			var data = "codigoProduto=" + codigoProduto
					 + "&numeroEdicao=" + edicaoProduto;
			
			$.postJSON(
				"<c:url value='/produto/obterProdutoEdicao' />", 
				data,
				function(produtoEdicao) {

					$("#precoVenda" + ultimaLinhaPreenchida).val(produtoEdicao.precoVenda);
					
					$("#precoVendaFormatado" + ultimaLinhaPreenchida).text(produtoEdicao.precoVenda);
					$("#precoVendaFormatado" + ultimaLinhaPreenchida).formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

					$("#totalPrecoVendaDiferencas").text(($("input[id^='precoVenda']").sum()));
					$("#totalPrecoVendaDiferencas").formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

					obterEstoqueProduto(produtoEdicao);
				},
				null, 
				true
			);
		}

		function obterEstoqueProduto(produtoEdicao) {

			if (!produtoEdicao) {

				return;
			}

			var data = "idProdutoEdicao=" + produtoEdicao.id;
			
			$.postJSON(
				"<c:url value='/produto/obterEstoque' />", 
				data,
				function(estoqueProduto) {
					
					if (estoqueProduto) {
						$("#qtdeRecebimentoFisico" + ultimaLinhaPreenchida).val(estoqueProduto.qtde);
						$("#qtdeRecebimentoFisicoFormatado" + ultimaLinhaPreenchida).text(estoqueProduto.qtde);
						$("#totalRecebimentoFisico").text(($("input[id^='qtdeRecebimentoFisico']").sum()));
					}
				},
				null, 
				true
			);
		}

		function isAtributosDiferencaVazios(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca) {

			if (!$.trim(codigoProduto) 
					&& !$.trim(descricaoProduto)
					&& !$.trim(numeroEdicao) 
					&& !$.trim(qtdeDiferenca)) {

				return true;
			}
		}

		function formatarCampos() {

			$("input[name='codigoProduto']").numeric();
			$("input[name='numeroEdicao']").numeric();
			$("input[name='qtdeDiferenca']").numeric();
			
			$("input[name='descricaoProduto']").autocomplete({source: ""});
		}
		
		function paraEstoque(param){
			
			if (param){
				
				$("#fieldCota").hide("slow");
				$(".trCotas").remove();
			} else {
				
				$("#fieldCota").show("slow");
			}
		}
		
		function adicionarLinhaCota(linhaAtual){
			
			if ($('#trCota' + (linhaAtual + 1)).length == 0 && $('#cotaInput' + (linhaAtual)).val() != ""){
				
				var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
						'<td><input type="text" name="cotaInput" id="cotaInput'+ (linhaAtual + 1) +'" onblur="cota.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true, buscarReparteAtualCota('+ (linhaAtual + 1) +'));" style="width:60px;" /></td>' +
						'<td>'+
						     '<input type="text" name="nomeInput" id="nomeInput'+ (linhaAtual + 1) +'" style="width:180px;" '+
						         ' onkeyup="cota.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
						         ' onblur="cota.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', buscarReparteAtualCota('+ (linhaAtual + 1) +'));" ' +
						     '/>'+
						'</td>' +
						'<td align="center" id="reparteText'+ (linhaAtual + 1) +'"></td>' +
						'<td align="center">' +
						     '<input type="text" name="diferencaInput" id="diferencaInput'+ (linhaAtual + 1) +'" style="width:80px; text-align:center;" onblur="adicionarLinhaCota('+ (linhaAtual + 1) +');" onchange="calcularReparteAtual('+ (linhaAtual + 1) +')"/>' +
						'</td>' +
						'<td id="reparteAtualText'+ (linhaAtual + 1) +'" align="center"></td></tr>'
				);
				
				$("#grid_1").append(tr);
				
				$("#cotaInput" + (linhaAtual + 1)).focus();
			}
		}
		
		function buscarReparteAtualCota(idDiv){
			
			$("#diferencaInput" + idDiv).focus();
			
			setTimeout(
					function(){
						if ($("#cotaInput" + idDiv).val() != ""){
							$.postJSON(
								contextPath + "/estoque/diferenca/lancamento/rateio/buscarReparteCotaPreco",
								[
								 	{name: "idProdutoEdicao", value: $("#codigoProduto").val()},
								 	{name: "numeroCota", value: $("#cotaInput" + idDiv).val()}
								],
								function(result) {
									$("#reparteText" + idDiv).text(result[0]);
								},
								true
							);
						}
					}, 
					500
			);
		}
		
		function buscarPrecoProdutoEdicao(){
			
			$.postJSON(
				contextPath + "/estoque/diferenca/lancamento/rateio/buscarPrecoProdutoEdicao",
				[
				 	{name: "codigoProduto", value: $("#codigoProdutoInput").val()},
				 	{name: "numeroEdicao", value: $("#edicaoProdutoInput").val()}
				],
				function(result) {
					$("#reparteProduto").text(result[0]);
					$("#precoCapaProduto").text(result[1]);
				},
				true
			);
		}
		
		function calcularReparteAtual(idDiv){
			
			if ($("#diferencaInput" + idDiv).val() == ""){
				
				$("#diferencaInput" + idDiv).val(0);
			}
			
			$("#reparteAtualText" + idDiv).text(parseInt($("#reparteText" + idDiv).text()) - parseInt($("#diferencaInput" + idDiv).val()));
		}
		
		function pesquisarProdutosNota(){
			$(".lanctoFaltasSobrasCota_3Grid").flexOptions({
				"url" : contextPath + "/estoque/diferenca/lancamento/rateio/buscarProdutosCotaNota",
				params : [ {
					name : "dateNotaEnvio",
					value : $("#dateNotaEnvio").val()
				}, {
					name : "numeroCota",
					value : $("#cotaInputNota").val()
				}],
			});
			
			$(".lanctoFaltasSobrasCota_3Grid").flexReload();
			
			$("#divPesquisaProdutosNota").show();
		}
		
		function alterarReparteAtual(indexDiv){
			
			$("#qtdTotal" + indexDiv).text(
				parseInt($("#reparte" + indexDiv).text()) - parseInt($("#inputDiferencaProduto" + indexDiv).val())
			);
		}
		
	</script>
</div>