import { defineMessages } from 'react-intl';

export const scope = 'noteitdown.containers.LoginPage';

export default defineMessages({
  header: {
    id: `${scope}.forgot_password.header`,
    defaultMessage: 'Sign In',
  },
  forgotPassword: {
    id: `${scope}.forgot_password.message`,
    defaultMessage: 'Forgot Password?',
  },
  forwardSignUp: {
    id: `${scope}.forgot_password.message`,
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
});
