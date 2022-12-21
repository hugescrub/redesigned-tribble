$(document).ready(function () {
  $('*[data-action="approve"]').click(function () {
    $(this).html(
      `
      <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
      `
    );
    $("button").prop("disabled", true);
    var dataset = this.dataset;
    $.ajax({
      url: `http://localhost:8081/classificationResults/${dataset.classificationId}`,
      method: "patch",
      contentType: "application/json",
      xhrFields: {
        withCredentials: true,
      },
      data: JSON.stringify({
        isCorrect: true,
      }),
      error: function () {
        $("button").prop("disabled", false);
        showErrorAlert(CLASSIFICATION_ERROR_MSG);
        $('*[data-action="approve"]').html("Approve and publish");
      },
      success: function () {
        $.ajax({
          url: `portal/news/${dataset.id}`,
          method: "patch",
          contentType: "application/json",
          data: JSON.stringify({
            isApproved: true,
          }),
          error: function () {
            $("button").prop("disabled", false);
            showErrorAlert(INNER_ERROR_MSG);
            $('*[data-action="approve"]').html("Approve and publish");
          },
          success: function () {
            location.reload();
          },
        });
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
      `http://localhost:8081/classify?uuid=${this.dataset.id}&classificationId=${this.dataset.classificationId}`,
      function () {
        location.reload();
      }
    ).fail(function () {
      $("button").prop("disabled", false);
      showErrorAlert(CLASSIFICATION_ERROR_MSG);
      $('*[data-action="send"]').html("Send to classification again");
    });
  });

  $('*[data-action="mark"]').click(function () {
    var dataset = this.dataset;

    $("#admin-mark-modal").addClass("d-block");

    $("#publish").click(function () {
      var reason = $("#reason-select")[0].value;
      var correctLabel = $("#label-select")[0].value;

      if (reason === "classification") {
        $.ajax({
          url: `http://localhost:8081/classificationResults/${dataset.classificationId}`,
          method: "patch",
          contentType: "application/json",
          xhrFields: {
            withCredentials: true,
          },
          data: JSON.stringify({
            isCorrect: false,
            correctLabel,
          }),
          error: function () {
            $("button").prop("disabled", false);
            showErrorAlert(CLASSIFICATION_ERROR_MSG);
          },
          success: function () {
            $.ajax({
              url: `portal/news/${dataset.id}`,
              method: "patch",
              contentType: "application/json",
              data: JSON.stringify({
                classificationResult: `{"items":[{"label":"${correctLabel}","probability":1}]}`,
                isApproved: true
              }),
              error: function () {
                $("button").prop("disabled", false);
                showErrorAlert(INNER_ERROR_MSG);
              },
              success: function () {
                location.reload();
              },
            });
          },
        });
      } else {
        // impossible
      }
      $("#admin-mark-modal").removeClass("d-block");
    });

    $("#not-publish").click(function () {
      var reason = $("#reason-select")[0].value;
      var correctLabel = $("#label-select")[0].value;

      $("button").prop("disabled", true);
      if (reason === "classification") {
        $.ajax({
          url: `http://localhost:8081/classificationResults/${dataset.classificationId}`,
          method: "patch",
          contentType: "application/json",
          xhrFields: {
            withCredentials: true,
          },
          data: JSON.stringify({
            isCorrect: false,
            correctLabel,
          }),
          error: function () {
            $("button").prop("disabled", false);
            showErrorAlert(CLASSIFICATION_ERROR_MSG);
          },
          success: function () {
            $.ajax({
              url: `portal/news/${dataset.id}`,
              method: "patch",
              contentType: "application/json",
              data: JSON.stringify({
                classificationResult: "",
              }),
              error: function () {
                $("button").prop("disabled", false);
                showErrorAlert(INNER_ERROR_MSG);
              },
              success: function () {
                location.reload();
              },
            });
          },
        });
      } else {
        // TODO
      }
      $("#admin-mark-modal").removeClass("d-block");
    });
  });

  $("#reason-select").change(function () {
    if (this.value === "classification") {
      $("#publish").removeClass("d-none");
      $("#label-select-container").removeClass("d-none");
    } else {
      $("#publish").addClass("d-none");
      $("#label-select-container").addClass("d-none");
    }
  });

  $("#close-modal").click(function () {
    $("#admin-mark-modal").removeClass("d-block");
  });

  $("#close-alert").click(function () {
    $("#error-alert").addClass("d-none");
  });
});

function showErrorAlert(errorText) {
  $("#error-message").text(errorText);
  $("#error-alert").removeClass("d-none");
}

var CLASSIFICATION_ERROR_MSG =
  "There happens to be an error with classification service. Service is probably down. Contact administrator or try later.";
var INNER_ERROR_MSG =
  "An error happened while publishing the article. Refresh the page and try again.";
