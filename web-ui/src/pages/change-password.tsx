import { CONFIG } from 'src/config-global';

import { ChangePasswordView } from 'src/sections/auth/change-password';

export default function Page() {
  return (
    <>
      <title>{`Sign in - ${CONFIG.appName}`}</title>

      <ChangePasswordView />
    </>
  );
}
