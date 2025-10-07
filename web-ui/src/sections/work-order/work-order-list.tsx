import React, { useState, useEffect, SyntheticEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import {
  Container,
  Chip,
  Button,
  Box,
  Paper,
  Typography,
  Stack,
} from '@mui/material';
import { create, list, viewPDF } from 'src/api/workOrderApi';
import { Iconify } from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import TabList from '@mui/lab/TabList';
import TabContext from '@mui/lab/TabContext';
import Tab from '@mui/material/Tab';
import SimpleTable from 'src/components/table/simple-table';

export default function WorkOrderList() {
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const [workOrderStatus, setWorkOrderStatus] = useState<'DRAFT' | 'SENT'>('DRAFT');
  const [dataList, setDataList] = useState<any[]>([]);
  const [rowCount, setRowCount] = useState(0);
  const [value, setValue] = useState('1');
  const [filterName, setFilterName] = useState('');

  const columns: GridColDef[] = [
    {
      field: 'workOrderId',
      headerName: 'Work order id',
      flex: 0.3,
      renderCell: (params: GridRenderCellParams) => {
        const id: string = params.value;
        const shortId = id ? '...' + id.slice(-8) : ''; // últimos 8 caracteres

        return (
          <Chip
            label={shortId}
            variant="outlined"
            size="small"
            onClick={() => navigate(`/home/work-order/${params.row.workOrderId}`)}
          />
        );
      },
    },
    {
      field: 'workOrderId_',
      headerName: 'WO PDF',
      flex: 0.3,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label="PDF"
          variant="outlined"
          size="small"
          color="primary"
          onClick={() => viewPDF(params?.row?.workOrderId)}
        />
      ),
    },
    { field: 'workOrderNumber', headerName: 'Work order number', flex: 0.4 },
    { field: 'startTime', headerName: 'Start time', flex: 0.5 },
    { field: 'endTime', headerName: 'End time', flex: 0.5 },
    { field: 'creationDate', headerName: 'Creation date', flex: 0.5 },
    { field: 'updateDate', headerName: 'Update date', flex: 0.5 },
  ];

  useEffect(() => {
    refreshData(5, 0, '', filterName);
  }, [workOrderStatus, filterName]);

  const refreshData = (rowsPerPage: number, page: number, sort: string, filter: string) => {
    list(rowsPerPage, page, sort, filter, workOrderStatus)
      .then((r) => {
        setDataList(r.data.content);
        setRowCount(r.data.totalElements);
      })
      .catch(() => {
        showMessage('Error loading work orders', 'error');
      });
  };

  const handleChange = (event: SyntheticEvent, newValue: string) => {
    setValue(newValue);
    setWorkOrderStatus(newValue === '1' ? 'DRAFT' : 'SENT');
  };

  function createNewOrder() {
    create({}).then(resp=>{
      const workOrderId = resp?.data?.workOrderId;
      console.log("resp", resp.data.workOrderId);
      navigate(`/home/work-order/${workOrderId}?edit=on`);
    })
  }

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
        <Typography variant="h4" fontWeight={700}>
          Work Orders
        </Typography>
        <Button
          variant="contained"
          color='inherit'
          startIcon={<Iconify icon="mingcute:add-line" />}
          onClick={()=>createNewOrder()}
        >
          New Work Order
        </Button>
      </Stack>

      {/* Tabs */}
      <Paper elevation={2} sx={{ borderRadius: 3, mb: 3 }}>
        <TabContext value={value}>
          <Box sx={{ borderBottom: 1, borderColor: 'divider', px: 2 }}>
            <TabList
              onChange={handleChange}
              aria-label="work order tabs"
              textColor="primary"
              indicatorColor="primary"
            >
              <Tab label="Draft" value="1" />
              <Tab label="Sent" value="2" />
            </TabList>
          </Box>
          <SimpleTable
            columns={columns}
            refreshData={refreshData}
            dataList={dataList}
            rowCount={rowCount}
            getRowId={(row) => row.workOrderId}
            pageSizeDefault={5}
          />
        </TabContext>
      </Paper>
    </Container>
  );
}
