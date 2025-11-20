const close = document.getElementById("close");
const errorBox = document.getElementById("error");

close.addEventListener('click', () => {
    console.log(errorBox);
    closePopup(errorBox);
});

function closePopup(elementToClose) {
    elementToClose.style.display = "none";
}