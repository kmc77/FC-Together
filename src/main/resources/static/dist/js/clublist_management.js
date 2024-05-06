$(document).ready(function() {
    // 첫 번째 탭의 위치와 너비 계산
    var firstTabPosition = $("#nav-1 li:nth-child(6)").position();
    var firstTabWidth = $("#nav-1 li:nth-child(6)").width();

    // 첫 번째 슬라이더 초기 위치와 너비 설정
    $("#nav-1 .slide1").css({ opacity: 1, left: firstTabPosition.left, width: firstTabWidth });

    // 탭 클릭 이벤트 핸들러
    $("#nav-1 a").on("click", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();

        // 클릭된 탭으로 슬라이더 이동
        $("#nav-1 .slide1").css({ opacity: 1, left: position.left, width: width });

        // 기본적으로 모든 탭 클릭 시 보이는 요소들
        $('.container').show();
        $('#buttonContainer').show();

        // "구단 등록" 탭 클릭 시 동작
        if ($(this).parent().attr("id") === "register-team") {
            $('#cnt_bbs').show();
            $('#tableContainer').hide();
            $('#buttonContainer').hide();
        }

        // "K5, K7, Woman 구단 목록" 탭 클릭 시의 동작
        if ($(this).data('team-type') === "k5" || $(this).data('team-type') === "w1" || $(this).data('team-type') === "k7") {
            $('#cnt_bbs').hide();
        }
    });

    // 마우스 오버 이벤트 핸들러
    $("#nav-1 a").on("mouseover", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();

        // 마우스 오버된 탭으로 슬라이더 이동 및 스타일 적용
        $("#nav-1 .slide2").css({
            opacity: 1, left: position.left, width: width
        }).addClass("squeeze");
    });

    // 마우스 아웃 이벤트 핸들러
    $("#nav-1 a").on("mouseout", function() {
        $("#nav-1 .slide2").css({ opacity: 0 }).removeClass("squeeze");
    });
});
