/*
 * Sign In Page
 *
 */
import React, { memo } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import makeStyles from '@material-ui/core/styles/makeStyles';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { FormattedMessage } from 'react-intl';
import Button from '../../components/Button';
import messages from './messages';
import Paper from './Paper';
import StyledAvatar from './StyledAvatar';
import Form from '../../components/Form';

const key = 'signin';

const useStyles = makeStyles(theme => ({
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export function SignInPage({ username, onSubmitForm, onChangeUsername }) {
  const classes = useStyles();

  return (
    <article>
      <Helmet>
        <title>Sign In Page</title>
        <meta name="Sign In Page"/>
      </Helmet>
      <Container component="main" maxWidth="xs">
        <Paper>
          <StyledAvatar >
            <LockOutlinedIcon/>
          </StyledAvatar>
          <Typography component="h1" variant="h5">
            <FormattedMessage {...messages.header} />
          </Typography>
          <Form noValidate>
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
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary"/>}
              label=<FormattedMessage {...messages.rememberMe} />
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
            >
              <FormattedMessage {...messages.signInButton} />
            </Button>
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
  onSubmitForm: PropTypes.func,
  username: PropTypes.string,
  onChangeUsername: PropTypes.func,
};

const withConnect = connect();

export default compose(
  withConnect,
  memo,
)(SignInPage);
