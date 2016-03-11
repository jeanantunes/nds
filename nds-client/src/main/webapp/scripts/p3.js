var p3Controller = $.extend(true,{
	baseUrlForRequest : contextPath + "/financeiro/p3/",
	
	init : function init() {
		
		$("#filtro_data_inicial_p3").datepicker({
			  showOn: "button"
			, buttonImage: contextPath + "/images/calendar.gif"
			, buttonImageOnly: true
			, changeYear: true
			}
		);
		
		$("#filtro_data_final_p3").datepicker({
			  showOn: "button"
			, buttonImage: contextPath + "/images/calendar.gif"
			, buttonImageOnly: true
			, changeYear: true
			}
		);
	},
	
	validarDados : function(dateInicial, dateFinal, f) {

		var int_dateInicial, int_dateFinal; 

		if(dateInicial == "" || dateFinal == ""){
			  exibirMensagem("WARNING", ["Escolha uma data válida."]);
		  }
		else if($(f+" .opcaoDeRelatorio:checked").length==0){
			exibirMensagem("WARNING", ["Escolha uma opção de relatório."]);
		}
		else{
			 
			  int_dateInicial = parseInt(dateInicial.split("/")[2].toString() + dateInicial.split("/")[1].toString() + dateInicial.split("/")[0].toString());
			  int_dateFinal = parseInt(dateFinal.split("/")[2].toString() + dateFinal.split("/")[1].toString() + dateFinal.split("/")[0].toString());
			  
			  if(int_dateInicial == "" || int_dateFinal == ""){
				  	exibirMensagem("WARNING", ["Escolha uma data válida."]);
			    }else{
			    	if(int_dateInicial > int_dateFinal){
			    		exibirMensagem("WARNING", ["Data inicial maior que final. Escolha uma data válida."]);
			    	}else{
			    		$.post(contextPath + "/financeiro/p3/validarQtdRegistros",
			    				[{name:'dataInicial', value: dateInicial},
			    				 {name:'dataFinal', value: dateFinal},
			    				 {name:'opcaoDeRelatorio', value: $(f+" .opcaoDeRelatorio:checked").val()}],	
			    				 function(result) {

			    			if(result.quantidadeGerada>0){
			    				
			    				data = [];
								data.push({'name':'dataInicial', 'value':dateInicial});
								data.push({'name':'dataFinal', 'value':dateFinal});
								data.push({'name':'opcaoDeRelatorio', 'value':$(f+" .opcaoDeRelatorio:checked").val()});
								
								$.fileDownload(contextPath + "/financeiro/p3/gerarP3", {
						            httpMethod : "GET",
						            data : data,
						            failCallback : function(arg) {
						                exibirMensagem("WARNING", ["Erro ao gerar o arquivo P3!"]);
						            }
								});
			    				
			    				
			    				exibirMensagem("SUCCESS", ["Arquivo gerado com sucesso!! Efetuando o download, aguarde... "]);
			    			}else{
			    				exibirMensagem("WARNING", ["Não há Lançamentos para este período!"]);							
			    			}
			    		},
			    		null,
			    		true
			    		);
			    	}
			    }
		  }
	},
	
}, BaseController);

p3Controller.init();

//@ sourceURL=p3.js
