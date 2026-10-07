const formMessage = document.querySelector("#form-message");
const postList = document.querySelector("#post-list");

const editSection = document.querySelector("#edit-section");
const editId = document.querySelector("#edit-id");
const editTitle = document.querySelector("#edit-title");
const editContent = document.querySelector("#edit-content");

async function loadPosts() {
    postList.textContent = "게시글을 불러오는 중입니다...";

    try {
        const response = await fetch("/posts");

        if (!response.ok) {
            postList.textContent = "게시글을 불러오지 못했습니다.";
            return;
        }

        const posts = await response.json();

        postList.innerHTML = "";

        if (posts.length === 0) {
            postList.textContent = "등록된 게시글이 없습니다.";
            return;
        }

        posts.forEach(function (post) {
            const postElement = document.createElement("div");

            postElement.innerHTML = `
                <h3>${post.title}</h3>
                <p>${post.content}</p>
                <button
                    class="edit-button"
                    data-id="${post.id}"
                    data-title="${post.title}"
                    data-content="${post.content}">
                    수정
                </button>
                <button class="delete-button" data-id="${post.id}">
                    삭제
                </button>
            `;

            postList.appendChild(postElement);
        });

        const deleteButtons = document.querySelectorAll(".delete-button");

        deleteButtons.forEach(function (button) {
            button.addEventListener("click", async function () {
                const id = button.dataset.id;

                await fetch(`/posts/${id}`, {
                    method: "DELETE"
                });

                await loadPosts();
            })
        })

        const editButtons = document.querySelectorAll(".edit-button");

        editButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                const postId = button.dataset.id;

                editId.value = postId;
                editTitle.value = button.dataset.title;
                editContent.value = button.dataset.content;

                editSection.classList.remove("hidden");
            });
        });

    } catch (error) {
        postList.textContent =
            "서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.";

        console.error(error);
    }
}

    loadPosts();

    const postForm = document.querySelector("#post-form");

    postForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const title = document.querySelector("#title").value;
        const content = document.querySelector("#content").value;

        const response = await fetch("/posts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        });

        if (response.ok) {
            formMessage.textContent = "게시글이 등록되었습니다.";
            postForm.reset();
            await loadPosts();
        } else {
            const error = await response.json();
            formMessage.textContent = error.title || "게시글 등록에 실패했습니다.";
        }

    })

    const editForm = document.querySelector("#edit-form");

    editForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const id = editId.value;
        const title = editTitle.value;
        const content = editContent.value;

        const response = await fetch(`/posts/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        });

        if (response.ok) {
            formMessage.textContent = "게시글이 수정되었습니다.";
            editForm.reset();
            editSection.classList.add("hidden");
            await loadPosts();
        } else {
            const error = await response.json();
            formMessage.textContent =
                error.title || "게시글 수정에 실패했습니다.";
        }
    });

    const cancelEdit = document.querySelector("#cancel-edit");

    cancelEdit.addEventListener("click", function () {
        editForm.reset();
        editSection.classList.add("hidden");
    });
