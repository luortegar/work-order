import React, {
  createContext,
  useState,
  ReactNode,
  useMemo,
  useEffect,
  useCallback
} from 'react';
import { UserResponse } from 'src/api/response/userResponse';
import { viewMyUser } from 'src/api/userCrudApi';

type UserContextType = {
  user: UserResponse | null;
  setUser: React.Dispatch<React.SetStateAction<UserResponse | null>>;
  resetUser: () => void;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserResponse | null>(null);

  const resetUser = useCallback(() => {
    viewMyUser().then((resp) => {
      setUser(resp.data);
    });
  }, []);

  useEffect(() => {
    resetUser();
  }, [resetUser]);

  const value = useMemo(
    () => ({ user, setUser, resetUser }),
    [user, setUser, resetUser]
  );

  return (
    <UserContext.Provider value={value}>
      {children}
    </UserContext.Provider>
  );
};

export { UserProvider, UserContext };
