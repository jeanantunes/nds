// JavaScript Document
$(document).ready(function(){
	$("#nav-one li").hover(
		function(){ $("ul", this).fadeIn("fast"); }, 
		function() { } 
	);
	if (document.all) {
		$("#nav-one li").hoverClass ("sfHover");
	}
});

$.fn.hoverClass = function(c) {
	return this.each(function(){
		$(this).hover( 
			function() { $(this).addClass(c);  },
			function() { $(this).removeClass(c); }
		);
	});
};

var sURL = unescape(window.location.pathname);

function doLoad()
{
	setTimeout( "refresh()", 2*1000 );
}

function refresh()
{
	window.location.href = sURL;
}

<!--
function refresh()
{
	window.location.replace( sURL );
}
//-->
function refresh()
{

	window.location.reload( false );
}
//valor = true -> mostra do div
function mostra_div(p, valor) {
 for (var x in p) {
 var e = document.getElementById(p[x]);
  e.style.display = valor ? "block" : "none";
 }
}