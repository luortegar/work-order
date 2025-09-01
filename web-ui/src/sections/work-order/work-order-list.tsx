import React, { useState, useEffect, SyntheticEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button, Tabs, Tab, Box } from '@mui/material';
import { list, viewPDF } from 'src/api/workOrderApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { Iconify } from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import TabContext from '@mui/lab/TabContext';


export default function WorkOrderList() {
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const [tabValue, setTabValue] = useState<'draft' | 'sent'>('draft');
  const [dataList, setDataList] = useState<any[]>([]);
  const [rowCount, setRowCount] = useState(0);
  const [value, setValue] = React.useState('1');


  const columns: GridColDef[] = [
    {
      hideable: false,
      field: 'workOrderId',
      headerName: 'Work order id',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={params.value}
          color="default"
          variant="outlined"
          size="small"
          onClick={() => navigate(`/home/work-order/${params.row.workOrderId}`)}
        />
      ),
    },
    {
      hideable: false,
      field: 'workOrderId_',
      headerName: 'WO PDF',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label="pdf"
          color="default"
          variant="outlined"
          size="small"
          onClick={() => viewPDF(params?.row?.workOrderId)}
        />
      ),
    },
    { field: 'workOrderNumber', headerName: 'Work order number', width: 200, flex: 0.5 },
    {
      field: 'startTime',
      headerName: 'Start time',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => params.value || 'N/A',
    },
    {
      field: 'endTime',
      headerName: 'End time',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => params.value || 'N/A',
    },
    {
      field: 'creationDate',
      headerName: 'Creation date',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => params.value || 'N/A',
    },
    {
      field: 'updateDate',
      headerName: 'Update date',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => params.value || 'N/A',
    },
  ];

  useEffect(() => {
    refreshData(5, 0, '', tabValue);
  }, [tabValue]);

  const refreshData = (rowsPerPage: number, page: number, sort: string, filterName: string) => {
    list(rowsPerPage, page, sort, filterName)
      .then((r) => {
        setDataList(r.data.content);
        setRowCount(r.data.totalElements);
      })
      .catch(() => {
        showMessage('Error loading work orders', 'error');
      });
  };

  function handleChange(event: SyntheticEvent<Element, Event>, value: any): void {
    console.log('Function not implemented.')
    //throw new Error('Function not implemented.');
  }

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
        <Typography variant="h4">Work order</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="mingcute:add-line" />}
          onClick={() => navigate(`/home/work-order/new`)}
        >
          New work order
        </Button>
      </Stack>

      {/* Tabs para filtrar */}
      <TabContext value={value}>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
          <TabList onChange={handleChange} aria-label="lab API tabs example">
            <Tab label="Item One" value="1" />
            <Tab label="Item Two" value="2" />
            <Tab label="Item Three" value="3" />
          </TabList>
        </Box>
        <TabPanel value="1">

        </TabPanel>
        <TabPanel value="2"></TabPanel>
        <TabPanel value="3"></TabPanel>
      </TabContext>

      {/* Tabla */}
      <SimpleTable
        columns={columns}
        refreshData={refreshData}
        dataList={dataList}
        rowCount={rowCount}
        getRowId={(row) => row.workOrderId}
        pageSizeDefault={0}
      />
    </Container>
  );
}
