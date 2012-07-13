var CadastroCalendario = {

		datasDestacar :  {'2012/12/21':'Aniversario Michelzo' , '2012/8/6':'some other description'},

		popupCadastroFeriado : function(date, dates) {	
			
			$( "#dialog-novo" ).dialog({
				
				resizable: false,
				
				height:580,
				
				width:700,
				
				modal: true,
				
				buttons: {
					
					"Confirmar": function() {
						
						CadastroCalendario.cadastrarNovoFeriado();
						
						$( this ).dialog( "close" );
						
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
			
			$( "#dtFeriado" ).val(date);
			
		},
		
		cadastrarNovoFeriado : function(){
			
			var dtFeriado = $("#dtFeriado").val();
			var tipoFeriado = $("#tipoFeriado").val();
			var descricao = $("#descricao").val();
			var indOpera = $("#indOpera").is(":checked");
			var indEfetuaCobranca = $("#indEfetuaCobranca").is(":checked");
			var indRepeteAnualmente = $("#indRepeteAnualmente").is(":checked");
			
			var parametrosPesquisa = [{'dtFeriado' : dtFeriado},
			                     {'tipoFeriado' : tipoFeriado},
			                     {'descricao' : descricao},
			                     {'indOpera' : indOpera},
			                     {'indEfetuaCobranca' : indEfetuaCobranca},
			                     {'repeteAnualmente' : indRepeteAnualmente}];
			
			$.postJSON("<c:url value='/administracao/cadastroCalendario/novoFeriado'/>",
					parametrosPesquisa,
					
					   function(result) {
				           
							fecharDialogs();
				
							exibirMensagem(tipoMensagem, listaMensagens);
					       
		               }, null, true);
		},
		
		fecharDialogs : function() {
			$( "#dialog-novo" ).dialog( "close" );
		},
		
		popup_bt : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-editar" ).dialog({
				resizable: false,
				height:370,
				width:430,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		
		
		popup_excluir : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-excluir" ).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		
		highlightDays : function(date) {
			
			var search = date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + (date.getDate());
			
			jQuery.inArray(search, CadastroCalendario.datasDestacar);
			
			if (CadastroCalendario.datasDestacar[search]) {
		           return [true, 'highlight', CadastroCalendario.datasDestacar[search] || ''];
		    }
	        
	        return [true, ''];
	        
	     } 
		
};

$(document).ready(function() {

	
	
	$( "#feriados" ).datepicker({
		
		numberOfMonths: 12,
		showButtonPanel: false,
		altField: "#alternate",
		dateFormat: "dd/mm/yy",
		dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado','Domingo'],
        dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
        dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
        
        monthNames: ['<a href="index.htm">Janeiro</a>',
                     '<a href="index.htm">Fevereiro</a>',
                     '<a href="index.htm">Março</a>',
                     '<a href="index.htm">Abril</a>',
                     '<a href="index.htm">Maio</a>',
                     '<a href="index.htm">Junho</a>',
                     '<a href="index.htm">Julho</a>',
                     '<a href="index.htm">Agosto</a>',
                     '<a href="index.htm">Setembro</a>',
                     '<a href="index.htm">Outubro</a>',
                     '<a href="index.htm">Novembro</a>',
                     '<a href="index.htm">Dezembro</a>'
                     ],
        monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set', 'Out','Nov','Dez'],

        beforeShowDay: CadastroCalendario.highlightDays,
        
		onSelect: CadastroCalendario.popupCadastroFeriado
		
		
	});

	$( "#dtFeriado" ).datepicker({
		showOn: "button",
		dateFormat: "dd/mm/yy",
		buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true
	});

	
	$(".diaFeriadoGrid").flexigrid({
		url : '../xml/diasFeriado-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Dia',
			name : 'dia',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Tipo Feriado',
			name : 'tipo',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cidade',
			name : 'cidade',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Descrição',
			name : 'descricao',
			width : 180,
			sortable : true,
			align : 'left',
		}, {
			display : 'Opera',
			name : 'opera',
			width : 30,
			sortable : true,
			align : 'center'
		}, {
			display : 'Cobrança',
			name : 'efetuaCobranca',
			width : 45,
			sortable : true,
			align : 'center'
		}, {
			display : 'Anual',
			name : 'repeteAnual',
			width : 30,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 650,
		height : 120
	});
	
	

         



	
	
	
	
	
});	