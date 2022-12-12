$(document).ready(function () {
  $("#approve").click(function () {
    console.log(this);
  });
  $("#send").click(function () {
    $.post(
      `http://localhost:8081/classify?uuid=${this.dataset.id}`,
      function () {
        location.reload();
      }
    );
  });
});
