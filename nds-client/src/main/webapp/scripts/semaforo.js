var semaforoController = $.extend(true, {
	
	path : contextPath + '/devolucao/semaforo',
	
	init : function() {

		$(".statusProcessosEncalheGrid", this.workspace).flexigrid({
			url : semaforoController.path + "/statusProcessosEncalhe",
			preProcess: semaforoController.preProcessarGrid,
			dataType : 'json',
			colModel : [{
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				align : 'left'
			}, {
				display : 'Início',
				name : 'horaInicio',
				width : 80,
				align : 'center'
			}, {
				display : 'Fim',
				name : 'horaFim',
				width : 80,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 100,
				align : 'center'
			}, {
				display : 'Mensagem',
				name : 'mensagem',
				width : 360,
				align : 'left'
			}],
			width : 960,
			height : 500
		});
		
		semaforoController.obterStatusProcessosEncalhe();
	},
	
	pollStatusProcessosEncalhe : function() {
		
		setTimeout(function() {
			semaforoController.obterStatusProcessosEncalhe();
		}, 10000);
	},
	
	obterStatusProcessosEncalhe : function() {
		
		$(".statusProcessosEncalheGrid", this.workspace).flexReload();
	},
	
	preProcessarGrid : function(resultado) {
		
		if (resultado.mensagens) {
			
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {

			if (row.cell.status == 'Iniciado') {
				row.cell.status = "<img src= " + contextPath + "/images/ico_semdados.png />";
			} else if (row.cell.status == 'Finalizado') {
				row.cell.status = "<img src= " + contextPath + "/images/ico_operando.png />";
			} else {
				row.cell.status = "<img src= " + contextPath + "/images/ico_encerrado.png />";
			}
		});
		
		semaforoController.pollStatusProcessosEncalhe();
		
		return resultado;
	}
	
}, BaseController);
//@ sourceURL=semaforo.js