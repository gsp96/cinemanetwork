import React, { useState, useEffect } from 'react';
import { Navbar } from './components/Navbar';
import { HeroBanner } from './components/HeroBanner';
import { MovieSlider } from './components/MovieSlider';
import { MovieDetailModal } from './components/MovieDetailModal';
import { CommunitySection } from './components/CommunitySection';
import { AuthModal } from './components/AuthModal';
import { MovieCard } from './components/MovieCard';
import { apiClient } from './api/client';
import { BoxOffice, Movie, User } from './types';
import { Film, Award, TrendingUp, Filter } from 'lucide-react';

export const App: React.FC = () => {
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [activeTab, setActiveTab] = useState<'home' | 'movies' | 'community' | 'inquiry'>('home');
  const [boxOffices, setBoxOffices] = useState<BoxOffice[]>([]);
  const [topRated, setTopRated] = useState<Movie[]>([]);
  const [allMovies, setAllMovies] = useState<Movie[]>([]);
  const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);
  const [authModal, setAuthModal] = useState<'login' | 'signup' | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [genreFilter, setGenreFilter] = useState<string>('ALL');
  const [loading, setLoading] = useState(true);

  // Check login on mount
  useEffect(() => {
    const savedUser = localStorage.getItem('cinemanetwork_user');
    const token = localStorage.getItem('cinemanetwork_token');
    if (savedUser && token) {
      try {
        setCurrentUser(JSON.parse(savedUser));
        // Verify token with backend
        apiClient.get('/users/me')
          .then((res) => {
            if (res.data?.success) {
              setCurrentUser(res.data.data);
              localStorage.setItem('cinemanetwork_user', JSON.stringify(res.data.data));
            }
          })
          .catch(() => {
            handleLogout();
          });
      } catch (e) {
        handleLogout();
      }
    }
  }, []);

  // Fetch initial movies
  const fetchData = async () => {
    try {
      setLoading(true);
      const [boRes, topRes, moviesRes] = await Promise.all([
        apiClient.get('/movies/boxoffice'),
        apiClient.get('/movies/top-rated'),
        apiClient.get('/movies?size=24'),
      ]);

      if (boRes.data?.success) setBoxOffices(boRes.data.data);
      if (topRes.data?.success) setTopRated(topRes.data.data);
      if (moviesRes.data?.success) setAllMovies(moviesRes.data.data.content);
    } catch (e) {
      console.error('데이터 로딩 오류:', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleLoginSuccess = (user: User, token: string) => {
    setCurrentUser(user);
    localStorage.setItem('cinemanetwork_user', JSON.stringify(user));
    localStorage.setItem('cinemanetwork_token', token);
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('cinemanetwork_user');
    localStorage.removeItem('cinemanetwork_token');
  };

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    if (query) {
      setActiveTab('movies');
    }
  };

  // Filtered movies
  const filteredMovies = allMovies.filter((m) => {
    const matchQuery = !searchQuery || 
      m.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
      (m.director && m.director.toLowerCase().includes(searchQuery.toLowerCase()));
    const matchGenre = genreFilter === 'ALL' || (m.genres && m.genres.includes(genreFilter));
    return matchQuery && matchGenre;
  });

  const heroMovie = boxOffices.length > 0 ? boxOffices[0].movie : allMovies[0] || null;

  const genres = ['ALL', '액션', '드라마', '스릴러', '미스터리', '애니메이션', 'SF'];

  return (
    <div className="min-h-screen bg-[#0f1015] text-white flex flex-col">
      <Navbar
        currentUser={currentUser}
        onOpenAuth={(mode) => setAuthModal(mode)}
        onLogout={handleLogout}
        onSearch={handleSearch}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
      />

      <main className="flex-1 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 w-full">
        {/* TAB: HOME (BoxOffice & Top Rated) */}
        {activeTab === 'home' && (
          <div>
            <HeroBanner movie={heroMovie} onSelectMovie={setSelectedMovie} />

            {/* Box Office Slider */}
            <MovieSlider
              title="🔥 실시간 일일 박스오피스 TOP 10"
              subtitle="영화진흥위원회 KOBIS & TMDB 실시간 연동 집계"
              movies={boxOffices.map((bo) => ({
                movie: bo.movie,
                rank: bo.rank,
                audiAcc: bo.audiAcc,
              }))}
              onSelectMovie={setSelectedMovie}
            />

            {/* Top Rated Slider */}
            <MovieSlider
              title="⭐ 시네마네트워크 관객 최고 평점 명작"
              subtitle="실제 관객들의 별점과 리뷰로 검증된 추천작"
              movies={topRated.map((m) => ({ movie: m }))}
              onSelectMovie={setSelectedMovie}
            />
          </div>
        )}

        {/* TAB: MOVIES (Exploration & Search) */}
        {activeTab === 'movies' && (
          <div>
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6 border-b border-gray-800 pb-4">
              <div>
                <h2 className="text-2xl font-bold text-white">영화 탐색</h2>
                <p className="text-xs text-gray-400 mt-1">
                  {searchQuery ? `'${searchQuery}' 검색 결과 (${filteredMovies.length}건)` : '원하는 장르와 영화를 찾아보세요'}
                </p>
              </div>

              {/* Genre Filter Pills */}
              <div className="flex flex-wrap items-center gap-1.5">
                {genres.map((g) => (
                  <button
                    key={g}
                    onClick={() => setGenreFilter(g)}
                    className={`px-3 py-1 rounded-full text-xs font-medium transition-colors ${
                      genreFilter === g
                        ? 'bg-red-600 text-white font-semibold'
                        : 'bg-gray-800/80 text-gray-400 hover:text-white hover:bg-gray-700'
                    }`}
                  >
                    {g === 'ALL' ? '전체 장르' : g}
                  </button>
                ))}
              </div>
            </div>

            {filteredMovies.length === 0 ? (
              <div className="text-center py-20 bg-gray-900/30 rounded-2xl border border-gray-800">
                <Film className="w-12 h-12 text-gray-600 mx-auto mb-3" />
                <p className="text-sm text-gray-300">일치하는 영화를 찾을 수 없습니다.</p>
                <p className="text-xs text-gray-500 mt-1">다른 검색어 또는 장르를 선택해보세요.</p>
              </div>
            ) : (
              <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
                {filteredMovies.map((m) => (
                  <MovieCard
                    key={m.id}
                    movie={m}
                    onClick={() => setSelectedMovie(m)}
                  />
                ))}
              </div>
            )}
          </div>
        )}

        {/* TAB: COMMUNITY */}
        {activeTab === 'community' && (
          <CommunitySection
            currentUser={currentUser}
            onRequireLogin={() => setAuthModal('login')}
            defaultCategory="ALL"
          />
        )}

        {/* TAB: INQUIRY */}
        {activeTab === 'inquiry' && (
          <CommunitySection
            currentUser={currentUser}
            onRequireLogin={() => setAuthModal('login')}
            defaultCategory="INQUIRY"
          />
        )}
      </main>

      {/* Movie Detail Modal */}
      {selectedMovie && (
        <MovieDetailModal
          movie={selectedMovie}
          currentUser={currentUser}
          onClose={() => setSelectedMovie(null)}
          onRequireLogin={() => setAuthModal('login')}
        />
      )}

      {/* Auth Modal */}
      {authModal && (
        <AuthModal
          initialMode={authModal}
          onClose={() => setAuthModal(null)}
          onSuccess={handleLoginSuccess}
        />
      )}

      {/* Footer */}
      <footer className="bg-[#0b0c10] border-t border-gray-900 mt-16 py-8 text-xs text-gray-400">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center space-y-2">
          <div className="flex items-center justify-center gap-2 text-gray-300 font-semibold mb-2">
            <Film className="w-4 h-4 text-red-500" />
            <span>CinemaNetwork 플랫폼</span>
          </div>
          <p>
            영화진흥위원회 KOBIS Open API 및 The Movie Database (TMDB) API를 연동하여 실시간 박스오피스와 고해상도 영화 메타데이터를 제공합니다.
          </p>
          <p className="text-[11px] text-gray-400">
            개별 저작권 이미지의 경우 씨네마 네트워크(주)는 비상업적 연구/포트폴리오 목적으로 표시하며 권리는 원저작자에게 있습니다.
          </p>
          <p className="text-[11px] text-gray-400 pt-2 border-t border-gray-800">
            © 2026 CinemaNetwork. Modernized with Spring Boot 3 & React TypeScript.
          </p>
        </div>
      </footer>
    </div>
  );
};
