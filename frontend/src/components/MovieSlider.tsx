import React, { useRef } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { Movie } from '../types';
import { MovieCard } from './MovieCard';

interface MovieSliderProps {
  title: string;
  subtitle?: string;
  movies: { movie: Movie; rank?: number; audiAcc?: number }[];
  onSelectMovie: (movie: Movie) => void;
}

export const MovieSlider: React.FC<MovieSliderProps> = ({
  title,
  subtitle,
  movies,
  onSelectMovie,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);

  const handleScroll = (direction: 'left' | 'right') => {
    if (scrollRef.current) {
      const offset = direction === 'left' ? -600 : 600;
      scrollRef.current.scrollBy({ left: offset, behavior: 'smooth' });
    }
  };

  if (!movies || movies.length === 0) return null;

  return (
    <div className="mb-10 relative group">
      {/* Header */}
      <div className="flex items-end justify-between mb-4">
        <div>
          <h2 className="text-xl sm:text-2xl font-bold text-white tracking-tight">
            {title}
          </h2>
          {subtitle && (
            <p className="text-xs sm:text-sm text-gray-400 mt-1">{subtitle}</p>
          )}
        </div>

        {/* Scroll Controls */}
        <div className="flex items-center gap-2">
          <button
            onClick={() => handleScroll('left')}
            className="p-1.5 rounded-full bg-gray-800/80 hover:bg-gray-700 text-gray-300 hover:text-white transition-all shadow"
            title="이전"
          >
            <ChevronLeft className="w-5 h-5" />
          </button>
          <button
            onClick={() => handleScroll('right')}
            className="p-1.5 rounded-full bg-gray-800/80 hover:bg-gray-700 text-gray-300 hover:text-white transition-all shadow"
            title="다음"
          >
            <ChevronRight className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* Horizontal List */}
      <div
        ref={scrollRef}
        className="flex gap-4 overflow-x-auto pb-4 scroll-smooth scrollbar-none"
        style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
      >
        {movies.map((item) => (
          <MovieCard
            key={item.movie.id}
            movie={item.movie}
            rank={item.rank}
            audiAcc={item.audiAcc}
            onClick={() => onSelectMovie(item.movie)}
          />
        ))}
      </div>
    </div>
  );
};
