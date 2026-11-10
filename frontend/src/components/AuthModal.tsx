import React, { useState } from 'react';
import { X, Film, Sparkles } from 'lucide-react';
import { apiClient } from '../api/client';
import { User } from '../types';

interface AuthModalProps {
  initialMode: 'login' | 'signup';
  onClose: () => void;
  onSuccess: (user: User, token: string) => void;
}

export const AuthModal: React.FC<AuthModalProps> = ({
  initialMode,
  onClose,
  onSuccess,
}) => {
  const [mode, setMode] = useState<'login' | 'signup'>(initialMode);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg('');
    setLoading(true);

    try {
      if (mode === 'signup') {
        const res = await apiClient.post('/auth/signup', {
          email,
          password,
          nickname,
        });
        if (res.data?.success) {
          alert('회원가입이 완료되었습니다! 로그인해주세요.');
          setMode('login');
          setLoading(false);
          return;
        }
      } else {
        const res = await apiClient.post('/auth/login', {
          email,
          password,
        });
        if (res.data?.success) {
          const { user, accessToken } = res.data.data;
          onSuccess(user, accessToken);
          onClose();
        }
      }
    } catch (err: any) {
      setErrorMsg(err.response?.data?.message || '인증 처리에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleDemoLogin = async (demoEmail: string, demoPw: string) => {
    setErrorMsg('');
    setLoading(true);
    try {
      const res = await apiClient.post('/auth/login', {
        email: demoEmail,
        password: demoPw,
      });
      if (res.data?.success) {
        const { user, accessToken } = res.data.data;
        onSuccess(user, accessToken);
        onClose();
      }
    } catch (err: any) {
      setErrorMsg(err.response?.data?.message || '데모 로그인 실패');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
      <div className="relative w-full max-w-md bg-[#14151b] border border-gray-800 rounded-2xl p-6 sm:p-8 shadow-2xl">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1.5 rounded-full text-gray-400 hover:text-white hover:bg-gray-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Logo and Title */}
        <div className="text-center mb-6">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#e50914] to-red-500 flex items-center justify-center mx-auto mb-2 shadow-lg shadow-red-900/40">
            <Film className="w-6 h-6 text-white" />
          </div>
          <h3 className="text-xl font-bold text-white">
            Cinema<span className="text-[#e50914]">Network</span>
          </h3>
          <p className="text-xs text-gray-400 mt-1">
            {mode === 'login' ? '로그인하고 영화 리뷰를 작성해보세요.' : '신규 회원가입'}
          </p>
        </div>

        {/* Mode Switch Tabs */}
        <div className="grid grid-cols-2 bg-gray-900 p-1 rounded-xl mb-4 border border-gray-800">
          <button
            type="button"
            onClick={() => { setMode('login'); setErrorMsg(''); }}
            className={`py-1.5 text-xs font-semibold rounded-lg transition-colors ${
              mode === 'login' ? 'bg-red-600 text-white shadow' : 'text-gray-400 hover:text-white'
            }`}
          >
            로그인
          </button>
          <button
            type="button"
            onClick={() => { setMode('signup'); setErrorMsg(''); }}
            className={`py-1.5 text-xs font-semibold rounded-lg transition-colors ${
              mode === 'signup' ? 'bg-red-600 text-white shadow' : 'text-gray-400 hover:text-white'
            }`}
          >
            회원가입
          </button>
        </div>

        {errorMsg && (
          <div className="p-3 mb-4 text-xs font-medium text-red-300 bg-red-950/60 border border-red-800/80 rounded-lg">
            {errorMsg}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-3.5">
          <div>
            <label className="block text-xs font-medium text-gray-300 mb-1">이메일</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="example@cinemanetwork.com"
              required
              className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500"
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-gray-300 mb-1">비밀번호</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="6자 이상 비밀번호"
              required
              className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500"
            />
          </div>

          {mode === 'signup' && (
            <div>
              <label className="block text-xs font-medium text-gray-300 mb-1">닉네임</label>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                placeholder="2~20자 사이 닉네임"
                required
                className="w-full bg-gray-900 border border-gray-700 text-xs rounded-lg p-2.5 text-gray-200 focus:outline-none focus:border-red-500"
              />
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-[#e50914] hover:bg-red-700 disabled:opacity-50 text-white text-xs font-semibold py-2.5 rounded-lg shadow-md transition-colors mt-2"
          >
            {loading ? '처리 중...' : mode === 'login' ? '로그인' : '회원가입 완료'}
          </button>
        </form>

        {/* Demo Fast Logins */}
        <div className="mt-5 pt-4 border-t border-gray-800">
          <p className="text-[11px] text-gray-400 font-semibold mb-2 flex items-center gap-1 justify-center">
            <Sparkles className="w-3.5 h-3.5 text-yellow-400" />
            원클릭 데모 계정 체험
          </p>
          <div className="grid grid-cols-2 gap-2">
            <button
              type="button"
              onClick={() => handleDemoLogin('user@cinemanetwork.com', 'user1234')}
              className="text-[11px] bg-gray-800/80 hover:bg-gray-700 text-gray-300 py-1.5 px-2 rounded-lg border border-gray-700 transition-colors"
            >
              일반 유저 로그인
            </button>
            <button
              type="button"
              onClick={() => handleDemoLogin('admin@cinemanetwork.com', 'admin1234')}
              className="text-[11px] bg-red-950/40 hover:bg-red-900/40 text-red-300 py-1.5 px-2 rounded-lg border border-red-800/60 transition-colors"
            >
              관리자 로그인
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
