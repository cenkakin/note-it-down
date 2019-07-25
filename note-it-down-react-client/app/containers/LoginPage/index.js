/* eslint-disable react/prop-types */
import React, { memo } from 'react';
import { connect } from 'react-redux';
import { Helmet } from 'react-helmet';
import { FormattedMessage, injectIntl } from 'react-intl';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import TextField from '@material-ui/core/TextField';
import { compose } from 'redux';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';

import { Formik } from 'formik';
import * as Yup from 'yup';
import { useSnackbar } from 'notistack';
import { createStructuredSelector } from 'reselect';
import messages from './messages';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import StyledButton from '../../components/Button/StyledButton';
import Form from './Form';
import { api } from '../../utils/request';
import { successfulLogin } from '../App/actions';

const LoginSchema = Yup.object().shape({
  email: Yup.string()
    .email('Email is invalid')
    .required('Email is required'),
  password: Yup.string()
    .min(6, 'Password must be at least 6 characters')
    .required('Password is required'),
});

const initialValues = { email: '', password: '' };

export function LoginPage({ onLoggedIn, intl }) {
  const { enqueueSnackbar } = useSnackbar();

  const onSubmit = (fields, { resetForm, setSubmitting }) => {
    api
      .post('auth/login', {
        username: fields.email,
        password: fields.password,
      })
      .then(res => {
        setSubmitting(false);
        if (res.ok) {
          resetForm(initialValues);
          const token = res.headers.authorization;
          const user = {
            email: fields.email,
            token,
          };
          api.setToken(token);
          onLoggedIn(user);
        } else {
          enqueueSnackbar(intl.formatMessage(messages.invalidCredentials), {
            variant: 'error',
            autoHideDuration: 4000,
          });
        }
      });
  };

  return (
    <article>
      <Helmet>
        <title>Login Page</title>
        <meta name="Sign In Page" />
      </Helmet>
      <Container component="main" maxWidth="xs">
        <Paper>
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography component="h1" variant="h5">
            <FormattedMessage {...messages.header} />
          </Typography>
          <Formik
            initialValues={initialValues}
            onSubmit={onSubmit}
            validationSchema={LoginSchema}
            render={props => {
              const {
                values,
                touched,
                errors,
                isSubmitting,
                handleChange,
                handleBlur,
                handleSubmit,
              } = props;

              const isError = {
                email: errors.email && touched.email,
                password: errors.password && touched.password,
              };

              return (
                <Form onSubmit={handleSubmit}>
                  <TextField
                    error={isError.email}
                    helperText={isError.email && errors.email}
                    value={values.email}
                    variant="outlined"
                    fullWidth
                    id="email"
                    label="Email Address"
                    name="email"
                    autoComplete="email"
                    onChange={handleChange}
                    onBlur={handleBlur}
                  />
                  <TextField
                    error={isError.password}
                    helperText={isError.password && errors.password}
                    value={values.password}
                    variant="outlined"
                    margin="normal"
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    autoComplete="current-password"
                    onChange={handleChange}
                    onBlur={handleBlur}
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
                    disabled={isSubmitting}
                  >
                    <FormattedMessage {...messages.loginButton} />
                  </StyledButton>
                  <Grid container>
                    <Grid item xs>
                      <Link href="#" variant="body2">
                        <FormattedMessage {...messages.forgotPassword} />
                      </Link>
                    </Grid>
                    <Grid item>
                      <Link href="/sign-up" variant="body2">
                        <FormattedMessage {...messages.forwardSignUp} />
                      </Link>
                    </Grid>
                  </Grid>
                </Form>
              );
            }}
          />
        </Paper>
      </Container>
    </article>
  );
}

LoginPage.propTypes = {
  intl: PropTypes.object,
  onLoggedIn: PropTypes.func,
};

const mapStateToProps = createStructuredSelector({});

function mapDispatchToProps(dispatch) {
  return {
    onLoggedIn: user => dispatch(successfulLogin(user)),
  };
}

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(injectIntl(LoginPage));
