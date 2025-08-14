import { CONFIG } from 'src/config-global';

import { WorkOrderView } from 'src/sections/work-order/view';

// ----------------------------------------------------------------------

export default function Page() {
  return (
    <>
      <title>{`Users - ${CONFIG.appName}`}</title>

      <WorkOrderView />
    </>
  );
}
