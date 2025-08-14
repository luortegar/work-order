import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button } from '@mui/material';
import { list } from 'src/api/clientApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { Iconify } from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';

export default function ClientList() {
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const columns: GridColDef[] = [
    {
        hideable: false,
        field: 'clientId',
        headerName: 'Client ID',
        width: 200,
        flex: 0.5,
        renderCell: (params: GridRenderCellParams) => (
          <Chip
            label={params.value}
            color='default'
            variant='outlined'
            size='small'
            onClick={() => navigate(`/home/client/${params.row.clientId}`)}
          />
        ),
    },
    {
        hideable: false,
        field: 'branch',
        headerName: 'Branch',
        width: 150,
        flex: 0.4,
        sortable:false,
        renderCell: (params: GridRenderCellParams) => (
          <Chip
            label={'Branch'}
            color='default'
            variant='outlined'
            size='small'
            onClick={() => navigate(`/home/client/${params.row.clientId}/branch`)}
          />
        ),
    },
    {
      hideable: false,
      field: 'employees',
      headerName: 'Employees',
      width: 150,
      flex: 0.4,
      sortable:false,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={'Employees'}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => navigate(`/home/client/${params.row.clientId}/employees`)}
        />
      ),
  },
    { field: 'companyName', headerName: 'Company Name', width: 200, flex: 0.5 },
    { field: 'uniqueTaxpayerIdentification', headerName: 'Tax ID', width: 200, flex: 0.5 },
    { field: 'business', headerName: 'Business', width: 200, flex: 0.5 },
    { field: 'address', headerName: 'Address', width: 200, flex: 0.5 },
    { field: 'commune', headerName: 'Commune', width: 200, flex: 0.5 },
    { field: 'city', headerName: 'City', width: 200, flex: 0.5 },
    { field: 'typeOfPurchase', headerName: 'Type of Purchase', width: 300, flex: 1 },
  ];

  const [dataList, setDataList] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  useEffect(() => {
    refreshData(5, 0, '', '');
  }, []);

  const refreshData = (rowsPerPage:number, page:number, sort:string, filterName:string) => {
    list(rowsPerPage, page, sort, filterName).then((r) => {
      setDataList(r.data.content);
      setRowCount(r.data.totalElements);
      console.log(r);
    });
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        
        <Typography variant="h4">Client</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="eva:trending-down-fill" />}
          onClick={() => navigate(`/home/client/new`)}
        >
          New client
        </Button>
      </Stack>
      <SimpleTable columns={columns} refreshData={refreshData} dataList={dataList} rowCount={rowCount} getRowId={(row) => row.clientId} pageSizeDefault={0} />
    </Container>
  );
}
