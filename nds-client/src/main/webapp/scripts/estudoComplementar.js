
var estudoComplementarController =$.extend(true,  {
	
	gerarEstudoComplementar : function(){

		var codigoEstudo = $('#codigoEstudo').val(), 
			reparteSobra = $('#reparteSobra').val(),
			tipoSelecao  =  $('#tipoSelecao :selected').val(),
			isMultiplo = false;
		
		if (tipoSelecao==0){
			exibirMensagem("WARNING", ["Selecione o tipo de base."]);
			$('#tipoSelecao').focus();
			return;
		}
		
		if(codigoEstudo==""){
			exibirMensagem("WARNING", ["Informe o número do estudo base."]);
			$('#codigoEstudo').focus();
			return;
		}
		var reparteCota  = $('#reparteCota').val();
		
		
		if ($('#checkboxDistMult').attr("checked")==undefined){
			if ($('#reparteCota').val()<0){
				exibirMensagem("WARNING", ["Reparte cota negativo."]);
				return
			}

			if ($('#reparteCota').val()=="" || $('#reparteCota').val()==0){
				exibirMensagem("WARNING", ["Informe o valor de reparte por Cota."]);
				return;
			}
		}else {
			reparteCota = $('#distrMult').val();
			isMultiplo = true;
		}
		
		if ($('#reparteLancamento').val()<0){
			exibirMensagem("WARNING", ["Reparte Lancamento negativo."]);
			return
		}
		
		if ($('#reparteLancamento').val()=="" || $('#reparteLancamento').val()==0){
			exibirMensagem("WARNING", ["Informe o valor de Lancamento"]);
			return;
		}

		var reparteLancamento =$('#reparteLancamento').val();

		if ($('#reparteDistribuicao').val()<0){
			exibirMensagem("WARNING", ["Reparte Distribuido negativo!"]);
			return
		}
		
		if ($('#reparteDistribuicao').val()==0 || $('#ReparteDistribuicao').val()==""){
			exibirMensagem("WARNING", ["Informe o valor de reparte para distribuição."]);
			return;
		}
		var reparteDistribuicao = $('#reparteDistribuicao').val();
		if ($('#checkboxDistMult').attr("checked")!=undefined){
			if ($('#distrMult').val=="0" || $('#distrMult').val==""){
				exibirMensagem("WARNING", ["Informe o fator de distribuição multipla."]);
				return;
				
			}	
		}

		if ($('#reparteSobra').val()<0){
			exibirMensagem("WARNING", ["A sobra negativa."]);
			return
		}
		
		var dados = [];
	                  dados.push({name:"parametros.reparteCota",          value: reparteCota});
	                  dados.push({name:"parametros.codigoEstudo",         value: codigoEstudo});
	                  dados.push({name:"parametros.reparteDistribuicao",  value: reparteDistribuicao});
	                  dados.push({name:"parametros.reparteLancamento",    value: reparteLancamento});
	                  dados.push({name:"parametros.reparteSobra",         value: reparteSobra});
	                  dados.push({name:"parametros.tipoSelecao",          value: tipoSelecao});
	                  dados.push({name:"parametros.idLancamento",         value: $('#idLancamento').val()});
	                  dados.push({name:"parametros.idProdutoEdicao",      value: $('#idProdutoEdicao').val()});
	                  dados.push({name:"parametros.idCopia",      		  value: $('#idCopia').val()});
	                  dados.push({name:"parametros.multiplo",             value: isMultiplo});
	                  dados.push({name:"parametros.dataLancamento",             value: $('#publicacaoDataLncto').text()});
	      					           
		 $.ajax({
			 url:  'lancamento/gerarEstudo',
			 data:  dados ,
	         type: "POST",
	         
	         success: function(data) {
	         
	         	 if (data.mensagens) {
        
	         		 exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
	         		 
	         	 } else {
	         		 
	         		 exibirMensagem("SUCCESS", ["Estudo Complementar Gerado"]);
	         		 
	         		 $('#idEstudoComplementar').html(data);
	         		 
	         		if(typeof(matrizDistribuicao)=="object"){
	            		matrizDistribuicao.carregarGrid();
	         		}
	 	         }
		     }
		 });
	},

	
//----------------------------
consultarEstudo:function (obj) { 
    if(obj.value!=""){
		var codigoEstudo = $('#codigoEstudo').val(); 
		
	
		$.ajax({
		dataType: "json",
		url: 'lancamento/pesquisaEstudoBase/' + codigoEstudo ,
		
		success: function(json){
				if (json.mensagens) {
					exibirMensagem(json.mensagens.tipoMensagem, json.mensagens.listaMensagens);
					estudoComplementarController.voltar();
				}else{
					$('#idEstudoComplementar').html(''); 
					$('#idProduto').html(json.baseEstudoVO.codigoProduto); 
					$('#nomeProdutoLabel').html(json.baseEstudoVO.nomeProduto);
					
					$('#numeroEdicao').html(json.baseEstudoVO.numeroEdicao);
					$('#nomeClassificacao').html(json.baseEstudoVO.nomeClassificacao);
					$('#publicacao').html(json.baseEstudoVO.codigoProduto);
					$('#publicacaoNomeProduto').html(json.baseEstudoVO.nomeProduto);
					$('#publicacaoEdicao').html(json.baseEstudoVO.numeroEdicao);
					$('#publicacaoPEB').html(json.baseEstudoVO.idPEB);
					$('#publicacaoNomeFornecedor').html(json.baseEstudoVO.nomeFornecedor);
					$('#publicacaoDataLncto').html(json.baseEstudoVO.dataLncto);
					$('#publicacaoDataRclto').html(json.baseEstudoVO.dataRclto);
					$('#publicacaoClassificacao').html(json.baseEstudoVO.nomeClassificacao);
					$('#EC-segmento').html(json.baseEstudoVO.segmentoDoProduto);
				}
			}
		});
    }
    else{

		$('#idEstudoComplementar').html(""); 
		$('#idProduto').html(""); 
		$('#nomeProdutoLabel').html("");
		
		$('#numeroEdicao').html("");
		$('#nomeClassificacao').html("");
		$('#publicacao').html("");
		$('#publicacaoNomeProduto').html("");
		$('#publicacaoEdicao').html("");
		$('#publicacaoPEB').html("");
		$('#publicacaoNomeFornecedor').html("");
		$('#publicacaoDataLncto').html("");
		$('#publicacaoDataRclto').html("");
		$('#publicacaoClassificacao').html("");
		$('#reparteDistribuicao').val("");
		$('#reparteLancamento').val("");
		$('#tipoSelecao').val('selected');
		$('#reparteCota').val(2);
		$('#EC-segmento').val('');
		
    }
},

	analisar : function() {
		//testa se registro selecionado possui estudo gerado
		if ($('#idEstudoComplementar').html() == null || $('#idEstudoComplementar').html() == "") {
			exibirMensagem("WARNING",["Gere o estudo antes de fazer a análise."]);
			return;
		} else {
			// Deve ir direto para EMS 2031
			matrizDistribuicao.redirectToTelaAnalise($('#idEstudoComplementar').html());
		}
	},

	voltar : function() {
		
		$(".ui-tabs-selected").find("span").click();
		$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
	}
	

}, BaseController);

//@ sourceURL=estudoComplementar.js