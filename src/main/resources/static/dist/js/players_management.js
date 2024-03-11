$(document).ready(function() {
  var firstTabPosition = $("#nav-1 li:nth-child(6)").position();
  var firstTabWidth = $("#nav-1 li:nth-child(6)").width();
  $("#nav-1 .slide1").css({ opacity: 1, left: +firstTabPosition.left, width: firstTabWidth });

  $("#nav-1 a").on("click", function() {
    var position = $(this).parent().position();
    var width = $(this).parent().width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });

    // 여기서 특정 조건에 따른 로직을 삭제했습니다.
    $('#cnt_bbs').hide();
    $('.container').show();
    $('#buttonContainer').show();
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
