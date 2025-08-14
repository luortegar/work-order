import React, { createContext, useState, useEffect, ReactNode, useContext, useMemo } from 'react';

interface AuthContextType {
  token: string | null;
  setToken: (token: string | null) => void;
  refreshToken: string | null;
  setRefreshToken: (token: string | null) => void;
  currentTenantId: string | null;
  setCurrentTenantId: (currentTenantId: string | null) => void;
  tenantDetailsList: any[];
  setTenantDetailsList: (tenantDetailsList: any[]) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(() => sessionStorage.getItem('token'));
  const [refreshToken, setRefreshToken] = useState<any | null>(() => sessionStorage.getItem('refreshToken'));
  const [currentTenantId, setCurrentTenantId] = useState<any | null>(() => sessionStorage.getItem('currentTenantId'));
  const [tenantDetailsList, setTenantDetailsList] = useState<any[]>(() => {
    const savedPrivileges = sessionStorage.getItem('tenantDetailsList');
    return savedPrivileges ? JSON.parse(savedPrivileges) : [];
  });


  useEffect(() => {
    sessionStorage.setItem('token', token ?? '');
  }, [token]);

  useEffect(() => {
    sessionStorage.setItem('refreshToken', refreshToken ?? '');
  }, [refreshToken]);

  useEffect(() => {
    sessionStorage.setItem('currentTenantId', currentTenantId ?? '');
  }, [currentTenantId]);

  useEffect(() => {
    sessionStorage.setItem('tenantDetailsList', JSON.stringify(tenantDetailsList));
  }, [tenantDetailsList]);

  const value = useMemo(
    () => ({ token, setToken, refreshToken, setRefreshToken, currentTenantId, setCurrentTenantId, tenantDetailsList, setTenantDetailsList }),
    [token, refreshToken,currentTenantId, tenantDetailsList]
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export { AuthProvider, useAuth };