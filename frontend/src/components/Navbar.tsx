import React, { useState } from 'react';
import { Film, Search, User as UserIcon, LogOut, MessageSquare, Shield } from 'lucide-react';
import { User } from '../types';

interface NavbarProps {
  currentUser: User | null;
  onOpenAuth: (mode: 'login' | 'signup') => void;
  onLogout: () => void;
  onSearch: (query: string) => void;
  activeTab: 'home' | 'movies' | 'community' | 'inquiry';
  setActiveTab: (tab: 'home' | 'movies' | 'community' | 'inquiry') => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  currentUser,
  onOpenAuth,
  onLogout,
  onSearch,
  activeTab,
  setActiveTab,
}) => {
  const [searchInput, setSearchInput] = useState('');

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(searchInput.trim());
  };

  return (
    <header className="sticky top-0 z-40 bg-[#0f1015]/90 backdrop-blur-md border-b border-gray-800 transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between gap-4">
        {/* Brand Logo */}
        <div 
          onClick={() => { setActiveTab('home'); onSearch(''); setSearchInput(''); }}
          className="flex items-center gap-2 cursor-pointer group"
        >
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#e50914] to-red-500 flex items-center justify-center shadow-lg shadow-red-900/40 group-hover:scale-105 transition-transform">
            <Film className="w-6 h-6 text-white" />
          </div>
          <span className="text-xl font-bold tracking-tight text-white group-hover:text-red-400 transition-colors">
            Cinema<span className="text-[#e50914]">Network</span>
          </span>
        </div>

        {/* Navigation Links */}
        <nav className="hidden md:flex items-center gap-1">
          <button
            onClick={() => setActiveTab('home')}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
              activeTab === 'home'
                ? 'text-white bg-gray-800/80 font-semibold'
                : 'text-gray-400 hover:text-white hover:bg-gray-800/40'
            }`}
          >
            박스오피스
          </button>
          <button
            onClick={() => setActiveTab('movies')}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
              activeTab === 'movies'
                ? 'text-white bg-gray-800/80 font-semibold'
                : 'text-gray-400 hover:text-white hover:bg-gray-800/40'
            }`}
          >
            영화 탐색
          </button>
          <button
            onClick={() => setActiveTab('community')}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
              activeTab === 'community'
                ? 'text-white bg-gray-800/80 font-semibold'
                : 'text-gray-400 hover:text-white hover:bg-gray-800/40'
            }`}
          >
            커뮤니티
          </button>
          <button
            onClick={() => setActiveTab('inquiry')}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
              activeTab === 'inquiry'
                ? 'text-white bg-gray-800/80 font-semibold'
                : 'text-gray-400 hover:text-white hover:bg-gray-800/40'
            }`}
          >
            고객센터 (1:1문의)
          </button>
        </nav>

        {/* Search Bar */}
        <form onSubmit={handleSearchSubmit} className="relative flex-1 max-w-xs">
          <input
            type="text"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            placeholder="영화 제목, 감독 검색..."
            className="w-full bg-gray-900/90 text-sm text-gray-200 pl-9 pr-4 py-2 rounded-full border border-gray-700 focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500 placeholder-gray-500 transition-all"
          />
          <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
        </form>

        {/* User Actions */}
        <div className="flex items-center gap-3">
          {currentUser ? (
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-2 pl-2 pr-3 py-1 bg-gray-800/60 rounded-full border border-gray-700/60">
                <img
                  src={currentUser.profileImageUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100'}
                  alt={currentUser.nickname}
                  className="w-7 h-7 rounded-full object-cover border border-gray-600"
                />
                <span className="text-sm font-medium text-gray-200">
                  {currentUser.nickname}
                </span>
                {currentUser.role === 'ADMIN' && (
                  <span className="px-1.5 py-0.5 text-[10px] font-semibold bg-red-600 text-white rounded">
                    ADMIN
                  </span>
                )}
              </div>
              <button
                onClick={onLogout}
                title="로그아웃"
                className="p-2 text-gray-400 hover:text-red-400 hover:bg-gray-800/50 rounded-full transition-colors"
              >
                <LogOut className="w-4 h-4" />
              </button>
            </div>
          ) : (
            <div className="flex items-center gap-2">
              <button
                onClick={() => onOpenAuth('login')}
                className="text-sm font-medium text-gray-300 hover:text-white px-3 py-1.5 rounded-lg hover:bg-gray-800/50 transition-colors"
              >
                로그인
              </button>
              <button
                onClick={() => onOpenAuth('signup')}
                className="text-sm font-medium bg-[#e50914] hover:bg-red-700 text-white px-3.5 py-1.5 rounded-lg shadow-md transition-colors"
              >
                회원가입
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
