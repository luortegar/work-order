
# How to Run the Project

## Configuration

Before running the project, update the configuration file with the desired local file storage path:

```properties
started-kit.file.local.path=<desired_path>
```  

Replace `<desired_path>` with the absolute path where you want to store local files.

## Build and Run

Follow these steps to build and start the application:

1. **Build the project:**

   ```sh
   gradle build
   ```  

2. **Run the application:**

   ```sh
   gradle bootRun
   ```  

Once the application starts successfully, it should be up and running! 🚀

## Default User

A default user is preloaded into the database via the `data.sql` file. You can use the following credentials to log in:

- **Email:** `test@email.com`
- **Password:** `asd12`

The corresponding SQL entry is:

```sql
INSERT INTO tbl_user (user_id, first_name, last_name, email, password)
VALUES ('7f000001-88ea-1791-8188-eab8dc090000', 'Test_0', 'Duper', 'test@email.com',
'$2a$10$rnyh.E9fcfW1mSI9tiQ6L.sFXD3WAh7znN1/WAl9uEqxzCOO72pIK');
```  

