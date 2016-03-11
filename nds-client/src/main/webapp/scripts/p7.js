var p7Controller = $.extend(true,{
	baseUrlForRequest : contextPath + "/financeiro/p7/",
	
	init : function init() {
		
//		$("#filtro_data_inicial_p3").datepicker({
//			  showOn: "button"
//			, buttonImage: contextPath + "/images/calendar.gif"
//			, buttonImageOnly: true
//			, changeYear: true
//			}
//		);
//		
//		$("#filtro_data_final_p3").datepicker({
//			  showOn: "button"
//			, buttonImage: contextPath + "/images/calendar.gif"
//			, buttonImageOnly: true
//			, changeYear: true
//			}
//		);
		
		var month,year;
		var dateInventario;
		
		$( "#datepickerMesAno").datepicker({
			changeMonth: true,
	        changeYear: true,
			showOn: "button",
			dateFormat: 'MM/yy',
			monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
	        monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			onClose: function(dateText, inst) { 
		        month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
		        year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		        dateInventario = new Date(year, month, 1);
		        
		        $("#mesInput").val(month);
		        $("#anoInput").val(year);
		        $("#mesInputXLS").val(month);
		        $("#anoInputXLS").val(year);
		        
		        $(this).datepicker('setDate', dateInventario);
		    },
		    beforeShow: function() {
		    	$(".ui-datepicker-calendar").hide();
		    }
		});
		
		$( "#datepickerMesAno").datepicker($.datepicker.regional["pt-BR"]);

	},
	
	validarDados : function (month, year, f, tipo){
		
		var dateInventario = new Date(year, month, 1);
		
		if(month==null && year==null){
			exibirMensagem("WARNING", ["Escolha uma data válida"]);
		}else if(dateInventario.getTime() > new Date().getTime()){
			exibirMensagem("WARNING", ["Mês/Ano maior que o mês atual."]);
		}else{
			$.postJSON(contextPath + "/financeiro/integracaoFiscalP7/verificarExportar", 
					$(f).serialize(),
					function(result) {
							console.log(result);
							
							if(result.quantidadeGerada>0){
								
								data = [];
								data.push({'name':'mes', 'value':month});
								data.push({'name':'ano', 'value':year});
								
								
								if(tipo === 'xls'){
									
									$.fileDownload(contextPath + "/financeiro/integracaoFiscalP7/exportarXLS", {
										httpMethod : "POST",
										data : data,
										failCallback : function(arg) {
											exibirMensagem("WARNING", ["Erro ao gerar o arquivo P7!"]);
										}
									});
									
								}else{
									$.fileDownload(contextPath + "/financeiro/integracaoFiscalP7/exportar", {
										httpMethod : "POST",
										data : data,
										failCallback : function(arg) {
											exibirMensagem("WARNING", ["Erro ao gerar o arquivo P7!"]);
										}
									});
								}
								
								//$(f).submit();
								exibirMensagem("SUCCESS", [" Aguarde enquanto o arquivo é gerado e disponibilizado para download! "]);
							}else{
								exibirMensagem("WARNING", ["Nenhum dado encontrado para inventário."]);							
							}
				   	},
				   null,
				   true
			);
			
		}
	},
	
}, BaseController);

//p3Controller.init();

//@ sourceURL=p7.js
