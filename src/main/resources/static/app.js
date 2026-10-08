const formMessage = document.querySelector("#form-message");
const postList = document.querySelector("#post-list");

const editSection = document.querySelector("#edit-section");
const editId = document.querySelector("#edit-id");
const editTitle = document.querySelector("#edit-title");
const editContent = document.querySelector("#edit-content");

async function loadComments(postId, commentList) {
    const response = await fetch(`/posts/${postId}/comments`);

    if (!response.ok) {
        commentList.textContent = "댓글을 불러오지 못했습니다.";
        return;
    }

    const comments = await response.json();

    if (comments.length === 0) {
        commentList.textContent = "등록된 댓글이 없습니다.";
        return;
    }

    commentList.innerHTML = comments
        .map(function (comment) {
            return `<p>${comment.content}</p>`;
        })
        .join("");
}

function setupCommentForms() {
    const commentForms = document.querySelectorAll(".comment-form");

    commentForms.forEach(function (form) {
        form.addEventListener("submit", async function (event) {
            event.preventDefault();

            const postId = form.dataset.postId;
            const content = form.elements.content.value;

            const response = await fetch(`/posts/${postId}/comments`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ content: content })
            });

            if (!response.ok) {
                alert("댓글 등록에 실패했습니다.");
                return;
            }

            form.reset();

            const commentList = form
                .closest(".comment-section")
                .querySelector(".comment-list");

            await loadComments(postId, commentList);
        });
    });
}

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
                <div class="comment-section">
                <h4>댓글</h4>
            
                <div class="comment-list" data-post-id="${post.id}">
                    댓글을 불러오는 중...
                </div>
            
                <form class="comment-form" data-post-id="${post.id}">
                    <input
                        type="text"
                        name="content"
                        placeholder="댓글을 입력하세요"
                        required
                    />
                    <button type="submit">댓글 등록</button>
                </form>
                </div>
            `;

            postList.appendChild(postElement);

            const commentList = postElement.querySelector(".comment-list");

            loadComments(post.id, commentList);
        
        });

        setupCommentForms();

        const deleteButtons = document.querySelectorAll(".delete-button");

        deleteButtons.forEach(function (button) {
            button.addEventListener("click", async function () {
                const id = button.dataset.id;

                try {
                    const response = await fetch(`/posts/${id}`, {
                        method: "DELETE"
                    });

                    if (!response.ok) {
                        formMessage.textContent = "게시글 삭제에 실패했습니다.";
                        return;
                    }

                    formMessage.textContent = "게시글이 삭제되었습니다.";
                    await loadPosts();
                } catch (error) {
                    formMessage.textContent =
                        "서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.";
                    console.error(error);
                }
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

        try {
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
        } catch (error) {
            formMessage.textContent =
                "서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.";
            console.error(error);
        }
    });

    const editForm = document.querySelector("#edit-form");

    editForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const id = editId.value;
        const title = editTitle.value;
        const content = editContent.value;

        try {
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
        } catch (error) {
            formMessage.textContent =
                "서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.";
            console.error(error);
        }
    });

    const cancelEdit = document.querySelector("#cancel-edit");

    cancelEdit.addEventListener("click", function () {
        editForm.reset();
        editSection.classList.add("hidden");
    });
