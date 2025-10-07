import { CONFIG } from 'src/config-global';

import { lazy, Suspense } from 'react';
import { useRoutes } from 'react-router-dom';

const List = lazy(() => import('../sections/client/view/client-list'));
const View = lazy(() => import('../sections/client/view/client-view'));
const BranchList = lazy(() => import('../sections/client/branch/view/branch-list'));
const BranchView = lazy(() => import('../sections/client/branch/view/branch-view'));

const EmployeesList = lazy(() => import('../sections/client/employees/view/employees-list'));
const EmployeesView = lazy(() => import('../sections/client/employees/view/employees-view'));

const EquipmentList = lazy(() => import('../sections/client/equipment/view/equipment-list'));
const EquipmentView = lazy(() => import('../sections/client/equipment/view/equipment-view'));

const BranchEmployeesList = lazy(() => import('../sections/client/branch-employees/view/employees-list'));
const BranchEmployeesView = lazy(() => import('../sections/client/branch-employees/view/employees-view'));

const InspectionVisitList = lazy(() => import('../sections/client/inspectionVisit/view/inspectionVisit-list'));
const InspectionVisitView = lazy(() => import('../sections/client/inspectionVisit/view/inspectionVisit-view'));

// ----------------------------------------------------------------------

export default function WorkOrderPage() {
  const routes = useRoutes([
    { path: '/:clientId', element: <Suspense fallback={<div>Loading...</div>}><View /></Suspense> },
    { path: '/:clientId/branch', element: <Suspense fallback={<div>Loading...</div>}><BranchList /></Suspense> },
    { path: '/:clientId/branch/:branchId', element: <Suspense fallback={<div>Loading...</div>}><BranchView /></Suspense> },

    { path: '/:clientId/employees', element: <Suspense fallback={<div>Loading...</div>}><EmployeesList /></Suspense> },
    { path: '/:clientId/employees/:userId', element: <Suspense fallback={<div>Loading...</div>}><EmployeesView /></Suspense> },

    { path: '/:clientId/branch/:branchId/equipment', element: <Suspense fallback={<div>Loading...</div>}><EquipmentList /></Suspense> },
    { path: '/:clientId/branch/:branchId/equipment/:equipmentId', element: <Suspense fallback={<div>Loading...</div>}><EquipmentView /></Suspense> },

    { path: '/:clientId/branch/:branchId/employees', element: <Suspense fallback={<div>Loading...</div>}><BranchEmployeesList /></Suspense> },
    { path: '/:clientId/branch/:branchId/employees/:userId', element: <Suspense fallback={<div>Loading...</div>}><BranchEmployeesView /></Suspense> },

    { path: '/:clientId/branch/:branchId/inspectionVisit', element: <Suspense fallback={<div>Loading...</div>}><InspectionVisitList /></Suspense> },
    { path: '/:clientId/branch/:branchId/inspectionVisit/:inspectionVisitId', element: <Suspense fallback={<div>Loading...</div>}><InspectionVisitView /></Suspense> },


    { path: '', element: <Suspense fallback={<div>Loading...</div>}><List /></Suspense> },
  ]);

  return (
    <>
      <title>{`Dashboard - ${CONFIG.appName}`}</title>
      <meta
        name="description"
        content="The starting point for your next project with Minimal UI Kit, built on the newest version of Material-UI ©, ready to be customized to your style"
      />
      <meta name="keywords" content="react,material,kit,application,dashboard,admin,template" />
      {routes}
    </>
  );
}
