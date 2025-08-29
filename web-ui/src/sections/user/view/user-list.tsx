import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button } from '@mui/material';
import { listUser } from 'src/api/userCrudApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import {Iconify} from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';

// Puedes eliminar la función convertArrayToDate ya que ya no es necesaria

export default function UserList() {
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  // Define columnas según el nuevo formato JSON
  const columns: GridColDef[] = [
    {
      hideable: false,
      field: 'userId',
      headerName: 'User ID',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={params.value}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => navigate(`/home/user/${params.row.userId}`)}
        />
      ),
    },
    { field: 'firstName', headerName: 'First Name', width: 200, flex: 0.5 },
    { field: 'lastName', headerName: 'Last Name', width: 200, flex: 0.5 },
    { field: 'email', headerName: 'Email', width: 250, flex: 0.5 },
  ];

  const [dataList, setDataList] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  useEffect(() => {
    refreshData(5, 0, '', '');
  }, []);
  const refreshData = (rowsPerPage:number, page:number, sort:string, filterName:string) => {
    listUser(rowsPerPage, page, sort, filterName).then((r) => {
      setDataList(r.data.content);
      setRowCount(r.data.totalElements);
      console.log(r);
    });
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">Users</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="mingcute:add-line" />}
          onClick={() => navigate(`/home/user/new`)}
        >
          New User
        </Button>
      </Stack>
      <SimpleTable columns={columns} refreshData={refreshData} dataList={dataList} rowCount={rowCount} getRowId={(row) => row.userId} pageSizeDefault={0}/>
    </Container>
  );
}
