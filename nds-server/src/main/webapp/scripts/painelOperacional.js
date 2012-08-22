var painelOperacionalController = $.extend(true, {
	
	init:function() {
		this.bindButtons();
		this.initAccordion();
	},
	
	mostrarIndicadores:function(estado) {
		$(".grupoIndicadorEstado").hide();
		$("#div_" + estado).show();
	},
	
	openDistrib:function(idDistrib) {
		$("#detalhe_" + idDistrib).fadeIn('slow');
	},
	
	closeDistrib:function(idDistrib) {
		$("#detalhe_" + idDistrib).fadeOut('slow');
	},
	
	bindButtons:function() {
		
		var _this = this;
		
		$("#map > li").click(function() {  
			var estado = $(this).attr("estado").toUpperCase(); 
			_this.mostrarIndicadores(estado);
		});
		
		
		$(".linkPainel a").click(function() {
			var idDistrib = $(this).attr("id");
			_this.openDistrib(idDistrib);
		});
		
		$(".detalhesPainel a").click(function() {
			var idDistrib = $(this).attr("id");
			_this.closeDistrib(idDistrib);
		});
	},
	
	initAccordion : function() {
		$(".accordion").accordion({
			autoHeight : false,
			navigation : true
		});
	}
}
/** FIXME: remover comentarios quando alterar o layout
 *
 * ,BoxController
 * 
 * 
 **/
);

$(function() {
	painelOperacionalController.init();
});