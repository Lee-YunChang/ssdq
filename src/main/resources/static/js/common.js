$( document ).ready(function() {
		
	var bodySize = $('body').outerWidth();
	var HtmlSize = $('html').outerWidth();
	if(bodySize < 1601 && $('body').hasClass('dashboard')) {
		//$('.btn_fold').addClass('toggled');
		$('.left_area').addClass('fold');
		$('#content').addClass('fullsize');
	}else if(HtmlSize < 740 && $('body').hasClass('dashboard') == false){
		//$('.btn_fold').addClass('toggled');
		$('.left_area').addClass('fold');
		$('#content').addClass('fullsize');
	}
	var contentHeight = $('.content_area').outerHeight();
	var bodyHeight = $('body').outerHeight();
	if(contentHeight > bodyHeight){
		$('.left_area').css('min-height',contentHeight+ 71 +'px');
	}
	
//	$( window ).resize( function() {
//		var bodySize = $('body').outerWidth();
//		var HtmlSize = $('html').outerWidth();
//		
//		if(bodySize < 1601 && $('body').hasClass('dashboard')) {
//			$('.btn_fold').addClass('toggled');
//			$('.left_area').addClass('fold');
//			$('#content').addClass('fullsize');
//			$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//			$('.sub_menu').css('display','none');
//		}else if(bodySize > 1601 && $('body').hasClass('dashboard')){
//			$('.btn_fold').removeClass('toggled');
//			$('.left_area').removeClass('fold');
//			$('#content').removeClass('fullsize');
//		}
//		if(HtmlSize < 1201 && $('body').hasClass('dashboard') == false){
//			$('.btn_fold').addClass('toggled');
//			$('.left_area').addClass('fold');
//			$('#content').addClass('fullsize');
//			$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//			$('.sub_menu').css('display','none');
//		}else if(HtmlSize > 1201 && $('body').hasClass('dashboard') == false){
//			$('.btn_fold').removeClass('toggled');
//			$('.left_area').removeClass('fold');
//			$('#content').removeClass('fullsize');
//		}
//		
//	});
//	$('.btn_fold').click(function(){
//		var bodySize = $('body').outerWidth();
//		var HtmlSize = $('html').outerWidth();
//		if(bodySize > 1601 && $('body').hasClass('dashboard')){
//			$(this).toggleClass('toggled');
//			$('.left_area').toggleClass('fold');
//			$('#content').toggleClass('fullsize');
//		}else if(HtmlSize > 740 && $('body').hasClass('dashboard') == false){
//			$(this).toggleClass('toggled');
//			$('.left_area').toggleClass('fold');
//			$('#content').toggleClass('fullsize');
//		}
//		//서브메뉴가 열린상태에서 lnb를 접엇을경우 초기화
//		//$('.lnb_menu ul li a').parent('li').removeClass('on');
//		$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//		$('.sub_menu').css('display','none');
//
//	});

	$('.btn_fold').click(function(){
		var bodySize = $('body').outerWidth();
		if(bodySize > 1601 && $('body').hasClass('dashboard')){
			$(this).toggleClass('toggled');
			$('.left_area').toggleClass('fold');
			$('#content').toggleClass('fullsize');
		}else if(bodySize > 1201 && $('body').hasClass('dashboard') == false){
			$(this).toggleClass('toggled');
			$('.left_area').toggleClass('fold');
			$('#content').toggleClass('fullsize');
		}
		//서브메뉴가 열린상태에서 lnb를 접엇을경우 초기화
		//$('.lnb_menu ul li a').parent('li').removeClass('on');
		$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
		$('.sub_menu').css('display','none');
		
		//헤더 메뉴 접었을시 tui Grid 테이블 영역 100%
		$('.tui-grid-rside-area').width(100 + '%');
		
		var lnbHeight = $('.left_area').height();
		$('#lnb').css('min-height',lnbHeight + 150+'px');
		console.log(lnbHeight);
	});
	
 // 관리자 onoff버튼
 $('.btn-switch input').prop('checked',false);
 $('.btn-switch').change(function(){
	 var text = $(this).children().children('.switch_txt').text();
	 if(text == 'ON'){
		 $(this).children('i.slide-object').addClass('off').removeClass('on');
		 $(this).children().children('.switch_txt').text('OFF').css('color','#d85050');
	 }else if(text == 'OFF') {
		$(this).children('i.slide-object').addClass('on').removeClass('off');
		$(this).children().children('.switch_txt').text('ON').css('color','#509dd8');
	 }
 });

 /* top 이동 버튼 */
 $(".btn_top").click(function(){
	$('html,body').animate( { scrollTop : 0 }, 400 );
	return false;
});

var selectTarget = $('.selectbox select');
selectTarget.change(function(){
	var select_name = $(this).children('option:selected').text();
	$(this).siblings('label').text(select_name);
});


/* lnb메뉴 */
/*$('.lnb_menu ul li a').on('click',function(){
		//맨처음 on을 지운다
	$('.lnb_menu ul li a').parent('li').removeClass('on');

	if($(this).hasClass('open') == false){
		$('.lnb_menu').find('a.open').next().slideUp('fast');
		$('.lnb_menu').find('a.open').addClass('closed').removeClass('open');
	}
	if($(this).siblings('ul.sub_menu').length != 0){
		if($(this).hasClass('closed')){
			$(this).removeClass('closed').addClass('open');
			$(this).next().slideDown('fast');
		}else if($(this).hasClass('closed') == false) {
			$(this).addClass('closed').removeClass('open');
			$(this).next().slideUp('fast');
			$(this).parents().removeClass('on');
		}
	}
	$(this).parent().addClass('on');
	
})*/

	$('.lnb_menu ul li a').on('click',function(){
		//맨처음 on을 지운다
	$('.lnb_menu ul li a').parent('li').removeClass('on');
	
	if($(this).hasClass('open') == false){
		$('.lnb_menu').find('a.open').next().slideUp('fast');
		$('.lnb_menu').find('a.open').addClass('closed').removeClass('open');
	}
	if($(this).siblings('ul.sub_menu').length != 0){
		if($(this).hasClass('closed')){
			$(this).removeClass('closed').addClass('open');
			$(this).next().slideDown('fast');
		}else if($(this).hasClass('closed') == false) {
			$(this).addClass('closed').removeClass('open');
			$(this).next().slideUp('fast');
			$(this).parents().removeClass('on');
		}
	}
	$(this).parent().addClass('on');
	
	})

});

