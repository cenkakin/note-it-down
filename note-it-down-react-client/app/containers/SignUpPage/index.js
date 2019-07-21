/* eslint-disable react/prop-types */
import React, { memo } from 'react';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import { Helmet } from 'react-helmet';
import { FormattedMessage, injectIntl } from 'react-intl';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import TextField from '@material-ui/core/TextField';
import { compose } from 'redux';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import * as Yup from 'yup';
import { Formik } from 'formik';
import { useSnackbar } from 'notistack';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import Form from './Form';
import StyledButton from '../../components/Button/StyledButton';
import messages from './messages';
import { apiCall } from '../../utils/request';

const SignUpSchema = Yup.object().shape({
  email: Yup.string()
    .email('Email is invalid')
    .required('Email is required'),
  password: Yup.string()
    .min(6, 'Password must be at least 6 characters')
    .required('Password is required'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password'), null], 'Passwords must match')
    .required('Confirm Password is required'),
});

const initialValues = { email: '', password: '', confirmPassword: '' };

export function SignUpPage() {
  const { enqueueSnackbar } = useSnackbar();

  const onSubmit = (fields, { resetForm, setSubmitting }) => {
    apiCall
      .post('auth/users', {
        email: fields.email,
        password: fields.password,
      })
      .then(res => {
        setSubmitting(false);
        if (res.ok) {
          resetForm(initialValues);
        } else {
          enqueueSnackbar(res.data.message || 'Error Happened!', {
            variant: 'error',
            autoHideDuration: 4000,
          });
        }
      });
  };

  return (
    <article>
      <Helmet>
        <title>Sign Up</title>
        <meta name="Sign Up Page" />
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
            validationSchema={SignUpSchema}
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
                confirmPassword:
                  errors.confirmPassword && touched.confirmPassword,
              };

              return (
                <Form onSubmit={handleSubmit}>
                  <Grid container spacing={2}>
                    <Grid item xs={12}>
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
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        error={isError.password}
                        helperText={isError.password && errors.password}
                        value={values.password}
                        variant="outlined"
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        onChange={handleChange}
                        onBlur={handleBlur}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        error={isError.confirmPassword}
                        helperText={
                          isError.confirmPassword && errors.confirmPassword
                        }
                        value={values.confirmPassword}
                        variant="outlined"
                        fullWidth
                        name="confirmPassword"
                        label="Confirm"
                        type="password"
                        id="confirmPassword"
                        onChange={handleChange}
                        onBlur={handleBlur}
                      />
                    </Grid>
                  </Grid>
                  <StyledButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    disabled={isSubmitting}
                  >
                    <FormattedMessage {...messages.signUpButton} />
                  </StyledButton>
                  <Grid container justify="flex-end">
                    <Grid item>
                      <Link href="/login" variant="body2">
                        <FormattedMessage {...messages.forwardLogin} />
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

export function mapDispatchToProps() {
  return {};
}

const mapStateToProps = createStructuredSelector({});

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(injectIntl(SignUpPage));
