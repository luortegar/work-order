import { useState, useCallback, useEffect } from 'react';

import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import InputAdornment from '@mui/material/InputAdornment';

import { useRouter } from 'src/routes/hooks';

import { Iconify } from 'src/components/iconify';
import { sigIn } from 'src/api/userApi';
import { useAuth } from '../../context/auth/AuthContext';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { AuthenticateRequest } from 'src/api/request/AuthenticateRequest';
import { AxiosResponse } from 'axios';
import { AuthenticateResponse, TenantDetails } from 'src/api/response/authenticate';
import {jwtDecode} from "jwt-decode";


// ----------------------------------------------------------------------

export function SignInView() {
  const router = useRouter();
  const { setToken, setRefreshToken, setCurrentTenantId, setTenantDetailsList, setUserId } = useAuth();
  const { showMessage } = useSnackbar();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({ email: false, password: false });

  const [showPassword, setShowPassword] = useState(false);

  const getDefaultTenantId = (tenants: TenantDetails[]) => {
    let defaultTenant = tenants.find((tenant) => tenant.isDefault);
    if (!defaultTenant) defaultTenant = tenants.find((tenant) => tenant.tenantId !== null);
    return defaultTenant ? defaultTenant.tenantId : null;
  };

  const handleClick = () => {
    const newErrors = {
      email: !email.trim(),
      password: !password.trim(),
    };
    setErrors(newErrors);

    if (newErrors.email || newErrors.password) return;

    const authenticateRequest: AuthenticateRequest = { email, password };

    sigIn(authenticateRequest)
      .then((response: AxiosResponse<AuthenticateResponse, any>) => {
        setToken(response.data.token);
        const decoded = jwtDecode<MyToken>(response.data.token);
        console.log("decoded___", decoded)
        setUserId(decoded.jti || "123");
        setRefreshToken(response.data.refreshToken);
        setTenantDetailsList(response.data.tenantDetailsList);
        setCurrentTenantId(getDefaultTenantId(response.data.tenantDetailsList));
        router.push('/home');
      })
      .catch((error) => {
        console.log(error);
        const errorMessage = error?.response?.data?.message
        showMessage(errorMessage?errorMessage: "Unknown error!", "error")
      })
  }

  useEffect(() => {
    const token = sessionStorage.getItem('token');
    if (token) router.push('/home');
  }, [router])

  const renderForm = (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'flex-end',
        flexDirection: 'column',
      }}
    >
    <TextField
      fullWidth
      name="email"
      label="Email address"
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      error={errors.email}
      helperText={errors.email && 'Email is required'}
      sx={{ mb: 3 }}
      slotProps={{
        inputLabel: { shrink: true },
      }}
    />

      <Link variant="body2" color="inherit" sx={{ mb: 1.5 }}  href="/forgot-password">
        Forgot password?
      </Link>

      <TextField
        fullWidth
        name="password"
        label="Password"
        type={showPassword ? 'text' : 'password'}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        error={errors.password}
        helperText={errors.password && 'Password is required'}
        slotProps={{
          inputLabel: { shrink: true },
          input: {
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                  <Iconify icon={showPassword ? 'solar:eye-bold' : 'solar:eye-closed-bold'} />
                </IconButton>
              </InputAdornment>
            ),
          },
        }}
        sx={{ mb: 3 }}
      />

      <Button
        fullWidth
        size="large"
        type="submit"
        color="inherit"
        variant="contained"
        onClick={handleClick}
      >
        Sign in
      </Button>
    </Box>
  );

  return (
    <>
      <Box
        sx={{
          gap: 1.5,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          mb: 5,
        }}
      >
        <Typography variant="h5">Sign in</Typography>

        {/*
        <Typography
          variant="body2"
          sx={{
            color: 'text.secondary',
          }}
        >
          Don’t have an account?
          <Link variant="subtitle2" sx={{ ml: 0.5 }}>
            Get started
          </Link>
        </Typography>
        */}
      </Box>
      {renderForm}
    </>
  );
}
