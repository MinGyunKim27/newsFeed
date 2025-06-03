// static/js/newsfeed.js

const baseUrl = "http://localhost:8080";
let currentPage = 0;
let isLoading = false;
let hasMore = true;
const token = localStorage.getItem("token");
const userId = localStorage.getItem("userId"); // ✅ 로그인 시 저장된 사용자 ID 필요
// 전역 변수 수정
let existingImages = []; // 기존 이미지 관리용
let imagesToDelete = []; // 삭제할 이미지 ID들

// 페이지 이동
function goTo(path) {
    window.location.href = `/${path}`;
}

// 수정된 코드:
function goToProfile(authorId) {
    if (authorId == userId) {  // ✅ 매개변수명과 일치
        window.location.href = `/mypage.html`;
    } else {
        window.location.href = `/user-profile.html?userId=${authorId}`;
    }
}

// 게시글 불러오기 (수정됨)
async function loadPosts(page = 0, append = false) {
    try {
        // 기존 try { 바로 아래에 추가
        if (isLoading || (!hasMore && page > 0)) return;
        isLoading = true;

        console.log("📰 게시물 로딩 시작...");

        const res = await fetch(`${baseUrl}/api/posts?page=${page}&size=10`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!res.ok) {
            throw new Error(`게시물 로딩 실패: ${res.status}`);
        }

        const data = await res.json();
        console.log("📰 게시물 데이터:", data);

        const feed = document.getElementById("post-list");
        if (!append) {
            feed.innerHTML = "";
        }

        // formatDate 함수
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

        // 각 게시물 처리
        data.posts.forEach(post => {
            console.log(`📰 게시물 처리 중: ${post.id}`);

            const postElement = document.createElement("div");
            postElement.className = "post";

            // 이미지 HTML 생성 (images 배열로 수정)
            console.log("🖼️ 게시물 이미지 데이터:", post.images);

            const imageHtml = post.images && post.images.length > 0
                ? post.images.length === 1
                    ? `<img onclick="openImageModal('${baseUrl}/images/${post.images[0].imageUrl}')" 
             src="${baseUrl}/images/${post.images[0].imageUrl}" 
             alt="게시물 이미지" 
             style="width: 100%; max-height: 400px; object-fit: cover; cursor: pointer; border-radius: 8px;"
             onerror="console.error('❌ 이미지 로딩 실패'); this.style.display='none';" />`
                    : `<div style="display: flex; gap: 8px; overflow-x: auto; padding: 8px 0; scrollbar-width: thin;">
            ${post.images.map((imageObj, imgIndex) => {
                        const fullUrl = `${baseUrl}/images/${imageObj.imageUrl}`;
                        return `<img onclick="openImageModal('${fullUrl}')" 
                           src="${fullUrl}" 
                           alt="게시물 이미지 ${imgIndex + 1}" 
                           style="min-width: 200px; max-width: 200px; height: 150px; object-fit: cover; cursor: pointer; border-radius: 8px; flex-shrink: 0;"
                           onerror="console.error('❌ 이미지 로딩 실패:', '${fullUrl}'); this.style.display='none';"
                           onload="console.log('✅ 이미지 로딩 성공:', '${fullUrl}');" />`;
                    }).join('')}
           </div>`
                : "";

            postElement.innerHTML = `
                <div class="author-info" style="display: flex; align-items: center; margin-bottom: 12px; position: relative;">
                    <img class="author-img" 
                         src="${post.authorImageUrl ? `${baseUrl}/images/${post.authorImageUrl}` : 'https://via.placeholder.com/40x40?text=No+Img'}" 
                         alt="작성자 이미지" 
                         onclick="goToProfile(${post.authorId})"
                         style="width: 40px; height: 40px; border-radius: 50%; margin-right: 12px; cursor: pointer; object-fit: cover;"
                         onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8IS0tIOq3uOudvOuUlOyWuO2KuCDsoJXsnZggLS0+CiAgPGRlZnM+CiAgICA8bGluZWFyR3JhZGllbnQgaWQ9ImJnR3JhZGllbnQiIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPgogICAgICA8c3RvcCBvZmZzZXQ9IjAlIiBzdHlsZT0ic3RvcC1jb2xvcjojNjY3ZWVhO3N0b3Atb3BhY2l0eToxIiAvPgogICAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTI7c3RvcC1vcGFjaXR5OjEiIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogICAgPGxpbmVhckdyYWRpZW50IGlkPSJwZXJzb25HcmFkaWVudCIgeDE9IjAlIiB5MT0iMCUiIHgyPSIxMDAlIiB5Mj0iMTAwJSI+CiAgICAgIDxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiNmZmZmZmY7c3RvcC1vcGFjaXR5OjAuOSIgLz4KICAgICAgPHN0b3Agb2Zmc2V0PSIxMDAlIiBzdHlsZT0ic3RvcC1jb2xvcjojZmZmZmZmO3N0b3Atb3BhY2l0eTowLjciIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogIDwvZGVmcz4KICA8IS0tIOybkO2YhSDrsLDqsr0gKOq3uOudvOuUlOyWuO2KuCkgLS0+CiAgPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9InVybCgjYmdHcmFkaWVudCkiLz4KICA8IS0tIOyCrOumhCDsi6TroKjsmIvsnIQgKOuNlCDshLjroKjrkJzsmYQg65SU7J6Q7J2EKSAtLT4KICA8IS0tIOuquOumrCAtLT4KICA8Y2lyY2xlIGN4PSIyMCIgY3k9IjE0IiByPSI3IiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgogIDwhLS0g66qJ7YG1ICjrjZQg7J6Q7Jew7Iqk7Yuw7JuQIOqzoOyEoCkgLS0+CiAgPHBhdGggZD0iTTYgMzYgQzYgMjcsIDEyIDIzLCAyMCAyMyBDMjggMjMsIDM0IDI3LCAzNCAzNiBDMzQgMzgsIDM0IDQwLCAzNCA0MCBMOSA0MCBDOCA0MCwgNiAzOCwgNiAzNiBaIiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgo8L3N2Zz4K'" />
                    <div class="author-details">
                        <strong onclick="goToProfile(${post.authorId})" 
                                style="font-weight: 600; color: #374151; cursor: pointer; transition: color 0.3s ease;"
                                onmouseover="this.style.color='#007aff'"
                                onmouseout="this.style.color='#374151'">
                            ${post.author}
                        </strong>
                        <span class="post-time" style="color: #6b7280; font-size: 14px; margin-left: 8px;">
                            ${formatDate(post.createdAt)}
                        </span>
                    </div>
                    ${post.authorId == userId ?
                `<div class="post-menu" style="margin-left: auto; position: relative;">
                            <button class="menu-btn" onclick="togglePostMenu(${post.id})" style="background: none; border: none; cursor: pointer; font-size: 18px; color: #6b7280; padding: 8px;">⋯</button>
                            <div id="post-menu-${post.id}" class="menu-dropdown" style="display: none; position: absolute; right: 0; top: 35px; background: white; border: 1px solid #e5e7eb; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); z-index: 100; min-width: 120px;">
                                <button class="change-btn" onclick="editPost(${post.id}); hidePostMenu(${post.id})"
                                        style="width: 100%; padding: 12px 16px; border: none; background: none; text-align: left; cursor: pointer; border-bottom: 1px solid #f3f4f6;" 
                                        onmouseover="this.style.background='#f9fafb'" onmouseout="this.style.background='none'">
                                    수정
                                </button>
                                <button class="delete-btn" onclick="deletePost(${post.id}); hidePostMenu(${post.id})"
                                        style="width: 100%; padding: 12px 16px; border: none; background: none; text-align: left; cursor: pointer; color: #ef4444;" 
                                        onmouseover="this.style.background='#fef2f2'" onmouseout="this.style.background='none'">
                                    삭제
                                </button>
                            </div>
                        </div>` :
                `<button class="follow-btn-small" data-author-id="${post.authorId}" onclick="toggleFollow(${post.authorId}, this)" 
                                 style="margin-left: auto; padding: 6px 12px; background: linear-gradient(135deg, #007aff, #0056b3); color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 12px; font-weight: 500;">
                            팔로우
                        </button>`
            }
                </div>
                
                <h3 style="margin: 0 0 8px 0; font-size: 18px; color: #1f2937;">${post.title}</h3>
                <p style="margin: 0 0 12px 0; line-height: 1.5; color: #374151;">${post.content}</p>
                ${imageHtml}
                
                <div class="post-actions" style="display: flex; gap: 16px; align-items: center; padding-top: 12px; border-top: 1px solid #f3f4f6; margin-top: 12px;">
                    <span class="action like-action" onclick="toggleLike(${post.id}, this)" 
                          style="display: flex; align-items: center; gap: 4px; cursor: pointer; color: #6b7280; padding: 8px 12px; border-radius: 8px; transition: all 0.3s ease;"
                          onmouseover="this.style.backgroundColor='#f3f4f6'; this.style.color='#ef4444';"
                          onmouseout="this.style.backgroundColor='transparent'; this.style.color=this.classList.contains('liked') ? '#ef4444' : '#6b7280';">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="1.8" viewBox="0 0 24 24">
                            <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 1 0-7.8 7.8l1 1L12 21l7.8-7.8 1-1a5.5 5.5 0 0 0 0-7.8z"/>
                        </svg>
                        <span id="like-count-${post.id}">0</span>
                    </span>
                    <span class="action" onclick="toggleComments(${post.id})"
                          style="display: flex; align-items: center; gap: 4px; cursor: pointer; color: #6b7280; padding: 8px 12px; border-radius: 8px; transition: all 0.3s ease;"
                          onmouseover="this.style.backgroundColor='#f3f4f6'; this.style.color='#007aff';"
                          onmouseout="this.style.backgroundColor='transparent'; this.style.color='#6b7280';">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" stroke="currentColor" stroke-width="1.8" viewBox="0 0 24 24">
                            <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 1 1 17 0z"/>
                        </svg>
                        <span id="comment-count-${post.id}">0</span>
                    </span>
                </div>
                
                <div id="comment-section-${post.id}" class="comment-section" style="display:none; margin-top:16px; padding-top: 16px; border-top: 1px solid #f3f4f6;">
                    <div id="comment-list-${post.id}" style="margin-bottom:12px;"></div>
                    <div class="comment-form" style="display: flex; gap: 8px;">
                        <textarea id="comment-input-${post.id}" placeholder="댓글을 입력하세요..." 
                                  style="flex: 1; padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 6px; resize: vertical; min-height: 36px; font-family: inherit;"></textarea>
                        <button onclick="submitComment(${post.id})" 
                                style="padding: 8px 16px; background: #3b82f6; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: 500;">
                            작성
                        </button>
                    </div>
                </div>
            `;

            feed.appendChild(postElement);

            // 각 게시물별로 팔로우 상태 확인 (수정됨)
            if (post.authorId != userId) {
                checkAuthorFollowStatus(post.authorId);
            }

            // 비동기로 데이터 로드
            loadCommentCount(post.id);
            loadLikeCount(post.id);
            loadUserLikeStatus(post.id);
        });

        console.log("✅ 게시물 로딩 완료");

        // 기존 console.log("✅ 게시물 로딩 완료"); 아래에 추가
        hasMore = data.posts.length === 10;
        isLoading = false;

    } catch (err) {
        console.error("❌ 뉴스피드 로딩 실패:", err);
        alert("뉴스피드를 불러오지 못했습니다: " + err.message);
    }
}

