import React, { memo } from 'react';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import { Helmet } from 'react-helmet';
import { FormattedMessage, injectIntl } from 'react-intl';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import TextField from '@material-ui/core/TextField';
import { compose } from 'redux';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import Form from './Form';
import StyledButton from '../../components/Button/StyledButton';
import messages from './messages';

const key = 'signUp';

export function SignUpPage() {
  //  useInjectReducer({ key, reducer });
  //  useInjectSaga({ key, saga });

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
          <Form>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  name="password"
                  label="Confirm"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                />
              </Grid>
            </Grid>
            <StyledButton
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
            >
              <FormattedMessage {...messages.signUpButton} />
            </StyledButton>
            <Grid container justify="flex-end">
              <Grid item>
                <Link href="#" variant="body2">
                  <FormattedMessage {...messages.forwardLogin} />
                </Link>
              </Grid>
            </Grid>
          </Form>
        </Paper>
      </Container>
    </article>
  );
}

SignUpPage.propTypes = {
  intl: PropTypes.object,
  email: PropTypes.string,
  password: PropTypes.string,
};

export function mapDispatchToProps(dispatch) {
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
