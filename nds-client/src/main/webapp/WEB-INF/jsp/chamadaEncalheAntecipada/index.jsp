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
				            ];
			return formData;
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
				params: EncalheAntecipado.params(),newp: 1
			});
			
			$("#ceAntecipadaCotaGrid").flexReload();
		},
		
		pesquisarCotasPorProduto:function(){
			
			$("#ceAntecipadaGrid").flexOptions({
				url: "<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisar' />",
				params: EncalheAntecipado.params(),newp: 1
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
	
		exibirDialogData:function(){
			
			if(!EncalheAntecipado.isItensSelecionados()){
				return;
			}
			
			$("#dialog-novo" ).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: {
					"Confirmar": function() {
						EncalheAntecipado.gravar();
						$("#dataAntecipacao").val("");
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
						$("#dataAntecipacao").val("");
					}
				}
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
				 $("#dataProgramada").val(result);
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
					resultado.mensagens.listaMensagens
				);
				
				$("#grids").hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
					
				var parametroCheckbox = '\'#qntExemplar' + index + '\', this';
				
				var inputCheck = '<input type="checkbox" id="ch'+index+'" name="'+EncalheAntecipado.groupNameCheck+'" onclick="EncalheAntecipado.calcularTotalCota( '+parametroCheckbox +')" />';	
				
				var inputQuantidadeExemplares = 
					'<input type="hidden" id="qntExemplar' + index + '" name="qntExemplares" value="'+ row.cell.qntExemplares +'"/>';
						
				var inputNomeCota= 
						'<input type="hidden" id="nomeCota' + index + '" name="descricaoCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputNumeroCota= 
					'<input type="hidden" id="numCota' + index + '" name="numCota" value="'+ row.cell.numeroCota+'"/>';
				
				row.cell.numeroCota = row.cell.numeroCota + inputNumeroCota + inputNomeCota;	
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
			             {name:"fornecedor",value:EncalheAntecipado.getHiddenFornecedor()}];
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/obterQuantidadeExemplares' />",
					param, function (result){
				
						$(idInputExemplares).val(result);
						
						var check  = document.getElementById("sel").checked ; 
						
						if(check){
							EncalheAntecipado.calcularTotalCota(idInputExemplares, $(idInputCkeck));
							$(idInputCkeck).attr("checked",true);
							$("#sel").attr("checked",true);
						}
					}, function (result){
						
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
					
				row.cell.cota = inputCodigoCota + hiddenId;
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
		}
		
	};
	
	
$(function() {
		
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
		onSuccess: EncalheAntecipado.formatarCampos,
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
		
		<div class="effectDialog ui-state-highlight ui-corner-all"
			style="display: none; position: absolute; z-index: 2000; width: 250px;">
			<p><span style="float: left;" class="ui-icon ui-icon-info"></span>
				<b class="effectDialogText"></b>
			</p>
		</div>
	
		<p>Data Antecipada:<input name="dataAntecipacao" type="text" id="dataAntecipacao" style="width:80px;"/></p>
		<p>Confirma a gravação dessas informações? </p>      
	</div>
	
	<fieldset class="classFieldset">
   	   
   	   <legend> CE Produto</legend>
   	   
   	   <input type="hidden" id="codProdutoHidden" name="codProdutoHidden" value=""/>
   	   <input type="hidden" id="numeroEdicaoHidden" name="numeroEdicaoHidden" value=""/>
   	   <input type="hidden" id="codFornecedorHidden" name="codFornecedorHidden" value=""/>
   	   
   	   <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		    <td width="54">Código:</td>
		    
		    <td colspan="3">
		    	
		    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
								   									   EncalheAntecipado.pesquisarProdutosErrorCallBack);" />
		    	
		    </td>
		    
		    <td width="76">Produto:</td>
		    <td width="258">
		    	<input type="text" name="produto" id="produto" style="width: 213px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
					    	   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
					    	   EncalheAntecipado.pesquisarProdutosErrorCallBack);"/>
		    </td>
		    
		    <td width="45">Edição:</td>
		    <td width="100">
		    	
		    	<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
						   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
						   										EncalheAntecipado.validarEdicaoSuccessCallBack,
					    	   									EncalheAntecipado.validarEdicaoErrorCallBack);"/>
		    	
		    </td>
		    <td width="110">Data Programada:</td>
		    <td width="115">
		    	<input name="dataProgramada" type="text" id="dataProgramada" style="width:95px; text-align:center;" disabled="disabled" />
		    </td>
		  </tr>
		  <tr>
		    <td>Box:</td>
		    <td colspan="3">
			    <select name="box" id="box" style="width:100px;">
			      <option selected="selected" value="-1"></option>
			      <option selected="selected">Todos</option>
			      <c:forEach var="box" items="${listaBoxes}">
							<option value="${box.key}">${box.value}</option>
				  </c:forEach>
			    </select>
			</td>
		    <td>Fornecedor:</td>
		    <td>
		    	<select name="fornecedor" id="fornecedor" style="width:220px;">
		      		<option selected="selected">Todos</option>
		      		<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
					</c:forEach>
		    	</select>
		    </td>
		    <td>&nbsp;</td>
		    <td colspan="2">&nbsp;</td>
		    <td>
		    	<span class="bt_pesquisar">
		    		<a href="javascript:;" onclick="EncalheAntecipado.pesquisar();">Pesquisar</a>
		    	</span>
		    </td>
		  </tr>
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
						   	<td width="448" valign="top">
							    <span class="bt_confirmar_novo" title="Gravar">
							    	<a onclick="EncalheAntecipado.exibirDialogData();" href="javascript:;">
							    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Gravar
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
						    </td>
						      
						    <td width="452"> 
							     <div class="box_resumo" style="width: 300px;">
						          <table width="200" border="0" cellspacing="2" cellpadding="2">
						            <tr>
						              	<td width="8"> <strong>Totais</strong></td>
						              	<td width="178">
						                  <table width="150" border="0" cellspacing="1" cellpadding="1">
						                    <tr>
						                      <td width="73" height="23"><strong>Cotas:</strong></td>
						                      <td width="70">
						                      	<input type="text" id="idTotalCotas" style="width:60px; text-align:center;" value="0" disabled="disabled"/>
						                      </td>
						                      <td width="70"><strong>Exemplares:</strong></td>
						                      <td width="70">
						                      	<input type="text" id="idTotalExemplares" style="width:60px; text-align:center;" value="0"  disabled="disabled"/>
						                      </td>
						                    </tr>
						                    </table>
						                </td>
						              </tr>
						            </table>
					           </div>
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