async function editPost(postId) {
    try {
        console.log("🔧 게시물 수정 요청:", postId);

        const res = await fetch(`${baseUrl}/api/posts/${postId}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!res.ok) {
            throw new Error(`게시물 조회 실패: ${res.status}`);
        }

        const post = await res.json();
        console.log("🔧 게시물 정보:", post);

        openEditPostModal(post.id, post.title, post.content, post.images || []);

    } catch (err) {
        console.error("❌ 게시물 정보 로딩 실패:", err);
        alert("게시물 정보를 불러올 수 없습니다: " + err.message);
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
            const commentList = document.getElementById(`comment-list-${postId}`);

            if (!data.content || data.content.length === 0) {
                commentList.innerHTML = "<p style='color: #6b7280; font-size: 14px; text-align: center; padding: 12px;'>댓글이 없습니다.</p>";
            } else {
                const commentsHtml = data.content.map(comment => `
                    <div class="comment-item" style="padding: 8px 0; border-bottom: 1px solid #f3f4f6; text-align: left !important;">
                        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px; text-align: left !important;">
                            <div style="display: flex; align-items: center; text-align: left !important;">
                                <strong style="font-size: 14px; color: #374151; text-align: left !important;">${comment.name || '익명'}</strong>
                                <span style="font-size: 12px; color: #9ca3af; margin-left: 8px; text-align: left !important;">${formatDate(comment.createdAt)}</span>
                            </div>

                            ${comment.userId == userId ? `
                                <div class="comment-menu" style="position: relative;">
                                    <button class="comment-menu-btn" onclick="toggleCommentMenu(${comment.id})"
                                            style="background: none; border: none; cursor: pointer; font-size: 14px; color: #6b7280; padding: 4px;">⋯</button>
                                    <div id="comment-menu-${comment.id}" class="comment-dropdown"
                                         style="display: none; position: absolute; right: 0; top: 20px; background: white; border: 1px solid #e5e7eb; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); z-index: 100; min-width: 80px;">
                                        <button onclick="editComment(${comment.id}, '${comment.content.replace(/'/g, "\\'")}'); hideCommentMenu(${comment.id})"
                                                class="comment-edit-btn"
                                                style="width: 100%; padding: 8px 12px; border: none; background: none; text-align: left; cursor: pointer; font-size: 12px;"
                                                onmouseover="this.style.background='#f9fafb'" onmouseout="this.style.background='none'">
                                            수정
                                        </button>
                                        <button onclick="deleteComment(${comment.id}, ${postId}); hideCommentMenu(${comment.id})"
                                                class="comment-delete-btn"
                                                style="width: 100%; padding: 8px 12px; border: none; background: none; text-align: left; cursor: pointer; color: #ef4444; font-size: 12px;"
                                                onmouseover="this.style.background='#fef2f2'" onmouseout="this.style.background='none'">
                                            삭제
                                        </button>
                                    </div>
                                </div>
                            ` : ''}
                        </div>
                        <p id="comment-text-${comment.id}" style="margin: 0; font-size: 14px; line-height: 1.4; color: #4b5563; text-align: left !important;">${comment.content || ''}</p>

                        <!-- 수정 입력 필드 (숨겨진 상태) -->
                        <div id="comment-edit-${comment.id}" style="display: none; margin-top: 8px; text-align: left !important;">
                            <textarea id="comment-edit-input-${comment.id}"
                                      style="width: 100%; padding: 8px; border: 1px solid #e5e7eb; border-radius: 4px; font-size: 14px; resize: vertical; min-height: 60px; box-sizing: border-box; text-align: left !important;">${comment.content || ''}</textarea>
                            <div style="margin-top: 8px; text-align: right;">
                                <button onclick="cancelEditComment(${comment.id})"
                                        style="padding: 4px 12px; margin-right: 8px; background: #e5e7eb; color: #374151; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">취소</button>
                                <button onclick="saveEditComment(${comment.id}, ${postId})"
                                        style="padding: 4px 12px; background: #3b82f6; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">저장</button>
                            </div>
                        </div>
                    </div>
                `).join("");
                commentList.innerHTML = commentsHtml;
            }

            // 댓글 수 업데이트
            const countEl = document.getElementById(`comment-count-${postId}`);
            if (countEl) {
                countEl.textContent = data.content.length;
            }
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

        console.log("👤 사용자 정보:", data); // 디버깅용

        const profileImage = data.profileImage?.trim();
        const img = document.getElementById("profile-img");

        if (profileImage) {
            // ❌ 현재: profileImage가 imageId일 수 있음
            // userProfileImageUrl = `${baseUrl}/images/${profileImage}`;

            // ✅ 수정: profileImage가 실제 파일명인지 확인
            console.log("👤 프로필 이미지 값:", profileImage);

            // 숫자인지 확인 (imageId인 경우)
            if (!isNaN(profileImage)) {
                console.log("⚠️ profileImage가 ID 형태입니다:", profileImage);
                // 이 경우 백엔드에서 실제 파일명을 주도록 수정해야 함
                userProfileImageUrl = `${baseUrl}/images/${profileImage}`;
            } else {
                // 파일명인 경우
                userProfileImageUrl = `${baseUrl}/images/${profileImage}`;
            }

            img.src = userProfileImageUrl;
            console.log("👤 최종 프로필 이미지 URL:", userProfileImageUrl);
        } else {
            img.src = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8IS0tIOq3uOudvOuUlOyWuO2KuCDsoJXsnZggLS0+CiAgPGRlZnM+CiAgICA8bGluZWFyR3JhZGllbnQgaWQ9ImJnR3JhZGllbnQiIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPgogICAgICA8c3RvcCBvZmZzZXQ9IjAlIiBzdHlsZT0ic3RvcC1jb2xvcjojNjY3ZWVhO3N0b3Atb3BhY2l0eToxIiAvPgogICAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTI7c3RvcC1vcGFjaXR5OjEiIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogICAgPGxpbmVhckdyYWRpZW50IGlkPSJwZXJzb25HcmFkaWVudCIgeDE9IjAlIiB5MT0iMCUiIHgyPSIxMDAlIiB5Mj0iMTAwJSI+CiAgICAgIDxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiNmZmZmZmY7c3RvcC1vcGFjaXR5OjAuOSIgLz4KICAgICAgPHN0b3Agb2Zmc2V0PSIxMDAlIiBzdHlsZT0ic3RvcC1jb2xvcjojZmZmZmZmO3N0b3Atb3BhY2l0eTowLjciIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogIDwvZGVmcz4KICA8IS0tIOybkO2YhSDrsLDqsr0gKOq3uOudvOuUlOyWuO2KuCkgLS0+CiAgPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9InVybCgjYmdHcmFkaWVudCkiLz4KICA8IS0tIOyCrOumhCDsi6TroKjsmIvsnIQgKOuNlCDshLjroKjrkJzsmYQg65SU7J6Q7J2EKSAtLT4KICA8IS0tIOuquOumrCAtLT4KICA8Y2lyY2xlIGN4PSIyMCIgY3k9IjE0IiByPSI3IiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgogIDwhLS0g66qJ7YG1ICjrjZQg7J6Q7Jew7Iqk7Yuw7JuQIOqzoOyEoCkgLS0+CiAgPHBhdGggZD0iTTYgMzYgQzYgMjcsIDEyIDIzLCAyMCAyMyBDMjggMjMsIDM0IDI3LCAzNCAzNiBDMzQgMzgsIDM0IDQwLCAzNCA0MCBMOSA0MCBDOCA0MCwgNiAzOCwgNiAzNiBaIiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgo8L3N2Zz4K";
        }

        // 에러 핸들링 추가
        img.onerror = () => {
            console.error("❌ 프로필 이미지 로딩 실패:", img.src);
            img.src = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8IS0tIOq3uOudvOuUlOyWuO2KuCDsoJXsnZggLS0+CiAgPGRlZnM+CiAgICA8bGluZWFyR3JhZGllbnQgaWQ9ImJnR3JhZGllbnQiIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPgogICAgICA8c3RvcCBvZmZzZXQ9IjAlIiBzdHlsZT0ic3RvcC1jb2xvcjojNjY3ZWVhO3N0b3Atb3BhY2l0eToxIiAvPgogICAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTI7c3RvcC1vcGFjaXR5OjEiIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogICAgPGxpbmVhckdyYWRpZW50IGlkPSJwZXJzb25HcmFkaWVudCIgeDE9IjAlIiB5MT0iMCUiIHgyPSIxMDAlIiB5Mj0iMTAwJSI+CiAgICAgIDxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiNmZmZmZmY7c3RvcC1vcGFjaXR5OjAuOSIgLz4KICAgICAgPHN0b3Agb2Zmc2V0PSIxMDAlIiBzdHlsZT0ic3RvcC1jb2xvcjojZmZmZmZmO3N0b3Atb3BhY2l0eTowLjciIC8+CiAgICA8L2xpbmVhckdyYWRpZW50PgogIDwvZGVmcz4KICA8IS0tIOybkO2YhSDrsLDqsr0gKOq3uOudvOuUlOyWuO2KuCkgLS0+CiAgPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9InVybCgjYmdHcmFkaWVudCkiLz4KICA8IS0tIOyCrOumhCDsi6TroKjsmIvsnIQgKOuNlCDshLjroKjrkJzsmYQg65SU7J6Q7J2EKSAtLT4KICA8IS0tIOuquOumrCAtLT4KICA8Y2lyY2xlIGN4PSIyMCIgY3k9IjE0IiByPSI3IiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgogIDwhLS0g66qJ7YG1ICjrjZQg7J6Q7Jew7Iqk7Yuw7JuQIOqzoOyEoCkgLS0+CiAgPHBhdGggZD0iTTYgMzYgQzYgMjcsIDEyIDIzLCAyMCAyMyBDMjggMjMsIDM0IDI3LCAzNCAzNiBDMzQgMzgsIDM0IDQwLCAzNCA0MCBMOSA0MCBDOCA0MCwgNiAzOCwgNiAzNiBaIiBmaWxsPSJ1cmwoI3BlcnNvbkdyYWRpZW50KSIvPgo8L3N2Zz4K";
        };

    } catch (err) {
        console.error("❌ 사용자 정보 오류:", err);
    }
}

//게시물 작성
async function submitPost() {
    const title = document.getElementById("postTitle").value.trim();
    const content = document.getElementById("postContent").value.trim();
    const imageFiles = Array.from(document.getElementById("imageUpload").files);

    if (!title || !content) {
        alert("제목과 내용을 입력하세요.");
        return;
    }

    let imageIds = [];
    if (imageFiles.length > 0) {
        for (const file of imageFiles) {
            const formData = new FormData();
            formData.append("file", file);

            try {
                const res = await fetch(`${baseUrl}/api/image/upload`, {
                    method: "POST",
                    headers: { "Authorization": `Bearer ${token}` },
                    body: formData
                });

                const data = await res.json();
                imageIds.push(data.imageId);
            } catch (err) {
                alert("이미지 업로드 실패: " + err.message);
                return;
            }
        }
    }

    // payload 변경:
    const payload = {
        title,
        content,
        imageIds: imageIds
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

document.addEventListener("DOMContentLoaded", () => {
    initializeTheme(); // 테마 먼저 적용
    loadUserInfo();
    loadPosts();
    initializeDropZones();

    // 스크롤 이벤트 추가
    window.addEventListener('scroll', () => {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 1000 && !isLoading && hasMore) {
            currentPage++;
            loadPosts(currentPage, true);
        }
    });
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
                    button.style.background = "linear-gradient(135deg, #3b82f6, #2777fa)";
                } else {
                    button.classList.add('following');
                    button.style.background = "linear-gradient(135deg, #3181fa, #dc2626)";
                }
            });
        }
    } catch (err) {
        console.error('팔로우 처리 실패:', err);
        alert('팔로우 처리 중 오류가 발생했습니다.');
    }
}

let editingPostId = null;

// 수정된 openEditPostModal 함수
function openEditPostModal(postId, title, content, images) {
    console.log("🔧 수정 모달 열림:", postId, title, content, images);

    editingPostId = postId;
    existingImages = [...(images || [])]; // 기존 이미지 복사
    imagesToDelete = []; // 삭제할 이미지 목록 초기화

    document.getElementById('editPostTitle').value = title;
    document.getElementById('editPostContent').value = content;
    document.getElementById('editImageUpload').value = '';

    renderExistingImages();
    document.getElementById('editPostModal').style.display = 'flex';
}

// 기존 이미지 렌더링 함수
function renderExistingImages() {
    const existingImagesDiv = document.getElementById('existingImages');

    if (existingImages && existingImages.length > 0) {
        existingImagesDiv.innerHTML = `
            <label style="font-weight: 500; margin-bottom: 8px; display: block;">기존 이미지:</label>
            <div style="display: flex; gap: 8px; flex-wrap: wrap;">
                ${existingImages.map((img, index) => `
                    <div style="position: relative; display: inline-block;">
                        <img src="${baseUrl}/images/${img.imageUrl}" 
                             style="width: 80px; height: 80px; object-fit: cover; border-radius: 6px;">
                        <button type="button" onclick="removeExistingImage(${index})" 
                                style="position: absolute; top: -5px; right: -5px; width: 20px; height: 20px; 
                                       background: #ef4444; color: white; border: none; border-radius: 50%; 
                                       cursor: pointer; font-size: 12px; display: flex; align-items: center; justify-content: center;">×</button>
                    </div>
                `).join('')}
            </div>
            ${imagesToDelete.length > 0 ? `
                <div style="margin-top: 8px; padding: 8px; background: #fef2f2; border-radius: 6px; font-size: 12px; color: #dc2626;">
                    삭제 예정: ${imagesToDelete.length}개 이미지
                </div>
            ` : ''}
        `;
    } else {
        existingImagesDiv.innerHTML = '';
    }
}


// 누락된 함수 추가
async function checkAuthorFollowStatus(authorId) {
    if (!authorId || authorId == userId) return;

    try {
        console.log(`👥 ${authorId} 팔로우 상태 확인 중...`);

        const res = await fetch(`${baseUrl}/api/users/follows/${authorId}/isFollow`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.ok) {
            const data = await res.json();
            console.log(`👥 ${authorId} 팔로우 상태:`, data.follow);

            // 해당 authorId를 가진 모든 버튼 업데이트
            const allFollowButtons = document.querySelectorAll(`[data-author-id="${authorId}"]`);

            allFollowButtons.forEach(button => {
                if (data.follow) {
                    button.textContent = '팔로잉';
                    button.classList.add('following');
                    button.style.background = "linear-gradient(135deg, #3181fa, #dc2626)";
                } else {
                    button.textContent = '팔로우';
                    button.classList.remove('following');
                    button.style.background = "linear-gradient(135deg, #3b82f6, #2777fa)";
                }
            });
        }
    } catch (err) {
        console.error(`❌ ${authorId} 팔로우 상태 확인 실패:`, err);
    }
}

// 수정된 closeEditPostModal 함수
function closeEditPostModal() {
    document.getElementById('editPostModal').style.display = 'none';
    editingPostId = null;
    existingImages = [];
    imagesToDelete = [];
}

// 수정된 updatePost 함수
async function updatePost() {
    const title = document.getElementById('editPostTitle').value.trim();
    const content = document.getElementById('editPostContent').value.trim();
    const imageFiles = Array.from(document.getElementById("editImageUpload").files);

    if (!title || !content) {
        alert('제목과 내용을 입력하세요.');
        return;
    }

    try {
        // 1. 새 이미지 업로드
        let newImageIds = [];
        if (imageFiles.length > 0) {
            for (const file of imageFiles) {
                const formData = new FormData();
                formData.append("file", file);

                const res = await fetch(`${baseUrl}/api/image/upload`, {
                    method: "POST",
                    headers: { "Authorization": `Bearer ${token}` },
                    body: formData
                });

                const data = await res.json();
                newImageIds.push(data.imageId);
            }
        }

        // 2. 삭제할 이미지가 있으면 먼저 삭제
        if (imagesToDelete.length > 0) {
            for (const imageId of imagesToDelete) {
                try {
                    await fetch(`${baseUrl}/api/images/${imageId}`, {
                        method: 'DELETE',
                        headers: { "Authorization": `Bearer ${token}` }
                    });
                    console.log(`🗑️ 이미지 ${imageId} 삭제 완료`);
                } catch (err) {
                    console.error(`❌ 이미지 ${imageId} 삭제 실패:`, err);
                }
            }
        }

        // 3. 게시물 업데이트
        const payload = {
            title,
            content,
            imageIds: [
                ...existingImages.map(img => img.imageId), // 유지할 기존 이미지
                ...newImageIds // 새로 추가할 이미지
            ]
        };

        console.log("📤 업데이트 payload:", payload);

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
            await loadPosts(0, false); // 게시물 목록 새로고침
        } else {
            const errorText = await res.text();
            throw new Error(`수정 실패: ${res.status} - ${errorText}`);
        }

    } catch (err) {
        console.error('❌ 게시물 수정 실패:', err);
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

// 좋아요 토글 함수 추가
async function toggleLike(postId, likeElement) {
    try {
        const countSpan = likeElement.querySelector('span');
        const currentCount = parseInt(countSpan.textContent) || 0;
        const isLiked = likeElement.classList.contains('liked');

        if (isLiked) {
            // 좋아요 취소
            const res = await fetch(`${baseUrl}/api/posts/${postId}/likes`, {
                method: 'DELETE',
                headers: { "Authorization": `Bearer ${token}` }
            });

            if (res.ok) {
                likeElement.classList.remove('liked');
                countSpan.textContent = currentCount - 1;
                likeElement.style.color = '#6b7280';
            }
        } else {
            // 좋아요 추가
            const res = await fetch(`${baseUrl}/api/posts/${postId}/likes`, {
                method: 'POST',
                headers: { "Authorization": `Bearer ${token}` }
            });

            if (res.ok) {
                likeElement.classList.add('liked');
                countSpan.textContent = currentCount + 1;
                likeElement.style.color = '#ef4444';
            }
        }
    } catch (err) {
        console.error('좋아요 처리 실패:', err);
        alert('좋아요 처리 중 오류가 발생했습니다.');
    }
}

// 좋아요 수 불러오기 함수 추가
async function loadLikeCount(postId) {
    try {
        const res = await fetch(`${baseUrl}/api/posts/${postId}/likes/count`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.ok) {
            const count = await res.json();
            const countEl = document.querySelector(`#like-count-${postId}`);
            if (countEl) {
                countEl.textContent = count;
            }
        }
    } catch (err) {
        console.error(`좋아요 수 로딩 실패 (${postId}):`, err);
    }
}