$(window).load(function() {
	$.datepicker.setDefaults({
		dateFormat: 'yy-mm-dd',
		prevText: '이전 달',
		nextText: '다음 달',
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		yearSuffix: '년'
	});
});

function fn_modalClose(){
    $(".modal").modal('hide');
}

function fn_modalCloseLayer(){
	/*레이어 닫기*/	
	$('.layer_pop_wrap').removeClass('on');
	$('.cover').hide();	
}

function isSessionConChk(sessionCon) {
	if(sessionCon == null){
		alert('진단대상 데이터베이스 설정이 안되어 있습니다.\n\n 설정페이지로 이동합니다.');
		/*location.href = "/mngr/basicInfo/dgnssDbmsList";*/
		location.href = "/mngr/basicInfo/dgnssDbmsList?up=collapse0&sn=6";
		return false;
	}else {
		return true;
	}
}

function isCsvFileCheck(sessionCon){
	var data = {
			"dgnssDbmsId":sessionCon
	};
	$.ajax({
		url: "/mngr/analysis/selectDbmsKnd",
		data : data,
		success: function(data){
			var obj = JSON.parse(data);
			
			if("CSV" == obj.dbmsKnd){
				alert("CSV 파일은 해당 기능을 지원하지 않습니다.\n\n 설정페이지로 이동합니다.");
				/*location.href = "/mngr/basicInfo/dgnssDbmsList";*/
				location.href = "/mngr/basicInfo/dgnssDbmsList?up=collapse0&sn=6";
				return false;
			}
			
		}
	});
}

