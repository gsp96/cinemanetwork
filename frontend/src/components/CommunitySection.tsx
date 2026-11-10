import React, { useState, useEffect } from 'react';
import { MessageSquare, Lock, Eye, MessageCircle, PlusCircle, Trash2, ShieldAlert } from 'lucide-react';
import { Post, PostCategory, User, Comment } from '../types';
import { apiClient } from '../api/client';

interface CommunitySectionProps {
  currentUser: User | null;
  onRequireLogin: () => void;
  defaultCategory?: PostCategory | 'ALL';
}

export const CommunitySection: React.FC<CommunitySectionProps> = ({
  currentUser,
  onRequireLogin,
  defaultCategory,
}) => {
  const [selectedCategory, setSelectedCategory] = useState<PostCategory | 'ALL'>(
    defaultCategory || 'ALL'
  );
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [activePost, setActivePost] = useState<Post | null>(null);
  const [showCreateModal, setShowCreateModal] = useState(false);

  // New Post Form
  const [newTitle, setNewTitle] = useState('');
  const [newContent, setNewContent] = useState('');
  const [newCategory, setNewCategory] = useState<PostCategory>('MOVIE_TALK');
  const [newIsSecret, setNewIsSecret] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Comment Form
  const [commentInput, setCommentInput] = useState('');
  const [isSubmittingComment, setIsSubmittingComment] = useState(false);

  const fetchPosts = async () => {
    try {
      setLoading(true);
      const categoryParam = selectedCategory !== 'ALL' ? `category=${selectedCategory}&` : '';
      const res = await apiClient.get(`/posts?${categoryParam}size=20`);
      if (res.data?.success) {
        setPosts(res.data.data.content);
      }
    } catch (e) {
      console.error('게시글 불러오기 실패:', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, [selectedCategory]);

  const handleOpenDetail = async (post: Post) => {
    try {
      const res = await apiClient.get(`/posts/${post.id}`);
      if (res.data?.success) {
        setActivePost(res.data.data);
      }
    } catch (err: any) {
      alert(err.response?.data?.message || '게시글을 열람할 수 없습니다.');
    }
  };

  const handleCreatePost = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) {
      onRequireLogin();
      return;
    }
    if (!newTitle.trim() || !newContent.trim()) {
      alert('제목과 내용을 모두 입력해주세요.');
      return;
    }

    try {
      setIsSubmitting(true);
      const res = await apiClient.post('/posts', {
        title: newTitle.trim(),
        content: newContent.trim(),
        category: newCategory,
        isSecret: newIsSecret,
      });

      if (res.data?.success) {
        setShowCreateModal(false);
        setNewTitle('');
        setNewContent('');
        setNewIsSecret(false);
        fetchPosts();
      }
    } catch (err: any) {
      alert(err.response?.data?.message || '게시글 등록에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeletePost = async (postId: number) => {
    if (!window.confirm('게시글을 삭제하시겠습니까?')) return;
    try {
      await apiClient.delete(`/posts/${postId}`);
      setActivePost(null);
      fetchPosts();
    } catch (e) {
      console.error(e);
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) {
      onRequireLogin();
      return;
    }
    if (!commentInput.trim() || !activePost) return;

    try {
      setIsSubmittingComment(true);
      const res = await apiClient.post(`/posts/${activePost.id}/comments`, {
        content: commentInput.trim(),
      });
      if (res.data?.success) {
        setCommentInput('');
        handleOpenDetail(activePost);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setIsSubmittingComment(false);
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (!window.confirm('댓글을 삭제하시겠습니까?')) return;
    if (!activePost) return;
    try {
      await apiClient.delete(`/posts/${activePost.id}/comments/${commentId}`);
      handleOpenDetail(activePost);
    } catch (e) {
      console.error(e);
    }
  };

  const getCategoryLabel = (cat: PostCategory) => {
    switch (cat) {
      case 'MOVIE_TALK': return '영화 수다';
      case 'FREE': return '자유 토론';
      case 'INQUIRY': return '1:1 문의';
      case 'NOTICE': return '공지사항';
      default: return cat;
    }
  };

  return (
    <div className="py-6 max-w-5xl mx-auto">
      {/* Category Tabs & Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6 border-b border-gray-800 pb-4">
        <div className="flex flex-wrap items-center gap-2">
          {(['ALL', 'MOVIE_TALK', 'FREE', 'INQUIRY', 'NOTICE'] as const).map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
                selectedCategory === cat
                  ? 'bg-red-600 text-white shadow-md'
                  : 'bg-gray-800/80 text-gray-400 hover:text-white hover:bg-gray-700'
              }`}
            >
              {cat === 'ALL' ? '전체' : getCategoryLabel(cat)}
            </button>
          ))}
        </div>

        <button
          onClick={() => {
            if (!currentUser) {
              onRequireLogin();
            } else {
              setNewCategory(selectedCategory !== 'ALL' ? selectedCategory : 'MOVIE_TALK');
              setShowCreateModal(true);
            }
          }}
          className="flex items-center gap-1.5 bg-[#e50914] hover:bg-red-700 text-white text-xs font-semibold px-4 py-2 rounded-lg shadow-md transition-colors self-start sm:self-auto"
        >
          <PlusCircle className="w-4 h-4" />
          글쓰기
        </button>
      </div>

      {/* Post List */}
      {loading ? (
        <p className="text-center py-12 text-xs text-gray-500">게시글 목록을 불러오는 중...</p>
      ) : posts.length === 0 ? (
        <div className="text-center py-16 bg-gray-900/30 rounded-2xl border border-gray-800">
          <MessageSquare className="w-10 h-10 text-gray-600 mx-auto mb-2" />
          <p className="text-sm text-gray-400">등록된 게시글이 없습니다.</p>
          <p className="text-xs text-gray-500 mt-1">첫 번째 글을 작성해보세요!</p>
        </div>
      ) : (
        <div className="space-y-2">
          {posts.map((post) => (
            <div
              key={post.id}
              onClick={() => handleOpenDetail(post)}
              className="p-4 bg-[#14151b] hover:bg-[#1a1b24] border border-gray-800/80 hover:border-gray-700 rounded-xl cursor-pointer transition-all flex items-center justify-between gap-4"
            >
              <div className="flex items-center gap-3 min-w-0">
                <span className="text-[11px] font-semibold text-gray-400 bg-gray-800 px-2 py-0.5 rounded flex-shrink-0">
                  {getCategoryLabel(post.category)}
                </span>
                <div className="flex items-center gap-1.5 min-w-0">
                  {post.isSecret && <Lock className="w-3.5 h-3.5 text-yellow-500 flex-shrink-0" />}
                  <h4 className="text-sm font-medium text-gray-200 truncate">
                    {post.title}
                  </h4>
                  {post.commentCount > 0 && (
                    <span className="text-xs font-bold text-red-400 flex-shrink-0">
                      [{post.commentCount}]
                    </span>
                  )}
                </div>
              </div>

              <div className="flex items-center gap-4 text-xs text-gray-400 flex-shrink-0">
                <span className="hidden sm:inline text-gray-300 font-medium">
                  {post.user.nickname}
                </span>
                <span className="flex items-center gap-1 text-[11px]">
                  <Eye className="w-3.5 h-3.5 text-gray-500" />
                  {post.viewCount}
                </span>
                <span className="text-[11px] text-gray-500">
                  {new Date(post.createdAt).toLocaleDateString()}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Post Detail Modal */}
      {activePost && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm overflow-y-auto">
          <div className="w-full max-w-2xl bg-[#14151b] border border-gray-800 rounded-2xl p-6 sm:p-8 max-h-[90vh] flex flex-col my-8">
            {/* Header */}
            <div className="flex items-start justify-between border-b border-gray-800 pb-4 mb-4">
              <div>
                <div className="flex items-center gap-2 mb-1.5">
                  <span className="text-xs font-semibold text-red-500 bg-red-950/60 px-2 py-0.5 rounded">
                    {getCategoryLabel(activePost.category)}
                  </span>
                  {activePost.isSecret && (
                    <span className="flex items-center gap-1 text-xs text-yellow-400 bg-yellow-950/40 px-2 py-0.5 rounded">
                      <Lock className="w-3 h-3" />
                      비밀글
                    </span>
                  )}
                </div>
                <h3 className="text-xl font-bold text-white">{activePost.title}</h3>
                <div className="flex items-center gap-3 text-xs text-gray-400 mt-2">
                  <span>작성자: <b className="text-gray-200">{activePost.user.nickname}</b></span>
                  <span>조회수: {activePost.viewCount}</span>
                  <span>{new Date(activePost.createdAt).toLocaleString()}</span>
                </div>
              </div>

              <div className="flex items-center gap-2">
                {(currentUser?.id === activePost.user.id || currentUser?.role === 'ADMIN') && (
                  <button
                    onClick={() => handleDeletePost(activePost.id)}
                    className="p-1.5 text-gray-400 hover:text-red-400 transition-colors"
                    title="게시글 삭제"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                )}
                <button
                  onClick={() => setActivePost(null)}
                  className="px-2 py-1 text-xs bg-gray-800 text-gray-300 rounded hover:bg-gray-700"
                >
                  닫기
                </button>
              </div>
            </div>

            {/* Content Body */}
            <div className="overflow-y-auto flex-1 space-y-6 pr-1">
              <div className="text-sm text-gray-200 whitespace-pre-wrap leading-relaxed min-h-[120px] bg-gray-900/40 p-4 rounded-xl border border-gray-800/80">
                {activePost.content}
              </div>

              {/* Comment Section */}
              <div className="border-t border-gray-800 pt-4">
                <h4 className="text-xs font-bold text-gray-300 mb-3 flex items-center gap-1.5">
                  <MessageCircle className="w-4 h-4 text-red-500" />
                  댓글 ({activePost.comments?.length || 0})
                </h4>

                {/* Comment list */}
                <div className="space-y-2 mb-4">
                  {activePost.comments?.map((comment) => (
                    <div
                      key={comment.id}
                      className="p-3 bg-gray-900/60 border border-gray-800 rounded-lg flex items-start justify-between gap-2"
                    >
                      <div>
                        <div className="flex items-center gap-2 mb-1">
                          <span className="text-xs font-semibold text-gray-200">
                            {comment.user.nickname}
                          </span>
                          <span className="text-[10px] text-gray-500">
                            {new Date(comment.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                          </span>
                        </div>
                        <p className="text-xs text-gray-300">{comment.content}</p>
                      </div>

                      {(currentUser?.id === comment.user.id || currentUser?.role === 'ADMIN') && (
                        <button
                          onClick={() => handleDeleteComment(comment.id)}
                          className="text-gray-500 hover:text-red-400 p-1"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      )}
                    </div>
                  ))}
                </div>

                {/* Add Comment Input */}
                <form onSubmit={handleAddComment} className="flex gap-2">
                  <input
                    type="text"
                    value={commentInput}
                    onChange={(e) => setCommentInput(e.target.value)}
                    placeholder={
                      currentUser ? '따뜻한 댓글을 남겨주세요...' : '댓글을 작성하려면 로그인하세요.'
                    }
                    disabled={!currentUser || isSubmittingComment}
                    className="flex-1 bg-gray-900 border border-gray-700 text-xs rounded-lg px-3 py-2 text-gray-200 focus:outline-none focus:border-red-500"
                  />
                  <button
                    type="submit"
                    disabled={!currentUser || isSubmittingComment}
                    className="bg-[#e50914] hover:bg-red-700 disabled:opacity-50 text-white text-xs font-semibold px-4 py-2 rounded-lg transition-colors"
                  >
                    등록
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Create Post Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-lg bg-[#14151b] border border-gray-800 rounded-2xl p-6 shadow-2xl">
            <h3 className="text-lg font-bold text-white mb-4">새 게시글 작성</h3>
            <form onSubmit={handleCreatePost} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-gray-300 mb-1">카테고리</label>
                <select
                  value={newCategory}
                  onChange={(e) => setNewCategory(e.target.value as PostCategory)}
                  className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500"
                >
                  <option value="MOVIE_TALK">영화 수다</option>
                  <option value="FREE">자유 토론</option>
                  <option value="INQUIRY">1:1 고객센터 문의</option>
                  {currentUser?.role === 'ADMIN' && <option value="NOTICE">공지사항</option>}
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-300 mb-1">제목</label>
                <input
                  type="text"
                  value={newTitle}
                  onChange={(e) => setNewTitle(e.target.value)}
                  placeholder="제목을 입력하세요"
                  className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-300 mb-1">내용</label>
                <textarea
                  value={newContent}
                  onChange={(e) => setNewContent(e.target.value)}
                  placeholder="내용을 작성하세요..."
                  rows={5}
                  className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500 resize-none"
                />
              </div>

              {newCategory === 'INQUIRY' && (
                <div className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    id="secretCheck"
                    checked={newIsSecret}
                    onChange={(e) => setNewIsSecret(e.target.checked)}
                    className="w-4 h-4 rounded text-red-600 focus:ring-red-500 bg-gray-900 border-gray-700"
                  />
                  <label htmlFor="secretCheck" className="text-xs text-gray-300 cursor-pointer">
                    비밀글로 등록 (관리자와 작성자만 확인 가능)
                  </label>
                </div>
              )}

              <div className="flex justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowCreateModal(false)}
                  className="px-4 py-2 bg-gray-800 text-gray-300 text-xs font-medium rounded-lg hover:bg-gray-700"
                >
                  취소
                </button>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="px-4 py-2 bg-[#e50914] text-white text-xs font-semibold rounded-lg hover:bg-red-700 disabled:opacity-50"
                >
                  {isSubmitting ? '등록 중...' : '등록하기'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
