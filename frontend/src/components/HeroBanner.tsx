import React from 'react';
import { Star, Calendar, Clock, Info } from 'lucide-react';
import { Movie } from '../types';

interface HeroBannerProps {
  movie: Movie | null;
  onSelectMovie: (movie: Movie) => void;
}

export const HeroBanner: React.FC<HeroBannerProps> = ({ movie, onSelectMovie }) => {
  if (!movie) return null;

  return (
    <div className="relative w-full h-[480px] lg:h-[540px] overflow-hidden rounded-2xl mb-8 group">
      {/* Background Image with Gradient Overlay */}
      <img
        src={movie.backdropPath || movie.posterPath || 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200'}
        alt={movie.title}
        className="absolute inset-0 w-full h-full object-cover object-center scale-100 group-hover:scale-105 transition-transform duration-700 ease-out"
      />
      <div className="absolute inset-0 bg-gradient-to-t from-[#0f1015] via-[#0f1015]/60 to-transparent" />
      <div className="absolute inset-0 bg-gradient-to-r from-[#0f1015] via-[#0f1015]/80 to-transparent" />

      {/* Hero Content */}
      <div className="absolute bottom-0 left-0 p-8 sm:p-12 max-w-2xl">
        <div className="flex items-center gap-2 mb-3">
          <span className="px-2.5 py-1 text-xs font-bold uppercase tracking-wider bg-[#e50914] text-white rounded-md shadow-md">
            박스오피스 1위 HOT
          </span>
          <span className="text-xs text-gray-300 bg-gray-900/80 px-2 py-1 rounded border border-gray-700">
            {movie.genres}
          </span>
        </div>

        <h1 className="text-3xl sm:text-5xl font-extrabold text-white tracking-tight mb-3 drop-shadow-md">
          {movie.title}
        </h1>

        <div className="flex items-center gap-4 text-sm text-gray-300 mb-4">
          <div className="flex items-center gap-1 text-[#f5c518] font-bold">
            <Star className="w-4 h-4 fill-[#f5c518]" />
            <span>{movie.ratingAvg > 0 ? movie.ratingAvg.toFixed(1) : '평가 전'}</span>
            <span className="text-xs text-gray-400 font-normal">({movie.reviewCount}명)</span>
          </div>
          {movie.releaseDate && (
            <div className="flex items-center gap-1 text-gray-400">
              <Calendar className="w-4 h-4" />
              <span>{movie.releaseDate} 개봉</span>
            </div>
          )}
          {movie.runtime && movie.runtime > 0 && (
            <div className="flex items-center gap-1 text-gray-400">
              <Clock className="w-4 h-4" />
              <span>{movie.runtime}분</span>
            </div>
          )}
        </div>

        <p className="text-gray-300 text-sm line-clamp-3 mb-6 leading-relaxed">
          {movie.overview || '상세 줄거리 준비 중입니다.'}
        </p>

        <div className="flex items-center gap-3">
          <button
            onClick={() => onSelectMovie(movie)}
            className="flex items-center gap-2 bg-[#e50914] hover:bg-red-700 text-white font-semibold px-6 py-3 rounded-xl shadow-lg shadow-red-900/30 transition-all hover:scale-105"
          >
            <Info className="w-5 h-5" />
            상세 정보 & 리뷰 보기
          </button>
        </div>
      </div>
    </div>
  );
};
