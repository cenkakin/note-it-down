import React, { useEffect } from 'react';
import Snackbar from '@material-ui/core/Snackbar';
import PropTypes from 'prop-types';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import styled from 'styled-components';

const StyledSnackbarContent = styled(SnackbarContent)`
  && {
    background-color: green;
    margin: ${props => props.theme.spacing(1)}px;
  }
`;

const StyledMessage = styled.span`
  && {
    display: flex;
    align-items: center;
  }
`;

const StyledCloseIcon = styled(CloseIcon)`
  && {
    font-size: 20;
  }
`;

const StyledCheckIcon = styled(CheckCircleIcon)`
  && {
    opacity: 0.9;
    font-size: 20;
    margin-right: ${props => props.theme.spacing(1)}px;
  }
`;

function MySnackbarContentWrapper(props) {
  const { message, onClose } = props;

  return (
    <StyledSnackbarContent
      aria-describedby="client-snackbar"
      message={
        <StyledMessage>
          <StyledCheckIcon />
          {message}
        </StyledMessage>
      }
      action={[
        <IconButton
          key="close"
          aria-label="Close"
          color="inherit"
          onClick={onClose}
        >
          <StyledCloseIcon />
        </IconButton>,
      ]}
    />
  );
}

MySnackbarContentWrapper.propTypes = {
  onClose: PropTypes.func,
  message: PropTypes.string,
};

function SuccessMessage({ isOpen, message, ...others }) {
  const [open, setOpen] = React.useState(false);

  useEffect(() => {
    // When initial state username is not null, submit the form to load repos
    setOpen(true);
  }, [isOpen]);

  function handleClose(event, reason) {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
  }

  return (
    <Snackbar
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={open}
      autoHideDuration={113000}
      onClose={handleClose}
      {...others}
    >
      <MySnackbarContentWrapper
        onClose={handleClose}
        variant="success"
        message={message || 'Successful Operation!'}
      />
    </Snackbar>
  );
}

SuccessMessage.propTypes = {
  isOpen: PropTypes.bool,
  message: PropTypes.string,
};

export default SuccessMessage;
