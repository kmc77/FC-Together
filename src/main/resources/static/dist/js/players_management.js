$(document).ready(function() {
  var firstTabPosition = $("#nav-1 li:nth-child(6)").position();
  var firstTabWidth = $("#nav-1 li:nth-child(6)").width();
  $("#nav-1 .slide1").css({ opacity: 1, left: +firstTabPosition.left, width: firstTabWidth });

  $("#nav-1 a").on("click", function() {
    var position = $(this).parent().position();
    var width = $(this).parent().width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });

    // 기본적으로 모든 탭을 클릭했을 때 보이는 요소들
    $('.container').show();
    $('#buttonContainer').show();

    // "선수 등록" 탭을 클릭했을 때의 동작
    if($(this).parent().attr("id") === "register-player") {
      $('#cnt_bbs').show();
      $('#tableContainer').hide();
      $('#buttonContainer').hide();


    }

  // "K5, K7, W1 선수 목록" 탭 클릭 시의 동작
  if($(this).data('player-type') === "k5" || $(this).data('player-type') === "w1" || $(this).data('player-type') === "k7") {
    $('#cnt_bbs').hide();
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