function fn_pagination(totalCnt){
	var pageIndex = $('#pageIndex').val() == "" ? 1 : $('#pageIndex').val();
	var itemsPerPage = $('#itemsPerPage').val() === undefined ? 5 : $('#itemsPerPage').val();
	
	console.log("$('#itemsPerPage').val() = " , $('#itemsPerPage').val())
	
	var pagination = new tui.Pagination('pagination', {
		totalItems: totalCnt,
        itemsPerPage: itemsPerPage,
        visiblePages: 10,
        page: Number(pageIndex),
		template: {
            page: '<a href="#" class="tui-page-btn">{{page}}</a>',
            currentPage: '<strong class="tui-page-btn tui-is-selected">{{page}}</strong>',
            moveButton:
                '<a href="#" class="tui-page-btn tui-{{type}} custom-class-{{type}}">' +
                    '<span class="tui-ico-{{type}}">{{type}}</span>' +
                '</a>',
            disabledMoveButton:
                '<span class="tui-page-btn tui-is-disabled tui-{{type}} custom-class-{{type}}">' +
                    '<span class="tui-ico-{{type}}">{{type}}</span>' +
                '</span>',
            moreButton:
                '<a href="#" class="tui-page-btn tui-{{type}}-is-ellip custom-class-{{type}}">' +
                    '<span class="tui-ico-ellip">...</span>' +
                '</a>'
        }
    });
	
	
    pagination.on('beforeMove', function(eventData) {
        $('#pageIndex').val(eventData.page);
       
        // 데이터 조회 function명 동일하게 사용
        fn_search();
    });
}

/*
 * 숫자만 입력 가능
 */
function maxLengthCheck(object){
    $(object).val(object.value.replace(/[^0-9]/g,""));
	
	if (object.value.length > object.maxLength){
		object.value = object.value.slice(0, object.maxLength);
	}  
}

/*
 * 영문 + 숫자만 입력 가능
 */
function inputCheck(object){
    $(object).val(object.value.replace(/[^a-z0-9]/gi,''));
}



/**
 * 알파벳, 숫자, '_' 조합만 사용 가능
 * 입력문자열, 구분코드(G : 그룹코드,D : 상세코드), 메시지입력 ID, validation ID (true, false)
 */
function fn_codeStrTest(str, gubun, id, vId){
	var chkStr = /^[A-Za-z0-9_+]*$/;
	var isChkStr = 0;
	var isChk = 0;

	if(!chkStr.test(str) || str == ""){
		
		$("#"+id).text("(알파벳, 숫자, '_' 조합만 사용가능)");
		$("#"+id).css("color", "red");
		
		fn_btnClass('groupCodeLayer', true);
		$("#"+vId).val(false);
		return false;
    }else{
    	isChkStr = 1;
    }
	
	if(isChkStr > 0){
		
		$.ajax({
    		url: '/mngr/cmmnMng/selectCodeDoubleChk',
    		method : 'POST',
    		data: {"gubun" : gubun, "searchCode" : str},
    		success: function(data){
    			var result = JSON.parse(data);
    			
    			if(result){
    				$("#"+id).text("(사용가능)");
    		    	$("#"+id).css("color", "green");
    		    	
    		    	fn_btnClass('groupCodeLayer', false);
    		    	
    		    	$("#"+vId).val(true);
    		    	
    		    	
    			}else{
    	    		$("#"+id).text("(빈값 또는 코드 중복 사용불가)");
    	        	$("#"+id).css("color", "red");
    	        	
    	        	fn_btnClass('groupCodeLayer', true);
    	        	
    	        	$("#"+vId).val(false);
    	        	alert("코드가 빈값 또는 중복되었습니다.");
    	        	if(gubun == 'G'){
    	        		$("#groupCode").val("");
       		    	 	$("#groupCode").focus();
    	        	}
    	        	if(gubun == 'D'){
    	        		$("#detailCmmnCode").val("");
       		    	 	$("#detailCmmnCode").focus();
    	        	}
    			}
            }
        });
	}
};


