import { lazy, Suspense } from 'react';
import { useRoutes } from 'react-router-dom';
import { CONFIG } from 'src/config-global';


// ----------------------------------------------------------------------
const UserList = lazy(() => import('src/sections/user/view/user-list'));
const UserView = lazy(() => import('src/sections/user/view/user-view'));

// ----------------------------------------------------------------------

export default function RolePage() {
  const routes = useRoutes([
    { path: '/:id', element: <Suspense fallback={<div>Loading...</div>}><UserView /></Suspense> },
    { path: '', element: <Suspense fallback={<div>Loading...</div>}><UserList /></Suspense> },
  ]);

  return (
    <>
      <title>{`Work order - ${CONFIG.appName}`}</title>
      <meta
        name="description"
        content="The starting point for your next project with Minimal UI Kit, built on the newest version of Material-UI ©, ready to be customized to your style"
      />
      <meta name="keywords" content="react,material,kit,application,dashboard,admin,template" />
      {routes}
    </>
  );
}
