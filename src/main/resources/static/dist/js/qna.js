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

    // '문의 답변' 탭을 클릭했을 때만 viewContainer를 보이고, container와 buttonContainer를 숨김
    if ($(this).text().trim() === '문의 답변') {
      $('#viewContainer').show();
      $('#tableContainer').hide();
      $('#buttonContainer').hide(); // buttonContainer를 숨기게 설정
    } else {
      $('#viewContainer').hide();
      $('#tableContainer').show();
      $('#buttonContainer').show(); // buttonContainer를 보이게 설정
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
