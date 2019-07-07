import React, { memo, useEffect } from 'react';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import { Helmet } from 'react-helmet';
import { FormattedMessage } from 'react-intl';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import TextField from '@material-ui/core/TextField';
import { useInjectReducer } from 'utils/injectReducer';
import { useInjectSaga } from 'utils/injectSaga';
import { compose } from 'redux';
import { Redirect } from 'react-router-dom';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Form from '../../components/Form';

import { makeSelectEmail, makeSelectLoginError, makeSelectPassword } from './selectors';
import { changeEmail, changePassword, login } from './actions';
import messages from './messages';
import { makeSelectLoggedIn } from '../App/selectors';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import StyledButton from '../../components/Button/StyledButton';
import reducer from './reducer';
import saga from './saga';
import SuccessMessage from '../../components/SuccessMessage';

const key = 'login';

export function LoginPage({
  email,
  password,
  onSubmitForm,
  onChangeEmail,
  onChangePassword,
  loginError,
  loggedIn,
}) {
  useInjectReducer({ key, reducer });
  useInjectSaga({ key, saga });

  useEffect(() => {
    // When initial state username is not null, submit the form to load repos
    if (loginError != null && loginError !== '') {
      // message.error(intl.formatMessage(loginError));
    }
  }, [loggedIn]);

  // const { formatMessage } = intl;

  function handleSubmit(e) {
    e.preventDefault();
    onSubmitForm();
  }

  return (
    <article>
      <SuccessMessage isOpen />
      <Helmet>
        <title>Sign In Page</title>
        <meta name="Sign In Page" />
      </Helmet>
      <Container component="main" maxWidth="xs">
        {loggedIn ? <Redirect to="/" /> : null}
        <Paper>
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography component="h1" variant="h5">
            <FormattedMessage {...messages.header} />
          </Typography>
          <Form onSubmit={handleSubmit}>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              onChange={onChangeEmail}
              value={email}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={onChangePassword}
              value={password}
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label=<FormattedMessage {...messages.rememberMe} />
            />
            <StyledButton
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
            >
              <FormattedMessage {...messages.loginButton} />
            </StyledButton>
            <Grid container>
              <Grid item xs>
                {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                <Link href="#" variant="body2">
                  <FormattedMessage {...messages.forgotPassword} />
                </Link>
              </Grid>
              <Grid item>
                <Link href="/signup" variant="body2">
                  <FormattedMessage {...messages.forwardSignUp} />
                </Link>
              </Grid>
            </Grid>
          </Form>
        </Paper>
      </Container>
    </article>
  );
}

LoginPage.propTypes = {
  intl: PropTypes.object,
  form: PropTypes.object,
  onSubmitForm: PropTypes.func,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  loginError: PropTypes.any,
  loggedIn: PropTypes.bool,
  email: PropTypes.string,
  password: PropTypes.string,
};

export function mapDispatchToProps(dispatch) {
  return {
    onChangeEmail: e => dispatch(changeEmail(e.target.value)),
    onChangePassword: e => dispatch(changePassword(e.target.value)),
    onSubmitForm: () => {
      dispatch(login());
    },
  };
}

const mapStateToProps = createStructuredSelector({
  loggedIn: makeSelectLoggedIn(),
  email: makeSelectEmail(),
  password: makeSelectPassword(),
  loginError: makeSelectLoginError(),
});

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(LoginPage);
