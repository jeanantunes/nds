var digitacaoContagemDevolucaoController = $.extend(true, {

		init : function(userProfileOperador) {
			/**
			 * Renderiza componente de Data(período) da tela
			 */
			$('input[id^="data"]', digitacaoContagemDevolucaoController.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="data"]', digitacaoContagemDevolucaoController.workspace).mask("99/99/9999");

			var colunas = digitacaoContagemDevolucaoController.montarColunas();
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexigrid({
				
				dataType : 'json',
				preProcess:digitacaoContagemDevolucaoController.executarPreProcessamento,
				onSuccess:function(){$('input[id^="valorExemplarNota"]', digitacaoContagemDevolucaoController.workspace).numeric();},
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
			
			if (digitacaoContagemDevolucaoController.isRoleOperador(userProfileOperador)){
				
				//Oculta os campos que não serão visíveis pelo perfil de usuário Operador
				//$("#btnConfirmar", digitacaoContagemDevolucaoController.workspace).hide();
				//$("#bt_sellAll", digitacaoContagemDevolucaoController.workspace).hide();
			}
			
			$("#dataDe", digitacaoContagemDevolucaoController.workspace).focus();
			
		},
		
		/**
			Verifica se o role do usuário logado é Operador 
		**/
		isRoleOperador: function(userProfileOperador){
			
			var userProfile = userProfileOperador;
			
			return userProfile;	
		},
		
		/**
			Monta as colunas do grid em função do perfil de usuário logado no sistema 
		**/
		montarColunas: function (){
			
			if(digitacaoContagemDevolucaoController.isRoleOperador()){
				return digitacaoContagemDevolucaoController.montarColunasPerfilOperador();
			}
			
			return digitacaoContagemDevolucaoController.montarColunasPerfilAdmin();
		},
		
		/**
			Executa a pesquisa de contagens de devolução. 
		**/
		pesquisar: function (){
			
			var formData = $('#pesquisaContagemDevolucaoForm', digitacaoContagemDevolucaoController.workspace).serializeArray();
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexOptions({
				url: contextPath + "/devolucao/digitacao/contagem/pesquisar",
				params: formData
			});
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexReload();

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
				
				$("#grids", digitacaoContagemDevolucaoController.workspace).hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
				
				var idInput = "valorExemplarNota" + index ;
				
				var inputExemplarNota = '<input id="'+idInput+'" name="qtdNota" type="text" style="width:80px; text-align: center;"  maxlength="17" value="'+row.cell.qtdNota+'"/>';
				
				if(!digitacaoContagemDevolucaoController.isRoleOperador()){
					
					inputExemplarNota = '<input id="'+idInput+'" name="qtdNota" maxlength="17" type="text" style="width:80px; text-align: center;"  value="'+row.cell.qtdNota+'" onchange="ContagemDevolucao.limparCheck(\'ch'+index+'\')"/>';
					
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
			
			$("#totalGeral", digitacaoContagemDevolucaoController.workspace).html(resultado.valorTotal);
			
			$("#grids", digitacaoContagemDevolucaoController.workspace).show();
			
			digitacaoContagemDevolucaoController.limparCheck('sel');
				
			return resultado.tableModel;
		},
		
		/**
		*	Replica o valor do campo exemplar devolução para o campo exemplares de nota. 
		**/	
		replicarValor: function (input,id,valor){
			
			valor = (input.checked == false)?"":valor;
			
			$('#'+id, digitacaoContagemDevolucaoController.workspace).val(valor);
			
			if(input.checked == false) {
				$('#'+id, digitacaoContagemDevolucaoController.workspace).prop('disabled', false);
			} else {
				$('#'+id,digitacaoContagemDevolucaoController.workspace).prop('disabled', true);
			}
			
		},
		
		/**
			Limpa os valores do checked. 
		**/
		limparCheck:function (id){
			
			$('#'+id, digitacaoContagemDevolucaoController.workspace).attr("checked",false);	
		},
		
		/**
			Replica todos os valores do campo exemplar devolução para os campos exemplares de nota. 
		**/
		replicarValorAll: function(){
			
			var linhasDaGrid = $("#contagemDevolucaoGrid tr", digitacaoContagemDevolucaoController.workspace);
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaExemplarDevolucao = linha.find("td")[4];
				var colunaExemplarNota = linha.find("td")[6];
				var colunaReplicarValor = linha.find("td")[8];
				
				var vlQntDevolucao = $(colunaExemplarDevolucao).find("div").html();
				
				$(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').val(vlQntDevolucao);
				
				$(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').prop('disabled', true);
				
				$(colunaReplicarValor, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="checkgroup"]').attr("checked",true);
				
			});
		},
		
		/**
			Verifica se todos os itens do grid estão selecionados. 
			Se itens  selecionados, limpa todos os valores de exemplar de nota.
			Se itens não selecionados, replica todos os valores para os campos exemplares de nota. 
		**/
		checkAllReplicarValor: function (todos, checkgroupName) {
			
			if(todos.checked == false) {
				
				digitacaoContagemDevolucaoController.limparValorAll();
			}		
			else {										
				
				digitacaoContagemDevolucaoController.replicarValorAll();
			}	
		},
		
		/**
			Salva as informações de contagem de devolução informadas no grid.
		**/
		salvar:function(){
			
			var param = digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao();
			
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/salvar", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
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
			
			var param = digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao();
			
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/confirmar", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
			);
		},
		
		/**
	 		Monta o componente de confiramção da ação de Confirmar contagem de devolução
		**/
		popupConfirmar: function () {

			$("#dialog-confirmar", digitacaoContagemDevolucaoController.workspace).dialog({
				resizable : false,
				height : 140,
				width : 320,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						digitacaoContagemDevolucaoController.confirmarOperacao();
						
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				form: $("#dialog-confirmar", this.workspace).parents("form")
			});
		},
		
		/**
		 	Monta a lista de parâmetros necessarios para as ações de Salvar e Confirmar contagem de devolução
		**/
		obterListaDigitacaoContagemDevolucao: function () {

			var linhasDaGrid = $("#contagemDevolucaoGrid tr", digitacaoContagemDevolucaoController.workspace);

			var listaDigitacaoContagemDevolucao = "";
			
			//Verifica o role do usuario para obter o indice da coluna com os valores do exemplar nota
			var indexColunaExemplarNota = (digitacaoContagemDevolucaoController.isRoleOperador())?4:6;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCodigoProduto = linha.find("td")[0];
				var colunaNumeroEdicao = linha.find("td")[2];
				var colunaExemplarNota = linha.find("td")[indexColunaExemplarNota];
				
				var codigoProduto = $(colunaCodigoProduto, digitacaoContagemDevolucaoController.workspace).find("div").html();
				
				var numeroEdicao = $(colunaNumeroEdicao, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var qtdNota = $(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').val();
				
				var dataRecolhimentoDistribuidor = $(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="idDataRecolhimentoDist"]').val();
				
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
			
			var linhasDaGrid = $("#contagemDevolucaoGrid tr", digitacaoContagemDevolucaoController.workspace);
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				var colunaExemplarNota = linha.find("td")[6];
				var colunaReplicarValor = linha.find("td")[8];
				
				var inputReplicarValor = $(colunaReplicarValor, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="checkgroup"]');
				
				if(inputReplicarValor.attr('checked')){
					
					$(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').val("");
					
					$(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').prop('disabled', false);
					
				}
				
				$(colunaReplicarValor, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="checkgroup"]').attr("checked",false);
				
			});
		},
		
		/**
		 * Monta as colunas do grid para usuário com perfil de Administrador ou perfil que não seja Operador.
		 */
		montarColunasPerfilAdmin : function(){
			
			var colModel = [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
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
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 85,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exemplar Devolução',
				name : 'qtdDevolucao',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 70,
				sortable : false,
				align : 'right'
			}, {
				display : 'Total c/ Desc. R$',
				name : 'valorTotalComDesconto',
				width : 90,
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
				width : 70,
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
		},

		/**
		 * Monta as colunas do grid para usuário com perfil de Operador
		 */
		montarColunasPerfilOperador : function() {
			
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
		
}, BaseController);



//@ sourceURL=digitacaoContagemDevolucao.js

