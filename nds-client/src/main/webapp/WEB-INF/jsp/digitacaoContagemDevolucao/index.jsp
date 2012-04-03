<head>

<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script  type="text/javascript">

var ContagemDevolucao = {
		
		/**
			Verifica se o role do usuário logado é Operador 
		**/
		isRoleOperador: function(){
			
			var userProfile = ${userProfileOperador};
			
			return userProfile;	
		},
		/**
			Monta as colunas do grid em função do perfil de usuário logado no sistema 
		**/
		montarColunas: function (){
			
			if(ContagemDevolucao.isRoleOperador()){
				return montarColunasPerfilOperador();
			}
			
			return montarColunasPerfilAdmin();
		},
		/**
			Executa a pesquisa de contagens de devolução. 
		**/
		pesquisar: function (){
			
			var formData = $('#pesquisaContagemDevolucaoForm').serializeArray();
			
			$("#contagemDevolucaoGrid").flexOptions({
				url: "<c:url value='/devolucao/digitacao/contagem/pesquisar' />",
				params: formData
			});
			
			$("#contagemDevolucaoGrid").flexReload();

		},
		/**
			Executa o pré processamento das informações do grid. 
		**/
		executarPreProcessamento: function (resultado){
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#grids").hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
				
				var idInput = "valorExemplarNota" + index ;
				
				var inputExemplarNota = '<input id="'+idInput+'" name="qtdNota" type="text" style="width:80px; text-align: center;"  maxlength="20" value="'+row.cell.qtdNota+'"/>';
				
				if(!ContagemDevolucao.isRoleOperador()){
					
					inputExemplarNota = '<input id="'+idInput+'" name="qtdNota" maxlength="20" type="text" style="width:80px; text-align: center;"  value="'+row.cell.qtdNota+'" onchange="ContagemDevolucao.limparCheck(\'ch'+index+'\')"/>';
					
					var inputCheckReplicarValor = '<input type="checkbox" id="ch'+index+'" name="checkgroup" onclick="ContagemDevolucao.replicarValor(this,\''+idInput+'\','+row.cell.qtdDevolucao+');"/>';
					
					//Altera cor do valor da quantidade, caso seja um valo negativo
					if(row.cell.diferenca < 0){
						row.cell.diferenca = "<span style='color:red'>"+row.cell.diferenca+"</span>";
					}
					
					row.cell.replicarQtde = inputCheckReplicarValor;
				}
				
				//Inputs Hidden da grid
				var inputDataRecolhimentoDistribuidor= '<input name="idDataRecolhimentoDist" type="hidden" value="' + row.cell.dataRecolhimentoDistribuidor + '" />';
				
				//Insere input text para o campo exemplar nota
				row.cell.qtdNota =  inputDataRecolhimentoDistribuidor + inputExemplarNota;
				
			});
			
			$("#totalGeral").html(resultado.valorTotal);
			
			$("#grids").show();
			
			ContagemDevolucao.limparCheck('sel');
				
			return resultado.tableModel;
		},
		/**
			Replica o valor do campo exemplar devolução para o campo exemplares de nota. 
		**/	
		replicarValor: function (input,id,valor){
			
			valor = (input.checked == false)?"":valor;
			
			$('#'+id).val(valor);
		},
		/**
			Limpa os valores do checked. 
		**/
		limparCheck:function (id){
			
			$('#'+id).attr("checked",false);	
		},
		
		/**
			Replica todos os valores do campo exemplar devolução para os campos exemplares de nota. 
		**/
		replicarValorAll: function(){
			
			var linhasDaGrid = $("#contagemDevolucaoGrid tr");
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaExemplarDevolucao = linha.find("td")[4];
				var colunaExemplarNota = linha.find("td")[6];
				var colunaReplicarValor = linha.find("td")[8];
				
				var vlQntDevolucao = $(colunaExemplarDevolucao).find("div").html();
					
				$(colunaExemplarNota).find("div").find('input[name="qtdNota"]').val(vlQntDevolucao);
				
				$(colunaReplicarValor).find("div").find('input[name="checkgroup"]').attr("checked",true);
				
			});
		},
		
		/**
			Verifica se todos os itens do grid estão selecionados. 
			Se itens  selecionados, limpa todos os valores de exemplar de nota.
			Se itens não selecionados, replica todos os valores para os campos exemplares de nota. 
		**/
		checkAllReplicarValor: function (todos, checkgroupName) {
			
			if(todos.checked == false) {
				
				ContagemDevolucao.limparValorAll();
			}		
			else {										
				
				ContagemDevolucao.replicarValorAll();
			}	
		},
		/**
			Salva as informações de contagem de devolução informadas no grid.
		**/
		salvar:function(){
			
			var param = ContagemDevolucao.obterListaDigitacaoContagemDevolucao();
			
			$.postJSON(
				"<c:url value='/devolucao/digitacao/contagem/salvar'/>", 
				param,
				function(result) {
					ContagemDevolucao.pesquisar();
				},
				ContagemDevolucao.tratarErro, false
			);
		},
		/**
 			Trata erro nas ações de Salvar e Confirmar contagem de devolução. 
		**/
		tratarErro: function (resultado){
			
			if (resultado.mensagens) {
				
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
			}
		},
		
		/**
	 		Efetua a chamada ao método Confirmação de contagem de devoluação do controller. 
		**/
		confirmarOperacao: function (){
			
			var param = ContagemDevolucao.obterListaDigitacaoContagemDevolucao();
			
			$.postJSON(
				"<c:url value='/devolucao/digitacao/contagem/confirmar'/>", 
				param,
				function(result) {
					ContagemDevolucao.pesquisar();
				},
				ContagemDevolucao.tratarErro, false
			);
		},
		
		/**
	 		Monta o componente de confiramção da ação de Confirmar contagem de devolução
		**/
		popupConfirmar: function () {

			$("#dialog-confirmar").dialog({
				resizable : false,
				height : 140,
				width : 320,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						ContagemDevolucao.confirmarOperacao();
						
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		},
		/**
		 	Monta a lista de parâmetros necessarios para as ações de Salvar e Confirmar contagem de devolução
		**/
		obterListaDigitacaoContagemDevolucao: function () {

			var linhasDaGrid = $("#contagemDevolucaoGrid tr");

			var listaDigitacaoContagemDevolucao = "";
			
			//Verifica o role do usuario para obter o indice da coluna com os valores do exemplar nota
			var indexColunaExemplarNota = (ContagemDevolucao.isRoleOperador())?4:6;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCodigoProduto = linha.find("td")[0];
				var colunaNumeroEdicao = linha.find("td")[2];
				var colunaExemplarNota = linha.find("td")[indexColunaExemplarNota];
				
				var codigoProduto = $(colunaCodigoProduto).find("div").html();
				
				var numeroEdicao = $(colunaNumeroEdicao).find("div").html();

				var qtdNota = $(colunaExemplarNota).find("div").find('input[name="qtdNota"]').val();
				
				var dataRecolhimentoDistribuidor = $(colunaExemplarNota).find("div").find('input[name="idDataRecolhimentoDist"]').val();
				
				if (!$.trim(qtdNota)) {

					return true;
				}

				var digitacaoContagemDevolucao = 'listaDigitacaoContagemDevolucao[' + index + '].codigoProduto=' + codigoProduto + '&';

					digitacaoContagemDevolucao += 'listaDigitacaoContagemDevolucao[' + index + '].numeroEdicao=' + numeroEdicao + '&';

					digitacaoContagemDevolucao += 'listaDigitacaoContagemDevolucao[' + index + '].dataRecolhimentoDistribuidor=' + dataRecolhimentoDistribuidor  + '&';
					
					digitacaoContagemDevolucao += 'listaDigitacaoContagemDevolucao[' + index + '].qtdNota=' + qtdNota  + '&';

				listaDigitacaoContagemDevolucao = (listaDigitacaoContagemDevolucao + digitacaoContagemDevolucao);
			});

			return listaDigitacaoContagemDevolucao;
		},
		/**
			Limpa todos os dados do grid que estiverem selecionados
		**/
		limparValorAll: function (){
			
			var linhasDaGrid = $("#contagemDevolucaoGrid tr");
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				var colunaExemplarNota = linha.find("td")[6];
				var colunaReplicarValor = linha.find("td")[8];
				
				var inputReplicarValor = $(colunaReplicarValor).find("div").find('input[name="checkgroup"]');
				
				if(inputReplicarValor.attr('checked')){
					
					$(colunaExemplarNota).find("div").find('input[name="qtdNota"]').val("");		
				}
				
				$(colunaReplicarValor).find("div").find('input[name="checkgroup"]').attr("checked",false);
				
			});
		}
};