// 사용자의 좋아요 상태 확인 함수
async function loadUserLikeStatus(postId) {
    try {
        const res = await fetch(`${baseUrl}/api/posts/${postId}/likes`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.ok) {
            const likeUserList = await res.json(); // LikeResponseDto 배열
            const likeElement = document.querySelector(`#like-count-${postId}`).parentElement;

            // 현재 사용자가 좋아요 누른 사람 목록에 있는지 확인
            const userLiked = likeUserList.some(like => like.userId == userId);

            if (userLiked) {
                likeElement.classList.add('liked');
                likeElement.style.color = '#ef4444'; // 빨간색으로 표시
            } else {
                likeElement.classList.remove('liked');
                likeElement.style.color = '#6b7280'; // 회색으로 표시
            }
        }
    } catch (err) {
        console.error(`좋아요 상태 로딩 실패 (${postId}):`, err);
    }
}

// 메인 메뉴 토글
function toggleMainMenu() {
    const menu = document.getElementById('main-menu');
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
}

// 서브메뉴
function toggleSubmenu(submenuId) {
    const display = document.getElementById(submenuId);
    display.style.display = display.style.display === 'block' ? 'none' : 'block';
}

// 서브메뉴 표시
function showSubmenu(submenuId) {
    document.getElementById(submenuId).style.display = 'block';
}

