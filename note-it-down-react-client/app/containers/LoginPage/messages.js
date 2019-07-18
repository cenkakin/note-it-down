import { defineMessages } from 'react-intl';

export const scope = 'noteitdown.containers.LoginPage';

export default defineMessages({
  header: {
    id: `${scope}.forgot_password.header`,
    defaultMessage: 'Login',
  },
  forgotPassword: {
    id: `${scope}.forgot_password.message`,
    defaultMessage: 'Forgot Password?',
  },
  forwardSignUp: {
    id: `${scope}.forward_sign_up.message`,
    defaultMessage: "Don't have an account? Sign Up",
  },
  rememberMe: {
    id: `${scope}.remember_me.message`,
    defaultMessage: 'Remember me',
  },
  loginButton: {
    id: `${scope}.sign_in_button.message`,
    defaultMessage: 'Log In',
  },
  invalidCredentials: {
    id: `${scope}.invalid_credentials`,
    defaultMessage: 'Invalid credentials!',
  },
});
