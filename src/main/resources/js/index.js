var div = document.querySelector("#info");
var tbody = document.querySelector(".table > tbody");

localStorage.setItem("name", "Ricardo");

$("#degrees").click(() => getData("degrees"));

$("#subjects").click(() => getData("subjects"));

$("#sendData").click(() => addTeacher());

$("#cleanData").click(() => {
    $(".title").remove('');
    $("p").remove();
    $('input').val('');
    $("#error-message").text('');
});

var addTeacher = () => {

    $.ajax({
        url: "http://localhost:8080/school-system/teachers",
        type: "post",
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
            name: $("#full-name").val(),
            address: $("#address").val(),
            phonenumber: $("#phonenumber").val(),
            email: $("#email").val(),
            subjectId: $("#subject-id").val()
        }),
        success: function (response) {
            $('input').val('');
            $("#error-message").text('');
            getTeachers();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#error-message").text("Preencha todos os campos");
            console.log(textStatus, errorThrown);
        }
    });
};

var getTeachers = () => {

    $.get("http://localhost:8080/school-system/teachers", function (data, status) {

        var targetFields = ["id", "name", "address", "email", "phonenumber"];

        $(".dynamic-data").remove();

        data.forEach(teacher => {

            var tr = document.createElement("tr");
            tr.className = "hoverable-tr";

            targetFields.forEach(targetField => {

                var th = document.createElement("th");
                th.className = "dynamic-data";

                if (teacher[targetField]) {
                    th.innerHTML = teacher[targetField];
                } else {
                    th.innerHTML = "------";
                }

                tr.appendChild(th);
            });

            var th = document.createElement("th");
            th.className = "dynamic-data action-th";
            th.name = teacher.id;

            var deleteButton = buildDynamicButton(teacher.id, "delete");
            var editButton = buildDynamicButton("edit");

            th.appendChild(deleteButton);
            th.appendChild(editButton);

            tr.appendChild(th);

            tbody.appendChild(tr);
        });
    });
};

var removeTeacher = id => {

    $.ajax({ url: "http://localhost:8080/school-system/teachers/" + id, method: "GET" })
        .then(function (data) {
            getTeachers();
            console.log("Pintou!");
        })
        .catch(function (err) {
            console.log("error.");
        });
};

var getData = endpoint => {

    $.get("http://localhost:8080/school-system/" + endpoint, function (data, status) {

        $("p").remove();
        $(".title").remove();

        var title = document.createElement("h1");
        title.className = "title";
        title.innerHTML = endpoint.charAt(0).toUpperCase() + endpoint.slice(1);
        div.appendChild(title);

        data.forEach(element => {

            var p = document.createElement("p");
            var p2 = document.createElement("p");

            p.innerHTML = "<strong>" + title.innerHTML + " ID:</strong> " + element.id;
            p2.innerHTML = "<strong>" + title.innerHTML + " name:</strong>" + element.name;

            div.appendChild(p);
            div.appendChild(p2);
        });
    });
};

var buildDynamicButton = (id = 0, action) => {

    var dynamicButton = document.createElement("button");

    if(action == "delete"){
        dynamicButton.addEventListener("click", () => removeTeacher(id));
        dynamicButton.className = "delete-user btn btn-danger";
        dynamicButton.innerHTML = "DELETE";
    } else {
        dynamicButton.addEventListener("click", () => console.log("Edit user"));
        dynamicButton.className = "edit-user btn btn-warning";
        dynamicButton.innerHTML = "EDIT";
    }

    return dynamicButton;
};

window.onload = () => {
    getTeachers();
    document.cookie = "username = Ricardo";
};




