$(document).ready(function() {
  var firstTabPosition = $("#nav-1 li:nth-child(3)").position();
  var firstTabWidth = $("#nav-1 li:nth-child(3)").width();
  $("#nav-1 .slide1").css({ opacity: 1, left: +firstTabPosition.left, width: firstTabWidth });

  $("#nav-1 a").on("click", function() {
    var position = $(this).parent().position();
    var width = $(this).parent().width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });

    // '규정 작성' 탭을 클릭했을 때만 CK Editor를 보이고, container와 buttonContainer를 숨김
      if ($(this).text().trim() === '규정 작성') {
        $('#ck-editor-wrapper').show();
        $('#tableContainer').hide();
        $('#buttonContainer').hide();
      } else {
        $('#ck-editor-wrapper').hide();
        $('.container').show();
        $('#buttonContainer').show();
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


