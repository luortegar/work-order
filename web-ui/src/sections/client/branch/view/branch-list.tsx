import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button } from '@mui/material';
import { listBranch } from 'src/api/branchApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { Iconify } from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';

export default function BranchList() {
  const { clientId } = useParams();

  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const columns: GridColDef[] = [
    {
        hideable: false,
        field: 'branchId',
        headerName: 'Branch ID',
        width: 200,
        flex: 0.5,
        renderCell: (params: GridRenderCellParams) => (
          <Chip
            label={params.value}
            color='default'
            variant='outlined'
            size='small'
            onClick={() => navigate(`/home/client/${clientId}/branch/${params.row.branchId}`)}
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
          onClick={() => navigate(`/home/client/${clientId}/branch/${params.row.branchId}/employees`)}
          />
      ),
    },
    {
      hideable: false,
      field: 'equipments',
      headerName: 'Equipment',
      width: 150,
      flex: 0.4,
      sortable:false,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={'Equipment'}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => navigate(`/home/client/${clientId}/branch/${params.row.branchId}/equipment`)}
          />
      ),
  },
    { field: 'branchName', headerName: 'Branch name', width: 200, flex: 0.5 },
    { field: 'address', headerName: 'Address', width: 200, flex: 0.5 },
    { field: 'commune', headerName: 'Commune', width: 200, flex: 0.5 },
    { field: 'region', headerName: 'Region', width: 200, flex: 0.5 },
  ];

  const [dataList, setDataList] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  useEffect(() => {
    refreshData(5, 0, '', '');
  }, []);

  const refreshData = (rowsPerPage:number, page:number, sort:string, filterName:string) => {
    if(clientId){
      listBranch(clientId, rowsPerPage, page, sort, filterName).then((r) => {
        setDataList(r.data.content);
        setRowCount(r.data.totalElements);
        console.log(r);
      });
    }
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">Branch</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify width={24} icon="mingcute:add-line" sx={{ '--color': 'white' }} />}
          onClick={() => navigate(`/home/client/${clientId}/branch/new`)}
        >
          New branch
        </Button>
      </Stack>
      <SimpleTable columns={columns} refreshData={refreshData} dataList={dataList} rowCount={rowCount} getRowId={(row) => row.branchId} pageSizeDefault={0} />
    </Container>
  );
}