function fn_tabLink(obj){
	
	var thisObjId = $(obj).attr("id");
	var newTabId = $("#nowTabId").val();
	var objLength = $(obj).parent().parent().children().length;
	
	if(newTabId != thisObjId){
		
		for(var i=1; i<=objLength; i++){
			if("tab"+i == thisObjId){
				$("#nowTabId").val(thisObjId);
				//$("#"+thisObjId).prop("class", "nav-link active");
				$("#tab_area"+i).prop("class", "on");
				$("#"+thisObjId+"_div").show();
				fn_searchTab(thisObjId);
			}else{
				//$("#tab"+i).prop("class", "nav-link");
				$("#tab_area"+i).prop("class", "");
				$("#tab"+i+"_div").hide();
			}
		}
	}
}

function fn_btnClass(layer, bln){
	
	if(layer == 'groupCodeLayer'){
		if(bln){
			$(".btn-secondary").prop("disabled", true);
    		$(".btn-secondary").prop("class", "btn btn-danger");
		}else{
			$(".btn-danger").prop("disabled", false);
			$(".btn-danger").prop("class", "btn btn-secondary");
		}
	}
	
}

/**
 * 공통코드 옵션값 가져오기
 */
function fn_menuGroupList(selectId, menuGroupSn){
	$.ajax({
		url: '/mngr/menuMng/selectMenuGroupList',
		method : 'POST',
		data: {selectId : selectId},
		success: function(data){
			if(data){
				var obj = JSON.parse(data);
				var optionData = obj.result;
				generateOption(selectId, optionData, menuGroupSn, "group");
			}
        },
    });
}


function fn_upMenuList(selectId, menuGroupSn, menuSn){
	$.ajax({
		url: '/mngr/menuMng/selectUpMenuList',
		method : 'POST',
		data: {selectId : selectId, menuGroupSn : menuGroupSn},
		success: function(data){
			if(data){
				var obj = JSON.parse(data);
				var optionData = obj.result;
				generateOption(selectId, optionData, menuSn, "up");
			}
        },
    });
}


function generateOption(selectId, optionData, menuSn, gubun) {
	
	$('#'+selectId).append('<option value="">선택</option>');
	
	if (optionData.length > 0) {
		$.each(optionData, function(index, option) {
			
			if(gubun == "group"){
				var option = $('<option value="'+option.menuGroupSn+'">'+option.menuGroupNm+'</option>');	
			} else if (gubun == "up"){
				var option = $('<option value="'+option.menuSn+'">'+option.menuNm+'</option>');
			} else { 
				var option = $('<option value="'+option.code+'">'+option.codeNm+'</option>');
			}
			
			$('#'+selectId).append(option);
		});
		
		if ($.trim(menuSn) != '') {
			$('#'+selectId+' > option[value="'+menuSn+'"]').attr('selected', 'selected');
		}
	}
	
}

function wrapWindowByMask() {
    //화면의 높이와 너비를 구한다.
    var maskHeight = $(document).height(); 
    //var maskWidth = $(document).width();
    var maskWidth = window.document.body.clientWidth;
    var mask = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg = '';
    
    loadingImg += "<div id='loadingImg' style='position:absolute; left:50%; top:40%; transform:translate(-50%, -50%); display:none; z-index:10000;'>";
    loadingImg += " <img src='/img/loading.gif'/>";
    loadingImg += "</div>";  
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
        .append(loadingImg)
       
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#mask').css({
            'width' : maskWidth
            , 'height': maskHeight
            , 'opacity' : '0.3'
    }); 
 
    //마스크 표시
    $('#mask').show();   
 
    //로딩중 이미지 표시
    $('#loadingImg').show();
}


function closeWindowByMask() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove();  
}

function isBrowserMsie() {
	var agent = navigator.userAgent.toLowerCase();
	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
		return true;
	} else {
		return false;
	}
}