// 서브메뉴 숨기기
function hideSubmenu(submenuId) {
    document.getElementById(submenuId).style.display = 'none';
}

// 테마 초기화 함수
function initializeTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.body.className = savedTheme === 'dark' ? 'dark-theme' : 'light-theme';
}

// 테마 설정 (수정된 버전)
function setTheme(theme) {
    document.body.className = theme === 'dark' ? 'dark-theme' : 'light-theme';
    localStorage.setItem('theme', theme);
    toggleMainMenu();
    showThemeNotification(`${theme === 'dark' ? '다크' : '라이트'} 테마로 변경되었습니다.`);
}

// 테마 알림 함수
function showThemeNotification(message) {
    const existingNotification = document.querySelector('.theme-notification');
    if (existingNotification) {
        existingNotification.remove();
    }

    const notification = document.createElement('div');
    notification.className = 'theme-notification';
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: var(--notification-bg, #10b981);
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 1000;
        font-weight: 500;
        transform: translateX(100%);
        transition: transform 0.3s ease;
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);

    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 피드 타입 설정
function setFeedType(type) {
    // 기존 setFilter 함수 활용
    setFilter(type);
    toggleMainMenu(); // 메뉴 닫기
}

// 로그아웃 함수 수정
function logout() {
    if (confirm('로그아웃 하시겠습니까?')) {
        // 모든 localStorage 데이터 제거
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        localStorage.clear(); // 모든 로컬 스토리지 정리

        // 페이지 캐시도 정리
        if ('caches' in window) {
            caches.keys().then(names => {
                names.forEach(name => {
                    caches.delete(name);
                });
            });
        }

        // 강제 새로고침으로 페이지 이동
        window.location.replace('/login.html');
    }
}


// 작성 패널 토글
function toggleWritePanel() {
    const panel = document.getElementById('write-panel');
    panel.style.display = panel.style.display === 'flex' ? 'none' : 'flex';
}

// 작성 패널 닫기
function closeWritePanel() {
    document.getElementById('write-panel').style.display = 'none';
    // 입력 필드 초기화
    document.getElementById('quick-title').value = '';
    document.getElementById('quick-content').value = '';
    document.getElementById('quick-image').value = '';
}

// 빠른 게시물 작성
async function submitQuickPost() {
    const title = document.getElementById('quick-title').value.trim();
    const content = document.getElementById('quick-content').value.trim();
    // 동일하게 수정
    const imageFiles = Array.from(document.getElementById("quick-image").files);

    if (!title || !content) {
        alert('제목과 내용을 입력하세요.');
        return;
    }

    let imageIds = [];
    if (imageFiles.length > 0) {
        for (const file of imageFiles) {
            const formData = new FormData();
            formData.append("file", file);

            try {
                const res = await fetch(`${baseUrl}/api/image/upload`, {
                    method: "POST",
                    headers: {"Authorization": `Bearer ${token}`},
                    body: formData
                });

                const data = await res.json();
                imageIds.push(data.imageId);
            } catch (err) {
                alert("이미지 업로드 실패: " + err.message);
                return;
            }
        }
    }

    // 수정:
    const payload = {
        title,
        content,
        imageIds: imageIds
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
    } catch (err) {
        console.error("❌ 게시 실패:", err);
        alert("게시 실패: " + err.message);
    }

    closeWritePanel();
    await loadPosts();

}

// document.addEventListener를 별도로 분리
document.addEventListener('click', function(e) {
    // 햄버거 메뉴 닫기
    if (!e.target.closest('.hamburger-menu') && !e.target.closest('.menu-options')) {
        const mainMenu = document.getElementById('main-menu');
        if (mainMenu) mainMenu.style.display = 'none';
    }

    // 게시물 메뉴 닫기
    if (!e.target.closest('.post-menu')) {
        document.querySelectorAll('.menu-dropdown').forEach(m => m.style.display = 'none');
    }

    // 댓글 메뉴 닫기 - 새로 추가!
    if (!e.target.closest('.comment-menu')) {
        document.querySelectorAll('.comment-dropdown').forEach(m => m.style.display = 'none');
    }

    // 서브메뉴 외부 클릭 시 닫기
    if (!e.target.closest('.has-submenu') && !e.target.closest('.submenu')) {
        document.querySelectorAll('.submenu').forEach(submenu => {
            submenu.style.display = 'none';
        });
    }

    // 이미지 모달 배경 클릭 시 닫기
    if (e.target.id === 'imageModal') {
        closeImageModal();
    }

    // 모달 배경 클릭 시 닫기
    if (e.target.id === 'overlay') {
        closeModal();
    }

    // 수정 모달 배경 클릭 시 닫기
    if (e.target.id === 'editPostModal') {
        closeEditPostModal();
    }
});

// 필터 설정 함수 추가
function setFilter(filterType) {
    // 현재는 간단하게 alert만, 나중에 실제 필터링 로직 구현
    console.log(`${filterType} 필터 적용됨`);
    // 실제 필터링 로직 추가 예정
    loadPosts(); // 게시물 다시 로드
}

// 필터 드롭다운 토글
function toggleFilterDropdown() {
    const options = document.getElementById('filter-options');
    options.style.display = options.style.display === 'block' ? 'none' : 'block';
}

// 수정된 removeExistingImage 함수
function removeExistingImage(index) {
    if (confirm('이 이미지를 삭제하시겠습니까?')) {
        const removedImage = existingImages[index];

        // 삭제할 이미지 목록에 추가
        if (removedImage.imageId) {
            imagesToDelete.push(removedImage.imageId);
        }

        // 기존 이미지 목록에서 제거
        existingImages.splice(index, 1);

        // 화면 다시 렌더링
        renderExistingImages();

        console.log("🗑️ 삭제 예정 이미지:", imagesToDelete);
        console.log("🖼️ 남은 기존 이미지:", existingImages);
    }
}

// 댓글 메뉴 토글 함수 추가
function toggleCommentMenu(commentId) {
    const menu = document.getElementById(`comment-menu-${commentId}`);
    const isVisible = menu.style.display === 'block';

    // 다른 모든 댓글 메뉴 닫기
    document.querySelectorAll('.comment-dropdown').forEach(m => m.style.display = 'none');

    // 현재 메뉴 토글
    menu.style.display = isVisible ? 'none' : 'block';
}

// 댓글 메뉴 숨기기 함수 추가
function hideCommentMenu(commentId) {
    document.getElementById(`comment-menu-${commentId}`).style.display = 'none';
}

// 댓글 수정 모드로 전환 함수 추가
function editComment(commentId, currentContent) {
    // 기존 텍스트 숨기기
    document.getElementById(`comment-text-${commentId}`).style.display = 'none';
    // 수정 폼 보이기
    document.getElementById(`comment-edit-${commentId}`).style.display = 'block';
    // 텍스트 에어리어에 포커스
    document.getElementById(`comment-edit-input-${commentId}`).focus();
}

// 댓글 수정 취소 함수 추가
function cancelEditComment(commentId) {
    // 수정 폼 숨기기
    document.getElementById(`comment-edit-${commentId}`).style.display = 'none';
    // 기존 텍스트 보이기
    document.getElementById(`comment-text-${commentId}`).style.display = 'block';
}

// 댓글 수정 저장 함수 추가
async function saveEditComment(commentId, postId) {
    const newContent = document.getElementById(`comment-edit-input-${commentId}`).value.trim();

    if (!newContent) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    try {
        const res = await fetch(`${baseUrl}/api/comments/${commentId}`, {
            method: 'PUT',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ content: newContent })
        });

        if (res.ok) {
            // 수정된 내용으로 업데이트
            document.getElementById(`comment-text-${commentId}`).textContent = newContent;
            // 수정 모드 종료
            cancelEditComment(commentId);
            alert('댓글이 수정되었습니다.');
        } else {
            throw new Error(`HTTP ${res.status}`);
        }
    } catch (err) {
        console.error('댓글 수정 실패:', err);
        alert('댓글 수정 중 오류가 발생했습니다: ' + err.message);
    }
}

