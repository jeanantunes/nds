<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
			
	<script language="javascript" type="text/javascript">
	
	var EncalheAntecipado = {
		
		totalCota:0,
		totalExemplares:0,
		tipoPesquisaSelecionado:"",
		tipoPesquisaGridCota:"PES_COTA",
		groupNameCheckGridCota:"groupGridCota",
		groupNameCheck:"checkgroup",
		nameGridPesquisaCota:"ceAntecipadaCotaGrid",
		nameGrid:"ceAntecipadaGrid",
			 
		params:function(){
			
			var formData = [ 
				             {name:"codigoProduto",value:$("#codigoProduto").val()},
				             {name:"numeroEdicao",value:$("#edicao").val()},
				             {name:"box",value:$("#box").val()},
				             {name:"fornecedor",value:$("#fornecedor").val()},
				             {name:"rota",value:$("#rota").val()},
				             {name:"roteiro",value:$("#roteiro").val()},
				             {name:"programacaoRealizada",value:EncalheAntecipado.getProgramacaoRealizada}
				             
				            ];
			return formData;
		},
		
		getProgramacaoRealizada:function(){
			return ($("#checkCE").attr("checked") == "checked");
		},
		
		getHiddenProduto: function (){
			return $("#codProdutoHidden").val();
		},
		
		getHiddenNumeroEdicao: function (){
		   	return $("#numeroEdicaoHidden").val();
		},
		
		getHiddenFornecedor: function (){
			return $("#codFornecedorHidden").val();
		},
		
		setHiddenProduto: function (){
			 $("#codProdutoHidden").val($("#codigoProduto").val());
		},
		
		setHiddenNumeroEdicao: function (){
		   	$("#numeroEdicaoHidden").val($("#edicao").val());
		},
		
		setHiddenFornecedor: function (){
			$("#codFornecedorHidden").val($("#fornecedor").val());
		},
		
		getCodigoProdutoPesquisa: function (){
			return  "codigoProduto=" + $("#codigoProduto").val();
		},
			
		pesquisar: function(){
			
			if($("#box").val() == -1){
				
				EncalheAntecipado.montarGridPesquisaCotas();
			}
			else{

				EncalheAntecipado.pesquisarCotasPorProduto();
			}
			
			EncalheAntecipado.desmarcarCheckTodos();
			EncalheAntecipado.zerarTotais();	
		},
		
		montarGridPesquisaCotas: function (){
			
			$("#ceAntecipadaCotaGrid").flexOptions({
				url: "<c:url value='/devolucao/chamadaEncalheAntecipada/montarPesquisaCotas' />",
				params: EncalheAntecipado.params(),newp: 1,
				onSuccess:function(){
					EncalheAntecipado.formatarCampos();
					EncalheAntecipado.sumarizarCotasSelecionadas(EncalheAntecipado.nameGrid);
					EncalheAntecipado.processarRenderizacaoDeBotoesCE();			
				}
			});
			
			$("#ceAntecipadaCotaGrid").flexReload();
		},
		
		pesquisarCotasPorProduto:function(){
			
			$("#ceAntecipadaGrid").flexOptions({
				url: "<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisar' />",
				params: EncalheAntecipado.params(),newp: 1,
				onSuccess:function(){
					EncalheAntecipado.sumarizarCotasSelecionadas(EncalheAntecipado.nameGrid);
					EncalheAntecipado.processarRenderizacaoDeBotoesCE();			
				}
			});
			
			$("#ceAntecipadaGrid").flexReload();
		},
		
		gravar: function (){
			
			var listaChamadaEncalheAntecipada ="";
			
			var dataRecolhimento = $("#dataAntecipacao").val();
			
			var paramProduto = "&codigoProduto="+EncalheAntecipado.getHiddenProduto();
			var paramEdicao = "&numeroEdicao=" +EncalheAntecipado.getHiddenNumeroEdicao();
			var paramDataRecolhimento = "dataRecolhimento=" +dataRecolhimento;
			var paramDataProgramada = "&dataProgramada=" + $("#dataProgramada").val();
			
			if(EncalheAntecipado.tipoPesquisaGridCota == EncalheAntecipado.tipoPesquisaSelecionado){
				
				listaChamadaEncalheAntecipada = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGridPesquisaCota);
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/gravarCotasPesquisa' />",
						 listaChamadaEncalheAntecipada 
						 + paramDataRecolhimento
						 + paramProduto
						 + paramEdicao
						 + paramDataProgramada, 
						 function (result){
					 
							 EncalheAntecipado.montarGridPesquisaCotas();
							 EncalheAntecipado.zerarTotais();
							 
							 $("#dialog-novo" ).dialog("close");
							 
							 EncalheAntecipado.desmarcarCheckTodos();
							 EncalheAntecipado.exibirMensagemSucesso(result);
						},
						EncalheAntecipado.tratarErroPesquisaCota,true);	
				 
			}
			else {
				
				var checkTodos  = $("#sel").attr('checked');
				
				if(typeof checkTodos == "undefined" || !checkTodos == 'checked'){
					
					listaChamadaEncalheAntecipada  = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGrid);
					checkTodos="";
				}
			
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/gravarCotas' />",
						listaChamadaEncalheAntecipada 
						+ paramDataRecolhimento
						+ paramProduto
						+ paramEdicao
						+ paramDataProgramada
						+ "&gravarTodos=" + checkTodos , 
						function (result){
					
							EncalheAntecipado.pesquisarCotasPorProduto();
							EncalheAntecipado.zerarTotais();
							
							 $("#dialog-novo" ).dialog("close");
							 
							 EncalheAntecipado.desmarcarCheckTodos();
							 EncalheAntecipado.exibirMensagemSucesso(result);
					 
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
				
				$("#dialog-novo" ).dialog("close");
			}
			
			var linhasDaGrid = $('#'+EncalheAntecipado.nameGridPesquisaCota+' tr');

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
			
			var linhasDaGrid = $('#'+EncalheAntecipado.nameGridPesquisaCota+' tr')[index];
		
			var valor = $(linhasDaGrid);
			
			valor.removeClass('linhaComErro');	
		},
		
		checkAll:function(input){
			
			if (EncalheAntecipado.tipoPesquisaSelecionado == EncalheAntecipado.tipoPesquisaGridCota){
				
				checkAll(input,EncalheAntecipado.groupNameCheckGridCota);
				
				if(input.checked == false){
					
					EncalheAntecipado.zerarTotais();
				}
				else {
					
					EncalheAntecipado.totalExemplares = $("input[id^='qntExemplares']").sum();
					
					var checked = $("input[name=" + EncalheAntecipado.groupNameCheckGridCota + "]:checked").length;	
					EncalheAntecipado.totalCota = eval(checked);
				
					EncalheAntecipado.atribuirValorQntCotas();
					EncalheAntecipado.atribuirValorQntExemplares();
				}
			}
			else{
				
				checkAll(input,EncalheAntecipado.groupNameCheck);
				
				if(input.checked == false){
					EncalheAntecipado.zerarTotais();
				}
				else {
					$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/obterTotalCotaExemplar' />",
							   null, function (result){
						
						EncalheAntecipado.totalCota = result.qtdeTotalCotas;
						EncalheAntecipado.totalExemplares = result.qntTotalExemplares;
						
						EncalheAntecipado.atribuirValorQntCotas();
						EncalheAntecipado.atribuirValorQntExemplares();
					});	
				}
			}
		},
		
		zerarTotais: function (){
			
			EncalheAntecipado.totalCota = 0;
			EncalheAntecipado.totalExemplares = 0;	
			
			EncalheAntecipado.atribuirValorQntCotas();
			EncalheAntecipado.atribuirValorQntExemplares();
		},
		
		isItensSelecionados: function (){
			var groupName = (EncalheAntecipado.tipoPesquisaGridCota == EncalheAntecipado.tipoPesquisaSelecionado)
								? EncalheAntecipado.groupNameCheckGridCota
									: EncalheAntecipado.groupNameCheck;

			if(!verifyAtLeastOneChecked(groupName)){
				var mensagens = new Array('Selecione um item para realizar a operação!') ;
				exibirMensagem('WARNING',mensagens);
				return false;
			}
			return true;
		},
	
		exibirDialogData:function(tipoOperacao){
			
			if(!EncalheAntecipado.isItensSelecionados()){
				return;
			}
		
			$("#dialog-novo" ).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
				         {id:"btn_confirma_programacao_ce",text:"Confirmar",
			        	  click: function() {
			        		  if(tipoOperacao =="Novo"){
									
									EncalheAntecipado.gravar();
								}
								else{
									
									EncalheAntecipado.reprogramarAntecipacaoEncalhe();	
								}
								$("#dataAntecipacao").val("");
			        	  	}
				         },
			        	{id:"btn_cancelar_antecipacao_ce",text:"Cancelar",
				         click:function(){
				        	 $( this ).dialog( "close" );
							 $("#dataAntecipacao").val("");
			        		}	  
			        	}  
				],
			});
		},
			
		pesquisarProdutosSuccessCallBack:function() {
			
			EncalheAntecipado.pesquisarBox(EncalheAntecipado.getCodigoProdutoPesquisa());
			EncalheAntecipado.pesquisarFornecedor(EncalheAntecipado.getCodigoProdutoPesquisa());
			
			$("#dataProgramada").val("");
		},
		
		pesquisarProdutosErrorCallBack: function() {
			
			EncalheAntecipado.pesquisarBox(EncalheAntecipado.getCodigoProdutoPesquisa());
			EncalheAntecipado.pesquisarFornecedor(EncalheAntecipado.getCodigoProdutoPesquisa());
			
			$("#dataProgramada").val("");
		},
		
		montarComboFornecedores:function(result) {
			var comboFornecedores =  montarComboBox(result, true);
			
			$("#fornecedor").html(comboFornecedores);
		},
		
		montarComboBoxs:function(result) {
			var comboBoxes = "<option selected='selected'  value='-1'></option>";  
				
			comboBoxes = comboBoxes + montarComboBox(result, true);
			
			$("#box").html(comboBoxes);
		}, 
		
		pesquisarFornecedor:function(data){
		
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor' />",
					   data, EncalheAntecipado.montarComboFornecedores);
			
		},
		
		pesquisarBox:function(data){
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarBox' />",
					   data, EncalheAntecipado.montarComboBoxs);
		},
		
		validarEdicaoSuccessCallBack : function(){
			
			 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
             			 {name:"numeroEdicao",value:$("#edicao").val()},
						];
			
			 $.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada' />",
					   data, function(result){
				 if(result.result!= ""){
					 $("#dataProgramada").val(result);	 
				 }
			 });
		},
		
		validarEdicaoErrorCallBack: function(){
			 $("#dataProgramada").val("");
		},
		
		executarPreProcessamentoCota: function (resultado){
			
			EncalheAntecipado.limparGridPesquisaCota();
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens,""
				);
				
				$("#grids").hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
					
				var parametroCheckbox = '\'#qntExemplar' + index + '\', this';
				
				var inputCheck = '<input type="checkbox"  id="ch'+index+'" name="'+EncalheAntecipado.groupNameCheck+'" onclick="EncalheAntecipado.calcularTotalGridCota( '+parametroCheckbox +')" />';
				
				var inputQuantidadeExemplares = 
					'<input type="hidden" id="qntExemplar' + index + '" name="qntExemplares" value="'+ row.cell.qntExemplares +'"/>';
						
				var inputNomeCota= 
						'<input type="hidden" id="nomeCota' + index + '" name="descricaoCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputNumeroCota= 
					'<input type="hidden" id="numCota' + index + '" name="numCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamdaEncalhe+'"/>';
					
				row.cell.numeroCota = row.cell.numeroCota + inputNumeroCota + inputNomeCota  + inputHiddenCodigoChamadaEncalhe;	
				row.cell.qntExemplares = row.cell.qntExemplares + inputQuantidadeExemplares;
				row.cell.sel = inputCheck;
			});
			
			EncalheAntecipado.setHiddenFornecedor();
			EncalheAntecipado.setHiddenNumeroEdicao();
			EncalheAntecipado.setHiddenProduto();
			
			$("#grids").show();
			$("#gridAntecipada").show();
			$("#gridPesquisaCota").hide();
			
			EncalheAntecipado.tipoPesquisaSelecionado = "";
			
			return resultado.tableModel;
		},
		
		pesquisarCotaErrorCallBack: function (idInputCkeck,idInputExemplares,idInputCota,indexLinha){
			
			var exemplares = eval( ($(idInputExemplares).val()=="")?0:$(idInputExemplares).val());
			
			var check  = $(idInputCkeck).attr('checked');
			
			EncalheAntecipado.limparLinha(indexLinha);
			
			if(exemplares > 0 &&  check == "checked"){
			
				EncalheAntecipado.totalCota -= 1;
				EncalheAntecipado.totalExemplares -= exemplares;
				
				EncalheAntecipado.atribuirValorQntCotas();
				EncalheAntecipado.atribuirValorQntExemplares();
			}
			
			$(idInputCkeck).attr("checked",false);
			$(idInputCkeck).attr("disabled","disabled");
			$(idInputExemplares).val("");
			
		},
		
		pesquisarCotaSuccessCallBack: function (idInputCkeck,idInputExemplares,
												idInputCota,indexLinha){
			
			$(idInputCkeck).removeAttr("disabled"); 
			
			EncalheAntecipado.limparLinha(indexLinha);
			
			var param = [{name:"numeroCota",value:$(idInputCota).val()},
			             {name:"codigoProduto", value:EncalheAntecipado.getHiddenProduto()},
			             {name:"numeroEdicao",value:EncalheAntecipado.getHiddenNumeroEdicao()},
			             {name:"fornecedor",value:EncalheAntecipado.getHiddenFornecedor()},
			             {name:"programacaoRealizada",value:EncalheAntecipado.getProgramacaoRealizada()}];
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/obterQuantidadeExemplares' />",
					param, 
					function (result){
				
						$(idInputExemplares).val(result.quantidade);
						$("#codigoChamadaAntecipada" + indexLinha).val(result.idChamadaEncalhe);
						
						var check  = document.getElementById("sel").checked ; 
						
						if(check){
							
							EncalheAntecipado.calcularTotalCota(idInputExemplares, $(idInputCkeck));
							$(idInputCkeck).attr("checked",true);
							$("#sel").attr("checked",true);
						}
					}, 
					function (result){
						
						$(idInputCkeck).attr("checked",false);
						$(idInputCkeck).attr("disabled","disabled");
						$(idInputExemplares).val("");
					});	
		},
		
		executarPreProcessamentoGridCota:function(resultado){
			
			EncalheAntecipado.limparGridCota();
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#grids").hide();

				return resultado;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var idCheck = "chCota"+index;
				
				var paramIdCheck = '\'#chCota' + index +'\','+'\'#qntExemplares' + index + '\','+'\'#numCota' + index + '\','+index;
				
				var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';

				var parametroPesquisaCota = '\'#numCota' + index + '\', \'#descricaoCota'+ index + '\', false,function(){EncalheAntecipado.pesquisarCotaSuccessCallBack('+paramIdCheck+')}, function(){ EncalheAntecipado.pesquisarCotaErrorCallBack('+paramIdCheck+')}';
				
				var inputCodigoCota = 
					'<input type="text" id="numCota' + index + '" name="numCota" style="width:80px; float:left; margin-right:10px;" maxlenght="255" onchange="cota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" />';

				var parametroAutoCompleteCota = '\'#descricaoCota' + index + '\', true';

				var inputDescricaoCota = 
					'<input type="text" id="descricaoCota' + index + '" name="descricaoCota" style="width:580px;" maxlenght="255" onkeyup="cota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="cota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';

				var inputQuantidadeExemplares = 
					'<input type="text" id="qntExemplares' + index + '" name="qntExemplares" disabled="disabled" style="width:80px; text-align: center"   />';
				
				var parametroCheckbox = '\'#qntExemplares' + index + '\', this';
					
				var inputCheck = '<input disabled="disabled" type="checkbox" id="'+idCheck+'" name="'+EncalheAntecipado.groupNameCheckGridCota+'" onclick="EncalheAntecipado.calcularTotalCota( '+parametroCheckbox +')" />';	
				
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamdaEncalhe+'"/>';
				
				row.cell.cota = inputCodigoCota + hiddenId + inputHiddenCodigoChamadaEncalhe;
				row.cell.nome = inputDescricaoCota;
				row.cell.qtdeExemplares = inputQuantidadeExemplares;	
				row.cell.sel = inputCheck;
			});
			
			EncalheAntecipado.setHiddenFornecedor();
			EncalheAntecipado.setHiddenNumeroEdicao();
			EncalheAntecipado.setHiddenProduto();
			
			$("#grids").show();
			$("#gridAntecipada").hide();
			$("#gridPesquisaCota").show();
			
			EncalheAntecipado.tipoPesquisaSelecionado = EncalheAntecipado.tipoPesquisaGridCota;
			
			$("input[name='numCota']").numeric();
			
			return resultado;
		},
		
		calcularTotalGridCota: function (idInputExemplares, inputCkeck){
			
			var check  = document.getElementById("sel").checked ; 
			
			if(check){

				var exemplares = eval( ($(idInputExemplares).val()=="")?0:$(idInputExemplares).val());
				var checked = $("input[name=" + EncalheAntecipado.groupNameCheck + "]:checked").length;
				
				EncalheAntecipado.totalExemplares = ($("input[id^='qntExemplar']").sum() - exemplares);	
				EncalheAntecipado.totalCota = eval(checked);
			
				EncalheAntecipado.atribuirValorQntCotas();
				EncalheAntecipado.atribuirValorQntExemplares();		

				EncalheAntecipado.desmarcarCheckTodos();				
				
			}else{
				EncalheAntecipado.calcularTotalCota(idInputExemplares, inputCkeck);
			}	
		},
		
		calcularTotalCota: function(idExemplares,input){
			
			var exemplares = eval( ($(idExemplares).val()=="")?0:$(idExemplares).val());
			
			if(input.checked == false){
				EncalheAntecipado.totalCota -= 1;
				EncalheAntecipado.totalExemplares -= exemplares;	
			}
			else{
				
				EncalheAntecipado.totalCota += 1;
				EncalheAntecipado.totalExemplares += exemplares;
			}
			
			EncalheAntecipado.atribuirValorQntCotas();
			EncalheAntecipado.atribuirValorQntExemplares();
			
			EncalheAntecipado.desmarcarCheckTodos();	
		},
		
		atribuirValorQntExemplares: function (){
			$("#idTotalExemplares").val(EncalheAntecipado.totalExemplares);
		},
		
		atribuirValorQntCotas: function (){
			$("#idTotalCotas").val(EncalheAntecipado.totalCota);
		},
		
		desmarcarCheckTodos: function (){
			$('#sel').attr("checked",false);
		},
		
		obterParametrosGrid: function(grid){
			
			var linhasDaGrid = $('#'+grid+' tr');
			
			var listaChamadaEncalheAntecipada = "";
			
			var groupName = (EncalheAntecipado.nameGridPesquisaCota == grid)
							? EncalheAntecipado.groupNameCheckGridCota
									:EncalheAntecipado.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(EncalheAntecipado.nameGridPesquisaCota == grid)?3:4];
				var colunaCota = linha.find("td")[ (EncalheAntecipado.nameGridPesquisaCota == grid)?0:1];
				var colunaNomeCota =  linha.find("td")[ (EncalheAntecipado.nameGridPesquisaCota == grid)?1:2];
				var colunaQntExemplares =  linha.find("td")[ (EncalheAntecipado.nameGridPesquisaCota == grid)?2:3];
				
				var id = $(colunaCota).find("div").find('input[name="id"]').val();
				
				var codigoCota = $(colunaCota).find("div").find('input[name="numCota"]').val();
				
				var nomeCota = $(colunaNomeCota).find("div").find('input[name="descricaoCota"]').val();
				
				var qntExemplares =  $(colunaQntExemplares).find("div").find('input[name="qntExemplares"]').val();
			
				var codigoChamdaEncalhe = $(colunaCota).find("div").find('input[name="codigoChamadaAntecipada"]').val();
				
				var check  = $(colunaCheck).find("div").find('input[name="'+groupName+'"]');
				
				if (EncalheAntecipado.isAtributosVaziosOrSelecionados(codigoCota,check)){
					return true;
				}
				
				var cotaSelecionada = 'listaChamadaEncalheAntecipada[' + index + '].codigoProduto=' + EncalheAntecipado.getHiddenProduto() + '&';

				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].numeroEdicao=' + EncalheAntecipado.getHiddenNumeroEdicao() + '&';

				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].numeroCota=' + codigoCota  + '&';
				
				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].nomeCota=' + nomeCota  + '&';
				
				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].id=' + id  + '&';
				
				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].qntExemplares =' + qntExemplares  + '&';
				
				cotaSelecionada += 'listaChamadaEncalheAntecipada[' + index + '].codigoChamdaEncalhe =' + codigoChamdaEncalhe  + '&';
				
				

				listaChamadaEncalheAntecipada = (listaChamadaEncalheAntecipada + cotaSelecionada);
				
			});
			
			return listaChamadaEncalheAntecipada;
		},
		
		isAtributosVaziosOrSelecionados: function (codigoCota,inputCheck){
			
			var check  = inputCheck.attr('checked');
			
			return (typeof check == "undefined" || !$.trim(codigoCota));
		},
		
		limparGridPesquisaCota: function (){
			 $('#'+ EncalheAntecipado.nameGridPesquisaCota+' tr').remove();
		},
		
		limparGridCota: function (){
			$('#'+ EncalheAntecipado.nameGrid +' tr').remove();
		},
		
		formatarCampos: function (){
			
			$("input[name='numCota']").numeric();
			$("input[name='descricaoCota']").autocomplete({source: ""});
		},
		
		exportar:function(fileType) {
			
			var paramDataProgaramada = "&dataProgaramada=" + $("#dataProgramada").val();
			
			if (EncalheAntecipado.tipoPesquisaSelecionado == EncalheAntecipado.tipoPesquisaGridCota){
				
				if(!EncalheAntecipado.isItensSelecionados()){
					return;
				}
				
				var listaChamadaEncalheAntecipada = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGridPesquisaCota);
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/validarCotasPesquisa'/>",
						 listaChamadaEncalheAntecipada, function (result){
					
					var paramProduto = "&codigoProduto="+EncalheAntecipado.getHiddenProduto();
					var paramEdicao = "&numeroEdicao=" +EncalheAntecipado.getHiddenNumeroEdicao();
					var paramFornecedor = "&fornecedor="+EncalheAntecipado.getHiddenFornecedor();
					
					window.location = 
						contextPath + "/devolucao/chamadaEncalheAntecipada/exportarPesquisaCotas?fileType=" 
								+ fileType + paramDataProgaramada
								+paramProduto + paramEdicao + paramFornecedor;
				},
				EncalheAntecipado.tratarErroPesquisaCota,true);
			}
			else{
				
				window.location = 
					contextPath + "/devolucao/chamadaEncalheAntecipada/exportar?fileType=" + fileType + paramDataProgaramada;
			}
		},
		
		recarregarComboRotas:function(idRoteiro){
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/recarregarListaRotas' />",
					[{name:"roteiro",value:idRoteiro}], function(result){
				
				var comboRotas =  montarComboBox(result, true);
				
				$("#rota").html(comboRotas);	
			});
		},
		
		recarregarComboRoteiroRotas:function(idBox){
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/recarregarRoteiroRota' />",
					[{name:"idBox",value:idBox}], function(result){
				
				var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
				var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
				
				$("#rota").html(comboRotas);
				$("#roteiro").html(comboRoteiros);
			});
		},
		
		sumarizarCotasSelecionadas:function(grid){
			
			var linhasDaGrid = $('#'+grid+' tr');
			
			var groupName = (EncalheAntecipado.nameGridPesquisaCota == grid)
							? EncalheAntecipado.groupNameCheckGridCota
									:EncalheAntecipado.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(EncalheAntecipado.nameGridPesquisaCota == grid)?3:4];
				var colunaQntExemplares =  linha.find("td")[ (EncalheAntecipado.nameGridPesquisaCota == grid)?2:3];
				
				var qntExemplares =  $(colunaQntExemplares).find("div").find('input[name="qntExemplares"]').val();
			
				var inputCheck  = $(colunaCheck).find("div").find('input[name="'+groupName+'"]');
				
				var check  = inputCheck.attr('checked');
				
				if (typeof check != "undefined"){
					EncalheAntecipado.totalCota += 1;
					EncalheAntecipado.totalExemplares += eval(qntExemplares);
				}
			});
			
			EncalheAntecipado.atribuirValorQntCotas();
			EncalheAntecipado.atribuirValorQntExemplares();
		},
		
		cancelarProgramacaoAntecipacaoEncalhe:function(){
			
			var listaChamadaEncalheAntecipada ="";

			var paramProduto = "&codigoProduto="+EncalheAntecipado.getHiddenProduto();
			var paramEdicao = "&numeroEdicao=" +EncalheAntecipado.getHiddenNumeroEdicao();
			

			if(EncalheAntecipado.tipoPesquisaGridCota == EncalheAntecipado.tipoPesquisaSelecionado){
				
				listaChamadaEncalheAntecipada = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGridPesquisaCota);
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotasPesquisa' />",
						listaChamadaEncalheAntecipada 
						+ paramProduto
						+ paramEdicao,
						function (result){
					 
							 EncalheAntecipado.montarGridPesquisaCotas();
							 EncalheAntecipado.zerarTotais();
							 
							 $("#dialog-novo" ).dialog("close");
							 $("#dialog-cancelamentoCE" ).dialog("close");
							 
							 EncalheAntecipado.desmarcarCheckTodos();
							 EncalheAntecipado.exibirMensagemSucesso(result);
						},
						EncalheAntecipado.tratarErroPesquisaCota,true);	
			}
			else{
				
				var checkTodos  = $("#sel").attr('checked');
				
				if(typeof checkTodos == "undefined" || !checkTodos == 'checked'){
					
					listaChamadaEncalheAntecipada  = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGrid);
					checkTodos="";
				}
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotas' />",
						listaChamadaEncalheAntecipada 
						+ paramProduto
						+ paramEdicao
						+ "&cancelarTodos=" + checkTodos  , 
						function (result){
					
							EncalheAntecipado.pesquisarCotasPorProduto();
							EncalheAntecipado.zerarTotais();
						
							EncalheAntecipado.desmarcarCheckTodos();
							EncalheAntecipado.exibirMensagemSucesso(result);
							
							 $("#dialog-cancelamentoCE" ).dialog("close");
					 
						}, null,true);

			}
			
		},
		
		reprogramarAntecipacaoEncalhe:function(){
			
			var listaChamadaEncalheAntecipada ="";
			
			var dataRecolhimento = $("#dataAntecipacao").val();
			
			var paramProduto = "&codigoProduto="+EncalheAntecipado.getHiddenProduto();
			var paramEdicao = "&numeroEdicao=" +EncalheAntecipado.getHiddenNumeroEdicao();
			var paramDataRecolhimento = "dataRecolhimento=" +dataRecolhimento;
			var paramDataProgramada = "&dataProgramada=" + $("#dataProgramada").val();
			
			if(EncalheAntecipado.tipoPesquisaGridCota == EncalheAntecipado.tipoPesquisaSelecionado){
				
				listaChamadaEncalheAntecipada = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGridPesquisaCota);
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/reprogramarCotasPesquisa' />",
						 listaChamadaEncalheAntecipada 
						 + paramDataRecolhimento
						 + paramProduto
						 + paramEdicao
						 + paramDataProgramada, 
						 function (result){
					 
							 EncalheAntecipado.montarGridPesquisaCotas();
							 EncalheAntecipado.zerarTotais();
							 
							 $("#dialog-novo" ).dialog("close");
							 
							 EncalheAntecipado.desmarcarCheckTodos();
							 EncalheAntecipado.exibirMensagemSucesso(result);
						},
						EncalheAntecipado.tratarErroPesquisaCota,true);	
				 
			}
			else {
				
				var checkTodos  = $("#sel").attr('checked');
				
				if(typeof checkTodos == "undefined" || !checkTodos == 'checked'){
					
					listaChamadaEncalheAntecipada  = EncalheAntecipado.obterParametrosGrid(EncalheAntecipado.nameGrid);
					checkTodos="";
				}
			
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/reprogramarCotas' />",
						listaChamadaEncalheAntecipada 
						+ paramDataRecolhimento
						+ paramProduto
						+ paramEdicao
						+ paramDataProgramada
						+ "&gravarTodos=" + checkTodos , 
						function (result){
							EncalheAntecipado.pesquisarCotasPorProduto();
							EncalheAntecipado.zerarTotais();
							
							 $("#dialog-novo" ).dialog("close");
							 
							 EncalheAntecipado.desmarcarCheckTodos();
							 EncalheAntecipado.exibirMensagemSucesso(result);
					 
						}, null,true);
			}
		},
		
		processarRenderizacaoDeBotoesCE:function(){
			
			if (EncalheAntecipado.getProgramacaoRealizada() == true){
				$("#bt_cancelar_programacao").show();
				$("#bt_reprogramar_CE").show();
				$("#bt_confirmar_novo").hide();
			}
			else{
				$("#bt_cancelar_programacao").hide();
				$("#bt_reprogramar_CE").hide();
				$("#bt_confirmar_novo").show();
			}
		},
		
		exibirDialogCancelamentoCE:function(){
			
			if(!EncalheAntecipado.isItensSelecionados()){
				return;
			}
			
			$("#dialog-cancelamentoCE" ).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
			         {id:"btn_confirma_cancelar_ce",text:"Confirmar",
		        	  click: function() {
		        		  EncalheAntecipado.cancelarProgramacaoAntecipacaoEncalhe();	
		        	  	}
			         },
		        	{id:"btn_cancelar_ce",text:"Cancelar",
			         click:function(){
		        			$( this ).dialog( "close" );
		        		}	  
		        	}  
				],
			});
		},
		
	};
	
