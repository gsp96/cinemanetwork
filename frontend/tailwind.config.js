/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        cinema: {
          bg: '#0f1015',
          card: '#181920',
          hover: '#22232c',
          primary: '#e50914', // 넷플릭스/시네마 레드
          gold: '#f5c518',    // IMDb 골드
          text: '#ffffff',
          muted: '#9ca3af'
        }
      }
    },
  },
  plugins: [],
}
