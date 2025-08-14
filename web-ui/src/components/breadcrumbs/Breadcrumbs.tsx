import React from 'react';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import { Breadcrumbs as MUIBreadcrumbs, Link, Typography, Tooltip } from '@mui/material';

const Breadcrumbs: React.FC = () => {
  const location = useLocation();
  const pathnames = location.pathname.split('/').filter((x) => x);

  const truncate = (str: string) => {
    return str.length > 10 ? `${str.slice(0, 10)}...` : str;
  };

  return (
    <MUIBreadcrumbs aria-label="breadcrumb" sx={{ padding: '16px' }}>
      {pathnames.map((value, index) => {
        const to = `/${pathnames.slice(0, index + 1).join('/')}`;
        const displayValue = truncate(value);

        return index === pathnames.length - 1 ? (
          <Tooltip title={value} key={to}>
            <Typography color="textPrimary">
              {displayValue}
            </Typography>
          </Tooltip>
        ) : (
          <Tooltip title={value} key={to}>
            <Link component={RouterLink} to={to} underline="hover" color="inherit">
              {displayValue}
            </Link>
          </Tooltip>
        );
      })}
    </MUIBreadcrumbs>
  );
};

export default Breadcrumbs;
