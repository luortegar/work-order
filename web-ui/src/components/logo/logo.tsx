// src/components/logo/logo.jsx

import type { LinkProps } from '@mui/material/Link';

import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import { styled } from '@mui/material/styles';

import { RouterLink } from 'src/routes/components';

// ----------------------------------------------------------------------

// Importa solo el SVG que necesitas
import fullLogoSvg from 'src/assets/logo_ingeros_horizontal.svg';

// ----------------------------------------------------------------------

export type LogoProps = LinkProps & {
  disabled?: boolean;
};

export function Logo({
  sx,
  disabled,
  href = '/',
  ...other
}: LogoProps) {
  return (
    <LogoRoot
      component={RouterLink}
      href={href}
      aria-label="Logo"
      underline="none"
      sx={[
        {
          width: 200,
          height: 36,
          flexShrink: 0,
          pointerEvents: disabled ? 'none' : 'auto',
          display: 'inline-flex',
        },
        ...(Array.isArray(sx) ? sx : [sx]),
      ]}
      {...other}
    >
      <Box
        component="img"
        src={fullLogoSvg}
        alt="Logo"
        sx={{
          width: '100%',
          height: '100%',
          objectFit: 'contain',
        }}
      />
    </LogoRoot>
  );
}

// ----------------------------------------------------------------------

const LogoRoot = styled(Link)({
  verticalAlign: 'middle',
});