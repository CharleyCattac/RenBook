function init_widget() {
    var myUploadWidget;
    document.getElementById("upload_widget_opener").addEventListener("click", function() {
        myUploadWidget = cloudinary.openUploadWidget({
                cloudName: "renbookcloud",
                uploadPreset: "d6cp2bhd",
                //Step 3:  Add list of sources that should be added as tabs in the widget.
                sources: [
                    "local",
                    "url"
                ],
                cropping: true,
                multiple: false,
                defaultSource: "local",
                //UI Customization
                styles: {
                    palette: {
                        window: "#10173a",
                        sourceBg: "#20304b",
                        windowBorder: "#9999ff",
                        tabIcon: "#33ffcc",
                        inactiveTabIcon: "#0e2f5a",
                        menuIcons: "#ffccff",
                        link: "#ff0066",
                        action: "#33ffcc",
                        inProgress: "#00ffcc",
                        complete: "#33ff00",
                        error: "#cc3333",
                        textDark: "#000000",
                        textLight: "#ffffff"
                    }
                },
                fonts: {
                    default: null,
                    "'Cute Font', cursive": "https://fonts.googleapis.com/css?family=Cute+Font",
                    "'Gamja Flower', cursive": "https://fonts.googleapis.com/css?family=Gamja+Flower|PT+Serif"
                }
            },
            function(error, result) {
                if (result.event === "success") {
                    //myUploadWidget.close();

                    var imageInfo = document.getElementById("imageInfo");
                    if (imageInfo !== null) {
                        var uploadInfo = document.createTextNode(document.getElementById("imageSuccess").value)
                        var a = document.createElement("A");
                        a.appendChild(uploadInfo);
                        a.style.color = "green";
                        imageInfo.appendChild(a);
                    }

                    var avatar = document.getElementById('myImg');
                    if (avatar !== null) {
                        avatar.setAttribute("src", result.info.url);
                    }

                    $("#avatarUrl").val(result.info.url);

                } else if (error !== undefined){
                    var errorInfo = document.getElementById("imageInfo");
                    if (errorInfo !== null) {
                        var errorMessage = document.createTextNode(document.getElementById("imageError").value)
                        var a = document.createElement("A");
                        a.appendChild(errorMessage);
                        a.style.color = "red";
                        errorInfo.appendChild(a);
                    }
                }
            });
    }, false);
}