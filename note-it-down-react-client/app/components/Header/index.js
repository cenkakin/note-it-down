import React, { memo } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import ExitIcon from '@material-ui/icons/ExitToApp';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { api } from '../../utils/request';
import { logOut } from '../../containers/App/actions';

const StyledContainer = styled.div`
  flex-grow: 1;
`;

const StyledTitle = styled(Typography)`
  && {
    flex-grow: 1;
    padding-left: ${props => props.theme.spacing(5)}px;
  }
`;

export function MenuAppBar({ title, onLogout }) {
  return (
    <StyledContainer>
      <AppBar position="static" color="secondary">
        <Toolbar>
          <StyledTitle variant="h6">{title || 'Home'}</StyledTitle>
          <div>
            <IconButton aria-label="log-out" onClick={onLogout} color="inherit">
              <ExitIcon />
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
    </StyledContainer>
  );
}

MenuAppBar.propTypes = {
  title: PropTypes.string,
  onLogout: PropTypes.func,
};

const mapStateToProps = createStructuredSelector({});

export function mapDispatchToProps(dispatch) {
  return {
    onLogout: () => {
      api.removeToken();
      dispatch(logOut());
    },
  };
}

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(MenuAppBar);
