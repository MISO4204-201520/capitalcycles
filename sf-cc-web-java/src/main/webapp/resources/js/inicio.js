$( document ).ready(function() {
	if (window.location.search && window.location.search.length > 0){
		var logg = document.getElementById("formAutenticar:loggin");
		if (logg){
			logg.setAttribute("style","display:none");
		}
	}
	setTimeout(animacion, 2000);
});

function animacion(){
	if ($('#imgPre1').is(':visible')){
		$('#imgPre1').fadeOut(2000);
		$('#imgPre2').fadeIn(2000);
	}else if ($('#imgPre2').is(':visible')){
		$('#imgPre2').fadeOut(2000);
		$('#imgPre3').fadeIn(2000);
	}else if ($('#imgPre3').is(':visible')){
		$('#imgPre3').fadeOut(2000);
		$('#imgPre1').fadeIn(2000);
	}
	setTimeout(animacion, 4000);
}