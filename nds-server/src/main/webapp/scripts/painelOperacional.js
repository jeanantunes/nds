var painelOperacionalController = $.extend(true, {
	
	init:function() {
		this.bindButtons();
		this.initAccordion();
	},
	
	mostrarIndicadores:function(estado) {
		$(".grupoIndicadorEstado", painelOperacionalController.workspace).hide();
		$("#div_" + estado, painelOperacionalController.workspace).show();
	},
	
	openDistrib:function(idDistrib) {
		$("#detalhe_" + idDistrib, painelOperacionalController.workspace).fadeIn('slow');
	},
	
	closeDistrib:function(idDistrib) {
		$("#detalhe_" + idDistrib, painelOperacionalController.workspace).fadeOut('slow');
	},
	
	bindButtons:function() {
		
		var _this = this;
		
		$("#map > li", painelOperacionalController.workspace).click(function() {  
			var estado = $(this, painelOperacionalController.workspace).attr("estado").toUpperCase(); 
			_this.mostrarIndicadores(estado);
		});
		
		
		$(".linkPainel a", painelOperacionalController.workspace).click(function() {
			var idDistrib = $(this, painelOperacionalController.workspace).attr("id");
			_this.openDistrib(idDistrib);
		});
		
		$(".detalhesPainel a", painelOperacionalController.workspace).click(function() {
			var idDistrib = $(this, painelOperacionalController.workspace).attr("id");
			_this.closeDistrib(idDistrib);
		});
	},
	
	initAccordion : function() {
		$(".accordion", painelOperacionalController.workspace).accordion({
			autoHeight : false,
			navigation : true
		});
	}
} ,BaseController);

$(function() {
	painelOperacionalController.init();
});