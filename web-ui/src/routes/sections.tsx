import type { RouteObject } from 'react-router';

import { lazy, Suspense } from 'react';
import { Outlet } from 'react-router-dom';
import { varAlpha } from 'minimal-shared/utils';

import Box from '@mui/material/Box';
import LinearProgress, { linearProgressClasses } from '@mui/material/LinearProgress';

import { AuthLayout } from 'src/layouts/auth';
import { DashboardLayout } from 'src/layouts/dashboard';

// ----------------------------------------------------------------------

export const DashboardPage = lazy(() => import('src/pages/dashboard'));
export const BlogPage = lazy(() => import('src/pages/blog'));
export const UserPage = lazy(() => import('src/pages/user'));
export const WorkOrderPage = lazy(() => import('src/pages/work-order'));
export const ClientPage = lazy(() => import('src/pages/client-page'));
export const ProfilePage = lazy(() => import('src/pages/profile'));


export const SignInPage = lazy(() => import('src/pages/sign-in'));
export const ForgotpasswordPage = lazy(() => import('src/pages/forgot-password'));

export const ProductsPage = lazy(() => import('src/pages/products'));
export const Page404 = lazy(() => import('src/pages/page-not-found'));

const renderFallback = () => (
  <Box
    sx={{
      display: 'flex',
      flex: '1 1 auto',
      alignItems: 'center',
      justifyContent: 'center',
    }}
  >
    <LinearProgress
      sx={{
        width: 1,
        maxWidth: 320,
        bgcolor: (theme) => varAlpha(theme.vars.palette.text.primaryChannel, 0.16),
        [`& .${linearProgressClasses.bar}`]: { bgcolor: 'text.primary' },
      }}
    />
  </Box>
);

export const routesSection: RouteObject[] = [
  {
    element: (
      <DashboardLayout>
        <Suspense fallback={renderFallback()}>
          <Outlet />
        </Suspense>
      </DashboardLayout>
    ),
    children: [
      { path: 'home', element: <DashboardPage /> },
      { path: 'home/work-order/*', element: <WorkOrderPage /> },
      { path: 'home/client/*', element: <ClientPage /> },
      { path: 'home/user/*', element: <UserPage /> },
      { path: 'home/profile', element: <ProfilePage /> },


      //{ path: 'user', element: <UserPage /> },
      { path: 'products', element: <ProductsPage /> },
      { path: 'blog', element: <BlogPage /> },
    ],
  },

  {
    element: (
       <AuthLayout>
        <Suspense fallback={renderFallback()}>
          <Outlet />
        </Suspense>
       </AuthLayout>
    ),
    children: [
      {index: true, element: <SignInPage /> },
      { path: 'sign-in', element: <SignInPage /> },
      { path: 'forgot-password', element: <ForgotpasswordPage /> },

    ],
  },

  {
    path: '404',
    element: <Page404 />,
  },
  { path: '*', element: <Page404 /> },
];
