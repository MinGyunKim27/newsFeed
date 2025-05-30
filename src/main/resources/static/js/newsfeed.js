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
        await checkFollowStatus();
        const postElement = document.createElement("div");

        const data = await res.json();
        const feed = document.getElementById("post-list");
        feed.innerHTML = "";

        // formatDate 함수를 파일 어디든 추가
        function formatDate(dateString) {
            if (!dateString) return '';

            try {
                const date = new Date(dateString);
                const now = new Date();
                const diffMs = now - date;
                const diffMins = Math.floor(diffMs / 60000);
                const diffHours = Math.floor(diffMs / 3600000);
                const diffDays = Math.floor(diffMs / 86400000);

                if (diffMins < 1) return '방금 전';
                if (diffMins < 60) return `${diffMins}분 전`;
                if (diffHours < 24) return `${diffHours}시간 전`;
                if (diffDays < 7) return `${diffDays}일 전`;

                return date.toLocaleDateString('ko-KR');
            } catch (err) {
                return '';
            }
        }

        data.posts.forEach(post => {
            const postElement = document.createElement("div");
            postElement.className = "post";
            postElement.innerHTML = `
                 <div class="author-info">
                    <img class="author-img" src="${post.authorImageUrl
                                ? `${baseUrl}/images/${post.authorImageUrl}`
                                : 'https://via.placeholder.com/32x32?text=No+Img'}" 
                         alt="작성자 이미지" />
                    <div class="author-details">
                        <strong style="font-weight: 600; color: #374151;">${post.author}</strong>
                        <span class="post-time" style="color: #6b7280; font-size: 14px; margin-left: 8px;">${formatDate(post.createdAt)}</span>
                    </div>
                    ${post.authorId == userId ?
                `<div class="post-menu">
                        <button class="menu-btn" onclick="togglePostMenu(${post.id})" style="background: none; border: none; cursor: pointer; font-size: 18px; color: #6b7280; padding: 8px;">
                            ⋯
                            </button>
                            <div id="post-menu-${post.id}" class="menu-dropdown" style="display: none; position: absolute; right: 0; top: 35px; background: white; border: 1px solid #e5e7eb; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); z-index: 100; min-width: 120px;">
                                <button onclick="openEditPostModal(${post.id}, '${post.title}', '${post.content}', '${post.imageUrl || ''}'); hidePostMenu(${post.id})" 
                                        style="width: 100%; padding: 12px 16px; border: none; background: none; text-align: left; cursor: pointer; border-bottom: 1px solid #f3f4f6;" 
                                        onmouseover="this.style.background='#f9fafb'" onmouseout="this.style.background='none'">
                                    수정
                                </button>
                                <button onclick="deletePost(${post.id}); hidePostMenu(${post.id})" 
                                        style="width: 100%; padding: 12px 16px; border: none; background: none; text-align: left; cursor: pointer; color: #ef4444;" 
                                        onmouseover="this.style.background='#fef2f2'" onmouseout="this.style.background='none'">
                                    삭제
                                </button>
                            </div>
                        </div>` :
                                    `<button class="follow-btn-small" data-author-id="${post.authorId}" onclick="toggleFollow(${post.authorId}, this)">
                            팔로우
                        </button>`
                            }
                </div>

                <h3>${post.title}</h3>
                <p>${post.content}</p>
                ${post.imageUrl
                ? `<img onclick="openImageModal('${baseUrl}/images/${post.imageUrl}')" src="${baseUrl}/images/${post.imageUrl}" alt="게시물 이미지">`
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
            checkAuthorFollowStatus(post.authorId, postElement);
            async function checkAuthorFollowStatus(authorId, postElement) {
                try {
                    const res = await fetch(`${baseUrl}/api/users/follows/${authorId}/isFollow`, {
                        headers: { "Authorization": `Bearer ${token}` }
                    });

                    if (res.ok) {
                        const data = await res.json();

                        // 해당 authorId를 가진 모든 버튼 업데이트 (초기 로딩 시)
                        const allFollowButtons = document.querySelectorAll(`[data-author-id="${authorId}"]`);

                        allFollowButtons.forEach(button => {
                            if (data.follow) {
                                button.textContent = '팔로잉';
                                button.classList.add('following');
                                button.style.background = "linear-gradient(135deg, #10b981, #059669)";
                            }
                        });
                    }
                } catch (err) {
                    console.error('팔로우 상태 확인 실패:', err);
                }
            }
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
        await loadPosts();
    } catch (err) {
        console.error("❌ 게시 실패:", err);
        alert("게시 실패: " + err.message);
    }
}

// 이미지 모달 열기 (수정된 부분)
function openImageModal(imageSrc) {
    const modalImage = document.getElementById("modalImage");
    modalImage.src = imageSrc;
    document.getElementById("imageModal").style.display = "flex";
}

// 이미지 모달 닫기
function closeImageModal() {
    document.getElementById("imageModal").style.display = "none";
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

// 팔로우 여부 확인
async function checkFollowStatus() {
    try {
        const res = await fetch(`${baseUrl}/api/users/follows/${authorId}/isFollow`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.ok) {
            const data = await res.json();
            isFollow = data.follow || false;
            updateFollowButton();
        }
    } catch (err) {
        console.error("팔로우 여부 확인 실패:", err);
    }
}

// 버튼 상태 업데이트
function updateFollowButton() {
    const btn = document.querySelector(".follow-btn-small");
    if (btn) {
        btn.textContent = isFollow ? "팔로잉" : "팔로우";
        btn.style.background = isFollow
            ? "linear-gradient(135deg, #3181fa, #dc2626)"
            : "linear-gradient(135deg, #3b82f6, #2777fa)";
    }
}

// 수정해야 할 부분:
async function toggleFollow(authorId, buttonElement) {
    const btn = buttonElement || event.target;
    const isFollowing = btn.textContent.trim() === '팔로잉';

    try {
        const method = isFollowing ? 'DELETE' : 'POST';
        const res = await fetch(`${baseUrl}/api/users/follows/${authorId}`, {
            method: method,
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (res.ok) {
            // 해당 authorId를 가진 모든 팔로우 버튼 찾아서 일괄 업데이트
            const allFollowButtons = document.querySelectorAll(`[data-author-id="${authorId}"]`);

            allFollowButtons.forEach(button => {
                button.textContent = isFollowing ? '팔로우' : '팔로잉';

                if (isFollowing) {
                    button.classList.remove('following');
                    button.style.background = "linear-gradient(135deg, #007aff, #0056b3)";
                } else {
                    button.classList.add('following');
                    button.style.background = "linear-gradient(135deg, #10b981, #059669)";
                }
            });
        }
    } catch (err) {
        console.error('팔로우 처리 실패:', err);
        alert('팔로우 처리 중 오류가 발생했습니다.');
    }
}

let editingPostId = null;

function openEditPostModal(postId, title, content, imageUrl) {
    editingPostId = postId;
    document.getElementById('editPostTitle').value = title;
    document.getElementById('editPostContent').value = content;
    document.getElementById('editImageUpload').value = '';
    document.getElementById('editPostModal').style.display = 'flex';
}

function closeEditPostModal() {
    document.getElementById('editPostModal').style.display = 'none';
    editingPostId = null;
}

async function updatePost() {
    const title = document.getElementById('editPostTitle').value.trim();
    const content = document.getElementById('editPostContent').value.trim();
    const imageFile = document.getElementById('editImageUpload').files[0];

    if (!title || !content) {
        alert('제목과 내용을 입력하세요.');
        return;
    }

    let imageUrl = "";

    // 새 이미지가 선택된 경우 업로드
    if (imageFile) {
        const formData = new FormData();
        formData.append("file", imageFile);

        try {
            const res = await fetch(`${baseUrl}/api/image/upload`, {
                method: "POST",
                headers: { "Authorization": `Bearer ${token}` },
                body: formData
            });

            const text = await res.text();
            const match = text.match(/파일 업로드 성공: (.+)/);
            if (match && match[1]) {
                imageUrl = match[1];
            }
        } catch (err) {
            alert("이미지 업로드 실패: " + err.message);
            return;
        }
    }

    const payload = { title, content };
    if (imageUrl) payload.imageUrl = imageUrl;

    try {
        const res = await fetch(`${baseUrl}/api/posts/${editingPostId}`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert('게시물이 수정되었습니다.');
            closeEditPostModal();
            await loadPosts();
        } else {
            throw new Error('수정 실패');
        }
    } catch (err) {
        alert('게시물 수정 실패: ' + err.message);
    }
}

function togglePostMenu(postId) {
    const menu = document.getElementById(`post-menu-${postId}`);
    const isVisible = menu.style.display === 'block';

    // 다른 모든 메뉴 닫기
    document.querySelectorAll('.menu-dropdown').forEach(m => m.style.display = 'none');

    // 현재 메뉴 토글
    menu.style.display = isVisible ? 'none' : 'block';
}

function hidePostMenu(postId) {
    document.getElementById(`post-menu-${postId}`).style.display = 'none';
}

async function deletePost(postId) {
    if (!confirm('정말로 이 게시물을 삭제하시겠습니까?')) return;

    try {
        const res = await fetch(`${baseUrl}/api/posts/${postId}`, {
            method: 'DELETE',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (res.ok) {
            alert('게시물이 삭제되었습니다.');
            await loadPosts();
        } else {
            throw new Error('삭제 실패');
        }
    } catch (err) {
        alert('게시물 삭제 실패: ' + err.message);
    }
}

// 문서 클릭 시 메뉴 닫기
document.addEventListener('click', function(e) {
    if (!e.target.closest('.post-menu')) {
        document.querySelectorAll('.menu-dropdown').forEach(m => m.style.display = 'none');
    }
});