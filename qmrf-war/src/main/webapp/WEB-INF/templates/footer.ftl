<script type="text/javascript">

<!-- show the footer when the mouse is near -->
	$(document).ready( function () {
    	$('div#footer-in').mouseenter( function () {
    		$('div#footer').stop().animate({bottom: '15px'}, 'fast');
    	});
    	$('div#footer-out').mouseleave( function () {
    		$('div#footer').stop().animate({bottom: '-17px'}, 'slow');
    	});
    });
</script>
	
<script type="text/javascript">
<!--  initialize the toTop link and show it when the contents are scrolled down enough -->
	$(document).ready( function () {
		$('#toTop').click( function () {
			$('html, body').animate({scrollTop: '0'}, 1000);
		});
		$(window).scroll( function () {
					var h = $('#header').height();
					var p = $(window).scrollTop();
					if ( p > (h + 100) ) {
						$('#toTop').stop().animate({left: '-5px'}, 'fast');
					} else if ( p < (h + 50) ) {
						$('#toTop').stop().animate({left: '-30px'}, 'slow');
					}
				});
		});
</script>
		

<div id='footer'>
<!--
		(2007-2012-2015) 
-->
</div>