$(function() {
	
	    definirAcaoPesquisaTeclaEnter();	
	
		$("#dataAntecipacao").datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]').mask("99/99/9999");
		
		$("#edicao").numeric();
	
		$("#produto").autocomplete({source: ""});
		
		$("#codigoProduto").focus();
		
		$("#ceAntecipadaGrid").flexigrid({
			preProcess:EncalheAntecipado.executarPreProcessamentoCota,
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
	
	$("#ceAntecipadaCotaGrid").flexigrid({
		preProcess:EncalheAntecipado.executarPreProcessamentoGridCota,
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
	
});
	</script>
</head>

<body>
	
	<div id="dialog-novo" title="CE Antecipada">
		
		<jsp:include page="../messagesDialog.jsp" />
			
		<p>Data Antecipada:<input name="dataAntecipacao" type="text" id="dataAntecipacao" style="width:80px;"/></p>
		<p>Confirma a gravação dessas informações? </p>      
	</div>
	
	<div id="dialog-cancelamentoCE" title="CE Antecipada" style="display: none;">
		<p>Deseja cancelar a Programação da CE Antecipada deste Produto? </p>      
	</div>
		
	
	
	<fieldset class="classFieldset">
   	   
   	   <legend> CE Produto</legend>
   	   
   	   <input type="hidden" id="codProdutoHidden" name="codProdutoHidden" value=""/>
   	   <input type="hidden" id="numeroEdicaoHidden" name="numeroEdicaoHidden" value=""/>
   	   <input type="hidden" id="codFornecedorHidden" name="codFornecedorHidden" value=""/>
   	   
   	   <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		
		<tbody>		
		
		 <tr>
		    <td width="54">Código:</td>
		    
		    <td colspan="3">
		    	
		    	<input type="text" name="codigoProduto" id="codigoProduto" class="campoDePesquisa"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
								   									   EncalheAntecipado.pesquisarProdutosErrorCallBack);" />
		    	
		    </td>
		    
		    <td width="54">Produto:</td>
		    <td width="264">
		    	<input type="text" name="produto" id="produto" style="width: 213px;" maxlength="255" class="campoDePesquisa"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
					    	   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
					    	   EncalheAntecipado.pesquisarProdutosErrorCallBack);"/>
		    </td>
		    
		    <td width="42">Edição:</td>
		    <td width="165">
		    	
		    	<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled" class="campoDePesquisa"
						   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
						   										EncalheAntecipado.validarEdicaoSuccessCallBack,
					    	   									EncalheAntecipado.validarEdicaoErrorCallBack);"/>
		    	
		    </td>
		    <td width="112">Data Programada:</td>
		    <td width="106">
		    	<input name="dataProgramada" type="text" id="dataProgramada" style="width:95px; text-align:center;" disabled="disabled" class="campoDePesquisa" />
		    </td>
		  </tr>
		  
		  </tbody>
		</table>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		<tbody>
		  <tr>
		    <td width="58">Box:</td>
		    <td colspan="3">
			    <select name="box" id="box" style="width:150px;" onchange="EncalheAntecipado.recarregarComboRoteiroRotas(this.value)" class="campoDePesquisa">
			      <option selected="selected" value="-1"></option>
			      <option selected="selected">Todos</option>
			      <c:forEach var="box" items="${listaBoxes}">
							<option value="${box.key}">${box.value}</option>
				  </c:forEach>
			    </select>
			</td>
			
			<td width="81">Roteiro:</td>
		    <td width="187">
		    	<select class="campoDePesquisa" name="roteiro" id="roteiro" style="width:150px; float:left; margin-right:5px;" onchange="EncalheAntecipado.recarregarComboRotas(this.value)" >

			      <option selected="selected" value="">Todos</option>
			      <c:forEach var="roteiro" items="${listaRoteiros}">
							<option value="${roteiro.key}">${roteiro.value}</option>
				  </c:forEach>
			    </select>
		    </td>
		    <td width="37">Rota:</td>
		    <td width="155">
		    	<select class="campoDePesquisa" name="rota" id="rota" style="width:150px; float:left; margin-right:5px;"  >

			      <option selected="selected" value="">Todos</option>
			      <c:forEach var="rota" items="${listaRotas}">
							<option value="${rota.key}">${rota.value}</option>
				  </c:forEach>
			    </select>
		    </td>
		    <td width="82">Fornecedor:</td>
		    <td>
		    	<select class="campoDePesquisa" name="fornecedor" id="fornecedor" style="width:130px;">

		      		<option selected="selected">Todos</option>
		      		<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
					</c:forEach>
		    	</select>
		    </td>
		  </tr>
		 
		  <tr>
			
			<td>Munic&iacute;pio:</td>
			
		  	<td colspan="3">
				
				<select class="campoDePesquisa" name="fornecedor" id="municipio" style="width:150px;">
		      		<option selected="selected">Todos</option>
		      		<c:forEach var="municipio" items="${listaMunicipios}">
							<option value="${municipio.key}">${municipio.value}</option>
					</c:forEach>
		    	</select>
				
			</td>
			
			<td>Tipo de Ponto:</td>

			<td>
				<select class="campoDePesquisa" name="tipoPonto" id="tipoPonto" style="width:150px;">
		      		<option selected="selected">Todos</option>
		      		<c:forEach var="tpPonto" items="${listaTipoPonto}">
							<option value="${tpPonto.key}">${tpPonto.value}</option>
					</c:forEach>
		    	</select>
				
			</td>		  	
		  	
			<td align="right">
				<input class="campoDePesquisa" type="checkbox" id="checkCE" name="checkCE">
			</td>

		  	<td>Com CE</td>
			 <td>&nbsp;</td>
		    <td width="136" colspan="2">
		    	<span class="bt_pesquisar">
		    		<a href="javascript:;" onclick="EncalheAntecipado.pesquisar();" id="btn_pesquisa_ce" class="botaoPesquisar">Pesquisar</a>
		    	</span>
		    </td>
		  </tr>
		  </tbody>
        </table>

	 </fieldset>
     
     <div class="linha_separa_fields">&nbsp;</div>
      
          <div class="grids" id="grids" style="display:none;">
			
			 <fieldset class="classFieldset">
       	  		<legend> CE Produto</legend>	  
				  
				  <div class="gridAntecipada" id="gridAntecipada" style="display:none;">
				  	<table class="ceAntecipadaGrid" id="ceAntecipadaGrid"></table>
				  </div>	  
				  
				  <div class="gridPesquisaCota" id="gridPesquisaCota" style="display:none;">
				  	<table class="ceAntecipadaCotaGrid" id="ceAntecipadaCotaGrid"></table>
				  </div>
	          
		          <table width="950" border="0" cellspacing="1" cellpadding="1">
					  
				  		<tr>
						   	<td width="468" valign="top">
							    <span class="bt_confirmar_novo" title="Gravar" id="bt_confirmar_novo">
							    	<a onclick="EncalheAntecipado.exibirDialogData('Novo');" href="javascript:;">
							    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Gravar
							    	</a>
							    </span>
							    
							    <span class="bt_confirmar_novo" title="Reprogramar" id="bt_reprogramar_CE" style="display: block;">
							    	<a onclick="EncalheAntecipado.exibirDialogData('');" href="javascript:;">
							    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Reprogramar
							    	</a>
							    </span>
							
							    <span class="bt_novos" title="Gerar Arquivo">
							    	<a href="javascript:;" onclick="EncalheAntecipado.exportar('XLS')">
							    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
							    	</a>
							    </span>
							
								<span class="bt_novos" title="Imprimir">
									<a href="javascript:;" onclick="EncalheAntecipado.exportar('PDF')">
										<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
									</a>
								</span>
								
								<span title="Cancelar Programação" class="bt_novos" id="bt_cancelar_programacao" style="display: block;">
									<a onclick="EncalheAntecipado.exibirDialogCancelamentoCE();" href="javascript:;">
										<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_bloquear.gif">Cancelar Programação
									</a>
								</span>
								
						    </td>
						      
						    <td width="295">
						    
						    	<fieldset class="box_field">
						          <legend>Totais</legend>
						          <table width="200" cellspacing="2" cellpadding="2" border="0">
						            <tbody><tr>
						              <td width="8">&nbsp;</td>
						              <td width="178">
						                <div class="box_resumo">
						                  <table width="150" cellspacing="1" cellpadding="1" border="0">
						                    <tbody><tr>
						                      <td width="83" height="23"><strong>Cotas:</strong></td>
						                      <td width="60"><input type="text" id="idTotalCotas" style="width:60px; text-align:center;" value="0" disabled="disabled"/></td>
						                      </tr>
						                    <tr>
						                      <td><strong>Exemplares:</strong></td>
						                      <td><input type="text" id="idTotalExemplares" style="width:60px; text-align:center;" value="0"  disabled="disabled"/></td>
						                      </tr>
						                    </tbody></table>
						                  </div>
						                </td>
						              </tr>
						            </tbody></table>
						          
						          </fieldset>
								
			          		</td>
			          		
			      			<td width="177" valign="top">
				        		<span class="bt_sellAll">
				        			<label for="sel">Selecionar Todos</label>
				        			<input type="checkbox" name="Todos" id="sel" onclick="EncalheAntecipado.checkAll(this);" style="float:left;"/>
				        		</span>
			    		     </td>
		      			</tr>
		 		</table>
			</fieldset>
		</div>
		
      
</body>