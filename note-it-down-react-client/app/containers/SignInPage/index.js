/*
 * Sign In Page
 *
 */
import React, { memo } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { FormattedMessage } from 'react-intl';
import messages from './messages';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import Form from '../../components/Form';
import StyledButton from '../../components/Button/StyledButton';
import { loadRepos } from '../App/actions';
import { createStructuredSelector } from 'reselect';
import { makeSelectEmail, makeSelectPassword } from './selectors';
import { changeEmail, changePassword } from './actions';
import { useInjectReducer } from '../../../internals/templates/utils/injectReducer';
import { useInjectSaga } from '../../utils/injectSaga';
import reducer from './reducer';

const key = 'signin';

export function SignInPage({
  email,
  password,
  onSubmitForm,
  onChangeEmail,
  onChangePassword,
}) {
  useInjectReducer({ key, reducer });

  return (
    <article>
      <Helmet>
        <title>Sign In Page</title>
        <meta name="Sign In Page"/>
      </Helmet>
      <Container component="main" maxWidth="xs">
        <Paper>
          <StyledAvatar>
            <LockOutlinedIcon/>
          </StyledAvatar>
          <Typography component="h1" variant="h5">
            <FormattedMessage {...messages.header} />
          </Typography>
          <Form onSubmit={onSubmitForm}>
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
              control={<Checkbox value="remember" color="primary"/>}
              label=<FormattedMessage {...messages.rememberMe} />
            />
            <StyledButton
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
            >
              <FormattedMessage {...messages.signInButton} />
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

SignInPage.propTypes = {
  email: PropTypes.string,
  password: PropTypes.string,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  onSubmitForm: PropTypes.func,
};

export function mapDispatchToProps(dispatch) {
  return {
    onChangeEmail: evt => dispatch(changeEmail(evt.target.value)),
    onChangePassword: evt => dispatch(changePassword(evt.target.value)),
    onSubmitForm: evt => {
      if (evt !== undefined && evt.preventDefault) evt.preventDefault();
      dispatch(loadRepos());
    },
  };
}

const mapStateToProps = createStructuredSelector({
  email: makeSelectEmail(),
  password: makeSelectPassword(),
});

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(SignInPage);