//$( document ).ready(function() {
//	var bodySize = $('body').outerWidth();
//	if(bodySize < 1601 && $('body').hasClass('dashboard')) {
//		$('.btn_fold').addClass('toggled');
//		$('.left_area').addClass('fold');
//		$('#content').addClass('fullsize');
//	}else if(bodySize < 1201 && $('body').hasClass('dashboard') == false){
//		$('.btn_fold').addClass('toggled');
//		$('.left_area').addClass('fold');
//		$('#content').addClass('fullsize');
//	}
//	
//	$( window ).resize( function() {
//		var bodySize = $('body').outerWidth();
//		if(bodySize < 1601 && $('body').hasClass('dashboard')) {
//			$('.btn_fold').addClass('toggled');
//			$('.left_area').addClass('fold');
//			$('#content').addClass('fullsize');
//			$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//			$('.sub_menu').css('display','none');
//		}else if(bodySize > 1601 && $('body').hasClass('dashboard')){
//			$('.btn_fold').removeClass('toggled');
//			$('.left_area').removeClass('fold');
//			$('#content').removeClass('fullsize');
//		}
//		if(bodySize < 1201 && $('body').hasClass('dashboard') == false){
//			$('.btn_fold').addClass('toggled');
//			$('.left_area').addClass('fold');
//			$('#content').addClass('fullsize');
//			$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//			$('.sub_menu').css('display','none');
//		}else if(bodySize > 1201 && $('body').hasClass('dashboard') == false){
//			$('.btn_fold').removeClass('toggled');
//			$('.left_area').removeClass('fold');
//			$('#content').removeClass('fullsize');
//		}
//		
//	});
//	$('.btn_fold').click(function(){
//		var bodySize = $('body').outerWidth();
//		if(bodySize > 1601 && $('body').hasClass('dashboard')){
//			$(this).toggleClass('toggled');
//			$('.left_area').toggleClass('fold');
//			$('#content').toggleClass('fullsize');
//		}else if(bodySize > 1201 && $('body').hasClass('dashboard') == false){
//			$(this).toggleClass('toggled');
//			$('.left_area').toggleClass('fold');
//			$('#content').toggleClass('fullsize');
//		}
//		//서브메뉴가 열린상태에서 lnb를 접엇을경우 초기화
//		//$('.lnb_menu ul li a').parent('li').removeClass('on');
//		$('.lnb_menu ul li').find('a.open').addClass('closed').removeClass('open');
//		$('.sub_menu').css('display','none');
//		
//		//헤더 메뉴 접었을시 tui Grid 테이블 영역 100%
//		$('.tui-grid-rside-area').width(100 + '%');
//	});
//
// // 관리자 onoff버튼
// $('.btn-switch input').prop('checked',false);
// $('.btn-switch').change(function(){
//	 var text = $(this).children().children('.switch_txt').text();
//	 if( text == 'ON'){
//		 $(this).children().children('.switch_txt').text('OFF').css('color','#509dd8');
//		 $(this).children().children('.switch_txt').css('margin-left','20px');
//	 }else if(text == 'OFF') {
//		 $(this).children().children('.switch_txt').text('ON').css('color','#d85050');
//		 $(this).children().children('.switch_txt').css('margin-left','-20px');
//	 }
// });
//
// /* top 이동 버튼 */
// $(".btn_top").click(function(){
//	$('html,body').animate( { scrollTop : 0 }, 400 );
//	return false;
//});
//
//var selectTarget = $('.selectbox select');
//selectTarget.change(function(){
//	var select_name = $(this).children('option:selected').text();
//	$(this).siblings('label').text(select_name);
//});
//
//
///* lnb메뉴 */
//$('.lnb_menu ul li a').on('click',function(){
//		//맨처음 on을 지운다
//	$('.lnb_menu ul li a').parent('li').removeClass('on');
//
//	if($(this).hasClass('open') == false){
//		$('.lnb_menu').find('a.open').next().slideUp('fast');
//		$('.lnb_menu').find('a.open').addClass('closed').removeClass('open');
//	}
//	if($(this).siblings('ul.sub_menu').length != 0){
//		if($(this).hasClass('closed')){
//			$(this).removeClass('closed').addClass('open');
//			$(this).next().slideDown('fast');
//		}else if($(this).hasClass('closed') == false) {
//			$(this).addClass('closed').removeClass('open');
//			$(this).next().slideUp('fast');
//			$(this).parents().removeClass('on');
//		}
//	}
//	$(this).parent().addClass('on');
//	
//})
//
//});
