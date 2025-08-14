// SnackbarContext.tsx
import React, { createContext, useContext, useState, ReactNode, useMemo } from 'react';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert, { AlertProps } from '@mui/material/Alert';
import { grey, green, amber, red } from '@mui/material/colors';
import { ThemeProvider, createTheme, styled } from '@mui/material/styles';

type SnackbarContextType = {
  showMessage: (message: string, severity?: AlertProps['severity']) => void;
};

const SnackbarContext = createContext<SnackbarContextType | undefined>(undefined);

export const useSnackbar = () => {
  const context = useContext(SnackbarContext);
  if (context === undefined) {
    throw new Error('useSnackbar must be used within a SnackbarProvider');
  }
  return context;
};

// Se utiliza styled para poder aplicar estilos fácilmente
const StyledAlert = styled(MuiAlert)({});

const SnackbarProvider = ({ children }: { children: ReactNode }) => {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState<AlertProps['severity']>('info');

  const showMessage = (msg: string, sev: AlertProps['severity'] = 'info') => {
    setMessage(msg);
    setSeverity(sev);
    setOpen(true);
  };

  const handleClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpen(false);
  };

  const value = useMemo(() => ({
    showMessage
  }), []);

  // Definición del tema con paleta de colores pastel
  const pastelTheme = createTheme({
    palette: {
      success: {
        main: green['300'],
        contrastText: grey['900'],
      },
      info: {
        main: grey['300'],
        contrastText: grey['900'],
      },
      warning: {
        main: amber['300'],
        contrastText: grey['900'],
      },
      error: {
        main: red['300'],
        contrastText: grey['900'],
      },
    },
    components: {
      MuiAlert: {
        styleOverrides: {
          filledSuccess: ({ theme }) => ({
            backgroundColor: theme.palette.success.main,
            color: theme.palette.success.contrastText,
          }),
          filledInfo: ({ theme }) => ({
            backgroundColor: theme.palette.info.main,
            color: theme.palette.info.contrastText,
          }),
          filledWarning: ({ theme }) => ({
            backgroundColor: theme.palette.warning.main,
            color: theme.palette.warning.contrastText,
          }),
          filledError: ({ theme }) => ({
            backgroundColor: theme.palette.error.main,
            color: theme.palette.error.contrastText,
          }),
        },
      },
    },
  });

  return (
    <ThemeProvider theme={pastelTheme}>
      <SnackbarContext.Provider value={value}>
        {children}
        <Snackbar
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
          open={open}
          autoHideDuration={4000}
          onClose={handleClose}
        >
          <StyledAlert
            onClose={handleClose}
            severity={severity}
            elevation={6}
            variant="filled"
          >
            {message}
          </StyledAlert>
        </Snackbar>
      </SnackbarContext.Provider>
    </ThemeProvider>
  );
};

export default SnackbarProvider;