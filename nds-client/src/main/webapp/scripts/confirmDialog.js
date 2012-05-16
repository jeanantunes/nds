/**
 * @author Diego Fernandes
 */

/**
 * Wapper do dialog do Jquey UI
 * 
 * @param message
 *            Texto de mensagem.
 * @param onConfirm
 *            função a ser invocada quando cliando no botão de confirmar. Se a
 *            função retornar um <code>true</code> o dialog será fechado.
 * @param onCancel
 *            função a ser invocada quando cliando no botão de cancelar ou
 *            fechado o dialog.
 * @returns {ConfirmDialog}
 */
function ConfirmDialog(message, onConfirm, onCancel) {
	this.$dialog = $('<div></div>').html(message).dialog(
			{
				autoOpen : false,
				title : 'Alerta!',
				resizable : false,
				height : 140,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						var close = onConfirm();
						if (close) {
							$(this).dialog("close");
						}

					},
					"Cancelar" : function() {
						$(this).dialog("close");
						onCancel();
					}
				},
				close : function(event, ui) {
					if (event.originalEvent
							&& $(event.originalEvent.target).closest(
									".ui-dialog-titlebar-close").length) {
						onCancel();
					}
					event.stopPropagation();
				}
			});
};
ConfirmDialog.prototype.open = function() {
	this.$dialog.dialog('open');
};

ConfirmDialog.prototype.close = function() {
	this.$dialog.dialog("close");
};

ConfirmDialog.prototype.isOpen = function() {
	return this.$dialog.dialog('isOpen');
};
