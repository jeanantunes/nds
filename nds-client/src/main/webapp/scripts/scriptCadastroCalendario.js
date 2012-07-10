var CadastroCalendario = {
		
		dates : [],

		popup : function(date, dates) {		
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
			
			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:580,
				width:700,
				modal: true,
				buttons: {
					"Confirmar": function() {
						CadastroCalendario.novoFeriado();
						D = date.match(/\d+/g);
						date = new Date(+D[2], D[1]-1, +D[0]);
						dates.push(date);
						highlightDays(date);
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 3000, callback);
						console.log(dates);					
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
			$( "#dtFeriado" ).val(date);
			
		},
		
		novoFeriado : function(){
			
			var dtFeriado = $("#dtFeriado").val();
			var tipoFeriado = $("#novoTipoFeriado").val();
			var descricao = $("#novoDescricao").val();
			var opera = $("#novoOpera").is(":checked");novoOpera
			var efetuaCobranca = $("#novoEfetuaCobranca").is(":checked");
			var repeteAnualmente = $("#novoRepeteAnualmente").is(":checked");
			
			
			$.postJSON("<c:url value='/administracao/cadastroCalendario/novoFeriado'/>",
					   "dtFeriado="+dtFeriado+
					   "&tipoFeriado="+ tipoFeriado,
					   function(result) {
				           fecharDialogs();
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   //pesquisar();
		               },
					   null,
					   true);
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
	        for (var i = 0; i < dates.length; i++) {
	                if (dates[i] == date) {
	                        //$("#feriados .ui-datepicker-calendar tbody tr td a").addClass('ui-state-active');
	                }
	        }
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

		onSelect: function(dateText, inst) {
			popup(dateText, dates);
		}
	});

	$( "#dtFeriado" ).datepicker({
		showOn: "button",
		dateFormat: "dd/mm/yy",
		buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true
	});
	$( "#dtFeriado1" ).datepicker({
		showOn: "button",
		dateFormat: "dd/mm/yy",
		buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true
	});
	
	
	
});	