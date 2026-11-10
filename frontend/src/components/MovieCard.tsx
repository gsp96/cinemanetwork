import React from 'react';
import { Star } from 'lucide-react';
import { Movie } from '../types';

interface MovieCardProps {
  movie: Movie;
  rank?: number;
  audiAcc?: number;
  onClick: () => void;
}

export const MovieCard: React.FC<MovieCardProps> = ({ movie, rank, audiAcc, onClick }) => {
  return (
    <div
      onClick={onClick}
      className="group relative flex-shrink-0 w-44 sm:w-52 cursor-pointer transition-all duration-300 hover:-translate-y-1.5"
    >
      {/* Poster Image Container */}
      <div className="relative aspect-[2/3] w-full rounded-xl overflow-hidden bg-gray-900 border border-gray-800 shadow-lg group-hover:border-gray-600 group-hover:shadow-red-950/20">
        <img
          src={movie.posterPath || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500'}
          alt={movie.title}
          loading="lazy"
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/20 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end p-3">
          <span className="text-xs text-white font-medium bg-[#e50914] px-2.5 py-1 rounded-md shadow">
            상세보기
          </span>
        </div>

        {/* Rank Badge */}
        {rank !== undefined && (
          <div className="absolute top-2 left-2 w-8 h-8 rounded-lg bg-black/75 backdrop-blur-md border border-white/20 flex items-center justify-center font-black text-base text-white shadow-md">
            {rank}
          </div>
        )}
      </div>

      {/* Info */}
      <div className="mt-2.5 px-0.5">
        <h3 className="font-semibold text-sm text-gray-100 truncate group-hover:text-red-400 transition-colors">
          {movie.title}
        </h3>
        
        <div className="flex items-center justify-between text-xs text-gray-400 mt-1">
          <div className="flex items-center gap-1 text-[#f5c518] font-bold">
            <Star className="w-3.5 h-3.5 fill-[#f5c518]" />
            <span>{movie.ratingAvg > 0 ? movie.ratingAvg.toFixed(1) : '-'}</span>
          </div>
          {audiAcc ? (
            <span className="text-[11px] text-gray-400">
              {(audiAcc / 10000).toFixed(0)}만 관객
            </span>
          ) : (
            <span className="text-[11px] text-gray-500 truncate max-w-[80px]">
              {movie.genres?.split(',')[0] || '영화'}
            </span>
          )}
        </div>
      </div>
    </div>
  );
};
