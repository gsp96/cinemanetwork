export interface User {
  id: number;
  email: string;
  nickname: string;
  profileImageUrl: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}

export interface Movie {
  id: number;
  kobisMovieCd?: string;
  tmdbId?: number;
  title: string;
  originalTitle?: string;
  overview?: string;
  releaseDate?: string;
  posterPath?: string;
  backdropPath?: string;
  genres?: string;
  director?: string;
  actors?: string;
  runtime?: number;
  ratingAvg: number;
  reviewCount: number;
}

export interface BoxOffice {
  id: number;
  rank: number;
  targetDate: string;
  audiCnt: number;
  audiAcc: number;
  movie: Movie;
}

export interface Review {
  id: number;
  movieId: number;
  movieTitle: string;
  rating: number;
  content: string;
  likesCount: number;
  isLiked?: boolean;
  user: User;
  createdAt: string;
  updatedAt: string;
}

export interface Comment {
  id: number;
  postId: number;
  content: string;
  user: User;
  createdAt: string;
}

export type PostCategory = 'MOVIE_TALK' | 'FREE' | 'NOTICE' | 'INQUIRY';

export interface Post {
  id: number;
  category: PostCategory;
  title: string;
  content?: string;
  viewCount: number;
  commentCount: number;
  isSecret: boolean;
  user: User;
  comments?: Comment[];
  createdAt: string;
  updatedAt?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  code: string;
  message: string;
  data: T;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
