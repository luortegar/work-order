import { lazy, Suspense } from 'react';
import { useRoutes } from 'react-router-dom';
import { CONFIG } from 'src/config-global';


const List = lazy(() => import('../sections/work-order/work-order-list'));
const View = lazy(() => import('../sections/work-order/work-order-view'));

// ----------------------------------------------------------------------

export default function WorkOrderPage() {
  const routes = useRoutes([
    { path: '/:id', element: <Suspense fallback={<div>Loading...</div>}><View /></Suspense> },
    { path: '/:id?edit=on', element: <Suspense fallback={<div>Loading...</div>}><View /></Suspense> },
    { path: '/', element: <Suspense fallback={<div>Loading...</div>}><List /></Suspense> },
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
