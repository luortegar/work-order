import React, { useState, useEffect } from 'react';
import { TextField, Autocomplete } from '@mui/material';
import { Controller, Control } from 'react-hook-form';

interface ControlledAutocompleteProps {
  name: string;
  control: Control<any>;
  options: any[];
  optionKey: string;
  labelKey: string;
  label: string;
  disabled?: boolean;
  fetchOptions?: (input: string) => void;
}

const ControlledAutocomplete: React.FC<ControlledAutocompleteProps> = ({
  name,
  control,
  options,
  optionKey,
  labelKey,
  label,
  disabled = false,
  fetchOptions,
  ...rest
}) => {
  const [inputValue, setInputValue] = useState('');
  
  // Este useEffect se encargará de sincronizar el inputValue
  // cuando el valor del formulario cambie.
  useEffect(() => {
    // Necesitamos obtener el valor actual del formulario aquí
    // para poder buscar la opción correspondiente
    const formValue = control._defaultValues[name];
    if (formValue) {
      const selectedOption = options.find((option) => option[optionKey] === formValue);
      if (selectedOption) {
        setInputValue(selectedOption[labelKey] || '');
      }
    }
  }, [control, name, options, optionKey, labelKey]);

  return (
    <Controller
      name={name}
      control={control}
      render={({ field }) => {
        const selectedOption = options.find((option) => option[optionKey] === field.value) || null;

        return (
          <Autocomplete
            {...field}
            {...rest}
            options={options}
            value={selectedOption}
            inputValue={inputValue}
            getOptionLabel={(option) => (typeof option === 'string' ? option : option[labelKey] || '')}
            isOptionEqualToValue={(option, value) => option[optionKey] === value[optionKey]}
            onChange={(event, newValue, reason) => {
              field.onChange(newValue ? newValue[optionKey] : null);
              if (newValue) {
                setInputValue(newValue[labelKey] || '');
              } else if (reason === 'clear') {
                setInputValue('');
              }
            }}
            onInputChange={(event, newInputValue, reason) => {
              if (reason === 'input') {
                setInputValue(newInputValue);
                if (fetchOptions) {
                  fetchOptions(newInputValue);
                }
              }
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label={label}
                variant="outlined"
                fullWidth
              />
            )}
            disabled={disabled}
          />
        );
      }}
    />
  );
};

export default ControlledAutocomplete;