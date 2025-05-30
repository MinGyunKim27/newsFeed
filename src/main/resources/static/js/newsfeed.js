// static/js/newsfeed.js

const baseUrl = "http://localhost:8080";
const token = localStorage.getItem("token");
const userId = localStorage.getItem("userId"); // ✅ 로그인 시 저장된 사용자 ID 필요

// 페이지 이동
function goTo(path) {
    window.location.href = `/${path}`;
}

// 게시글 불러오기
async function loadPosts() {
    try {
        const res = await fetch(`${baseUrl}/api/posts?page=0&size=10`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        const data = await res.json();
        const feed = document.getElementById("post-list");
        feed.innerHTML = "";

        data.posts.forEach(post => {
            const postElement = document.createElement("div");
            postElement.className = "post";
            postElement.innerHTML = `
                <div class="author-info">
                    <img class="author-img" src="${post.authorImageUrl
                ? `${baseUrl}/images/${post.authorImageUrl}`
                : 'https://via.placeholder.com/32x32?text=No+Img'}" 
                        alt="작성자 이미지" />
                    <strong style="font-weight: 600; color: #374151;">${post.author}</strong>
                </div>

                <h3>${post.title}</h3>
                <p>${post.content}</p>
                ${post.imageUrl
                        ? `<img src="${baseUrl}/images/${post.imageUrl}" alt="게시물 이미지">`
                        : ""}
                <div class="post-actions">
                    <span class="action">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="#4B5563" stroke-width="1.8" viewBox="0 0 24 24">
                            <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 1 0-7.8 7.8l1 1L12 21l7.8-7.8 1-1a5.5 5.5 0 0 0 0-7.8z"/>
                        </svg> <span>1</span>
                    </span>
                    <span class="action" onclick="toggleComments(${post.id})">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="#4B5563" stroke-width="1.8" viewBox="0 0 24 24">
                            <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 1 1 17 0z"/>
                        </svg>
                        <span id="comment-count-${post.id}">0</span>
                    </span>
                </div>
                <div id="comment-section-${post.id}" class="comment-input" style="display:none; margin-top:10px;">
                    <div id="comment-list-${post.id}" style="margin-bottom:8px;"></div>
                    <div class="comment-form">
                        <textarea id="comment-input-${post.id}" class="comment-input" placeholder="댓글을 입력하세요..."></textarea>
                        <button class="comment-submit" onclick="submitComment(${post.id})">작성</button>
                    </div>
                </div>
            `;
            loadCommentCount(post.id);
            feed.appendChild(postElement);
        });
    } catch (err) {
        console.error("❌ 뉴스피드 로딩 실패:", err);
        alert("뉴스피드를 불러오지 못했습니다: " + err.message);
    }
}

// 댓글 수 불러오기
function loadCommentCount(postId) {
    fetch(`${baseUrl}/api/posts/${postId}/comments`, {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => {
            const countEl = document.getElementById(`comment-count-${postId}`);
            if (countEl) countEl.textContent = data.content.length;
        })
        .catch(err => console.error(`❌ 댓글 수 로딩 실패 (${postId}):`, err));
}

// 댓글 보기 토글
function toggleComments(postId) {
    const section = document.getElementById(`comment-section-${postId}`);
    const visible = section.style.display === "block";
    section.style.display = visible ? "none" : "block";
    if (!visible) loadComments(postId);
}

// 댓글 불러오기
function loadComments(postId) {
    fetch(`${baseUrl}/api/posts/${postId}/comments`, {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => {
            const list = data.content.map(c => `
                <div class="comment-item">
                    <strong>${c.name}</strong><br>${c.content}
                </div>
            `).join("") || "<p>(댓글 없음)</p>";
            document.getElementById(`comment-list-${postId}`).innerHTML = list;
            document.getElementById(`comment-count-${postId}`).textContent = data.content.length;
        })
        .catch(err => {
            console.error("❌ 댓글 로딩 에러:", err);
            document.getElementById(`comment-list-${postId}`).innerHTML = "댓글 불러오기 실패";
        });
}

// 댓글 작성
function submitComment(postId) {
    const content = document.getElementById(`comment-input-${postId}`).value.trim();
    if (!content) return alert("댓글을 입력하세요.");

    fetch(`${baseUrl}/api/posts/${postId}/comments`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ content })
    })
        .then(res => res.json())
        .then(() => {
            document.getElementById(`comment-input-${postId}`).value = "";
            loadComments(postId);
        })
        .catch(err => alert("❌ 댓글 작성 실패: " + err.message));
}

let userProfileImageUrl = "";

async function loadUserInfo() {
    try {
        const res = await fetch(`${baseUrl}/api/users/${userId}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!res.ok) throw new Error("유저 정보를 불러올 수 없습니다.");
        const data = await res.json();

        const profileImage = data.profileImage?.trim();
        const img = document.getElementById("profile-img");

        if (profileImage) {
            userProfileImageUrl = `${baseUrl}/images/${profileImage}`;
            img.src = userProfileImageUrl;
        } else {
            img.src = "https://via.placeholder.com/40x40?text=No+Img";
        }
    } catch (err) {
        console.error("❌ 사용자 정보 오류:", err);
    }
}

//게시물 작성
async function submitPost() {
    const title = document.getElementById("postTitle").value.trim();
    const content = document.getElementById("postContent").value.trim();
    const imageFile = document.getElementById("imageUpload").files[0];

    if (!title || !content) {
        alert("제목과 내용을 입력하세요.");
        return;
    }

    let imageUrl = "";

    if (imageFile) {
        const formData = new FormData();
        formData.append("file", imageFile);

        try {
            const res = await fetch(`${baseUrl}/api/image/upload`, {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                },
                body: formData
            });

            const text = await res.text();
            const match = text.match(/파일 업로드 성공: (.+)/);
            if (match && match[1]) {
                imageUrl = match[1];
            } else {
                alert("❌ 이미지 업로드 응답 해석 실패");
                return;
            }
        } catch (err) {
            alert("❌ 이미지 업로드 실패: " + err.message);
            return;
        }
    }

    const payload = {
        title,
        content,
        imageUrl
    };

    try {
        const res = await fetch(`${baseUrl}/api/posts`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error("게시물 등록 실패");

        alert("✅ 게시물 작성 완료!");
        closeModal();
        loadPosts();
    } catch (err) {
        console.error("❌ 게시 실패:", err);
        alert("게시 실패: " + err.message);
    }
}




// 팝업 열고 닫기
function openModal() {
    document.getElementById("overlay").style.display = "flex";
}
function closeModal() {
    document.getElementById("overlay").style.display = "none";
}

// 초기 로딩
document.addEventListener("DOMContentLoaded", () => {
    loadUserInfo();
    loadPosts();
});
