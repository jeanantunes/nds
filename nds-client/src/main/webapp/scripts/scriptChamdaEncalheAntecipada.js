var chamdaEncalheAnteipadaController = $.extend(true, {
		
		totalCota:0,
		totalExemplares:0,
		tipoPesquisaSelecionado:"",
		tipoPesquisaGridCota:"PES_COTA",
		groupNameCheckGridCota:"groupGridCota",
		groupNameCheck:"checkgroup",
		nameGridPesquisaCota:"ceAntecipadaCotaGrid",
		nameGrid:"ceAntecipadaGrid",
		pesquisaCota:null,
			 
		params:function(){
			
			var formData = [ 
				             {name:"codigoProduto",value:$("#codigoProduto",this.workspace).val()},
				             {name:"numeroEdicao",value:$("#edicao",this.workspace).val()},
				             {name:"box",value:$("#box",this.workspace).val()},
				             {name:"fornecedor",value:$("#fornecedor",this.workspace).val()},
				             {name:"rota",value:$("#rota",this.workspace).val()},
				             {name:"roteiro",value:$("#roteiro",this.workspace).val()},
				             {name:"programacaoRealizada",value:chamdaEncalheAnteipadaController.getProgramacaoRealizada},
				             {name:"municipio",value:$("#municipio",this.workspace).val()},
				             {name:"tipoPontoPDV",value:$("#tipoPontoPDV",this.workspace).val()}
				            ];
			return formData;
		},
		
		getProgramacaoRealizada:function(){
			return ($("#checkCE",this.workspace).attr("checked") == "checked");
		},
		
		getHiddenProduto: function (){
			return $("#codProdutoHidden",this.workspace).val();
		},
		
		getHiddenNumeroEdicao: function (){
		   	return $("#numeroEdicaoHidden",this.workspace).val();
		},
		
		getHiddenFornecedor: function (){
			return $("#codFornecedorHidden",this.workspace).val();
		},
		
		setHiddenProduto: function (){
			 $("#codProdutoHidden",this.workspace).val($("#codigoProduto",this.workspace).val());
		},
		
		setHiddenNumeroEdicao: function (){
		   	$("#numeroEdicaoHidden",this.workspace).val($("#edicao",this.workspace).val());
		},
		
		setHiddenFornecedor: function (){
			$("#codFornecedorHidden",this.workspace).val($("#fornecedor",this.workspace).val());
		},
		
		setHiddenMunicipio: function (){
		   	$("#codMunicipioHidden",this.workspace).val($("#municipio",this.workspace).val());
		},
		
		setHiddenTipoPontoPDV: function (){
			$("#codTipoPontoPdvHidden",this.workspace).val($("#tipoPontoPDV",this.workspace).val());
		},
		
		getHiddenMunicipio: function (){
		   	return $("#codMunicipioHidden",this.workspace).val();
		},
		
		getHiddenTipoPontoPDV: function (){
			return $("#codTipoPontoPdvHidden",this.workspace).val();
		},
		
		getCodigoProdutoPesquisa: function (){
			return  {"codigoProduto": $("#codigoProduto",this.workspace).val()};
		},
			
		pesquisar: function(){
			
			if($("#box",this.workspace).val() == -1){
				
				chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
			}
			else{

				chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
			}
			
			chamdaEncalheAnteipadaController.desmarcarCheckTodos();
			chamdaEncalheAnteipadaController.zerarTotais();	
		},
		
		montarGridPesquisaCotas: function (){
			
			$("#ceAntecipadaCotaGrid",this.workspace).flexOptions({
				url: contextPath + "/devolucao/chamadaEncalheAntecipada/montarPesquisaCotas",
				params: chamdaEncalheAnteipadaController.params(),newp: 1,
				onSuccess:function(){
					chamdaEncalheAnteipadaController.formatarCampos();
					chamdaEncalheAnteipadaController.sumarizarCotasSelecionadas(chamdaEncalheAnteipadaController.nameGrid);
					chamdaEncalheAnteipadaController.processarRenderizacaoDeBotoesCE();			
				}
			});
			
			$("#ceAntecipadaCotaGrid",this.workspace).flexReload();
		},
		
		pesquisarCotasPorProduto:function(){
			
			$("#ceAntecipadaGrid",this.workspace).flexOptions({
				url: contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisar",
				params: chamdaEncalheAnteipadaController.params(),newp: 1,
				onSuccess:function(){
					chamdaEncalheAnteipadaController.sumarizarCotasSelecionadas(chamdaEncalheAnteipadaController.nameGrid);
					chamdaEncalheAnteipadaController.processarRenderizacaoDeBotoesCE();			
				}
			});
			
			$("#ceAntecipadaGrid",this.workspace).flexReload();
		},
		
		gravar: function (){
			
			var listaChamadaEncalheAntecipada = null;
			
			var params = {'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
				'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
				'dataRecolhimento':$("#dataAntecipacao",this.workspace).val(),
				'dataProgramada':$("#dataProgramada").val(),
				'gravarTodos':$("#sel", this.workspace).is(':checked')};
			
			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){
				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota),params);
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/gravarCotasPesquisa",params, 
						 function (result){
					 
							 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
							 chamdaEncalheAnteipadaController.zerarTotais();
							 
							 $("#dialog-novo",this.workspace).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
						},
						chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
				 
			}
			else {
				
				var checkTodos = params['gravarTodos'];
				if(checkTodos == "undefined" || !checkTodos ){
					
					params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid),params);
				}
			
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/gravarCotas",
						params , 
						function (result){
					
							if (checkTodos) {
								$("#grids",chamdaEncalheAnteipadaController.workspace).hide();
							} else {
								chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
							}
							
							chamdaEncalheAnteipadaController.zerarTotais();
							
							$("#dialog-novo",this.workspace).dialog("close");
							 
							chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
					 
						}, null,true);
			}
			
		},
		
		exibirMensagemSucesso: function (result){
			
			var tipoMensagem = result.tipoMensagem;
			var listaMensagens = result.listaMensagens;
			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			 }
		},
		
		validarItensSelecionados:function(group){
			
			return verifyAtLeastOneChecked(group); 
		},
		
		tratarErroPesquisaCota: function (jsonData){
			
			if (!jsonData || !jsonData.mensagens) {

				return;
			}
			
			var dadosValidacao = jsonData.mensagens.dados;
			
			if(dadosValidacao){
				
				exibirMensagem(
						jsonData.mensagens.tipoMensagem, 
						jsonData.mensagens.listaMensagens
				);
				
				$("#dialog-novo",this.workspace ).dialog("close");
			}
			
			var linhasDaGrid = $('#'+chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',this.workspace);

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				if (dadosValidacao 
						&& ($.inArray(index, dadosValidacao) > -1)) {

					linha.removeClass('erow').addClass('linhaComErro');
					
				} else {

					linha.removeClass('linhaComErro');					
				}
			});
		},
		
		limparLinha: function(index){
			
			var linhasDaGrid = $('#'+chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',this.workspace)[index];
		
			var valor = $(linhasDaGrid);
			
			valor.removeClass('linhaComErro');	
		},
		
		checkAll:function(input){
			
			if (chamdaEncalheAnteipadaController.tipoPesquisaSelecionado == chamdaEncalheAnteipadaController.tipoPesquisaGridCota){
				
				checkAll(input,chamdaEncalheAnteipadaController.groupNameCheckGridCota);
				
				if(input.checked == false){
					
					chamdaEncalheAnteipadaController.zerarTotais();
				}
				else {
					
					chamdaEncalheAnteipadaController.totalExemplares = $("input[id^='qntExemplares']",this.workspace).sum();
					
					var checked = $("input[name=" + chamdaEncalheAnteipadaController.groupNameCheckGridCota + "]:checked").length;	
					chamdaEncalheAnteipadaController.totalCota = eval(checked);
				
					chamdaEncalheAnteipadaController.atribuirValorQntCotas();
					chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
				}
			}
			else{
				
				checkAll(input,chamdaEncalheAnteipadaController.groupNameCheck);
				
				if(input.checked == false){
					chamdaEncalheAnteipadaController.zerarTotais();
				}
				else {
					$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/obterTotalCotaExemplar",
							   null, function (result){
						
						chamdaEncalheAnteipadaController.totalCota = result.qtdeTotalCotas;
						chamdaEncalheAnteipadaController.totalExemplares = result.qntTotalExemplares;
						
						chamdaEncalheAnteipadaController.atribuirValorQntCotas();
						chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
					});	
				}
			}
		},
		
		zerarTotais: function (){
			
			chamdaEncalheAnteipadaController.totalCota = 0;
			chamdaEncalheAnteipadaController.totalExemplares = 0;	
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
		},
		
		isItensSelecionados: function (){
			var groupName = (chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado)
								? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									: chamdaEncalheAnteipadaController.groupNameCheck;

			if(!verifyAtLeastOneChecked(groupName)){
				var mensagens = new Array('Selecione um item para realizar a operação!') ;
				exibirMensagem('WARNING',mensagens);
				return false;
			}
			return true;
		},
	
		exibirDialogData:function(tipoOperacao){
			
			if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
				return;
			}
		
			$("#dialog-novo",this.workspace).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
				         {id:"btn_confirma_programacao_ce",text:"Confirmar",
			        	  click: function() {
			        		  if(tipoOperacao =="Novo"){
									
									chamdaEncalheAnteipadaController.gravar();
								}
								else{
									
									chamdaEncalheAnteipadaController.reprogramarAntecipacaoEncalhe();	
								}
								$("#dataAntecipacao",this.workspace).val("");
			        	  	}
				         },
			        	{id:"btn_cancelar_antecipacao_ce",text:"Cancelar",
				         click:function(){
				        	 $( this ).dialog( "close" );
							 $("#dataAntecipacao",this.workspace).val("");
			        		}	  
			        	}  
				],
				form: $("#dialog-novo", this.workspace).parents("form")
			});
		},
			
		pesquisarProdutosSuccessCallBack:function() {
			
			chamdaEncalheAnteipadaController.pesquisarBox(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			chamdaEncalheAnteipadaController.pesquisarFornecedor(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			
			$("#dataProgramada",this.workspace).val("");
		},
		
		pesquisarProdutosErrorCallBack: function() {
			
			chamdaEncalheAnteipadaController.pesquisarBox(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			chamdaEncalheAnteipadaController.pesquisarFornecedor(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			
			$("#dataProgramada",this.workspace).val("");
		},
		
		montarComboFornecedores:function(result) {
			var comboFornecedores =  montarComboBox(result, true);
			
			$("#fornecedor",this.workspace).html(comboFornecedores);
		},
		
		montarComboBoxs:function(result) {
			var comboBoxes = montarComboBox(result, true);
			
			$("#box",this.workspace).html(comboBoxes);
		}, 
		
		pesquisarFornecedor:function(data){
		
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor",
					   data, chamdaEncalheAnteipadaController.montarComboFornecedores);
			
		},
		
		pesquisarBox:function(data){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarBox",
					   data, chamdaEncalheAnteipadaController.montarComboBoxs);
		},
		
		validarEdicaoSuccessCallBack : function(){
			
			 var data = [{name:"codigoProduto",value:$("#codigoProduto",this.workspace).val()},
             			 {name:"numeroEdicao",value:$("#edicao",this.workspace).val()},
						];
			
			 $.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada",
					   data, function(result){
				 if(result.result!= ""){
					 $("#dataProgramada",this.workspace).val(result);	 
				 }
			 });
		},
		
		validarEdicaoErrorCallBack: function(){
			 $("#dataProgramada",this.workspace).val("");
		},
		
		executarPreProcessamentoCota: function (resultado){
			
			chamdaEncalheAnteipadaController.limparGridPesquisaCota();
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens,""
				);
				
				$("#grids",chamdaEncalheAnteipadaController.workspace).hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
					
				var parametroCheckbox = '\'#qntExemplar' + index + '\', this';
				
				var inputCheck = '<input type="checkbox"  id="ch'+index+'" name="'+chamdaEncalheAnteipadaController.groupNameCheck+'" onclick="chamdaEncalheAnteipadaController.calcularTotalGridCota( '+parametroCheckbox +')" />';
				
				var inputQuantidadeExemplares = 
					'<input type="hidden" id="qntExemplar' + index + '" name="qntExemplares" value="'+ row.cell.qntExemplares +'"/>';
						
				var inputNomeCota= 
						'<input type="hidden" id="nomeCota' + index + '" name="descricaoCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputNumeroCota= 
					'<input type="hidden" id="numCota' + index + '" name="numCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamdaEncalhe+'"/>';
				
				var inputHiddenIdLancamento= 	
						'<input type="hidden" id="idLancamento' + index + '" name="idLancamento" value="'+ row.cell.idLancamento+'"/>';	
					
				row.cell.numeroCota = row.cell.numeroCota + inputNumeroCota + inputNomeCota  + inputHiddenCodigoChamadaEncalhe + inputHiddenIdLancamento;	
				row.cell.qntExemplares = row.cell.qntExemplares + inputQuantidadeExemplares;
				row.cell.sel = inputCheck;
			});
			
			chamdaEncalheAnteipadaController.setHiddenFornecedor();
			chamdaEncalheAnteipadaController.setHiddenNumeroEdicao();
			chamdaEncalheAnteipadaController.setHiddenProduto();
			chamdaEncalheAnteipadaController.setHiddenMunicipio();
			chamdaEncalheAnteipadaController.setHiddenTipoPontoPDV();
			
			$("#grids",chamdaEncalheAnteipadaController.workspace).show();
			$("#gridAntecipada",this.workspace).show();
			$("#gridPesquisaCota",this.workspace).hide();
			
			chamdaEncalheAnteipadaController.tipoPesquisaSelecionado = "";
			
			return resultado.tableModel;
		},
		
		pesquisarCotaErrorCallBack: function (idInputCkeck,idInputExemplares,idInputCota,indexLinha){
			
			var exemplares = eval( ($(idInputExemplares,this.workspace).val()=="")?0:$(idInputExemplares,this.workspace).val());
			
			var check  = $(idInputCkeck,this.workspace).attr('checked');
			
			chamdaEncalheAnteipadaController.limparLinha(indexLinha);
			
			if(exemplares > 0 &&  check == "checked"){
			
				chamdaEncalheAnteipadaController.totalCota -= 1;
				chamdaEncalheAnteipadaController.totalExemplares -= exemplares;
				
				chamdaEncalheAnteipadaController.atribuirValorQntCotas();
				chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
			}
			
			$(idInputCkeck,this.workspace).attr("checked",false);
			$(idInputCkeck,this.workspace).attr("disabled","disabled");
			$(idInputExemplares,this.workspace).val("");
			
		},
		
		pesquisarCotaSuccessCallBack: function (idInputCkeck,idInputExemplares,
												idInputCota,indexLinha){
			
			$(idInputCkeck,this.workspace).removeAttr("disabled"); 
			
			chamdaEncalheAnteipadaController.limparLinha(indexLinha);
			
			var param = [{name:"numeroCota",value:$(idInputCota,this.workspace).val()},
			             {name:"codigoProduto", value:chamdaEncalheAnteipadaController.getHiddenProduto()},
			             {name:"numeroEdicao",value:chamdaEncalheAnteipadaController.getHiddenNumeroEdicao()},
			             {name:"fornecedor",value:chamdaEncalheAnteipadaController.getHiddenFornecedor()},
			             {name:"programacaoRealizada",value:chamdaEncalheAnteipadaController.getProgramacaoRealizada()},
			             {name:"municipio",value:chamdaEncalheAnteipadaController.getHiddenMunicipio()},
			             {name:"tipoPontoPDV",value:chamdaEncalheAnteipadaController.getHiddenTipoPontoPDV()}
			             ];
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/obterQuantidadeExemplares",
					param, 
					function (result){
				
						$(idInputExemplares).val(result.quantidade);
						$("#codigoChamadaAntecipada" + indexLinha,this.workspace).val(result.idChamadaEncalhe);
						
						var check  = document.getElementById("sel",this.workspace).checked ; 
						
						if(check){
							
							chamdaEncalheAnteipadaController.calcularTotalCota(idInputExemplares, $(idInputCkeck,this.workspace));
							$(idInputCkeck,this.workspace).attr("checked",true);
							$("#sel",this.workspace).attr("checked",true);
						}
					}, 
					function (result){
						
						$(idInputCkeck,this.workspace).attr("checked",false);
						$(idInputCkeck,this.workspace).attr("disabled","disabled");
						$(idInputExemplares,this.workspace).val("");
					});	
		},
		
		executarPreProcessamentoGridCota:function(resultado){
			
			chamdaEncalheAnteipadaController.limparGridCota();
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#grids",chamdaEncalheAnteipadaController.workspace).hide();

				return resultado;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var idCheck = "chCota"+index;
				
				var paramIdCheck = '\'#chCota' + index +'\','+'\'#qntExemplares' + index + '\','+'\'#numCota' + index + '\','+index;
				
				var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';

				var parametroPesquisaCota = '\'#numCota' + index + '\', \'#descricaoCota'+ index + '\', false,function(){chamdaEncalheAnteipadaController.pesquisarCotaSuccessCallBack('+paramIdCheck+')}, function(){ chamdaEncalheAnteipadaController.pesquisarCotaErrorCallBack('+paramIdCheck+')}';
				
				var inputCodigoCota = 
					'<input type="text" id="numCota' + index + '" name="numCota" style="width:80px; float:left; margin-right:10px;" maxlenght="255" onchange="chamdaEncalheAnteipadaController.pesquisaCota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" />';

				var parametroAutoCompleteCota = '\'#descricaoCota' + index + '\', true';

				var inputDescricaoCota = 
					'<input type="text" id="descricaoCota' + index + '" name="descricaoCota" style="width:580px;" maxlenght="255" onkeyup="chamdaEncalheAnteipadaController.pesquisaCota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="chamdaEncalheAnteipadaController.pesquisaCota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';

				var inputQuantidadeExemplares = 
					'<input type="text" id="qntExemplares' + index + '" name="qntExemplares" disabled="disabled" style="width:80px; text-align: center"   />';
				
				var parametroCheckbox = '\'#qntExemplares' + index + '\', this';
					
				var inputCheck = '<input disabled="disabled" type="checkbox" id="'+idCheck+'" name="'+chamdaEncalheAnteipadaController.groupNameCheckGridCota+'" onclick="chamdaEncalheAnteipadaController.calcularTotalCota( '+parametroCheckbox +')" />';	
				
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamdaEncalhe+'"/>';
					
				var inputHiddenIdLancamento= 	
						'<input type="hidden" id="idLancamento' + index + '" name="idLancamento" value="'+ row.cell.idLancamento+'"/>';
						
				
				row.cell.cota = inputCodigoCota + hiddenId + inputHiddenCodigoChamadaEncalhe +inputHiddenIdLancamento;
				row.cell.nome = inputDescricaoCota;
				row.cell.qtdeExemplares = inputQuantidadeExemplares;	
				row.cell.sel = inputCheck;
			});
			
			chamdaEncalheAnteipadaController.setHiddenFornecedor();
			chamdaEncalheAnteipadaController.setHiddenNumeroEdicao();
			chamdaEncalheAnteipadaController.setHiddenProduto();
			chamdaEncalheAnteipadaController.setHiddenMunicipio();
			chamdaEncalheAnteipadaController.setHiddenTipoPontoPDV();
			
			$("#grids",chamdaEncalheAnteipadaController.workspace).show();
			$("#gridAntecipada",this.workspace).hide();
			$("#gridPesquisaCota",this.workspace).show();
			
			chamdaEncalheAnteipadaController.tipoPesquisaSelecionado = chamdaEncalheAnteipadaController.tipoPesquisaGridCota;
			
			$("input[name='numCota']",this.workspace).numeric();
			
			return resultado;
		},
		
		calcularTotalGridCota: function (idInputExemplares, inputCkeck){
			
			var check  = document.getElementById("sel",this.workspace).checked ; 
			
			if(check){

				var exemplares = eval( ($(idInputExemplares,this.workspace).val()=="")?0:$(idInputExemplares,this.workspace).val());
				var checked = $("input[name=" + chamdaEncalheAnteipadaController.groupNameCheck + "]:checked",this.workspace).length;
				
				chamdaEncalheAnteipadaController.totalExemplares = ($("input[id^='qntExemplar']",this.workspace).sum() - exemplares);	
				chamdaEncalheAnteipadaController.totalCota = eval(checked);
			
				chamdaEncalheAnteipadaController.atribuirValorQntCotas();
				chamdaEncalheAnteipadaController.atribuirValorQntExemplares();		

				chamdaEncalheAnteipadaController.desmarcarCheckTodos();				
				
			}else{
				chamdaEncalheAnteipadaController.calcularTotalCota(idInputExemplares, inputCkeck);
			}	
		},
		
		calcularTotalCota: function(idExemplares,input){
			
			var exemplares = eval( ($(idExemplares).val()=="")?0:$(idExemplares).val());
			
			if(input.checked == false){
				chamdaEncalheAnteipadaController.totalCota -= 1;
				chamdaEncalheAnteipadaController.totalExemplares -= exemplares;	
			}
			else{
				
				chamdaEncalheAnteipadaController.totalCota += 1;
				chamdaEncalheAnteipadaController.totalExemplares += exemplares;
			}
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
			
			chamdaEncalheAnteipadaController.desmarcarCheckTodos();	
		},
		
		atribuirValorQntExemplares: function (){
			$("#idTotalExemplares",this.workspace).val(chamdaEncalheAnteipadaController.totalExemplares);
		},
		
		atribuirValorQntCotas: function (){
			$("#idTotalCotas",this.workspace).val(chamdaEncalheAnteipadaController.totalCota);
		},
		
		desmarcarCheckTodos: function (){
			$('#sel',this.workspace).attr("checked",false);
		},
		
		obterParametrosGrid: function(grid){
			
			var linhasDaGrid = $('#'+grid+' tr',this.workspace);
			
			var listaChamadaEncalheAntecipada = new Array();
			
			var groupName = (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)
							? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									:chamdaEncalheAnteipadaController.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?3:4];
				var colunaCota = linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?0:1];
				var colunaNomeCota =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?1:2];
				var colunaQntExemplares =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?2:3];
				
				var id = $(colunaCota,this.workspace).find("div").find('input[name="id"]').val();
				
				var codigoCota = $(colunaCota,this.workspace).find("div").find('input[name="numCota"]').val();
				
				var nomeCota = $(colunaNomeCota,this.workspace).find("div").find('input[name="descricaoCota"]').val();
				
				var qntExemplares =  $(colunaQntExemplares,this.workspace).find("div").find('input[name="qntExemplares"]').val();
			
				var codigoChamdaEncalhe = $(colunaCota,this.workspace).find("div").find('input[name="codigoChamadaAntecipada"]').val();
				
				var idLancamento = $(colunaCota,this.workspace).find("div").find('input[name="idLancamento"]').val();
				
				var check  = $(colunaCheck,this.workspace).find("div").find('input[name="'+groupName+'"]');
				
				if (chamdaEncalheAnteipadaController.isAtributosVaziosOrSelecionados(codigoCota,check)){
					return true;
				}
				
				var cotaSelecionada = {'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
						'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
						'numeroCota':codigoCota,
						'nomeCota':nomeCota,
						'id':id,
						'qntExemplares':qntExemplares,
						'codigoChamdaEncalhe':codigoChamdaEncalhe,
						'idLancamento':idLancamento};
			
				listaChamadaEncalheAntecipada.push(cotaSelecionada);
				
			});
			
			return listaChamadaEncalheAntecipada;
		},
		
		isAtributosVaziosOrSelecionados: function (codigoCota,inputCheck){
			
			var check  = inputCheck.attr('checked');
			
			return (typeof check == "undefined" || !$.trim(codigoCota));
		},
		
		limparGridPesquisaCota: function (){
			 $('#'+ chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',this.workspace).remove();
		},
		
		limparGridCota: function (){
			$('#'+ chamdaEncalheAnteipadaController.nameGrid +' tr',this.workspace).remove();
		},
		
		formatarCampos: function (){
			
			$("input[name='numCota']",this.workspace).numeric();
			$("input[name='descricaoCota']",this.workspace).autocomplete({source: ""});
		},
		
		exportar:function(fileType) {
			
			var paramDataProgaramada = "&dataProgaramada=" + $("#dataProgramada",this.workspace).val();
			
			if (chamdaEncalheAnteipadaController.tipoPesquisaSelecionado == chamdaEncalheAnteipadaController.tipoPesquisaGridCota){
				
				if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
					return;
				}
				
				var listaChamadaEncalheAntecipada = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota));
				
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/validarCotasPesquisa",
						 listaChamadaEncalheAntecipada, function (result){
					
					var paramProduto = "&codigoProduto="+chamdaEncalheAnteipadaController.getHiddenProduto();
					var paramEdicao = "&numeroEdicao=" +chamdaEncalheAnteipadaController.getHiddenNumeroEdicao();
					var paramFornecedor = "&fornecedor="+chamdaEncalheAnteipadaController.getHiddenFornecedor();
					var paramMunicipio = "&municipio="+chamdaEncalheAnteipadaController.getHiddenMunicipio();
					var paramTipoPontoPDV ="&tipoPontoPDV=" + chamdaEncalheAnteipadaController.getHiddenTipoPontoPDV();
					
					window.location = 
						contextPath + "/devolucao/chamadaEncalheAntecipada/exportarPesquisaCotas?fileType=" 
								+ fileType + paramDataProgaramada
								+paramProduto + paramEdicao + paramFornecedor + paramMunicipio + paramTipoPontoPDV;
				},
				chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);
			}
			else{
				
				window.location = 
					contextPath + "/devolucao/chamadaEncalheAntecipada/exportar?fileType=" + fileType + paramDataProgaramada;
			}
		},
		
		recarregarComboRotas:function(idRoteiro){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/recarregarListaRotas",
					[{name:"roteiro",value:idRoteiro}], function(result){
				
				var comboRotas =  montarComboBox(result, true);
				
				$("#rota",this.workspace).html(comboRotas);	
			});
		},
		
		recarregarComboRoteiroRotas:function(idBox){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/recarregarRoteiroRota",
					[{name:"idBox",value:idBox}], function(result){
				
				var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
				var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
				
				$("#rota",this.workspace).html(comboRotas);
				$("#roteiro",this.workspace).html(comboRoteiros);
			});
		},
		
		sumarizarCotasSelecionadas:function(grid){
			
			var linhasDaGrid = $('#'+grid+' tr',this.workspace);
			
			var groupName = (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)
							? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									:chamdaEncalheAnteipadaController.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?3:4];
				var colunaQntExemplares =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?2:3];
				
				var qntExemplares =  $(colunaQntExemplares,this.workspace).find("div").find('input[name="qntExemplares"]').val();
			
				var inputCheck  = $(colunaCheck,this.workspace).find("div").find('input[name="'+groupName+'"]');
				
				var check  = inputCheck.attr('checked');
				
				if (typeof check != "undefined"){
					chamdaEncalheAnteipadaController.totalCota += 1;
					chamdaEncalheAnteipadaController.totalExemplares += eval(qntExemplares);
				}
			});
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
		},
		
		cancelarProgramacaoAntecipacaoEncalhe:function(){
			
			var params = {
					codigoProduto:chamdaEncalheAnteipadaController.getHiddenProduto(),
					numeroEdicao:chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
					'cancelarTodos':$("#sel", this.workspace).is(':checked')};

			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota), params);
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotasPesquisa",
						params,
						function (result){
					 
							 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
							 chamdaEncalheAnteipadaController.zerarTotais();
							 
							 $("#dialog-novo",this.workspace ).dialog("close");
							 $("#dialog-cancelamentoCE",this.workspace ).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
						},
						chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
			}
			else{
				
				var checkTodos = params['cancelarTodos'];
				if(checkTodos == "undefined" || !checkTodos ){
					
					params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid), params);
				}
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotas",
						params, 
						function (result){
					
							if (checkTodos) {
								$("#grids",chamdaEncalheAnteipadaController.workspace).hide();
							} else {
								chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
							}
							
							chamdaEncalheAnteipadaController.zerarTotais();
						
							chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
							
							 $("#dialog-cancelamentoCE",this.workspace ).dialog("close");
					 
						}, null,true);

			}
			
		},
		
		reprogramarAntecipacaoEncalhe:function(){
			
			var params ={'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
					'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
					'dataRecolhimento':$("#dataAntecipacao",this.workspace).val(),
					'dataProgramada': $("#dataProgramada",this.workspace).val(),
					'gravarTodos':$("#sel", this.workspace).is(':checked')};
			
			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){
				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada',chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota),params);
								
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/reprogramarCotasPesquisa",
						params, 
						 function (result){
					 
							 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
							 chamdaEncalheAnteipadaController.zerarTotais();
							 
							 $("#dialog-novo",this.workspace).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
						},
						chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
				 
			}
			else {
				
				var checkTodos = params['gravarTodos'];
				if(checkTodos == "undefined" || !checkTodos ) {
					
					params = serializeArrayToPost('listaChamadaEncalheAntecipada',chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid),params);
				}
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/reprogramarCotas",
						params, 
						function (result){
							
							if (checkTodos) {
								$("#grids",chamdaEncalheAnteipadaController.workspace).hide();
							} else {
								chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
							}

							chamdaEncalheAnteipadaController.zerarTotais();
							
							 $("#dialog-novo",this.workspace).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
					 
						}, null,true);
			}
		},
		
		processarRenderizacaoDeBotoesCE:function(){
			
			if (chamdaEncalheAnteipadaController.getProgramacaoRealizada() == true){
				$("#bt_cancelar_programacao",this.workspace).show();
				$("#bt_reprogramar_CE",this.workspace).show();
				$("#bt_confirmar_novo",this.workspace).hide();
			}
			else{
				$("#bt_cancelar_programacao",this.workspace).hide();
				$("#bt_reprogramar_CE",this.workspace).hide();
				$("#bt_confirmar_novo",this.workspace).show();
			}
		},
		
		exibirDialogCancelamentoCE:function(){
			
			if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
				return;
			}
			
			$("#dialog-cancelamentoCE" ,this.workspace).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
			         {id:"btn_confirma_cancelar_ce",text:"Confirmar",
		        	  click: function() {
		        		  chamdaEncalheAnteipadaController.cancelarProgramacaoAntecipacaoEncalhe();	
		        	  	}
			         },
		        	{id:"btn_cancelar_ce",text:"Cancelar",
			         click:function(){
		        			$( this ).dialog( "close" );
		        		}	  
		        	}  
				],
				form: $("#dialog-cancelamentoCE", this.workspace).parents("form")
			});
		},
		
		init : function(pesquisaCota) {
			
			chamdaEncalheAnteipadaController.pesquisaCota = pesquisaCota;
			
		    definirAcaoPesquisaTeclaEnter(this.workspace);	

			$("#dataAntecipacao",this.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="data"]',this.workspace).mask("99/99/9999");
			
			$("#edicao",this.workspace).numeric();

			$("#produto",this.workspace).autocomplete({source: ""});
			
			$("#codigoProduto",this.workspace).focus();
			
			$("#ceAntecipadaGrid",this.workspace).flexigrid({
				preProcess:chamdaEncalheAnteipadaController.executarPreProcessamentoCota,
				dataType : 'json',
				colModel : [ {
					display : 'Box',
					name : 'box',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Cota',
					name : 'numeroCota',
					width : 130,
					sortable : true,
					align : 'center'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 530,
					sortable : true,
					align : 'left'
				}, {
					display : 'Qtde. Exemplares',
					name : 'qntExemplares',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : ' ',
					name : 'sel',
					width : 50,
					sortable : true,
					align : 'center'
				}],
				sortname : "box",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});

		$("#ceAntecipadaCotaGrid",this.workspace).flexigrid({
			preProcess:chamdaEncalheAnteipadaController.executarPreProcessamentoGridCota,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 590,
				sortable : false,
				align : 'left'
			}, {
				display : 'Qtde. Exemplares',
				name : 'qtdeExemplares',
				width : 130,
				sortable : false,
				align : 'center'
			}, {
				display : ' ',
				name : 'sel',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			disableSelect : true,
			width : 960,
			height : 180
		});
	},
		
}, BaseController);
