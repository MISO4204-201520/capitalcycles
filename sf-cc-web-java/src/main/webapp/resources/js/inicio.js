$( document ).ready(function() {
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