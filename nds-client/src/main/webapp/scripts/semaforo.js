var semaforoController = $.extend(true, {
	
	total:0,
	andamento:0,
	erro:0,
	finalizado:0,
	stopRefresh:true,
	varTimeout:null,
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
		if (!semaforoController.stopRefresh ) {
		varTimeout=setTimeout(function() {
			semaforoController.obterStatusProcessosEncalhe();
		}, 30000);
		}
		else
			if (semaforoController.varTimeout != null  )
			clearTimeout(semaforoController.varTimeout)
	},
	
	obterStatusProcessosEncalhe : function() {
		if (!semaforoController.stopRefresh )
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
		total=0;
		finalizado=0;
		andamento=0;
		erro=0;
		
		$.each(resultado.rows, function(index, row) {
            total++;
			if (row.cell.status == 'Iniciado') {
				andamento++;
				row.cell.status = "<img src= " + contextPath + "/images/ico_semdados.png />";
			} else if (row.cell.status == 'Finalizado') {
				finalizado++;
				row.cell.status = "<img src= " + contextPath + "/images/ico_operando.png />";
			} else {
				erro++;
				row.cell.status = "<img src= " + contextPath + "/images/ico_encerrado.png />";
			}
			
			if (!row.cell.mensagem){
				row.cell.mensagem = "";
			}
		});
		$("#totalId",semaforoController.workspace).html("Total=" + total+" (Finalizados="+finalizado+" Andamento="+andamento+" Erros="+erro+")");
		
		semaforoController.pollStatusProcessosEncalhe();
		
		return resultado;
	}
	
}, BaseController);
//@ sourceURL=semaforo.js