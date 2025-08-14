import { CONFIG } from 'src/config-global';

import { ForgotPasswordView } from 'src/sections/auth/forgot-password';

// ----------------------------------------------------------------------

export default function Page() {
  return (
    <>
      <title>{`Sign in - ${CONFIG.appName}`}</title>

      <ForgotPasswordView />
    </>
  );
}
