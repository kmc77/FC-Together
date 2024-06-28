$(document).ready(function() {
    var firstTabPosition = $("#nav-1 li:nth-child(3)").position();
    var firstTabWidth = $("#nav-1 li:nth-child(3)").width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +firstTabPosition.left, width: firstTabWidth });

    // 초기 상태 설정: container는 보이고, viewContainer는 숨김
    $('#tableContainer').show();
    $('#viewContainer').hide();
    $('#buttonContainer').show(); // buttonContainer를 보이게 설정

    $("#nav-1 a").on("click", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();
        $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });

        var sectionId = $(this).attr("href");
        $(".content-section").hide();
        $(sectionId).show();

        // '팝업 등록' 탭을 클릭했을 때만 등록 폼을 보임
        if ($(this).text().trim() === '팝업 등록') {
            $('#popup-register').show();
            $('#popup-list').hide();
        } else {
            $('#popup-register').hide();
            $('#popup-list').show();
        }
    });

    $("#nav-1 a").on("mouseover", function() {
        var position = $(this).parent().position();
        var width = $(this).parent().width();
        $("#nav-1 .slide2").css({
            opacity: 1, left: +position.left, width: width
        }).addClass("squeeze");
    });

    $("#nav-1 a").on("mouseout", function() {
        $("#nav-1 .slide2").css({ opacity: 0 }).removeClass("squeeze");
    });
});
