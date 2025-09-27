import { createContext } from 'preact';
import { useContext } from 'preact/hooks';

type AuthContextType = {
  token: string | null;
  setToken: (t: string | null) => void;
};

export const AuthContext = createContext<AuthContextType>({
  token: null,
  setToken: () => {}
});

// Optional: custom hook for convenience
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};
