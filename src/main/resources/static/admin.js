$(document).ready(function () {
  $('*[data-action="approve"]').click(function () {
    $.ajax({
      url: `portal/news/approve/${this.dataset.id}`,
      method: "patch",
      success: function () {
        location.reload();
      },
    });
  });
  $('*[data-action="send"]').click(function () {
    $(this).html(
      `
      <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
      `
    );
    $("button").prop("disabled", true);
    $.post(
      `http://localhost:8081/classify?uuid=${this.dataset.id}`,
      function () {
        location.reload();
      }
    );
  });
});
