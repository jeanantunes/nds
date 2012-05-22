function TipoMovimento() {
	
	var T = this;
	
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	};
	
	$(function() {
		$("#dataInicial").mask("99/99/9999");
		$("#dataFinal").mask("99/99/9999");
		
		$("#dataLancamentoEd").mask("99/99/9999");
		$("#dataRecolhimentoEd").mask("99/99/9999");
		
		$("#edicaoProduto").numeric();

		$("#peb").numeric();

		$("#qtde").numeric();
	});
}