/**
 * Renderiza o grid ao abri a página.
 */
$(function() {
	
	var colunas = ContagemDevolucao.montarColunas();
	
	$("#contagemDevolucaoGrid").flexigrid({
		
		dataType : 'json',
		preProcess:ContagemDevolucao.executarPreProcessamento,
		onSuccess:function(){$('input[id^="valorExemplarNota"]').numeric();},
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
	
	if (ContagemDevolucao.isRoleOperador()){
		
		//Oculta os campos que não serão visíveis pelo perfil de usuário Operador
		$("#btnConfirmar").hide();
		$("#bt_sellAll").hide();
	}
	
	$("#dataDe").focus();
});

/**
 * Monta as colunas do grid para usuário com perfil de Administrador ou perfil que não seja Operador.
 */
function montarColunasPerfilAdmin(){
	
	var colModel = [ {
		display : 'Código',
		name : 'codigoProduto',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Produto',
		name : 'nomeProduto',
		width : 120,
		sortable : true,
		align : 'left'
	}, {
		display : 'Edição',
		name : 'numeroEdicao',
		width : 80,
		sortable : true,
		align : 'center'
	}, {
		display : 'Preço Capa R$',
		name : 'precoVenda',
		width : 90,
		sortable : true,
		align : 'right'
	}, {
		display : 'Exemplar Devolução',
		name : 'qtdDevolucao',
		width : 120,
		sortable : true,
		align : 'center'
	}, {
		display : 'Total R$',
		name : 'valorTotal',
		width : 80,
		sortable : false,
		align : 'right'
	}, {
		display : 'Exemplar Nota',
		name : 'qtdNota',
		width : 110,
		sortable : false,
		align : 'center'
	}, {
		display : 'Diferença',
		name : 'diferenca',
		width : 80,
		sortable : false,
		align : 'center'
	},{
		display : 'Replicar Qtde',
		name : 'replicarQtde',
		width : 80,
		sortable : false,
		align : 'center'
	}];
	
	return colModel;
}

/**
 * Monta as colunas do grid para usuário com perfil de Operador
 */
function montarColunasPerfilOperador() {
	
	var colModel = [ {
		display : 'Código',
		name : 'codigoProduto',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Produto',
		name : 'nomeProduto',
		width : 400,
		sortable : true,
		align : 'left'
	}, {
		display : 'Edição',
		name : 'numeroEdicao',
		width : 80,
		sortable : true,
		align : 'center'
	}, {
		display : 'Preço Capa R$',
		name : 'precoVenda',
		width : 90,
		sortable : true,
		align : 'right'
	}, {
		display : 'Exemplar Nota',
		name : 'qtdNota',
		width : 137,
		sortable : false,
		align : 'center'
	}];
	
	return colModel;
}

/**
 * Renderiza componente de Data(período) da tela
 */
$(function() {
	$('input[id^="data"]').datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('input[id^="data"]').mask("99/99/9999");
});

</script>

</head>

<body>
	
	<fieldset class="classFieldset">
		
		  <legend> Pesquisar Fornecedor</legend>
		  
		  <form id="pesquisaContagemDevolucaoForm"
				name="pesquisaContagemDevolucaoForm" 
				method="post">
		  	
		  	<div id="dialog-confirmar" title="Digitação de Contagem para Devolução" 
		  		 style="display: none; width: auto; height: auto; overflow: visible;">
				<p>Confirma a Digitação de Contagem para Devolução?</p>
			</div>
		  	
			  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				 <tr>
				     <td width="73">Período de:</td>
				    <td width="121"><input name="dataDe" type="text" id="dataDe" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td width="22">Até:</td>
				    <td width="131"><input name="dataAte" type="text" id="dataAte" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td colspan="77">Fornecedor:</td>
				    <td width="287">
				    <select name="idFornecedor" id="idFornecedor" style="width:250px;">
				      <option value="-1"  selected="selected">Todos</option>
				      <c:forEach items="${listaFornecedores}" var="fornecedor">
				      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
				      </c:forEach>
				    </select></td>
				    <td width="203">
				    	<span class="bt_pesquisar">
				    		<a id="btnPesquisar" href="javascript:;" onclick="ContagemDevolucao.pesquisar();">Pesquisar</a>
				    	</span>
				    </td>
				  </tr>
			  </table>
			
		</form>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
	
		 <legend>Devolução Fornecedor</legend>
		 
		 <div class="grids" id="grids" style="display:none;">
		 	
		 	 <table class="contagemDevolucaoGrid" id="contagemDevolucaoGrid"></table>

			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="51%">
							
							<span class="bt_novos" title="Gerar Arquivo">
							
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png"
								hspace="5" border="0" /> Arquivo </a> 
							</span> 
								
							
							<span class="bt_novos" title="Imprimir"> 
							
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"
								hspace="5" border="0" /> 
								Imprimir 
							</a>
							 
							</span> 
							
							<span id="btnSalvar" class="bt_novos" title="Salvar"> 
						
							<a href="javascript:ContagemDevolucao.salvar();"> 
							<img border="0" hspace="5" alt="Salvar"
								src="${pageContext.request.contextPath}/images/ico_salvar.gif" />
								Salvar
							</a> 
							</span> 
							
							<span id="btnConfirmar" class="bt_confirmar_novo" title="Confirmar"> 
							<a href="javascript:ContagemDevolucao.popupConfirmar();"> 
								<img border="0" hspace="5" alt="Confirmar"
								src="${pageContext.request.contextPath}/images/ico_check.gif">
							Confirmar
							</a> 
							</span>
							
					</td>
					
					<td width="17%">
						<strong>Total Geral R$:</strong>
					</td>
					
					<td width="14%" id="totalGeral"></td>
					
					<td width="18%">
						<span id="bt_sellAll" class="bt_sellAll">
							<label for="sel">Selecionar Todos</label> 
							<input type="checkbox" name="Todos" id="sel" onclick="ContagemDevolucao.checkAllReplicarValor(this, 'checkgroup');"
							style="float: left;" /> 
						</span>
					</td>
				</tr>
			</table>

		</div>
	</fieldset>
</body>
		
          