// 댓글 삭제 함수 추가
async function deleteComment(commentId, postId) {
    if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) return;

    try {
        const res = await fetch(`${baseUrl}/api/comments/${commentId}`, {
            method: 'DELETE',
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (res.ok) {
            alert('댓글이 삭제되었습니다.');
            // 댓글 목록 새로고침
            loadComments(postId);
        } else {
            throw new Error(`HTTP ${res.status}`);
        }
    } catch (err) {
        console.error('댓글 삭제 실패:', err);
        alert('댓글 삭제 중 오류가 발생했습니다: ' + err.message);
    }
}

// 드롭존 초기화 함수
function initializeDropZones() {
    setupDropZone('dropZone', 'imageUpload');
    setupDropZone('editDropZone', 'editImageUpload');
    setupDropZone('quickDropZone', 'quick-image');
}

// 개별 드롭존 설정
function setupDropZone(dropZoneId, inputId) {
    const dropZone = document.getElementById(dropZoneId);
    const input = document.getElementById(inputId);

    if (!dropZone || !input) return;

    // 클릭 시 파일 선택
    dropZone.onclick = () => input.click();

    // 드래그 이벤트들
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, preventDefaults, false);
    });

    ['dragenter', 'dragover'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => dropZone.classList.add('drag-over'), false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => dropZone.classList.remove('drag-over'), false);
    });

    dropZone.addEventListener('drop', (e) => handleDrop(e, input), false);
}

// 기본 이벤트 방지
function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

// 드롭 처리
function handleDrop(e, input) {
    const dt = e.dataTransfer;
    const files = dt.files;
    input.files = files;
}

// 붙여넣기 이벤트 (전역)
document.addEventListener('paste', handlePaste);

function handlePaste(e) {
    const items = e.clipboardData.items;
    for (let item of items) {
        if (item.type.indexOf('image') !== -1) {
            const file = item.getAsFile();
            // 현재 열린 모달에 따라 처리
            if (document.getElementById('overlay').style.display === 'flex') {
                addFileToInput(file, 'imageUpload');
            } else if (document.getElementById('editPostModal').style.display === 'flex') {
                addFileToInput(file, 'editImageUpload');
            } else if (document.getElementById('write-panel').style.display === 'flex') {
                addFileToInput(file, 'quick-image');
            }
        }
    }
}

// 파일을 input에 추가
function addFileToInput(file, inputId) {
    const input = document.getElementById(inputId);
    const dt = new DataTransfer();
    dt.items.add(file);
    input.files = dt.files;
}

// formatDate 함수가 loadComments 내부가 아닌 전역에 있어야 하므로 확인하고 없으면 추가
if (typeof formatDate === 'undefined') {
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
}