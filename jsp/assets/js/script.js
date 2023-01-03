const triggers = document.querySelectorAll(".js-modal-trigger");
const supprimer = document.querySelector("#supprimer");

triggers.forEach((trigger) => {
  trigger.addEventListener("click", (e) => {
    e.preventDefault();
    let url = `${trigger.getAttribute("href")}?${trigger.id}`;
    supprimer.setAttribute("href", url);
  });
});
