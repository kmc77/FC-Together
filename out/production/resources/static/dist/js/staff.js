$(document).ready(function() {
    // 첫 번째 탭의 위치와 너비를 계산하여 슬라이더 초기 위치 설정
    var firstTabPosition = $("#nav-1 li:nth-child(3)").position();
    var firstTabWidth = $("#nav-1 li:nth-child(3)").width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +firstTabPosition.left, width: firstTabWidth });

    // 탭 클릭 이벤트
    $("#nav-1 a").on("click", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();
        $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });

        // 기본적으로 모든 탭을 클릭했을 때 보이는 요소들
        $('.container').show();
        $('#buttonContainer').show();

        // "스태프 등록" 탭을 클릭했을 때의 동작
        if($(this).parent().attr("id") === "register-staff") {
            $('#cnt_bbs').show();
            $('#tableContainer').hide();
            $('#buttonContainer').hide();
        }

        // "스태프 목록" 탭 클릭 시의 동작
        if($(this).data('staff-type') === "staff") {
            $('#cnt_bbs').hide();
            // 스태프 목록 특화된 동작을 여기 추가할 수 있습니다.
        }

        // 다른 특정 탭을 클릭했을 때의 동작은 여기에 추가할 수 있습니다.
    });

    // 마우스 오버 이벤트
    $("#nav-1 a").on("mouseover", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();
        $("#nav-1 .slide2").css({
            opacity: 1, left: +position.left, width: width
        }).addClass("squeeze");
    });

    // 마우스 아웃 이벤트
    $("#nav-1 a").on("mouseout", function() {
        $("#nav-1 .slide2").css({ opacity: 0 }).removeClass("squeeze");
    });
});
