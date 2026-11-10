import React, { useState, useEffect } from 'react';
import { X, Star, ThumbsUp, Trash2, Calendar, Clock, Film } from 'lucide-react';
import { Movie, Review, User } from '../types';
import { apiClient } from '../api/client';

interface MovieDetailModalProps {
  movie: Movie;
  currentUser: User | null;
  onClose: () => void;
  onRequireLogin: () => void;
}

export const MovieDetailModal: React.FC<MovieDetailModalProps> = ({
  movie,
  currentUser,
  onClose,
  onRequireLogin,
}) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [rating, setRating] = useState<number>(5.0);
  const [hoverRating, setHoverRating] = useState<number | null>(null);
  const [reviewContent, setReviewContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loadingReviews, setLoadingReviews] = useState(true);

  const fetchReviews = async () => {
    try {
      setLoadingReviews(true);
      const res = await apiClient.get(`/movies/${movie.id}/reviews?size=20`);
      if (res.data?.success) {
        setReviews(res.data.data.content);
      }
    } catch (e) {
      console.error('리뷰 불러오기 실패:', e);
    } finally {
      setLoadingReviews(false);
    }
  };

  useEffect(() => {
    fetchReviews();
  }, [movie.id]);

  const handleCreateReview = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) {
      onRequireLogin();
      return;
    }
    if (!reviewContent.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }

    try {
      setIsSubmitting(true);
      const res = await apiClient.post(`/movies/${movie.id}/reviews`, {
        rating,
        content: reviewContent.trim(),
      });
      if (res.data?.success) {
        setReviewContent('');
        fetchReviews();
      }
    } catch (err: any) {
      alert(err.response?.data?.message || '리뷰 등록에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleToggleLike = async (reviewId: number) => {
    if (!currentUser) {
      onRequireLogin();
      return;
    }
    try {
      await apiClient.post(`/reviews/${reviewId}/like`);
      fetchReviews();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDeleteReview = async (reviewId: number) => {
    if (!window.confirm('리뷰를 삭제하시겠습니까?')) return;
    try {
      await apiClient.delete(`/reviews/${reviewId}`);
      fetchReviews();
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm overflow-y-auto">
      <div className="relative w-full max-w-3xl bg-[#14151b] border border-gray-800 rounded-2xl shadow-2xl overflow-hidden my-8 max-h-[90vh] flex flex-col">
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 z-10 p-2 rounded-full bg-black/60 text-gray-300 hover:text-white hover:bg-black/90 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Backdrop Banner */}
        <div className="relative h-48 sm:h-64 w-full flex-shrink-0 overflow-hidden bg-gray-900">
          <img
            src={movie.backdropPath || movie.posterPath || 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200'}
            alt={movie.title}
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-[#14151b] via-[#14151b]/50 to-transparent" />
        </div>

        {/* Modal Body */}
        <div className="p-6 sm:p-8 -mt-16 relative overflow-y-auto space-y-6">
          <div className="flex gap-6 items-start">
            {/* Poster thumbnail */}
            <img
              src={movie.posterPath || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500'}
              alt={movie.title}
              className="w-28 sm:w-36 aspect-[2/3] object-cover rounded-xl border-2 border-gray-700 shadow-xl flex-shrink-0"
            />
            {/* Movie Header Info */}
            <div className="flex-1 min-w-0 pt-2">
              <span className="text-xs font-semibold text-red-500 bg-red-950/60 border border-red-900/50 px-2 py-0.5 rounded">
                {movie.genres}
              </span>
              <h2 className="text-2xl sm:text-3xl font-extrabold text-white mt-1.5 truncate">
                {movie.title}
              </h2>
              {movie.originalTitle && (
                <p className="text-xs text-gray-400 italic mb-2">
                  {movie.originalTitle}
                </p>
              )}

              <div className="flex flex-wrap items-center gap-3 text-xs text-gray-300 mt-2">
                <div className="flex items-center gap-1 text-[#f5c518] font-bold text-sm">
                  <Star className="w-4 h-4 fill-[#f5c518]" />
                  <span>{movie.ratingAvg > 0 ? movie.ratingAvg.toFixed(1) : '평가 전'}</span>
                </div>
                {movie.releaseDate && (
                  <span className="flex items-center gap-1 text-gray-400">
                    <Calendar className="w-3.5 h-3.5" />
                    {movie.releaseDate}
                  </span>
                )}
                {movie.runtime && movie.runtime > 0 && (
                  <span className="flex items-center gap-1 text-gray-400">
                    <Clock className="w-3.5 h-3.5" />
                    {movie.runtime}분
                  </span>
                )}
              </div>

              {movie.director && (
                <p className="text-xs text-gray-400 mt-2">
                  <span className="text-gray-300 font-medium">감독:</span> {movie.director}
                </p>
              )}
              {movie.actors && (
                <p className="text-xs text-gray-400 mt-0.5 truncate">
                  <span className="text-gray-300 font-medium">출연:</span> {movie.actors}
                </p>
              )}
            </div>
          </div>

          {/* Overview */}
          <div>
            <h3 className="text-sm font-bold text-gray-200 mb-1.5">줄거리</h3>
            <p className="text-xs sm:text-sm text-gray-300 leading-relaxed bg-gray-900/40 p-4 rounded-xl border border-gray-800">
              {movie.overview || '상세 줄거리 정보가 등록되어 있지 않습니다.'}
            </p>
          </div>

          {/* Review Write Form */}
          <div className="bg-gray-900/70 p-5 rounded-xl border border-gray-800">
            <h3 className="text-sm font-bold text-gray-100 mb-3 flex items-center gap-1.5">
              <Film className="w-4 h-4 text-red-500" />
              이 영화를 평가해주세요
            </h3>

            {/* Interactive Star Rating */}
            <div className="flex items-center gap-2 mb-3">
              <span className="text-xs text-gray-400">별점:</span>
              <div className="flex items-center gap-1">
                {[1, 2, 3, 4, 5].map((starValue) => {
                  const currentDisplay = hoverRating !== null ? hoverRating : rating;
                  const isFilled = currentDisplay >= starValue;
                  return (
                    <button
                      type="button"
                      key={starValue}
                      onMouseEnter={() => setHoverRating(starValue)}
                      onMouseLeave={() => setHoverRating(null)}
                      onClick={() => setRating(starValue)}
                      className="p-1 focus:outline-none transition-transform hover:scale-125"
                    >
                      <Star
                        className={`w-6 h-6 ${
                          isFilled
                            ? 'text-[#f5c518] fill-[#f5c518]'
                            : 'text-gray-600'
                        }`}
                      />
                    </button>
                  );
                })}
              </div>
              <span className="text-sm font-bold text-[#f5c518] ml-2">
                {(hoverRating !== null ? hoverRating : rating).toFixed(1)}점
              </span>
            </div>

            <form onSubmit={handleCreateReview} className="space-y-3">
              <textarea
                value={reviewContent}
                onChange={(e) => setReviewContent(e.target.value)}
                placeholder={
                  currentUser
                    ? '이 영화에 대한 감상평을 솔직하게 남겨주세요 (최대 1000자)'
                    : '리뷰를 작성하려면 로그인이 필요합니다.'
                }
                disabled={!currentUser || isSubmitting}
                rows={3}
                className="w-full bg-[#181920] border border-gray-700 rounded-lg p-3 text-sm text-gray-200 placeholder-gray-500 focus:outline-none focus:border-red-500 disabled:opacity-60 resize-none"
              />
              <div className="flex justify-end">
                <button
                  type="submit"
                  disabled={!currentUser || isSubmitting}
                  className="bg-[#e50914] hover:bg-red-700 disabled:opacity-50 text-white text-xs font-semibold px-4 py-2 rounded-lg transition-colors"
                >
                  {isSubmitting ? '등록 중...' : '리뷰 등록하기'}
                </button>
              </div>
            </form>
          </div>

          {/* Review List */}
          <div>
            <h3 className="text-sm font-bold text-gray-200 mb-3 flex items-center justify-between">
              <span>관객 리뷰 ({reviews.length})</span>
            </h3>

            {loadingReviews ? (
              <p className="text-center text-xs text-gray-500 py-6">리뷰를 불러오는 중입니다...</p>
            ) : reviews.length === 0 ? (
              <p className="text-center text-xs text-gray-500 py-6">첫 번째 리뷰의 주인공이 되어보세요!</p>
            ) : (
              <div className="space-y-3">
                {reviews.map((r) => (
                  <div
                    key={r.id}
                    className="p-3.5 bg-gray-900/40 border border-gray-800/80 rounded-xl flex flex-col gap-2"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <img
                          src={r.user.profileImageUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100'}
                          alt={r.user.nickname}
                          className="w-6 h-6 rounded-full object-cover"
                        />
                        <span className="text-xs font-semibold text-gray-200">
                          {r.user.nickname}
                        </span>
                        <div className="flex items-center text-[#f5c518] text-xs font-bold gap-0.5 ml-1">
                          <Star className="w-3 h-3 fill-[#f5c518]" />
                          <span>{r.rating.toFixed(1)}</span>
                        </div>
                      </div>

                      <div className="flex items-center gap-2">
                        <span className="text-[10px] text-gray-500">
                          {new Date(r.createdAt).toLocaleDateString()}
                        </span>
                        {(currentUser?.id === r.user.id || currentUser?.role === 'ADMIN') && (
                          <button
                            onClick={() => handleDeleteReview(r.id)}
                            className="text-gray-500 hover:text-red-400 p-1"
                            title="삭제"
                          >
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        )}
                      </div>
                    </div>

                    <p className="text-xs text-gray-300 leading-relaxed break-all">
                      {r.content}
                    </p>

                    <div className="flex justify-end items-center">
                      <button
                        onClick={() => handleToggleLike(r.id)}
                        className={`flex items-center gap-1 text-[11px] px-2 py-1 rounded transition-colors ${
                          r.isLiked
                            ? 'text-red-400 bg-red-950/40 border border-red-900/40'
                            : 'text-gray-400 hover:text-white bg-gray-800/50'
                        }`}
                      >
                        <ThumbsUp className="w-3 h-3" />
                        <span>{r.likesCount}</span>
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
