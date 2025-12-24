
const API = {
    course: "/api/course/",
    sendMessage: "/api/message/"
};

const params = new URLSearchParams(window.location.search);
const courseId = params.get("id");
const chatId = params.get("chatId");

loadCourse(courseId);

function goBack() {
    window.location.href = "/?chatId=" + chatId;
}

/* LOAD COURSE */
function loadCourse(courseId) {
    fetch(API.course + courseId)
        .then(res => res.json())
        .then(course => {
            courseTitle.textContent = course.title;
            courseDescription.textContent = course.description || "";
            renderLessons(course.lessons || []);
        });
}

/* LESSONS */
function renderLessons(lessons) {
    lessons
        .sort((a, b) => a.orderNumber - b.orderNumber)
        .forEach(lesson => {
            const card = document.createElement("div");
            card.className = "lesson-card";

            let html = `
                <h4 class="lesson-title">${lesson.title}</h4>
                <p class="lesson-description">${lesson.description || ""}</p>
            `;

            if (lesson.messageIds?.length) {
                lesson.messageIds.forEach((id, i) => {
                    html += `
                        <button
                            class="message-button"
                            onclick="sendMessage(${id}, this)">
                            Урок ${i + 1}
                        </button>
                    `;
                });
            }

            card.innerHTML = html;
            lessonsContainer.appendChild(card);
        });
}

let popupTimeout;

function showPopup(message) {
    const popup = document.getElementById("popupMessage");
    const popupText = document.getElementById("popupText");
    const popupClose = document.getElementById("popupClose");

    popupText.textContent = message;
    popup.classList.add("show");

    clearTimeout(popupTimeout);
    popupTimeout = setTimeout(() => {
        popup.classList.remove("show");
    }, 3000);

    popupClose.onclick = () => {
        popup.classList.remove("show");
        clearTimeout(popupTimeout);
    };
}

function sendMessage(messageId, btn) {
    btn.disabled = true;

    fetch(`${API.sendMessage}${messageId}?chatId=${chatId}`, { method: "POST" })
        .then(res => {
            if (res.ok) {
                showPopup("Сообщение отправлено в бот 🚀");
                btn.disabled = false;
            } else {
                showPopup("Ошибка при отправке сообщения");
                btn.disabled = false;
            }
        })
        .catch(() => {
            showPopup("Ошибка соединения с сервером");
            btn.disabled = false;
        });
}
