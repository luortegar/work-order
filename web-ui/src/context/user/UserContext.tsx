import React, { createContext, useState, ReactNode, useMemo, useEffect } from 'react';
import { UserResponse } from 'src/api/response/userResponse';
import { viewMyUser } from 'src/api/userCrudApi';

type UserContextType = {
  user: UserResponse | null | undefined;
  setUser: React.Dispatch<React.SetStateAction<UserResponse | null | undefined>>;
  resetUser: () => void;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserResponse | null>()


  const resetUser = () =>{
    viewMyUser().then((resp)=>{
      setUser(resp.data)
    })
  }

  useEffect(() => {
    resetUser()
  }, []);

  const value = useMemo(
    () => ({ user, setUser, resetUser}),
    [user]
  );

  return (
    
    <UserContext.Provider value={value}>
      {children}
    </UserContext.Provider>
  );
};



export { UserProvider, UserContext };